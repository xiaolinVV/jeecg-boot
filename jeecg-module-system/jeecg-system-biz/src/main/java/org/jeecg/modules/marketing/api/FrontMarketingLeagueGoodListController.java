package org.jeecg.modules.marketing.api;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.marketing.service.IMarketingLeagueGoodListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("front/marketingLeagueGoodList")
@Controller
public class FrontMarketingLeagueGoodListController {


    @Autowired
    private IMarketingLeagueGoodListService iMarketingLeagueGoodListService;


    /*
     * 根据类型查询商品列表
     *
     * */
    @RequestMapping("getMarketingLeagueGoodListByTypeId")
    @ResponseBody
    private Result<?> getMarketingLeagueGoodListByTypeId(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                         @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                         String typeId){
        return Result.ok(iMarketingLeagueGoodListService.getMarketingLeagueGoodListByTypeId(new Page<>(pageNo,pageSize),typeId));
    }



}
