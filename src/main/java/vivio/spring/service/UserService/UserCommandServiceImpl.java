package vivio.spring.service.UserService;

import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import vivio.spring.converter.UserConverter;
import vivio.spring.domain.User;
import vivio.spring.repository.UserRepository;
import vivio.spring.util.RedisUtil;
import vivio.spring.web.dto.UserRequestDTO;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.Duration;
import java.util.Date;
import java.util.Optional;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
@org.springframework.transaction.annotation.Transactional(readOnly = true)
public class UserCommandServiceImpl implements UserCommandService{

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;
    private final RedisUtil redisUtil;

    private int authNumber;

    @Override
    @Transactional
    public User joinUser(UserRequestDTO.JoinDto request){
        log.info(request.getName());
        User newUser= UserConverter.toUser(request);
        newUser.setPassword(String.valueOf(passwordEncoder.encode(newUser.getPassword())));
        log.info(newUser.getPassword());
        return userRepository.save(newUser);
    }

    @Override
    @Transactional
    public String LoginUser(UserRequestDTO.LoginDTO request){
        Optional<User> userOptional=userRepository.findByEmail(request.getEmail());
        try {
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                log.info(user.getPassword());
                if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                    Date now = new Date();

                    return Jwts.builder()
                            .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                            .setIssuer("vivio")
                            .setIssuedAt(now)
                            .setExpiration(new Date(now.getTime() + Duration.ofMinutes(30).toMillis()))
                            .claim("id", user.getId())
                            .claim("email", user.getEmail())
                            .signWith(SignatureAlgorithm.HS256, "secretKeysecretKeysecretKeysecretKeysecretKeysecretKeysecretKeysecretKeysecretKeysecretKey")
                            .compact();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            log.info(e.getMessage());
            return null;
        }
        return null;
    }
    @Override
    public void makeRandomNumber(){
        Random r = new Random();
        String randomNumber = "";
        for(int i=0; i<6; i++){
            randomNumber += Integer.toString(r.nextInt(10));

        }

        authNumber = Integer.parseInt(randomNumber);
    }
    @Override
    public String joinEmail(String email){
        makeRandomNumber();
        String setFrom = "viviosever@gmail.com";
        String toMail = email;
        String title = "[ViViO] 회원가입 인증 이메일입니다.";
        String content =
                "저희 서비스에 가입해주셔서 감사합니다." +
                        "<br><br>" +
                        "인증번호는 " + authNumber +"입니다."+
                        "<br>" +
                        "인증번호를 제대로 입력해주세요. 감사합니다!";
        mailSend(setFrom, toMail,title,content);
        return Integer.toString(authNumber);
    }
    @Override
    public void mailSend(String setFrom, String toMail, String title, String content) {
        MimeMessage message = mailSender.createMimeMessage();//JavaMailSender 객체를 사용하여 MimeMessage 객체를 생성
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");//이메일 메시지와 관련된 설정을 수행합니다.
            // true를 전달하여 multipart 형식의 메시지를 지원하고, "utf-8"을 전달하여 문자 인코딩을 설정
            helper.setFrom(setFrom);//이메일의 발신자 주소 설정
            helper.setTo(toMail);//이메일의 수신자 주소 설정
            helper.setSubject(title);//이메일의 제목을 설정
            helper.setText(content, true);//이메일의 내용 설정 두 번째 매개 변수에 true를 설정하여 html 설정으로한다.
            mailSender.send(message);
        } catch (MessagingException e) {//이메일 서버에 연결할 수 없거나, 잘못된 이메일 주소를 사용하거나, 인증 오류가 발생하는 등 오류
            // 이러한 경우 MessagingException이 발생
            e.printStackTrace();//e.printStackTrace()는 예외를 기본 오류 스트림에 출력하는 메서드
        }
        redisUtil.setDataExpire(Integer.toString(authNumber),toMail,60*5L);


    }
    @Override
    public boolean CheckAuthNum(String email, String authNum){
        if(redisUtil.getData(authNum)==null){
            return false;
        }
        else if(redisUtil.getData(authNum).equals(email)){
            return true;
        }else{
            return false;
        }
    }


}
