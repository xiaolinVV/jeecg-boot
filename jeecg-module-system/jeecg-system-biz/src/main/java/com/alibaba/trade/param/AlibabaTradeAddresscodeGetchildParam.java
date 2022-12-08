package com.alibaba.trade.param;

import com.alibaba.ocean.rawsdk.client.APIId;
import com.alibaba.ocean.rawsdk.common.AbstractAPIRequest;

public class AlibabaTradeAddresscodeGetchildParam extends AbstractAPIRequest<AlibabaTradeAddresscodeGetchildResult> {

    public AlibabaTradeAddresscodeGetchildParam() {
        super();
        oceanApiId = new APIId("com.alibaba.trade", "alibaba.trade.addresscode.getchild", 1);
    }

    private String areaCode;

    /**
     * @return 地址码，如果不输入则获取最上层信息
     */
    public String getAreaCode() {
        return areaCode;
    }

    /**
     * 设置地址码，如果不输入则获取最上层信息     *
     * 参数示例：<pre>330108</pre>     
     * 此参数必填
     */
    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    private String webSite;

    /**
     * @return 站点信息,1688或者alibaba
     */
    public String getWebSite() {
        return webSite;
    }

    /**
     * 设置站点信息,1688或者alibaba     *
     * 参数示例：<pre>1688</pre>     
     * 此参数必填
     */
    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

}
