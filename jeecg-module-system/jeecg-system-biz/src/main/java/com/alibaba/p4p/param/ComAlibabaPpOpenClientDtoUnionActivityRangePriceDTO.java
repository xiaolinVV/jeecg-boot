package com.alibaba.p4p.param;

public class ComAlibabaPpOpenClientDtoUnionActivityRangePriceDTO {

    private long[] price;

    /**
     * @return 区间价数组，每个item为一个价格,以分为单位
     */
    public long[] getPrice() {
        return price;
    }

    /**
     * 设置区间价数组，每个item为一个价格,以分为单位     *
     * 参数示例：<pre>[1,2,3]</pre>     
     * 此参数必填
     */
    public void setPrice(long[] price) {
        this.price = price;
    }

    private int[] beginQuantity;

    /**
     * @return 区间对应的起批量
     */
    public int[] getBeginQuantity() {
        return beginQuantity;
    }

    /**
     * 设置区间对应的起批量     *
     * 参数示例：<pre>[3,6,9]</pre>     
     * 此参数必填
     */
    public void setBeginQuantity(int[] beginQuantity) {
        this.beginQuantity = beginQuantity;
    }

}
