package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.marketing.dto.MarketingCertificateDTO;
import org.jeecg.modules.marketing.entity.MarketingCertificate;
import org.jeecg.modules.marketing.vo.MarketingCertificateVO;

import java.util.List;
import java.util.Map;

/**
 * @Description: 兑换券
 * @Author: jeecg-boot
 * @Date:   2019-11-13
 * @Version: V1.0
 */
public interface MarketingCertificateMapper extends BaseMapper<MarketingCertificate> {

    IPage<MarketingCertificateDTO> findMarketingCertificate(Page<MarketingCertificateDTO> page,@Param("marketingCertificateVO") MarketingCertificateVO marketingCertificateVO);

    List<MarketingCertificateVO> findCertificateVO(@Param("marketingCertificateVO") MarketingCertificateVO marketingCertificateVO);

    /**
     * 查询兑换券的列表
     *
     * @param page
     * @return
     */
    public IPage<Map<String,Object>> findMarketingCertificateList(Page<Map<String,Object>> page,@Param("name") String name);


    /**
     * 查询兑换券的列表根据店铺id
     *
     * @param page
     * @return
     */
    public IPage<Map<String,Object>> findMarketingCertificateBySysUserId(Page<Map<String,Object>> page,@Param("sysUserId") String sysUserId);

    /**
     * 兑换券详情信息
     * @param id
     * @return
     */
    public Map<String,Object> findMarketingCertificateInfo(@Param("id") String id);

    List<MarketingCertificateVO> findCertificate(@Param("marketingCertificateVO") MarketingCertificateVO marketingCertificateVO);

    List<MarketingCertificateVO> findGiveMarketingCertificateVO(@Param("marketingCertificateVO") MarketingCertificateVO marketingCertificateVO);

    List<Map<String,Object>> pageListBySout(@Param("sout") int sout);

    List<MarketingCertificateVO> findCertificateData(@Param("marketingCertificateVO") MarketingCertificateVO marketingCertificateVO);

    List<MarketingCertificateVO> findCertificateDataByType(@Param("marketingCertificateVO") MarketingCertificateVO marketingCertificateVO);
}
