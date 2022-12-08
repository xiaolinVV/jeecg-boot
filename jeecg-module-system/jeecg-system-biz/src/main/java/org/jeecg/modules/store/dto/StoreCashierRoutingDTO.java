package org.jeecg.modules.store.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Description: 店铺收银分账设置
 * @Author: jeecg-boot
 * @Date:   2022-06-06
 * @Version: V1.0
 */
@Data
public class StoreCashierRoutingDTO {

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
	private java.util.Date createTime;
	/**修改人*/
	@Excel(name = "修改人", width = 15)
    @ApiModelProperty(value = "修改人")
	private String updateBy;
	/**修改时间*/
	@Excel(name = "修改时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
	private java.util.Date updateTime;
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
	/**礼包列表id*/
	@Excel(name = "礼包列表id", width = 15)
    @ApiModelProperty(value = "礼包列表id")
	private String storeManageId;
	/**账户类型；（0：对私；1：对公）*/
	@Excel(name = "账户类型；（0：对私；1：对公）", width = 15)
    @ApiModelProperty(value = "账户类型；（0：对私；1：对公）")
	private String accountType;
	/**银行卡号*/
	@Excel(name = "银行卡号", width = 15)
    @ApiModelProperty(value = "银行卡号")
	private String bankCard;
	/**真实姓名*/
	@Excel(name = "真实姓名", width = 15)
    @ApiModelProperty(value = "真实姓名")
	private String realName;
	/**身份证号*/
	@Excel(name = "身份证号", width = 15)
    @ApiModelProperty(value = "身份证号")
	private String idNumber;
	/**手机号*/
	@Excel(name = "手机号", width = 15)
    @ApiModelProperty(value = "手机号")
	private String phone;
	/**汇付天下分账结算账户id*/
	@Excel(name = "汇付天下分账结算账户id", width = 15)
    @ApiModelProperty(value = "汇付天下分账结算账户id")
	private String settleAccount;
	/**分账比例*/
	@Excel(name = "分账比例", width = 15)
    @ApiModelProperty(value = "分账比例")
	private java.math.BigDecimal fashionableProportion;
	private String isStore;

	private String fashionableWay;

	private String label;

	private String fashionableType;

	private String memberListId;

	private String nickName;
}
