package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.entity.MarketingFreeAnnouncement;

import java.util.Map;

/**
 * @Description: 免单公告
 * @Author: jeecg-boot
 * @Date:   2021-02-04
 * @Version: V1.0
 */
public interface IMarketingFreeAnnouncementService extends IService<MarketingFreeAnnouncement> {

    /**
     * 免单公告列表查询
     *
     * @param page
     * @return
     */
    public IPage<Map<String,Object>> selectMarketingFreeAnnouncement(Page<Map<String,Object>> page);


    /**
     * 查询免单公告详情
     * @param marketingFreeAnnouncementId
     * @return
     */
    public Map<String,Object> getMarketingFreeAnnouncementById(String marketingFreeAnnouncementId);

}
