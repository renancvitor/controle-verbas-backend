package com.api.controleverbasbackend.infra;

public class AutorizacaoException extends RuntimeException {

    public AutorizacaoException(String mensagem) {
        super(mensagem);
    }
}
