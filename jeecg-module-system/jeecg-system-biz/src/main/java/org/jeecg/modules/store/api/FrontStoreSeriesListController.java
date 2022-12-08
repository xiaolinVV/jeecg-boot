package org.jeecg.modules.store.api;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.store.service.IStoreSeriesListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("front/storeSeriesList")
public class FrontStoreSeriesListController {

    @Autowired
    private IStoreSeriesListService iStoreSeriesListService;


    /**
     * 根据系列id获取店铺信息
     *
     * @param storeSeriesManageId
     * @param pageNo
     * @param pageSize
     * @return
     */
    @PostMapping("getStoreSeriesListByStoreManageId")
    public Result<?> getStoreSeriesListByStoreManageId(String storeSeriesManageId,@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                       @RequestParam(name="pageSize", defaultValue="10") Integer pageSize){

        return Result.ok(iStoreSeriesListService.getStoreSeriesListByStoreManageId(new Page<>(pageNo,pageSize),storeSeriesManageId));
    }
}
