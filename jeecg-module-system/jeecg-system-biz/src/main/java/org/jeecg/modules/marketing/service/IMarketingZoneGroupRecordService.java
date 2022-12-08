package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.entity.MarketingZoneGroupRecord;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 拼团记录
 * @Author: jeecg-boot
 * @Date:   2021-07-23
 * @Version: V1.0
 */
public interface IMarketingZoneGroupRecordService extends IService<MarketingZoneGroupRecord> {

    IPage<Map<String,Object>> queryPageList(Page<Map<String,Object>> page, QueryWrapper<MarketingZoneGroupRecord> queryWrapper, Map<String, Object> requestMap);

    /**
     * 获取首页中奖数据
     *
     * @return
     */
    public List<Map<String,Object>> getIndexByStatus();

    List<Map<String,Object>> getZoneGroupManageMemberList(String id);

    IPage<Map<String,Object>> getMarketingZoneGroupRecordByMemberId(Page<Map<String,Object>> page, HashMap<String, Object> map);

    Map<String,Object> getMarketingZoneGroupRecordDetails(MarketingZoneGroupRecord marketingZoneGroupRecord);


    /**
     * 根据用户id获取单天拼的次数
     *
     * @param memberId
     * @return
     */
    public int getCountByMemberId(String memberId,String marketingZoneGroupId);

    /**
     * 获取需要清算的拼购记录
     *
     * @return
     */
    public List<Map<String,Object>> recordsLiquidation();


    /**
     * 分配奖励
     *
     * @param marketingZoneGroupRecordId
     */
    public void distributionRewards(String marketingZoneGroupRecordId);

    /**
     * 分级奖励
     * @param marketingZoneGroupRecordId
     */
    public void classificationReward(String marketingZoneGroupRecordId);

    /**
     * 获取会员上一天的拼购次数
     *
     * @param memberId
     * @return
     */
    public int getRecordCount(String memberId,String id,String mgimi);



    /**
     * 获取拼中的人
     *
     * @param marketingZoneGroupManageId
     * @return
     */
    public Map<String,Object> getWinning(String marketingZoneGroupManageId);


}
