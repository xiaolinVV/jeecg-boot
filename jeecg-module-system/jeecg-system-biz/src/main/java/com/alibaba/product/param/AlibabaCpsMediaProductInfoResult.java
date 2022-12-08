package com.alibaba.product.param;

public class AlibabaCpsMediaProductInfoResult {

    private Boolean success;

    /**
     * @return 是否成功
     */
    public Boolean getSuccess() {
        return success;
    }

    /**
     * 设置是否成功     *
          
     * 此参数必填
     */
    public void setSuccess(Boolean success) {
        this.success = success;
    }

    private String errorCode;

    /**
     * @return 错误码
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * 设置错误码     *
          
     * 此参数必填
     */
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    private String errorMsg;

    /**
     * @return 错误信息
     */
    public String getErrorMsg() {
        return errorMsg;
    }

    /**
     * 设置错误信息     *
          
     * 此参数必填
     */
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    private AlibabaProductProductInfo productInfo;

    /**
     * @return 商品详情
     */
    public AlibabaProductProductInfo getProductInfo() {
        return productInfo;
    }

    /**
     * 设置商品详情     *
          
     * 此参数必填
     */
    public void setProductInfo(AlibabaProductProductInfo productInfo) {
        this.productInfo = productInfo;
    }

    private AlibabaProductProductBizGroupInfo[] bizGroupInfos;

    /**
     * @return 业务信息
     */
    public AlibabaProductProductBizGroupInfo[] getBizGroupInfos() {
        return bizGroupInfos;
    }

    /**
     * 设置业务信息     *
          
     * 此参数必填
     */
    public void setBizGroupInfos(AlibabaProductProductBizGroupInfo[] bizGroupInfos) {
        this.bizGroupInfos = bizGroupInfos;
    }

}
