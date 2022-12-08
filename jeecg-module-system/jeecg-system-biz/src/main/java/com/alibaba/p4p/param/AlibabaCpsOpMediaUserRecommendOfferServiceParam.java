package com.alibaba.p4p.param;

import com.alibaba.ocean.rawsdk.client.APIId;
import com.alibaba.ocean.rawsdk.common.AbstractAPIRequest;

public class AlibabaCpsOpMediaUserRecommendOfferServiceParam extends AbstractAPIRequest<AlibabaCpsOpMediaUserRecommendOfferServiceResult> {

    public AlibabaCpsOpMediaUserRecommendOfferServiceParam() {
        super();
        oceanApiId = new APIId("com.alibaba.p4p", "alibaba.cps.op.mediaUserRecommendOfferService", 1);
    }

    private String deviceIdMd5;

    /**
     * @return 设备id求md5(32位小写)(手机号与设备号至少一个)
     */
    public String getDeviceIdMd5() {
        return deviceIdMd5;
    }

    /**
     * 设置设备id求md5(32位小写)(手机号与设备号至少一个)     *
     * 参数示例：<pre>xxxxxxx</pre>     
     * 此参数必填
     */
    public void setDeviceIdMd5(String deviceIdMd5) {
        this.deviceIdMd5 = deviceIdMd5;
    }

    private String phoneMd5;

    /**
     * @return 手机号求md5(32位小写)(手机号与设备号至少一个)
     */
    public String getPhoneMd5() {
        return phoneMd5;
    }

    /**
     * 设置手机号求md5(32位小写)(手机号与设备号至少一个)     *
     * 参数示例：<pre>xxxxxxx</pre>     
     * 此参数必填
     */
    public void setPhoneMd5(String phoneMd5) {
        this.phoneMd5 = phoneMd5;
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
     * @return 每页数量
     */
    public Integer getPageSize() {
        return pageSize;
    }

    /**
     * 设置每页数量     *
     * 参数示例：<pre>20</pre>     
     * 此参数必填
     */
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

}
