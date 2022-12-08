package com.alibaba.product.param;

public class AlibabaProductProductPriceRange {

    private Integer startQuantity;

    /**
     * @return 起批量
     */
    public Integer getStartQuantity() {
        return startQuantity;
    }

    /**
     * 设置起批量     *
     * 参数示例：<pre>3</pre>     
     * 此参数必填
     */
    public void setStartQuantity(Integer startQuantity) {
        this.startQuantity = startQuantity;
    }

    private Double price;

    /**
     * @return 价格
     */
    public Double getPrice() {
        return price;
    }

    /**
     * 设置价格     *
     * 参数示例：<pre>445</pre>     
     * 此参数必填
     */
    public void setPrice(Double price) {
        this.price = price;
    }

}
