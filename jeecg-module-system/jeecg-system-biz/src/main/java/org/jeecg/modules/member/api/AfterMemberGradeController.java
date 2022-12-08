package org.jeecg.modules.member.api;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.member.entity.MemberEquities;
import org.jeecg.modules.member.entity.MemberGrade;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 会员等级接口
 */
@RequestMapping("after/memberGrade")
@Controller
public class AfterMemberGradeController {
    @Autowired
    private IMemberListService iMemberListService;
    @Autowired
    private IMemberGradeService iMemberGradeService;
    @Autowired
    private IMemberGrowthRecordService iMemberGrowthRecordService;
    @Autowired
    private IMemberEquitiesService iMemberEquitiesService;
    @RequestMapping("getMemberGradeInfo")
    @ResponseBody
    public Result<Map<String,Object>> getMemberGradeInfo(@RequestHeader(defaultValue = "") String sysUserId,
                                                    HttpServletRequest request){
        String memberId=request.getAttribute("memberId").toString();
        Result<Map<String,Object>> result =new Result<>();
        MemberList memberList = iMemberListService.getById(memberId);
        HashMap<String, Object> map = new HashMap<>();
        List<MemberEquities> memberEquitiesList = iMemberEquitiesService.list(new LambdaQueryWrapper<MemberEquities>()
                .eq(MemberEquities::getDelFlag, "0"));
        if (memberEquitiesList.size()>0){
            map.put("memberEquities",memberEquitiesList.get(0).getEquities());
        }
        if (StringUtils.isNotBlank(memberList.getMemberGradeId())){
            MemberGrade memberGrade = iMemberGradeService.getById(memberList.getMemberGradeId());
            if (oConvertUtils.isNotEmpty(memberGrade)){
                map.put("memberGradeLogo",memberGrade.getGradeLogo());
                map.put("memberGradeName",memberGrade.getGradeName());
                map.put("memberGrowthValue",memberList.getGrowthValue());
                //获取下一级
                LambdaQueryWrapper<MemberGrade> memberGradeLambdaQueryWrapper = new LambdaQueryWrapper<MemberGrade>()
                        .eq(MemberGrade::getDelFlag, "0")
                        .eq(MemberGrade::getStatus, "1")
                        .eq(MemberGrade::getSort, memberGrade.getSort().add(new BigDecimal(1)));
                if (iMemberGradeService.count(memberGradeLambdaQueryWrapper)>0){
                    MemberGrade goUpMemberGrade = iMemberGradeService.list(memberGradeLambdaQueryWrapper.orderByAsc(MemberGrade::getSort)).get(0);
                    map.put("goUpMemberGradeDistance",goUpMemberGrade.getGrowthValueSmall().subtract(memberList.getGrowthValue()));
                    map.put("goUpMemberGradeName",goUpMemberGrade.getGradeName());
                    map.put("goUpMemberGrowthValue",goUpMemberGrade.getGrowthValueSmall());
                }else {
                    map.put("goUpMemberGradeDistance","");
                    map.put("goUpMemberGradeName","");
                    map.put("goUpMemberGrowthValue",memberGrade.getGrowthValueBig());
                }
                List<Map<String, String>> referMemberGradeList = iMemberGradeService.getReferMemberGradeList();
                map.put("referMemberGradeList",referMemberGradeList);
                result.setResult(map);
                result.success("返回会员等级");
            }else {
                return result.error500("会员信息异常,请联系管理员");
            }
        }else {
            map.put("memberGradeLogo","");
            map.put("memberGradeName","普通会员");
            map.put("memberGrowthValue",memberList.getGrowthValue());
            //获取下一级
            LambdaQueryWrapper<MemberGrade> memberGradeLambdaQueryWrapper = new LambdaQueryWrapper<MemberGrade>()
                    .eq(MemberGrade::getDelFlag, "0")
                    .eq(MemberGrade::getStatus, "1")
                    .orderByAsc(MemberGrade::getSort);
            if (iMemberGradeService.count(memberGradeLambdaQueryWrapper)>0){
                MemberGrade goUpMemberGrade = iMemberGradeService.list(memberGradeLambdaQueryWrapper.orderByAsc(MemberGrade::getSort)).get(0);
                map.put("goUpMemberGradeDistance",goUpMemberGrade.getGrowthValueSmall().subtract(memberList.getGrowthValue()));
                map.put("goUpMemberGradeName",goUpMemberGrade.getGradeName());
                map.put("goUpMemberGrowthValue",goUpMemberGrade.getGrowthValueSmall());
            }else {
                map.put("goUpMemberGradeDistance","");
                map.put("goUpMemberGradeName","");
                map.put("goUpMemberGrowthValue","");
            }
            List<Map<String, String>> referMemberGradeList = iMemberGradeService.getReferMemberGradeList();
            map.put("referMemberGradeList",referMemberGradeList);
            result.setResult(map);
            result.success("返回会员等级");
        }

        return result;
    }
}
