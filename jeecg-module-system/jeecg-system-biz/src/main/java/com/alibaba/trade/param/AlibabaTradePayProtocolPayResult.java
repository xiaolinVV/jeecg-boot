package com.alibaba.trade.param;

public class AlibabaTradePayProtocolPayResult {

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

    private String code;

    /**
     * @return 错误码
     */
    public String getCode() {
        return code;
    }

    /**
     * 设置错误码     *
          
     * 此参数必填
     */
    public void setCode(String code) {
        this.code = code;
    }

    private String message;

    /**
     * @return 错误描述
     */
    public String getMessage() {
        return message;
    }

    /**
     * 设置错误描述     *
          
     * 此参数必填
     */
    public void setMessage(String message) {
        this.message = message;
    }

}
