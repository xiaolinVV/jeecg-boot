package org.jeecg.modules.marketing.store.prefecture.api;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.marketing.store.prefecture.service.IMarketingStorePrefectureGiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("after/marketingStorePrefectureGive")
public class AfterMarketingStorePrefectureGiveController {



    @Autowired
    private IMarketingStorePrefectureGiveService iMarketingStorePrefectureGiveService;


    /**
     * 获取限购信息列表
     *
     * @param pageNo
     * @param pageSize
     * @param memberId
     * @return
     */
    @PostMapping("getMarketingStorePrefectureGiveList")
    public Result<?> getMarketingStorePrefectureGiveList(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                         @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                         @RequestAttribute("memberId") String memberId){
        return Result.ok(iMarketingStorePrefectureGiveService.getMarketingStorePrefectureGiveList(new Page<>(pageNo,pageSize),memberId));
    }
}
