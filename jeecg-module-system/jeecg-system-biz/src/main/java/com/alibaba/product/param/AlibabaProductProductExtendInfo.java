package com.alibaba.product.param;

public class AlibabaProductProductExtendInfo {

    private String key;

    /**
     * @return 扩展结构的key
     */
    public String getKey() {
        return key;
    }

    /**
     * 设置扩展结构的key     *
     * 参数示例：<pre>代销价格,consignPrice;
    买家保障,buyerProtection;</pre>     
     * 此参数必填
     */
    public void setKey(String key) {
        this.key = key;
    }

    private String value;

    /**
     * @return 扩展结构的value
     */
    public String getValue() {
        return value;
    }

    /**
     * 设置扩展结构的value     *
     * 参数示例：<pre>代销价格,key为skuId，value为用户设置的代销价，
    示例：31151771910:2088.0;31151771909:2088.0;31151771908:2088.0;31152339121:2088.0;
    买家保障,string数组，value为买保全拼，
    示例：["psbj","swtwlybt","swtbh","ssbxsfh"]</pre>     
     * 此参数必填
     */
    public void setValue(String value) {
        this.value = value;
    }

}
