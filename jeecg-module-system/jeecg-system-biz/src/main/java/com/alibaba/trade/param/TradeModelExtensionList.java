package com.alibaba.trade.param;

public class TradeModelExtensionList {

    private String tradeWay;

    /**
     * @return 交易方式
     */
    public String getTradeWay() {
        return tradeWay;
    }

    /**
     * 设置交易方式     *
     * 参数示例：<pre> </pre>     
     * 此参数必填
     */
    public void setTradeWay(String tradeWay) {
        this.tradeWay = tradeWay;
    }

    private String name;

    /**
     * @return 交易方式名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置交易方式名称     *
     * 参数示例：<pre> </pre>     
     * 此参数必填
     */
    public void setName(String name) {
        this.name = name;
    }

    private String tradeType;

    /**
     * @return 开放平台下单时候传入的tradeType
     */
    public String getTradeType() {
        return tradeType;
    }

    /**
     * 设置开放平台下单时候传入的tradeType     *
     * 参数示例：<pre> </pre>     
     * 此参数必填
     */
    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    private String description;

    /**
     * @return 交易描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置交易描述     *
     * 参数示例：<pre> </pre>     
     * 此参数必填
     */
    public void setDescription(String description) {
        this.description = description;
    }

    private Boolean opSupport;

    /**
     * @return 是否支持
     */
    public Boolean getOpSupport() {
        return opSupport;
    }

    /**
     * 设置是否支持     *
     * 参数示例：<pre> </pre>     
     * 此参数必填
     */
    public void setOpSupport(Boolean opSupport) {
        this.opSupport = opSupport;
    }

}
