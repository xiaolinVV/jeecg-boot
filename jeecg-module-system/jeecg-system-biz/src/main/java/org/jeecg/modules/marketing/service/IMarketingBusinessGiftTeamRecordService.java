package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.dto.MarketingBusinessGiftTeamRecordDTO;
import org.jeecg.modules.marketing.entity.MarketingBusinessGiftTeamRecord;

import java.util.Map;

/**
 * @Description: 团队记录
 * @Author: jeecg-boot
 * @Date:   2021-10-16
 * @Version: V1.0
 */
public interface IMarketingBusinessGiftTeamRecordService extends IService<MarketingBusinessGiftTeamRecord> {


    /**
     * 后台团队记录查询
     *
     * @param page
     * @param marketingBusinessGiftTeamRecordDTO
     * @return
     */
    public IPage<Map<String,Object>> queryPageList(Page<Map<String,Object>> page,MarketingBusinessGiftTeamRecordDTO marketingBusinessGiftTeamRecordDTO);

}
