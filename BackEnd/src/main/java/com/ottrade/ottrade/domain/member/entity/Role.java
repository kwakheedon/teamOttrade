package com.ottrade.ottrade.domain.member.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    user("ROLE_USER", "일반 사용자"),
    admin("ROLE_ADMIN", "관리자");

    private final String key;
    private final String title;
}