package org.jeecg.modules.marketing.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class MemberDistributionDTO {
    /**头像绝对地址*/
    @Excel(name = "头像绝对地址", width = 15)
    @ApiModelProperty(value = "头像绝对地址")
    private String headPortrait;
    /**手机号*/
    @Excel(name = "手机号", width = 15)
    @ApiModelProperty(value = "手机号")
    private String phone;
    /**会员昵称*/
    @Excel(name = "会员昵称", width = 15)
    @ApiModelProperty(value = "会员昵称")
    private String nickName;
    /**会员类型；建立会员类型数据字典*/
    @Excel(name = "会员类型；建立会员类型数据字典", width = 15,dicCode = "member_type")
    @ApiModelProperty(value = "会员类型；建立会员类型数据字典")
    @Dict(dicCode = "member_type")
    private String memberType;
    /**
     * 成员级别
     */
    private String mType;
    /**创建时间*/
    @Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    /**
     * 推荐人
     */
    private String mName;
}
