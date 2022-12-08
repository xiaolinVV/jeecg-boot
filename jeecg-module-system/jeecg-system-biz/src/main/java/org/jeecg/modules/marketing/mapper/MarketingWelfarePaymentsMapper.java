package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.marketing.entity.MarketingWelfarePayments;
import org.jeecg.modules.marketing.vo.MarketingDisplayVO;
import org.jeecg.modules.marketing.vo.MarketingWelfarePaymentsVO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 店铺福利金
 * @Author: jeecg-boot
 * @Date:   2019-11-16
 * @Version: V1.0
 */
public interface MarketingWelfarePaymentsMapper extends BaseMapper<MarketingWelfarePayments> {

    IPage<MarketingWelfarePaymentsVO> findWelfarePaymentsTotal(Page<MarketingWelfarePaymentsVO> page,@Param("marketingWelfarePaymentsVO") MarketingWelfarePaymentsVO marketingWelfarePaymentsVO);

    IPage<MarketingWelfarePaymentsVO> findStoreWelfarePayments(Page<MarketingWelfarePaymentsVO> page,@Param("marketingWelfarePaymentsVO") MarketingWelfarePaymentsVO marketingWelfarePaymentsVO);

    IPage<MarketingWelfarePaymentsVO> findWelfarePaymentsBuy(Page<MarketingWelfarePaymentsVO> page,@Param("marketingWelfarePaymentsVO") MarketingWelfarePaymentsVO marketingWelfarePaymentsVO);

    IPage<MarketingWelfarePaymentsVO> findStoreWelfarePaymentsList(Page<MarketingWelfarePaymentsVO> page,@Param("marketingWelfarePaymentsVO") MarketingWelfarePaymentsVO marketingWelfarePaymentsVO);

    IPage<Map<String,Object>> findBackStoreWelfarePayments(Page<MarketingWelfarePayments> page,@Param("weType") String weType,@Param("sysUserId") String sysUserId);

    List<MarketingDisplayVO> everydaySendOutWelfare(@Param("map") HashMap<String, Object> map);

    List<Map<String,Object>> storeComplimentary();
}
