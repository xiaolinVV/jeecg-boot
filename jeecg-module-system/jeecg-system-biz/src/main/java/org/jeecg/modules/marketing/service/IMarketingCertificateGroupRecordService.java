package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.entity.MarketingCertificateGroupRecord;
import org.jeecg.modules.marketing.vo.MarketingCertificateGroupRecordVO;

import java.util.List;
import java.util.Map;

/**
 * @Description: 拼好券记录
 * @Author: jeecg-boot
 * @Date:   2021-03-29
 * @Version: V1.0
 */
public interface IMarketingCertificateGroupRecordService extends IService<MarketingCertificateGroupRecord> {

    IPage<MarketingCertificateGroupRecordVO> queryPageList(Page<MarketingCertificateGroupRecordVO> page, QueryWrapper<MarketingCertificateGroupRecordVO> queryWrapper);

    List<Map<String,Object>> getMemberList();

    List<Map<String,Object>> getMemberListByMarketingCertificateGroupManageId(String marketingCertificateGroupManageId);

    List<MarketingCertificateGroupRecordVO> getMarketingCertificateGroupManageRecordList(String id);
}
