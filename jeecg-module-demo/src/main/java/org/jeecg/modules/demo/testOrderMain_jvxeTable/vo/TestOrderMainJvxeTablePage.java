package org.jeecg.modules.demo.testOrderMain_jvxeTable.vo;

import java.util.List;
import org.jeecg.modules.demo.testOrderMain_jvxeTable.entity.TestOrderMainJvxeTable;
import org.jeecg.modules.demo.testOrderMain_jvxeTable.entity.TestOrderProductJvxeTable;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelEntity;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 测试订单主表,jvxe风格
 * @Author: jeecg-boot
 * @Date:   2022-11-17
 * @Version: V1.0
 */
@Data
@ApiModel(value="test_order_mainPage对象", description="测试订单主表,jvxe风格")
public class TestOrderMainJvxeTablePage {

	/**主键*/
	@ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**创建人*/
	@ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
	/**更新人*/
	@ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
	/**订单编码*/
	@Excel(name = "订单编码", width = 15)
	@ApiModelProperty(value = "订单编码")
    private java.lang.String orderCode;
	/**下单时间*/
	@Excel(name = "下单时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "下单时间")
    private java.util.Date orderDate;
	/**描述*/
	@Excel(name = "描述", width = 15)
	@ApiModelProperty(value = "描述")
    private java.lang.String descc;
	/**下拉多选*/
	@Excel(name = "下拉多选", width = 15, dicCode = "sex")
    @Dict(dicCode = "sex")
	@ApiModelProperty(value = "下拉多选")
    private java.lang.String xiala;

	@ExcelCollection(name="订单产品明细")
	@ApiModelProperty(value = "订单产品明细")
	private List<TestOrderProductJvxeTable> testOrderProductJvxeTableList;

}
