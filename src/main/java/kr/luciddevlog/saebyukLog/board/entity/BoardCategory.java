package kr.luciddevlog.saebyukLog.board.entity;

import kr.luciddevlog.saebyukLog.common.enumConverterForJPA.AbstractCodedEnumConverter;
import kr.luciddevlog.saebyukLog.common.enumConverterForJPA.CodedEnum;

public enum BoardCategory implements CodedEnum<Integer> {
    NOTICE(1),
    REVIEW(2);

    private final int code;

    BoardCategory(int code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    @jakarta.persistence.Converter(autoApply = true)
    static class Converter extends AbstractCodedEnumConverter<BoardCategory, Integer> {
        public Converter() {
            super(BoardCategory.class);
        }
    }

}
