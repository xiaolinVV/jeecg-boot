package org.jeecg.modules.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.order.dto.OrderEvaluateDTO;
import org.jeecg.modules.order.entity.OrderEvaluate;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.order.vo.EvaluateVO;

import java.util.List;
import java.util.Map;

/**
 * @Description: 平台商品评价
 * @Author: jeecg-boot
 * @Date:   2019-11-12
 * @Version: V1.0
 */
public interface IOrderEvaluateService extends IService<OrderEvaluate> {


    /**
     * 根据商品id查询评论列表信息
     * @param page
     * @return
     */
    public IPage<Map<String,Object>> findOrderEvaluateByGoodId(Page<Map<String,Object>> page, Map<String,Object> paraMap);

    List<OrderEvaluateDTO> discussList(String orderListId);

    /**
     * 添加评价信息
     * @param evaluateVO
     * @param memberId
     */
    public void addEvaluate(EvaluateVO evaluateVO, String memberId);

}
