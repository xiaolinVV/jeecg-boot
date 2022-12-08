package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.marketing.dto.MarketingCertificateGoodDTO;
import org.jeecg.modules.marketing.entity.MarketingCertificateGood;

import java.util.List;
import java.util.Map;

/**
 * @Description: 兑换券商品映射
 * @Author: jeecg-boot
 * @Date:   2019-11-13
 * @Version: V1.0
 */
public interface MarketingCertificateGoodMapper extends BaseMapper<MarketingCertificateGood> {

    List<MarketingCertificateGoodDTO> findGoodByCertificateId(@Param("marketingCertificateId") String marketingCertificateId);

    List<Map<String,Object>> findMarketingCertificateSeckillListGoodByCertificateId(@Param("id") String id);

    List<Map<String,Object>> findMarketingCertificateSeckillPageListGoodByCertificateId(Page<Map<String,Object>> page,@Param("id") String id);
}
