package org.jeecg.modules.marketing.api;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.marketing.entity.MarketingZoneGroupGrouping;
import org.jeecg.modules.marketing.entity.MarketingZoneGroupOrder;
import org.jeecg.modules.marketing.service.IMarketingZoneGroupGroupingService;
import org.jeecg.modules.marketing.service.IMarketingZoneGroupOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("after/marketingZoneGroupOrder")
@Controller
@Slf4j
public class AfterMarketingZoneGroupOrderController {

    @Autowired
    private IMarketingZoneGroupOrderService iMarketingZoneGroupOrderService;

    @Autowired
    private IMarketingZoneGroupGroupingService iMarketingZoneGroupGroupingService;

    /**
     * 拼中商品列表
     *
     * @param memberId
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("findMarketingZoneGroupGroupingByMemberId")
    @ResponseBody
    public Result<?> findMarketingZoneGroupGroupingByMemberId(@RequestAttribute(required = false, name = "memberId") String memberId,
                                                              @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                              @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        return Result.ok(iMarketingZoneGroupGroupingService.findMarketingZoneGroupGroupingByMemberId(new Page<Map<String, Object>>(pageNo, pageSize), memberId));
    }

    /**
     * 拼中商品订单列表
     *
     * @param id
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("getMarketingZoneGroupOrderPageByGroupingId")
    @ResponseBody
    public Result<?> getMarketingZoneGroupOrderPageByGroupingId(String id,
                                                                @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                                @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        return Result.ok(iMarketingZoneGroupOrderService.getMarketingZoneGroupOrderPageByGroupingId(new Page<Map<String, Object>>(pageNo, pageSize), id));
    }

    /**
     * 拼中商品订单详情
     * @param id
     * @return
     */
    @RequestMapping("getMarketingZoneGroupOrderDetails")
    @ResponseBody
    public Result<?> getMarketingZoneGroupOrderDetails(String id){
        if (StringUtils.isBlank(id)) {
            return Result.error("前端id未传递!");
        }
        MarketingZoneGroupOrder marketingZoneGroupOrder = iMarketingZoneGroupOrderService.getById(id);
        if (marketingZoneGroupOrder != null){
            return Result.ok(iMarketingZoneGroupOrderService.getMarketingZoneGroupOrderDetails(marketingZoneGroupOrder));
        }else {
            return Result.error("id传递有误,未找到对应的记录");
        }
    }

    /**
     * 催单
     * @param id
     * @return
     */
    @RequestMapping("orderReminder")
    @ResponseBody
    public Result<?> orderReminder(String id) {
        if (StringUtils.isBlank(id)) {
            return Result.error("前端id未传递!");
        }
        MarketingZoneGroupOrder marketingZoneGroupOrder = iMarketingZoneGroupOrderService.getById(id);
        if (marketingZoneGroupOrder!=null){
            boolean b = iMarketingZoneGroupOrderService.updateById(marketingZoneGroupOrder.setStatus("2"));
            if (b) {
                return Result.ok(marketingZoneGroupOrder.getMarketingZoneGroupGroupingId());
            } else {
                return Result.error("失败");
            }
        }else {
            return Result.error("id传递有误,未找到对应的记录");
        }

    }

    /**
     * 寄售
     * @param id
     * @return
     */
    @RequestMapping("orderConsignment")
    @ResponseBody
    public Result<?> orderConsignment(String id) {
        if (StringUtils.isBlank(id)) {
            return Result.error("前端id未传递!");
        }
        MarketingZoneGroupOrder marketingZoneGroupOrder = iMarketingZoneGroupOrderService.getById(id);
        MarketingZoneGroupGrouping marketingZoneGroupGrouping=iMarketingZoneGroupGroupingService.getById(marketingZoneGroupOrder.getMarketingZoneGroupGroupingId());
        if(iMarketingZoneGroupOrderService.count(new LambdaQueryWrapper<MarketingZoneGroupOrder>().eq(MarketingZoneGroupOrder::getMarketingZoneGroupGroupingId,marketingZoneGroupGrouping.getId()))<marketingZoneGroupGrouping.getTransformationThreshold().intValue()){
            return Result.error("未达到转化值："+marketingZoneGroupGrouping.getTransformationThreshold()+"不可寄售！！！");
        }
        if (marketingZoneGroupOrder!=null){
            boolean b = iMarketingZoneGroupOrderService.updateById(marketingZoneGroupOrder.setStatus("3").setConsignmentTime(new Date()));
            if (b) {
                return Result.ok(marketingZoneGroupOrder.getMarketingZoneGroupGroupingId());
            } else {
                return Result.error("失败");
            }
        }else {
            return Result.error("id传递有误,未找到对应的记录");
        }
    }

    /**
     * 一键寄售
     * @param id
     * @return
     */
    @RequestMapping("oneKeyConsignment")
    @ResponseBody
    public Result<?> oneKeyConsignment(String id) {
        if (StringUtils.isBlank(id)) {
            return Result.error("前端id未传递!");
        }
        MarketingZoneGroupGrouping marketingZoneGroupGrouping = iMarketingZoneGroupGroupingService.getById(id);
        if(iMarketingZoneGroupOrderService.count(new LambdaQueryWrapper<MarketingZoneGroupOrder>().eq(MarketingZoneGroupOrder::getMarketingZoneGroupGroupingId,marketingZoneGroupGrouping.getId()))<marketingZoneGroupGrouping.getTransformationThreshold().intValue()){
            return Result.error("未达到转化值："+marketingZoneGroupGrouping.getTransformationThreshold()+"不可寄售！！！");
        }
        if (marketingZoneGroupGrouping!=null){
            return Result.error("功能暂未开放");
        }else {
            return Result.error("id传递有误,未找到对应的记录");
        }
    }

    /**
     * 一键寄售判断
     * @param id
     * @return
     */
    @RequestMapping("isViewOneKeyConsignment")
    @ResponseBody
    public Result<?> isViewOneKeyConsignment(String id){
        if (StringUtils.isBlank(id)) {
            return Result.error("前端id未传递!");
        }

        MarketingZoneGroupGrouping marketingZoneGroupGrouping = iMarketingZoneGroupGroupingService.getById(id);
        if (marketingZoneGroupGrouping != null){
            HashMap<String, Object> map = new HashMap<>();
            map.put("canConsignment",marketingZoneGroupGrouping.getCanConsignment());
            return Result.ok(map);
        }else {
            return Result.error("id传递有误,未找到对应的记录");
        }
    }
}
