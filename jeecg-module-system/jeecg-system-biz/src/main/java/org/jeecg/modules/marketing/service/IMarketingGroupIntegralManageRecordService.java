package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.marketing.dto.MarketingGroupIntegralManageRecordDTO;
import org.jeecg.modules.marketing.entity.MarketingGroupIntegralManageRecord;

import java.util.List;
import java.util.Map;

/**
 * @Description: 拼购记录
 * @Author: jeecg-boot
 * @Date:   2021-06-28
 * @Version: V1.0
 */
public interface IMarketingGroupIntegralManageRecordService extends IService<MarketingGroupIntegralManageRecord> {

    /**
     * 拼团记录列表
     *
     * @param page
     * @param marketingGroupIntegralManageRecordDTO
     * @return
     */
    public IPage<Map<String,Object>> queryPageList(Page<Map<String,Object>> page, @Param("marketingGroupIntegralManageRecordDTO") MarketingGroupIntegralManageRecordDTO marketingGroupIntegralManageRecordDTO);



    /**
     * 根据拼团列表id获取拼团记录
     *
     * @param marketingGroupIntegralManageListId
     * @return
     */
    public IPage<Map<String,Object>> getByMarketingGroupIntegralManageListId(Page<Map<String,Object>> page,String marketingGroupIntegralManageListId);


    /**
     * 根据拼团列表id获取拼团记录
     *
     * @param marketingGroupIntegralManageListId
     * @return
     */
    public List<Map<String,Object>> getByMarketingGroupIntegralManageListId(String marketingGroupIntegralManageListId);


    /**
     * 获取前10条   成功的拼团记录
     * @return
     */
    public List<Map<String,Object>> getWinningState();


    /**
     * 分配奖励
     *
     * @param marketingGroupIntegralManageRecordId
     */
    public void distributionRewards(String marketingGroupIntegralManageRecordId);


    /**
     * 分级奖励
     * @param marketingGroupIntegralManageRecordId
     */
    public void classificationReward(String marketingGroupIntegralManageRecordId);


    /**
     * 获取用户拼购成团没结算的数据
     *
     * @param memberId
     * @return
     */
    public int getDistributionRewards(String memberId);


    /**
     * 获取拼中的人
     *
     * @param marketingGroupIntegralManageListId
     * @return
     */
    public Map<String,Object> getWinning(String marketingGroupIntegralManageListId);

    /**
     * 获取需要清算的拼购记录
     *
     * @return
     */
    public List<Map<String,Object>> recordsLiquidation();

    /**
     * 获取会员上一天的拼购次数
     *
     * @param memberId
     * @return
     */
    public int getRecordCount(String memberId,String id,String mgimi);

}
