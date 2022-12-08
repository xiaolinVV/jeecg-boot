package org.jeecg.modules.marketing.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description: 店铺分销设置
 * @Author: jeecg-boot
 * @Date:   2019-12-10
 * @Version: V1.0
 */
@Data
@TableName("marketing_store_distribution_setting")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="marketing_store_distribution_setting对象", description="店铺分销设置")
public class MarketingStoreDistributionSettingVO {

	/**主键ID*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键ID")
	private String id;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
    @ApiModelProperty(value = "创建人")
	private String createBy;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
	private Date createTime;
	/**修改人*/
	@Excel(name = "修改人", width = 15)
    @ApiModelProperty(value = "修改人")
	private String updateBy;
	/**修改时间*/
	@Excel(name = "修改时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
	private Date updateTime;
	/**创建年*/
	@Excel(name = "创建年", width = 15)
    @ApiModelProperty(value = "创建年")
	private Integer year;
	/**创建月*/
	@Excel(name = "创建月", width = 15)
    @ApiModelProperty(value = "创建月")
	private Integer month;
	/**创建日*/
	@Excel(name = "创建日", width = 15)
    @ApiModelProperty(value = "创建日")
	private Integer day;
	/**删除状态（0，正常，1已删除）*/
	@Excel(name = "删除状态（0，正常，1已删除）", width = 15)
    @ApiModelProperty(value = "删除状态（0，正常，1已删除）")
	@TableLogic
	private String delFlag;
	/**普通一级分销比例*/
	@Excel(name = "普通一级分销比例", width = 15)
    @ApiModelProperty(value = "普通一级分销比例")
	private BigDecimal commonFirst;
	/**普通二级分销比例*/
	@Excel(name = "普通二级分销比例", width = 15)
    @ApiModelProperty(value = "普通二级分销比例")
	private BigDecimal commonSecond;
	/**会员一级分销比例*/
	@Excel(name = "会员一级分销比例", width = 15)
    @ApiModelProperty(value = "会员一级分销比例")
	private BigDecimal vipFirst;
	/**会员二级分销比例*/
	@Excel(name = "会员二级分销比例", width = 15)
    @ApiModelProperty(value = "会员二级分销比例")
	private BigDecimal vipSecond;
	/**状态；0：停用；1：启用*/
	@Excel(name = "状态；0：停用；1：启用", width = 15)
    @ApiModelProperty(value = "状态；0：停用；1：启用")
	private String status;
	@Excel(name = "店铺id", width = 15)
	@ApiModelProperty(value = "店铺id")
	private String sysUserId;
	/**logo图片地址*/
	@Excel(name = "logo图片地址", width = 15)
	@ApiModelProperty(value = "logo图片地址")
	private String logoAddr;
	/**门店名称*/
	@Excel(name = "门店名称", width = 15)
	@ApiModelProperty(value = "门店名称")
	private String storeName;
	/**老板姓名（联系人名称）*/
	@Excel(name = "老板姓名（联系人名称）", width = 15)
	@ApiModelProperty(value = "老板姓名（联系人名称）")
	private String bossName;
	/**老板手机（联系人手机号，是登录账号）*/
	@Excel(name = "老板手机（联系人手机号，是登录账号）", width = 15)
	@ApiModelProperty(value = "老板手机（联系人手机号，是登录账号）")
	private String bossPhone;
	/**会员昵称*/
	@Excel(name = "会员昵称", width = 15)
	@ApiModelProperty(value = "会员昵称")
	private String nickName;
	/**会员类型；建立会员类型数据字典*/
	@Excel(name = "会员类型；建立会员类型数据字典", width = 15,dicCode = "member_type")
	@ApiModelProperty(value = "会员类型；建立会员类型数据字典")
	@Dict(dicCode = "member_type")
	private String memberType;
	@Excel(name = "累计佣金", width = 15)
	@ApiModelProperty(value = "累计佣金")
	private BigDecimal totalCommission;
	@Excel(name = "已提现", width = 15)
	@ApiModelProperty(value = "已提现")
	private BigDecimal haveWithdrawal;
	/**
	 * 状态说明
	 */
	private String statusName;
    /**
     * 会员手机
     */
	private String phone;
    /**
     * 会员头像
     */
	private String headPortrait;


	/**
     *
	 * 推荐人
	 */
	private String pName;
	/**
	 * 小程序二维码
	 */
	private String address;
	/**
	 * 一级分销
	 */
	private String mlone;
	/**
	 * 二级分销
	 */
	private String mltwo;
	/**
	 *  成员统计
	 */
	private String mlSum;
	/**
	 * 分销级别
	 */
	private String mType;
	/**
	 * 分销人姓名
	 */
	private String mName;
	/**
	 * 冻结金额(可提现金额)
	 */
	private String accountFrozen;
	/**
	 *搜索条件id
	 */
	private String Uid;
	private String createTime_begin;
	private String createTime_end;

	private String updateTime_begin;
	private String updateTime_end;
}
