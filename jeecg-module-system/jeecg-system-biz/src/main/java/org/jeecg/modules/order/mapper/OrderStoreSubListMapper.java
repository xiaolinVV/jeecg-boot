package org.jeecg.modules.order.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.order.dto.OrderStoreSubListDTO;
import org.jeecg.modules.order.entity.OrderStoreSubList;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 店铺包裹订单列表
 * @Author: jeecg-boot
 * @Date:   2019-11-30
 * @Version: V1.0
 */
public interface OrderStoreSubListMapper extends BaseMapper<OrderStoreSubList> {
    /**
     * 根据orderStoreListId查询列表
     * sysUserId 数据权限使用
     * @param orderStoreListId
     * @param sysUserId
     * @return
     */
    List<OrderStoreSubListDTO> selectorderStoreListId(@Param("orderStoreListId")String orderStoreListId, @Param("sysUserId")String sysUserId);
    /**
     * 根据orderStoreListId查询列表
     * sysUserId 数据权限使用
     * @param orderStoreListId
     * @param sysUserId
     * @return
     */
    List<OrderStoreSubListDTO> selectorderStoreListId(@Param("orderStoreListId")String orderStoreListId, @Param("sysUserId")String sysUserId,@Param("parentId")String parentId,@Param("notParentId")String notParentId);

}
