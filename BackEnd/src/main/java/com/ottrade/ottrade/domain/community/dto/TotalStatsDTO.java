package com.ottrade.ottrade.domain.community.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TotalStatsDTO {
    private long totalUsers;
    private long totalPosts;
}