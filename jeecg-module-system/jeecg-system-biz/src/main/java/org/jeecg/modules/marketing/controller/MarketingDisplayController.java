package org.jeecg.modules.marketing.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.good.service.IGoodSpecificationService;
import org.jeecg.modules.marketing.service.IMarketingWelfarePaymentsService;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.service.IMemberListService;
import org.jeecg.modules.order.service.IOrderListService;
import org.jeecg.modules.store.entity.StoreManage;
import org.jeecg.modules.store.service.IStoreManageService;
import org.jeecg.modules.system.service.ISysUserService;
import org.jeecg.modules.system.vo.SysWorkbenchVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

@Slf4j
@Api(tags="大屏显示")
@RestController
@RequestMapping("MarketingDisplay")
public class MarketingDisplayController {

    @Autowired
    private IStoreManageService iStoreManageService;

    @Autowired
    private IMarketingWelfarePaymentsService iMarketingWelfarePaymentsService;
    @Autowired
    private IMemberListService iMemberListService;
    @Autowired
    private IOrderListService iOrderListService;
    @Autowired
    private IGoodSpecificationService iGoodSpecificationService;
    @Autowired
    private ISysUserService iSysUserService;
    @GetMapping("findStoreStatistics")
    public Result<Map<String,Object>> findStoreStatistics(){
        Result<Map<String, Object>> result = new Result<>();
        HashMap<String, Object> map = new HashMap<>();

        LocalDateTime now = LocalDateTime.now();
        //当前时间时间戳
        map.put("dateTime",now.toInstant(ZoneOffset.of("+8")).toEpochMilli());
        //星期几
        map.put("week","星期"+now.getDayOfWeek().getValue());

        LambdaQueryWrapper<StoreManage> storeManageLambdaQueryWrapper = new LambdaQueryWrapper<StoreManage>()
                .in(StoreManage::getPayStatus, "1", "2");
        //入驻商家统计
        map.put("storeSum",iStoreManageService.count(storeManageLambdaQueryWrapper));
        List<StoreManage> storeManageList = iStoreManageService.list(storeManageLambdaQueryWrapper
                .in(StoreManage::getAttestationStatus, "1", "2")
                .last("limit 100"));
        ArrayList<Map<String, Object>> maps = new ArrayList<>();
        storeManageList.forEach(sml->{
            HashMap<String, Object> hashMap = new HashMap<>();
            if (StringUtils.isNotBlank(sml.getSubStoreName())){
                hashMap.put("storeName",sml.getStoreName()+"("+sml.getSubStoreName()+")");
            }else {
                hashMap.put("storeName",sml.getStoreName());
            }
            if (StringUtils.isNotBlank(sml.getAreaAddress())){
                List<String> strings = Arrays.asList(StringUtils.split(sml.getAreaAddress(), ","));
                if (strings.size()>2){
                    hashMap.put("prefecture",strings.get(2));
                }else {
                    hashMap.put("prefecture","-");
                }
                if (strings.size()>1){
                    hashMap.put("municipality",strings.get(1));
                }else {
                    hashMap.put("municipality","-");
                }
                if (strings.size()>0){
                    hashMap.put("province",strings.get(0));
                }else {
                    hashMap.put("province","-");
                }
            }else {
                hashMap.put("province","-");
                hashMap.put("municipality","-");
                hashMap.put("prefecture","-");
            }
            maps.add(hashMap);

        });
        map.put("storeList",maps);
        result.setResult(map);
        result.success("入驻商户清单");
        return result;
    }
    @GetMapping("everydaySendOutWelfare")
    public Result<List<Map<String,Object>>> everydaySendOutWelfare(){
        Result<List<Map<String,Object>>> result = new Result<>();
        List<Map<String,Object>> s = iMarketingWelfarePaymentsService.everydaySendOutWelfare();
        result.setResult(s);
        result.success("返回大屏每日送出福利金");
        return result;
    }
    @GetMapping("everydayStoreAndMember")
    public Result<List<Map<String,Object>>>everydayStoreAndMember(){
        Result<List<Map<String,Object>>> result = new Result<>();
        ArrayList<Map<String, Object>> maps = new ArrayList<>();
        for (int i = 7; i > 0; i--) {
            //推算当前时间前一天日期
            Calendar calendar=Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR,-i+1);
//            String s1 = calendar.get(Calendar.YEAR) + "-" + StringUtils.leftPad((calendar.get(Calendar.MONTH) + 1)+"",2,"0") + "-" + StringUtils.leftPad((calendar.get(Calendar.DAY_OF_MONTH))+"",2,"0");
            String s1 = DateUtils.date2Str(calendar.getTime(), new SimpleDateFormat("yyyy-MM-dd"));
            HashMap<String, Object> storeMap = new HashMap<>();

            long storeSum = iStoreManageService.count(new LambdaQueryWrapper<StoreManage>()
                    .in(StoreManage::getPayStatus, "1", "2")
                    .in(StoreManage::getAttestationStatus, "1", "2")
                    .apply("date_format(create_time,'%Y-%m-%d') = {0}", s1)
            );
            storeMap.put("name","店铺");
            storeMap.put("day",calendar.get(Calendar.DAY_OF_MONTH));
            storeMap.put("sum",storeSum);
            HashMap<String, Object> memberMap = new HashMap<>();
            long memberSum = iMemberListService.count(new LambdaQueryWrapper<MemberList>()
                    .apply("date_format(create_time,'%Y-%m-%d') = {0}", s1));
            memberMap.put("name","会员");
            memberMap.put("day",calendar.get(Calendar.DAY_OF_MONTH));
            memberMap.put("sum",memberSum);
            maps.add(storeMap);
            maps.add(memberMap);
        }

        result.setResult(maps);
        result.success("返回大屏每日新增店铺和会员");
        return result;
    }
    @GetMapping("orderSumList")
    public Result<List<Map<String,Object>>> orderSumList(){
        Result<List<Map<String,Object>>> result = new Result<>();
        List<Map<String,Object>> s = iOrderListService.orderSumList();
        result.setResult(s);
        result.success("商城商品的销售订单");
        return result;
    }
    @GetMapping("storeComplimentary")
    public Result<List<Map<String,Object>>> storeComplimentary(){
        Result<List<Map<String,Object>>> result = new Result<>();
        List<Map<String,Object>> s = iMarketingWelfarePaymentsService.storeComplimentary();
        result.setResult(s);
        result.success("店铺送福利金排行");
        return result;
    }
    @GetMapping("monthStoreAndMember")
    public Result<List<Map<String,Object>>> monthStoreAndMember(){
        Result<List<Map<String,Object>>> result = new Result<>();
        ArrayList<Map<String, Object>> maps = new ArrayList<>();
        for (int i = 6; i > 0; i--) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH,-i+1);
            Integer i1 = Integer.valueOf(DateUtils.date2Str(calendar.getTime(), new SimpleDateFormat("M")));
            HashMap<String, Object> storeMap = new HashMap<>();
            long storeSum = iStoreManageService.count(new LambdaUpdateWrapper<StoreManage>()
                    .eq(StoreManage::getYear, DateUtils.getDate("yyyy"))
                    .eq(StoreManage::getMonth, i1)
                    .in(StoreManage::getPayStatus, "1", "2")
                    .in(StoreManage::getAttestationStatus, "1", "2")
            );
            storeMap.put("name","店铺");
            storeMap.put("month",i1+"月");
            storeMap.put("sum",storeSum);
            HashMap<String, Object> memberMap = new HashMap<>();
            long memberSum = iMemberListService.count(new LambdaUpdateWrapper<MemberList>()
                    .eq(MemberList::getYear, DateUtils.getDate("yyyy"))
                    .eq(MemberList::getMonth,i1));
            memberMap.put("name","会员");
            memberMap.put("month",i1+"月");
            memberMap.put("sum",memberSum);
            maps.add(storeMap);
            maps.add(memberMap);
        }
        result.setResult(maps);
        result.success("店铺/会员月增长");
        return result;
    }
    @GetMapping("manageSum")
    public Result<Map<String,Object>> manageSum(){
        Result<Map<String,Object>> result = new Result<>();
        HashMap<String, Object> map = new HashMap<>();
        map.put("memberSum",iMemberListService.count(new LambdaUpdateWrapper<MemberList>()
                .eq(MemberList::getDelFlag,"0")));
        SysWorkbenchVO sysWorkbenchVO = iSysUserService.totalSum();
        map.put("totalSum",sysWorkbenchVO.getEarnings());
        map.put("skuSum",iGoodSpecificationService.getGoodSkuCount());
        result.setResult(map);
        result.success("经营概况");
        return result;
    }
}
