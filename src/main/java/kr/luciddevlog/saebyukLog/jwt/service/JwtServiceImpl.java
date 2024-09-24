package kr.luciddevlog.saebyukLog.jwt.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.luciddevlog.saebyukLog.user.repository.UserItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class JwtServiceImpl implements JwtService{

    // 환경 변수 주입
    @Value("${jwt.secretKey}")
    private String secretKEy;
    @Value("${jwt.access.expiration")
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
    private final ObjectMapper objectMapper;

    @Autowired
    public JwtServiceImpl(UserItemRepository userItemRepository, ObjectMapper objectMapper) {
        this.userItemRepository = userItemRepository;
        this.objectMapper = objectMapper;
    }

//    @Override
//    public String createAccessToken(String username) {
//
//    }

}
