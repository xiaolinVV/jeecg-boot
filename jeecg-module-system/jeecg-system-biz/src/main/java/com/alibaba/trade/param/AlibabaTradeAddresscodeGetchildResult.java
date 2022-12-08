package com.alibaba.trade.param;

public class AlibabaTradeAddresscodeGetchildResult {

    private AlibabaTradeAddressCode[] result;

    /**
     * @return 子地区信息，可能为空。如果返回值是空，则说明输入参数无法找到下一级区域信息，或者输入code已经是最底层区域
     */
    public AlibabaTradeAddressCode[] getResult() {
        return result;
    }

    /**
     * 设置子地区信息，可能为空。如果返回值是空，则说明输入参数无法找到下一级区域信息，或者输入code已经是最底层区域     *
          
     * 此参数必填
     */
    public void setResult(AlibabaTradeAddressCode[] result) {
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
