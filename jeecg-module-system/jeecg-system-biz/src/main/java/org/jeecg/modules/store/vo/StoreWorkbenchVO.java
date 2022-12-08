package org.jeecg.modules.store.vo;

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
public class StoreWorkbenchVO {
    /**
     * id
     */
    private String id;
    /**
     *头像
     */
    private String logoAddr;
    /**
     *联系人
     */
    private String bossName;
    /**
     *店铺名称
     */
    private String storeName;
    /**
     *角色
     */
    private String roleName;
    @Excel(name = "认证状态：0：待审核；1：已认证；2：免认证；3：未通过；4：过期；", width = 15,dicCode = "store_attestation_status")
    @ApiModelProperty(value = "认证状态：0：待审核；1：已认证；2：免认证；3：未通过；4：过期；")
    @Dict(dicCode = "store_attestation_status")
    private String attestationStatus;
    /**
     * 开通
     */
    private String openUp;
    /**
     *状态
     */
    private String statusName;
    /**
     *有效订单
     */
    private String orderSum;
    /**
     *累计收入
     */
    private String earnings;
    /**
     *累计会员
     */
    private String memberSum;
    /**
     *已送福利金
     */
    private String welfareSum;
    /**
     *待发货订单
     */
    private String waitDeliverGoods;
    /**
     *在售商品
     */
    private String forGoods;
    /**
     *已被领取优惠券
     */
    private String byGetDiscount;
    /**
     *已被使用优惠券
     */
    private String byUseDiscount;
    //累计收益时间开始
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date earningTime_begin;
    //累计收益时间结束
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date earningTime_end;
    //统计会员数时间开始
    private String memberTime_begin;
    //统计会员数时间结束
    private String memberTime_end;
    //累计收益时间集合
    private List<String> earningTimeList;
    //统计会员数时间集合
    private List<String> memberTimeList;
    private List<Map<String,Object>> memberList;//会员数据(统计普通会员和vip)
    private List<Map<String,Object>> memberSexList;//会员数据(统计性别)
    private List<Map<String,Object>> earningsList;//收益数据
    /**
     * 树状图累计收益
     */
    private BigDecimal earningsPatch;
    /**
     * 饼状图会员数
     */
    private Long memberPatch;
    //店铺userid
    private String sysUserId;
}
