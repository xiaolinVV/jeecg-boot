package org.jeecg.modules.marketing.store.prefecture.entity;

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
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Description: 店铺专区记录
 * @Author: 靠勤网络
 * @Date:   2022-12-10
 * @Version: V1.0
 */
@Data
@TableName("marketing_store_prefecture_record")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="marketing_store_prefecture_record对象", description="店铺专区记录")
public class MarketingStorePrefectureRecord {
    
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
	/**会员列表id*/
	@Excel(name = "会员列表id", width = 15)
    @ApiModelProperty(value = "会员列表id")
	private String memberListId;
	/**商品主图*/
	@Excel(name = "商品主图", width = 15)
    @ApiModelProperty(value = "商品主图")
	private String mainPicture;
	/**商品名称*/
	@Excel(name = "商品名称", width = 15)
    @ApiModelProperty(value = "商品名称")
	private String goodName;
	/**商品编号*/
	@Excel(name = "商品编号", width = 15)
    @ApiModelProperty(value = "商品编号")
	private String goodNo;
	/**金额*/
	@Excel(name = "金额", width = 15)
    @ApiModelProperty(value = "金额")
	private java.math.BigDecimal price;
	/**赠送说明*/
	@Excel(name = "赠送说明", width = 15)
    @ApiModelProperty(value = "赠送说明")
	private String give;
	/**专区id*/
	@Excel(name = "专区id", width = 15)
    @ApiModelProperty(value = "专区id")
	private String marketingStorePrefectureListId;
	/**专区商品id*/
	@Excel(name = "专区商品id", width = 15)
    @ApiModelProperty(value = "专区商品id")
	private String marketingStorePrefectureGoodId;
	/**订单id*/
	@Excel(name = "订单id", width = 15)
    @ApiModelProperty(value = "订单id")
	private String orderStoreListId;

	private String TMemberId;
}
