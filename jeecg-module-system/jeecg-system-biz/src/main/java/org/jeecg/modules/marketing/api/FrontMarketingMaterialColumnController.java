package org.jeecg.modules.marketing.api;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.marketing.service.IMarketingMaterialColumnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * 素材库栏目接口控制层
 */
@RequestMapping("front/marketingMaterialColumn")
@Controller
public class FrontMarketingMaterialColumnController {

    @Autowired
   private IMarketingMaterialColumnService iMarketingMaterialColumnService;

    /**
     * 查询素材栏目名称
     * @return
     */
    @RequestMapping("getMarketingMaterialColumnNameList")
    @ResponseBody
    public Result<List<Map<String,Object>>> getMarketingMaterialColumnNameList(){
        Result<List<Map<String,Object>>> result=new Result<>();
        List<Map<String,Object>> mapList= Lists.newArrayList();
        //加上推荐栏目
        Map<String, Object> objectMap = Maps.newHashMap();
        objectMap.put("name", "推荐");
        objectMap.put("id", "");
        mapList.add(objectMap);

        //加入素材栏目
        CollectionUtils.addAll(mapList,  iMarketingMaterialColumnService.getMarketingMaterialColumnName().iterator());

        result.setResult(mapList);

        result.success("查询素材栏目数据成功");
        return  result;
    }





}
