package com.evision.dosage.pojo.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author DingZhanYang
 * @date 2020/2/19 16:27
 */
@Getter
@Setter
@NoArgsConstructor
public class PagingResponseBody<T> extends DosageResponseBody<T>{
    /**
     * 查询的全部数量
     */
    private long total;
    /**
     * 当前页
     */
    private int nowPage;

    public static <T> PagingResponseBody<T> getInstance(int status, String message, T data, long total, int nowPage){
        PagingResponseBody<T> instance = new PagingResponseBody<>();
        instance.setStatus(status);
        instance.setMessage(message);
        instance.setData(data);
        instance.setTotal(total);
        instance.setNowPage(nowPage);
        return instance;
    }

    public static <T> PagingResponseBody<T> sucess(T data, long total, int nowPage){
        return getInstance(SUCCESS, "success", null, -1, -1);
    }

    public static <T> PagingResponseBody<T> failure(String message){
        return getInstance(FAILURE, message, null, -1, -1);
    }
}
