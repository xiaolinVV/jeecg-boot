package org.jeecg.modules.marketing.api;


import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.marketing.entity.MarketingPrefecture;
import org.jeecg.modules.marketing.service.IMarketingPrefectureService;
import org.jeecg.modules.marketing.service.IMarketingPrefectureTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 平台专区类型api
 */

@Controller
@RequestMapping("front/marketingPrefectureType")
public class FrontMarketingPrefectureTypeController {

    @Autowired
    private IMarketingPrefectureTypeService iMarketingPrefectureTypeService;

    @Autowired
    private IMarketingPrefectureService iMarketingPrefectureService;


    /**
     * 根据平台专区id查询专区类型
     * @param id
     * @return
     */
    @RequestMapping("findByMarketingPrefectureId")
    @ResponseBody
    public Result<List<Map<String,Object>>> findByMarketingPrefectureId(String id){
        Result<List<Map<String,Object>>>result=new Result<>();
        //参数判断
        if(StringUtils.isBlank(id)){
            result.error500("平台专区id不能为空");
            return result;
        }
        MarketingPrefecture marketingPrefecture = iMarketingPrefectureService.getById(id);
        if (marketingPrefecture.getIsViewType().equals("1")){
            result.setResult(iMarketingPrefectureTypeService.findByMarketingPrefectureId(id));
        }else {
            ArrayList<Map<String, Object>> maps = new ArrayList<>();
            result.setResult(maps);
        }

        result.success("平台专区类型查询成功！！！");
        return result;
    }

    /**
     * 获取二级分类
     * @param id
     * @return
     */
    @RequestMapping("findMarketingPrefectureTypeTwoById")
    @ResponseBody
    public Result<List<Map<String,Object>>> findMarketingPrefectureTypeTwoById(String id){
        Result<List<Map<String, Object>>> result = new Result<>();
        result.setResult(iMarketingPrefectureTypeService.findMarketingPrefectureTypeTwoById(id));
        return result;
    }
}
