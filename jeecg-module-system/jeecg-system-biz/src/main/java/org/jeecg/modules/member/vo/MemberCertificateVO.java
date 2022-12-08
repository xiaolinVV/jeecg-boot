package org.jeecg.modules.member.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class MemberCertificateVO {
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
    private String nickName;
    /**券号*/
    @Excel(name = "券号", width = 15)
    @ApiModelProperty(value = "券号")
    private String qqzixuangu;
    /**优惠券名称*/
    @Excel(name = "优惠券名称", width = 15)
    @ApiModelProperty(value = "优惠券名称")
    private String name;
    /**
     *兑换商品
     */
    private String applyGood ;
    /**
     *核销门店
     */
    private String storeQuantity ;
    /**
     *状态
     */
    private String statusName ;
    /*优惠券状态；0:未生效；1：已生效（未使用）；2：已使用；3：已过期；4：已失效；5：已赠送；*/
    @Excel(name = "优惠券状态；0:未生效；1：已生效（未使用）；2：已使用；3：已过期；4：已失效；5：已赠送；", width = 15,dicCode = "vouchers_status")
    @ApiModelProperty(value = "优惠券状态；0:未生效；1：已生效（未使用）；2：已使用；3：已过期；4：已失效；5：已赠送；")
    @Dict(dicCode = "vouchers_status")
    private String status;
    /**获取渠道*/
    @Excel(name = "获取渠道", width = 15)
    @ApiModelProperty(value = "获取渠道")
    private String theChannel;
    /**创建时间*/
    @Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    /**
     *有效期
     */
    private String indate ;
    /**
     * 使用时间
     */

    private String useTime;
    /**
     *核销人
     */
    private String cancel ;

    /**查询条件开始时间*/
    @Excel(name = "查询条件开始时间", width = 15)
    private String createTime_begin;
    /**查询条件开始时间*/
    @Excel(name = "查询条件结束时间", width = 15)
    private String createTime_end;
    /**
     * 兑换券id
     */
    private String marketingCertificateId;
}
