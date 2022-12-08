package org.jeecg.modules.order.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.modules.order.dto.OrderEvaluateStoreDTO;
import org.jeecg.modules.order.entity.OrderEvaluateStore;
import org.jeecg.modules.order.entity.OrderStoreGoodRecord;
import org.jeecg.modules.order.entity.OrderStoreList;
import org.jeecg.modules.order.mapper.OrderEvaluateStoreMapper;
import org.jeecg.modules.order.service.IOrderEvaluateStoreService;
import org.jeecg.modules.order.service.IOrderStoreGoodRecordService;
import org.jeecg.modules.order.service.IOrderStoreListService;
import org.jeecg.modules.order.vo.EvaluateVO;
import org.jeecg.modules.order.vo.GoodVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: 店铺评价
 * @Author: jeecg-boot
 * @Date:   2019-11-17
 * @Version: V1.0
 */
@Service
public class OrderEvaluateStoreServiceImpl extends ServiceImpl<OrderEvaluateStoreMapper, OrderEvaluateStore> implements IOrderEvaluateStoreService {

    @Autowired
    private IOrderStoreListService iOrderStoreListService;
    @Autowired
    private IOrderStoreGoodRecordService iOrderStoreGoodRecordService;

    @Override
    public IPage<Map<String, Object>> findOrderEvaluateByGoodId(Page<Map<String, Object>> page, Map<String, Object> paraMap) {
        return baseMapper.findOrderEvaluateByGoodId(page,paraMap);
    }

    @Override
    public List<OrderEvaluateStoreDTO> discussList(String orderStoreListId){
        return baseMapper.discussList(orderStoreListId);
    };

    /**
     * 添加评价信息
     * @param evaluateVO
     */
    @Override
    public void addEvaluate(EvaluateVO  evaluateVO,String memberId){
        //修改订单信息
        OrderStoreList orderStoreList=iOrderStoreListService.getById(evaluateVO.getId());
        orderStoreList.setLogisticsStar(new BigDecimal(evaluateVO.getLogisticsStar()));
        orderStoreList.setShippingStar(new BigDecimal(evaluateVO.getShippingStar()));
        orderStoreList.setServiceStar(new BigDecimal(evaluateVO.getServiceStar()));
        orderStoreList.setEvaluateTime(new Date());
        orderStoreList.setIsEvaluate("1");
        iOrderStoreListService.updateById(orderStoreList);
        JSONArray jsonArray = JSONArray.parseArray(evaluateVO.getGoods());
        List<GoodVO> goodVOList = jsonArray.toJavaList(GoodVO.class);
        goodVOList.forEach(goodVO -> {
            OrderStoreGoodRecord orderStoreGoodRecord  =  iOrderStoreGoodRecordService.getById(goodVO.getId());
            if(orderStoreGoodRecord!=null){
                OrderEvaluateStore orderEvaluateStore = new OrderEvaluateStore();
                orderEvaluateStore.setDelFlag("0");
                orderEvaluateStore.setOrderStoreListId(orderStoreList.getId());//店铺订单id
                orderEvaluateStore.setMemberListId(memberId);  //会员列表id
                orderEvaluateStore.setGoodStoreListId(orderStoreGoodRecord.getGoodStoreListId()) ;//店铺商品id
                orderEvaluateStore.setGoodStoreSpecificationId(orderStoreGoodRecord.getGoodStoreSpecificationId());//店铺商品规格id
                orderEvaluateStore.setContent(goodVO.getContent());//评价内容
                if(StringUtils.isNotBlank(goodVO.getPictures())){
                    orderEvaluateStore.setPictures(goodVO.getPictures());//评价图片
                }
                orderEvaluateStore.setDescriptionStar(new BigDecimal(goodVO.getDescriptionStar()));//评价星级
                orderEvaluateStore.setSysUserId(orderStoreList.getSysUserId());//店铺id
               this.saveOrUpdate(orderEvaluateStore);

            }

        });

    }

}
