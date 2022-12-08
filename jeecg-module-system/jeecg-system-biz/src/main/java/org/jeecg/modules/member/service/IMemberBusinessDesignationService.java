package org.jeecg.modules.member.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.member.entity.MemberBusinessDesignation;

import java.util.Map;

/**
 * @Description: 创业团队成员
 * @Author: jeecg-boot
 * @Date:   2021-07-30
 * @Version: V1.0
 */
public interface IMemberBusinessDesignationService extends IService<MemberBusinessDesignation> {

    IPage<Map<String,Object>> findMemberBusinessDesignationBySuperiorMemberId(Page<Map<String,Object>> page, String memberId);

    IPage<Map<String,Object>> queryPageList(Page<Map<String,Object>> page, Map<String, Object> requestMap);
}
