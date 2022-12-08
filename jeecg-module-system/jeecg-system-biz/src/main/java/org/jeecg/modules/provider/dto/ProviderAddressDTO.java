package org.jeecg.modules.provider.dto;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.util.Date;

@Data
public class ProviderAddressDTO {

    /**主键ID*/
    private String id;
    /**创建人*/
    private String createBy;
    /**创建时间*/
    private Date createTime;
    /**修改人*/
    private String updateBy;
    /**修改时间*/
    private Date updateTime;
    /**创建年*/
    private Integer year;
    /**创建月*/
    private Integer month;
    /**创建日*/
    private Integer day;
    /**删除状态（0，正常，1已删除）*/
    @TableLogic
    private String delFlag;
    /**供应商id*/
    private String sysUserId;
    /**区域id*/
    private String sysAreaId;
    /**所在城市*/
    private String areaAddress;
    /**详细地址*/
    private String detailedAddress;
    /**是否默认发货地址；0：否；1：是*/
    private String isDeliver;
    /**是否默认退货地址；0：否；1：是*/
    private String isReturn;
    /**联系人姓名*/
    private String contactName;
    /**联系人手机号*/
    private String contactPhone;
    /**联系电话*/
    private String contactTelephone;
    /**********添加字段***********/
    /**区域名称*/
    private String sysAreaName;
    /**供应商名称*/
    private String sysUserName;
}
