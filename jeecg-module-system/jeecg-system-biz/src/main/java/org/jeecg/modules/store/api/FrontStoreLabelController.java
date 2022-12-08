package org.jeecg.modules.store.api;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.store.entity.StoreLabel;
import org.jeecg.modules.store.service.IStoreLabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 标签控制器
 */

@Controller
@RequestMapping("front/storeLabel")
public class FrontStoreLabelController {

    @Autowired
    private IStoreLabelService iStoreLabelService;


    /**
     * 获取推荐标签信息
     * @return
     */
    @RequestMapping("getLabelRecommend")
    @ResponseBody
    public Result<?> getLabelRecommend(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                        @RequestParam(name="pageSize", defaultValue="10") Integer pageSize){
        return Result.ok(iStoreLabelService.page(new Page<>(pageNo,pageSize),new LambdaQueryWrapper<StoreLabel>()
                .eq(StoreLabel::getStatus,"1")
                .eq(StoreLabel::getIsRecommend,"1")
                .orderByAsc(StoreLabel::getSort)));
    }

    /**
     * 获取标签列表
     * @return
     */
    @RequestMapping("getLabelList")
    @ResponseBody
    public Result<?> getLabelList(){
        return Result.ok(iStoreLabelService.list(new LambdaQueryWrapper<StoreLabel>()
                .eq(StoreLabel::getStatus,"1")
                .orderByAsc(StoreLabel::getSort)));
    }
}
