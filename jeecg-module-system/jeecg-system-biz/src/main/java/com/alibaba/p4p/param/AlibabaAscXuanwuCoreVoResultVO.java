package com.alibaba.p4p.param;

public class AlibabaAscXuanwuCoreVoResultVO {

    private Boolean success;

    /**
     * @return 接口状态
     */
    public Boolean getSuccess() {
        return success;
    }

    /**
     * 设置接口状态     *
     * 参数示例：<pre>false</pre>     
     * 此参数必填
     */
    public void setSuccess(Boolean success) {
        this.success = success;
    }

    private String errorCode;

    /**
     * @return errorCode
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * 设置errorCode     *
     * 参数示例：<pre>101</pre>     
     * 此参数必填
     */
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    private String errorMsg;

    /**
     * @return errorMsg
     */
    public String getErrorMsg() {
        return errorMsg;
    }

    /**
     * 设置errorMsg     *
     * 参数示例：<pre>msg</pre>     
     * 此参数必填
     */
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    private Boolean result;

    /**
     * @return 结果
     */
    public Boolean getResult() {
        return result;
    }

    /**
     * 设置结果     *
     * 参数示例：<pre>true</pre>     
     * 此参数必填
     */
    public void setResult(Boolean result) {
        this.result = result;
    }

}
