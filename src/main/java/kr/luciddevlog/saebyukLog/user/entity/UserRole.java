package kr.luciddevlog.saebyukLog.user.entity;

import kr.luciddevlog.saebyukLog.common.enumConverterForJPA.AbstractCodedEnumConverter;
import kr.luciddevlog.saebyukLog.common.enumConverterForJPA.CodedEnum;
import lombok.Getter;

@Getter
public enum UserRole implements CodedEnum<String> {
    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_USER("ROLE_USER");

    private final String code;

    UserRole(String code) {
        this.code = code;
    }

    @jakarta.persistence.Converter(autoApply = true)
    static class Converter extends AbstractCodedEnumConverter<UserRole, String> {
        public Converter() {
            super(UserRole.class);
        }
    }
}
