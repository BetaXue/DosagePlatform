package com.evision.dosage.pojo.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author DingZhanYang
 * @date 2020/2/20 9:47
 */
@Getter
@Setter
@NoArgsConstructor
public class QueryRequestBody extends PageRequest {
    private String queryContent;
}
