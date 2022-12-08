package org.jeecg.modules.marketing.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.entity.MarketingCertificateSeckillList;
import org.jeecg.modules.marketing.vo.MarketingCertificateSeckillListVO;

import java.util.List;
import java.util.Map;

/**
 * @Description: 限时抢券列表
 * @Author: jeecg-boot
 * @Date:   2021-03-29
 * @Version: V1.0
 */
public interface IMarketingCertificateSeckillListService extends IService<MarketingCertificateSeckillList> {

    List<Map<String,Object>> pageListBySout(String marketingCertificateSeckillActivityListId, Integer sout);

    IPage<MarketingCertificateSeckillListVO> queryPageList(Page<MarketingCertificateSeckillListVO> page, QueryWrapper<MarketingCertificateSeckillListVO> queryWrapper);

    IPage<Map<String,Object>> pageListByMarketingCertificateSeckillActivityListId(Page<Map<String,Object>> page, String marketingCertificateSeckillActivityListId);
}
