package org.jeecg.modules.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.order.dto.OrderProviderListDTO;
import org.jeecg.modules.order.entity.OrderProviderList;
import org.jeecg.modules.order.vo.OrderProviderListVO;

import java.util.List;
import java.util.Map;

/**
 * @Description: 供应商订单列表
 * @Author: jeecg-boot
 * @Date: 2019-11-12
 * @Version: V1.0
 */
public interface OrderProviderListMapper extends BaseMapper<OrderProviderList> {

    List<OrderProviderListDTO> selectorderListId(@Param("orderListId") String orderListId, @Param("sysUserId") String sysUserId, @Param("parentId") String parentId, @Param("notParentId") String notParentId);


    /**
     * 1688对接获取供应商的订单信息
     *
     * @param id
     * @return
     */
    Map<String, Object> getOrderProviderLisById(@Param("id") String id);

    IPage<OrderProviderListDTO> queryPageList(Page<OrderProviderList> page, @Param("orderProviderListVO") OrderProviderListVO orderListVO);

    @Select("select  *from  order_provider_list where  parent_id=#{parentId} ")
    List<OrderProviderListDTO> selectByParentId(String parentId);
}
