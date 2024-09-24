package kr.luciddevlog.saebyukLog.user.dto;

import kr.luciddevlog.saebyukLog.user.entity.UserItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Getter
public class MemberInfoDto {
    private final Long id;
    private final String userName;
    private final LocalDateTime createdAt;
    private final String name;
    private final String email;
}
