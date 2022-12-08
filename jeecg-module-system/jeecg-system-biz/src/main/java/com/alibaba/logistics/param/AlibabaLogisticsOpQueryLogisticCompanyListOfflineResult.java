package com.alibaba.logistics.param;

public class AlibabaLogisticsOpQueryLogisticCompanyListOfflineResult {

    private AlibabaLogisticsOpLogisticsCompanyModel[] result;

    /**
     * @return 物流公司列表
     */
    public AlibabaLogisticsOpLogisticsCompanyModel[] getResult() {
        return result;
    }

    /**
     * 设置物流公司列表     *
          
     * 此参数必填
     */
    public void setResult(AlibabaLogisticsOpLogisticsCompanyModel[] result) {
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
          
     * 此参数必填
     */
    public void setSuccess(Boolean success) {
        this.success = success;
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
     * @return 错误码描述
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * 设置错误码描述     *
          
     * 此参数必填
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    private String extErrorMessage;

    /**
     * @return 扩展错误码描述
     */
    public String getExtErrorMessage() {
        return extErrorMessage;
    }

    /**
     * 设置扩展错误码描述     *
          
     * 此参数必填
     */
    public void setExtErrorMessage(String extErrorMessage) {
        this.extErrorMessage = extErrorMessage;
    }

}
