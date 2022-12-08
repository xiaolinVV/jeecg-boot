package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.entity.MarketingLiveLotteryRecord;

import java.util.Map;

/**
 * @Description: 直播管理-中奖记录
 * @Author: jeecg-boot
 * @Date:   2021-09-15
 * @Version: V1.0
 */
public interface IMarketingLiveLotteryRecordService extends IService<MarketingLiveLotteryRecord> {

    IPage<Map<String,Object>> queryPageList(Page<Map<String,Object>> page, QueryWrapper<MarketingLiveLotteryRecord> queryWrapper, Map<String, Object> requestMap);

    IPage<Map<String,Object>> getMarketingLiveLotteryRecordList(Page<Map<String,Object>> page,Map<String, Object> map);

    Map<String,Object> runLottery(String memberId, long duration, String marketingLiveLotteryId);
}
