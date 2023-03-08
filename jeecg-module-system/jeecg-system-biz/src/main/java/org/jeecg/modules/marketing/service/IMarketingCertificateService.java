package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.marketing.dto.MarketingCertificateDTO;
import org.jeecg.modules.marketing.entity.MarketingCertificate;
import org.jeecg.modules.marketing.entity.MarketingCertificateRecord;
import org.jeecg.modules.marketing.vo.MarketingCertificateVO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Description: 兑换券
 * @Author: jeecg-boot
 * @Date:   2019-11-13
 * @Version: V1.0
 */
public interface IMarketingCertificateService extends IService<MarketingCertificate> {

    Result<MarketingCertificate> saveMarketingCertificate(MarketingCertificate marketingCertificate, String goodListIds, String sysUserIds);

    IPage<MarketingCertificateDTO> findMarketingCertificate(Page<MarketingCertificateDTO> page,MarketingCertificateVO marketingCertificateVO);

    List<MarketingCertificateVO> findCertificateVO(MarketingCertificateVO marketingCertificateVO);


    IPage<MarketingCertificateVO> findCertificateVO(Page<MarketingCertificateDTO> page,MarketingCertificateVO marketingCertificateVO);

    /**
     * 查询兑换券的列表
     *
     * @param page
     * @return
     */
    public IPage<Map<String,Object>> findMarketingCertificateList(Page<Map<String,Object>> page,String name);

    /**
     * 兑换券详情信息
     * @param id
     * @return
     */
    public Map<String,Object> findMarketingCertificateInfo(String id);

    /**
     * 查询兑换券的列表根据店铺id
     *
     * @param page
     * @return
     */
    public IPage<Map<String,Object>> findMarketingCertificateBySysUserId(Page<Map<String,Object>> page,String sysUserId);

    Result<MarketingCertificate> edit(MarketingCertificateVO marketingCertificateVO);

    List<MarketingCertificateVO> findCertificate(MarketingCertificateVO marketingCertificateVO);

    List<MarketingCertificateVO> findGiveMarketingCertificateVO(MarketingCertificateVO marketingCertificateVO);

    List<Map<String,Object>> pageListBySout(int sout);

    /**
     * 券生成
     *
     * @param marketingCertificateId   券id
     * @param sysUserId  渠道店铺
     * @param quantity  数量
     * @param memberId  会员id
     * @param isContinuous   是否连续
     */
    public List<MarketingCertificateRecord>  generate(String marketingCertificateId, String sysUserId, BigDecimal quantity, String memberId, Boolean isContinuous);

    List<MarketingCertificateVO> findCertificateData(MarketingCertificateVO marketingCertificateVO);

    List<MarketingCertificateVO> findCertificateDataByType(MarketingCertificateVO marketingCertificateVO);
}
