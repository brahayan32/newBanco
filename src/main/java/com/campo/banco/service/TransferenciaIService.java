package com.campo.banco.service;

import com.campo.banco.dto.TransferenciaDTO;
import com.campo.banco.model.TransferenciaEntity;
import reactor.core.publisher.Mono;

public interface TransferenciaIService {
    Mono<TransferenciaEntity> transferir(TransferenciaDTO dto);
}
