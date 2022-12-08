package org.jeecg.modules.marketing.api;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.good.service.IGoodStoreListService;
import org.jeecg.modules.marketing.service.IMarketingRecommendTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;


/**
 * 推荐分类api
 */
@RequestMapping("front/marketingRecommendType")
@Controller
public class FrontMarketingRecommendTypeController {

    @Autowired
    private IMarketingRecommendTypeService iMarketingRecommendTypeService;

    @Autowired
    private IGoodStoreListService iGoodStoreListService;

    /**
     * 查询推荐分类信息
     * @return
     */
    @RequestMapping("findTypeList")
    @ResponseBody
    public Result<List<Map<String,Object>>> findTypeList(@RequestHeader(defaultValue = "") String sysUserId){
        Result<List<Map<String,Object>>> result=new Result<>();
        List<Map<String,Object>> goodTypes= Lists.newArrayList();

        if(StringUtils.isNotBlank(sysUserId)){
            Page<Map<String,Object>> page = new Page<Map<String,Object>>(1, 1);
            long size=iGoodStoreListService.findGoodListBySysUserId(page,sysUserId).getTotal();
            if(size>0) {
                //店铺分类
                Map<String, Object> goodTypeMap = Maps.newHashMap();
                goodTypeMap.put("goodTypeId", sysUserId);
                goodTypeMap.put("isPlatform", 0);
                goodTypeMap.put("nickName", "店铺商品");
                goodTypes.add(goodTypeMap);
            }
        }

        //添加推荐分类数据
        CollectionUtils.addAll(goodTypes,iMarketingRecommendTypeService.findMarketingRecommendTypes().iterator());

        result.setResult(goodTypes);
        result.success("查询推荐分类信息成功");
        return result;
    }
 }
