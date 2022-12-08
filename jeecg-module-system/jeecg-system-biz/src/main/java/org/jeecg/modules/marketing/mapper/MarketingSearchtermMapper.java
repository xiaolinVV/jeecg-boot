package org.jeecg.modules.marketing.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.marketing.entity.MarketingSearchterm;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 推荐搜词
 * @Author: jeecg-boot
 * @Date:   2019-10-11
 * @Version: V1.0
 */
public interface MarketingSearchtermMapper extends BaseMapper<MarketingSearchterm> {
    //查询搜索词集合
    public List<MarketingSearchterm> getmarketingSearchtermList();
}
