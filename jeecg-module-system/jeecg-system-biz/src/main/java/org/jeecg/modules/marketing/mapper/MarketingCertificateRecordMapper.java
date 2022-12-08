package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.marketing.dto.MarketingCertificateRecordDTO;
import org.jeecg.modules.marketing.entity.MarketingCertificateRecord;
import org.jeecg.modules.marketing.vo.MarketingCertificateRecordVO;

import java.util.Map;

/**
 * @Description: 兑换券记录
 * @Author: jeecg-boot
 * @Date:   2019-11-13
 * @Version: V1.0
 */
public interface MarketingCertificateRecordMapper extends BaseMapper<MarketingCertificateRecord> {

    /**
     * 查询核销券
     * @param qqzixuangu 券码
     * @param sysUserId 店铺Id
     * @return
     */
    IPage<MarketingCertificateRecordDTO> couponVerification(Page<MarketingCertificateRecord> page, @Param("qqzixuangu") String qqzixuangu, @Param("sysUserId") String sysUserId);

    /**
     *根据会员id查询会员兑换券记录
     * @param paramMap
     * @return
     */
    public IPage<Map<String,Object>> findMarketingCertificateRecordByMemberId(Page<Map<String,Object>> page, @Param("paramMap") Map<String,Object> paramMap);

    /**
     * 根据id获取兑换券详情
     *
     * @param id
     * @return
     */
    public Map<String,Object> findMarketingCertificateRecordInfo(@Param("id") String id);

    IPage<MarketingCertificateRecordVO> findCertificateRecord(Page<MarketingCertificateRecordVO> page,@Param("marketingCertificateRecordVO") MarketingCertificateRecordVO marketingCertificateRecordVO);

    IPage<MarketingCertificateRecordVO> findStoreCertificateRecord(Page<MarketingCertificateRecordVO> page,@Param("marketingCertificateRecordVO") MarketingCertificateRecordVO marketingCertificateRecordVO);

    void updateMarketingCertificateTakeEffect();

    void updateMarketingCertificatePastDue();

}
