package com.api.controleverbasbackend.infra.mensageria;

import java.lang.annotation.*;

@Repeatable(Loggables.class)
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Loggable {
    TipoLog tipo(); // Tipo da ação: INSERT, DELETE, etc.

    String entidade(); // Nome da entidade sendo manipulada (ex: "Usuario")
}
