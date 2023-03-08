package org.jeecg.modules.member.dto;

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
 * @Description: 会员资金流水
 * @Author: jeecg-boot
 * @Date:   2019-12-18
 * @Version: V1.0
 */
@Data
@TableName("member_account_capital")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="member_account_capital对象", description="会员资金流水")
public class MemberAccountCapitalDTO {

	/**主键ID*/
	@TableId(type = IdType.ASSIGN_UUID)
	@ApiModelProperty(value = "主键ID")
	private String id;
	/**创建人*/
	@ApiModelProperty(value = "创建人")
	private String createBy;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "创建时间")
	private Date createTime;
	/**修改人*/
	@ApiModelProperty(value = "修改人")
	private String updateBy;
	/**修改时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "修改时间")
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
	/**会员id*/
	private String memberListId;
	/**交易类型；交易类型；0：分销佣金；1：余额提现,做成数据字典*/
	@Excel(name = "交易类型；0：订单交易；1：余额提现；2：订单退款；3：福利金赠送；4：余额充值；5：归属店铺奖励；6：渠道销售奖励；做成数据字典", width = 15,dicCode = "member_deal_type")
	@ApiModelProperty(value = "交易类型；0：订单交易；1：余额提现；2：订单退款；3：福利金赠送；4：余额充值；5：归属店铺奖励；6：渠道销售奖励；做成数据字典")
	@Dict(dicCode = "member_deal_type")
	private String payType;
	/**支付和收入；0：收入；1：支出*/
	private String goAndCome;
	/**交易金额*/
	private BigDecimal amount;
	/**单号*/
	private String orderNo;
	/**余额*/
	private BigDecimal balance;

	/**新增代码*/
	/**店铺号码*/
	private String phone;
	/**店铺名称*/
	private String nickName;
	/**查询条件开始时间*/
	private String createTime_begin;
	/**查询条件开始时间*/
	private String createTime_end;
	/**归属店铺Id*/
	private String sysUserId;
}
