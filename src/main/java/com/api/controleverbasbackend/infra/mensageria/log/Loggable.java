package com.api.controleverbasbackend.infra.mensageria.log;

import java.lang.annotation.*;

import com.api.controleverbasbackend.domain.sistemalog.TipoLog;

@Repeatable(Loggables.class)
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Loggable {
    TipoLog tipo();

    String entidade();
}
