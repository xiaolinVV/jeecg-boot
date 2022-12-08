package org.jeecg.modules.store.api;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.store.entity.StoreSeriesAdvertising;
import org.jeecg.modules.store.service.IStoreSeriesAdvertisingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("front/storeSeriesAdvertising")
public class FrontStoreSeriesAdvertisingController {


    @Autowired
    private IStoreSeriesAdvertisingService iStoreSeriesAdvertisingService;

    /**
     * 获取广告列表
     *
     * @return
     */
    @PostMapping("getStoreSeriesAdvertising")
    public Result<?> getStoreSeriesAdvertising(String storeSeriesManageId){
        return Result.ok(iStoreSeriesAdvertisingService.list(new LambdaQueryWrapper<StoreSeriesAdvertising>()
                .eq(StoreSeriesAdvertising::getStatus,"1")
                .eq(StoreSeriesAdvertising::getStoreSeriesManageId,storeSeriesManageId)
                .ge(StoreSeriesAdvertising::getEndTime,new Date())
                .le(StoreSeriesAdvertising::getStartTime,new Date())
                .orderByAsc(StoreSeriesAdvertising::getSort)));
    }
}
