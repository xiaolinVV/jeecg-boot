package org.jeecg.modules.store.api;


import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.store.service.IStoreTablesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("after/storeTables")
public class AfterStoreTablesController {

    @Autowired
    private IStoreTablesService iStoreTablesService;


    /**
     * 根据桌台二维码获取店铺需要的信息
     *
     * @param id
     * @return
     */
    @PostMapping("getStoreTablesInfoById")
    public Result<?> getStoreTablesInfoById(String id){
        return Result.ok();
    }
}
