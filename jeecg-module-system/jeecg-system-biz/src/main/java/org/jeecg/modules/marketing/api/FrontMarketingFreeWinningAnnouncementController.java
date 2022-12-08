package org.jeecg.modules.marketing.api;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.marketing.service.IMarketingFreeWinningAnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("front/marketingFreeWinningAnnouncement")
@Controller
public class FrontMarketingFreeWinningAnnouncementController {

    @Autowired
    private IMarketingFreeWinningAnnouncementService iMarketingFreeWinningAnnouncementService;

    /**
     * 获取中奖公告列表
     *
     * @return
     */
    @RequestMapping("getMarketingFreeWinningAnnouncementList")
    @ResponseBody
    public Result<?> getMarketingFreeWinningAnnouncementList(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                                  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize){
        return Result.ok(iMarketingFreeWinningAnnouncementService.getMarketingFreeWinningAnnouncementList(new Page<>(pageNo,pageSize)));
    }

}
