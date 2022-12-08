package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
public interface MarketingGroupIntegralManageRecordMapper extends BaseMapper<MarketingGroupIntegralManageRecord> {


    /**
     * 拼团记录列表
     *
     * @param page
     * @param marketingGroupIntegralManageRecordDTO
     * @return
     */
    public IPage<Map<String,Object>> queryPageList(Page<Map<String,Object>> page,@Param("marketingGroupIntegralManageRecordDTO") MarketingGroupIntegralManageRecordDTO marketingGroupIntegralManageRecordDTO);


    /**
     * 根据拼团列表id获取拼团记录
     *
     * @param marketingGroupIntegralManageListId
     * @return
     */
    public IPage<Map<String,Object>> getByMarketingGroupIntegralManageListId(Page<Map<String,Object>> page,@Param("marketingGroupIntegralManageListId") String marketingGroupIntegralManageListId);


    /**
     * 获取前10条   成功的拼团记录
     * @return
     */
    public List<Map<String,Object>>  getWinningState();


    /**
     * 根据拼团列表id获取拼团记录
     *
     * @param marketingGroupIntegralManageListId
     * @return
     */
    public List<Map<String,Object>> getByMarketingGroupIntegralManageListId(@Param("marketingGroupIntegralManageListId") String marketingGroupIntegralManageListId);


    /**
     * 获取用户拼购成团没结算的数据
     *
     * @param memberId
     * @return
     */
    public int getDistributionRewards(@Param("memberId") String memberId);


    /**
     * 获取拼中的人
     *
     * @param marketingGroupIntegralManageListId
     * @return
     */
    public Map<String,Object> getWinning(@Param("marketingGroupIntegralManageListId") String marketingGroupIntegralManageListId);

    /**
     * 获取需要清算的拼购记录
     *
     * @return
     */
    public List<Map<String,Object>> recordsLiquidation();

    /**
     * 获取会员上一天的拼购次数
     *
     * @param paramMap
     * @return
     */
    public int getRecordCount(@Param("paramMap") Map<String,Object> paramMap);

}
