package org.jeecg.modules.member.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.member.dto.MemberGradeDTO;
import org.jeecg.modules.member.entity.MemberGrade;
import org.jeecg.modules.member.vo.MemberGradeVO;

import java.util.List;
import java.util.Map;

/**
 * @Description: 会员等级
 * @Author: jeecg-boot
 * @Date:   2020-06-11
 * @Version: V1.0
 */
public interface MemberGradeMapper extends BaseMapper<MemberGrade> {

    IPage<MemberGradeVO> queryPageList(Page<MemberGrade> page,@Param("memberGradeDTO") MemberGradeDTO memberGradeDTO);

    List<Map<String,Object>> findMemberGradeListMap();

    List<Map<String,String>> getReferMemberGradeList();

}
