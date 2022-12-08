package com.alibaba.p4p.param;

public class ComAlibabaPpOpenClientResultOpenUnionOpGroupFeedListResult {

    private ComAlibabaPpOpenClientDtoOpenUnionOpGroupFeedDTO[] resultList;

    /**
     * @return 列表
     */
    public ComAlibabaPpOpenClientDtoOpenUnionOpGroupFeedDTO[] getResultList() {
        return resultList;
    }

    /**
     * 设置列表     *
     * 参数示例：<pre>[]</pre>     
     * 此参数必填
     */
    public void setResultList(ComAlibabaPpOpenClientDtoOpenUnionOpGroupFeedDTO[] resultList) {
        this.resultList = resultList;
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
     * 参数示例：<pre>12</pre>     
     * 此参数必填
     */
    public void setTotalRow(Integer totalRow) {
        this.totalRow = totalRow;
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

    private String errorMsg;

    /**
     * @return success=false时，错误Msg
     */
    public String getErrorMsg() {
        return errorMsg;
    }

    /**
     * 设置success=false时，错误Msg     *
     * 参数示例：<pre>msg</pre>     
     * 此参数必填
     */
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    private String errorCode;

    /**
     * @return success=false时，错误code
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * 设置success=false时，错误code     *
     * 参数示例：<pre>code</pre>     
     * 此参数必填
     */
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

}
