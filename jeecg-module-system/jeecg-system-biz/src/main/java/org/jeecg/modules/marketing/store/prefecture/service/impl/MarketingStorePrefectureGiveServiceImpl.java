package org.jeecg.modules.marketing.store.prefecture.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.marketing.store.prefecture.entity.MarketingStorePrefectureGive;
import org.jeecg.modules.marketing.store.prefecture.entity.MarketingStorePrefectureGood;
import org.jeecg.modules.marketing.store.prefecture.entity.MarketingStorePrefectureList;
import org.jeecg.modules.marketing.store.prefecture.mapper.MarketingStorePrefectureGiveMapper;
import org.jeecg.modules.marketing.store.prefecture.service.IMarketingStorePrefectureGiveService;
import org.jeecg.modules.marketing.store.prefecture.service.IMarketingStorePrefectureGoodService;
import org.jeecg.modules.marketing.store.prefecture.service.IMarketingStorePrefectureListService;
import org.jeecg.modules.store.entity.StoreFranchiser;
import org.jeecg.modules.store.service.IStoreFranchiserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * @Description: 店铺专区限制记录
 * @Author: jeecg-boot
 * @Date:   2022-12-16
 * @Version: V1.0
 */
@Service
public class MarketingStorePrefectureGiveServiceImpl extends ServiceImpl<MarketingStorePrefectureGiveMapper, MarketingStorePrefectureGive> implements IMarketingStorePrefectureGiveService {

    @Autowired
    @Lazy
    private IMarketingStorePrefectureGoodService iMarketingStorePrefectureGoodService;

    @Autowired
    private IMarketingStorePrefectureListService iMarketingStorePrefectureListService;


    @Autowired
    private IStoreFranchiserService iStoreFranchiserService;


    @Override
    public boolean ifBuy(String memberId, String marketingStorePrefectureGoodId) {
        MarketingStorePrefectureGood marketingStorePrefectureGood=iMarketingStorePrefectureGoodService.getById(marketingStorePrefectureGoodId);
        MarketingStorePrefectureList marketingStorePrefectureList=iMarketingStorePrefectureListService.getById(marketingStorePrefectureGood.getMarketingStorePrefectureListId());
        /*是限购专区*/
        if(marketingStorePrefectureList.getPrefectureType().equals("1")){

            if(iStoreFranchiserService.count(new LambdaQueryWrapper<StoreFranchiser>()
                    .eq(StoreFranchiser::getStoreManageId,marketingStorePrefectureList.getStoreManageId())
                    .eq(StoreFranchiser::getMemberListId,memberId))>0){
                return true;
            }

            MarketingStorePrefectureGive marketingStorePrefectureGive=this.getOne(new LambdaQueryWrapper<MarketingStorePrefectureGive>()
                    .eq(MarketingStorePrefectureGive::getMarketingStorePrefectureListId,marketingStorePrefectureList.getId())
                    .eq(MarketingStorePrefectureGive::getMemberListId,memberId)
                    .orderByDesc(MarketingStorePrefectureGive::getCreateTime)
                    .last("limit 1"),false);
            if(marketingStorePrefectureGive==null){
                return false;
            }
            if(marketingStorePrefectureGive.getTradetime()==null){
                return true;
            }
            Date expirationDate = null;
            if(marketingStorePrefectureGive.getModel().equals("0")){
                expirationDate = DateUtil.offsetDay(marketingStorePrefectureGive.getTradetime(), Convert.toInt(marketingStorePrefectureGive.getTime()));
            }
            if(marketingStorePrefectureGive.getModel().equals("1")){
                expirationDate = DateUtil.offsetMonth(marketingStorePrefectureGive.getTradetime(), Convert.toInt(marketingStorePrefectureGive.getTime()));
            }
            if(DateUtil.compare(DateUtil.date(),expirationDate) >= 0){
                return true;
            }else{
                return false;
            }
        }
        return true;
    }

    @Override
    public IPage<Map<String, Object>> getMarketingStorePrefectureGiveList(Page<Map<String, Object>> page, String memberId) {
        return baseMapper.getMarketingStorePrefectureGiveList(page,memberId);
    }
}
