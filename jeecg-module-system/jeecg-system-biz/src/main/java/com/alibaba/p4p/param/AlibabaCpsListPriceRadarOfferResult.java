package com.alibaba.p4p.param;

public class AlibabaCpsListPriceRadarOfferResult {

    private AlibabaCpsOpenPriceRadarOfferDTO[] result;

    /**
     * @return 结果返回
     */
    public AlibabaCpsOpenPriceRadarOfferDTO[] getResult() {
        return result;
    }

    /**
     * 设置结果返回     *
          
     * 此参数必填
     */
    public void setResult(AlibabaCpsOpenPriceRadarOfferDTO[] result) {
        this.result = result;
    }

}
