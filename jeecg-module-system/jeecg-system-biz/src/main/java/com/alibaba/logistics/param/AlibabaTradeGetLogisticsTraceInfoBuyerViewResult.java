package com.alibaba.logistics.param;

public class AlibabaTradeGetLogisticsTraceInfoBuyerViewResult {

    private AlibabaLogisticsOpenPlatformLogisticsTrace[] logisticsTrace;

    /**
     * @return 跟踪单详情
     */
    public AlibabaLogisticsOpenPlatformLogisticsTrace[] getLogisticsTrace() {
        return logisticsTrace;
    }

    /**
     * 设置跟踪单详情     *
          
     * 此参数必填
     */
    public void setLogisticsTrace(AlibabaLogisticsOpenPlatformLogisticsTrace[] logisticsTrace) {
        this.logisticsTrace = logisticsTrace;
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

    private String errorMessage;

    /**
     * @return 错误描述
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * 设置错误描述     *
          
     * 此参数必填
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

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

}
