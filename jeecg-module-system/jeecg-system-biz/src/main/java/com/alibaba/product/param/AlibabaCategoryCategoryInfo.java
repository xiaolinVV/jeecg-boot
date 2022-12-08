package com.alibaba.product.param;

public class AlibabaCategoryCategoryInfo {

    private Long categoryID;

    /**
     * @return 类目ID
     */
    public Long getCategoryID() {
        return categoryID;
    }

    /**
     * 设置类目ID     *
     * 参数示例：<pre>123456</pre>     
     * 此参数必填
     */
    public void setCategoryID(Long categoryID) {
        this.categoryID = categoryID;
    }

    private String name;

    /**
     * @return 类目名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置类目名称     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setName(String name) {
        this.name = name;
    }

    private Integer level;

    /**
     * @return 类目层级，1688无此内容
     */
    public Integer getLevel() {
        return level;
    }

    /**
     * 设置类目层级，1688无此内容     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setLevel(Integer level) {
        this.level = level;
    }

    private Boolean isLeaf;

    /**
     * @return 是否叶子类目（只有叶子类目才能发布商品）
     */
    public Boolean getIsLeaf() {
        return isLeaf;
    }

    /**
     * 设置是否叶子类目（只有叶子类目才能发布商品）     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setIsLeaf(Boolean isLeaf) {
        this.isLeaf = isLeaf;
    }

    private long[] parentIDs;

    /**
     * @return 父类目ID数组,1688只返回一个父id
     */
    public long[] getParentIDs() {
        return parentIDs;
    }

    /**
     * 设置父类目ID数组,1688只返回一个父id     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setParentIDs(long[] parentIDs) {
        this.parentIDs = parentIDs;
    }

    private long[] childIDs;

    /**
     * @return 子类目ID数组，1688无此内容
     */
    public long[] getChildIDs() {
        return childIDs;
    }

    /**
     * 设置子类目ID数组，1688无此内容     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setChildIDs(long[] childIDs) {
        this.childIDs = childIDs;
    }

    private AlibabaChildCategoryInfo[] childCategorys;

    /**
     * @return 子类目信息
     */
    public AlibabaChildCategoryInfo[] getChildCategorys() {
        return childCategorys;
    }

    /**
     * 设置子类目信息     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setChildCategorys(AlibabaChildCategoryInfo[] childCategorys) {
        this.childCategorys = childCategorys;
    }

}
