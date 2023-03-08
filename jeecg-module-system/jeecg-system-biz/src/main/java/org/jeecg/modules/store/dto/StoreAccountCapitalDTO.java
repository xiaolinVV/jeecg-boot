package org.jeecg.modules.store.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;

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
public class StoreAccountCapitalDTO {
    
	/**主键ID*/
	@TableId(type = IdType.ASSIGN_UUID)
	private String id;
	/**创建人*/
	private String createBy;
	/**创建时间*/
	@Excel(name = "交易时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
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
	@Excel(name = "交易类型", width = 15,dicCode = "store_deal_type")
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
	 * 店铺账号
	 */
	private String userName;

}
