package org.jeecg.modules.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 省市区县实体
 * @Author: jeecg-boot
 * @Date:   2019-10-13
 * @Version: V1.0
 */
@Data
@TableName("sys_area")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="sys_area对象", description="省市区县")
public class SysArea {
    
	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
	private Integer id;
	/**parentId*/
	@Excel(name = "parentId", width = 15)
    @ApiModelProperty(value = "parentId")
	private Integer parentId;
	/**name*/
	@Excel(name = "name", width = 15)
    @ApiModelProperty(value = "name")
	private String name;
	/**leve*/
	@Excel(name = "leve", width = 15)
    @ApiModelProperty(value = "leve")
	private Integer leve;
	/**代理Id*/
	@Excel(name = "agencyManageId", width = 15)
	@ApiModelProperty(value = "agencyManageId")
	private String  agencyManageId;
	/**腾讯地图Id*/
	@Excel(name = "tengXunId", width = 15)
	@ApiModelProperty(value = "tengXunId")
	private String tengXunId;
	private String pinyin;
	private String fullname;
}
