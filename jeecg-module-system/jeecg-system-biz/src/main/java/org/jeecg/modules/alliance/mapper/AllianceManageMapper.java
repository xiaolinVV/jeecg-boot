package org.jeecg.modules.alliance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.alliance.dto.AllianceManageDTO;
import org.jeecg.modules.alliance.entity.AllianceManage;
import org.jeecg.modules.alliance.vo.AllianceManageVO;
import org.jeecg.modules.alliance.vo.AllianceWorkbenchVO;

import java.util.List;
import java.util.Map;

/**
 * @Description: 加盟商管理
 * @Author: jeecg-boot
 * @Date:   2020-05-17
 * @Version: V1.0
 */
public interface AllianceManageMapper extends BaseMapper<AllianceManage> {

    AllianceManageVO findAllianceManageInfo(@Param("id") String id);

    IPage<AllianceManageVO> queryPageList(Page<AllianceManage> page,@Param("allianceManageDTO") AllianceManageDTO allianceManageDTO);

    AllianceWorkbenchVO findWorkbench(@Param("userId") String userId);

    List<Map<String,Object>> findAllianceManageByphone(@Param("phone") String phone);

    List<Map<String,Object>> getAllianceStoreManage(@Param("id") String id);
}
