package com.alibaba.trade.param;

import com.alibaba.ocean.rawsdk.client.APIId;
import com.alibaba.ocean.rawsdk.common.AbstractAPIRequest;

public class AlibabaCreateOrderPreviewCybMediaParam extends AbstractAPIRequest<AlibabaCreateOrderPreviewCybMediaResult> {

    public AlibabaCreateOrderPreviewCybMediaParam() {
        super();
        oceanApiId = new APIId("com.alibaba.trade", "alibaba.createOrder.preview4CybMedia", 1);
    }

    private AlibabaTradeFastAddress addressParam;

    /**
     * @return 收货地址信息
     */
    public AlibabaTradeFastAddress getAddressParam() {
        return addressParam;
    }

    /**
     * 设置收货地址信息     *
     * 参数示例：<pre>{"address":"网商路699号","phone": "0517-88990077","mobile": "15251667788","fullName": "张三","postCode": "000000","areaText": "滨江区","townText": "","cityText": "杭州市","provinceText": "浙江省"}</pre>     
     * 此参数必填
     */
    public void setAddressParam(AlibabaTradeFastAddress addressParam) {
        this.addressParam = addressParam;
    }

    private AlibabaTradeFastCargo[] cargoParamList;

    /**
     * @return 商品信息
     */
    public AlibabaTradeFastCargo[] getCargoParamList() {
        return cargoParamList;
    }

    /**
     * 设置商品信息     *
     * 参数示例：<pre>[{"specId": "b266e0726506185beaf205cbae88530d","quantity": 5,"offerId": 554456348334},{"specId": "2ba3d63866a71fbae83909d9b4814f01","quantity": 6,"offerId": 554456348334}]</pre>     
     * 此参数必填
     */
    public void setCargoParamList(AlibabaTradeFastCargo[] cargoParamList) {
        this.cargoParamList = cargoParamList;
    }

}
