package org.jeecg.modules.provider.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class ProviderWorkbenchVO {
    //id
    private String id;
    /**
     *头像
     */
    private String avatar;
    /**
     *联系人
     */
    private String linkman;
    /**
     *公司名称
     */
    private String name;
    /**
     *角色
     */
    private String roleName;
    /**
     *状态
     */
    private String status;
    /**
     *状态说明
     */
    private String statusName;
    /**
     *有效订单
     */
    private String orderSum;
    /**
     *累计收益
     */
    private String earnings;
    /**
     *待发货订单
     */
    private String waitDeliverGoods;
    /**
     *在售商品
     */
    private String forGoods;
    /**
     * 供应商UserId
     */
    private String sysUserId;
    //累计收益时间开始
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date earningTime_begin;
    //累计收益时间结束
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date earningTime_end;
    //累计收益时间集合
    private List<String> earningTimeList;
    //收益数据
    private List<Map<String,Object>> earningsList;
    /**
     * 树状图累计收益
     */
    private BigDecimal earningsPatch;
}
