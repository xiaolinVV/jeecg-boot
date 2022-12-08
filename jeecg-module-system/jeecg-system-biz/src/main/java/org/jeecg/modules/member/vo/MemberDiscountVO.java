package org.jeecg.modules.member.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class MemberDiscountVO {
    /**头像绝对地址*/
    @Excel(name = "头像绝对地址", width = 15)
    @ApiModelProperty(value = "头像绝对地址")
    private String headPortrait;
    /**手机号*/
    @Excel(name = "手机号", width = 15)
    @ApiModelProperty(value = "手机号")
    private String phone;
    /**会员昵称*/
    @Excel(name = "会员昵称", width = 15)
    @ApiModelProperty(value = "会员昵称")
    private String niName;
    /**券号*/
    @Excel(name = "券号", width = 15)
    @ApiModelProperty(value = "券号")
    private String qqzixuangu;
    /**优惠券名称*/
    @Excel(name = "优惠券名称", width = 15)
    @ApiModelProperty(value = "优惠券名称")
    private String name;
    /**
     *使用门槛
     */
    private String usingThreshold ;
    /**
     *优惠内容
     */
    private String preferentialContent ;
    /**
     *适用商品
     */
    private String applyGood ;
    /**门店名称*/
    @Excel(name = "门店名称", width = 15)
    @ApiModelProperty(value = "门店名称")
    private String storeName;
    /**
     * 发行人
     */
    private String issuer;
    /**
     *有效期
     */
    private String indate;
    /**
     * 状态
     */
    private String statusName;
    /*优惠券状态；0:未生效；1：已生效（未使用）；2：已使用；3：已过期；4：已失效；5：已赠送；*/
    @Excel(name = "优惠券状态；0:未生效；1：已生效（未使用）；2：已使用；3：已过期；4：已失效；5：已赠送；", width = 15,dicCode = "vouchers_status")
    @ApiModelProperty(value = "优惠券状态；0:未生效；1：已生效（未使用）；2：已使用；3：已过期；4：已失效；5：已赠送；")
    @Dict(dicCode = "vouchers_status")
    private String status;
    /**创建时间*/
    @Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    //使用时间
    @Excel(name = "使用时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date userTime;


    /**查询条件开始时间*/
    @Excel(name = "查询条件开始时间", width = 15)
    private String createTime_begin;
    /**查询条件开始时间*/
    @Excel(name = "查询条件结束时间", width = 15)
    private String createTime_end;


    /**查询条件开始时间*/
    @Excel(name = "查询条件开始时间", width = 15)
    private String userTime_begin;
    /**查询条件开始时间*/
    @Excel(name = "查询条件结束时间", width = 15)
    private String userTime_end;
    /**
     * 优惠券id
     */
    private String marketingDiscountId;
    /**
     * 0:店铺 1:平台
     */
    private String isPlatform;

    private Integer pageNo;

    private Integer pageSize;
}
