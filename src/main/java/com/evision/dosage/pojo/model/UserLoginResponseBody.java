package com.evision.dosage.pojo.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author DingZhanYang
 * @date 2020/2/18 10:51
 */
@Getter
@Setter
@NoArgsConstructor
public class UserLoginResponseBody {
    private String token;
    private String signTime;
    public UserLoginResponseBody(String token, String signTime){
        this.token = token;
        this.signTime = signTime;
    }
}
