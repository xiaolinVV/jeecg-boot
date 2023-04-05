package org.jeecg.modules.marketing.store.giftbag.api;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.marketing.store.giftbag.entity.*;
import org.jeecg.modules.marketing.store.giftbag.service.*;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.service.IMemberListService;
import org.jeecg.modules.store.entity.StoreManage;
import org.jeecg.modules.store.service.IStoreManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("after/marketingStoreGiftbagTeam")
public class AfterMarketingStoreGiftbagTeamController {


    @Autowired
    private IMarketingStoreGiftbagTeamService iMarketingStoreGiftbagTeamService;

    @Autowired
    private IMarketingStoreGiftbagSettingService iMarketingStoreGiftbagSettingService;

    @Autowired
    private IMemberListService iMemberListService;

    @Autowired
    private IMarketingStoreGiftbagIdentityService iMarketingStoreGiftbagIdentityService;

    @Autowired
    private IStoreManageService iStoreManageService;

    @Autowired
    private IMarketingStoreGiftbagListService iMarketingStoreGiftbagListService;

    @Autowired
    private IMarketingStoreGiftbagTeamMemberService iMarketingStoreGiftbagTeamMemberService;


    /**
     * 获取团队列表
     *
     * @param pageNo
     * @param pageSize
     * @param memberId
     * @return
     */
    @PostMapping("getMarketingStoreGiftbagTeamList")
    public Result<?> getMarketingStoreGiftbagTeamList( @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                       @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                       @RequestAttribute("memberId") String memberId,
                                                       String status
                                                       ){
        Map<String,Object> paramMap= Maps.newHashMap();
        paramMap.put("memberId",memberId);
        paramMap.put("status",status);
        IPage<Map<String,Object>> mapIPage=iMarketingStoreGiftbagTeamService.getMarketingStoreGiftbagTeamList(new Page<>(pageNo,pageSize),paramMap);
        mapIPage.getRecords().forEach(m->{
            MarketingStoreGiftbagTeam marketingStoreGiftbagTeam=iMarketingStoreGiftbagTeamService.getById(m.get("id").toString());
            MarketingStoreGiftbagList marketingStoreGiftbagList=iMarketingStoreGiftbagListService.getMarketingStoreGiftbagListByStoreManageId(marketingStoreGiftbagTeam.getStoreManageId());
            Map<String,Object> marketingStoreGiftbagListMap=Maps.newHashMap();
            marketingStoreGiftbagListMap.put("id",marketingStoreGiftbagList.getId());
            marketingStoreGiftbagListMap.put("surfacePlot",marketingStoreGiftbagList.getSurfacePlot());
            marketingStoreGiftbagListMap.put("price",marketingStoreGiftbagList.getPrice());
            StoreManage storeManage=iStoreManageService.getById(marketingStoreGiftbagList.getStoreManageId());
            marketingStoreGiftbagListMap.put("storeManageId",storeManage.getId());
            marketingStoreGiftbagListMap.put("takeOutPhone",storeManage.getTakeOutPhone());
            marketingStoreGiftbagListMap.put("storeName",storeManage.getStoreName() + "(" + storeManage.getSubStoreName() + ")");
            MarketingStoreGiftbagSetting marketingStoreGiftbagSetting=iMarketingStoreGiftbagSettingService.getMarketingStoreGiftbagSetting();
            marketingStoreGiftbagListMap.put("labei",marketingStoreGiftbagSetting.getAnotherName());
            m.put("marketingStoreGiftbagListMap",marketingStoreGiftbagListMap);

            List<Map<String,Object>> membersMaps= Lists.newArrayList();

            iMarketingStoreGiftbagTeamMemberService.list(new LambdaQueryWrapper<MarketingStoreGiftbagTeamMember>()
                    .eq(MarketingStoreGiftbagTeamMember::getMarketingStoreGiftbagTeamId,marketingStoreGiftbagTeam.getId())
                    .orderByAsc(MarketingStoreGiftbagTeamMember::getIdentity)).forEach(ms->{
                        Map<String,Object> memberMap=Maps.newHashMap();
                MarketingStoreGiftbagIdentity storeGiftbagIdentity = iMarketingStoreGiftbagIdentityService.getOne(new LambdaQueryWrapper<MarketingStoreGiftbagIdentity>()
                        .eq(MarketingStoreGiftbagIdentity::getLevel, ms.getIdentity()));
                memberMap.put("IdentityName",storeGiftbagIdentity != null ? StrUtil.emptyIfNull(storeGiftbagIdentity.getIdentityName()):"");
                MemberList memberList = iMemberListService.getById(ms.getMemberListId());
                memberMap.put("headPortrait",memberList != null ? StrUtil.emptyIfNull(memberList.getHeadPortrait()) :"");
                membersMaps.add(memberMap);
            });
            m.put("membersMaps",membersMaps);
        });

        return Result.ok(mapIPage);
    }
}
