package org.jeecg.modules.store.api;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.store.entity.StoreCashierSetting;
import org.jeecg.modules.store.entity.StoreManage;
import org.jeecg.modules.store.service.IStoreCashierSettingService;
import org.jeecg.modules.store.service.IStoreManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("back/storeCashierSetting")
public class BackStoreCashierSettingController {

    @Autowired
    private IStoreCashierSettingService iStoreCashierSettingService;
    @Autowired
    private IStoreManageService iStoreManageService;

    /**
     * 获取店铺收银信息
     *
     * @param sysUserId
     * @return
     */
    @PostMapping("getPresentProportionById")
    public Result<?> getPresentProportionById(@RequestAttribute("sysUserId") String sysUserId){
        StoreManage storeManage=iStoreManageService.getOne(new LambdaQueryWrapper<StoreManage>()
                .eq(StoreManage::getSysUserId,sysUserId)
                .in(StoreManage::getPayStatus,"1","2")
                .eq(StoreManage::getStatus,"1")
                .last("limit 1"));
        if(storeManage!=null){
            StoreCashierSetting storeCashierSetting=iStoreCashierSettingService.getOne(new LambdaQueryWrapper<StoreCashierSetting>()
                    .eq(StoreCashierSetting::getStoreManageId,storeManage.getId()));
            if(storeCashierSetting!=null){
                return Result.ok(storeCashierSetting);
            }
        }
        return Result.ok("暂无数据");
    }



    /**
     * 修改收银积分
     *
     * @param sysUserId
     * @param presentProportion
     * @return
     */
    @PostMapping("updatePresentProportion")
    public Result<?> updatePresentProportion(@RequestAttribute("sysUserId") String sysUserId, BigDecimal presentProportion,String isIntegral){
        StoreManage storeManage=iStoreManageService.getOne(new LambdaQueryWrapper<StoreManage>()
                .eq(StoreManage::getSysUserId,sysUserId)
                .in(StoreManage::getPayStatus,"1","2")
                .eq(StoreManage::getStatus,"1")
                .last("limit 1"));
        if(storeManage==null){
            return Result.error("未找到相关店铺");
        }
        StoreCashierSetting storeCashierSetting=iStoreCashierSettingService.getOne(new LambdaQueryWrapper<StoreCashierSetting>()
                .eq(StoreCashierSetting::getStoreManageId,storeManage.getId()));
        if(storeCashierSetting==null){
            return Result.error("商户未开启收银功能");
        }
        if(presentProportion.doubleValue()<storeCashierSetting.getMinPresentProportion().doubleValue()){
            return Result.error("积分比例不得低于："+storeCashierSetting.getMinPresentProportion()+"%");
        }
        storeCashierSetting.setIsIntegral(isIntegral);
        storeCashierSetting.setPresentProportion(presentProportion);
        iStoreCashierSettingService.saveOrUpdate(storeCashierSetting);
        return Result.ok("积分设置成功");
    }
}
