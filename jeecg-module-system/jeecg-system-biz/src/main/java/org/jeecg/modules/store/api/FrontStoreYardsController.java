package org.jeecg.modules.store.api;


import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.store.service.IStoreYardsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("front/storeYards")
public class FrontStoreYardsController {


    @Autowired
    private IStoreYardsService iStoreYardsService;


    /**
     * 根据id获取店铺二维码的信息
     *
     * @param id
     * @return
     */
    @PostMapping("getStoreYardsById")
    public Result<?> getStoreYardsById(String id){
        return Result.ok(iStoreYardsService.getById(id));
    }
}
