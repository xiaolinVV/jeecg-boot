package org.jeecg.modules.member.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class MemberListDTO {
    private static final long serialVersionUID = 1L;
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
    private Date createTime;
    /**修改人*/
    @Excel(name = "修改人", width = 15)
    @ApiModelProperty(value = "修改人")
    private String updateBy;
    /**修改时间*/
    @Excel(name = "修改时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;
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
    @TableLogic//mybatis-plus配置逻辑
    private String delFlag;
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
    /**性别；参照数据字典sex*/
    @Excel(name = "性别；参照数据字典sex", width = 15)
    @ApiModelProperty(value = "性别；参照数据字典sex")
    private String sex;
    /**地区*/
    @Excel(name = "地区", width = 15)
    @ApiModelProperty(value = "地区")
    private String areaAddr;
    /**会员类型；建立会员类型数据字典*/
    @Excel(name = "会员类型；建立会员类型数据字典", width = 15,dicCode = "member_type")
    @ApiModelProperty(value = "会员类型；建立会员类型数据字典")
    @Dict(dicCode = "member_type")
    private String memberType;
    /**福利金*/
    @Excel(name = "福利金", width = 15)
    @ApiModelProperty(value = "福利金")
    private BigDecimal welfarePayments;
    /**余额*/
    @Excel(name = "余额", width = 15)
    @ApiModelProperty(value = "余额")
    private BigDecimal balance;
    /**是否开店；0：否；1：是*/
    @Excel(name = "是否开店；0：否；1：是", width = 15)
    @ApiModelProperty(value = "是否开店；0：否；1：是")
    private String isOpenStore;
    /**加入vip的时间*/
    @Excel(name = "加入vip的时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "加入vip的时间")
    private Date vipTime;
    /**状态：0：停用：1：启用*/
    @Excel(name = "状态：0：停用：1：启用", width = 15)
    @ApiModelProperty(value = "状态：0：停用：1：启用")
    private String status;
    @Excel(name = "停用说明", width = 15)
    @ApiModelProperty(value = "停用说明")
    private String stopRemark;
    @Excel(name = "微信的openid", width = 15)
    @ApiModelProperty(value = "微信的openid")
    private String openid;
    @Excel(name = "微信使用", width = 15)
    @ApiModelProperty(value = "微信使用")
    private String sessionKey;
    @Excel(name = "推广人类型;0:店铺；1：会员；2：平台", width = 15)
    @ApiModelProperty(value = "推广人类型;0:店铺；1：会员；2：平台")
    private String promoterType;
    @Excel(name = "店铺或者会员id", width = 15)
    @ApiModelProperty(value = "店铺或者会员id")
    private String promoter;
    @Excel(name = "归属店铺id", width = 15)
    @ApiModelProperty(value = "归属店铺id")
    private String sysUserId;
    @Excel(name = "推广二维码地址", width = 15)
    @ApiModelProperty(value = "推广二维码地址")
    private String promotionQrCode;
    @Excel(name = "累计佣金", width = 15)
    @ApiModelProperty(value = "累计佣金")
    private BigDecimal totalCommission;
    @Excel(name = "已提现", width = 15)
    @ApiModelProperty(value = "已提现")
    private BigDecimal haveWithdrawal;
    @Excel(name = "冻结金额", width = 15)
    @ApiModelProperty(value = "冻结金额")
    private BigDecimal accountFrozen;
    @Excel(name = "不可用金额", width = 15)
    @ApiModelProperty(value = "不可用金额")
    private BigDecimal unusableFrozen;
    /**
     *可用券
     */
    private String discount;
    /**
     *商品收藏
     */
    private String goodsCollection;
    /**
     *店铺关注
     */
    private String attentionStore;
    /**
     *浏览记录
     */
    private String browsingHistory;
    /**
     *是否开店
     */
    private String isOpenStores;
    /**
     *归属店铺
     */
    private String storeName ;

    //有无归属店铺
    private String isStore;
    /**
     *状态
     */
    private String statusName;
    /**
     * 推广人
     */
    private String promoterName;
    /**
     * 普通会员统计
     */
    private String ordinarySum;
    /**
     * Vip会员统计
     */
    private String vipSum;
    /**
     * 男会员
     */
    private String memberMan;
    /**
     * 女会员
     */
    private String memberWoMan;
    /**
     * 未知性别
     */
    private String memberUnknown;

    private String isHaveStore;

    /**
     * 饼状图会员数
     */
    private Long memberPatch;

    private List<Map<String,Object>> memberList;//会员数据(统计普通会员和vip)
    private List<Map<String,Object>> memberSexList;//会员数据(统计性别)

    /**
     * 用户个人二维码地址
     */
    private String qrcodeAddr;

    @Excel(name = "冻结福利金", width = 15)
    @ApiModelProperty(value = "冻结福利金")
    private BigDecimal welfarePaymentsFrozen;
    @Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String remark;
    @Excel(name = "不可用福利金", width = 15)
    @ApiModelProperty(value = "不可用福利金")
    private BigDecimal welfarePaymentsUnusable;
    @Excel(name = "会员等级id", width = 15)
    @ApiModelProperty(value = "会员等级id")
    private String memberGradeId;

    @Excel(name = "成长值", width = 15)
    @ApiModelProperty(value = "成长值")
    private BigDecimal growthValue;
    @Excel(name = "会员称号id", width = 15)
    @ApiModelProperty(value = "会员称号id")
    private String memberDesignationId;
    /**查询条件开始时间*/
    @Excel(name = "查询条件开始时间", width = 15)
    private String createTime_begin;
    /**查询条件开始时间*/
    @Excel(name = "查询条件结束时间", width = 15)
    private String createTime_end;
    /**查询条件开始时间*/
    @Excel(name = "查询条件开始时间", width = 15)
    private String vipTime_begin;
    /**查询条件开始时间*/
    @Excel(name = "查询条件结束时间", width = 15)
    private String vipTime_end;

    private String designationName;
    /**
     * 称号团队名称
     */
    private String groupName;
}
