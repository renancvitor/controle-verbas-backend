package com.api.controleverbasbackend.infra.exception;

public class AutorizacaoException extends RuntimeException {

    public AutorizacaoException(String mensagem) {
        super(mensagem);
    }
}
