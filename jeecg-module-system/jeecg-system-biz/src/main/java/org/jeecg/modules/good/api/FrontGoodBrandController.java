package org.jeecg.modules.good.api;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Maps;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.good.entity.GoodBrand;
import org.jeecg.modules.good.entity.GoodMachineBrand;
import org.jeecg.modules.good.entity.GoodMachineModel;
import org.jeecg.modules.good.service.IGoodBrandService;
import org.jeecg.modules.good.service.IGoodMachineBrandService;
import org.jeecg.modules.good.service.IGoodMachineModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("front/goodBrand")
public class FrontGoodBrandController {

    @Autowired
    private IGoodBrandService iGoodBrandService;

    @Autowired
    private IGoodMachineModelService iGoodMachineModelService;

    @Autowired
    private IGoodMachineBrandService iGoodMachineBrandService;


    /**
     * 获取商品品牌列表
     *
     * @return
     */
    @PostMapping("getGoodBrandList")
    public Result<?> getGoodBrandList(){
        Map<String,Object> resultMap= Maps.newHashMap();
        resultMap.put("goodBrandList",iGoodBrandService.list(new LambdaQueryWrapper<GoodBrand>().orderByAsc(GoodBrand::getSort)));
        resultMap.put("goodMachineBrandList",iGoodMachineBrandService.list(new LambdaQueryWrapper<GoodMachineBrand>().orderByAsc(GoodMachineBrand::getSort)));
        resultMap.put("goodMachineModelList",iGoodMachineModelService.list(new LambdaQueryWrapper<GoodMachineModel>().orderByAsc(GoodMachineModel::getSort)));
        return Result.ok(resultMap);
    }


}
