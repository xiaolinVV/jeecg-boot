package com.alibaba.trade.param;

public class AlibabaTradeAddresscodeParseResult {

    private AlibabaTradeReceiveAddress result;

    /**
     * @return 解析后的收获地址
     */
    public AlibabaTradeReceiveAddress getResult() {
        return result;
    }

    /**
     * 设置解析后的收获地址     *
          
     * 此参数必填
     */
    public void setResult(AlibabaTradeReceiveAddress result) {
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
