package org.jeecg.modules.provider.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description: 供应商资金流水
 * @Author: jeecg-boot
 * @Date: 2019-12-18
 * @Version: V1.0
 */
@Data
@TableName("provider_account_capital")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "provider_account_capital对象", description = "供应商资金流水")
public class ProviderAccountCapitalVO {

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改人
     */
    private String updateBy;
    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 创建年
     */
    private Integer year;
    /**
     * 创建月
     */
    private Integer month;
    /**
     * 创建日
     */
    private Integer day;
    /**
     * 删除状态（0，正常，1已删除）
     */
    @TableLogic
    private String delFlag;
    /**
     * 供应商id
     */
    private String sysUserId;
    /**
     * 交易类型；0：订单交易；1：余额提现；2：订单退款；3：余额充值；对应数据字典
     */
    @Dict(dicCode = "provide_deal_type")
    private String payType;
    /**
     * 支付和收入；0：收入；1：支出
     */
    private String goAndCome;
    /**
     * 交易金额
     */
    private BigDecimal amount;
    /**
     * 单号
     */
    private String orderNo;
    /**
     * 余额
     */
    private BigDecimal balance;

    /**
     * 供应商号码
     */
    private String linkPhone;

//    /**
//     * 供应商名称
//     */
//    private String providerName;

    /**
     * 供应商名称
     */
    private String name;

    /**
     * 查询条件开始时间
     */
    private String createTime_begin;
    /**
     * 查询条件开始时间
     */
    private String createTime_end;
    /**
     * 登录账号
     */
    private String userName;

}
