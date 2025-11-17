package com.campo.banco.controller;
import com.campo.banco.model.TransferenciaEntity;
import com.campo.banco.service.TransferenciaIService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/transferencias")
@RequiredArgsConstructor
public class TransferenciaController {

    private final TransferenciaIService transferenciaIService;

    // ✔ GET listar transferencias
    @GetMapping
    public Flux<TransferenciaEntity> listar() {
        return transferenciaIService.listarTodas();
    }

    // ✔ GET buscar transferencia por ID
    @GetMapping("/{id}")
    public Mono<TransferenciaEntity> obtenerPorId(@PathVariable Long id) {
        return transferenciaIService.buscarPorId(id);
    }

    // ✔ DELETE eliminar transferencia
    @DeleteMapping("/{id}")
    public Mono<Void> eliminar(@PathVariable Long id) {
        return transferenciaIService.eliminar(id);
    }
    @PostMapping
    public Mono<TransferenciaEntity> crear(@RequestBody TransferenciaEntity transferencia) {
        return transferenciaIService.crearTransferencia(transferencia);
    }
}
