package com.alibaba.p4p.param;

import com.alibaba.ocean.rawsdk.client.APIId;
import com.alibaba.ocean.rawsdk.common.AbstractAPIRequest;

public class AlibabaCpsOpListCybUserGroupFeedParam extends AbstractAPIRequest<AlibabaCpsOpListCybUserGroupFeedResult> {

    public AlibabaCpsOpListCybUserGroupFeedParam() {
        super();
        oceanApiId = new APIId("com.alibaba.p4p", "alibaba.cps.op.listCybUserGroupFeed", 1);
    }

    private Long groupId;

    /**
     * @return 选品组id，不传表示取默认选品组下商品；
     */
    public Long getGroupId() {
        return groupId;
    }

    /**
     * 设置选品组id，不传表示取默认选品组下商品；     *
     * 参数示例：<pre>1</pre>     
     * 此参数必填
     */
    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    private Integer pageNo;

    /**
     * @return 页码
     */
    public Integer getPageNo() {
        return pageNo;
    }

    /**
     * 设置页码     *
     * 参数示例：<pre>1</pre>     
     * 此参数必填
     */
    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    private Integer pageSize;

    /**
     * @return 每页总数
     */
    public Integer getPageSize() {
        return pageSize;
    }

    /**
     * 设置每页总数     *
     * 参数示例：<pre>10</pre>     
     * 此参数必填
     */
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    private Long feedId;

    /**
     * @return 商品id
     */
    public Long getFeedId() {
        return feedId;
    }

    /**
     * 设置商品id     *
     * 参数示例：<pre>12313</pre>     
     * 此参数必填
     */
    public void setFeedId(Long feedId) {
        this.feedId = feedId;
    }

    private String title;

    /**
     * @return 商品标题模糊搜索
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置商品标题模糊搜索     *
     * 参数示例：<pre>女装</pre>     
     * 此参数必填
     */
    public void setTitle(String title) {
        this.title = title;
    }

}
