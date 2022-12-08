package org.jeecg.modules.alliance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
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
public interface IAllianceManageService extends IService<AllianceManage> {

    AllianceManageVO findAllianceManageInfo(String id);

    IPage<AllianceManageVO> queryPageList(Page<AllianceManage> page, AllianceManageDTO allianceManageDTO);

    AllianceWorkbenchVO findWorkbench(String userId);

    List<Map<String,Object>> findAllianceManageByphone(String phone);

    List<Map<String,Object>> getAllianceStoreManage(String id);

    /**
     * 全国合伙人总数
     * @return
     */
    Map<String,Object> partnerSum();

    /**
     * 今日合伙人
     * @return
     */
    Map<String,Object> toDayPartner();

    /**
     * 全国业绩总和
     * @return
     */
    Map<String,Object> getPerformanceSum();

    /**
     * 今日业绩
     * @return
     */
    Map<String,Object> getToDayPerformance();
}
