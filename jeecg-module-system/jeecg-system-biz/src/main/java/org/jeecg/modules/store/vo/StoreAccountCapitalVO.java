package org.jeecg.modules.store.vo;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description: 店铺资金流水
 * @Author: jeecg-boot
 * @Date:   2019-12-11
 * @Version: V1.0
 */
@Data
@TableName("store_account_capital")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="store_account_capital对象", description="店铺资金流水")
public class StoreAccountCapitalVO {
    
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
	/**店铺管理id*/
	private String storeManageId;
	/**交易类型；0：订单交易；1：余额提现；2：订单退款；3：福利金赠送；4：余额充值；5：归属店铺奖励；6：渠道销售奖励；做成数据字典*/
	@Dict(dicCode = "store_deal_type")
	private String payType;
	/**支付和收入；0：收入；1：支出*/
	private String goAndCome;
	/**交易金额*/
	private BigDecimal amount;
	/**单号*/
	private String orderNo;
	/**账号余额*/
    private BigDecimal balance;

	/**新增代码*/
	/**店铺号码*/
	private String bossPhone;
	/**店铺名称*/
	private String storeName;
	/**查询条件开始时间*/
	private String createTime_begin;
	/**查询条件开始时间*/
	private String createTime_end;
	/**店铺Id*/
	private String sysUserId;
	/**
	 * 首页树状图当日金额
	 */
	private BigDecimal totalPrice;
	/**
	 * 首页树状图时间
	 */
	private String mydate;
	//店铺账号
	private String userName;

}
