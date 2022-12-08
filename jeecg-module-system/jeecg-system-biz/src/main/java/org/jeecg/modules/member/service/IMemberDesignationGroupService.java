package org.jeecg.modules.member.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
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
public interface IMemberDesignationGroupService extends IService<MemberDesignationGroup> {

    IPage<MemberDesignationGroupVO> queryPageList(Page<MemberDesignationGroupVO> page, QueryWrapper<MemberDesignationGroupVO> queryWrapper);

    List<Map<String,Object>> findMemberDesignationGroupByName(String name);

    List<Map<String,Object>> getToDayPartner(String id, String date);

    List<Map<String,Object>> getNewPartner(String id);

    List<Map<String,Object>> getPerformanceSum(String id);

    List<Map<String,Object>> getToDayPerformance(String id);
}
