package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.entity.MarketingBusinessDesignation;

/**
 * @Description: 创业团队称号管理
 * @Author: jeecg-boot
 * @Date:   2021-07-30
 * @Version: V1.0
 */
public interface IMarketingBusinessDesignationService extends IService<MarketingBusinessDesignation> {

    /**
     * 会员升级
     *
     * @param memberId
     */
    public void upgrade(String memberId,String tMemberId);

}
