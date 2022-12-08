package com.alibaba.p4p.param;

public class AlibabaAscXuanwuCoreDtoPageResultDTO {

    private String errorCode;

    /**
     * @return errorCode
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * 设置errorCode     *
     * 参数示例：<pre>errorCode</pre>     
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

    private AlibabaWxbUnionClientModelDtoOverPricedCybSearchOffersDTO[] result;

    /**
     * @return 列表
     */
    public AlibabaWxbUnionClientModelDtoOverPricedCybSearchOffersDTO[] getResult() {
        return result;
    }

    /**
     * 设置列表     *
     * 参数示例：<pre>[]</pre>     
     * 此参数必填
     */
    public void setResult(AlibabaWxbUnionClientModelDtoOverPricedCybSearchOffersDTO[] result) {
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

    private Integer totalCount;

    /**
     * @return 总数
     */
    public Integer getTotalCount() {
        return totalCount;
    }

    /**
     * 设置总数     *
     * 参数示例：<pre>223133</pre>     
     * 此参数必填
     */
    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

}
