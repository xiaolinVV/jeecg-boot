package org.jeecg.modules.marketing.api;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.marketing.entity.MarketingLeagueIdentity;
import org.jeecg.modules.marketing.entity.MarketingLeagueMember;
import org.jeecg.modules.marketing.service.IMarketingLeagueIdentityService;
import org.jeecg.modules.marketing.service.IMarketingLeagueMemberService;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.service.IMemberListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@RequestMapping("after/marketingLeagueMember")
@Controller
public class AfterMarketingLeagueMemberController {

    @Autowired
    private IMarketingLeagueMemberService iMarketingLeagueMemberService;

    @Autowired
    private IMarketingLeagueIdentityService iMarketingLeagueIdentityService;

    @Autowired
    private IMemberListService iMemberListService;



    /**
     * 申请成为城市服务商
     *
     * @param memberId
     * @return
     */
    @RequestMapping("facilitator")
    @ResponseBody
    public Result<?> facilitator(@RequestAttribute("memberId") String memberId,String sysAreas,String areaExplain){
        MarketingLeagueIdentity marketingLeagueIdentity=iMarketingLeagueIdentityService.getOne(new LambdaQueryWrapper<MarketingLeagueIdentity>().eq(MarketingLeagueIdentity::getGetWay,"5"));

        MarketingLeagueMember marketingLeagueMemberPro=iMarketingLeagueMemberService.getOne(new LambdaQueryWrapper<MarketingLeagueMember>()
                .eq(MarketingLeagueMember::getMemberListId,memberId)
                .eq(MarketingLeagueMember::getAdditionalIdentity,"0"));
        MarketingLeagueIdentity marketingLeagueIdentityPro=iMarketingLeagueIdentityService.getById(marketingLeagueMemberPro.getMarketingLeagueIdentityId());
        if(marketingLeagueIdentityPro.getGetWay().equals("0")){
            return Result.error("会员当前身份等级不足");
        }
        if(marketingLeagueIdentityPro.getGetWay().equals("1")){
            return Result.error("会员当前身份等级不足");
        }

        if(marketingLeagueIdentity!=null){
            //查询身份是否存在
            long count = iMarketingLeagueMemberService.count(new LambdaQueryWrapper<MarketingLeagueMember>()
                    .eq(MarketingLeagueMember::getMarketingLeagueIdentityId, marketingLeagueIdentity.getId())
                    .eq(MarketingLeagueMember::getMemberListId, memberId));
            if (count > 0) {
                return Result.error("会员身份已存在");
            }

            long directDriveCount=iMarketingLeagueMemberService.count(new LambdaQueryWrapper<MarketingLeagueMember>()
                    .ne(MarketingLeagueMember::getMarketingLeagueIdentityId, iMarketingLeagueIdentityService.getOne(new LambdaQueryWrapper<MarketingLeagueIdentity>().eq(MarketingLeagueIdentity::getGetWay,"0")).getId())
                    .eq(MarketingLeagueMember::getSrcParantId, memberId)
                    .eq(MarketingLeagueMember::getAdditionalIdentity,"0"));

            if(directDriveCount<marketingLeagueIdentity.getDirectDrive().intValue()){
                return Result.error("直推必须超过："+marketingLeagueIdentity.getDirectDrive()+"人");
            }


            long marketingLeagueMemberCount=iMarketingLeagueMemberService.count(new LambdaQueryWrapper<MarketingLeagueMember>()
                    .eq(MarketingLeagueMember::getMarketingLeagueIdentityId, marketingLeagueIdentity.getId())
                    .eq(MarketingLeagueMember::getSysAreas, sysAreas));

            if(marketingLeagueMemberCount>=3){
                return Result.error("区域身份数量过多");
            }

            MarketingLeagueMember marketingLeagueMember = new MarketingLeagueMember();
            marketingLeagueMember.setMemberListId(memberId);
            marketingLeagueMember.setMarketingLeagueIdentityId(marketingLeagueIdentity.getId());
            marketingLeagueMember.setAdditionalIdentity(marketingLeagueIdentity.getAdditionalIdentity());
            marketingLeagueMember.setSysAreas(sysAreas);
            marketingLeagueMember.setAreaExplain(areaExplain);
            iMarketingLeagueMemberService.save(marketingLeagueMember);

            return Result.ok("已成功变更身份");

        }else{
            return Result.error("未找到城市服务商");
        }

    }


    /**
     * 获取城市服务商信息
     *
     * @param memberId
     * @return
     */
    @RequestMapping("getIdentityFacilitator")
    @ResponseBody
    public Result<?> getIdentityFacilitator(@RequestAttribute("memberId") String memberId){
        MarketingLeagueIdentity marketingLeagueIdentity=iMarketingLeagueIdentityService.getOne(new LambdaQueryWrapper<MarketingLeagueIdentity>().eq(MarketingLeagueIdentity::getGetWay,"5"));
        if(marketingLeagueIdentity!=null){
            return Result.ok(marketingLeagueIdentity);
        }else{
            return Result.error("未找到城市服务商");
        }
    }


    /**
     *
     * 获取加盟专区会员信息
     *
     * @param memberId
     * @return
     */
    @RequestMapping("getMarketingLeagueMember")
    @ResponseBody
    public Result<?> getMarketingLeagueMember(@RequestAttribute("memberId") String memberId){
        Map<String,Object> resultMap=Maps.newHashMap();
        MemberList memberList=iMemberListService.getById(memberId);
        resultMap.put("phone",memberList.getPhone());
        resultMap.put("headPortrait",memberList.getHeadPortrait());
        List<String> identityList= Lists.newArrayList();
        List<String> identityGetWayList=Lists.newArrayList();
        List<MarketingLeagueMember> marketingLeagueMemberList=iMarketingLeagueMemberService.list(new LambdaQueryWrapper<MarketingLeagueMember>().eq(MarketingLeagueMember::getMemberListId,memberId));
        for (MarketingLeagueMember m:marketingLeagueMemberList) {
            MarketingLeagueIdentity marketingLeagueIdentity=iMarketingLeagueIdentityService.getById(m.getMarketingLeagueIdentityId());
            identityList.add(marketingLeagueIdentity.getIdentityName());
            identityGetWayList.add(marketingLeagueIdentity.getGetWay());
        }
        resultMap.put("identityList",identityList);
        resultMap.put("identityGetWayList",identityGetWayList);
        return Result.ok(resultMap);
    }



    /**
     * 我的用户
     *
     * @param memberId
     * @return
     */
    @RequestMapping("getMarketingLeagueMemberCount")
    @ResponseBody
    public Result<?> getMarketingLeagueMemberCount(@RequestAttribute("memberId") String memberId){
        Map<String,Object> resultMap= Maps.newHashMap();
        //普通会员
        resultMap.put("nomalMember",iMarketingLeagueMemberService.count(new LambdaQueryWrapper<MarketingLeagueMember>()
                .eq(MarketingLeagueMember::getParantId,memberId)
                .eq(MarketingLeagueMember::getMarketingLeagueIdentityId,iMarketingLeagueIdentityService.getOne(new LambdaQueryWrapper<MarketingLeagueIdentity>()
                        .eq(MarketingLeagueIdentity::getGetWay,"0")).getId())));
        resultMap.put("newMember",iMarketingLeagueMemberService.count(new LambdaQueryWrapper<MarketingLeagueMember>()
                .eq(MarketingLeagueMember::getParantId,memberId)
                .eq(MarketingLeagueMember::getMarketingLeagueIdentityId,iMarketingLeagueIdentityService.getOne(new LambdaQueryWrapper<MarketingLeagueIdentity>()
                        .eq(MarketingLeagueIdentity::getGetWay,"1")).getId())));
        resultMap.put("storeMember",iMarketingLeagueMemberService.count(new LambdaQueryWrapper<MarketingLeagueMember>()
                .eq(MarketingLeagueMember::getParantId,memberId)
                .eq(MarketingLeagueMember::getMarketingLeagueIdentityId,iMarketingLeagueIdentityService.getOne(new LambdaQueryWrapper<MarketingLeagueIdentity>()
                        .eq(MarketingLeagueIdentity::getGetWay,"2")).getId())));
        return Result.ok(resultMap);
    }

    /**
     * 获取人员列表
     *
     * @param pageNo
     * @param pageSize
     * @param memberId
     * @param getWay
     * @return
     */
    @RequestMapping("getMarketingLeagueMemberList")
    @ResponseBody
    public Result<?> getMarketingLeagueMemberList(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                  @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                  @RequestAttribute("memberId") String memberId,
                                                  String getWay){
        Map<String,Object> paramMap=Maps.newHashMap();
        paramMap.put("memberId",memberId);
        paramMap.put("getWay",getWay);
        return Result.ok(iMarketingLeagueMemberService.getMarketingLeagueMemberList(new Page<>(pageNo,pageSize),paramMap));
    }
}
