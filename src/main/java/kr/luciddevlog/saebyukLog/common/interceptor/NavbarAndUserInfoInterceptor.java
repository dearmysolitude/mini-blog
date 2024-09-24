package kr.luciddevlog.saebyukLog.common.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.luciddevlog.saebyukLog.common.dto.NavViewDto;
import kr.luciddevlog.saebyukLog.user.dto.MemberInfoDto;
import kr.luciddevlog.saebyukLog.user.entity.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;

@Component
public class NavbarAndUserInfoInterceptor implements HandlerInterceptor {
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        if (modelAndView != null) {
                modelAndView.addObject("pageTitle", "Micro Blog: 새벽로그");
                modelAndView.addObject("navItems", getNavItems());
                modelAndView.addObject("urls", getPageUrls());

            // 사용자 정보 추가
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getPrincipal() instanceof CustomUserDetails) {
                CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
                modelAndView.addObject("user", MemberInfoDto.builder()
                        .id(userDetails.getId())
                        .userName(userDetails.getUsername())
                        .name(userDetails.getUserInfo().getName())
                        .createdAt(userDetails.getUserInfo().getCreatedAt())
                        .email(userDetails.getUserInfo().getEmail())
                        .build());
            }
        }
    }

    private List<NavViewDto> getNavItems() {
        // NavItems 생성 로직
        return Arrays.asList(
                new NavViewDto("My Page", ""),
                new NavViewDto("Feed", ""),
                new NavViewDto("Trending", "")
        );
    }

    private List<NavViewDto> getPageUrls() {
        return Arrays.asList(

        );
    }
}
