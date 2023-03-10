package com.example.aop;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class RestControllerAspect {
    private static final Logger logger = LoggerFactory.getLogger(RestControllerAspect.class);

    @Autowired
    private ObjectMapper mapper;

    @Pointcut("within(com.example.aop.controller..*) " +
            "&& @annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void pointcut() {
    }

    @Before("pointcut()")
    public void logMethod(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        RequestMapping mapping = signature.getMethod().getAnnotation(RequestMapping.class);

        Map<String, Object> parameters = getParameters(joinPoint);
        String methodName = signature.getMethod().getName();

        try {
            logger.info("Method name: {}", methodName);
            logger.info("==> path(s): {}, method(s): {}, arguments: {} ",
                    mapping.path(), mapping.method(), mapper.writeValueAsString(parameters));
        } catch (JsonProcessingException e) {
            logger.error("Error while converting", e);
        }
    }

    @AfterReturning(pointcut = "pointcut()", returning = "entity")
    public void logMethodAfter(JoinPoint joinPoint, ResponseEntity<?> entity) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        RequestMapping mapping = signature.getMethod().getAnnotation(RequestMapping.class);

        try {
            logger.info("<== path(s): {}, method(s): {}, retuning: {}",
                    mapping.path(), mapping.method(), mapper.writeValueAsString(entity));
        } catch (JsonProcessingException e) {
            logger.error("Error while converting", e);
        }
    }

    @AfterThrowing(pointcut = "pointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        logger.error("Exception in {}.{}() with cause = {}. Stack trace: {}", joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(), e.getCause() != null ? e.getCause() : "NULL", e.getStackTrace().toString());
    }


    private Map<String, Object> getParameters(JoinPoint joinPoint) {
        CodeSignature signature = (CodeSignature) joinPoint.getSignature();

        HashMap<String, Object> map = new HashMap<>();

        String[] parameterNames = signature.getParameterNames();

        for (int i = 0; i < parameterNames.length; i++) {
            map.put(parameterNames[i], joinPoint.getArgs()[i]);
        }

        return map;
    }
}
