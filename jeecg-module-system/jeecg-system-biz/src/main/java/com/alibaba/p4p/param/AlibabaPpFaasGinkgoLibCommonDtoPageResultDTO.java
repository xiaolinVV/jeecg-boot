package com.alibaba.p4p.param;

public class AlibabaPpFaasGinkgoLibCommonDtoPageResultDTO {

    private Boolean success;

    /**
     * @return 1
     */
    public Boolean getSuccess() {
        return success;
    }

    /**
     * 设置1     *
     * 参数示例：<pre>1</pre>     
     * 此参数必填
     */
    public void setSuccess(Boolean success) {
        this.success = success;
    }

    private String errorCode;

    /**
     * @return 1
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * 设置1     *
     * 参数示例：<pre>1</pre>     
     * 此参数必填
     */
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    private String errorMsg;

    /**
     * @return 1
     */
    public String getErrorMsg() {
        return errorMsg;
    }

    /**
     * 设置1     *
     * 参数示例：<pre>1</pre>     
     * 此参数必填
     */
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    private AlibabaPpFaasGinkgoLibUnionDtoRecommendOfferDTO[] result;

    /**
     * @return 1
     */
    public AlibabaPpFaasGinkgoLibUnionDtoRecommendOfferDTO[] getResult() {
        return result;
    }

    /**
     * 设置1     *
     * 参数示例：<pre>1</pre>     
     * 此参数必填
     */
    public void setResult(AlibabaPpFaasGinkgoLibUnionDtoRecommendOfferDTO[] result) {
        this.result = result;
    }

}
