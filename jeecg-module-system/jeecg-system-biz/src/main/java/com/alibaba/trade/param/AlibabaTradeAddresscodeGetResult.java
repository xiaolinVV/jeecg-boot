package com.alibaba.trade.param;

public class AlibabaTradeAddresscodeGetResult {

    private AlibabaTradeAddressCode result;

    /**
     * @return 返回地区信息
     */
    public AlibabaTradeAddressCode getResult() {
        return result;
    }

    /**
     * 设置返回地区信息     *
          
     * 此参数必填
     */
    public void setResult(AlibabaTradeAddressCode result) {
        this.result = result;
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

}
