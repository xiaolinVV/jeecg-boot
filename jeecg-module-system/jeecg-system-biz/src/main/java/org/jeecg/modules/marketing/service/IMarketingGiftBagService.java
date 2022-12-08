package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.marketing.dto.MarketingGiftBagDTO;
import org.jeecg.modules.marketing.entity.MarketingGiftBag;
import org.jeecg.modules.marketing.entity.MarketingGiftBagRecord;
import org.jeecg.modules.marketing.vo.MarketingGiftBagVO;

import java.util.Map;

/**
 * @Description: 礼包管理
 * @Author: jeecg-boot
 * @Date:   2019-12-09
 * @Version: V1.0
 */
public interface IMarketingGiftBagService extends IService<MarketingGiftBag> {

    /**
     * 获取礼包列表
     * @param page
     * @param paramMap
     * @return
     */
    IPage<Map<String, Object>> getMarketingGiftBagList(Page<Map<String,Object>> page, Map<String,Object> paramMap);


    /**
     * 根据礼包id获取信息
     *
     * @param id
     * @return
     */
    Map<String,Object> findMarketingGiftBagById(String id);

    /**
     * 支付成功回调
     * @param memberId
     * @param id
     */
    public void paySuccess(Object memberId,String id);

    IPage<MarketingGiftBagVO> findMarketingGifiBagPageList(Page<MarketingGiftBagVO> page, MarketingGiftBagVO marketingGiftBagVO);

    String deleteAndDelExplain(String id, String delExplain);

    /**
     * 礼包新增
     * @param marketingGiftBagVO
     * @return
     */
    Result<MarketingGiftBag> add(MarketingGiftBagVO marketingGiftBagVO);

    Result<MarketingGiftBagDTO> edit(MarketingGiftBagVO marketingGiftBagVO);

    IPage<Map<String,Object>> isPrepositionList(Page<Map<String,Object>> page, MarketingGiftBagDTO marketingGiftBagDTO);

    void marketingGiftBagMemberGradeUpdate(Object memberId,MarketingGiftBag marketingGiftBag,MarketingGiftBagRecord marketingGiftBagRecord);

    void marketingGiftBagMemberWelfarePaymentsUpdate(Object memberId,MarketingGiftBag marketingGiftBag,MarketingGiftBagRecord marketingGiftBagRecord);

    void marketingGiftBagDiscountUpdate(Object memberId,MarketingGiftBag marketingGiftBag);

    void marketingGiftBagCertificateUpdate(Object memberId,MarketingGiftBag marketingGiftBag,String distributionChannel);

    void marketingGiftBagDistributionCommissionUpdate(Object memberId,MarketingGiftBag marketingGiftBag,MarketingGiftBagRecord marketingGiftBagRecord);

    void marketingGiftBagMemberDesignationUpdateNew(Object memberId,MarketingGiftBag marketingGiftBag,MarketingGiftBagRecord marketingGiftBagRecord);


    /**
     * 根据礼包生成礼品卡
     *
     * @param marketingGiftBagId
     * @param memberId
     */
    public void generateGiftCar(String marketingGiftBagId,String memberId);
}
