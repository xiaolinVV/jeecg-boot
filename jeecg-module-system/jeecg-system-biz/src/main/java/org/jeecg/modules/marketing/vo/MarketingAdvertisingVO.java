package org.jeecg.modules.marketing.vo;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.util.Date;

/**
 * @Description: 广告管理
 * @Author: jeecg-boot
 * @Date:   2019-10-10
 * @Version: V1.0
 */
@Data
@TableName("marketing_advertising")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="marketing_advertising对象", description="广告管理")
public class MarketingAdvertisingVO {
    
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
	/**平台广告为空，店铺广告填写角色问店铺的商家*/
	private String sysUserId;
	/**广告标题*/
	private String title;
	/**广告类型；建立数据字典读取，字典名称：ad_type；0:为平台广告；1：店铺广告*/
	@Excel(name = "广告类型；建立数据字典读取，字典名称：ad_type；0:为平台广告；1：店铺广告", width = 15)
	@Dict(dicCode = "ad_type")
	private String adType;
	/**广告位置；建立数据字典读取，字典名称：ad_location；0:首页；1：分类*/
	@Dict(dicCode = "ad_location")
	private String adLocation;
	/**图片地址*/
	private String pictureAddr;
	/**跳转类型；0：无；1：商品详情*/
	private String goToType;
	/**如果广告类型为0代表平台商品id，如果为1代表店铺商品的id*/
	private String goodListId;
	/**排序*/
	private Integer sort;
	/**开始时间*/
	private Date startTime;
	/**结束时间*/
	private Date endTime;
	/**状态：0：停用；1：启用*/
	private String status;
	/**删除状态（0，正常，1已删除）*/
	@TableLogic
	private String delFlag;

	/*新增内容**/
    //商品名称
	private String goodName;
	/**查询条件创建时间*/
	private String createTime_begin;
	/**查询条件创建时间*/
	private String createTime_end;
	/**查询条件开始时间*/
	private String startTime_begin;
	/**查询条件开始时间*/
	private String startTime_end;
	/**查询条件创建时间*/
	private String endTime_begin;
	/**查询条件创建时间*/
	private String endTime_end;
	/**详情图*/
	private String pictureDetails;
	/**素材标题*/
	private String marketingMaterialTitle;
}
