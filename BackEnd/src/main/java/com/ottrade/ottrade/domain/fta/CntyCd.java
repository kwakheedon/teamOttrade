package com.ottrade.ottrade.domain.fta;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "fta")
public class CntyCd {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY) //Identity 자동으로 autoincrement
    private int id;

    @Column(unique=true)
    private String cntyCd;
}
