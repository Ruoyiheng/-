package com.yuyou.zizaiyou.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @TableName userinfo
 */
@TableName(value ="userinfo")
@Data
public class Userinfo implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    private String nickname;

    /**
     * 
     */
    private String phone;

    /**
     * 
     */
    private String email;

    /**
     * 
     */
    private String password;

    /**
     * 
     */
    private Integer gender =0;//保密

    /**
     * 
     */
    private Integer level=0;//用户级别

    /**
     * 
     */
    private String city;

    /**
     * 
     */
    private String headImgUrl;//头像

    /**
     * 
     */
    private String info;

    /**
     * 
     */
    private Integer state = 0;//正常

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}