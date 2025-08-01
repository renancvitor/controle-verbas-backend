package com.api.controleverbasbackend.infra.mensageria.log;

import com.api.controleverbasbackend.domain.sistemalog.SistemaLog;
import com.api.controleverbasbackend.domain.sistemalog.TipoLog;
import com.api.controleverbasbackend.dto.autenticacao.DadosTokenJWT;
import com.api.controleverbasbackend.dto.usuario.DadosResumidoUsuario;
import com.api.controleverbasbackend.infra.mensageria.kafka.LogProducer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Profile("dev")
@Aspect
@Component
public class LogAspect {

    private final LogProducer logProducer;
    private final ObjectMapper objectMapper;
    private final ApplicationContext applicationContext;

    public LogAspect(LogProducer logProducer,
            ApplicationContext applicationContext) {
        this.logProducer = logProducer;
        this.applicationContext = applicationContext;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Around("@annotation(com.api.controleverbasbackend.infra.mensageria.log.Loggable) || @annotation(com.api.controleverbasbackend.infra.mensageria.log.Loggables)")
    public Object gerarLogsMultiples(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        Loggable[] loggings;

        if (method.isAnnotationPresent(Loggable.class)) {
            loggings = new Loggable[] { method.getAnnotation(Loggable.class) };
        } else if (method.isAnnotationPresent(Loggables.class)) {
            loggings = method.getAnnotation(Loggables.class).value();
        } else {
            loggings = new Loggable[0];
        }

        Object retorno = null;
        boolean proceeded = false;

        for (Loggable loggable : loggings) {
            // Para evitar executar o método múltiplas vezes,
            // executa joinPoint.proceed() apenas na primeira iteração.
            if (!proceeded) {
                retorno = executarLog(joinPoint, loggable);
                proceeded = true;
            } else {
                // Só gera logs sem executar o método novamente
                gerarLogApenas(loggable, retorno, joinPoint.getArgs());
            }
        }

        return retorno;
    }

    private Object executarLog(ProceedingJoinPoint joinPoint, Loggable logavel) throws Throwable {
        String entidadeNome = logavel.entidade();
        TipoLog tipo = logavel.tipo();
        String usuario = obterUsuario();

        Object estadoAntigo = null;

        // Pega estado antigo SOMENTE se for PRE_UPDATE
        if (tipo == TipoLog.PRE_UPDATE) {
            Object id = extrairIdDoMetodo(joinPoint.getArgs());
            if (id != null) {
                estadoAntigo = buscarEntidadePorId(entidadeNome, id);
            }

            if (estadoAntigo != null) {
                logProducer.enviarLog(criarLog(usuario, TipoLog.PRE_UPDATE, entidadeNome, estadoAntigo,
                        "Estado ANTES da alteração em " + entidadeNome));
            }
        }

        Object estadoNovo = joinPoint.proceed();

        if (tipo == TipoLog.INSERT) {
            logProducer.enviarLog(criarLog(usuario, tipo, entidadeNome, estadoNovo,
                    "Estado APÓS a criação da entidade " + entidadeNome));
        } else if (tipo == TipoLog.POST_UPDATE) {
            logProducer.enviarLog(criarLog(usuario, tipo, entidadeNome, estadoNovo,
                    "Estado APÓS a alteração em " + entidadeNome));
        } else if (tipo == TipoLog.LOGIN) {
            String usuarioEmail = "sistema"; // fallback

            if (estadoNovo instanceof ResponseEntity<?> responseEntity) {
                Object body = responseEntity.getBody();
                if (body instanceof DadosTokenJWT dadosToken) {
                    DadosResumidoUsuario usuarioDTO = dadosToken.usuario();
                    if (usuarioDTO != null) {
                        usuarioEmail = usuarioDTO.email();
                    }
                }
            }

            Map<String, Object> payloadAuth = Map.of("usuario", usuarioEmail);
            logProducer.enviarLog(criarLog(usuarioEmail, tipo, entidadeNome, payloadAuth,
                    tipo.name() + " realizado com sucesso na entidade " + entidadeNome));
        }

        return estadoNovo;
    }

    private void gerarLogApenas(Loggable logavel, Object estadoNovo, Object[] args) {
        String entidadeNome = logavel.entidade();
        TipoLog tipo = logavel.tipo();
        String usuario = obterUsuario();

        if (tipo == TipoLog.PRE_UPDATE || tipo == TipoLog.DELETE) {
            Object id = extrairIdDoMetodo(args);
            Object estadoAntigo = buscarEntidadePorId(entidadeNome, id);
            if (estadoAntigo != null) {
                logProducer.enviarLog(criarLog(usuario, TipoLog.PRE_UPDATE, entidadeNome, estadoAntigo,
                        "Estado ANTES da alteração em " + entidadeNome));
            }
        } else if (tipo == TipoLog.INSERT || tipo == TipoLog.POST_UPDATE) {
            logProducer.enviarLog(criarLog(usuario, tipo, entidadeNome, estadoNovo,
                    "Estado APÓS a alteração em " + entidadeNome));
        }
    }

    private String obterUsuario() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) ? auth.getName()
                : "sistema";
    }

    private SistemaLog criarLog(String usuario, TipoLog tipo, String entidade, Object objeto, String mensagem) {
        try {
            String payloadJson = (objeto != null) ? objectMapper.writeValueAsString(objeto) : null;
            SistemaLog log = new SistemaLog();
            log.setTipo(tipo.name());
            log.setEntidade(entidade);
            log.setUsuario(usuario);
            log.setMensagem(mensagem);
            log.setPayload(payloadJson);
            log.setDataHora(LocalDateTime.now());
            return log;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao serializar log", e);
        }
    }

    private Object extrairIdDoMetodo(Object[] args) {
        for (Object arg : args) {
            if (arg instanceof Long || arg instanceof Integer || arg instanceof String) {
                return arg; // ID direto
            } else {
                try {
                    var method = arg.getClass().getMethod("id");
                    return method.invoke(arg);
                } catch (Exception ignored) {
                }
            }
        }
        return null;
    }

    private Object buscarEntidadePorId(String entidade, Object id) {
        if (id == null)
            return null;

        String nomeBean = entidade.substring(0, 1).toLowerCase() + entidade.substring(1) + "Repository";
        try {
            Object repository = applicationContext.getBean(nomeBean);

            Method findByIdMethod = null;
            for (Method method : repository.getClass().getMethods()) {
                if (method.getName().equals("findById") && method.getParameterCount() == 1) {
                    findByIdMethod = method;
                    break;
                }
            }
            if (findByIdMethod == null) {
                throw new RuntimeException("Método findById não encontrado no repositório: " + nomeBean);
            }

            Class<?> idType = findByIdMethod.getParameterTypes()[0];
            Object idConvertido = converterId(id, idType);

            Optional<?> entidadeOptional = (Optional<?>) findByIdMethod.invoke(repository, idConvertido);
            return entidadeOptional.orElse(null);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar entidade '" + entidade + "' com ID: " + id, e);
        }
    }

    private Object converterId(Object id, Class<?> idType) {
        if (id == null)
            return null;

        if (idType.isInstance(id)) {
            return id;
        }

        if (id instanceof Number) {
            Number num = (Number) id;
            if (idType == Long.class || idType == long.class) {
                return num.longValue();
            }
            if (idType == Integer.class || idType == int.class) {
                return num.intValue();
            }
            if (idType == Short.class || idType == short.class) {
                return num.shortValue();
            }
            if (idType == Byte.class || idType == byte.class) {
                return num.byteValue();
            }
            if (idType == Double.class || idType == double.class) {
                return num.doubleValue();
            }
            if (idType == Float.class || idType == float.class) {
                return num.floatValue();
            }
        }

        if (idType == String.class) {
            return id.toString();
        }

        throw new IllegalArgumentException(
                "Não foi possível converter ID do tipo " + id.getClass() + " para " + idType);
    }
}
