package com.campo.banco.model;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("cuentas")
public class CuentaEntity {
    @Id
    private Long id;
    private String titular;
    private Double saldo;
}
