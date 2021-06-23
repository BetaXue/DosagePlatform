package com.evision.dosage.exception;

/**
 * 自定义异常类
 *
 * @author: AubreyXue
 * @date: 2020-02-25 11:17
 **/
public class DosageException extends RuntimeException {

    /**
     * 设置自定义异常内容
     *
     * @param message 异常信息
     */
    public DosageException(String message) {
        super(message);
    }

}
