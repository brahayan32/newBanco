package com.campo.banco.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import com.campo.banco.model.CuentaEntity;

public interface CuentaRepository extends ReactiveCrudRepository<CuentaEntity, Long> {
}
