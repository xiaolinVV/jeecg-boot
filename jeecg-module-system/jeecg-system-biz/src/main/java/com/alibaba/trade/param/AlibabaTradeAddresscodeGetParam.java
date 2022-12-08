package com.alibaba.trade.param;

import com.alibaba.ocean.rawsdk.client.APIId;
import com.alibaba.ocean.rawsdk.common.AbstractAPIRequest;

public class AlibabaTradeAddresscodeGetParam extends AbstractAPIRequest<AlibabaTradeAddresscodeGetResult> {

    public AlibabaTradeAddresscodeGetParam() {
        super();
        oceanApiId = new APIId("com.alibaba.trade", "alibaba.trade.addresscode.get", 1);
    }

    private String areaCode;

    /**
     * @return 地址code码
     */
    public String getAreaCode() {
        return areaCode;
    }

    /**
     * 设置地址code码     *
     * 参数示例：<pre>330108</pre>     
     * 此参数必填
     */
    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    private String webSite;

    /**
     * @return 站点信息，指定调用的API是属于国际站（alibaba）还是1688网站（1688）
     */
    public String getWebSite() {
        return webSite;
    }

    /**
     * 设置站点信息，指定调用的API是属于国际站（alibaba）还是1688网站（1688）     *
     * 参数示例：<pre>1688</pre>     
     * 此参数必填
     */
    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

}
