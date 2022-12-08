package com.alibaba.p4p.param;

public class AlibabaPpOpenClientResultOpenUnionOpGroupListResult {

    private String errorCode;

    /**
     * @return errCode
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * 设置errCode     *
     * 参数示例：<pre>errCode</pre>     
     * 此参数必填
     */
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    private String errorMsg;

    /**
     * @return errMsg
     */
    public String getErrorMsg() {
        return errorMsg;
    }

    /**
     * 设置errMsg     *
     * 参数示例：<pre>errMsg</pre>     
     * 此参数必填
     */
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    private AlibabaPpOpenClientDtoOpenUnionOpGroupDTO[] result;

    /**
     * @return 结果
     */
    public AlibabaPpOpenClientDtoOpenUnionOpGroupDTO[] getResult() {
        return result;
    }

    /**
     * 设置结果     *
     * 参数示例：<pre>[]</pre>     
     * 此参数必填
     */
    public void setResult(AlibabaPpOpenClientDtoOpenUnionOpGroupDTO[] result) {
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

    private Integer totalRow;

    /**
     * @return 总数
     */
    public Integer getTotalRow() {
        return totalRow;
    }

    /**
     * 设置总数     *
     * 参数示例：<pre>11</pre>     
     * 此参数必填
     */
    public void setTotalRow(Integer totalRow) {
        this.totalRow = totalRow;
    }

}
