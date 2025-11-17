package com.campo.banco.service.impl;

import com.campo.banco.dto.TransferenciaDTO;
import com.campo.banco.model.CuentaEntity;
import com.campo.banco.model.TransferenciaEntity;
import com.campo.banco.repository.CuentaRepository;
import com.campo.banco.repository.TransferenciaRepository;
import com.campo.banco.service.TransferenciaIService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.reactive.TransactionalOperator;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TransferenciaServiceImpl implements TransferenciaIService {

    private final CuentaRepository cuentaRepository;
    private final TransferenciaRepository transferenciaRepository;
    private final ReactiveTransactionManager txManager;

    @Override
    public Mono<TransferenciaEntity> crearTransferencia(TransferenciaEntity t) {

        TransactionalOperator tx = TransactionalOperator.create(txManager);
        BigDecimal monto = BigDecimal.valueOf(t.getMonto());

        return cuentaRepository.findById(t.getCuentaOrigenId())
                .switchIfEmpty(Mono.error(new RuntimeException("Cuenta origen no existe")))
                .zipWith(
                        cuentaRepository.findById(t.getCuentaDestinoId())
                                .switchIfEmpty(Mono.error(new RuntimeException("Cuenta destino no existe")))
                )
                .flatMap(tuple -> {
                    var origen = tuple.getT1();
                    var destino = tuple.getT2();

                    if (origen.getSaldo().compareTo(monto) < 0)
                        return Mono.error(new RuntimeException("Saldo insuficiente"));

                    origen.setSaldo(origen.getSaldo().subtract(monto));
                    destino.setSaldo(destino.getSaldo().add(monto));

                    return cuentaRepository.save(origen)
                            .then(cuentaRepository.save(destino))
                            .then(transferenciaRepository.save(t));
                })
                .as(tx::transactional);
    }

    @Override
    public Mono<TransferenciaEntity> transferir(TransferenciaDTO dto) {
        return crearTransferencia(
                new TransferenciaEntity(
                        null,
                        dto.getCuentaOrigenId(),
                        dto.getCuentaDestinoId(),
                        dto.getMonto()
                )
        );
    }

    @Override
    public Flux<TransferenciaEntity> listar() {
        return transferenciaRepository.findAll();
    }

    @Override
    public Flux<TransferenciaEntity> listarTodas() {
        return transferenciaRepository.findAll();
    }

    @Override
    public Mono<TransferenciaEntity> buscarPorId(Long id) {
        return transferenciaRepository.findById(id);
    }

    @Override
    public Mono<Void> eliminar(Long id) {
        return transferenciaRepository.deleteById(id);
    }
}
