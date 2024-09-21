package kr.luciddevlog.saebyukLog.user.exception;

public class UserNotFoundException extends LoginException{
    public UserNotFoundException(String message) {
        super(message);
    }
}
