package kr.luciddevlog.saebyukLog.JWT;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.persistence.EntityManager;
import kr.luciddevlog.saebyukLog.jwt.service.JwtService;
import kr.luciddevlog.saebyukLog.user.entity.UserItem;
import kr.luciddevlog.saebyukLog.user.entity.UserRole;
import kr.luciddevlog.saebyukLog.user.entity.UserItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@Transactional
@SpringBootTest
class JwtTest {

    private static final Logger log = LoggerFactory.getLogger(JwtTest.class);
    @Autowired
    JwtService jwtService;
    @Autowired
    UserItemRepository userItemRepository;
    @Autowired
    EntityManager em;

    @Value("${jwt.secretKey}")
    private String secretKey;
    @Value("${jwt.access.header}")
    private String accessHeader;
    @Value("${jwt.refresh.header}")
    private String refreshHeader;

    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String USERNAME_CLAIM = "username";
    private static final String BEARER = "Bearer ";

    private String username = "username";

    @BeforeEach
    public void init() {
        UserItem userItem = UserItem.builder()
                .username(username)
                .password("1234567890")
                .name("Member1")
                .role(UserRole.ROLE_USER)
                .build();
        log.debug("Saving UserItem: {}", userItem);
        userItemRepository.save(userItem);
        clear();
    }

    private void clear() {
        em.flush();
        em.clear();
    }

    private DecodedJWT getVerify(String token) {
        return JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token);
    }

    @Test
    @Transactional
    void createAccessToken_AccessToken_발급() throws Exception {
        //given, when
        String accessToken = jwtService.createAccessToken(username);

        DecodedJWT verify = getVerify(accessToken);

        String subject = verify.getSubject();
        String findUsername = verify.getClaim(USERNAME_CLAIM).asString();

        //then
        assertThat(findUsername).isEqualTo(username);
        assertThat(subject).isEqualTo(ACCESS_TOKEN_SUBJECT);
    }

    @Test
    void createRefreshToken_RefreshToken_발급() throws Exception {
        // given, when
        String refreshToken = jwtService.createRefreshToken();
        DecodedJWT decodedJWT = getVerify(refreshToken);
        String subject = decodedJWT.getSubject();
        String username = decodedJWT.getClaim(USERNAME_CLAIM).asString();

        // then
        assertThat(subject).isEqualTo(REFRESH_TOKEN_SUBJECT);
        assertThat(username).isNull(); // refresh token은 username없이 생성됨.
    }

    @Test
    void updateRefreshToken_RefreshToken_업데이트() throws Exception {
        // given
        String refreshToken = jwtService.createRefreshToken();
        jwtService.updateRefreshToken(username, refreshToken);
        clear();
        Thread.sleep(3000);

        // when
        String reIssuedRefershToken = jwtService.createRefreshToken();
        jwtService.updateRefreshToken(username, reIssuedRefershToken);
        clear();

        // then
        assertThat(Exception.class, () -> userItemRepository.findByRefreshToken(refreshToken).get());
        assertThat(userItemRepository.findByRefreshToken(reIssuedRefershToken).get)
    }
}
