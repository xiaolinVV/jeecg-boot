package org.jeecg.modules.member.api;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.good.service.IGoodStoreListService;
import org.jeecg.modules.map.utils.TengxunMapUtils;
import org.jeecg.modules.marketing.service.IMarketingDiscountService;
import org.jeecg.modules.member.entity.MemberAttentionStore;
import org.jeecg.modules.member.service.IMemberAttentionStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;


/**
 * 会员店铺api
 */
@RequestMapping("after/memberAttentionStore")
@Controller
public class AfterMemberAttentionStoreController {


    @Autowired
    private IMemberAttentionStoreService iMemberAttentionStoreService;

    @Autowired
    private IGoodStoreListService iGoodStoreListService;

    @Autowired
    private IMarketingDiscountService iMarketingDiscountService;

    @Autowired
    private TengxunMapUtils tengxunMapUtils;


    /**
     * 添加关注店铺
     *
     * @return
     */
    @RequestMapping("addMemberAttentionStore")
    @ResponseBody
    public Result<String> addMemberAttentionStore(@RequestHeader(defaultValue = "") String sysUserId,
                                                  @RequestAttribute(value = "memberId",required = false) String memberId){
        Result<String> result=new Result<>();
        //参数校验
        if(StringUtils.isBlank(sysUserId)){
            result.error500("店铺id不能为空！！！");
            return result;
        }

        QueryWrapper<MemberAttentionStore> memberAttentionStoreQueryWrapper=new QueryWrapper<>();
        memberAttentionStoreQueryWrapper.eq("member_list_id",memberId);
        memberAttentionStoreQueryWrapper.eq("sys_user_id",sysUserId);
        long count=iMemberAttentionStoreService.count(memberAttentionStoreQueryWrapper);
        MemberAttentionStore memberAttentionStore=null;
        if(count>0) {
            memberAttentionStore=iMemberAttentionStoreService.getOne(memberAttentionStoreQueryWrapper);
            memberAttentionStore.setAttentionTime(new Date());
        }else{
            memberAttentionStore=new MemberAttentionStore();
            memberAttentionStore.setDelFlag("0");
            memberAttentionStore.setAttentionTime(new Date());
            memberAttentionStore.setMemberListId(memberId);
            memberAttentionStore.setSysUserId(sysUserId);
        }
        iMemberAttentionStoreService.saveOrUpdate(memberAttentionStore);
        result.success("关注店铺成功");
        return result;
    }



    /**
     * 取消关注店铺
     * @param ids
     * @return
     */
    @RequestMapping("delMemberAttentionStore")
    @ResponseBody
    public Result<String> delMemberAttentionStore(String ids){
        Result<String> result=new Result<>();
        //参数校验
        if(StringUtils.isBlank(ids)){
            result.error500("店铺id不能为空！！！");
            return result;
        }

        iMemberAttentionStoreService.removeByIds(Arrays.asList(StringUtils.split(ids,",")));

        result.success("取消关注店铺成功");
        return result;
    }

    /**
     * 根据不同方式获取店铺列表通过用户
     *
     * @param pattern
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("findMemberAttentionStoreByMember")
    @ResponseBody
    public Result<IPage<Map<String,Object>>> findMemberAttentionStoreByMember(Integer pattern,
                                                                              String longitude,
                                                                              String latitude,
                                                                              HttpServletRequest request,
                                                                              @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                                              @RequestParam(name="pageSize", defaultValue="10") Integer pageSize){
        Result<IPage<Map<String,Object>>> result=new Result<>();
        String memberId=request.getAttribute("memberId").toString();
        //参数验证
        if(pattern==null){
            result.error500("pattern不能为空！！！");
            return result;
        }


        //组织查询参数
        Page<Map<String,Object>> page = new Page<Map<String,Object>>(pageNo, pageSize);
        Map<String,Object> paramObjectMap= Maps.newHashMap();
        paramObjectMap.put("pattern",pattern);
        paramObjectMap.put("longitude",longitude);
        paramObjectMap.put("latitude",latitude);
        paramObjectMap.put("memberId",memberId);
        IPage<Map<String,Object>> storeManageMapList=iMemberAttentionStoreService.findMemberAttentionStoreByMember(page,paramObjectMap);
        if (storeManageMapList.getRecords().size()>0){
            Map<String,Object> mapsMap=Maps.newHashMap();
            storeManageMapList.getRecords().stream().forEach(s->{
                //获取商品
                Page<Map<String,Object>> storePage = new Page<Map<String,Object>>(1, 3);
                s.put("storeGoods",iGoodStoreListService.findGoodListBySysUserId(storePage,s.get("sysUserId").toString()).getRecords());
                if(Integer.parseInt(s.get("disCount").toString())>0) {
                    //获取券
                    Page<Map<String, Object>> discountPage = new Page<Map<String, Object>>(1, 4);
                    s.put("storeDiscounts", iMarketingDiscountService.findMarketingDiscountBySysUserId(discountPage, s.get("sysUserId").toString()).getRecords());
                }else{
                    s.put("storeDiscounts", "");
                }
                mapsMap.put(s.get("latitude")+","+s.get("longitude"),s);
            });

            if(StringUtils.isNotBlank(latitude)&&StringUtils.isNotBlank(longitude)) {

                JSONArray mapJsonArray= JSON.parseArray(tengxunMapUtils.findDistance(latitude+","+longitude,StringUtils.join(mapsMap.keySet(),";")));
                if(mapJsonArray!=null) {
                    mapJsonArray.stream().forEach(mj -> {
                        JSONObject jb = (JSONObject) mj;
                        Map<String, Object> s = (Map<String, Object>) mapsMap.get(StringUtils.substringBefore(jb.getJSONObject("to").getString("lat"), ".") + "." + StringUtils.rightPad(StringUtils.substringAfter(jb.getJSONObject("to").getString("lat"), "."), 6, "0") + "," + StringUtils.substringBefore(jb.getJSONObject("to").getString("lng"), ".") + "." + StringUtils.rightPad(StringUtils.substringAfter(jb.getJSONObject("to").getString("lng"), "."), 6, "0"));
                        BigDecimal dis=new BigDecimal(jb.getString("distance"));
                        if(dis.doubleValue()>1000){
                            s.put("distance", dis.divide(new BigDecimal(1000),2) + "km");
                        }else{
                            s.put("distance", dis + "m");
                        }

                    });
                }
            }
        }
        result.setResult(storeManageMapList);
        result.success("查询店铺列表成功");
        return result;
    }
}
