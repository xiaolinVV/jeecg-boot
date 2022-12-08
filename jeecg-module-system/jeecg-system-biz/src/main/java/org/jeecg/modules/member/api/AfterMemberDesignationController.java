package org.jeecg.modules.member.api;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.member.entity.*;
import org.jeecg.modules.member.service.*;
import org.jeecg.modules.member.vo.MemberDesignationVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RequestMapping("after/memberDesignation")
@Controller
@Slf4j
public class AfterMemberDesignationController {

    @Autowired
    private IMemberListService iMemberListService;

    @Autowired
    private IMemberDesignationService iMemberDesignationService;

    @Autowired
    private IMemberDesignationCountService iMemberDesignationCountService;

    @Autowired
    private IMemberDesignationMemberListService iMemberDesignationMemberListService;
    @Autowired
    private IMemberDesignationGroupService iMemberDesignationGroupService;
    @RequestMapping("getMemberDesignationInfo")
    @ResponseBody
    public Result<Map<String,Object>> getMemberDesignationInfo(@RequestHeader(defaultValue = "") String sysUserId,
                                                               String id,
                                                         HttpServletRequest request){
        String memberId=request.getAttribute("memberId").toString();
        log.info("id+++"+id);
        Result<Map<String,Object>> result =new Result<>();

        MemberDesignationMemberList memberDesignationMemberList = iMemberDesignationMemberListService.getById(id);

        MemberList memberList = iMemberListService.getById(memberId);


        HashMap<String, Object> map = new HashMap<>();

        MemberDesignationVO memberDesignationVO = iMemberDesignationService.getMemberDesignationById(memberDesignationMemberList.getMemberDesignationId());
        map.put("id",id);
        map.put("name",memberDesignationVO.getName());
        map.put("logoAddr",memberDesignationVO.getLogoAddr());
        map.put("sort",memberDesignationVO.getSort());
        ArrayList<Map<String, Object>> maps = new ArrayList<>();
        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("designationName",memberDesignationVO.getName()+"分红");
        stringObjectHashMap.put("balance","￥"+memberDesignationVO.getBalance());
        stringObjectHashMap.put("participation","当前参与分红人数");
        stringObjectHashMap.put("participationSum",iMemberDesignationMemberListService.count(new LambdaQueryWrapper<MemberDesignationMemberList>().eq(MemberDesignationMemberList::getMemberDesignationId,memberDesignationVO.getId())));
        maps.add(stringObjectHashMap);

        if (memberDesignationVO.getLowLevelDividends().equals("1")&&StringUtils.isNotBlank(memberDesignationVO.getMemberDesignations())){
            List<String> stringList = Arrays.asList(StringUtils.split(memberDesignationVO.getMemberDesignations(), ","));
            stringList.forEach(sl->{
                MemberDesignation memberDesignation = iMemberDesignationService.getById(sl);

                HashMap<String, Object> stringObjectHashMap1 = new HashMap<>();
                stringObjectHashMap1.put("designationName",memberDesignation.getName()+"分红");
                stringObjectHashMap1.put("balance","￥"+memberDesignation.getBalance());
                stringObjectHashMap1.put("participation","当前参与分红人数");
                stringObjectHashMap1.put("participationSum",iMemberDesignationMemberListService.count(new LambdaQueryWrapper<MemberDesignationMemberList>().eq(MemberDesignationMemberList::getMemberDesignationId,sl)));
                maps.add(stringObjectHashMap1);
            });

        }
        map.put("memberDesignationList",maps);
        iMemberDesignationService.list(new LambdaQueryWrapper<MemberDesignation>()
                .eq(MemberDesignation::getDelFlag,"0")
                .eq(MemberDesignation::getStatus,"1")
                .eq(MemberDesignation::getLowLevelDividends,"1")
        );

        ArrayList<Map<String, Object>> arrayList = new ArrayList<>();
        if (StringUtils.isNotBlank(memberDesignationVO.getMemberDesignationGroupId())){
            //会员直推人数统计

            long memberDirectCount = iMemberDesignationMemberListService.count(new LambdaQueryWrapper<MemberDesignationMemberList>()
                    .eq(MemberDesignationMemberList::getDelFlag, "0")
                    .eq(MemberDesignationMemberList::getOldTManageId, memberList.getId())
                    .eq(MemberDesignationMemberList::getMemberDesignationGroupId,memberDesignationMemberList.getMemberDesignationGroupId())
            );
            HashMap<String, Object> objectHashMap = new HashMap<>();
            objectHashMap.put("groupName","团队总人数");
            objectHashMap.put("value",memberDesignationMemberList.getTotalMembers());
            arrayList.add(objectHashMap);
            HashMap<String, Object> objectHashMap1 = new HashMap<>();
            objectHashMap1.put("groupName","直推人数");
            objectHashMap1.put("value",memberDirectCount);
            arrayList.add(objectHashMap1);

            List<Map<String,Object>> memberDesignationCountList = iMemberDesignationCountService.findMemberdesignationCountListById(memberList.getId(), memberDesignationMemberList.getMemberDesignationGroupId());
            memberDesignationCountList.forEach(mdcl->{
                HashMap<String, Object> objectHashMap2 = new HashMap<>();
                objectHashMap2.put("groupName",mdcl.get("name"));
                objectHashMap2.put("value",mdcl.get("totalMembers"));
                arrayList.add(objectHashMap2);
            });

        }
        map.put("groupList",arrayList);
        result.setResult(map);
        result.success("会员称号");
        return result;
    }


    @RequestMapping("memberDesignationRule")
    @ResponseBody
    public Result<List<Map<String,Object>>> memberDesignationRule(String id,
                                                                  HttpServletRequest request){
        Result<List<Map<String,Object>>> result =new Result<>();
        MemberDesignationMemberList memberDesignationMemberList = iMemberDesignationMemberListService.getById(id);
        MemberDesignation memberDesignation = iMemberDesignationService.getById(memberDesignationMemberList.getMemberDesignationId());
        if (memberDesignation.getIsDefault().equals("1")){
            ArrayList<Map<String, Object>> mapArrayList = new ArrayList<>();
            HashMap<String, Object> map = new HashMap<>();
            map.put("id",memberDesignation.getId());
            map.put("name",memberDesignation.getName());
            map.put("logoAddr",memberDesignation.getLogoAddr());
            map.put("customRemark",memberDesignation.getCustomRemark());
            mapArrayList.add(map);
            result.setResult(mapArrayList);
        }else {
            result.setResult(iMemberDesignationService.listMaps(new QueryWrapper<MemberDesignation>()
                    .select("id,name,logo_addr as logoAddr,custom_remark as customRemark")
                    .eq("del_flag","0")
                    .eq("member_designation_group_id",memberDesignation.getMemberDesignationGroupId())
                    .eq("is_default","0")
                    .orderByAsc("sort")
            ));
        }

        result.success("称号规则");
        return result;
    }
    @RequestMapping("findMemberDesignationList")
    @ResponseBody
    public Result<?> findMemberDesignationList(HttpServletRequest request){
        String memberId=request.getAttribute("memberId").toString();
        return Result.ok(iMemberDesignationMemberListService.findMemberDesignationList(memberId));
    }
}
