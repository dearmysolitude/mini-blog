package kr.luciddevlog.saebyukLog.user.exception;

public class InvalidCredentialsException extends LoginException {
    public InvalidCredentialsException(String message) {
        super(message);
    }
}
