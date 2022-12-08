package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.entity.MarketingLeagueBuyerRecord;
import org.jeecg.modules.order.entity.OrderList;

import java.util.Map;

/**
 * @Description: 加盟专区-购买记录
 * @Author: jeecg-boot
 * @Date:   2021-12-25
 * @Version: V1.0
 */
public interface IMarketingLeagueBuyerRecordService extends IService<MarketingLeagueBuyerRecord> {


    /**
     * 资金分配
     * @param orderList
     */
    public void allocation(OrderList orderList);


    /**
     * 查询购买记录
     *
     * @param page
     * @param paramMap
     * @return
     */
    public IPage<Map<String,Object>> queryPageList(Page<Map<String,Object>> page,Map<String,Object> paramMap);

}
