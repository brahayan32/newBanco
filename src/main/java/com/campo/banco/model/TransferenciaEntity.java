package com.campo.banco.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("transferencias")
public class TransferenciaEntity {
    @Id
    private Long id;
    private Long cuentaOrigenId;
    private Long cuentaDestinoId;
    private Double monto;
}
