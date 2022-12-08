package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
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
public interface IMarketingCertificateSeckillRecordService extends IService<MarketingCertificateSeckillRecord> {

    List<Map<String,Object>> dataList(String marketingCertificateSeckillActivityListId);

    IPage<MarketingCertificateSeckillRecordVO> queryPageList(Page<MarketingCertificateSeckillRecordVO> page, QueryWrapper<MarketingCertificateSeckillRecordVO> queryWrapper);

    List<Map<String,Object>> lookQqzixuanguByPayId(String payId);
}
