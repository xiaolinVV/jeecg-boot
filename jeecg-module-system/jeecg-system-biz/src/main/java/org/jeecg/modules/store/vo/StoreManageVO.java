package org.jeecg.modules.store.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecg.modules.system.entity.SysArea;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class StoreManageVO {
    private static final long serialVersionUID = 1L;
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
    /**关联店铺用户id，审核通过后创建店铺账号进行关联*/
    @Excel(name = "关联店铺用户id，审核通过后创建店铺账号进行关联", width = 15)
    @ApiModelProperty(value = "关联店铺用户id，审核通过后创建店铺账号进行关联")
    private String sysUserId;
    /**门店名称*/
    @Excel(name = "门店名称", width = 15)
    @ApiModelProperty(value = "门店名称")
    private String storeName;
    /**分店名称*/
    @Excel(name = "分店名称", width = 15)
    @ApiModelProperty(value = "分店名称")
    private String subStoreName;
    /**logo图片地址*/
    @Excel(name = "logo图片地址", width = 15)
    @ApiModelProperty(value = "logo图片地址")
    private String logoAddr;
    /**门脸照图片地址*/
    @Excel(name = "门脸照图片地址", width = 15)
    @ApiModelProperty(value = "门脸照图片地址")
    private String storePicture;
    /**区域id*/
    @Excel(name = "区域id", width = 15)
    @ApiModelProperty(value = "区域id")
    private String sysAreaId;
    /**城市区域地址说明*/
    @Excel(name = "城市区域地址说明", width = 15)
    @ApiModelProperty(value = "城市区域地址说明")
    private String areaAddress;
    /**门店详细地址*/
    @Excel(name = "门店详细地址", width = 15)
    @ApiModelProperty(value = "门店详细地址")
    private String storeAddress;
    /**开通类型：0：包年；1：终生*/
    @Excel(name = "开通类型：0：包年；1：终生", width = 15,dicCode = "store_open_type")
    @ApiModelProperty(value = "开通类型：0：包年；1：终生")
    @Dict(dicCode = "store_open_type")
    private String openType;
    /**开通费用*/
    @Excel(name = "开通费用", width = 15)
    @ApiModelProperty(value = "开通费用")
    private BigDecimal money;
    /**主营分类；建立数据库字典：store_main_type对应*/
    @Excel(name = "主营分类；建立数据库字典：store_main_type对应", width = 15,dicCode = "store_main_type")
    @ApiModelProperty(value = "主营分类；建立数据库字典：store_main_type对应")
    @Dict(dicCode = "store_main_type")
    private String mainType;
    /**次营分类；建立数据库字典：store_next_type对应*/
    @Excel(name = "次营分类；建立数据库字典：store_next_type对应", width = 15,dicCode = "store_next_type")
    @ApiModelProperty(value = "次营分类；建立数据库字典：store_next_type对应")
    @Dict(dicCode = "store_next_type")
    private String nextType;
    /**综合评分*/
    @Excel(name = "综合评分", width = 15)
    @ApiModelProperty(value = "综合评分")
    private BigDecimal comprehensiveEvaluation;
    /**老板姓名（联系人名称）*/
    @Excel(name = "老板姓名（联系人名称）", width = 15)
    @ApiModelProperty(value = "老板姓名（联系人名称）")
    private String bossName;
    /**老板手机（联系人手机号，是登录账号）*/
    @Excel(name = "老板手机（联系人手机号，是登录账号）", width = 15)
    @ApiModelProperty(value = "老板手机（联系人手机号，是登录账号）")
    private String bossPhone;
    /**客服电话*/
    @Excel(name = "客服电话", width = 15)
    @ApiModelProperty(value = "客服电话")
    private String takeOutPhone;
    /**经度*/
    @Excel(name = "经度", width = 15)
    @ApiModelProperty(value = "经度")
    private String longitude;
    /**纬度*/
    @Excel(name = "纬度", width = 15)
    @ApiModelProperty(value = "纬度")
    private String latitude;
    /**开通时间*/
    @Excel(name = "开通时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "开通时间")
    private Date startTime;
    /**到期时间，如果是终生就为空*/
    @Excel(name = "到期时间，如果是终生就为空", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "到期时间，如果是终生就为空")
    private Date endTime;
    /**状态：0:停用；1：启用*/
    @Excel(name = "状态：0:停用；1：启用", width = 15)
    @ApiModelProperty(value = "状态：0:停用；1：启用")
    private String status;
    @Excel(name = "状态", width = 15)
    private String statusName;
    /**认证状态：0：待审核；1：已认证；2：免认证；3：未通过；4：过期；*/
    @Excel(name = "认证状态：0：待审核；1：已认证；2：免认证；3：未通过；4：过期；", width = 15,dicCode = "store_attestation_status")
    @ApiModelProperty(value = "认证状态：0：待审核；1：已认证；2：免认证；3：未通过；4：过期；")
    @Dict(dicCode = "store_attestation_status")
    private String attestationStatus;
    /**备注：未通过原因和其他的一些说明*/
    @Excel(name = "备注：未通过原因和其他的一些说明", width = 15)
    @ApiModelProperty(value = "备注：未通过原因和其他的一些说明")
    private String remark;
    /**企业营业执照图片地址*/
    @Excel(name = "企业营业执照图片地址", width = 15)
    @ApiModelProperty(value = "企业营业执照图片地址")
    private String licenseForEnterprise;
    /**统一信用代码*/
    @Excel(name = "统一信用代码", width = 15)
    @ApiModelProperty(value = "统一信用代码")
    private String socialCreditCode;
    /**身份证号码*/
    @Excel(name = "身份证号码", width = 15)
    @ApiModelProperty(value = "身份证号码")
    private String idCode;
    /**身份证正面照片地址*/
    @Excel(name = "身份证正面照片地址", width = 15)
    @ApiModelProperty(value = "身份证正面照片地址")
    private String idPictureZ;
    /**身份证反面照片地址*/
    @Excel(name = "身份证反面照片地址", width = 15)
    @ApiModelProperty(value = "身份证反面照片地址")
    private String idPictureF;
    /**删除状态（0，正常，1已删除）*/
    @Excel(name = "删除状态（0，正常，1已删除）", width = 15)
    @ApiModelProperty(value = "删除状态（0，正常，1已删除）")
    @TableLogic //mybatis-plus配置逻辑
    private String delFlag;
    /**经办人类型：0：代办人；1：个人；2：法人*/
    @Excel(name = "经办人类型：0：代办人；1：个人；2：法人", width = 15,dicCode = "store_agent_type")
    @ApiModelProperty(value = "经办人类型：0：代办人；1：个人；2：法人")
    @Dict(dicCode = "store_agent_type")
    private String agentType;
    /**经办人姓名*/
    @Excel(name = "经办人姓名", width = 15)
    @ApiModelProperty(value = "经办人姓名")
    private String agentName;
    /**手持身份证照片地址*/
    @Excel(name = "手持身份证照片地址", width = 15)
    @ApiModelProperty(value = "手持身份证照片地址")
    private String idHand;
    /**授权书图片地址*/
    @Excel(name = "授权书图片地址", width = 15)
    @ApiModelProperty(value = "授权书图片地址")
    private String agentAuthorization;
    /**支付状态：0：未支付；1：已支付*/
    @Excel(name = "支付状态：0：未支付；1：已支付", width = 15)
    @ApiModelProperty(value = "支付状态：0：未支付；1：已支付")
    private String payStatus;
    /**支付类型：0：微信；1：支付宝*/
    @Excel(name = "支付类型：0：微信；1：支付宝", width = 15)
    @ApiModelProperty(value = "支付类型：0：微信；1：支付宝")
    private String payType;
    /**支付时间*/
    @Excel(name = "支付时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "支付时间")
    private Date payTime;
    /**评分*/
    @Excel(name = "评分", width = 15)
    @ApiModelProperty(value = "评分")
    private BigDecimal grade;
    /**店内照*/
    @Excel(name = "店内照", width = 15)
    @ApiModelProperty(value = "店内照")
    private String accordingStore;
    /**主体类型：0:个体；1：企业或者个体户*/
    @Excel(name = "主体类型：0:个体；1：企业或者个体户", width = 15,dicCode = "store_straight")
    @ApiModelProperty(value = "主体类型：0:个体；1：企业或者个体户")
    @Dict(dicCode = "store_straight")
    private String straight;
    @Excel(name = "服务距离", width = 15)
    @ApiModelProperty(value = "服务距离")
    private BigDecimal serviceRange;
    @Excel(name = "到店自提状态; 0：停用；1:启用",width = 15)
    @ApiModelProperty(value = "到店自提状态")
    private String pickUpStatus;
    @Excel(name = "自提备注",width = 15)
    @ApiModelProperty(value = "自提备注")
    private String pickUpRemark;
    @Excel(name = "配送方式；建立字典来做对应配送方式",width = 15,dicCode = "store_distribution_type")
    @ApiModelProperty(value = "配送方式")
    @Dict(dicCode = "store_distribution_type")
    private String distributionType;
    @Excel(name = "配送计费规则",width = 15)
    @ApiModelProperty(value = "配送计费规则")
    private String accountingRules;
    @Excel(name = "配送状态状态；0：停用；1:启用",width = 15)
    @ApiModelProperty(value = "配送状态状态")
    private String distributionStatus;
    @Excel(name = "自提停用说明",width = 15)
    @ApiModelProperty(value = "自提停用说明")
    private String pickUpStopRemark;
    @Excel(name = "配送停用说明",width = 15)
    @ApiModelProperty(value = "配送停用说明")
    private String distributionStopRemark;
    @Excel(name = "区域",width = 15)
    @ApiModelProperty(value = "区域")
    private List<SysArea>sysAreaList;
    @Excel(name = "店铺福利金",width = 15)
    @ApiModelProperty(value = "店铺福利金")
    private BigDecimal welfarePayments;
    @Excel(name = "店铺余额",width = 15)
    @ApiModelProperty(value = "店铺余额")
    private BigDecimal balance;
    @Excel(name = "停用说明",width = 15)
    @ApiModelProperty(value = "停用说明")
    private String closeExplain;
    @Excel(name = "冻结金额(待结算)",width = 15)
    @ApiModelProperty(value = "冻结金额(待结算)")
    private BigDecimal accountFrozen;
    @Excel(name = "不可用金额",width = 15)
    @ApiModelProperty(value = "不可用金额")
    private BigDecimal unusableFrozen;
    @Excel(name = "支付日志",width = 15)
    @ApiModelProperty(value = "支付日志")
    private String payParam;
    @Excel(name = "回调状态；0：未回调；1：已回调",width = 15)
    @ApiModelProperty(value = "回调状态；0：未回调；1：已回调")
    private String backStatus;
    @Excel(name = "回调次数",width = 15)
    @ApiModelProperty(value = "回调次数")
    private BigDecimal backTimes;
    @Excel(name = "支付前的用户信息",width = 15)
    @ApiModelProperty(value = "支付前的用户信息")
    private String memberListId;
    @Excel(name = "二维码表",width = 15)
    @ApiModelProperty(value = "二维码表")
    private String sysSmallcodeId;
    @Excel(name = "同城配送类型",width = 15)
    @ApiModelProperty(value = "同城配送类型")
    private String cityDistributionType;
    @Excel(name = "送福利金；0：不显示；1：显示",width = 15)
    @ApiModelProperty(value = "送福利金；0：不显示；1：显示")
    private String ifViewWelfarePayments;
    @Excel(name = "店铺会员价；0：停用；1：启用",width = 15)
    @ApiModelProperty(value = "店铺会员价；0：停用；1：启用")
    private String isViewVipPrice;
    /**商品审核*/
    @Excel(name = "商品审核", width = 15)
    @ApiModelProperty(value = "商品审核")
    private Boolean goodAudit;
    /**店铺类型: 0:联盟店 1:独立店*/
    @Excel(name = "店铺类型: 0:联盟店 1:独立店", width = 15)
    @ApiModelProperty(value = "店铺类型: 0:联盟店 1:独立店")
    private String storeType;
    /**
     * 是否连锁: 0:否 1:是
     */
    @Excel(name = "是否连锁: 0:否 1:是", width = 15)
    @ApiModelProperty(value = "是否连锁: 0:否 1:是")
    private Boolean isChain;
    /**
     * 配送计费规则拼接
     */
    private StringBuilder accountingName;
    //店铺二维码
    private String address;
    //数据权限使用id
    private String Uid;

    /**
     * 登录账号
     */
    private String userName;
    //所在城市
    private String placeDispatch;
    //省级代理UserId
    @ApiModelProperty(value = "省级代理UserId")
    private String ProvincialId;
    //市级代理UserId
    @ApiModelProperty(value = "市级代理UserId")
    private String municipalId;
    //县级代理UserId
    @ApiModelProperty(value = "县级代理UserId")
    private String countyId;

    @ApiModelProperty(value = "微信的appId")
    private String appId;
    @ApiModelProperty(value = "微信的：appSecret")
    private String appSecret;
    @ApiModelProperty(value = "微信的：mchId")
    private String mchId;
    @ApiModelProperty(value = "微信的：partnerKey")
    private String partnerKey;
    @ApiModelProperty(value = "营业时间说明")
    private String businessHoursExplain;
    @Excel(name = "推广人类型;0:店铺；1：会员；2：平台:3:加盟商", width = 15)
    @ApiModelProperty(value = "推广人类型;0:店铺；1：会员；2：平台:3:加盟商")
    private String promoterType;
    @Excel(name = "推广人", width = 15)
    @ApiModelProperty(value = "推广人")
    private String promoter;
    @Excel(name = "归属加盟商", width = 15)
    @ApiModelProperty(value = "归属加盟商")
    private String allianceUserId;
    @Excel(name = "是否开启福利金收款；0：关闭；1：开启", width = 15)
    @ApiModelProperty(value = "是否开启福利金收款；0：关闭；1：开启")
    private String isOpenWelfarePayments;
    /**
     * 登录密码
     */
    private String password;
    /**
     * 确认密码
     */
    private String confirmPassword;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 验证码
     */
    private String code;
    /**
     * 新手机
     */
    private String newPhone;
    /**
     * 新手机验证码
     */
    private String newPhoneCode;
    /**
     * 经纬度
     */
    private String longitudeAndLatitude;
    //推广人名称
    private String promoterName;

    private String allianceName;
    @Excel(name = "排序", width = 15)
    @ApiModelProperty(value = "排序")
    private BigDecimal sort;
    @Excel(name = "是否推荐；0：否；1：是", width = 15)
    @ApiModelProperty(value = "是否推荐；0：否；1：是")
    private String isRecommend;
    @Excel(name = "介绍", width = 15)
    @ApiModelProperty(value = "介绍")
    private String introduce;
}
