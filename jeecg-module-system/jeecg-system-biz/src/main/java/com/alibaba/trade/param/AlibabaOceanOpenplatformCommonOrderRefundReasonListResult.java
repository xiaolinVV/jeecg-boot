package com.alibaba.trade.param;

public class AlibabaOceanOpenplatformCommonOrderRefundReasonListResult {

    private String code;

    /**
     * @return 错误码
     */
    public String getCode() {
        return code;
    }

    /**
     * 设置错误码     *
     * 参数示例：<pre> </pre>     
     * 此参数必填
     */
    public void setCode(String code) {
        this.code = code;
    }

    private String message;

    /**
     * @return 错误信息
     */
    public String getMessage() {
        return message;
    }

    /**
     * 设置错误信息     *
     * 参数示例：<pre> </pre>     
     * 此参数必填
     */
    public void setMessage(String message) {
        this.message = message;
    }

    private AlibabaOceanOpenplatformBizTradeResultOrderRefundReasonListResult result;

    /**
     * @return 结果
     */
    public AlibabaOceanOpenplatformBizTradeResultOrderRefundReasonListResult getResult() {
        return result;
    }

    /**
     * 设置结果     *
     * 参数示例：<pre> </pre>     
     * 此参数必填
     */
    public void setResult(AlibabaOceanOpenplatformBizTradeResultOrderRefundReasonListResult result) {
        this.result = result;
    }

    private Boolean success;

    /**
     * @return 是否成功
     */
    public Boolean getSuccess() {
        return success;
    }

    /**
     * 设置是否成功     *
     * 参数示例：<pre> </pre>     
     * 此参数必填
     */
    public void setSuccess(Boolean success) {
        this.success = success;
    }

}
