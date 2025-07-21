package com.api.controleverbasbackend.infra.mensageria;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Loggable {
    String tipo() default "INFO";
}
