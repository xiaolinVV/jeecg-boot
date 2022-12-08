package org.jeecg.modules.marketing.api;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.marketing.service.IMarketingTextbookColumnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 素材库栏目接口控制层
 */
@RequestMapping("front/marketingTextbookColumn")
@Controller
public class FrontMarketingTextbookColumnController {
    @Autowired
    private IMarketingTextbookColumnService iMarketingTextbookColumnService;

    @RequestMapping("findMarketingTextbookColumnList")
    @ResponseBody
    public Result<List<Map<String,Object>>> findMarketingTextbookColumnList(HttpServletRequest request){
        Result<List<Map<String,Object>>> result=new Result<>();
        result.setResult(iMarketingTextbookColumnService.findMarketingTextbookColumn());
        result.success("返回栏目");
        return result;
    }
}
