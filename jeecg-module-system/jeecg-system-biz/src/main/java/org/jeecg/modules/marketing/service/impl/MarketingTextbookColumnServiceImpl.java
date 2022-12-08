package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.marketing.entity.MarketingTextbookColumn;
import org.jeecg.modules.marketing.mapper.MarketingTextbookColumnMapper;
import org.jeecg.modules.marketing.service.IMarketingTextbookColumnService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Description: 教程素材
 * @Author: jeecg-boot
 * @Date:   2020-08-20
 * @Version: V1.0
 */
@Service
public class MarketingTextbookColumnServiceImpl extends ServiceImpl<MarketingTextbookColumnMapper, MarketingTextbookColumn> implements IMarketingTextbookColumnService {

    @Override
    public List<Map<String, Object>> findMarketingTextbookColumn() {
        return baseMapper.findMarketingTextbookColumn();
    }


}
