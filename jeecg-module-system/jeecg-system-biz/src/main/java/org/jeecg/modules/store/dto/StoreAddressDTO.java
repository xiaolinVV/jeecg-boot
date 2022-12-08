package org.jeecg.modules.store.dto;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @Description: 地址库
 * @Author: jeecg-boot
 * @Date:   2019-10-21
 * @Version: V1.0
 */
@Data
@TableName("store_address")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="store_address对象", description="地址库")
public class StoreAddressDTO {
    
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
	/**区域id*/
	private String sysAreaId;
	/**所在城市*/
	private String areaAddress;
	/**详细地址*/
	private String detailedAddress;
	/**是否默认发货地址；0：否；1：是*/
	private String isDeliver;
	/**是否默认退货地址；0：否；1：是*/
	private String isReturn;
	/**联系人姓名*/
	private String contactName;
	/**联系人手机号*/
	private String contactPhone;
	/**联系电话*/
	private String contactTelephone;
	/**********添加字段***********/
	/**区域名称*/
	private String sysAreaName;
	/**供应商名称*/
	private String sysUserName;
}
