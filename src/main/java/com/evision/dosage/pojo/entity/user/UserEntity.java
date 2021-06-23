package com.evision.dosage.pojo.entity.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author DingZhanYang
 * @date 2020/2/17 17:07
 */
@Data
@TableName("user")
public class UserEntity extends UserCommonEntity {
    /**
     * 用户ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 角色ID
     */
    private int roleId;
    /**
     * 操作人
     */
    private int operatorId;
    /**
     * 是否修改过密码，变为活跃状态
     */
    private int isActivated;

}
