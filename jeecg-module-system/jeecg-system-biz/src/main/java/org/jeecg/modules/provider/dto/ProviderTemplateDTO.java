package org.jeecg.modules.provider.dto;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import org.jeecg.modules.system.entity.SysArea;

import java.util.Date;
import java.util.List;

@Data
public class ProviderTemplateDTO {
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
    /**供应商id*/
    private String sysUserId;
    /**模板名称*/
    private String name;
    /**发货地id*/
    private String sysAreaId;
    /**发货地描述*/
    private String placeDispatch;
    /**包邮配送区域json存储，存储区域id*/
    private String exemptionPostage;
    /**计费方式；0：按件数计费；1：按重量计费*/
    private String chargeMode;
    /**付邮费区域json描述，制作人过来问我，需要实体配合*/
    private String mailDelivery;
    /**模板类型；0：包邮；1：自定义*/
    private String templateType;
    private String isTemplate;
    private String delExplain;
    //地区集合
    private List<SysArea> listsysArea;
    //用户名
    private String realname;
}
