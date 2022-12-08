package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.java.Log;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.marketing.entity.MarketingFreeSession;
import org.jeecg.modules.marketing.mapper.MarketingFreeSessionMapper;
import org.jeecg.modules.marketing.service.IMarketingFreeSessionService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Description: 免单场次
 * @Author: jeecg-boot
 * @Date:   2021-02-04
 * @Version: V1.0
 */
@Service
@Log
public class MarketingFreeSessionServiceImpl extends ServiceImpl<MarketingFreeSessionMapper, MarketingFreeSession> implements IMarketingFreeSessionService {

    @Override
    public Map<String, Object> selectCurrentSession() {
        return baseMapper.selectCurrentSession();
    }

    @Override
    public void autoCreateOrStop() {
        //查询需要启用的场次
        this.list(new LambdaQueryWrapper<MarketingFreeSession>()
                .eq(MarketingFreeSession::getStatus,"0")
                .le(MarketingFreeSession::getStartTime, DateUtils.formatDate())
                .ge(MarketingFreeSession::getEndTime, DateUtils.formatDate())).forEach(mfs->{
                    log.info("场次自动启用id："+mfs.getId());
                    mfs.setStatus("1");
                    this.saveOrUpdate(mfs);
        });

        this.list(new LambdaQueryWrapper<MarketingFreeSession>()
                .eq(MarketingFreeSession::getStatus,"1")
                .lt(MarketingFreeSession::getEndTime,DateUtils.formatDate())).forEach(mfs->{
                    log.info("场次自动停用id："+mfs.getId());
                    mfs.setStatus("2");
                    this.saveOrUpdate(mfs);
        });

    }
}
