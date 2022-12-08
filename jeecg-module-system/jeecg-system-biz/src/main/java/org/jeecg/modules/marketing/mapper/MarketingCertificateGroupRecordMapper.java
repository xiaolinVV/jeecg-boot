package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
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
public interface MarketingCertificateGroupRecordMapper extends BaseMapper<MarketingCertificateGroupRecord> {

    IPage<MarketingCertificateGroupRecordVO> queryPageList(Page<MarketingCertificateGroupRecordVO> page,@Param(Constants.WRAPPER) QueryWrapper<MarketingCertificateGroupRecordVO> queryWrapper);

    List<Map<String,Object>> getMemberList();

    List<Map<String,Object>> getMemberListByMarketingCertificateGroupManageId(@Param("marketingCertificateGroupManageId") String marketingCertificateGroupManageId);

    List<MarketingCertificateGroupRecordVO> getMarketingCertificateGroupManageRecordList(@Param("id") String id);
}
