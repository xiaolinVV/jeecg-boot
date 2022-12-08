package org.jeecg.modules.system.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class SysWorkbenchVO {
    //id
    private String id;
    /**
     *头像
     */
    private String avatar;
    /**
     *联系人
     */
    private String realname;
    /**
     * 公司名称
     */
    private String name;
    /**
     *角色
     */
    private String roleName;
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
     *已认证店铺
     */
    private String storeSum;
    /**
     *供应商
     */
    private String providerSum;
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

    private List<Map<String,Object>> memberList;//会员数据(统计普通会员和vip)
    private List<Map<String,Object>> memberSexList;//会员数据(统计性别)
    //树状图的数据
    private List<Map<String,Object>> earningsList;
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
    /**
     * * 首页树状图时间
     */
    private String myDate;
    /**
     * 首页树状图当日金额
     */
    private BigDecimal totalPrice;
    //累计收益时间集合
    private List<String> earningTimeList;
    /**
     * 树状图累计收益
     */
    private BigDecimal earningsPatch;
    /**
     * 饼状图会员数
     */
    private Long memberPatch;

    private String sysUserId;
}
