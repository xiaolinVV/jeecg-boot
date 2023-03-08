package org.jeecg.modules.marketing.store.prefecture.api;


import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.marketing.store.prefecture.service.IMarketingStorePrefectureListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("front/marketingStorePrefectureList")
public class FrontMarketingStorePrefectureListController {

    @Autowired
    private IMarketingStorePrefectureListService iMarketingStorePrefectureListService;

    /**
     * 根据id获取专区信息
     *
     * @param marketingStorePrefectureListId
     * @return
     */
    @PostMapping("getMarketingStorePrefectureListById")
    public Result<?> getMarketingStorePrefectureListById(String marketingStorePrefectureListId){
        return Result.ok(iMarketingStorePrefectureListService.getById(marketingStorePrefectureListId));
    }

}
