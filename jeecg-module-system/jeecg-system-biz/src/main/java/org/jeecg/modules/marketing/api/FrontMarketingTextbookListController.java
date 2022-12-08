package org.jeecg.modules.marketing.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.marketing.entity.MarketingTextbookList;
import org.jeecg.modules.marketing.service.IMarketingTextbookListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Map;

/**
 * 素材库栏目接口控制层
 */
@RequestMapping("front/marketingTextbookList")
@Controller
public class FrontMarketingTextbookListController {
    @Autowired
    private IMarketingTextbookListService iMarketingTextbookListService;

    @RequestMapping("findMarketingTextbookList")
    @ResponseBody
    public Result<IPage<Map<String,Object>>> findMarketingTextbookList(String id,
                                                                       @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                                                       @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                                                       HttpServletRequest request){
        Result<IPage<Map<String,Object>>> result=new Result<>();
        Page<Map<String,Object>> page = new Page<>(pageNo, pageSize);
        result.setResult(iMarketingTextbookListService.findMarketingTextbookList(page,id));
        result.success("返回素材列表");
        return result;
    }
    @RequestMapping("findMarketingTextBookById")
    @ResponseBody
    public Result<Map<String,Object>> findMarketingTextBookById(String id,
                                                                HttpServletRequest request){
        Result<Map<String, Object>> result = new Result<>();
        MarketingTextbookList marketingTextbookList = iMarketingTextbookListService.getById(id);
        iMarketingTextbookListService.saveOrUpdate(marketingTextbookList.setInitialViews(marketingTextbookList.getInitialViews().add(new BigDecimal(1))));
        result.setResult(iMarketingTextbookListService.findMarketingTextBookById(id));
        result.success("返回教程详情");
        return result;
    }

    @RequestMapping("findMarketingTextBookByTitle")
    @ResponseBody
    public Result<IPage<Map<String,Object>>> findMarketingTextBookByTitle(String title,
                                                                          @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                                                          @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                                                          HttpServletRequest request){
        Result<IPage<Map<String,Object>>> result = new Result<>();
        Page<Map<String,Object>> page = new Page<>(pageNo, pageSize);
        result.setResult(iMarketingTextbookListService.findMarketingTextBookByTitle(page,title));
        result.success("通过标题查出列表");
        return result;
    }
}
