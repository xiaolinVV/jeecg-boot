package com.alibaba.p4p.param;

import java.util.Date;

public class AlibabaPpOpenClientDtoOpenUnionOpGroupDTO {

    private Date createTime;

    /**
     * @return 选品组创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置选品组创建时间     *
     * 参数示例：<pre>20190410000000000+0800</pre>     
     * 此参数必填
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    private Long feedCount;

    /**
     * @return 选品组下商品总数
     */
    public Long getFeedCount() {
        return feedCount;
    }

    /**
     * 设置选品组下商品总数     *
     * 参数示例：<pre>12</pre>     
     * 此参数必填
     */
    public void setFeedCount(Long feedCount) {
        this.feedCount = feedCount;
    }

    private Long id;

    /**
     * @return 选品组id
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置选品组id     *
     * 参数示例：<pre>11111</pre>     
     * 此参数必填
     */
    public void setId(Long id) {
        this.id = id;
    }

    private String title;

    /**
     * @return 选品组标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置选品组标题     *
     * 参数示例：<pre>我的选品组</pre>     
     * 此参数必填
     */
    public void setTitle(String title) {
        this.title = title;
    }

}
