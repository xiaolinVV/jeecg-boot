package com.alibaba.trade.param;

public class AlibabaOceanOpenplatformBizTradeResultOrderRefundUploadVoucherResult {

    private String imageDomain;

    /**
     * @return 图片域名
     */
    public String getImageDomain() {
        return imageDomain;
    }

    /**
     * 设置图片域名     *
     * 参数示例：<pre> </pre>     
     * 此参数必填
     */
    public void setImageDomain(String imageDomain) {
        this.imageDomain = imageDomain;
    }

    private String imageRelativeUrl;

    /**
     * @return 图片相对路径
     */
    public String getImageRelativeUrl() {
        return imageRelativeUrl;
    }

    /**
     * 设置图片相对路径     *
     * 参数示例：<pre> </pre>     
     * 此参数必填
     */
    public void setImageRelativeUrl(String imageRelativeUrl) {
        this.imageRelativeUrl = imageRelativeUrl;
    }

}
