package org.jeecg.modules.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.order.dto.OrderProviderListDTO;
import org.jeecg.modules.order.entity.OrderProviderList;
import org.jeecg.modules.order.vo.OrderProviderListVO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Description: 供应商订单列表
 * @Author: jeecg-boot
 * @Date:   2019-11-12
 * @Version: V1.0
 */
public interface IOrderProviderListService extends IService<OrderProviderList> {



    List<OrderProviderListDTO> selectorderListId(String orderListId,String sysUserId,String parentId,String notParentId);

    /**
     * 浏览信息查询
     *
     * @param orderProviderId
     * @return
     */
    String listSkip(String orderProviderId);

    /**
     * 1688对接获取供应商的订单列表信息
     *
     * @param id
     * @return
     */
    Map<String, Object> getOrderProviderLisById(String id);;

    IPage<OrderProviderListDTO> queryPageList(Page<OrderProviderList> page, OrderProviderListVO orderListVO, String userId) throws IllegalAccessException;


    /**
     * 1688发货
     * @param taobaoOrderId
     */
    public void taobaoShipments(String taobaoOrderId);

    List<OrderProviderListDTO> selectByParentId(String orderListId);

    void ShipmentOrderModification(OrderProviderList orderProviderList);

    /**
     * 根据订单id查询供应商信息和商品信息
     *新增：张靠勤   2021-3-18
     *
     * @param orderListId
     * @return
     */
    public List<Map<String,Object>> getOrderProviderListAndGoodListByOrderId(String orderListId);


    /**
     * 1688下单接口
     * @param orderProvideListId
     * @return
     */
    public Map<String, BigDecimal> placeOrder(String orderProvideListId);


    /**
     * 1688补单接口
     * @param orderProviderGoodRecordId
     * @return
     */
    public Map<String, BigDecimal> replenishment(String orderProviderGoodRecordId);
}
