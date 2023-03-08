package org.jeecg.modules.alliance.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description: 加盟商管理
 * @Author: jeecg-boot
 * @Date: 2020-05-17
 * @Version: V1.0
 */
@Data
@TableName("alliance_manage")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "alliance_manage对象", description = "加盟商管理")
public class AllianceManageVO {

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "主键ID")
    private String id;
    /**
     * 创建人
     */
    @Excel(name = "创建人", width = 15)
    @ApiModelProperty(value = "创建人")
    private String createBy;
    /**
     * 创建时间
     */
    @Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    /**
     * 修改人
     */
    @Excel(name = "修改人", width = 15)
    @ApiModelProperty(value = "修改人")
    private String updateBy;
    /**
     * 修改时间
     */
    @Excel(name = "修改时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;
    /**
     * 创建年
     */
    @Excel(name = "创建年", width = 15)
    @ApiModelProperty(value = "创建年")
    private Integer year;
    /**
     * 创建月
     */
    @Excel(name = "创建月", width = 15)
    @ApiModelProperty(value = "创建月")
    private Integer month;
    /**
     * 创建日
     */
    @Excel(name = "创建日", width = 15)
    @ApiModelProperty(value = "创建日")
    private Integer day;
    /**
     * 删除状态（0，正常，1已删除）
     */
    @Excel(name = "删除状态（0，正常，1已删除）", width = 15)
    @ApiModelProperty(value = "删除状态（0，正常，1已删除）")
    private String delFlag;
    /**
     * 名称
     */
    @Excel(name = "公司名称", width = 15)
    @ApiModelProperty(value = "公司名称")
    private String name;
    /**
     * 二维码id映射
     */
    @Excel(name = "二维码id映射", width = 15)
    @ApiModelProperty(value = "二维码id映射")
    private String sysSmallcodeId;
    /**
     * 利润分配；0：独享；1：共享
     */
    @Excel(name = "利润分配；0：独享；1：共享", width = 15)
    @ApiModelProperty(value = "利润分配；0：独享；1：共享")
    private String profitType;
    /**
     * 订单佣金比例
     */
    @Excel(name = "订单佣金比例", width = 15)
    @ApiModelProperty(value = "订单佣金比例")
    private BigDecimal orderCommissionRate;
    /**
     * 礼包佣金比例
     */
    @Excel(name = "礼包佣金比例", width = 15)
    @ApiModelProperty(value = "礼包佣金比例")
    private BigDecimal giftCommissionRate;
    /**
     * 开店佣金比例
     */
    @Excel(name = "开店佣金比例", width = 15)
    @ApiModelProperty(value = "开店佣金比例")
    private BigDecimal storeCommissionRate;
    /**
     * 福利金销售奖励
     */
    @Excel(name = "福利金销售奖励", width = 15)
    @ApiModelProperty(value = "福利金销售奖励")
    private BigDecimal welfareCommissionRate;
    /**
     * 推荐供应商销售奖励
     */
    @Excel(name = "推荐供应商销售奖励", width = 15)
    @ApiModelProperty(value = "推荐供应商销售奖励")
    private BigDecimal supplierSalesCommissionRate;
    /**
     * 兑换券销售奖励
     */
    @Excel(name = "兑换券销售奖励", width = 15)
    @ApiModelProperty(value = "兑换券销售奖励")
    private BigDecimal cashCouponSalesIncentives;
    /**
     * 开始时间
     */
    @Excel(name = "开始时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "开始时间")
    private Date startTime;
    /**
     * 结束时间
     */
    @Excel(name = "结束时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "结束时间")
    private Date endTime;
    /**
     * 备注
     */
    @Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String remark;
    /**
     * 状态；0：停用；1：启用
     */
    @Excel(name = "状态；0：停用；1：启用", width = 15)
    @ApiModelProperty(value = "状态；0：停用；1：启用")
    private String status;
    /**
     * 停用说明
     */
    @Excel(name = "停用说明", width = 15)
    @ApiModelProperty(value = "停用说明")
    private String closeExplain;
    /**
     * 加盟商id
     */
    @Excel(name = "加盟商id", width = 15)
    @ApiModelProperty(value = "加盟商id")
    private String sysUserId;
    /**
     * 所在城市三个逗号隔开
     */
    @Excel(name = "所在城市三个逗号隔开", width = 15)
    @ApiModelProperty(value = "所在城市三个逗号隔开")
    private String agencyAreaId;
    /**
     * 详细地址
     */
    @Excel(name = "详细地址", width = 15)
    @ApiModelProperty(value = "详细地址")
    private String areaAddress;
    /**
     * 公司电话
     */
    @Excel(name = "公司电话", width = 15)
    @ApiModelProperty(value = "公司电话")
    private String companyPhone;
    /**
     * 备注说明
     */
    @Excel(name = "备注说明", width = 15)
    @ApiModelProperty(value = "备注说明")
    private String remarkExplian;
    @Excel(name = "余额", width = 15)
    @ApiModelProperty(value = "余额")
    private BigDecimal balance;
    @Excel(name = "冻结金额（待结算）", width = 15)
    @ApiModelProperty(value = "冻结金额（待结算）")
    private BigDecimal accountFrozen;
    @Excel(name = "不可用金额", width = 15)
    @ApiModelProperty(value = "不可用金额")
    private BigDecimal unusableFrozen;
    @Excel(name = "利润分配；0：独享；1：共享", width = 15)
    @ApiModelProperty(value = "利润分配；0：独享；1：共享")
    private String distributionProfits;
    @Excel(name = "0：与交易渔区代理共享；1：与指定区域县级代理共享", width = 15)
    @ApiModelProperty(value = "0：与交易渔区代理共享；1：与指定区域县级代理共享")
    private String mutualAdvantages;
    @Excel(name = "加盟商比例", width = 15)
    @ApiModelProperty(value = "加盟商比例")
    private BigDecimal franchiseeRatio;
    @Excel(name = "县级代理比例", width = 15)
    @ApiModelProperty(value = "县级代理比例")
    private BigDecimal agencyRatio;
    @Excel(name = "省代id", width = 15)
    @ApiModelProperty(value = "省代id")
    private String provinceId;
    @Excel(name = "市代id", width = 15)
    @ApiModelProperty(value = "市代id")
    private String cityId;
    @Excel(name = "区县代id", width = 15)
    @ApiModelProperty(value = "区县代id")
    private String countyId;

    /**
     * 用户账号
     */
    private String username;
    /**
     * 用户头像
     */
    private String avatar;
    /**
     * 用户名字
     */
    private String realname;
    /**
     * 用户手机号
     */
    private String phone;
    /**
     * 二维码
     */
    private String address;

    /**
     * 分配比例
     */
    private String allocationStr;

    //大屏是否统计；0：不统计；1：统计
    private String isView;

}
