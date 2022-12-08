package com.alibaba.p4p.param;

public class ComAlibabaPpOpenClientResultOpenUnionActivityOfferDetailResult {

    private ComAlibabaPpOpenClientDtoUnionActivityOfferDetailDTO result;

    /**
     * @return 结果
     */
    public ComAlibabaPpOpenClientDtoUnionActivityOfferDetailDTO getResult() {
        return result;
    }

    /**
     * 设置结果     *
     * 参数示例：<pre>{}</pre>     
     * 此参数必填
     */
    public void setResult(ComAlibabaPpOpenClientDtoUnionActivityOfferDetailDTO result) {
        this.result = result;
    }

    private Boolean success;

    /**
     * @return 状态
     */
    public Boolean getSuccess() {
        return success;
    }

    /**
     * 设置状态     *
     * 参数示例：<pre>true</pre>     
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
     * 参数示例：<pre>errorMsg</pre>     
     * 此参数必填
     */
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

}
