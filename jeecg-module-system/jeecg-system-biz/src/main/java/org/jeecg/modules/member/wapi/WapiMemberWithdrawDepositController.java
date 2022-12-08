package org.jeecg.modules.member.wapi;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.member.entity.MemberBankCard;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.entity.MemberWithdrawDeposit;
import org.jeecg.modules.member.service.IMemberBankCardService;
import org.jeecg.modules.member.service.IMemberListService;
import org.jeecg.modules.member.service.IMemberWithdrawDepositService;
import org.jeecg.modules.member.vo.MemberWithdrawDepositVO;
import org.jeecg.modules.system.service.ISysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.HashMap;

@RequestMapping("wapi/memberWithdrawDeposit")
@Controller
public class WapiMemberWithdrawDepositController {
    @Autowired
    private IMemberWithdrawDepositService iMemberWithdrawDepositService;

    @Autowired
    private ISysDictService iSysDictService;

    @Autowired
    private IMemberListService iMemberListService;

    @Autowired
    private IMemberBankCardService iMemberBankCardService;
    @RequestMapping("pageList")
    @ResponseBody
    public Result<?> queryPageList(MemberWithdrawDepositVO memberWithdrawDepositVO,
                                                                 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                                 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {
        Result<IPage<MemberWithdrawDepositVO>> result = new Result<IPage<MemberWithdrawDepositVO>>();
        Page<MemberWithdrawDeposit> page = new Page<MemberWithdrawDeposit>(pageNo, pageSize);
        IPage<MemberWithdrawDepositVO> pageList = iMemberWithdrawDepositService.getMemberWithdrawDeposit(page, memberWithdrawDepositVO);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }
    @RequestMapping("memberWithdrawDepositAudit")
    @ResponseBody
    public Result<?> memberWithdrawDepositAudit(MemberWithdrawDepositVO memberWithdrawDepositVO){
        if (memberWithdrawDepositVO.getStatus().equals("2")){
         return iMemberWithdrawDepositService.remit(memberWithdrawDepositVO);
        }else if (memberWithdrawDepositVO.getStatus().equals("1")||memberWithdrawDepositVO.getStatus().equals("3")){
            return iMemberWithdrawDepositService.audit(memberWithdrawDepositVO);
        }else {
            return Result.ok("操作成功!");
        }
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

    //获取最低提现金额
    @RequestMapping("getWithdrawMinimum")
    @ResponseBody
    public Result<?> getWithdrawMinimum(){
        //获取最低提现金额
        String queryTableDictTextByKey = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "withdraw_minimum");
        return Result.ok(queryTableDictTextByKey);
    }
    //获取提现说明
    @RequestMapping("getWithdrawWarmPrompt")
    @ResponseBody
    public Result<?> getWithdrawWarmPrompt(){
        //获取提现说明
        String queryTableDictTextByKey = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "withdraw_warm_prompt");
        return Result.ok(queryTableDictTextByKey);
    }
    //提现
    @RequestMapping("memberWithdrawDepositAdd")
    @ResponseBody
    public Result<?> memberWithdrawDepositAdd(String money,
                                              String memberId,
                                              String bankCardId){
        MemberList memberList = iMemberListService.getById(memberId);
        MemberBankCard memberBankCard = iMemberBankCardService.getById(bankCardId);
        if (memberList.getBalance().doubleValue()>=Double.valueOf(money)){
            boolean b = iMemberWithdrawDepositService.memberWithdrawDepositAdd(new BigDecimal(money), memberList, memberBankCard);
            if (b){
                return Result.ok("操作成功");
            }else {
                return Result.ok("操作失败");
            }

        }else {
            return Result.error("提现失败,余额不足!");
        }

    }

    /**
     * 提现金额计算
     * @param money 金额
     * @return
     */
    @RequestMapping("withdrawXchanger")
    @ResponseBody
    public Result<?> withdrawXchanger(String money){
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
