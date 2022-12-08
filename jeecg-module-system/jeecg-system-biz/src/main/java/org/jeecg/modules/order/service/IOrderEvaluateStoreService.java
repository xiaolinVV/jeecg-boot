package org.jeecg.modules.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.order.dto.OrderEvaluateStoreDTO;
import org.jeecg.modules.order.entity.OrderEvaluateStore;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.order.vo.EvaluateVO;

import java.util.List;
import java.util.Map;

/**
 * @Description: 店铺评价
 * @Author: jeecg-boot
 * @Date:   2019-11-17
 * @Version: V1.0
 */
public interface IOrderEvaluateStoreService extends IService<OrderEvaluateStore> {

    /**
     * 根据商品id查询评论列表信息
     * @param page
     * @return
     */
    public IPage<Map<String,Object>> findOrderEvaluateByGoodId(Page<Map<String,Object>> page, Map<String,Object> paraMap);

    /**
     * 评论商品信息集合
     * @param orderStoreListId
     * @return
     */
   public  List<OrderEvaluateStoreDTO> discussList(String orderStoreListId);
    /**
     * 添加评价信息
     * @param evaluateVO
     * @param memberId
     */
    public void addEvaluate(EvaluateVO  evaluateVO,String memberId);
}
