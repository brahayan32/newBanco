package com.campo.banco.controller;

import org.springframework.web.bind.annotation.*;
import com.campo.banco.dto.TransferenciaDTO;
import com.campo.banco.model.TransferenciaEntity;
import com.campo.banco.service.TransferenciaIService;

import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/transferencias")
public class TransferenciaController {

    private final TransferenciaIService servicio;

    public TransferenciaController(TransferenciaIService servicio) {
        this.servicio = servicio;
    }

    @PostMapping
    public Mono<TransferenciaEntity> hacerTransferencia(@Valid @RequestBody TransferenciaDTO dto) {
        return servicio.transferir(dto);
    }
}
