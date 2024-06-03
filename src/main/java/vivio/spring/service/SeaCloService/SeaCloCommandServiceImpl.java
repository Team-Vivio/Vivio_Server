package vivio.spring.service.SeaCloService;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vivio.spring.converter.SeaCloConverter;
import vivio.spring.domain.ItemList;

import vivio.spring.repository.ItemListRepository;
import vivio.spring.web.dto.SeaCloReqeustDTO;
import vivio.spring.web.dto.SeaCloResponseDTO;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@org.springframework.transaction.annotation.Transactional(readOnly = true)
public class SeaCloCommandServiceImpl implements SeaCloCommandService {

    private static final String PROJECT_ID = "maptraveler";
    private static final String LOCATION = "us-east1"; // 수정 필요
    private static final String GCS_URI = "gs://vivi-o/musinsa.csv"; // 수정 필요
    private  final ItemListRepository itemListReposiotroy;
    @Override
    @Transactional()
    public  SeaCloResponseDTO.SeaCloListDTO createSeaClo(SeaCloReqeustDTO.createDTO request,MultipartFile file) throws IOException {
        //deleteProductSet("maptraveler","us-east1","product_set_id");
        //importProductsFromCsv();
        //getProductSet("maptraveler","us-east1","product_set_id");
        String type="";
        switch (request.getType()){
            case "top":
                type="top";
                break;
            case "bottom":
                type = "bottom";
                break;
        }
       return getSimilarProductsFile("maptraveler","us-east1","product_set_id_2","apparel-v2",file,"category="+type);
        //return null;
    }
    public static void getProductSet(String projectId, String computeRegion, String productSetId)
    throws IOException {
        GoogleCredentials credentials = GoogleCredentials.fromStream(new ClassPathResource("maptraveler-b8b198d2054d.json").getInputStream());
        ProductSearchSettings settings = ProductSearchSettings.newBuilder()
                .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                .build();
      try (ProductSearchClient client = ProductSearchClient.create(settings)) {

    // Get the full path of the product set.
    String formattedName = ProductSetName.format(projectId, computeRegion, productSetId);
    // List all the products available in the product set.
    for (Product product : client.listProductsInProductSet(formattedName).iterateAll()) {
      // Display the product information
      System.out.println(String.format("Product name: %s", product.getName()));
      System.out.println(
          String.format(
              "Product id: %s",
              product.getName().substring(product.getName().lastIndexOf('/') + 1)));
      System.out.println(String.format("Product display name: %s", product.getDisplayName()));
      System.out.println(String.format("Product description: %s", product.getDescription()));
      System.out.println(String.format("Product category: %s", product.getProductCategory()));
      System.out.println("Product labels: ");
      for (Product.KeyValue element : product.getProductLabelsList()) {
        System.out.println(String.format("%s: %s", element.getKey(), element.getValue()));
      }
    }
  }
}
    public void importProductsFromCsv() throws IOException {
        GoogleCredentials credentials = GoogleCredentials.fromStream(new ClassPathResource("maptraveler-b8b198d2054d.json").getInputStream());
        ProductSearchSettings settings = ProductSearchSettings.newBuilder()
                .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                .build();

        List<String> gcsUris = Arrays.asList(
                "gs://vivi-o/small_products_0.csv",
                "gs://vivi-o/small_products_1.csv",
                "gs://vivi-o/small_products_2.csv",
                "gs://vivi-o/small_products_3.csv",
                "gs://vivi-o/small_products_4.csv",
                "gs://vivi-o/small_products_5.csv",
                "gs://vivi-o/small_products_6.csv",
                "gs://vivi-o/small_products_7.csv"

        );

        try (ProductSearchClient client = ProductSearchClient.create(settings)) {
            String parent = LocationName.of(PROJECT_ID, LOCATION).toString();
            log.info(parent);

            for (String gcsUri : gcsUris) {
                ImportProductSetsGcsSource gcsSource = ImportProductSetsGcsSource.newBuilder()
                        .setCsvFileUri(gcsUri)
                        .build();
                ImportProductSetsInputConfig inputConfig = ImportProductSetsInputConfig.newBuilder()
                        .setGcsSource(gcsSource)
                        .build();
                ImportProductSetsRequest request = ImportProductSetsRequest.newBuilder()
                        .setParent(parent)
                        .setInputConfig(inputConfig)
                        .build();

                // Asynchronous request
                ImportProductSetsResponse response = client.importProductSetsAsync(request).get();

                log.info("Imported products from: " + gcsUri);
                response.getStatusesList().forEach(status -> {
                    if (status.getCode() == 0) {
                        log.info("Success: " + status.getMessage());
                    } else {
                        log.info("Error: " + status.getMessage());
                    }
                });
            }
        } catch (InterruptedException e) {
            log.info("Error importing product sets: " + e.getMessage());
            throw new IOException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
    public static File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
        // 임시 파일 생성
        File tempFile = File.createTempFile("upload_", "_temp", new File(System.getProperty("java.io.tmpdir")));
        tempFile.deleteOnExit(); // 애플리케이션 종료 시 파일 삭제 설정

        try (InputStream in = multipartFile.getInputStream();
             FileOutputStream out = new FileOutputStream(tempFile)) {
            // 입력 스트림에서 파일로 바이트를 쓴다
            int read;
            byte[] bytes = new byte[1024];
            while ((read = in.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
        }

        return tempFile; // 생성된 임시 파일 반환
    }
    public static void deleteProductSet(String projectId, String computeRegion, String productSetId)
            throws IOException {
        GoogleCredentials credentials = GoogleCredentials.fromStream(new ClassPathResource("maptraveler-b8b198d2054d.json").getInputStream());
        ProductSearchSettings settings = ProductSearchSettings.newBuilder()
                .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                .build();
        try (ProductSearchClient client = ProductSearchClient.create(settings)) {

            // Get the full path of the product set.
            String formattedName = ProductSetName.format(projectId, computeRegion, productSetId);
            // Delete the product set.
            client.deleteProductSet(formattedName);
            System.out.println(String.format("Product set deleted"));
        }catch(Exception e){
            throw new IOException(e);

        }
    }
    public SeaCloResponseDTO.SeaCloListDTO getSimilarProductsFile(
        String projectId,
        String computeRegion,
        String productSetId,
        String productCategory,
        MultipartFile img,
        String filter)
        throws IOException {
        GoogleCredentials credentials = GoogleCredentials.fromStream(new ClassPathResource("maptraveler-b8b198d2054d.json").getInputStream());
        ImageAnnotatorSettings imageAnnotatorSettings = ImageAnnotatorSettings.newBuilder().setCredentialsProvider(FixedCredentialsProvider.create(credentials)).build();
      try (ImageAnnotatorClient queryImageClient = ImageAnnotatorClient.create(imageAnnotatorSettings)) {

        // Get the full path of the product set.
        String productSetPath = ProductSetName.format(projectId, computeRegion, productSetId);
        File imgPath= convertMultipartFileToFile(img);
        // Read the image as a stream of bytes.

        byte[] content = Files.readAllBytes(imgPath.toPath());

        // Create annotate image request along with product search feature.
        Feature featuresElement = Feature.newBuilder().setType(Feature.Type.PRODUCT_SEARCH).build();
        // The input image can be a HTTPS link or Raw image bytes.
        // Example:
        // To use HTTP link replace with below code
        //  ImageSource source = ImageSource.newBuilder().setImageUri(imageUri).build();
        //  Image image = Image.newBuilder().setSource(source).build();
        Image image = Image.newBuilder().setContent(ByteString.copyFrom(content)).build();
        ImageContext imageContext =
            ImageContext.newBuilder()
                .setProductSearchParams(
                    ProductSearchParams.newBuilder()
                        .setProductSet(productSetPath)
                        .addProductCategories(productCategory)
                        .setFilter(filter)
                )
                .build();

        AnnotateImageRequest annotateImageRequest =
            AnnotateImageRequest.newBuilder()
                .addFeatures(featuresElement)
                .setImage(image)
                .setImageContext(imageContext)
                .build();
        List<AnnotateImageRequest> requests = Arrays.asList(annotateImageRequest);

        // Search products similar to the image.
        BatchAnnotateImagesResponse response = queryImageClient.batchAnnotateImages(requests);

        List<ProductSearchResults.Result> similarProducts =
            response.getResponses(0).getProductSearchResults().getResultsList();
          List<SeaCloResponseDTO.SeaCloItemDTO> items = similarProducts.stream()
                  .map(product -> {
                      try {
                          log.info("1");
                          String productIdPath = product.getProduct().getName();
                          String[] parts = productIdPath.split("/");
                          String itemId = parts[parts.length - 1];
                          Optional<ItemList> itemListOptional = itemListReposiotroy.findByTitle(itemId);

                          // 예외 처리: itemListOptional이 비어있는 경우 처리
                          if (!itemListOptional.isPresent()) {
                              return null;
                          }

                          ItemList itemList = itemListOptional.get();
                          return SeaCloConverter.toSeaCloItemDTO(itemList.getPrice(), itemId, itemList.getImage(), itemList.getUrl());
                      } catch (Exception e) {
                          // 예외 처리: 로그 출력
                          log.error("Error processing product: " + product.getProduct().getName(), e);
                          return null; // null을 반환하여 나중에 필터링
                      }
                  })
                  .filter(Objects::nonNull) // null이 아닌 항목만 필터링
                  .collect(Collectors.toList());
//        log.info("Similar Products: ");
//        for (ProductSearchResults.Result product : similarProducts) {
//            log.info(String.format("\nProduct name: %s", product.getProduct().getName()));
//            log.info(
//              String.format("Product display name: %s", product.getProduct().getDisplayName()));
//            log.info(
//              String.format("Product description: %s", product.getProduct().getDescription()));
//            log.info(String.format("Score(Confidence): %s", product.getScore()));
//            log.info(String.format("Image name: %s", product.getImage()));
//
//            String productIdPath = product.getProduct().getName();
//            String[] parts = productIdPath.split("/");
//            String itemId = parts[parts.length - 1];
//            log.info(String.format("Item ID: %s", itemId));
            return SeaCloConverter.toSeaCloListDTO(items);

      }
    }
}

