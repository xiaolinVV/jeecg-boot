package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.marketing.entity.MarketingZoneGroupGrouping;
import org.jeecg.modules.marketing.mapper.MarketingZoneGroupGroupingMapper;
import org.jeecg.modules.marketing.mapper.MarketingZoneGroupManageMapper;
import org.jeecg.modules.marketing.mapper.MarketingZoneGroupOrderMapper;
import org.jeecg.modules.marketing.service.IMarketingZoneGroupGroupingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

/**
 * @Description: 专区团分组
 * @Author: jeecg-boot
 * @Date:   2021-07-31
 * @Version: V1.0
 */
@Service
public class MarketingZoneGroupGroupingServiceImpl extends ServiceImpl<MarketingZoneGroupGroupingMapper, MarketingZoneGroupGrouping> implements IMarketingZoneGroupGroupingService {

    @Autowired(required = false)
    private MarketingZoneGroupOrderMapper marketingZoneGroupOrderMapper;
    @Autowired(required = false)
    private MarketingZoneGroupManageMapper marketingZoneGroupManageMapper;
    @Override
    public IPage<Map<String, Object>> findMarketingZoneGroupGroupingByMemberId(Page<Map<String, Object>> page, String memberId) {
        return ZoneGroupGroupingStepout(baseMapper.findMarketingZoneGroupGroupingByMemberId(page,memberId));
    }

    private IPage<Map<String,Object>> ZoneGroupGroupingStepout(IPage<Map<String,Object>> findMarketingZoneGroupGroupingByMemberId){
        findMarketingZoneGroupGroupingByMemberId.getRecords().forEach(fzg->{
            List<Map<String, Object>> marketingZoneGroupOrderList = marketingZoneGroupOrderMapper.listByGroupingId(fzg.get("id").toString());
            if (marketingZoneGroupOrderList.size()>0){
                fzg.put("goodsCount",marketingZoneGroupOrderList.size());
                fzg.put("goodsMainPicture",marketingZoneGroupOrderList.stream().map(mz -> mz.get("mainPicture")).collect(toList()));
                fzg.put("startTime",marketingZoneGroupOrderList.get(0).get("tuxedoTime"));
                int threshold = Integer.valueOf(fzg.get("transformationThreshold").toString()) - 1;
                fzg.put("endTime",marketingZoneGroupOrderList.size()<threshold?"-":marketingZoneGroupOrderList.get(marketingZoneGroupOrderList.size()-1).get("tuxedoTime"));
                fzg.put("consignmentCount",marketingZoneGroupOrderList.stream().filter(mz->mz.get("status").equals("3")).count());
                fzg.put("consignmentEndCount",marketingZoneGroupOrderList.stream().filter(mz->mz.get("status").equals("4")||mz.get("status").equals("2")).count());
            }else {
                fzg.put("goodsCount",marketingZoneGroupOrderList.size());
                fzg.put("goodsMainPicture","");
                fzg.put("startTime","-");
                fzg.put("endTime","-");
                fzg.put("consignmentCount",0);
                fzg.put("consignmentEndCount",0);
            }
        });
        return findMarketingZoneGroupGroupingByMemberId;
    }
}
