package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.dto.MarketingLeagueMemberDTO;
import org.jeecg.modules.marketing.entity.MarketingLeagueMember;

import java.util.Map;

/**
 * @Description: 加盟专区-人员关系
 * @Author: jeecg-boot
 * @Date:   2021-12-25
 * @Version: V1.0
 */
public interface IMarketingLeagueMemberService extends IService<MarketingLeagueMember> {


    /**
     * 升级为普通会员
     *
     * @param memberListId
     * @param parantId
     */
    public void ordinary(String memberListId,String parantId);

    /**
     * 查询加盟商会员列表
     *
     * @param page
     * @param marketingLeagueJoinUserDTO
     * @return
     */
    public IPage<Map<String,Object>> queryPageList(Page<Map<String,Object>> page, MarketingLeagueMemberDTO marketingLeagueJoinUserDTO);



    /**
     * 查询加盟商会员列表通过获得方式
     *
     * @param page
     * @param marketingLeagueJoinUserDTO
     * @return
     */
    public IPage<Map<String,Object>> queryPageListByGetWay(Page<Map<String,Object>> page, MarketingLeagueMemberDTO marketingLeagueJoinUserDTO);



    /**
     * 获取人员列表
     *
     * @param page
     * @param paramMap
     * @return
     */
    public IPage<Map<String,Object>> getMarketingLeagueMemberList(Page<Map<String,Object>> page,Map<String,Object> paramMap);


}
