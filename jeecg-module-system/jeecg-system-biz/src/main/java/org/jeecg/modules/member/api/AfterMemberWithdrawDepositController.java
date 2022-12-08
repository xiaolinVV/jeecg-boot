package org.jeecg.modules.member.api;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.member.entity.MemberBankCard;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.entity.MemberWithdrawDeposit;
import org.jeecg.modules.member.service.*;
import org.jeecg.modules.system.service.ISysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

/**
 * 会员提现api接口
 */
@RequestMapping("after/memberWithdrawDeposit")
@Controller
public class AfterMemberWithdrawDepositController {

    @Autowired
    private IMemberWithdrawDepositService iMemberWithdrawDepositService;

    @Autowired
    private IMemberRechargeRecordService iMemberRechargeRecordService;

    @Autowired
    private IMemberDistributionRecordService iMemberDistributionRecordService;
    @Autowired
    private IMemberListService iMemberListService;
    @Autowired
    private IMemberBankCardService iMemberBankCardService;

    @Autowired
    private ISysDictService iSysDictService;

    /**
     * 用户提现(弃用)
     * @param request
     * @return
     */
    @RequestMapping("addWithdrawDeposit")
    @ResponseBody
    public Result<String> addWithdrawDeposit(BigDecimal money,HttpServletRequest request){
        String memberId= request.getAttribute("memberId").toString();

        Result<String> result=new Result<>();
        if (money.doubleValue()<0){
            return result.error500("请输入正确金额!");
        }
        MemberList memberList=iMemberListService.getById(memberId);
        if (memberList.getBalance().doubleValue()<money.doubleValue()){
            return result.error500("提现金额不足!");
        }
        //提现
        String rs=iMemberWithdrawDepositService.addWithdrawDeposit(memberId,money);
        if(!rs.equals("SUCCESS")){
            result.error500(rs);
            return  result;
        }
        result.success("提现申请成功！！！");
        return result;
    }

    /**
     * 提现申请 到审核
     * @param money
     * @param request
     * @return
     */
    @RequestMapping("withdrawalCard")
    @ResponseBody
    public Result<String>withdrawalCard(@RequestParam(name = "money",required = true) BigDecimal money,
                                        HttpServletRequest request){
        Result<String> result = new Result<>();

        String memberId = request.getAttribute("memberId").toString();
        if (money.doubleValue()<0){
            return result.error500("请输入正确金额!");
        }
        //提现按照每天什么时间可以提现
        String withdrawOpenWeek= iSysDictService.queryTableDictTextByKey("sys_dict_item","item_value","item_text","withdraw_open_week");
        Calendar calendar=Calendar.getInstance();
        String week=calendar.get(Calendar.DAY_OF_WEEK)-1+"";
        if(StringUtils.indexOf(withdrawOpenWeek,week)==-1){
            return result.error500("该时间段不允许提现。。。。");
        }
        //提现倍数限制
        String withdrawAppointIntegersAndMultiples= iSysDictService.queryTableDictTextByKey("sys_dict_item","item_value","item_text","withdraw_appoint_integers_and_multiples");
        if(!withdrawAppointIntegersAndMultiples.equals("-1")){
            if((money.doubleValue()%Double.parseDouble(withdrawAppointIntegersAndMultiples))!=0){
                return result.error500("提现金额必须是"+withdrawAppointIntegersAndMultiples+"倍数");
            }
        }
        //提现时间限制
        String withdrawStartTime= iSysDictService.queryTableDictTextByKey("sys_dict_item","item_value","item_text","withdraw_start_time");
        String withdrawEndTime= iSysDictService.queryTableDictTextByKey("sys_dict_item","item_value","item_text","withdraw_end_time");
        try {
            Date startTime= DateUtils.parseDate(DateUtils.formatDate()+" "+withdrawStartTime,"yyyy-MM-dd HH:mm:ss");
            Date endTime=DateUtils.parseDate(DateUtils.formatDate()+" "+withdrawEndTime,"yyyy-MM-dd HH:mm:ss");
            if(new Date().getTime()>=startTime.getTime()&&new Date().getTime()<=endTime.getTime()){
                    //允许提现
            }else{
                return result.error500("该时间段不允许提现。。。。");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //单日提现次数限制
        String withdrawEverydayTimes= iSysDictService.queryTableDictTextByKey("sys_dict_item","item_value","item_text","withdraw_everyday_times");
        if(StringUtils.isNotBlank(withdrawEverydayTimes)&&!withdrawEverydayTimes.equals("-1")){
            long withdrawalCount=iMemberWithdrawDepositService.count(new LambdaQueryWrapper<MemberWithdrawDeposit>() .eq(MemberWithdrawDeposit::getYear,calendar.get(Calendar.YEAR))
                    .eq(MemberWithdrawDeposit::getMonth,calendar.get(Calendar.MONTH)+1)
                    .eq(MemberWithdrawDeposit::getDay,calendar.get(Calendar.DAY_OF_MONTH))
                    .eq(MemberWithdrawDeposit::getMemberListId,memberId));
            if(withdrawalCount>=new BigDecimal(withdrawEverydayTimes).intValue()){
                return result.error500("每日最多只能提现："+withdrawEverydayTimes+"次");
            }
        }
        //提现单笔最大金额限制
        String singleWithdrawMaximum= iSysDictService.queryTableDictTextByKey("sys_dict_item","item_value","item_text","single_withdraw_maximum");
        if(StringUtils.isNotBlank(singleWithdrawMaximum)&&!singleWithdrawMaximum.equals("-1")){
            if(money.doubleValue()>new BigDecimal(singleWithdrawMaximum).doubleValue()){
                return result.error500("单笔最高提现金额不能超过："+singleWithdrawMaximum+"元");
            }
        }

        MemberList member = iMemberListService.getById(memberId);
        MemberBankCard bankCard = iMemberBankCardService.getOne(new LambdaQueryWrapper<MemberBankCard>()
                .eq(MemberBankCard::getCarType, "0")
                .eq(MemberBankCard::getMemberListId, memberId).orderByDesc(MemberBankCard::getCreateTime).last("limit 1"));


        String sameIdentityWithdrawEverydayTimes= iSysDictService.queryTableDictTextByKey("sys_dict_item","item_value","item_text","same_identity_withdraw_everyday_times");

        if(StringUtils.isNotBlank(sameIdentityWithdrawEverydayTimes)&&!sameIdentityWithdrawEverydayTimes.equals("-1")) {
            //同意身份证和名字提现次数限制
            long memberWithdrawDepositCount = iMemberWithdrawDepositService.count(new LambdaQueryWrapper<MemberWithdrawDeposit>()
                    .eq(MemberWithdrawDeposit::getYear, calendar.get(Calendar.YEAR))
                    .eq(MemberWithdrawDeposit::getMonth, calendar.get(Calendar.MONTH) + 1)
                    .eq(MemberWithdrawDeposit::getDay, calendar.get(Calendar.DAY_OF_MONTH))
                    .eq(MemberWithdrawDeposit::getIdentityNumber, bankCard.getIdentityNumber())
                    .eq(MemberWithdrawDeposit::getCardholder, bankCard.getCardholder()));
            if(memberWithdrawDepositCount>=Integer.parseInt(sameIdentityWithdrawEverydayTimes)){
                return result.error500("同一身份证单天不能超过："+sameIdentityWithdrawEverydayTimes+"次，提现");
            }
        }

        if (oConvertUtils.isEmpty(bankCard)){
            return result.error500("用户未绑定银行卡!");
        }
        if (member.getBalance().doubleValue()<money.doubleValue()){
            return result.error500("提现金额不足!");
        }else {
            //提现申请
            boolean b = iMemberWithdrawDepositService.withdrawalCard(money, member, bankCard);
            if (b){
                result.success("提现成功!请等待管理员审核!");
            }else {
                result.error500("提现失败!");
            }

        }

        return result;
    }
    /**
     *
     * 提现明细
     * @param pattern  -1:全部；0：待审核；1：待打款；2：已付款；3：无效
     * @param request
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("findMemberWithdrawDepositByMemberId")
    @ResponseBody
    public Result<IPage<Map<String,Object>>> findMemberWithdrawDepositByMemberId(Integer pattern , HttpServletRequest request,
                                                                                 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                                                 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize){
        String memberId= request.getAttribute("memberId").toString();
        Result<IPage<Map<String,Object>>> result=new Result<>();


        //组织查询参数
        Page<Map<String,Object>> page = new Page<Map<String,Object>>(pageNo, pageSize);
        Map<String,Object> paramObjectMap= Maps.newHashMap();
        paramObjectMap.put("memberId",memberId);
        paramObjectMap.put("pattern",pattern);


        result.setResult(iMemberWithdrawDepositService.findMemberWithdrawDepositByMemberId(page,paramObjectMap));

        result.success("查询提现金额列表");
        return result;
    }
    /**
     *
     * 提现明细(new)
     * @param pattern  -1:全部；0：待审核；1：待打款；2：已付款；3：无效
     * @param request
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("findMemberWithdrawDepositPageByMemberId")
    @ResponseBody
    public Result<IPage<Map<String,Object>>> findMemberWithdrawDepositPageByMemberId(@RequestParam(name = "pattern",required = true) Integer pattern , HttpServletRequest request,
                                                                                     @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                                                     @RequestParam(name="pageSize", defaultValue="10") Integer pageSize){
        String memberId= request.getAttribute("memberId").toString();
        Result<IPage<Map<String,Object>>> result=new Result<>();

        //组织查询参数
        Page<Map<String,Object>> page = new Page<Map<String,Object>>(pageNo, pageSize);
        Map<String,Object> paramObjectMap= Maps.newHashMap();
        paramObjectMap.put("memberId",memberId);
        paramObjectMap.put("pattern",pattern);
        result.setResult(iMemberWithdrawDepositService.findMemberWithdrawDepositPageByMemberId(page,paramObjectMap));

        result.success("查询提现金额列表");
        return result;
    }

    /**
     * 查询提现详情
     * @param id
     * @return
     */
    @RequestMapping("findMemberWithdrawDepositInfo")
    @ResponseBody
    public Result<Map<String,Object>> findMemberWithdrawDepositInfo(@RequestParam(name = "id",required = true)String id){
        Result<Map<String,Object>> result=new Result<>();

        if(StringUtils.isBlank(id)){
            result.error500("id不能为空");
            return  result;
        }

        Map<String,Object> objectMap=iMemberWithdrawDepositService.findMemberWithdrawDepositById(id);
        List<Map<String,Object>> memberRechargeRecordMapList=iMemberRechargeRecordService.findMemberRechargeRecordByMemberWithdrawDepositId(objectMap.get("id").toString());
        for (Map<String,Object> m:memberRechargeRecordMapList) {
            m.put("memberDistributionRecords",iMemberDistributionRecordService.findMemberDistributionRecordByMrrId(m.get("id").toString()));
        }
        objectMap.put("memberRechargeRecordMapList",memberRechargeRecordMapList);
        result.setResult(objectMap);
        result.success("查询提现详情");
        return result;

    }
    @RequestMapping("findMemberWithdrawDepositParticulars")
    @ResponseBody
    public Result<Map<String,Object>>findMemberWithdrawDepositParticulars(HttpServletRequest request,
                                                                          String id){
        Result<Map<String, Object>> result = new Result<>();
        Map<String, Object> memberWithdrawDepositParticulars = iMemberWithdrawDepositService.findMemberWithdrawDepositParticulars(id);
        result.success("返回提现详情");
        result.setResult(memberWithdrawDepositParticulars);
        return result;
    }
    //获取提现说明
    @RequestMapping("getWithdrawWarmPrompt")
    @ResponseBody
    public Result<?> getWithdrawWarmPrompt(){
        //获取提现说明
        String queryTableDictTextByKey = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "withdraw_warm_prompt");
        return Result.ok(queryTableDictTextByKey);
    }

    //获取最低提现金额
    @RequestMapping("getWithdrawMinimum")
    @ResponseBody
    public Result<?> getWithdrawMinimum(){
        //获取最低提现金额
        String queryTableDictTextByKey = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "withdraw_minimum");
        return Result.ok(queryTableDictTextByKey);
    }
    //获取字典配置提现手续费比例
    @RequestMapping("getWithdrawalServiceCharge")
    @ResponseBody
    public Result<?> getWithdrawalServiceCharge(){
        //获取字典配置提现手续费比例
        String withdrawalServiceCharge= StringUtils.substringBefore(iSysDictService
                .queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "withdrawal_service_charge"),"%");
        return Result.ok(withdrawalServiceCharge);
    }
    /**
     * 提现金额计算
     * @param money 金额
     * @return
     */
    @RequestMapping("withdrawXchanger")
    @ResponseBody
    public Result<?> withdrawXchanger(@RequestParam(required = false,defaultValue = "0")String money){
        HashMap<String, Object> map = new HashMap<>();
        //最小提现金额
        String withdrawMinimum = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "withdraw_minimum");
        //获取字典配置提现阈值，大于此数据值则手续费按照大于的取，小于等于此数据值则手续费小于的取
        String withdrawThreshold = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "withdraw_threshold");
        //提现手续费小于阈值类型：0：固定费用（fixed）；1：百分比（percent）；
        String withdrawServiceChargeLessType = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "withdraw_service_charge_less_type");
        //提现手续费大于阈值类型：0：固定费用（fixed）；1：百分比（percent）；
        String withdrawServiceChargeGreaterType = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "withdraw_service_charge_greater_type");
        //小于阈值固定费用（fixed）
        String withdrawServiceChargeLessFixed = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "withdraw_service_charge_less_fixed");
        //小于阈值百分比（percent）
        String withdrawServiceChargeLessPercent = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "withdraw_service_charge_less_percent");
        //大于阈值固定费用（fixed）
        String withdrawServiceChargeGreaterFixed = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "withdraw_service_charge_greater_fixed");
        //大于阈值百分比（percent）
        String withdrawServiceChargeGreaterPercent = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "withdraw_service_charge_greater_percent");
        if (Double.valueOf(money)<Double.valueOf(withdrawMinimum)){
            map.put("serviceCharge",withdrawServiceChargeLessFixed);
            map.put("amount",0);
            map.put("withdrawExplain",withdrawServiceChargeLessFixed);
            map.put("withdrawType",0);
        }else {
            if (Double.valueOf(money)<=Double.valueOf(withdrawThreshold)){
                if (withdrawServiceChargeLessType.equals("0")){
                    map.put("serviceCharge",new BigDecimal(withdrawServiceChargeLessFixed));
                    map.put("amount",new BigDecimal(money).subtract(new BigDecimal(withdrawServiceChargeLessFixed)));
                    map.put("withdrawExplain",withdrawServiceChargeLessFixed);
                    map.put("withdrawType",0);
                }else {
                    BigDecimal decimal = new BigDecimal(money);
                    map.put("serviceCharge",decimal.multiply(new BigDecimal(withdrawServiceChargeLessPercent).divide(new BigDecimal(100))).setScale(2, BigDecimal.ROUND_DOWN));
                    map.put("amount",decimal.subtract(new BigDecimal(String.valueOf(map.get("serviceCharge")))));
                    map.put("withdrawExplain",withdrawServiceChargeLessPercent);
                    map.put("withdrawType",1);
                }
            }else {
                if (withdrawServiceChargeGreaterType.equals("0")){
                    map.put("serviceCharge",new BigDecimal(withdrawServiceChargeGreaterFixed));
                    map.put("amount",new BigDecimal(money).subtract(new BigDecimal(String.valueOf(map.get("serviceCharge")))));
                    map.put("withdrawExplain",withdrawServiceChargeGreaterFixed);
                    map.put("withdrawType",2);
                }else {
                    map.put("serviceCharge",new BigDecimal(money).multiply(new BigDecimal(withdrawServiceChargeGreaterPercent).divide(new BigDecimal(100))).setScale(2, BigDecimal.ROUND_DOWN));
                    map.put("amount",new BigDecimal(money).subtract(new BigDecimal(String.valueOf(map.get("serviceCharge")))));
                    map.put("withdrawExplain",withdrawServiceChargeGreaterPercent);
                    map.put("withdrawType",3);
                }
            }
        }
        return Result.ok(map);
    }
}
