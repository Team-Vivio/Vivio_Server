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
import vivio.spring.web.dto.SeaCloResponseDTO;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@org.springframework.transaction.annotation.Transactional(readOnly = true)
public class SeaCloCommandServiceImpl implements SeaCloCommandService {

    private static final String PROJECT_ID = "maptraveler";
    private static final String LOCATION = "us-east1"; // 수정 필요
    private static final String GCS_URI = "gs://vivi-o/musinsa.csv"; // 수정 필요

    @Override
    @Transactional()
    public  SeaCloResponseDTO.SeaCloListDTO createSeaClo(MultipartFile file) throws IOException {
        //importProductsFromCsv();
        getSimilarProductsFile("maptraveler","us-east1","product_set_id","apparel-v2",file,"category=clothes");
        return null;
    }
    public void importProductsFromCsv() throws IOException {
        GoogleCredentials credentials = GoogleCredentials.fromStream(new ClassPathResource("maptraveler-b8b198d2054d.json").getInputStream());
        ProductSearchSettings settings = ProductSearchSettings.newBuilder()
        .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
        .build();
        try (ProductSearchClient client = ProductSearchClient.create(settings)) {
            String parent = LocationName.of(PROJECT_ID, LOCATION).toString();
            log.info(parent);
            ImportProductSetsGcsSource gcsSource = ImportProductSetsGcsSource.newBuilder()
                    .setCsvFileUri(GCS_URI)
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

            log.info("Imported products: ");
            response.getStatusesList().forEach(status -> {
                if (status.getCode() == 0) {
                    log.info("Success: " + status.getMessage());
                } else {
                    log.info("Error: " + status.getMessage());
                }
            });
        } catch (Exception e) {
             log.info("Error importing product sets: " + e.getMessage());
            throw new IOException(e);

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

    public static void getSimilarProductsFile(
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
        log.info("Similar Products: ");
        for (ProductSearchResults.Result product : similarProducts) {
          log.info(String.format("\nProduct name: %s", product.getProduct().getName()));
          log.info(
              String.format("Product display name: %s", product.getProduct().getDisplayName()));
          log.info(
              String.format("Product description: %s", product.getProduct().getDescription()));
          log.info(String.format("Score(Confidence): %s", product.getScore()));
          log.info(String.format("Image name: %s", product.getImage()));
        }
      }
    }
}

