package com.alibaba.trade.param;

public class AlibabaTradeRefundOpQueryOrderRefundListResult {

    private AlibabaTradeRefundOpOrderRefundModel[] opOrderRefundModels;

    /**
     * @return 退款单列表
     */
    public AlibabaTradeRefundOpOrderRefundModel[] getOpOrderRefundModels() {
        return opOrderRefundModels;
    }

    /**
     * 设置退款单列表     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setOpOrderRefundModels(AlibabaTradeRefundOpOrderRefundModel[] opOrderRefundModels) {
        this.opOrderRefundModels = opOrderRefundModels;
    }

    private Integer totalCount;

    /**
     * @return 符合条件总的记录条数
     */
    public Integer getTotalCount() {
        return totalCount;
    }

    /**
     * 设置符合条件总的记录条数     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    private Integer currentPageNum;

    /**
     * @return 查询的当前页码
     */
    public Integer getCurrentPageNum() {
        return currentPageNum;
    }

    /**
     * 设置查询的当前页码     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setCurrentPageNum(Integer currentPageNum) {
        this.currentPageNum = currentPageNum;
    }

}
