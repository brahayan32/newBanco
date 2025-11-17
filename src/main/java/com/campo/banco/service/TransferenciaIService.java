package com.campo.banco.service;

import com.campo.banco.dto.TransferenciaDTO;
import com.campo.banco.model.TransferenciaEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TransferenciaIService {

    Mono<TransferenciaEntity> transferir(TransferenciaDTO dto);

    Flux<TransferenciaEntity> listar();

    Flux<TransferenciaEntity> listarTodas();

    Mono<TransferenciaEntity> buscarPorId(Long id);

    Mono<Void> eliminar(Long id);

    Mono<TransferenciaEntity> crearTransferencia(TransferenciaEntity t);
}
