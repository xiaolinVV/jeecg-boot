package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.marketing.entity.MarketingCertificateSeckillRecord;
import org.jeecg.modules.marketing.vo.MarketingCertificateSeckillRecordVO;

import java.util.List;
import java.util.Map;

/**
 * @Description: 购买记录
 * @Author: jeecg-boot
 * @Date:   2021-03-29
 * @Version: V1.0
 */
public interface MarketingCertificateSeckillRecordMapper extends BaseMapper<MarketingCertificateSeckillRecord> {

    List<Map<String,Object>> dataList(@Param("marketingCertificateSeckillActivityListId") String marketingCertificateSeckillActivityListId);

    IPage<MarketingCertificateSeckillRecordVO> queryPageList(Page<MarketingCertificateSeckillRecordVO> page,@Param(Constants.WRAPPER) QueryWrapper<MarketingCertificateSeckillRecordVO> queryWrapper);

    List<Map<String,Object>> lookQqzixuanguByPayId(@Param("payId") String payId);
}
