package com.campo.banco.controller;

import com.campo.banco.model.CuentaEntity;
import com.campo.banco.repository.CuentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/cuentas")
public class CuentaController {

    @Autowired
    private CuentaRepository cuentaRepository;

    @GetMapping
    public Flux<CuentaEntity> listarCuentas() {
        return cuentaRepository.findAll();
    }

    @GetMapping("/{id}")
    public Mono<CuentaEntity> obtenerCuenta(@PathVariable Long id) {
        return cuentaRepository.findById(id);
    }

    @PostMapping
    public Mono<CuentaEntity> crearCuenta(@RequestBody CuentaEntity cuenta) {
        return cuentaRepository.save(cuenta);
    }
}
