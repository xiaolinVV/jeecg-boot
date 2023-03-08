package org.jeecg.modules.marketing.store.prefecture.api;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.marketing.store.prefecture.service.IMarketingStorePrefectureGoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("front/marketingStorePrefectureGood")
public class FrontMarketingStorePrefectureGoodController {


    @Autowired
    private IMarketingStorePrefectureGoodService iMarketingStorePrefectureGoodService;


    /**
     * 获取专区商品列表
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    @PostMapping("getMarketingStorePrefectureGoodList")
    public Result<?> getMarketingStorePrefectureGoodList(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                         @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                         String marketingStorePrefectureGoodListId){

        return Result.ok(iMarketingStorePrefectureGoodService.getMarketingStorePrefectureGoodList(new Page<>(pageNo,pageSize),marketingStorePrefectureGoodListId));
    }
}
