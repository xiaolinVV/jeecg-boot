package org.jeecg.modules.member.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.member.dto.MemberDistributionLevelDTO;
import org.jeecg.modules.member.entity.MemberDistributionLevel;

import java.util.Map;

/**
 * @Description: 会员分销等级关系
 * @Author: jeecg-boot
 * @Date:   2021-07-01
 * @Version: V1.0
 */
public interface MemberDistributionLevelMapper extends BaseMapper<MemberDistributionLevel> {


    /**
     *
     * 后端查询列表
     *
     * @param page
     * @param memberDistributionLevelDTO
     * @return
     */
    public IPage<Map<String,Object>> queryPageList(Page<Map<String,Object>> page,@Param("memberDistributionLevelDTO") MemberDistributionLevelDTO memberDistributionLevelDTO);

}
