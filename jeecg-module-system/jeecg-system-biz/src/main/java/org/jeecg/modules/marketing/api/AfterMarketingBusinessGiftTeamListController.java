package org.jeecg.modules.marketing.api;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.marketing.entity.MarketingBusinessGiftTeamList;
import org.jeecg.modules.marketing.entity.MarketingBusinessGiftTeamRecord;
import org.jeecg.modules.marketing.service.IMarketingBusinessGiftTeamListService;
import org.jeecg.modules.marketing.service.IMarketingBusinessGiftTeamRecordService;
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

@RequestMapping("after/marketingBusinessGiftTeamList")
@Controller
public class AfterMarketingBusinessGiftTeamListController {

    @Autowired
    private IMarketingBusinessGiftTeamListService iMarketingBusinessGiftTeamListService;

    @Autowired
    private IMarketingBusinessGiftTeamRecordService iMarketingBusinessGiftTeamRecordService;

    @Autowired
    private IMemberListService iMemberListService;


    /**
     * 根据会员id查询数据
     * @return
     */
    @RequestMapping("getByMemberId")
    @ResponseBody
    public Result<?> getByMemberId(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                   @RequestAttribute(value = "memberId",required = false) String memberId){

        IPage<Map<String, Object>> resultMapIPage= iMarketingBusinessGiftTeamListService.getByMemberId(new Page<>(pageNo,pageSize),memberId);
        resultMapIPage.getRecords().forEach(m->{
            MarketingBusinessGiftTeamRecord marketingBusinessGiftTeamRecord=iMarketingBusinessGiftTeamRecordService.getById(m.get("id").toString());
            m.put("serialNumber",marketingBusinessGiftTeamRecord.getSerialNumber());
            m.put("marketingBusinessGiftListId",marketingBusinessGiftTeamRecord.getMarketingBusinessGiftListId());
            m.put("status",marketingBusinessGiftTeamRecord.getStatus());
            List<Map<String,Object>> teamMapList= Lists.newArrayList();
            List<MarketingBusinessGiftTeamList> marketingBusinessGiftTeamLists=iMarketingBusinessGiftTeamListService.list(new LambdaQueryWrapper<MarketingBusinessGiftTeamList>().eq(MarketingBusinessGiftTeamList::getMarketingBusinessGiftTeamRecordId,marketingBusinessGiftTeamRecord.getId()).orderByDesc(MarketingBusinessGiftTeamList::getGrade));
            for (MarketingBusinessGiftTeamList marketingBusinessGiftTeamList:marketingBusinessGiftTeamLists) {
                Map<String,Object> marketingBusinessGiftTeamListMap= Maps.newHashMap();
                MemberList memberList=iMemberListService.getById(marketingBusinessGiftTeamList.getMemberListId());
                marketingBusinessGiftTeamListMap.put("headPortrait",memberList.getHeadPortrait());
                marketingBusinessGiftTeamListMap.put("grade",marketingBusinessGiftTeamList.getGrade());
                teamMapList.add(marketingBusinessGiftTeamListMap);
            }
            m.put("teamMapList",teamMapList);
        });
        return Result.ok(resultMapIPage);
    }


}
