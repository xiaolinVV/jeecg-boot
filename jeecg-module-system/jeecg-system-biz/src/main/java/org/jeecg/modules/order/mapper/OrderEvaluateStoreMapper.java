package org.jeecg.modules.order.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.order.dto.OrderEvaluateStoreDTO;
import org.jeecg.modules.order.entity.OrderEvaluateStore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 店铺评价
 * @Author: jeecg-boot
 * @Date:   2019-11-17
 * @Version: V1.0
 */
public interface OrderEvaluateStoreMapper extends BaseMapper<OrderEvaluateStore> {
    /**
     * 根据商品id查询评论列表信息
     * @param page
     * @param paraMap
     * @return
     */
    public IPage<Map<String,Object>> findOrderEvaluateByGoodId(Page<Map<String,Object>> page, @Param("paraMap") Map<String,Object> paraMap);
    /**
     * 评论商品信息集合
     * @param orderStoreListId
     * @return
     */
    List<OrderEvaluateStoreDTO>   discussList(@Param("orderStoreListId")String orderStoreListId);

}
