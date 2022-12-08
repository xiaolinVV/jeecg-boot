package org.jeecg.modules.member.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
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
public interface MemberDesignationMapper extends BaseMapper<MemberDesignation> {

    IPage<MemberDesignationVO> queryPageList(Page<MemberDesignation> page,@Param("memberDesignationDTO") MemberDesignationDTO memberDesignationDTO);

    List<Map<String,Object>> memberDesignationNameList(@Param("id") String id);

    List<Map<String,Object>> getMemberDesignationListBySort(@Param("sort") String sort,@Param("memberDesignationGroupId") String memberDesignationGroupId);

    MemberDesignationVO getMemberDesignationById(@Param("memberDesignationId") String memberDesignationId);
}
