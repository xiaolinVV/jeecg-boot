package org.jeecg.modules.good.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: 商品分类
 * @Author: jeecg-boot
 * @Date:   2019-10-17
 * @Version: V1.0
 */
@Data
@TableName("good_type")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="good_type对象", description="商品分类")
public class GoodType implements Serializable {

	/**id*/
	@TableId(type = IdType.ASSIGN_UUID)
	@ApiModelProperty(value = "id")
	private String id;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
	@ApiModelProperty(value = "创建人")
	private String createBy;
	/**创建日期*/
	@Excel(name = "创建日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "创建日期")
	private java.util.Date createTime;
	/**更新人*/
	@Excel(name = "更新人", width = 15)
	@ApiModelProperty(value = "更新人")
	private String updateBy;
	/**更新日期*/
	@Excel(name = "更新日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "更新日期")
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
	/**分类名称*/
	@Excel(name = "分类名称", width = 15)
	@ApiModelProperty(value = "分类名称")
	private String name;
	/**分类图片地址*/
	@Excel(name = "分类图片地址", width = 15)
	@ApiModelProperty(value = "分类图片地址")
	private String typePicture;
	/**分类级别；1：一级；2：二级；3：三级*/
	@Excel(name = "分类级别；1：一级；2：二级；3：三级", width = 15)
	@ApiModelProperty(value = "分类级别；1：一级；2：二级；3：三级")
	private String level;
	/**排序*/
	@Excel(name = "排序", width = 15)
	@ApiModelProperty(value = "排序")
	private String sort;
	/**状态；0：停用；1：启用*/
	@Excel(name = "状态；0：停用；1：启用", width = 15)
	@ApiModelProperty(value = "状态；0：停用；1：启用")
	private String status;
	/**父级分类id，顶级为空*/
	@Excel(name = "父级分类id，顶级为空", width = 15)
	@ApiModelProperty(value = "父级分类id，顶级为空")
	private String parentId;
	/**是否有子节点*/
	@Excel(name = "是否有子节点", width = 15)
	@ApiModelProperty(value = "是否有子节点")
	private String hasChild;
	/**停用说明*/
	@Excel(name = "停用说明", width = 15)
	@ApiModelProperty(value = "停用说明")
	private String stopRemark;


	@TableField(exist = false)
	private List<GoodType> children;

}
