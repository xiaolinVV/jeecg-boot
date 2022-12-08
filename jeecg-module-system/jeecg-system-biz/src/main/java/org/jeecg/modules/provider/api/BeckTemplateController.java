package org.jeecg.modules.provider.api;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.good.entity.GoodList;
import org.jeecg.modules.good.entity.GoodSpecification;
import org.jeecg.modules.good.entity.GoodStoreList;
import org.jeecg.modules.good.entity.GoodStoreSpecification;
import org.jeecg.modules.good.service.IGoodListService;
import org.jeecg.modules.good.service.IGoodSpecificationService;
import org.jeecg.modules.good.service.IGoodStoreListService;
import org.jeecg.modules.good.service.IGoodStoreSpecificationService;
import org.jeecg.modules.member.service.IMemberListService;
import org.jeecg.modules.provider.service.IProviderTemplateService;
import org.jeecg.modules.store.service.IStoreTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 运费模板接口控制器
 */
@RequestMapping("beck/template")
@Controller
public class BeckTemplateController {

    @Autowired
    private IGoodStoreListService iGoodStoreListService;

    @Autowired
    private IGoodStoreSpecificationService iGoodStoreSpecificationService;

    @Autowired
    private IGoodSpecificationService iGoodSpecificationService;

    @Autowired
    private IProviderTemplateService iProviderTemplateService;

    @Autowired
    private IStoreTemplateService iStoreTemplateService;

    @Autowired
    private IGoodListService iGoodListService;

    @Autowired
    private IMemberListService iMemberListService;

    /**
     * 根据商品id查询运费情况
     * @param goodId
     * @param isPlatform
     * @param sysAreaId
     * @return
     */
    @RequestMapping(value = "findTemplateByGoodIdAndSysAreaId")
    @ResponseBody
    public Result<Map<String,Object>> findTemplateByGoodIdAndSysAreaId(String goodId, String specification, BigDecimal quantity, Integer isPlatform, String sysAreaId, HttpServletRequest request){
        Result<Map<String,Object>> result=new Result<>();
        Map<String,Object> paramMap= Maps.newHashMap();

        //参数校验
        //判断数据不为空
        if(isPlatform==null){
            result.error500("isPlatform  是否平台类型参数不能为空！！！   ");
            return  result;
        }

        if(StringUtils.isBlank(goodId)){
            result.error500("goodId  商品id不能为空！！！   ");
            return  result;
        }

        if(StringUtils.isBlank(sysAreaId)){
            result.error500("sysAreaId  区域id不能为空！！！   ");
            return  result;
        }


        if(StringUtils.isBlank(specification)){
            result.error500("商品规格不能为空不能为空！！！   ");
            return  result;
        }

        Object sysUserId=request.getAttribute("sysUserId");
        //查询运费
        //查询店铺商品运费
        if(isPlatform.intValue()==0){
            GoodStoreList goodStoreList= iGoodStoreListService.getById(goodId);
            if(goodStoreList.getRepertory().longValue()>0){
                paramMap.put("isAvailable","1");
                paramMap.put("carriage",null);
            }else{
                paramMap.put("isAvailable","0");
                paramMap.put("carriage",null);
            }
            if(goodStoreList!=null){
                QueryWrapper<GoodStoreSpecification> goodStoreSpecificationQueryWrapper=new QueryWrapper<>();
                goodStoreSpecificationQueryWrapper.eq("good_store_list_id",goodId);
                goodStoreSpecificationQueryWrapper.eq("specification",specification);
                GoodStoreSpecification goodStoreSpecification=iGoodStoreSpecificationService.getOne(goodStoreSpecificationQueryWrapper);

                if(goodStoreSpecification.getRepertory().longValue()>0){
                    paramMap.put("isAvailable","1");
                    paramMap.put("carriage",null);
                }else{
                    paramMap.put("isAvailable","0");
                    paramMap.put("carriage",null);
                }

                List<Map<String,Object>> goodsMap= Lists.newArrayList();
                Map<String,Object> gm=Maps.newHashMap();
                goodsMap.add(gm);
                gm.put("goodId",goodId);
                gm.put("goodSpecificationId",goodStoreSpecification.getId());
                gm.put("quantity",quantity);
                //销售价
                gm.put("price", goodStoreSpecification.getPrice());
                //会员价
                gm.put("vipPrice", goodStoreSpecification.getVipPrice());
                BigDecimal freight=iStoreTemplateService.calculateFreight(goodsMap,sysAreaId);
                if(freight.doubleValue()==-1){
                    result.error500("运费模板有问题");
                    return  result;
                }

                if(freight.doubleValue()==0){
                    paramMap.put("carriage","免邮");
                }

                if(freight.doubleValue()>0){
                    paramMap.put("carriage",freight+"元");
                }
            }else{
                result.error500("isPlatform  参数不正确请联系平台管理员！！！  ");
                return  result;
            }

        }else
            //查询平台商品运费
            if(isPlatform.intValue()==1){

                GoodList goodList= iGoodListService.getById(goodId);
                if(goodList.getRepertory().longValue()>0){
                    paramMap.put("isAvailable","1");
                    paramMap.put("carriage",null);
                }else{
                    paramMap.put("isAvailable","0");
                    paramMap.put("carriage",null);
                }
                if(goodId!=null){
                    QueryWrapper<GoodSpecification> goodSpecificationQueryWrapper=new QueryWrapper<>();
                    goodSpecificationQueryWrapper.eq("good_list_id",goodId);
                    goodSpecificationQueryWrapper.eq("specification",specification);
                    GoodSpecification goodSpecification=iGoodSpecificationService.getOne(goodSpecificationQueryWrapper);

                    if(goodSpecification.getRepertory().longValue()>0){
                        paramMap.put("isAvailable","1");
                        paramMap.put("carriage",null);
                    }else{
                        paramMap.put("isAvailable","0");
                        paramMap.put("carriage",null);
                    }

                    List<Map<String,Object>> goodsMap= Lists.newArrayList();
                    Map<String,Object> gm=Maps.newHashMap();
                    goodsMap.add(gm);
                    gm.put("goodId",goodId);
                    gm.put("goodSpecificationId",goodSpecification.getId());
                    gm.put("quantity",quantity);
                    gm.put("price", goodSpecification.getPrice());
                    gm.put("vipPrice", goodSpecification.getVipPrice());

                    BigDecimal freight=iProviderTemplateService.calculateFreight(goodsMap,sysAreaId);
                    if(freight.doubleValue()==-1){
                        result.error500("运费模板有问题");
                        return  result;
                    }

                    if(freight.doubleValue()==0){
                        paramMap.put("carriage","免邮");
                    }

                    if(freight.doubleValue()>0){
                        paramMap.put("carriage",freight+"元");
                    }

                }else {
                    result.error500("商品不存在，你核对数据");
                    return  result;
                }

            }else{
                result.error500("isPlatform  参数不正确请联系平台管理员！！！  ");
                return  result;
            }
            result.setResult(paramMap);
        result.success("查询运费成功");
        return result;
    }
}
