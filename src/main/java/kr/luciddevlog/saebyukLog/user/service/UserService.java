package kr.luciddevlog.saebyukLog.user.service;

import kr.luciddevlog.saebyukLog.user.dto.MemberInfoDto;
import kr.luciddevlog.saebyukLog.user.dto.RegisterFormDto;
import kr.luciddevlog.saebyukLog.user.entity.UserItem;

public interface UserService {
    void register(RegisterFormDto form);
    void deleteUser(MemberInfoDto userInfo);
    void updateUserInfo(RegisterFormDto form, UserItem userItem);
}
