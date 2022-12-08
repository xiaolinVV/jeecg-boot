package com.alibaba.p4p.param;

import com.alibaba.ocean.rawsdk.client.APIId;
import com.alibaba.ocean.rawsdk.common.AbstractAPIRequest;

public class AlibabaCpsOpSaveMediaUserBehaviourParam extends AbstractAPIRequest<AlibabaCpsOpSaveMediaUserBehaviourResult> {

    public AlibabaCpsOpSaveMediaUserBehaviourParam() {
        super();
        oceanApiId = new APIId("com.alibaba.p4p", "alibaba.cps.op.saveMediaUserBehaviour", 1);
    }

    private String uuid;

    /**
     * @return 代表唯一一条日志记录
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * 设置代表唯一一条日志记录     *
     * 参数示例：<pre>safafwfweqr2313fsafa</pre>     
     * 此参数必填
     */
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    private String deviceIdMd5;

    /**
     * @return 设备id求md5(32位小写)(手机号与设备号至少一个)
     */
    public String getDeviceIdMd5() {
        return deviceIdMd5;
    }

    /**
     * 设置设备id求md5(32位小写)(手机号与设备号至少一个)     *
     * 参数示例：<pre>xxxxxxx</pre>     
     * 此参数必填
     */
    public void setDeviceIdMd5(String deviceIdMd5) {
        this.deviceIdMd5 = deviceIdMd5;
    }

    private String phoneMd5;

    /**
     * @return 手机号求md5(32位小写)(手机号与设备号至少一个)
     */
    public String getPhoneMd5() {
        return phoneMd5;
    }

    /**
     * 设置手机号求md5(32位小写)(手机号与设备号至少一个)     *
     * 参数示例：<pre>xxxxxxxx</pre>     
     * 此参数必填
     */
    public void setPhoneMd5(String phoneMd5) {
        this.phoneMd5 = phoneMd5;
    }

    private Long actionTime;

    /**
     * @return 行为时间，13位时间戳精确到毫秒
     */
    public Long getActionTime() {
        return actionTime;
    }

    /**
     * 设置行为时间，13位时间戳精确到毫秒     *
     * 参数示例：<pre>1564653506976</pre>     
     * 此参数必填
     */
    public void setActionTime(Long actionTime) {
        this.actionTime = actionTime;
    }

    private String actionType;

    /**
     * @return 行为类型，1688定义枚举值： CLICK(点击商品详情),COMMENT(评论),STORE(收藏),CART(加购物车),SEARCH(关键词搜索，配合actionDetail),曝光(VIEW)
     */
    public String getActionType() {
        return actionType;
    }

    /**
     * 设置行为类型，1688定义枚举值： CLICK(点击商品详情),COMMENT(评论),STORE(收藏),CART(加购物车),SEARCH(关键词搜索，配合actionDetail),曝光(VIEW)     *
     * 参数示例：<pre>COMMENT</pre>     
     * 此参数必填
     */
    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    private String actionDetail;

    /**
     * @return 行为类型补充字段，部分类型配合使用，如搜索内容
     */
    public String getActionDetail() {
        return actionDetail;
    }

    /**
     * 设置行为类型补充字段，部分类型配合使用，如搜索内容     *
     * 参数示例：<pre>女装</pre>     
     * 此参数必填
     */
    public void setActionDetail(String actionDetail) {
        this.actionDetail = actionDetail;
    }

    private Integer feedType;

    /**
     * @return 商品类型,1:1688商品;2 机构商品;
     */
    public Integer getFeedType() {
        return feedType;
    }

    /**
     * 设置商品类型,1:1688商品;2 机构商品;     *
     * 参数示例：<pre>1</pre>     
     * 此参数必填
     */
    public void setFeedType(Integer feedType) {
        this.feedType = feedType;
    }

    private Long feedId;

    /**
     * @return 商品id,1688商品必须传商品id
     */
    public Long getFeedId() {
        return feedId;
    }

    /**
     * 设置商品id,1688商品必须传商品id     *
     * 参数示例：<pre>12314</pre>     
     * 此参数必填
     */
    public void setFeedId(Long feedId) {
        this.feedId = feedId;
    }

    private String feedTitle;

    /**
     * @return 商品标题
     */
    public String getFeedTitle() {
        return feedTitle;
    }

    /**
     * 设置商品标题     *
     * 参数示例：<pre>一件商品</pre>     
     * 此参数必填
     */
    public void setFeedTitle(String feedTitle) {
        this.feedTitle = feedTitle;
    }

    private Double feedPrice;

    /**
     * @return 售卖价格，单位元，两位小数
     */
    public Double getFeedPrice() {
        return feedPrice;
    }

    /**
     * 设置售卖价格，单位元，两位小数     *
     * 参数示例：<pre>11.11</pre>     
     * 此参数必填
     */
    public void setFeedPrice(Double feedPrice) {
        this.feedPrice = feedPrice;
    }

    private String feedCategory;

    /**
     * @return 商品类目，多级类目英文分号分割，按一级类目;二级类目;叶子类目格式传
     */
    public String getFeedCategory() {
        return feedCategory;
    }

    /**
     * 设置商品类目，多级类目英文分号分割，按一级类目;二级类目;叶子类目格式传     *
     * 参数示例：<pre>类目1;类目2;类目3</pre>     
     * 此参数必填
     */
    public void setFeedCategory(String feedCategory) {
        this.feedCategory = feedCategory;
    }

}
