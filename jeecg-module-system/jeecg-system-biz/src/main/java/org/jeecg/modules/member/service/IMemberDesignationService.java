package org.jeecg.modules.member.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.member.dto.MemberDesignationDTO;
import org.jeecg.modules.member.entity.MemberDesignation;
import org.jeecg.modules.member.vo.MemberDesignationVO;

import java.util.List;
import java.util.Map;

/**
 * @Description: 称号管理
 * @Author: jeecg-boot
 * @Date:   2020-06-16
 * @Version: V1.0
 */
public interface IMemberDesignationService extends IService<MemberDesignation> {

    IPage<MemberDesignationVO> queryPageList(Page<MemberDesignation> page, MemberDesignationDTO memberDesignationDTO);

    List<Map<String, Object>> memberDesignationNameList(String id);

    List<Map<String, Object>> getMemberDesignationListBySort(String sort,String memberDesignationGroupId);

    MemberDesignationVO getMemberDesignationById(String memberDesignationId);
}
