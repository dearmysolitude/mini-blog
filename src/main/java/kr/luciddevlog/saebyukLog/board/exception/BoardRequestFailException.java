package kr.luciddevlog.saebyukLog.board.exception;

public class BoardRequestFailException extends RuntimeException {
    public BoardRequestFailException(String message) {
        super(message);
    }
}
