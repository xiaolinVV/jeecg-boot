package org.jeecg.modules.taobao.service;

import com.alibaba.ocean.rawsdk.common.SDKResult;
import com.alibaba.product.param.AlibabaCpsMediaProductInfoResult;
import com.alibaba.trade.param.AlibabaOpenplatformTradeModelNativeLogisticsItemsInfo;
import com.alibaba.trade.param.AlibabaTradeGetBuyerViewResult;

/**
 * @author 张少林
 * @date 2023年03月23日 6:19 下午
 */
public interface IAli1688Service {

    boolean addShop(Long offerId, String title);

    /**
     * 商品提交
     *
     * @param goodListJson
     * @param goodSpecificationsJson
     * @return
     */

    void submitPrefectureGoods(String goodListJson,
                               String goodSpecificationsJson, String marketingPrefectureTypeId);

    /**
     * 查询商品详情
     *
     * @param offerId
     * @return
     */
    SDKResult<AlibabaCpsMediaProductInfoResult> getProductInfoResult(Long offerId);

    /**
     * 获取1688订单详情
     *
     * @param taobaoOrderId 1688订单id
     * @return
     */
    SDKResult<AlibabaTradeGetBuyerViewResult> getAlibabaTradeGetBuyerViewResult(Long taobaoOrderId);

    /**
     * 获取1688订单物流信息
     * @param taobaoOrderId 1688订单id
     * @return
     */
    AlibabaOpenplatformTradeModelNativeLogisticsItemsInfo getAlibabaOpenplatformTradeModelNativeLogisticsItemsInfo(Long taobaoOrderId);

    /**
     *
     */
    void syncSkuId();
}
