package org.jeecg.modules.provider.api;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.service.IMemberListService;
import org.jeecg.modules.provider.service.IProviderTemplateService;
import org.jeecg.modules.store.service.IStoreTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 运费模板接口控制器
 */
@RequestMapping("front/template")
@Controller
public class FrontTemplateController {

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
    public Result<Map<String,Object>> findTemplateByGoodIdAndSysAreaId(String goodId, String specification, BigDecimal quantity, Integer isPlatform, String sysAreaId, @RequestAttribute(value = "memberId",required = false) String memberId){
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
                if(StringUtils.isBlank(memberId)) {
                    gm.put("price", goodStoreSpecification.getPrice());
                }else{
                    MemberList memberList=iMemberListService.getById(memberId);
                    if(memberList.getMemberType().equals("0")){
                        gm.put("price", goodStoreSpecification.getPrice());
                    }
                    if(memberList.getMemberType().equals("1")){
                        gm.put("price", goodStoreSpecification.getVipPrice());
                    }
                }
                BigDecimal freight=iStoreTemplateService.calculateFreight(goodsMap,sysAreaId);
                if(freight.doubleValue()==-1){
                    paramMap.put("isAvailable","2");
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
                    GoodSpecification goodSpecification=iGoodSpecificationService.getOne(new LambdaQueryWrapper<GoodSpecification>()
                            .eq(GoodSpecification::getGoodListId,goodId)
                            .eq(GoodSpecification::getSpecification,specification)
                            .orderByDesc(GoodSpecification::getCreateTime)
                            .last("limit 1"));

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
                    if(StringUtils.isBlank(memberId)) {
                        gm.put("price", goodSpecification.getPrice());
                    }else{
                        MemberList memberList=iMemberListService.getById(memberId);
                        if(memberList.getMemberType().equals("0")){
                            gm.put("price", goodSpecification.getPrice());
                        }
                        if(memberList.getMemberType().equals("1")){
                            gm.put("price", goodSpecification.getVipPrice());
                        }
                    }
                    BigDecimal freight=iProviderTemplateService.calculateFreight(goodsMap,sysAreaId);
                    if(freight.doubleValue()==-1){
                        paramMap.put("isAvailable","2");
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
