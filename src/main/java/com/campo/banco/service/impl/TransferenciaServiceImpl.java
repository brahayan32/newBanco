package com.campo.banco.service.impl;

import com.campo.banco.dto.TransferenciaDTO;
import com.campo.banco.model.CuentaEntity;
import com.campo.banco.model.TransferenciaEntity;
import com.campo.banco.repository.CuentaRepository;
import com.campo.banco.repository.TransferenciaRepository;
import com.campo.banco.service.TransferenciaIService;
import org.springframework.stereotype.Service;
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
    public Mono<TransferenciaEntity> transferir(TransferenciaDTO dto) {
        return cuentaRepository.findById(dto.getCuentaOrigenId())
                .zipWith(cuentaRepository.findById(dto.getCuentaDestinoId()))
                .flatMap(tuple -> {
                    CuentaEntity origen = tuple.getT1();
                    CuentaEntity destino = tuple.getT2();

                    if (origen == null || destino == null) {
                        return Mono.error(new IllegalArgumentException("Una de las cuentas no existe"));
                    }

                    if (origen.getSaldo() < dto.getMonto()) {
                        return Mono.error(new IllegalArgumentException("Saldo insuficiente"));
                    }

                    // Actualizar saldos
                    origen.setSaldo(origen.getSaldo() - dto.getMonto());
                    destino.setSaldo(destino.getSaldo() + dto.getMonto());

                    // Crear el registro de transferencia
                    TransferenciaEntity transferencia = new TransferenciaEntity();
                    transferencia.setCuentaOrigenId(dto.getCuentaOrigenId());
                    transferencia.setCuentaDestinoId(dto.getCuentaDestinoId());
                    transferencia.setMonto(dto.getMonto());

                    // Guardar todo de manera reactiva
                    return cuentaRepository.save(origen)
                            .then(cuentaRepository.save(destino))
                            .then(transferenciaRepository.save(transferencia));
                });
    }
}
