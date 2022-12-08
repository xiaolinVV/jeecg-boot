package com.alibaba.trade.param;

public class AlibabaTradeAddressCode {

    private String code;

    /**
     * @return 地区编码
     */
    public String getCode() {
        return code;
    }

    /**
     * 设置地区编码     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setCode(String code) {
        this.code = code;
    }

    private String name;

    /**
     * @return 地区名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置地区名称     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setName(String name) {
        this.name = name;
    }

    private String parentCode;

    /**
     * @return 父节点编码，可能为空
     */
    public String getParentCode() {
        return parentCode;
    }

    /**
     * 设置父节点编码，可能为空     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    private String post;

    /**
     * @return 邮编
     */
    public String getPost() {
        return post;
    }

    /**
     * 设置邮编     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setPost(String post) {
        this.post = post;
    }

    private String[] children;

    /**
     * @return 下一级编码列表，可能为空
     */
    public String[] getChildren() {
        return children;
    }

    /**
     * 设置下一级编码列表，可能为空     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setChildren(String[] children) {
        this.children = children;
    }

}
