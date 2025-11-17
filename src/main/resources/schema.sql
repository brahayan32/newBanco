CREATE TABLE IF NOT EXISTS cuentas (
    id SERIAL PRIMARY KEY,
    titular VARCHAR(100) NOT NULL,
    saldo DOUBLE PRECISION DEFAULT 0
);

CREATE TABLE IF NOT EXISTS transferencias (
    id SERIAL PRIMARY KEY,
    cuenta_origen_id INTEGER NOT NULL,
    cuenta_destino_id INTEGER NOT NULL,
    monto DOUBLE PRECISION NOT NULL,
    CONSTRAINT fk_transferencia_cuenta_origen FOREIGN KEY (cuenta_origen_id) REFERENCES cuentas (id),
    CONSTRAINT fk_transferencia_cuenta_destino FOREIGN KEY (cuenta_destino_id) REFERENCES cuentas (id)
);