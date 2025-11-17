package com.campo.banco.service;

import com.campo.banco.model.TransferenciaEntity;
import com.campo.banco.repository.CuentaRepository;
import com.campo.banco.repository.TransferenciaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TransferenciaIService {

    private final TransferenciaRepository transferenciaRepository;
    private final CuentaRepository cuentaRepository;
    private final ReactiveTransactionManager txManager;

    // ✔ LISTAR TODAS
    public Flux<TransferenciaEntity> listarTodas() {
        return transferenciaRepository.findAll();
    }

    // ✔ BUSCAR POR ID
    public Mono<TransferenciaEntity> buscarPorId(Long id) {
        return transferenciaRepository.findById(id);
    }

    // ✔ ELIMINAR
    public Mono<Void> eliminar(Long id) {
        return transferenciaRepository.deleteById(id);
    }

    // ✔ CREAR TRANSFERENCIA (CORREGIDO)
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

                    if (origen.getSaldo().compareTo(monto) < 0) {
                        return Mono.error(new RuntimeException("Saldo insuficiente"));
                    }

                    // Restar del origen
                    origen.setSaldo(origen.getSaldo().subtract(monto));

                    // Sumar al destino
                    destino.setSaldo(destino.getSaldo().add(monto));

                    return cuentaRepository.save(origen)
                            .then(cuentaRepository.save(destino))
                            .then(transferenciaRepository.save(t));
                })
                .as(tx::transactional);
    }
}
