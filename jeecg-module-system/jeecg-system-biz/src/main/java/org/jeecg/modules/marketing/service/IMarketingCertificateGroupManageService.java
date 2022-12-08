package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.entity.MarketingCertificateGroupManage;
import org.jeecg.modules.marketing.vo.MarketingCertificateGroupManageVO;

import java.util.List;
import java.util.Map;

/**
 * @Description: 拼好券管理
 * @Author: jeecg-boot
 * @Date:   2021-03-29
 * @Version: V1.0
 */
public interface IMarketingCertificateGroupManageService extends IService<MarketingCertificateGroupManage> {

    List<Map<String,Object>> dataList();

    IPage<Map<String,Object>> myMarketingCertificateGroupManage(Page<Map<String, Object>> page,String memberId, String status);

    IPage<MarketingCertificateGroupManageVO> queryPageList(Page<MarketingCertificateGroupManageVO> page, QueryWrapper<MarketingCertificateGroupManage> queryWrapper);

    Map<String,Object> getInfo(String id);
}
