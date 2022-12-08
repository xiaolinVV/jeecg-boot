package com.alibaba.trade.param;

public class AlibabaTradeCreateOrderCybMediaResult {

    private AlibabaTradeFastResult result;

    /**
     * @return 返回结果
     */
    public AlibabaTradeFastResult getResult() {
        return result;
    }

    /**
     * 设置返回结果     *
          
     * 此参数必填
     */
    public void setResult(AlibabaTradeFastResult result) {
        this.result = result;
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

}
