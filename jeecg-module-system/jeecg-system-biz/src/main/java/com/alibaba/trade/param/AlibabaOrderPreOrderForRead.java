package com.alibaba.trade.param;

public class AlibabaOrderPreOrderForRead {

    private String appkey;

    /**
     * @return 创建预订单的appkey
     */
    public String getAppkey() {
        return appkey;
    }

    /**
     * 设置创建预订单的appkey     *
     * 参数示例：<pre>12345</pre>     
     * 此参数必填
     */
    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    private String marketName;

    /**
     * @return 创建预订单时传入的市场名
     */
    public String getMarketName() {
        return marketName;
    }

    /**
     * 设置创建预订单时传入的市场名     *
     * 参数示例：<pre>dxc</pre>     
     * 此参数必填
     */
    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    private Boolean createPreOrderApp;

    /**
     * @return 预订单是否为当前查询的通过当前查询的ERP创建
     */
    public Boolean getCreatePreOrderApp() {
        return createPreOrderApp;
    }

    /**
     * 设置预订单是否为当前查询的通过当前查询的ERP创建     *
     * 参数示例：<pre>false</pre>     
     * 此参数必填
     */
    public void setCreatePreOrderApp(Boolean createPreOrderApp) {
        this.createPreOrderApp = createPreOrderApp;
    }

}
