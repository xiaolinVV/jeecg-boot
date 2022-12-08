package org.jeecg.modules.marketing.api;


import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.marketing.service.IMarketingPrefectureTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * 平台专区类型api
 */

@Controller
@RequestMapping("back/marketingPrefectureType")
public class BackMarketingPrefectureTypeController {

    @Autowired
    private IMarketingPrefectureTypeService iMarketingPrefectureTypeService;


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

        result.setResult(iMarketingPrefectureTypeService.findByMarketingPrefectureId(id));


        result.success("平台专区类型查询成功！！！");
        return result;
    }

}
