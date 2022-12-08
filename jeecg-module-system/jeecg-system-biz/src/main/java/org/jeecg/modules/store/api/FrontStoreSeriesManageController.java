package org.jeecg.modules.store.api;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.store.service.IStoreSeriesManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("front/storeSeriesManage")
public class FrontStoreSeriesManageController {


    @Autowired
    private IStoreSeriesManageService iStoreSeriesManageService;

    /**
     * 根据父类id获取数据
     *
     * @param parentId
     * @param pageNo
     * @param pageSize
     * @return
     */
    @PostMapping("getStoreSeriesManageByParentId")
    public Result<?> getStoreSeriesManageByParentId(String parentId,@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                    @RequestParam(name="pageSize", defaultValue="10") Integer pageSize){
        return Result.ok(iStoreSeriesManageService.getStoreSeriesManageByParentId(new Page<>(pageNo,pageSize),parentId));
    }

    /**
     * 根据id获取详情
     * @param id
     * @return
     */
    @PostMapping("getStoreSeriesManageById")
    public Result<?> getStoreSeriesManageById(String id){
        return Result.ok(iStoreSeriesManageService.getById(id));
    }
}
