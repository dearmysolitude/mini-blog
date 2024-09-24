package kr.luciddevlog.saebyukLog.user.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserInfoDto {
    private final String name;
    private final String email;
    private final LocalDateTime createdAt;

    public UserInfoDto(String name, String email, LocalDateTime createdAt) {
        this.name = name;
        this.email = email;
        this.createdAt = createdAt;
    }
}

