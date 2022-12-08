package org.jeecg.modules.store.dto;


import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 开店需要的参数
 */

@Data
public class StoreParamDTO {
    /**老板姓名（联系人名称）*/
    @NotNull(message = "联系人名称不能为空")
    private String bossName;
    /**老板手机（联系人手机号，是登录账号）*/
    @NotNull(message = "联系人手机号不能为空")
    private String bossPhone;
    @NotNull(message = "验证码不能为空")
    private String verificationCode;//验证码
    /**门店名称*/
    @NotNull(message = "门店名称不能为空")
    private String storeName;
    /**分店名称*/
    private String subStoreName;
    /**区域id*/
    @NotNull(message = "区域id为空")
    private String sysAreaId;
    /**城市区域地址说明*/
    @NotNull(message = "区域地址说明不能为空")
    private String areaAddress;
    /**门店详细地址*/
    @NotNull(message = "门店详细地址不能为空")
    private String storeAddress;
    /**开通类型：0：包年；1：终生*/
    @NotNull(message = "开店类型不能为空")
    private String openType;
    /**经度*/
    @NotNull(message = "经度不能为空")
    private BigDecimal longitude;
    /**纬度*/
    @NotNull(message = "纬度不能为空")
    private BigDecimal latitude;

    /**微信code*/
    private String code;

    /**
     * 推广的店铺id
     */
    private String tSysUserId;

    /**
     * 推广用户类型;0:店铺；3：加盟商
     */
    private String tType;
}
