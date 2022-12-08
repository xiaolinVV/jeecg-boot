package org.jeecg.modules.member.entity;

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
 * @Description: 购物车商品
 * @Author: jeecg-boot
 * @Date:   2019-10-30
 * @Version: V1.0
 */
@Data
@TableName("member_shopping_cart")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="member_shopping_cart对象", description="购物车商品")
public class MemberShoppingCart {
    
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
	/**会员列表id*/
	@Excel(name = "会员列表id", width = 15)
    @ApiModelProperty(value = "会员列表id")
	private String memberListId;
	/**商品的类型;0:店铺商品；1：平台商品*/
	@Excel(name = "商品的类型;0:店铺商品；1：平台商品", width = 15)
    @ApiModelProperty(value = "商品的类型;0:店铺商品；1：平台商品")
	private String goodType;
	/**平台商品id，可以为空*/
	@Excel(name = "平台商品id，可以为空", width = 15)
    @ApiModelProperty(value = "平台商品id，可以为空")
	private String goodListId;
	/**店铺商品id，可以为空*/
	@Excel(name = "店铺商品id，可以为空", width = 15)
    @ApiModelProperty(value = "店铺商品id，可以为空")
	private String goodStoreListId;
	/**加入时间*/
	@Excel(name = "加入时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "加入时间")
	private java.util.Date addTime;
	/**加入时价格*/
	@Excel(name = "加入时价格", width = 15)
    @ApiModelProperty(value = "加入时价格")
	private java.math.BigDecimal addPrice;
	/**店铺商品规格id*/
	@Excel(name = "店铺商品规格id", width = 15)
    @ApiModelProperty(value = "店铺商品规格id")
	private String goodStoreSpecificationId;
	/**商品规格id*/
	@Excel(name = "商品规格id", width = 15)
    @ApiModelProperty(value = "商品规格id")
	private String goodSpecificationId;

	/**商品数量id*/
	@Excel(name = "商品数量", width = 15)
	@ApiModelProperty(value = "商品数量")
	private java.math.BigDecimal quantity;
	@Excel(name = "是否显示；0：不显示；1：显示", width = 15)
	@ApiModelProperty(value = "是否显示；0：不显示；1：显示")
	private String isView;
	/**专区id*/
	@Excel(name = "专区id", width = 15)
	@ApiModelProperty(value = "专区id")
	private String marketingPrefectureId;
	/**专区标签*/
	@Excel(name = "专区标签", width = 15)
	@ApiModelProperty(value = "专区标签")
	private String prefectureLabel;

	/**
	 * 兑换券记录id
	 */
	private String marketingCertificateRecordId;
	/**
	 * 免单商品id
	 */
	private String marketingFreeGoodListId;

	/**
	 * 拼团记录id
	 */
	private String marketingGroupRecordId;


	/**店铺用户id*/
	private String sysUserId;

	/**
	 * 会员礼品卡列表id
	 */
	private String marketingStoreGiftCardMemberListId;

	private String marketingRushGroupId;

	private String marketingLeagueGoodListId;
}
