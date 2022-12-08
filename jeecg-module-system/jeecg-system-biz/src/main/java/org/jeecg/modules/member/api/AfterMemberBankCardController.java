package org.jeecg.modules.member.api;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Maps;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.member.entity.MemberBankCard;
import org.jeecg.modules.member.service.IMemberBankCardService;
import org.jeecg.modules.member.vo.MemberBankCardVO;
import org.jeecg.modules.system.service.ISysDictService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 会员银行卡接口
 */

@Controller
@RequestMapping("after/memberBankCard")
public class AfterMemberBankCardController {
    @Autowired
    private IMemberBankCardService iMemberBankCardService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ISysDictService iSysDictService;
    /**
     * 银行卡返显
     * @param request
     * @return
     */
    @RequestMapping("returnMemberBankCard")
    @ResponseBody
    public Result<?>returnMemberBankCard(HttpServletRequest request){
        String memberId = request.getAttribute("memberId").toString();
        LambdaQueryWrapper<MemberBankCard> memberBankCardLambdaQueryWrapper = new LambdaQueryWrapper<MemberBankCard>()
                .eq(MemberBankCard::getDelFlag, "0")
                .eq(MemberBankCard::getMemberListId, memberId)
                .eq(MemberBankCard::getCarType, "0")
                .orderByDesc(MemberBankCard::getCreateTime);
        List<MemberBankCard> memberBankCardList = iMemberBankCardService.list(memberBankCardLambdaQueryWrapper);
        if (memberBankCardList.size()>0){
            return Result.ok(memberBankCardList.get(0));
        }else {
            return Result.ok("");
        }
    }

    /**
     * 绑定银行卡
     * @param memberBankCardVO
     * @param request
     * @return
     */
    @RequestMapping("updateMemberBankCard")
    @ResponseBody
    public Result<String>updateMemberBankCard(MemberBankCardVO memberBankCardVO,
                                              HttpServletRequest request){
        String memberId = request.getAttribute("memberId").toString();
        Result<String> result = new Result<>();
        if (StringUtils.isBlank(memberBankCardVO.getPhone())){
            return result.error500("请输入手机号");
        }
        if (StringUtils.isBlank(memberBankCardVO.getCode())){
            return result.error500("请输入验证码");
        }
        if (StringUtils.isBlank(memberBankCardVO.getCardholder())){
            return result.error500("请输入收款人真实姓名");
        }
        if (StringUtils.isBlank(memberBankCardVO.getBankName())){
            return result.error500("请输入开户行");
        }
        if (StringUtils.isBlank(memberBankCardVO.getBankCard())){
            return result.error500("请输入银行卡卡号");
        }
        Object sbCode = redisUtil.get(memberBankCardVO.getPhone());

        if (oConvertUtils.isEmpty(sbCode)){
            return result.error500("验证码过期");
        }
        if (!sbCode.equals(memberBankCardVO.getCode())){
            return result.error500("验证码错误!");
        }
        MemberBankCard memberBankCard = iMemberBankCardService.getOne(new LambdaQueryWrapper<MemberBankCard>().
                eq(MemberBankCard::getMemberListId, memberId)
                .eq(MemberBankCard::getCarType, "0")
                .orderByDesc(MemberBankCard::getCreateTime)
                .last("limit 1"));
        if(memberBankCard==null){
            memberBankCard=new MemberBankCard();
        }else {
            //限制银行卡身份证和名字不可修改
            if (!memberBankCard.getIdentityNumber().equals(memberBankCardVO.getIdentityNumber())) {
                return result.success("身份证号必须和首次填入相同，不可修改");
            }
            if (!memberBankCard.getCardholder().equals(memberBankCardVO.getCardholder())) {
                return result.success("持卡人姓名必须和首次填入相同，不可修改");
            }
        }
        memberBankCardVO.setId(memberBankCard.getId());
        BeanUtils.copyProperties(memberBankCardVO,memberBankCard);
        memberBankCard.setMemberListId(memberId);
        memberBankCard.setCarType("0");
        boolean b = iMemberBankCardService.saveOrUpdate(memberBankCard);
        if (b){
            result.success("银行卡保存成功");
        }else {
            result.error500("保存失败,请重试!");
        }
        return result;
    }
    /**
     * 获取银行卡信息列表
     *
     * @return
     */
    @RequestMapping("getBlankList")
    @ResponseBody
    public Result<?> getBlankList(){
        try {
            Map<String,Object> resultMap= Maps.newHashMap();
            IOUtils.readLines(this.getClass().getClassLoader().getResourceAsStream("blank.properties"),"utf-8").forEach(blance->{
                resultMap.put(org.apache.commons.lang.StringUtils.substringBefore(blance,"="), org.apache.commons.lang.StringUtils.substringAfter(blance,"="));
            });

            return Result.ok(resultMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Result.ok();
    }

    /**
     * 获取银行卡城市
     * @return
     */
    @RequestMapping("findSrea")
    @ResponseBody
    public Result<?> findSrea(){
        try {
            return Result.ok(StringEscapeUtils.unescapeJava(org.apache.commons.lang.StringUtils.join(IOUtils.readLines(this.getClass().getClassLoader().getResourceAsStream("pay_area.properties"),"utf-8"),"")));
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error("异常");
        }
    }

    /**
     * 绑定银行卡必填项
     * @return
     */
    @RequestMapping("getWithdrawalBankCardRequired")
    @ResponseBody
    public Result<?> getWithdrawalBankCardRequired(){
        HashMap<String, Object> map = new HashMap<>();
        String withdrawalBankCardRequired = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "withdrawal_bank_card_required");
        map.put("withdrawalBankCardRequired",withdrawalBankCardRequired);
        return Result.ok(map);
    }
}
