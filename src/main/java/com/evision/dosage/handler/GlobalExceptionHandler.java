package com.evision.dosage.handler;

import com.evision.dosage.exception.DosageException;
import com.evision.dosage.pojo.model.DosageResponseBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常拦截器
 *
 * @author: AubreyXue
 * @date: 2020-02-25 11:16
 **/
@Slf4j
@RestControllerAdvice
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

    /**
     * 自定义异常捕捉器
     *
     * @param ex 自定义异常类
     * @return 自定义封装对象
     */
    @ExceptionHandler(value = DosageException.class)
    public DosageResponseBody handlerDosageException(DosageException ex) {
        log.error("业务异常:{}", ex);
        return DosageResponseBody.failure(ex.getMessage());
    }


}
