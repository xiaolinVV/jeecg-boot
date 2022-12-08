package org.jeecg.modules.marketing.api;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.marketing.service.IMarketingFreeGoodListService;
import org.jeecg.modules.system.service.ISysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * 免单商品接口
 */

@RequestMapping("front/marketingFreeGoodList")
@Controller
public class FrontMarketingFreeGoodListController {

    @Autowired
    private IMarketingFreeGoodListService iMarketingFreeGoodListService;

    @Autowired
    private ISysDictService iSysDictService;


    /**
     * 根据免单活动类型id查询商品列表
     *
     * @param marketingFreeGoodTypeId
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("selectMarketingFreeGoodListByMarketingFreeGoodTypeId")
    @ResponseBody
    public Result<IPage<Map<String,Object>>> selectMarketingFreeGoodListByMarketingFreeGoodTypeId(String marketingFreeGoodTypeId,@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                                                                  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize){
        Result<IPage<Map<String,Object>>> result=new Result<>();

        //组织查询参数
        if(StringUtils.isBlank(marketingFreeGoodTypeId)){
            result.error500("免单活动类型id不能为空！！！");
            return  result;
        }
        Page<Map<String,Object>> page = new Page<Map<String,Object>>(pageNo, pageSize);

        Map<String,Object> paramMap=Maps.newHashMap();
        //1  商品排序方式：是否启用排序值排序。0：停用（停用后客户端商品列表按照默认原系统的排序方式）；1：启用（启用后客户端商品列表排序按照排序值升序排序）；
        String goodsSortType = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "goods_sort_type");
        paramMap.put("pattern",goodsSortType);
        paramMap.put("marketingFreeGoodTypeId",marketingFreeGoodTypeId);

        result.setResult(iMarketingFreeGoodListService.selectMarketingFreeGoodListByMarketingFreeGoodTypeId(page,paramMap));

        result.success("活动商品列表查询成功!!!");
        return result;
    }




    /**
     *  通过模糊查询商品列表
     *
     * @param pattern   0:综合；1：销量；2：最新;3:价格降序；4：价格升序
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("selectMarketingFreeGoodListBySearch")
    @ResponseBody
    public Result<IPage<Map<String,Object>>> selectMarketingFreeGoodListBySearch(String search, Integer pattern, @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                            @RequestParam(name="pageSize", defaultValue="10") Integer pageSize){
        Result<IPage<Map<String,Object>>> result=new Result<>();
        //组织查询参数
        Page<Map<String,Object>> page = new Page<Map<String,Object>>(pageNo, pageSize);
        Map<String,Object> paramObjectMap= Maps.newHashMap();
        paramObjectMap.put("search",search);
        paramObjectMap.put("pattern",pattern);
        //1  商品排序方式：是否启用排序值排序。0：停用（停用后客户端商品列表按照默认原系统的排序方式）；1：启用（启用后客户端商品列表排序按照排序值升序排序）；
        String goodsSortType = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "goods_sort_type");
        if(goodsSortType.equals("1")&&pattern.equals("0")){
            paramObjectMap.put("pattern",5);
        }
        result.setResult(iMarketingFreeGoodListService.selectMarketingFreeGoodListBySearch(page,paramObjectMap));
        result.success("查询商品列表成功");
        return result;
    }

    

}
