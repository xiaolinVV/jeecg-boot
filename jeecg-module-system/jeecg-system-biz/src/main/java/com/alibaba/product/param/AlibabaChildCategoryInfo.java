package com.alibaba.product.param;

public class AlibabaChildCategoryInfo {

    private Long id;

    /**
     * @return 子类目ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置子类目ID     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setId(Long id) {
        this.id = id;
    }

    private String name;

    /**
     * @return 子类目名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置子类目名称     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setName(String name) {
        this.name = name;
    }

}
