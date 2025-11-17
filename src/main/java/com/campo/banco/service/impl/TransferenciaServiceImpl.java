package com.campo.banco.service.impl;

import org.springframework.stereotype.Service;

import com.campo.banco.dto.TransferenciaDTO;
import com.campo.banco.model.CuentaEntity;
import com.campo.banco.model.TransferenciaEntity;
import com.campo.banco.repository.CuentaRepository;
import com.campo.banco.repository.TransferenciaRepository;
import com.campo.banco.service.TransferenciaIService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransferenciaServiceImpl implements TransferenciaIService {

    private final CuentaRepository cuentaRepo;
    private final TransferenciaRepository transferenciaRepo;

    public TransferenciaServiceImpl(CuentaRepository cuentaRepo, TransferenciaRepository transferenciaRepo) {
        this.cuentaRepo = cuentaRepo;
        this.transferenciaRepo = transferenciaRepo;
    }

    @Override
    @Transactional
    public Mono<TransferenciaEntity> transferir(TransferenciaDTO dto) {

        Mono<CuentaEntity> origenMono = cuentaRepo.findById(dto.getCuentaOrigenId());
        Mono<CuentaEntity> destinoMono = cuentaRepo.findById(dto.getCuentaDestinoId());

        return origenMono.zipWith(destinoMono)
                .flatMap(tuple -> {
                    CuentaEntity origen = tuple.getT1();
                    CuentaEntity destino = tuple.getT2();

                    if (origen.getSaldo() < dto.getMonto()) {
                        return Mono.error(new RuntimeException("Saldo insuficiente"));
                    }

                    // Descontar y sumar
                    origen.setSaldo(origen.getSaldo() - dto.getMonto());
                    destino.setSaldo(destino.getSaldo() + dto.getMonto());

                    // Guardar ambas cuentas
                    return cuentaRepo.save(origen)
                            .then(cuentaRepo.save(destino))
                            .then(transferenciaRepo.save(
                                    new TransferenciaEntity(null,
                                            dto.getCuentaOrigenId(),
                                            dto.getCuentaDestinoId(),
                                            dto.getMonto()
                                    )
                            ));
                });
    }

    @Override
    public Flux<TransferenciaEntity> listar() {
        return transferenciaRepo.findAll();
    }
}
