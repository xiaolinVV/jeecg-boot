package org.jeecg.modules.order.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.modules.order.dto.OrderEvaluateDTO;
import org.jeecg.modules.order.entity.*;
import org.jeecg.modules.order.mapper.OrderEvaluateMapper;
import org.jeecg.modules.order.service.IOrderEvaluateService;
import org.jeecg.modules.order.service.IOrderListService;
import org.jeecg.modules.order.service.IOrderProviderGoodRecordService;
import org.jeecg.modules.order.service.IOrderProviderListService;
import org.jeecg.modules.order.vo.EvaluateVO;
import org.jeecg.modules.order.vo.GoodVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: 平台商品评价
 * @Author: jeecg-boot
 * @Date: 2019-11-12
 * @Version: V1.0
 */
@Service
public class OrderEvaluateServiceImpl extends ServiceImpl<OrderEvaluateMapper, OrderEvaluate> implements IOrderEvaluateService {

    @Autowired
    private OrderEvaluateMapper orderEvaluateMapper;
    @Autowired
    private IOrderProviderGoodRecordService iOrderProviderGoodRecordService;
    @Autowired
    private IOrderProviderListService iOrderProviderListService;
    @Autowired
    @Lazy
    private IOrderListService iOrderListService;

    @Override
    public IPage<Map<String, Object>> findOrderEvaluateByGoodId(Page<Map<String, Object>> page, Map<String, Object> paraMap) {
        return orderEvaluateMapper.findOrderEvaluateByGoodId(page, paraMap);
    }

    @Override
    public List<OrderEvaluateDTO> discussList(String orderListId) {
        return orderEvaluateMapper.discussList(orderListId);
    }

    ;

    /**
     * 添加评价信息
     *
     * @param evaluateVO
     * @param memberId
     */
    public void addEvaluate(EvaluateVO evaluateVO, String memberId) {
        //修改订单信息
        OrderList orderList = iOrderListService.getById(evaluateVO.getId());

        orderList.setLogisticsStar(new BigDecimal(evaluateVO.getLogisticsStar()));
        orderList.setShippingStar(new BigDecimal(evaluateVO.getShippingStar()));
        orderList.setServiceStar(new BigDecimal(evaluateVO.getServiceStar()));
        orderList.setEvaluateTime(new Date());
        orderList.setIsEvaluate("1");
        iOrderListService.updateById(orderList);
        JSONArray jsonArray = JSONArray.parseArray(evaluateVO.getGoods());
        List<GoodVO> goodVOList = jsonArray.toJavaList(GoodVO.class);
        goodVOList.forEach(goodVO -> {
            OrderProviderGoodRecord orderProviderGoodRecord = iOrderProviderGoodRecordService.getById(goodVO.getId());
            if (orderProviderGoodRecord != null) {
                OrderEvaluate orderEvaluate = new OrderEvaluate();
                orderEvaluate.setDelFlag("0");
                orderEvaluate.setOrderListId(orderList.getId());//店铺订单id
                orderEvaluate.setOrderProviderListId(orderProviderGoodRecord.getOrderProviderListId());//供应商订单id
                orderEvaluate.setMemberListId(memberId);  //会员列表id
                orderEvaluate.setGoodListId(orderProviderGoodRecord.getGoodListId());//店铺商品id
                orderEvaluate.setGoodSpecificationId(orderProviderGoodRecord.getGoodSpecificationId());//店铺商品规格id
                orderEvaluate.setContent(goodVO.getContent());//评价内容
                if (StringUtils.isNotBlank(goodVO.getPictures())) {
                    orderEvaluate.setPictures(goodVO.getPictures());//评价图片
                }

                orderEvaluate.setDescriptionStar(new BigDecimal(goodVO.getDescriptionStar()));//评价星级
                orderEvaluateMapper.insert(orderEvaluate);
            }

        });
    }
}
