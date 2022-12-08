package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.marketing.entity.MemberMarketingDistributionLevel;

import java.util.Map;

/**
 * @Description: 分销各个级别的人数统计
 * @Author: jeecg-boot
 * @Date:   2021-09-10
 * @Version: V1.0
 */
public interface MemberMarketingDistributionLevelMapper extends BaseMapper<MemberMarketingDistributionLevel> {

    /**
     * 获取推荐级别的数量
     *
     * @param paramMap
     * @return
     */
    public int getDistributionLevel(@Param("paramMap") Map<String,Object> paramMap);

}
