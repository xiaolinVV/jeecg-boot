package org.jeecg.modules.order.service;

import org.jeecg.modules.order.dto.OrderStoreSubListDTO;
import org.jeecg.modules.order.entity.OrderStoreSubList;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 店铺包裹订单列表
 * @Author: jeecg-boot
 * @Date:   2019-11-30
 * @Version: V1.0
 */
public interface IOrderStoreSubListService extends IService<OrderStoreSubList> {
    /**
     * 根据orderStoreListId查询列表
     * sysUserId 数据权限使用
     * @param orderStoreListId
     * @param sysUserId
     * @return
     */
    List<OrderStoreSubListDTO> selectorderStoreListId(String orderStoreListId, String sysUserId);

    List<OrderStoreSubListDTO> selectorderStoreListId(String orderStoreListId,String sysUserId,String parentId,String notParentId);
    public String listSkip(String orderId);

}
