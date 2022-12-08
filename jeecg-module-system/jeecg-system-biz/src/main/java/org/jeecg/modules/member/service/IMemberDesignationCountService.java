package org.jeecg.modules.member.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.modules.member.dto.MemberDesignationCountDTO;
import org.jeecg.modules.member.entity.MemberDesignationCount;
import org.jeecg.modules.member.vo.MemberDesignationCountVO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 会员称号统计
 * @Author: jeecg-boot
 * @Date:   2020-07-11
 * @Version: V1.0
 */
public interface IMemberDesignationCountService extends IService<MemberDesignationCount> {

    List<MemberDesignationCountVO> findListByMemberId(String id, String memberDesignationGroupId);

    List<MemberDesignationCountVO> getListByMap(HashMap<String,Object> map);

    IPage<MemberDesignationCountVO> findMemberDesignationCountPageListByMemberId(Page<MemberDesignationCountVO> page, MemberDesignationCountDTO memberDesignationCountDTO);

    List<Map<String,Object>> findMemberdesignationCountListById(String id, String memberDesignationGroupId);
}
