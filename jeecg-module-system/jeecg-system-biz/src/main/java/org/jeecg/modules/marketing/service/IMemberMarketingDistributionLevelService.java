package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.entity.MemberMarketingDistributionLevel;

/**
 * @Description: 分销各个级别的人数统计
 * @Author: jeecg-boot
 * @Date:   2021-09-10
 * @Version: V1.0
 */
public interface IMemberMarketingDistributionLevelService extends IService<MemberMarketingDistributionLevel> {


    /**
     * 获取推荐级别的数量
     *
     * @return
     */
    public int getDistributionLevel(String promoter,String marketingDistributionLevelId);

}
