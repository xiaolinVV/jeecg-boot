package org.jeecg.modules.agency.vo;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;

import java.util.Date;

/**
 * @Description: 代理资金流水
 * @Author: jeecg-boot
 * @Date:   2019-12-21
 * @Version: V1.0
 */
@Data
@TableName("agency_account_capital")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="agency_account_capital对象", description="代理资金流水")
public class AgencyAccountCapitalVO {

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
	/**代理id*/
	private String sysUserId;
	/**交易类型；0：订单交易；1：提现；2：订单退款；3：余额充值；做成数据字典*/
	@Dict(dicCode = "agency_deal_type")
	private String payType;
	/**支付和收入；0：收入；1：支出*/
	private String goAndCome;
	/**交易金额*/
	private java.math.BigDecimal amount;
	/**单号*/
	private String orderNo;
	/**账号余额*/
	private  String balance;


	/**新增代码*/
	/**号码*/
	private String phone;
	/**代理名称*/
	private String realname;
	/**
	 * 代理账号
	 */
	private String userName;
	/**查询条件开始时间*/
	private String createTime_begin;
	/**查询条件开始时间*/
	private String createTime_end;
}
