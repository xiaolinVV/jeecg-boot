package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
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
public interface MarketingZoneGroupRecordMapper extends BaseMapper<MarketingZoneGroupRecord> {

    IPage<Map<String,Object>> queryPageList(Page<Map<String,Object>> page, @Param(Constants.WRAPPER) QueryWrapper<MarketingZoneGroupRecord> queryWrapper,@Param("requestMap") Map<String, Object> requestMap);

    /**
     * 获取首页中奖数据
     *
     * @return
     */
    public List<Map<String,Object>> getIndexByStatus();

    List<Map<String,Object>> getZoneGroupManageMemberList(@Param("id") String id);

    IPage<Map<String,Object>> getMarketingZoneGroupRecordByMemberId(Page<Map<String,Object>> page,@Param("map") HashMap<String, Object> map);


    /**
     * 根据用户id获取单天拼的次数
     *
     * @param paramMap
     * @return
     */
    public int getCountByMemberId(@Param("paramMap") Map<String,Object> paramMap);


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

    /**
     * 获取拼中的人
     *
     * @param marketingZoneGroupManageId
     * @return
     */
    public Map<String,Object> getWinning(@Param("marketingZoneGroupManageId") String marketingZoneGroupManageId);
}
