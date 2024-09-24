package kr.luciddevlog.saebyukLog.common.dto;

import lombok.Getter;

@Getter
public class NavViewDto {
    private String name;
    private String url;

    public NavViewDto(String name, String url) {
        this.name = name;
        this.url = url;
    }
}

