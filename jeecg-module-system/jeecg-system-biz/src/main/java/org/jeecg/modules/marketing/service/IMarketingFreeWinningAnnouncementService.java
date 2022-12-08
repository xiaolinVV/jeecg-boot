package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.entity.MarketingFreeWinningAnnouncement;

import java.util.List;
import java.util.Map;

/**
 * @Description: 免单中奖公告
 * @Author: jeecg-boot
 * @Date:   2021-02-04
 * @Version: V1.0
 */
public interface IMarketingFreeWinningAnnouncementService extends IService<MarketingFreeWinningAnnouncement> {

    /**
     * 获取免单首页免单数据
     *
     * @return
     */
    public List<Map<String,Object>> getMarketingFreeWinningAnnouncementIndex();



    /**
     * 查询页面中奖公告列表
     * 张靠勤   2021-3-19
     * @param page
     * @param paramMap
     * @return
     */
    public IPage<Map<String,Object>> selectMarketingFreeWinningAnnouncementList(Page<Map<String,Object>> page,Map<String,String> paramMap);


    /**
     * 获取中奖公告列表
     *
     * @param page
     * @return
     */
    public IPage<Map<String,Object>> getMarketingFreeWinningAnnouncementList(Page<Map<String,Object>> page);
}
