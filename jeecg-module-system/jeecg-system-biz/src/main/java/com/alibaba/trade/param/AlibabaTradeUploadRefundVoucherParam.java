package com.alibaba.trade.param;

import com.alibaba.ocean.rawsdk.client.APIId;
import com.alibaba.ocean.rawsdk.common.AbstractAPIRequest;

public class AlibabaTradeUploadRefundVoucherParam extends AbstractAPIRequest<AlibabaTradeUploadRefundVoucherResult> {

    public AlibabaTradeUploadRefundVoucherParam() {
        super();
        oceanApiId = new APIId("com.alibaba.trade", "alibaba.trade.uploadRefundVoucher", 1);
    }

    private byte[] imageData;

    /**
     * @return 凭证图片数据。小于1M，jpg格式。
     */
    public byte[] getImageData() {
        return imageData;
    }

    /**
     * 设置凭证图片数据。小于1M，jpg格式。     *
     * 参数示例：<pre> </pre>     
     * 此参数必填
     */
    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

}
