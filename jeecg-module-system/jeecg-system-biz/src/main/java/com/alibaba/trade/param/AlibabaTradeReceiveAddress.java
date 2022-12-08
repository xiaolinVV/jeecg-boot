package com.alibaba.trade.param;

public class AlibabaTradeReceiveAddress {

    private String address;

    /**
     * @return 街道地址，不包括省市编码
     */
    public String getAddress() {
        return address;
    }

    /**
     * 设置街道地址，不包括省市编码     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setAddress(String address) {
        this.address = address;
    }

    private String addressCode;

    /**
     * @return 地址区域编码
     */
    public String getAddressCode() {
        return addressCode;
    }

    /**
     * 设置地址区域编码     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setAddressCode(String addressCode) {
        this.addressCode = addressCode;
    }

    private String addressCodeText;

    /**
     * @return * 地址区域编码对应的文本（包括国家，省，城市）
     */
    public String getAddressCodeText() {
        return addressCodeText;
    }

    /**
     * 设置* 地址区域编码对应的文本（包括国家，省，城市）     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setAddressCodeText(String addressCodeText) {
        this.addressCodeText = addressCodeText;
    }

    private Long addressId;

    /**
     * @return 地址ID
     */
    public Long getAddressId() {
        return addressId;
    }

    /**
     * 设置地址ID     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    private String bizType;

    /**
     * @return 业务类型
     */
    public String getBizType() {
        return bizType;
    }

    /**
     * 设置业务类型     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    private Boolean isDefault;

    /**
     * @return 是否为默认地址
     */
    public Boolean getIsDefault() {
        return isDefault;
    }

    /**
     * 设置是否为默认地址     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    private String fullName;

    /**
     * @return 收货人姓名
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * 设置收货人姓名     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    private Boolean latest;

    /**
     * @return 是否最近使用的地址
     */
    public Boolean getLatest() {
        return latest;
    }

    /**
     * 设置是否最近使用的地址     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setLatest(Boolean latest) {
        this.latest = latest;
    }

    private String mobile;

    /**
     * @return 手机
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * 设置手机     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    private String phone;

    /**
     * @return 电话
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置电话     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    private String postCode;

    /**
     * @return 邮编
     */
    public String getPostCode() {
        return postCode;
    }

    /**
     * 设置邮编     *
     * 参数示例：<pre></pre>     
     * 此参数必填
     */
    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

}
