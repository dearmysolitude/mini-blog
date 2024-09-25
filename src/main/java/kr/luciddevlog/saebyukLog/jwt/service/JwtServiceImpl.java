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
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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

    // 토큰 생성에 사용할 String; key 값과 json에 넣을 bearer
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

    // 만든 토큰을 json으로 만들어 response로 반환함
    @Override
    public void sendToken(HttpServletResponse response, String accessToken, String refreshToken) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);

        setAccessTokenHeader(response, accessToken);
        setRefreshTokenHeader(response, refreshToken);

        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put(ACCESS_TOKEN_SUBJECT, accessToken);
        tokenMap.put(REFRESH_TOKEN_SUBJECT, refreshToken);

        String token = objectMapper.writeValueAsString(tokenMap);

        response.getWriter().write(token);
    }

    // 리퀘스트 헤더로부터 Access 토큰을 가져옴: 앞에 붙었을 bearer 제거
    @Override
    public String extractAccessToken(HttpServletRequest request) throws IOException, ServletException {
        return Optional.ofNullable(request.getHeader(accessHeader)).map(accessToken -> accessToken.replace(BEARER, "")).orElse(null);
    }

    @Override
    public String extractRefreshToken(HttpServletRequest request) throws IOException, ServletException {
        return Optional.ofNullable(request.getHeader(accessHeader)).map(refreshToken -> refreshToken.replace(BEARER, "")).orElse(null);
    }

    // JWT 라이브러리에서 secretKey로 HMAC512 방식으로 암호화된 accessToken에서 decode된 accessToken으로 받아 사용자 이름(username)을 String으로 받았다.
    @Override
    public String extractUsername(String accessToken) {
        return JWT.require(Algorithm.HMAC512(secretKey)).build().verify(accessToken).getClaim(USERNAME_CLAIM).asString();
    }

    @Override
    public void setAccessTokenHeader(HttpServletResponse response, String accessToken) {
        response.setHeader(accessHeader, accessToken);
    }

    @Override
    public void setRefreshTokenHeader(HttpServletResponse response, String refreshToken) {
        response.setHeader(refreshHeader, refreshToken);
    }

}
