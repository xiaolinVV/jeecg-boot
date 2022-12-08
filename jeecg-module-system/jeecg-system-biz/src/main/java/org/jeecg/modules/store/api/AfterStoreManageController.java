package org.jeecg.modules.store.api;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.marketing.entity.MarketingPrefecture;
import org.jeecg.modules.marketing.service.IMarketingPrefectureService;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.service.IMemberListService;
import org.jeecg.modules.order.utils.WeixinHftxPayUtils;
import org.jeecg.modules.order.utils.WeixinPayUtils;
import org.jeecg.modules.pay.utils.NotifyUrlUtils;
import org.jeecg.modules.store.dto.StoreParamDTO;
import org.jeecg.modules.store.entity.StoreManage;
import org.jeecg.modules.store.entity.StoreSetting;
import org.jeecg.modules.store.service.IStoreManageService;
import org.jeecg.modules.store.service.IStoreSettingService;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysDictService;
import org.jeecg.modules.system.service.ISysUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Map;

@Controller
@RequestMapping("after/storeManage")
@Slf4j
public class AfterStoreManageController {

    @Autowired
    private WeixinPayUtils weixinPayUtils;

    @Autowired
    private WeixinHftxPayUtils weixinHftxPayUtils;

    @Autowired
    private IStoreManageService iStoreManageService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private ISysDictService iSysDictService;

    @Autowired
    private IMemberListService iMemberListService;

    @Autowired
    private ISysUserService iSysUserService;

    @Autowired
    private IMarketingPrefectureService iMarketingPrefectureService;

    @Autowired
    private IStoreSettingService iStoreSettingService;

    @Autowired
    private NotifyUrlUtils notifyUrlUtils;


    /**
     * 开店支付调用
     *
     * @param storeParamDTO
     * @param bindingResult
     * @param request
     * @return
     */
    @RequestMapping("openTheStore")
    @ResponseBody
    public Result<Map<String,Object>> openTheStore(@Valid StoreParamDTO storeParamDTO,
                                                   BindingResult bindingResult, HttpServletRequest request){
        String memberId=request.getAttribute("memberId").toString();
        Result<Map<String,Object>> result=new Result<>();
        Map<String,Object> objectMap= Maps.newHashMap();
        //参数验证
        if (bindingResult.getErrorCount() > 0){
            result.error500(bindingResult.getFieldError().toString());
            return result;
        }
        //验证码验证
        String smscode = storeParamDTO.getVerificationCode();
        Object code = redisUtil.get(storeParamDTO.getBossPhone());
        if(code==null){
            result.error500("手机号找不到验证码");
            return result;
        }
        if(!code.toString().equals(smscode)){
            result.error500("验证码输入不正确，请重新输入");
            return result;
        }
        //手机号码重复验证
        //查询店铺
        QueryWrapper<StoreManage> storeManageQueryWrapper=new QueryWrapper<>();
        storeManageQueryWrapper.eq("member_list_id",memberId);
        storeManageQueryWrapper.in("pay_status", "1","2");
        if(iStoreManageService.count(storeManageQueryWrapper)>0){
            result.error500("本会员已经开通了店铺");
            return result;
        }

        SysUser sysUser=iSysUserService.getUserByName(storeParamDTO.getBossPhone());
        if(sysUser!=null){
            result.error500("本手机号已经开通了店铺");
            return result;
        }

        QueryWrapper<StoreManage> storeManageQueryWrapper2=new QueryWrapper<>();
        storeManageQueryWrapper2.eq("boss_phone",storeParamDTO.getBossPhone());
        storeManageQueryWrapper2.eq("pay_status","1");
        if(iStoreManageService.count(storeManageQueryWrapper2)>0){
            result.error500("本手机号已经开通了店铺");
            return result;
        }

        BigDecimal totalPrice=new BigDecimal(0);

        //新建待支付店铺信息
        StoreManage storeManage=new StoreManage();
        //拷贝数据
        BeanUtils.copyProperties(storeParamDTO,storeManage);
        //设置开店基本信息
        //开通费用
        String openType=storeManage.getOpenType();
        if(openType.equals("0")){
            //包年
            totalPrice=new BigDecimal(iSysDictService.queryTableDictTextByKey("sys_dict_item","item_value","item_text","pack_years"));
        }else if (openType.equals("1")){
            //终生
            totalPrice=new BigDecimal(iSysDictService.queryTableDictTextByKey("sys_dict_item","item_value","item_text","lifelong"));
        }

        storeManage.setDelFlag("0");

        //开通费用
        storeManage.setMoney(totalPrice);
        //未开通
        storeManage.setStatus("0");
        //认证状态   未认证
        storeManage.setAttestationStatus("-1");
        //支付状态未支付
        storeManage.setPayStatus("0");
        //配送方式快递
        storeManage.setDistributionType("0");
        //配送状态启用
        storeManage.setDistributionStatus("0");
        storeManage.setMemberListId(memberId);
        storeManage.setWelfarePayments(new BigDecimal(0));
        storeManage.setBalance(new BigDecimal(0));
        storeManage.setAccountFrozen(new BigDecimal(0));
        storeManage.setUnusableFrozen(new BigDecimal(0));
        storeManage.setServiceRange(new BigDecimal(iSysDictService.queryTableDictTextByKey("sys_dict_item","item_value","item_text","default_service_distance")));
        storeManage.setGrade(new BigDecimal(5));
        storeManage.setComprehensiveEvaluation(new BigDecimal(5));
        storeManage.setBackStatus("0");
        storeManage.setBackTimes(new BigDecimal(0));

        //获取用户信息
        MemberList memberList=iMemberListService.getById(memberId);

        storeManage.setPromoterType(memberList.getPromoterType());
        storeManage.setPromoter(memberList.getPromoter());
        if (StringUtils.isNotBlank(memberList.getSysUserId())){
            StoreManage storeManage1 = iStoreManageService.getOne(new LambdaQueryWrapper<StoreManage>()
                    .eq(StoreManage::getSysUserId, memberList.getSysUserId())
                    .in(StoreManage::getPayStatus,"1","2"));
            if(storeManage1!=null) {
                storeManage.setAllianceUserId(storeManage1.getAllianceUserId());
            }
        }
        //保存店铺信息
        iStoreManageService.save(storeManage);

        //支付信息调用


        //设置回调地址

        String notifyUrl=notifyUrlUtils.getNotify("open_store_notify_url");


        String weixinMiniSoftPay = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "weixin_mini_soft_pay");

        Map<String,String> resultMap=null;

        if(weixinMiniSoftPay.equals("0")) {
            //官方微信支付调起
            resultMap = weixinPayUtils.payWeixin(request,storeManage.getId(),totalPrice,memberList.getOpenid(),notifyUrl);
        }

        if(weixinMiniSoftPay.equals("1")) {
            //汇付天下微信支付
            resultMap = weixinHftxPayUtils.payWeixin(totalPrice,storeManage.getId(), notifyUrl, memberList.getOpenid(),null);
        }

        //支付日志
        storeManage.setPayParam(resultMap.get("params"));

       //保存支付日志
        iStoreManageService.saveOrUpdate(storeManage);
        objectMap.put("notifyUrl",notifyUrl+"?id="+storeManage.getId());
        objectMap.put("jsonStr",resultMap.get("jsonStr"));
        result.setResult(objectMap);
        result.success("开店调起支付");
        return result;
    }


    /**
     * 根据用户查询开店信息
     * @param request
     * @return
     */
    @RequestMapping("getStoreManageByMemberId")
    @ResponseBody
    public Result<Map<String,Object>> getStoreManageByMemberId(HttpServletRequest request){
        String memberId=request.getAttribute("memberId").toString();
        Result<Map<String,Object>> result=new Result<>();
        Map<String,Object> objectMap= Maps.newHashMap();

        //查询店铺
        QueryWrapper<StoreManage> storeManageQueryWrapper=new QueryWrapper<>();
        storeManageQueryWrapper.eq("member_list_id",memberId);
        storeManageQueryWrapper.in("pay_status", "1","2");
        storeManageQueryWrapper.eq("status","1");
        if(iStoreManageService.count(storeManageQueryWrapper)<=0){
            result.error500("查询不到开店用户信息！！！");
            return result;
        }
        StoreManage storeManage=iStoreManageService.list(storeManageQueryWrapper).get(0);
        StoreSetting storeSetting=iStoreSettingService.getOne(new LambdaQueryWrapper<>());
        if(storeSetting!=null) {
            objectMap.put("storeManageUrl", storeSetting.getManageAddress());
            objectMap.put("userName", storeManage.getBossPhone());
            objectMap.put("passwd", storeSetting.getInitialPasswd());
        }
        result.setResult(objectMap);
        result.success("根据用户查询开店信息");
        return result;
    }

    /**
     * 获取特权店铺列表信息
     *
     * @param
     * @param memberId
     * @return
     */
    @RequestMapping("getPrivilegeStoreList")
    @ResponseBody
    public Result<?> getPrivilegeStoreList( @RequestAttribute("memberId") String memberId,
                                   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize){
        return Result.ok(iStoreManageService.getPrivilege(new Page<>(pageNo,pageSize),memberId));
    }

    /**
     * 获取特权店铺详情
     *
     * @param
     * @param memberId
     * @return
     */
    @RequestMapping("getPrivilegeStoreInfo")
    @ResponseBody
    public Result<?> getPrivilegeStoreInfo(@RequestAttribute("memberId") String memberId,
                                                String sysUserId){
        Map<String,Object> paramMap=Maps.newHashMap();
        paramMap.put("memberId",memberId);
        paramMap.put("sysUserId",sysUserId);
        Map<String,Object> resultMap=iStoreManageService.getPrivilegeInfo(paramMap);
        MemberList memberList=iMemberListService.getById(memberId);
        resultMap.put("welfarePayments",memberList.getWelfarePayments());
        resultMap.put("balance",memberList.getBalance());
        //获取团队专区
        MarketingPrefecture marketingPrefecture=iMarketingPrefectureService.getOne(new LambdaQueryWrapper<MarketingPrefecture>()
                .eq(MarketingPrefecture::getStatus,"1")
                .eq(MarketingPrefecture::getIsDesignation,"1")
                .orderByDesc(MarketingPrefecture::getCreateTime)
                .last("limit 1"));
        if(marketingPrefecture!=null){
            resultMap.put("prefectureId",marketingPrefecture.getId());
            resultMap.put("prefectureName",marketingPrefecture.getPrefectureName());
        }
        return Result.ok(resultMap);
    }
}
