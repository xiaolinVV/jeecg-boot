package org.jeecg.modules.store.api;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.store.entity.StoreManage;
import org.jeecg.modules.store.entity.StoreShouyinRecord;
import org.jeecg.modules.store.service.IStoreManageService;
import org.jeecg.modules.store.service.IStoreShouyinRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.Map;

@RestController
@RequestMapping("back/storeShouyinRecord")
public class BackStoreShouyinRecordController {


    @Autowired
    private IStoreManageService iStoreManageService;

    @Autowired
    private IStoreShouyinRecordService iStoreShouyinRecordService;


    /**
     * 获取收银记录列表
     *
     * @return
     */
    @PostMapping("getStoreShouyinRecordList")
    public Result<?> getStoreShouyinRecordList(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                               @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                               @RequestAttribute("sysUserId") String sysUserId){
        StoreManage storeManage=iStoreManageService.getOne(new LambdaQueryWrapper<StoreManage>()
                .eq(StoreManage::getSysUserId,sysUserId)
                .in(StoreManage::getPayStatus,"1","2")
                .eq(StoreManage::getStatus,"1")
                .last("limit 1"));
        if(storeManage==null){
            return Result.error("未找到相关店铺");
        }
        return Result.ok(iStoreShouyinRecordService.page(new Page<>(pageNo,pageSize),new LambdaQueryWrapper<StoreShouyinRecord>()
                .eq(StoreShouyinRecord::getStoreManageId,storeManage.getId())
                .orderByDesc(StoreShouyinRecord::getCreateTime)));
    }





    /**
     *
     * 收银统计
     *
     * @param sysUserId
     * @return
     */
    @PostMapping("statistics")
    public Result<?> statistics(@RequestAttribute("sysUserId") String sysUserId){
        Map<String,Object> resultMap= Maps.newHashMap();
        Calendar calendar=Calendar.getInstance();
        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH)+1;
        int day=calendar.get(Calendar.DAY_OF_MONTH);
        Map<String,Object> paramMap=Maps.newHashMap();
        StoreManage storeManage=iStoreManageService.getOne(new LambdaQueryWrapper<StoreManage>()
                .eq(StoreManage::getSysUserId,sysUserId)
                .in(StoreManage::getPayStatus,"1","2")
                .eq(StoreManage::getStatus,"1")
                .last("limit 1"));
        if(storeManage==null){
            return Result.error("未找到相关店铺");
        }
        paramMap.put("storeManageId",storeManage.getId());
        //累计统计
        resultMap.put("statistics",iStoreShouyinRecordService.statistics(paramMap));

        //本月统计
        paramMap.put("year",year);
        paramMap.put("month",month);
        resultMap.put("monthStatistics",iStoreShouyinRecordService.statistics(paramMap));

        //今日统计
        paramMap.put("day",day);
        resultMap.put("dayStatistics",iStoreShouyinRecordService.statistics(paramMap));

        return Result.ok(resultMap);
    }

}
