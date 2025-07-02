package com.ottrade.ottrade.domain.member.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RefreshToken {
    @Id
    private Long userId; // User의 ID를 PK로 사용

    private String token;
    
    public RefreshToken updateToken(String token) {
        this.token = token;
        return this;
    }

}
