package org.jeecg.modules.member.wapi;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.member.dto.MemberBankCardDTO;
import org.jeecg.modules.member.entity.MemberBankCard;
import org.jeecg.modules.member.service.IMemberBankCardService;
import org.jeecg.modules.member.vo.MemberBankCardVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@RequestMapping("wapi/memberBankCard")
@Controller
public class WapiMemberBankCardController {
    @Autowired
    private IMemberBankCardService iMemberBankCardService;

    /**
     * 银行卡返显
     * @param memberId
     * @return
     */
    @RequestMapping("findMemberBankCard")
    @ResponseBody
    public Result<?> findMemberBankCard(String memberId){
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
     * @return
     */
    @RequestMapping("setMemberBankCard")
    @ResponseBody
    public Result<?> setMemberBankCard(MemberBankCardVO memberBankCardVO){
        LambdaQueryWrapper<MemberBankCard> memberBankCardLambdaQueryWrapper = new LambdaQueryWrapper<MemberBankCard>()
                .eq(MemberBankCard::getDelFlag, "0")
                .eq(MemberBankCard::getMemberListId, memberBankCardVO.getMemberListId())
                .eq(MemberBankCard::getCarType, "0")
                .orderByDesc(MemberBankCard::getCreateTime);
        List<MemberBankCard> list = iMemberBankCardService.list(memberBankCardLambdaQueryWrapper);
        if (list.size()>0){
            MemberBankCard memberBankCard = list.get(0);
            memberBankCardVO.setId(memberBankCard.getId());
            BeanUtils.copyProperties(memberBankCardVO,memberBankCard);
            iMemberBankCardService.updateById(memberBankCard);
        }else {
            MemberBankCard memberBankCard = new MemberBankCard();
            BeanUtils.copyProperties(memberBankCardVO,memberBankCard);
            iMemberBankCardService.save(memberBankCard.setDelFlag("0"));
        }

        return Result.ok("绑定成功!");
    }

    /**
     * 银行卡列表
     * @param memberBankCardVO
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("memberBankCardPageList")
    @ResponseBody
    public Result<?> memberBankCardPageList(MemberBankCardVO memberBankCardVO,
                                            @RequestParam(name = "pageNo",defaultValue = "1")Integer pageNo,
                                            @RequestParam(name = "pageSize",defaultValue = "10")Integer pageSize){
        Page<MemberBankCardVO> page = new Page<>(pageNo,pageSize);
        MemberBankCardDTO memberBankCardDTO = new MemberBankCardDTO();
        BeanUtils.copyProperties(memberBankCardVO,memberBankCardDTO);
        IPage<MemberBankCardVO> memberBankCard = iMemberBankCardService.findMemberBankCard(page, memberBankCardDTO);
        return Result.ok(memberBankCard);
    }
}
