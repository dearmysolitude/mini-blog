package kr.luciddevlog.saebyukLog.user.entity;

import kr.luciddevlog.saebyukLog.common.enumConverterForJPA.AbstractCodedEnumConverter;
import kr.luciddevlog.saebyukLog.common.enumConverterForJPA.CodedEnum;
import lombok.Getter;

@Getter
public enum SocialType implements CodedEnum<String>{
    KAKAO("KAKAO"),
    NAVER("NAVER"),
    GOOGLE("GOOGLE");

    private final String code;

    SocialType(String code) {
        this.code = code;
    }

    @jakarta.persistence.Converter(autoApply = true)
    static class Converter extends AbstractCodedEnumConverter<SocialType, String> {
        public Converter() {
            super(SocialType.class);
        }
    }

}