package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.marketing.entity.MarketingGroupRecord;

import java.util.List;
import java.util.Map;

/**
 * @Description: 参团记录
 * @Author: jeecg-boot
 * @Date:   2021-03-23
 * @Version: V1.0
 */
public interface MarketingGroupRecordMapper extends BaseMapper<MarketingGroupRecord> {


    /**
     * 根据拼团管理id查询拼团记录
     *
     * 张靠勤  2021-4-4
     *
     * @param marketingGroupManageId
     * @return
     */
    public List<Map<String,Object>> getMarketingGroupRecordByMarketingGroupManageId(@Param("marketingGroupManageId") String marketingGroupManageId);


    /**
     * 查询中奖拼团记录列表
     *
     * 张靠勤  2021-4-5
     *
     * @param page
     * @param paramMap
     * @return
     */
    public IPage<Map<String,Object>> getMarketingGroupRecordByMemberId(Page<Map<String,Object>> page,@Param("paramMap") Map<String,Object> paramMap);


    /**
     * 根据参团记录id，查询参团数据
     *
     * @param marketingGroupRecordId
     * @return
     */
    public Map<String,Object> getMarketingGroupRecordById(@Param("marketingGroupRecordId") String marketingGroupRecordId);


    /**
     * 获取拼团记录列表
     *
     * 张靠勤   2021-4-6
     *
     * @param page
     * @param paramMap
     * @return
     */
    public IPage<Map<String,Object>> getMarketingGroupRecordList(Page<Map<String,Object>> page,@Param("paramMap") Map<String,String> paramMap);


    /**
     * 查询拼团首页拼团成功的数据
     *
     * 张靠勤  2021-4-8
     *
     * @return
     */
    public List<Map<String,Object>> getMarketingGroupRecordByRewardStatus();

}
