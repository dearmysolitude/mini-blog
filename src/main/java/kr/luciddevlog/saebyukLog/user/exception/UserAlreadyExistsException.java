package kr.luciddevlog.saebyukLog.user.exception;

public class UserAlreadyExistsException extends LoginException{
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
