package com.alibaba.p4p.param;

public class ComAlibabaPpOpenClientDtoUnionActivitySkuStockDTO {

    private Integer stock;

    /**
     * @return 库存
     */
    public Integer getStock() {
        return stock;
    }

    /**
     * 设置库存     *
     * 参数示例：<pre>1</pre>     
     * 此参数必填
     */
    public void setStock(Integer stock) {
        this.stock = stock;
    }

    private Long skuId;

    /**
     * @return skuId
     */
    public Long getSkuId() {
        return skuId;
    }

    /**
     * 设置skuId     *
     * 参数示例：<pre>11111</pre>     
     * 此参数必填
     */
    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

}
