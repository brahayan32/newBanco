package com.campo.banco.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import com.campo.banco.model.TransferenciaEntity;

public interface TransferenciaRepository extends ReactiveCrudRepository<TransferenciaEntity, Long> {
}
