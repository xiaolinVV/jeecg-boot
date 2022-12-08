package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.marketing.entity.MarketingBusinessGiftTeamList;

import java.util.List;
import java.util.Map;

/**
 * @Description: 团队记录
 * @Author: jeecg-boot
 * @Date:   2021-10-16
 * @Version: V1.0
 */
public interface MarketingBusinessGiftTeamListMapper extends BaseMapper<MarketingBusinessGiftTeamList> {


    /**
     * 根据记录id，查询团信息
     *
     * @param marketingBusinessGiftTeamRecordId
     * @return
     */
    public List<Map<String,Object>> getByteamRecordId(@Param("marketingBusinessGiftTeamRecordId") String marketingBusinessGiftTeamRecordId);


    /**
     * 根据会员id查询数据
     *
     * @param page
     * @param memberId
     * @return
     */
    public IPage<Map<String,Object>> getByMemberId(Page<Map<String,Object>> page,@Param("memberId") String memberId);

    /**
     * 根据会员id获取记录id
     *
     * @return
     */
    public Map<String,Object> getRecordIdByMemberId(@Param("memberId") String memberId);

}
