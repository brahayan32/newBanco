package com.campo.banco.service.impl;

import com.campo.banco.dto.TransferenciaDTO;
import com.campo.banco.model.CuentaEntity;
import com.campo.banco.model.TransferenciaEntity;
import com.campo.banco.repository.CuentaRepository;
import com.campo.banco.repository.TransferenciaRepository;
import com.campo.banco.service.TransferenciaIService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
public class TransferenciaServiceImpl implements TransferenciaIService {

    private final CuentaRepository cuentaRepository;
    private final TransferenciaRepository transferenciaRepository;

    public TransferenciaServiceImpl(CuentaRepository cuentaRepository, TransferenciaRepository transferenciaRepository) {
        this.cuentaRepository = cuentaRepository;
        this.transferenciaRepository = transferenciaRepository;
    }

    @Override
    @Transactional
    public Mono<TransferenciaEntity> transferir(TransferenciaDTO dto) {

        Mono<CuentaEntity> origenMono = cuentaRepository.findById(dto.getCuentaOrigenId())
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Cuenta de origen no existe")));

        Mono<CuentaEntity> destinoMono = cuentaRepository.findById(dto.getCuentaDestinoId())
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Cuenta de destino no existe")));

        return origenMono.zipWith(destinoMono)
                .flatMap(tuple -> {
                    CuentaEntity origen = tuple.getT1();
                    CuentaEntity destino = tuple.getT2();

                    if (origen.getSaldo() < dto.getMonto()) {
                        return Mono.error(new IllegalArgumentException("Saldo insuficiente"));
                    }

                    // Actualizar saldos en memoria
                    origen.setSaldo(origen.getSaldo() - dto.getMonto());
                    destino.setSaldo(destino.getSaldo() + dto.getMonto());

                    // Crear el registro de transferencia
                    TransferenciaEntity transferencia = new TransferenciaEntity();
                    transferencia.setCuentaOrigenId(dto.getCuentaOrigenId());
                    transferencia.setCuentaDestinoId(dto.getCuentaDestinoId());
                    transferencia.setMonto(dto.getMonto());

                    // Guardar todo de forma reactiva en secuencia dentro de la transacción
                    return cuentaRepository.save(origen)
                            .flatMap(o -> cuentaRepository.save(destino))
                            .flatMap(d -> transferenciaRepository.save(transferencia));
                });
    }
}
