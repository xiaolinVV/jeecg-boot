package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.marketing.dto.MarketingCertificateStoreDTO;
import org.jeecg.modules.marketing.entity.MarketingCertificateStore;

import java.util.List;
import java.util.Map;

/**
 * @Description: 兑换券门店映射
 * @Author: jeecg-boot
 * @Date:   2019-11-13
 * @Version: V1.0
 */
public interface MarketingCertificateStoreMapper extends BaseMapper<MarketingCertificateStore> {

    List<MarketingCertificateStoreDTO> findStoreByCertificateId(@Param("marketingCertificateId") String marketingCertificateId);

    IPage<Map<String,Object>> findstoreById(Page<Map<String,Object>> page,@Param("map") Map<String,Object> map);

    List<Map<String,Object>> findMarketingCertificateSeckillListStoreByCertificateId(@Param("id") String id);

    IPage<Map<String,Object>> findMarketingCertificateSeckillPageListStoreByCertificateId(Page<Map<String,Object>> page,@Param("id") String id,@Param("latitude") String latitude,@Param("longitude") String longitude);
}