package org.jeecg.modules.marketing.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.marketing.entity.MarketingWelfarePayments;
import org.jeecg.modules.marketing.vo.MarketingWelfarePaymentsVO;
import org.jeecg.modules.store.entity.StoreManage;

import java.util.List;
import java.util.Map;

/**
 * @Description: 店铺福利金
 * @Author: jeecg-boot
 * @Date:   2019-11-16
 * @Version: V1.0
 */
public interface IMarketingWelfarePaymentsService extends IService<MarketingWelfarePayments> {
    Result<MarketingWelfarePayments> updateWelfarePaymentsByBoosPhone(JSONObject jsonObject);

    IPage<MarketingWelfarePaymentsVO> findWelfarePaymentsTotal(Page<MarketingWelfarePaymentsVO> page, MarketingWelfarePaymentsVO marketingWelfarePaymentsVO);

    IPage<MarketingWelfarePaymentsVO> findStoreWelfarePayments(Page<MarketingWelfarePaymentsVO> page,MarketingWelfarePaymentsVO marketingWelfarePaymentsVO);

    IPage<MarketingWelfarePaymentsVO> findWelfarePaymentsBuy(Page<MarketingWelfarePaymentsVO> page, MarketingWelfarePaymentsVO marketingWelfarePaymentsVO);

    IPage<MarketingWelfarePaymentsVO> findStoreWelfarePaymentsList(Page<MarketingWelfarePaymentsVO> page, MarketingWelfarePaymentsVO marketingWelfarePaymentsVO);

    Result<StoreManage> updateStoreWelfarePayments(JSONObject jsonObject);

    IPage<Map<String,Object>> findBackStoreWelfarePayments(Page<MarketingWelfarePayments> page, String weType, String sysUserId);

    List<Map<String,Object>> everydaySendOutWelfare();

    List<Map<String,Object>> storeComplimentary();

}
