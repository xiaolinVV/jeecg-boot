package org.jeecg.modules.order.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @Description: 店铺评价
 * @Author: jeecg-boot
 * @Date:   2019-11-17
 * @Version: V1.0
 */
@Data
@TableName("order_evaluate_store")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="order_evaluate_store对象", description="店铺评价")
public class OrderEvaluateStoreDTO {
    
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

	private String delFlag;
	/**店铺订单id*/
	private String orderStoreListId;
	/**会员列表id*/
	private String memberListId;
	/**店铺商品id*/
	private String goodStoreListId;
	/**店铺商品规格id*/
	private String goodStoreSpecificationId;
	/**评价内容*/
	private String content;
	/**评价图片*/
	private String pictures;
	/**评价星级*/
	private java.math.BigDecimal descriptionStar;
	/**店铺id*/
	private String sysUserId;
	/**状态：0：未审核；1：审核通过；2：审核不通过*/
	private String status;
	/**不通过原因*/
	private String closeExplain;
	/**审核时间*/
	private Date auditTime;
	/**新增数据***/
	/**商品主图*/
	private String mainPicture;
	/**商品名称*/
	private String goodName;
	/**商品规格名称*/
	private String specification;

}
