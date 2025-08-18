package com.api.controleverbasbackend.infra.messaging.log;

import java.lang.annotation.*;

import com.api.controleverbasbackend.domain.enums.sistemalog.TipoLog;

@Repeatable(Loggables.class)
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Loggable {
    TipoLog tipo();

    String entidade();
}
