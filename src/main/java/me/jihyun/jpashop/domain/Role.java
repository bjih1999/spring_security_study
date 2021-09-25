package me.jihyun.jpashop.domain;

import lombok.Getter;

@Getter
public enum Role {
    USER("ROLE_USER", "일반사용자"),
    ADMIN("ROLE_ADMIN", "운영자");
    
    private String key;

    private String name;

    private Role(String key, String name) {
        this.key = key;
        this.name = name;
    }
}
