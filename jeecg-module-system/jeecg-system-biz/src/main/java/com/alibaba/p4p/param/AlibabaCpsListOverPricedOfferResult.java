package com.alibaba.p4p.param;

public class AlibabaCpsListOverPricedOfferResult {

    private AlibabaCpsOpenOfferDTO[] result;

    /**
     * @return 列表
     */
    public AlibabaCpsOpenOfferDTO[] getResult() {
        return result;
    }

    /**
     * 设置列表     *
          
     * 此参数必填
     */
    public void setResult(AlibabaCpsOpenOfferDTO[] result) {
        this.result = result;
    }

    private Integer totalRow;

    /**
     * @return 总数
     */
    public Integer getTotalRow() {
        return totalRow;
    }

    /**
     * 设置总数     *
          
     * 此参数必填
     */
    public void setTotalRow(Integer totalRow) {
        this.totalRow = totalRow;
    }

}
