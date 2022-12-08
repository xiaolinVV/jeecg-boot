package org.jeecg.modules.member.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.member.dto.MemberDistributionLevelDTO;
import org.jeecg.modules.member.entity.MemberDistributionLevel;

import java.util.Map;

/**
 * @Description: 会员分销等级关系
 * @Author: jeecg-boot
 * @Date:   2021-07-01
 * @Version: V1.0
 */
public interface IMemberDistributionLevelService extends IService<MemberDistributionLevel> {

    /**
     * 分销升级
     * @param memberId
     */
    public void upgrade(String memberId);


    /**
     * 分销团队升级
     *
     * @param memberId
     */
    public void teamUpgrade(String memberId);

    /**
     * 分销抢购团队升级
     *
     * @param memberId
     */
    public void teamRushUpgrade(String memberId);


    /**
     *
     * 后端查询列表
     *
     * @param page
     * @param memberDistributionLevelDTO
     * @return
     */
    public IPage<Map<String,Object>> queryPageList(Page<Map<String,Object>> page, MemberDistributionLevelDTO memberDistributionLevelDTO);

}
