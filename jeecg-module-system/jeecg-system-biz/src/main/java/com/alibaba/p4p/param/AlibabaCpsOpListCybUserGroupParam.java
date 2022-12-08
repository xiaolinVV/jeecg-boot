package com.alibaba.p4p.param;

import com.alibaba.ocean.rawsdk.client.APIId;
import com.alibaba.ocean.rawsdk.common.AbstractAPIRequest;

public class AlibabaCpsOpListCybUserGroupParam extends AbstractAPIRequest<AlibabaCpsOpListCybUserGroupResult> {

    public AlibabaCpsOpListCybUserGroupParam() {
        super();
        oceanApiId = new APIId("com.alibaba.p4p", "alibaba.cps.op.listCybUserGroup", 1);
    }

    private String pageNo;

    /**
     * @return 页码
     */
    public String getPageNo() {
        return pageNo;
    }

    /**
     * 设置页码     *
     * 参数示例：<pre>1</pre>     
     * 此参数必填
     */
    public void setPageNo(String pageNo) {
        this.pageNo = pageNo;
    }

    private String pageSize;

    /**
     * @return 每页总数
     */
    public String getPageSize() {
        return pageSize;
    }

    /**
     * 设置每页总数     *
     * 参数示例：<pre>10</pre>     
     * 此参数必填
     */
    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

}
