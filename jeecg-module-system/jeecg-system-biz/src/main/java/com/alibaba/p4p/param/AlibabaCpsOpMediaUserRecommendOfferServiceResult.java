package com.alibaba.p4p.param;

public class AlibabaCpsOpMediaUserRecommendOfferServiceResult {

    private AlibabaPpFaasGinkgoLibCommonDtoPageResultDTO result;

    /**
     * @return 商品列表，个性化推荐流，没有total返回，翻页到空结果为结束
     */
    public AlibabaPpFaasGinkgoLibCommonDtoPageResultDTO getResult() {
        return result;
    }

    /**
     * 设置商品列表，个性化推荐流，没有total返回，翻页到空结果为结束     *
          
     * 此参数必填
     */
    public void setResult(AlibabaPpFaasGinkgoLibCommonDtoPageResultDTO result) {
        this.result = result;
    }

}
