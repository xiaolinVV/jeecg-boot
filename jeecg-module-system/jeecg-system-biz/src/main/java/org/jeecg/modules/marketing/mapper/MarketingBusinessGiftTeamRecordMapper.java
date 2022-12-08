package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.marketing.dto.MarketingBusinessGiftTeamRecordDTO;
import org.jeecg.modules.marketing.entity.MarketingBusinessGiftTeamRecord;

import java.util.Map;

/**
 * @Description: 团队记录
 * @Author: jeecg-boot
 * @Date:   2021-10-16
 * @Version: V1.0
 */
public interface MarketingBusinessGiftTeamRecordMapper extends BaseMapper<MarketingBusinessGiftTeamRecord> {


    /**
     * 后台团队记录查询
     *
     * @param page
     * @param marketingBusinessGiftTeamRecordDTO
     * @return
     */
    public IPage<Map<String,Object>> queryPageList(Page<Map<String,Object>> page, @Param("marketingBusinessGiftTeamRecordDTO")MarketingBusinessGiftTeamRecordDTO marketingBusinessGiftTeamRecordDTO);

}
