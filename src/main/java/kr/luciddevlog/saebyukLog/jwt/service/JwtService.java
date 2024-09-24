package kr.luciddevlog.saebyukLog.jwt.service;

import com.auth0.jwt.JWT;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;

@Service
public interface JwtService {
    String createAccessToken(String username);
    String createRefreshToken();

    void updateRefreshToken(String username, String refreshToken);

    void destroyRefreshToken(String username);

    void sendToken(HttpServletResponse response, String accessToken, String refreshToken) throws IOException;

    String extractAccessToken(HttpServletRequest request) throws IOException, ServletException;

    String extractRefreshToken(HttpServletRequest request) throws IOException, ServletException;

    String extractUsername(String accessToken);

    void setAccessTokenHeader(HttpServletResponse response, String accessToken);
    void setRefreshTokenHeader(HttpServletResponse response, String refreshToken);
}
