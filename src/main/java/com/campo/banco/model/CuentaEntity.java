package com.campo.banco.model;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Data
@Table("cuentas")
public class CuentaEntity {
    @Id
    private Long id;
    private String titular;
    private BigDecimal saldo;
}
