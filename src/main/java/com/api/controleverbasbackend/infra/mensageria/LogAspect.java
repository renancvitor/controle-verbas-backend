package com.api.controleverbasbackend.infra.mensageria;

import java.time.LocalDateTime;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAspect {

    private final LogProducer logProducer;

    public LogAspect(LogProducer logProducer) {
        this.logProducer = logProducer;
    }

    @Around("@annotation(logavel)")
    public Object gerarLogAutomaticamente(ProceedingJoinPoint joinPoint, Loggable logavel) throws Throwable {
        Object retorno = joinPoint.proceed();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String usuario = (authentication != null && authentication.isAuthenticated()) ? authentication.getName()
                : "sistema";
        String nomeMetodo = joinPoint.getSignature().getName();
        String nomeClasse = joinPoint.getTarget().getClass().getSimpleName();

        SistemaLog log = new SistemaLog();
        log.setTipo(logavel.tipo());
        log.setUsuario(usuario);
        log.setDataHora(LocalDateTime.now());
        log.setMensagem("Método " + nomeMetodo + " da classe " + nomeClasse + " executado.");

        logProducer.enviarLog(log);

        return retorno;
    }
}