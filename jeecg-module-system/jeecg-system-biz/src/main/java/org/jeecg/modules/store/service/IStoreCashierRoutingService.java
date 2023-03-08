package org.jeecg.modules.store.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.order.entity.OrderStoreList;
import org.jeecg.modules.pay.entity.PayShouyinLog;
import org.jeecg.modules.store.dto.StoreCashierRoutingDTO;
import org.jeecg.modules.store.entity.StoreCashierRouting;

import java.util.List;
import java.util.Map;

/**
 * @Description: 店铺收银分账设置
 * @Author: jeecg-boot
 * @Date:   2022-06-06
 * @Version: V1.0
 */
public interface IStoreCashierRoutingService extends IService<StoreCashierRouting> {



    public Result<?> settingBankCard(StoreCashierRouting storeCashierRouting);

    /**
     *
     * 后台列表查询
     *
     * @param page
     * @param wrapper
     * @return
     */
    public IPage<StoreCashierRoutingDTO> queryPageList(Page<StoreCashierRoutingDTO> page, QueryWrapper wrapper);


    /**
     * 收银实际分账
     *
     * @param payShouyinLog
     * @param
     * @return
     */
    public List<Map<String, String>>  independentAccountShouYin(PayShouyinLog payShouyinLog);


    /**
     * 收银余额分账
     *
     * @param payShouyinLog
     * @param
     * @return
     */
    public void   independentAccountShouYinBalance(PayShouyinLog payShouyinLog);


    /**
     * 订单实际分账
     *
     * @param orderStoreListId
     * @param
     * @return
     */
    public List<Map<String, String>>  independentAccountOrder(List<Map<String, String>> divMembers,String orderStoreListId);


    /**
     * 订单余额分账
     *
     * @param orderStoreList
     * @param
     * @return
     */
    public void   independentAccountOrderBalance(OrderStoreList orderStoreList);


}
