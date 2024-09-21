package kr.luciddevlog.saebyukLog.user.exception;

import java.nio.file.AccessDeniedException;

public class UnauthorizedException extends AccessDeniedException {
    public UnauthorizedException(String s) {
        super(s);
    }
}
