package org.jeecg.modules.marketing.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.system.base.entity.JeecgEntity;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 创业出账资金
 * @Author: jeecg-boot
 * @Date:   2021-08-11
 * @Version: V1.0
 */
@Data
@TableName("marketing_business_make_money_record")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="marketing_business_make_money_record对象", description="创业出账资金")
public class MarketingBusinessMakeMoneyRecordVO extends JeecgEntity {
    
	/**主键ID*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键ID")
	private String id;
	/**流水号*/
	@Excel(name = "流水号", width = 15)
    @ApiModelProperty(value = "流水号")
	private String serialNumber;
	/**出账资金id*/
	@Excel(name = "出账资金id", width = 15)
    @ApiModelProperty(value = "出账资金id")
	private String marketingBusinessMakeMoneyId;
	/**会员id*/
	@Excel(name = "会员id", width = 15)
    @ApiModelProperty(value = "会员id")
	private String memberListId;
	/**金额*/
	@Excel(name = "金额", width = 15)
    @ApiModelProperty(value = "金额")
	private java.math.BigDecimal amount;
	/**手机号*/
	@Excel(name = "手机号", width = 15)
	@ApiModelProperty(value = "手机号")
	private String phone;
	/**会员昵称*/
	@Excel(name = "会员昵称", width = 15)
	@ApiModelProperty(value = "会员昵称")
	private String nickName;
}
