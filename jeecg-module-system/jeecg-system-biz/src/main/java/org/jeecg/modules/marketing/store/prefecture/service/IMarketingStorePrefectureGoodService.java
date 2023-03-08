package org.jeecg.modules.marketing.store.prefecture.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.store.prefecture.entity.MarketingStorePrefectureGood;
import org.jeecg.modules.member.entity.MemberShoppingCart;
import org.jeecg.modules.order.entity.OrderStoreList;

import java.util.Map;

/**
 * @Description: 店铺专区商品
 * @Author: jeecg-boot
 * @Date:   2022-12-10
 * @Version: V1.0
 */
public interface IMarketingStorePrefectureGoodService extends IService<MarketingStorePrefectureGood> {

    /**
     * 后端列表查询
     *
     * @param page
     * @param wrapper
     * @param goodName
     * @return
     */
    public IPage<Map<String,Object>> queryPageList(Page<Map<String,Object>> page,QueryWrapper wrapper,String goodName);



    /**
     * 专区商品列表
     *
     * @param page
     * @return
     */
    public IPage<Map<String,Object>> getMarketingStorePrefectureGoodList(Page<Map<String,Object>> page,String marketingStorePrefectureGoodListId);

    /**
     * 设置商品详情参数
     *
     * @param objectMap
     * @param id
     */
    public void settingGoodInfo(Map<String,Object> objectMap,String id);

    /*设置规格获取数据*/
    public void settingGoodSpecification(Map<String,Object> objectMap,String id,String specification);

    /**
     * 设置购物车数据
     *
     * @param memberShoppingCart
     * @param id
     * @param specification
     */
    public void settingMemberShoppingCar(MemberShoppingCart memberShoppingCart,String id,String specification);

    /**
     * 设置获取的购物车信息
     *
     * @param objectMap
     * @param id
     * @param specification
     */
    public void settingGetMemberShoppingCarInfo(Map<String,Object> objectMap,String id,String specification);

    /**
     * 交易成功的回调
     *
     * @param orderStoreList
     */
    public void success(OrderStoreList orderStoreList,String payOrderCarLogId);
}
