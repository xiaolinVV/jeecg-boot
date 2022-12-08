package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import org.jeecg.modules.marketing.entity.MemberMarketingDistributionLevel;
import org.jeecg.modules.marketing.mapper.MemberMarketingDistributionLevelMapper;
import org.jeecg.modules.marketing.service.IMemberMarketingDistributionLevelService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Description: 分销各个级别的人数统计
 * @Author: jeecg-boot
 * @Date:   2021-09-10
 * @Version: V1.0
 */
@Service
public class MemberMarketingDistributionLevelServiceImpl extends ServiceImpl<MemberMarketingDistributionLevelMapper, MemberMarketingDistributionLevel> implements IMemberMarketingDistributionLevelService {

    @Override
    public int getDistributionLevel(String promoter, String marketingDistributionLevelId) {
        Map<String,Object> paramMap= Maps.newHashMap();
        paramMap.put("promoter",promoter);
        paramMap.put("marketingDistributionLevelId",marketingDistributionLevelId);
        return baseMapper.getDistributionLevel(paramMap);
    }
}
