package com.ottrade.ottrade.domain.log.entity; // (패키지는 알맞게 수정해주세요)

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "pnm_log")
public class PnmLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String pnm;

    @CreationTimestamp
    @Column(name = "searched_at")
    private Timestamp searchedAt;
}