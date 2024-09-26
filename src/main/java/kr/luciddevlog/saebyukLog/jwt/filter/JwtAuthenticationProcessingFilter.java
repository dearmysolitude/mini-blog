package kr.luciddevlog.saebyukLog.jwt.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.luciddevlog.saebyukLog.jwt.service.JwtService;
import kr.luciddevlog.saebyukLog.user.repository.UserItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/*
    사용자는 기본적으로 AccessToken 만 사용하고
    1. Refresh token없고, AccessToken 유효한경우: 인증 처리 성공하나 Refresh Token을 재발급하지 않는다.
    2. AccessToken도 없는 경우, 인증 처리는 실패 > 403 ERORR
    3. Refresh Tokend이 있는 경우, DB의 RefreshToken 과 비교하여 일치하면 AccessToken 재발급, RefreshToken 재발급(RTR방식), 인증은 실패 처리
 */
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {

    private static final String NO_CHECK_URL = "/login";
    private final JwtService jwtService;
    private final UserItemRepository userItemRepository;
    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

    }
}
