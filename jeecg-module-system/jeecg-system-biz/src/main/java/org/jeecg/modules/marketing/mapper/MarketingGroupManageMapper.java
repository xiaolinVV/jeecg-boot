package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.marketing.entity.MarketingGroupManage;

import java.util.List;
import java.util.Map;

/**
 * @Description: 拼团管理
 * @Author: jeecg-boot
 * @Date:   2021-03-23
 * @Version: V1.0
 */
public interface MarketingGroupManageMapper extends BaseMapper<MarketingGroupManage> {

    /**
     * 获取成团进行中的成团商品数据
     *
     * 张靠勤  2021-4-8
     *
     * @return
     */
    public List<Map<String,Object>> getMarketingGroupManageByGood();

}
