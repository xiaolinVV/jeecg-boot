package org.jeecg.modules.marketing.store.prefecture.api;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.marketing.store.prefecture.entity.MarketingStorePrefectureGood;
import org.jeecg.modules.marketing.store.prefecture.entity.MarketingStorePrefectureRecord;
import org.jeecg.modules.marketing.store.prefecture.service.IMarketingStorePrefectureGoodService;
import org.jeecg.modules.marketing.store.prefecture.service.IMarketingStorePrefectureRecordService;
import org.jeecg.modules.store.entity.StoreFranchiser;
import org.jeecg.modules.store.entity.StoreManage;
import org.jeecg.modules.store.service.IStoreFranchiserService;
import org.jeecg.modules.store.service.IStoreManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("after/marketingStorePrefectureList")
public class AfterMarketingStorePrefectureListController {


    @Autowired
    private IStoreFranchiserService iStoreFranchiserService;

    @Autowired
    private IStoreManageService iStoreManageService;

    @Autowired
    private IMarketingStorePrefectureGoodService iMarketingStorePrefectureGoodService;

    @Autowired
    private IMarketingStorePrefectureRecordService iMarketingStorePrefectureRecordService;

    /**
     * 是否可以分享
     *
     * @param marketingStorePrefectureGoodId
     * @param sysUserId
     * @param memberId
     * @return
     */
    @RequestMapping("isShareMarketingStorePrefectureList")
    public Result<?> isShareMarketingStorePrefectureList(String marketingStorePrefectureGoodId,String sysUserId,
                             @RequestAttribute("memberId") String memberId){
        StoreManage storeManage=iStoreManageService.getStoreManageBySysUserId(sysUserId);
        /*判断用户是否是经销商*/
        long storeFranchiserCount=iStoreFranchiserService.count(new LambdaQueryWrapper<StoreFranchiser>()
                .eq(StoreFranchiser::getStoreManageId,storeManage.getId())
                .eq(StoreFranchiser::getMemberListId,memberId));
        if(storeFranchiserCount>0){
            return Result.ok();
        }
        MarketingStorePrefectureGood marketingStorePrefectureGood=iMarketingStorePrefectureGoodService.getById(marketingStorePrefectureGoodId);
        long recordCount=iMarketingStorePrefectureRecordService.count(new LambdaQueryWrapper<MarketingStorePrefectureRecord>()
                        .eq(MarketingStorePrefectureRecord::getMemberListId,memberId)
                .eq(MarketingStorePrefectureRecord::getMarketingStorePrefectureListId,marketingStorePrefectureGood.getMarketingStorePrefectureListId()));
        if(recordCount>0){
            return Result.ok();
        }
        return Result.error("本专区商品您暂无分享权限，请先获取到权限哦！！！");
    }
}
