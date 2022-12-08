package org.jeecg.modules.order.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.order.dto.OrderEvaluateDTO;
import org.jeecg.modules.order.entity.OrderEvaluate;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 平台商品评价
 * @Author: jeecg-boot
 * @Date: 2019-11-12
 * @Version: V1.0
 */
public interface OrderEvaluateMapper extends BaseMapper<OrderEvaluate> {
    /**
     * 根据商品id查询评论列表信息
     *
     * @param page
     * @param paraMap
     * @return
     */
    public IPage<Map<String, Object>> findOrderEvaluateByGoodId(Page<Map<String, Object>> page, @Param("paraMap") Map<String, Object> paraMap);

    /**
     * 评论商品信息集合
     *
     * @param orderListId
     * @return
     */
    List<OrderEvaluateDTO> discussList(@Param("orderListId") String orderListId);

     @Select("select *from  order_evaluate where order_provider_list_id=#{orderProviderListId}")
    List<OrderEvaluateDTO> selectByOderProviderListId(String orderProviderListId);
}
