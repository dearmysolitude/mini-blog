package kr.luciddevlog.saebyukLog.user.service;

import kr.luciddevlog.saebyukLog.user.dto.RegisterFormDto;

public interface UserService {
    void register(RegisterFormDto form);
}
