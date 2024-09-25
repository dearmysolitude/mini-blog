package kr.luciddevlog.saebyukLog.common.enumConverterForJPA;

import jakarta.persistence.AttributeConverter;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Objects;

@Slf4j
public abstract class AbstractCodedEnumConverter<T extends Enum<T> & CodedEnum<E>, E>
        implements AttributeConverter<T, E> {
    private final Class<T> clazz;

    public AbstractCodedEnumConverter(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public E convertToDatabaseColumn(T attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getCode();
    }

    @Override
    public T convertToEntityAttribute(E dbData) {
        if (Objects.isNull(dbData)) {
            return null;
        }
        return Arrays.stream(clazz.getEnumConstants())
                .filter(e -> e.getCode().equals(dbData))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown code: " + dbData));
    }
}
