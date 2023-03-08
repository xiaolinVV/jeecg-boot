package org.jeecg.modules.store.api;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Maps;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.store.entity.StoreCashierRouting;
import org.jeecg.modules.store.entity.StoreManage;
import org.jeecg.modules.store.service.IStoreCashierRoutingService;
import org.jeecg.modules.store.service.IStoreManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("back/storeCashierRouting")
public class BackStoreCashierRoutingController {

    @Autowired
    private IStoreCashierRoutingService iStoreCashierRoutingService;

    @Autowired
    private IStoreManageService iStoreManageService;


    /**
     * 获取店铺收银配置
     *
     * @param sysUserId
     * @return
     */
    @PostMapping("getStoreCashierRoutingByStore")
    public Result<?> getStoreCashierRoutingByStore(@RequestHeader(name = "sysUserId",defaultValue = "") String sysUserId){

        Map<String,Object> resultMap= Maps.newHashMap();

        StoreManage storeManage=iStoreManageService.getStoreManageBySysUserId(sysUserId);
        if(storeManage==null){
            return Result.error("未找到商户");
        }
        StoreCashierRouting storeCashierRouting=iStoreCashierRoutingService.getOne(new LambdaQueryWrapper<StoreCashierRouting>()
                .eq(StoreCashierRouting::getStoreManageId,storeManage.getId())
                .eq(StoreCashierRouting::getFashionableType,"0")
                .eq(StoreCashierRouting::getRoleType,"1")
                .eq(StoreCashierRouting::getIsStore,"1")
                .last("limit 1"));
        resultMap.put("storeCashierRouting",storeCashierRouting);
        return Result.ok(resultMap);
    }

    /**
     * 修改收银的分账银行卡信息
     *
     * @param sysUserId
     * @param bankType
     * @return
     */
    @PostMapping("updateStoreCashierRoutingByBankCard")
    public Result<?> updateStoreCashierRoutingByBankCard(@RequestHeader(name = "sysUserId",defaultValue = "") String sysUserId,String bankType){
        StoreManage storeManage=iStoreManageService.getStoreManageBySysUserId(sysUserId);
        if(storeManage==null){
            return Result.error("未找到商户");
        }
        StoreCashierRouting storeCashierRouting=iStoreCashierRoutingService.getOne(new LambdaQueryWrapper<StoreCashierRouting>()
                .eq(StoreCashierRouting::getStoreManageId,storeManage.getId())
                .eq(StoreCashierRouting::getFashionableType,"0")
                .eq(StoreCashierRouting::getRoleType,"1")
                .eq(StoreCashierRouting::getIsStore,"1")
                .last("limit 1"));
        //余额
        if(bankType.equals("1")){
            storeCashierRouting.setBankCard(null);
            storeCashierRouting.setAccountType("2");
            iStoreCashierRoutingService.saveOrUpdate(storeCashierRouting);
            return Result.ok("修改成功");
        }
        //银行卡
        if(bankType.equals("0")){
            return iStoreCashierRoutingService.settingBankCard(storeCashierRouting);
        }
        return Result.error("未知错误");
    }
}
