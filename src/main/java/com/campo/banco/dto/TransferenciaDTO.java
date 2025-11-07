package com.campo.banco.dto;

import lombok.Data;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Data
public class TransferenciaDTO {

    @NotNull(message = "La cuenta origen es obligatoria")
    private Long cuentaOrigenId;

    @NotNull(message = "La cuenta destino es obligatoria")
    private Long cuentaDestinoId;

    @NotNull(message = "El monto es obligatorio")
    @Min(value = 1, message = "El monto mínimo debe ser 1")
    private Double monto;
}
