package com.campo.banco.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("transferencias")
public class TransferenciaEntity {
    @Id
    private Long id;
    private Long cuentaOrigenId;
    private Long cuentaDestinoId;
    private Double monto;
}
