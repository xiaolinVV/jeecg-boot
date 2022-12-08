package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.marketing.entity.MarketingGroupIntegralManage;

import java.util.Map;

/**
 * @Description: 拼购管理
 * @Author: jeecg-boot
 * @Date:   2021-06-27
 * @Version: V1.0
 */
public interface MarketingGroupIntegralManageMapper extends BaseMapper<MarketingGroupIntegralManage> {


    /**
     * 获取拼购列表
     * @param page
     * @return
     */
    public IPage<Map<String,Object>> getMarketingGroupIntegralManage(Page<Map<String,Object>> page);

}
