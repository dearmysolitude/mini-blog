package kr.luciddevlog.saebyukLog.user.entity;

import kr.luciddevlog.saebyukLog.user.dto.UserInfoDto;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

@Getter
public class CustomUserDetails implements UserDetails {
    // Refactor 필요: UserItem 대신 최소한의 데이터만 가지는 DTO로 대체해야: 비밀번호, Username, Role
    private Long id;
    private String username;
    private String password;
    private final UserInfoDto userInfo;
    private Collection<? extends GrantedAuthority> authorities;
    private boolean isAdmin;

    public CustomUserDetails(UserItem userItem) {
        this.id = userItem.getId();
        this.username = userItem.getUsername();
        this.password = userItem.getPassword();
        this.authorities = Collections.singletonList(new SimpleGrantedAuthority(userItem.getRole().name()));
        this.userInfo = new UserInfoDto(userItem.getName(), userItem.getEmail(), userItem.getCreatedAt());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
