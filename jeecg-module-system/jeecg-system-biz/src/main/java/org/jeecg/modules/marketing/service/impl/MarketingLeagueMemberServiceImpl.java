package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.marketing.dto.MarketingLeagueMemberDTO;
import org.jeecg.modules.marketing.entity.MarketingLeagueIdentity;
import org.jeecg.modules.marketing.entity.MarketingLeagueMember;
import org.jeecg.modules.marketing.entity.MarketingLeagueSetting;
import org.jeecg.modules.marketing.mapper.MarketingLeagueMemberMapper;
import org.jeecg.modules.marketing.service.IMarketingLeagueIdentityService;
import org.jeecg.modules.marketing.service.IMarketingLeagueMemberService;
import org.jeecg.modules.marketing.service.IMarketingLeagueSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Description: 加盟专区-人员关系
 * @Author: jeecg-boot
 * @Date:   2021-12-25
 * @Version: V1.0
 */
@Service
public class MarketingLeagueMemberServiceImpl extends ServiceImpl<MarketingLeagueMemberMapper, MarketingLeagueMember> implements IMarketingLeagueMemberService {

    @Autowired
    private IMarketingLeagueSettingService iMarketingLeagueSettingService;

    @Autowired
    private IMarketingLeagueIdentityService iMarketingLeagueIdentityService;



    @Override
    public IPage<Map<String, Object>> queryPageList(Page<Map<String, Object>> page, MarketingLeagueMemberDTO marketingLeagueJoinUserDTO) {
        return baseMapper.queryPageList(page,marketingLeagueJoinUserDTO);
    }

    @Override
    public IPage<Map<String, Object>> queryPageListByGetWay(Page<Map<String, Object>> page, MarketingLeagueMemberDTO marketingLeagueJoinUserDTO) {
        return baseMapper.queryPageListByGetWay(page,marketingLeagueJoinUserDTO);
    }

    @Override
    public IPage<Map<String, Object>> getMarketingLeagueMemberList(Page<Map<String, Object>> page, Map<String, Object> paramMap) {
        return baseMapper.getMarketingLeagueMemberList(page,paramMap);
    }

    @Override
    public void ordinary(String memberListId, String parantId) {
        MarketingLeagueSetting marketingLeagueSetting=iMarketingLeagueSettingService.getMarketingLeagueSetting();
        if(marketingLeagueSetting!=null){
            MarketingLeagueIdentity marketingLeagueIdentity=iMarketingLeagueIdentityService.getOne(new LambdaQueryWrapper<MarketingLeagueIdentity>().eq(MarketingLeagueIdentity::getGetWay,"0").last("limit 1"));
            if(marketingLeagueIdentity!=null){
                MarketingLeagueMember marketingLeagueMember=new MarketingLeagueMember();
                marketingLeagueMember.setMemberListId(memberListId);
                marketingLeagueMember.setParantId(parantId);
                marketingLeagueMember.setMarketingLeagueIdentityId(marketingLeagueIdentity.getId());
                marketingLeagueMember.setAdditionalIdentity(marketingLeagueIdentity.getAdditionalIdentity());
                this.save(marketingLeagueMember);
            }
        }
    }
}
