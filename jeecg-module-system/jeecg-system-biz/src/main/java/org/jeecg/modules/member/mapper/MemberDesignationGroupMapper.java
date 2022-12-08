package org.jeecg.modules.member.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.member.entity.MemberDesignationGroup;
import org.jeecg.modules.member.vo.MemberDesignationGroupVO;

import java.util.List;
import java.util.Map;

/**
 * @Description: 团队管理
 * @Author: jeecg-boot
 * @Date:   2021-01-07
 * @Version: V1.0
 */
public interface MemberDesignationGroupMapper extends BaseMapper<MemberDesignationGroup> {

    IPage<MemberDesignationGroupVO> queryPageList(Page<MemberDesignationGroupVO> page,@Param(Constants.WRAPPER) QueryWrapper<MemberDesignationGroupVO> queryWrapper);

    List<Map<String,Object>> findMemberDesignationGroupByName(@Param("name") String name);

    List<Map<String,Object>> getToDayPartner(@Param("id") String id,@Param("date") String date);

    List<Map<String,Object>> getNewPartner(@Param("id") String id);

    List<Map<String,Object>> getPerformanceSum(@Param("id") String id);

    List<Map<String,Object>> getToDayPerformance(@Param("id") String id);
}
