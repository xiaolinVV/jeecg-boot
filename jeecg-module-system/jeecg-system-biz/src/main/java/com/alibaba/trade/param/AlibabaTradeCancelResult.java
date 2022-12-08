package com.alibaba.trade.param;

public class AlibabaTradeCancelResult {

    private Boolean success;

    /**
     * @return 是否处理成功：true为成功，false为失败，失败原因见error
     */
    public Boolean getSuccess() {
        return success;
    }

    /**
     * 设置是否处理成功：true为成功，false为失败，失败原因见error     *
          
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

    private String errorMessage;

    /**
     * @return 错误信息
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * 设置错误信息     *
          
     * 此参数必填
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
