package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.dto.MarketingCertificateRecordDTO;
import org.jeecg.modules.marketing.entity.MarketingCertificateRecord;
import org.jeecg.modules.marketing.vo.MarketingCertificateRecordVO;
import org.jeecg.modules.pay.entity.PayCertificateLog;

import java.util.Map;

/**
 * @Description: 兑换券记录
 * @Author: jeecg-boot
 * @Date:   2019-11-13
 * @Version: V1.0
 */
public interface IMarketingCertificateRecordService extends IService<MarketingCertificateRecord> {

    /**
     * 查询核销券
     * @param qqzixuangu 券码
     * @param sysUserId 店铺Id
     * @return
     */
   public IPage<MarketingCertificateRecordDTO> couponVerification(Page<MarketingCertificateRecord> page, String qqzixuangu, String sysUserId);

    /**
     *根据会员id查询会员兑换券记录
     * @param paramMap
     * @return
     */
    public IPage<Map<String,Object>> findMarketingCertificateRecordByMemberId(Page<Map<String,Object>> page, Map<String,Object> paramMap);


    /**
     * 根据id获取兑换券详情
     *
     * @param id
     * @return
     */
    public Map<String,Object> findMarketingCertificateRecordInfo(String id);

    IPage<MarketingCertificateRecordVO> findCertificateRecord(Page<MarketingCertificateRecordVO> page, MarketingCertificateRecordVO marketingCertificateRecordVO);

    IPage<MarketingCertificateRecordVO> findStoreCertificateRecord(Page<MarketingCertificateRecordVO> page, MarketingCertificateRecordVO marketingCertificateRecordVO);

    /**
     * 兑换券支付成功
     * @param payCertificateLog
     */
    public Map<String,Object> paySuccess(PayCertificateLog payCertificateLog);

    void updateMarketingCertificateRecordJob();

}
