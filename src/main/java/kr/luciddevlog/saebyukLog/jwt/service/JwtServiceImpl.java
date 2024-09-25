package kr.luciddevlog.saebyukLog.jwt.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.luciddevlog.saebyukLog.user.exception.UserNotFoundException;
import kr.luciddevlog.saebyukLog.user.repository.UserItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.Date;

public class JwtServiceImpl implements JwtService{

    // 환경 변수 주입
    @Value("${jwt.secretKey}")
    private String secretKey;
    @Value("${jwt.access.expiration}")
    private Long accessTokenExpirationPeriod;
    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenExpirationPeriod;
    @Value("${jwt.access.header}")
    private String accessHeader;
    @Value("${jwt.refresh.header}")
    private String refreshHeader;

    // 토큰 생성에 사용할 String들
    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String USERNAME_CLAIM = "username";
    private static final String BEARER = "Bearer ";

    private final UserItemRepository userItemRepository;
    private final ObjectMapper objectMapper; // 객체 직렬화

    @Autowired
    public JwtServiceImpl(UserItemRepository userItemRepository, ObjectMapper objectMapper) {
        this.userItemRepository = userItemRepository;
        this.objectMapper = objectMapper;
    }

    // 토큰 관리 로직
    @Override
    public String createAccessToken(String username) {
        return JWT.create()
                .withSubject(ACCESS_TOKEN_SUBJECT)
                .withExpiresAt(new Date(System.currentTimeMillis() + accessTokenExpirationPeriod))
                .withClaim(USERNAME_CLAIM, username)
                .sign(Algorithm.HMAC512(secretKey));
    }

    @Override
    public String createRefreshToken() {
        return JWT.create()
                .withSubject(REFRESH_TOKEN_SUBJECT)
                .withExpiresAt(new Date(System.currentTimeMillis() + refreshTokenExpirationPeriod))
                .sign(Algorithm.HMAC512(secretKey));
    }

    @Override
    public void updateRefreshToken(String username, String refreshToken) {
        userItemRepository.findByUsername(username)
                .ifPresentOrElse(
                        userItem -> userItem.updateRefreshToken(refreshToken),
                        () -> new UserNotFoundException("존재하지 않는 회원")
                );
    }

    @Override
    public void destroyRefreshToken(String username) {
        userItemRepository.findByUsername(username)
                .ifPresentOrElse(
                        userItem -> userItem.destroyRefreshToken(),
                        () -> new UserNotFoundException("존재하지 않는 회원")
                );
    }

    //
    @Override
    public void sendToken(HttpServletResponse response, String accessToken, String refreshToken) throws IOException {


    }

    @Override
    public String extractAccessToken(HttpServletRequest request) throws IOException, ServletException {

        return "";
    }

    @Override
    public String extractRefreshToken(HttpServletRequest request) throws IOException, ServletException {

        return "";
    }

    @Override
    public String extractUsername(String accessToken) {

        return "";
    }

    public void setAccessTokenHeader(HttpServletResponse response, String accessToken) {


    }
    public void setRefreshTokenHeader(HttpServletResponse response, String refreshToken) {

    }

}
