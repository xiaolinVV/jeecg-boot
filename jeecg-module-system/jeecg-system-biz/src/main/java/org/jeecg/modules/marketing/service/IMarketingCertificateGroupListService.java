package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.entity.MarketingCertificateGroupList;
import org.jeecg.modules.marketing.vo.MarketingCertificateGroupListVO;

import java.util.List;
import java.util.Map;

/**
 * @Description: 拼好券
 * @Author: jeecg-boot
 * @Date:   2021-03-29
 * @Version: V1.0
 */
public interface IMarketingCertificateGroupListService extends IService<MarketingCertificateGroupList> {

    IPage<MarketingCertificateGroupListVO> queryPageList(Page<MarketingCertificateGroupListVO> page, QueryWrapper<MarketingCertificateGroupListVO> queryWrapper);

    List<Map<String,Object>> pageListBySout(Integer sout);

    IPage<Map<String,Object>> pageListByMarketingCertificateGroupList(Page<Map<String,Object>> page);
}
