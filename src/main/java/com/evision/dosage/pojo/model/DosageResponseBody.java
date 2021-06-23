package com.evision.dosage.pojo.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author DingZhanYang
 * @date 2020/2/16 11:18
 */
@Getter
@Setter
@NoArgsConstructor
public class DosageResponseBody<T> {
    public static final int SUCCESS = 1;
    public static final int FAILURE = 0;
    private int status;
    private String message;
    private T data;

    public static <T> DosageResponseBody<T> getInstance(int status, String message, T data){
        DosageResponseBody<T> responseBody = new DosageResponseBody<T>();
        responseBody.setStatus(status);
        responseBody.setMessage(message);
        responseBody.setData(data);
        return responseBody;
    }

    public static <T> DosageResponseBody<T> success(){
        return getInstance(SUCCESS, "success", null);
    }
    public static <T> DosageResponseBody<T> success(T data){
        return getInstance(SUCCESS, "success", data);
    }

    public static <T> DosageResponseBody<T> failure(String message){
        return getInstance(FAILURE, message, null);
    }

}
