package org.jeecg.modules.system.entity;

import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 总账提现
 * @Author: jeecg-boot
 * @Date:   2021-05-15
 * @Version: V1.0
 */
@Data
@TableName("sys_withdraw_deposit")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="sys_withdraw_deposit对象", description="总账提现")
public class SysWithdrawDeposit {
    
	/**主键ID*/
	@TableId(type = IdType.ASSIGN_UUID)
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
	private String delFlag;
	/**单号*/
	@Excel(name = "单号", width = 15)
    @ApiModelProperty(value = "单号")
	private String orderNo;
	/**取现类型：T1-T+1取现；D1-D+1取现；D0-即时取现。*/
	@Excel(name = "取现类型：T1-T+1取现；D1-D+1取现；D0-即时取现。", width = 15)
    @ApiModelProperty(value = "取现类型：T1-T+1取现；D1-D+1取现；D0-即时取现。")
	private String cashType;
	/**取现金额，必须大于0，保留两位小数点，如0.10、100.05等*/
	@Excel(name = "取现金额，必须大于0，保留两位小数点，如0.10、100.05等", width = 15)
    @ApiModelProperty(value = "取现金额，必须大于0，保留两位小数点，如0.10、100.05等")
	private java.math.BigDecimal cashAmt;
	/**取现成功后的到账金额，值为取现金额 - 取现手续费金额。*/
	@Excel(name = "取现成功后的到账金额，值为取现金额 - 取现手续费金额。", width = 15)
    @ApiModelProperty(value = "取现成功后的到账金额，值为取现金额 - 取现手续费金额。")
	private java.math.BigDecimal realAmt;
	/**取现手续费金额*/
	@Excel(name = "取现手续费金额", width = 15)
    @ApiModelProperty(value = "取现手续费金额")
	private java.math.BigDecimal feeAmt;
	/**状态；0：交易处理中；1：交易成功；2：交易失败*/
	@Excel(name = "状态；0：交易处理中；1：交易成功；2：交易失败", width = 15)
    @ApiModelProperty(value = "状态；0：交易处理中；1：交易成功；2：交易失败")
	private String status;
	/**状态说明*/
	@Excel(name = "状态说明", width = 15)
    @ApiModelProperty(value = "状态说明")
	private String statusExplain;
}
