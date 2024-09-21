package kr.luciddevlog.saebyukLog.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import kr.luciddevlog.saebyukLog.user.entity.UserItem;
import lombok.Getter;

@Getter
public class LoginFormDto {
    @NotBlank(message = "아이디를 입력해 주세요.")
    private String username;

    @Size(min = 4, message = "비밀 번호는 4자 이상이어야 합니다.")
    private String password;

    public LoginFormDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public UserItem toEntity() {
        return UserItem.builder()
                .username(this.username)
                .password(this.password)
                .build();
    }
}
