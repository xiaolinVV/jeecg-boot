package org.jeecg.modules.order.api;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.order.service.IOrderEvaluateService;
import org.jeecg.modules.order.service.IOrderEvaluateStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.Map;


/**
 * 评价接口控制器
 */
@RequestMapping("front/orderEvaluate")
@Controller
public class FrontOrderEvaluateController {

    @Autowired
    private IOrderEvaluateService iOrderEvaluateService;

    @Autowired
    private IOrderEvaluateStoreService iOrderEvaluateStoreService;


    /**
     * 根据商品id查询评论信息
     *
     * @param goodId
     * @param pattern 0:全部；1：最新；2：有图
     * @param pageNo
     * @param pageSize
     * @return
     */
        @RequestMapping("findOrderEvaluateByGoodId")
        @ResponseBody
        public Result<IPage<Map<String,Object>>> findOrderEvaluateByGoodId(String goodId, Integer isPlatform,Integer pattern,@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                                           @RequestParam(name="pageSize", defaultValue="10") Integer pageSize){
            Result<IPage<Map<String,Object>>> result=new Result<>();

            //参数判断
            if(isPlatform==null){
                result.error500("isPlatform  是否平台类型参数不能为空！！！   ");
                return  result;
            }

            if(StringUtils.isBlank(goodId)){
                result.error500("goodId  商品id不能为空！！！   ");
                return  result;
            }

            //组织查询参数
            Page<Map<String,Object>> page = new Page<Map<String,Object>>(pageNo, pageSize);
            Map<String,Object> paramObjectMap= Maps.newHashMap();
            paramObjectMap.put("goodId",goodId);
            paramObjectMap.put("pattern",pattern);

            //查询店铺商品评价
            if(isPlatform.intValue()==0){
                IPage<Map<String,Object>> mapIPage=iOrderEvaluateStoreService.findOrderEvaluateByGoodId(page,paramObjectMap);
                mapIPage.getRecords().forEach(spm->{
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    spm.put("createTime",simpleDateFormat.format(spm.get("createTime")));
                });
                result.setResult(mapIPage);
            }else
                //查询平台商品评价列表
                if(isPlatform.intValue()==1){
                    IPage<Map<String,Object>> mapIPage=iOrderEvaluateService.findOrderEvaluateByGoodId(page,paramObjectMap);
                    mapIPage.getRecords().forEach(spm->{
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        spm.put("createTime",simpleDateFormat.format(spm.get("createTime")));
                    });
                    result.setResult(mapIPage);
                }else{
                    result.error500("isPlatform  参数不正确请联系平台管理员！！！  ");
                    return  result;
                }
            result.success("查询商品评论列表成功");
            return result;
        }
}
