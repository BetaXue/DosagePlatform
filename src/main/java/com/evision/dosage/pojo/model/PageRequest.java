package com.evision.dosage.pojo.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * 简单分页模型
 *
 * @author xuebing
 * @since 2020-02-17
 */
@Getter
@Setter
@NoArgsConstructor
public class PageRequest implements Serializable {

    private static final long serialVersionUID = -4869594085374385813L;
    /**
     * 当前页面数据量
     */
    private int pageSize = 10;
    /**
     * 当前页码
     */
    private int pageNum = 1;
    /**
     * 排序字段
     */
    private String field;
    /**
     * 排序规则，asc升序，desc降序
     */
    private String order;
}
