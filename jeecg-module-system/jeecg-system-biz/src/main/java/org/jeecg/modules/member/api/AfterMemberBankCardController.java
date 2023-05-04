package org.jeecg.modules.member.api;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.member.entity.MemberBankCard;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.service.IMemberBankCardService;
import org.jeecg.modules.member.service.IMemberListService;
import org.jeecg.modules.system.service.ISysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

/**
 * 会员银行卡接口
 */

@Controller
@RequestMapping("after/memberBankCard")
public class AfterMemberBankCardController {
    @Autowired
    private IMemberBankCardService iMemberBankCardService;
    @Autowired
    private ISysDictService iSysDictService;
    @Autowired
    private IMemberListService iMemberListService;


    /**
     * 银行卡返显
     * @param memberId
     * @return
     */
    @RequestMapping("returnMemberBankCard")
    @ResponseBody
    public Result<?> returnMemberBankCard(@RequestAttribute("memberId") String memberId){
        LambdaQueryWrapper<MemberBankCard> memberBankCardLambdaQueryWrapper = new LambdaQueryWrapper<MemberBankCard>()
                .eq(MemberBankCard::getMemberListId, memberId)
                .eq(MemberBankCard::getCarType, "0")
                .orderByDesc(MemberBankCard::getCreateTime);
        MemberBankCard memberBankCard = iMemberBankCardService.getOne(memberBankCardLambdaQueryWrapper);
      return Result.ok(memberBankCard);
    }

    /**
     * 添加银行卡(post)
     * @param memberBankCard
     * @return
     */
    @RequestMapping("addMemberBankCard")
    @ResponseBody
    public Result<?> addMemberBankCard( MemberBankCard memberBankCard,
                                       @RequestAttribute("memberId") String memberId){

        //查询是否已存在银行卡
        LambdaQueryWrapper<MemberBankCard> memberBankCardLambdaQueryWrapper = new LambdaQueryWrapper<MemberBankCard>()
                .eq(MemberBankCard::getMemberListId, memberId)
                .eq(MemberBankCard::getCarType, "0")
                .orderByDesc(MemberBankCard::getCreateTime);
        MemberBankCard memberBankCard1 = iMemberBankCardService.getOne(memberBankCardLambdaQueryWrapper);
        if( memberBankCard1!=null){
            memberBankCard.setId(memberBankCard1.getId());
            memberBankCard.setCreateTime(memberBankCard1.getCreateTime());
            memberBankCard.setCreateBy(memberBankCard1.getCreateBy());
        }
        memberBankCard.setMemberListId(memberId);
        iMemberBankCardService.saveOrUpdate(memberBankCard);
        //是否同步汇付银行卡信息：https://docs.adapay.tech/api/busiprocess.html#id2
        String huifuBankcardVerify = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "huifu_bankcard_verify");
        if(huifuBankcardVerify.equals("1")){
            if(StringUtils.isBlank(memberBankCard.getSettleAccount())){
                return iMemberBankCardService.createMemberPrivate(memberBankCard);
            }else{
                return iMemberBankCardService.updateSettleAccountPrivate(memberBankCard);
            }
        }
        return Result.ok("添加成功");
    }


    /**
     * getSysBankList
     * @return
     */
    @RequestMapping("getWithdrawalBankCardRequired")
    @ResponseBody
    public Result<?> getWithdrawalBankCardRequired(@RequestAttribute("memberId") String memberId){
        HashMap<String, Object> map = new HashMap<>();
        String withdrawalBankCardRequired = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "withdrawal_bank_card_required");
        map.put("withdrawalBankCardRequired",withdrawalBankCardRequired);
        MemberList memberList=iMemberListService.getById(memberId);
        map.put("phone",memberList.getPhone());
        return Result.ok(map);
    }
}
