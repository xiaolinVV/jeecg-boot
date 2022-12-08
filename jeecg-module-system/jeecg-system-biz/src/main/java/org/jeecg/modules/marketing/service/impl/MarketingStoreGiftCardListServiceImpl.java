package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.util.OrderNoUtils;
import org.jeecg.modules.marketing.dto.MarketingStoreGiftCardListDTO;
import org.jeecg.modules.marketing.entity.MarketingStoreGiftCardList;
import org.jeecg.modules.marketing.entity.MarketingStoreGiftCardMemberList;
import org.jeecg.modules.marketing.mapper.MarketingStoreGiftCardListMapper;
import org.jeecg.modules.marketing.service.IMarketingStoreGiftCardListService;
import org.jeecg.modules.marketing.service.IMarketingStoreGiftCardMemberListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: 店铺礼品卡列表
 * @Author: jeecg-boot
 * @Date:   2021-07-13
 * @Version: V1.0
 */
@Service
public class MarketingStoreGiftCardListServiceImpl extends ServiceImpl<MarketingStoreGiftCardListMapper, MarketingStoreGiftCardList> implements IMarketingStoreGiftCardListService {

    @Autowired
    private IMarketingStoreGiftCardMemberListService iMarketingStoreGiftCardMemberListService;


    @Override
    public IPage<Map<String, Object>> selectStoreGoods(Page<Map<String, Object>> page, Map<String, Object> paramMap) {
        return baseMapper.selectStoreGoods(page,paramMap);
    }

    @Override
    public IPage<Map<String,Object>> getSelectGoods(Page<Map<String,Object>> page,String marketingStoreGiftCardListId) {
        return baseMapper.getSelectGoods(page,marketingStoreGiftCardListId);
    }

    @Override
    public List<Map<String, Object>> getSelectGoods(String marketingStoreGiftCardListId) {
        return baseMapper.getSelectGoods(marketingStoreGiftCardListId);
    }

    @Override
    public IPage<Map<String, Object>> queryPageList(Page<Map<String, Object>> page, MarketingStoreGiftCardListDTO marketingStoreGiftCardListDTO) {
        return baseMapper.queryPageList(page,marketingStoreGiftCardListDTO);
    }

    @Override
    public List<Map<String, Object>> getGiftCarList(String marketingGiftBagId) {
        return baseMapper.getGiftCarList(marketingGiftBagId);
    }

    @Override
    public void generate(String marketingStoreGiftCardListId, String memberId,String waysObtain) {
        MarketingStoreGiftCardList marketingStoreGiftCardList=this.getById(marketingStoreGiftCardListId);
        if(marketingStoreGiftCardList==null){
            return;
        }
        MarketingStoreGiftCardMemberList marketingStoreGiftCardMemberList=new MarketingStoreGiftCardMemberList();
        marketingStoreGiftCardMemberList.setMemberListId(memberId);
        marketingStoreGiftCardMemberList.setMarketingStoreGiftCardListId(marketingStoreGiftCardListId);
        marketingStoreGiftCardMemberList.setSerialNumber(OrderNoUtils.getOrderNo());
        marketingStoreGiftCardMemberList.setGetTime(new Date());
        marketingStoreGiftCardMemberList.setStoreManageId(marketingStoreGiftCardList.getStoreManageId());
        marketingStoreGiftCardMemberList.setCarName(marketingStoreGiftCardList.getCarName());
        marketingStoreGiftCardMemberList.setDenomination(marketingStoreGiftCardList.getDenomination());
        marketingStoreGiftCardMemberList.setWaysObtain(waysObtain);
        Calendar calendar=Calendar.getInstance();
        if(marketingStoreGiftCardList.getTimeWay().equals("0")){
            marketingStoreGiftCardMemberList.setStartTime(marketingStoreGiftCardList.getStartTime());
            marketingStoreGiftCardMemberList.setEndTime(marketingStoreGiftCardList.getEndTime());
        }
        if(marketingStoreGiftCardList.getTimeWay().equals("1")){
            marketingStoreGiftCardMemberList.setStartTime(calendar.getTime());
            calendar.add(Calendar.DAY_OF_YEAR,marketingStoreGiftCardList.getTimeDigital().intValue());
            marketingStoreGiftCardMemberList.setEndTime(calendar.getTime());
        }
        if(marketingStoreGiftCardList.getTimeWay().equals("2")){
            calendar.add(Calendar.DAY_OF_YEAR,1);
            marketingStoreGiftCardMemberList.setStartTime(calendar.getTime());
            calendar.add(Calendar.DAY_OF_YEAR,marketingStoreGiftCardList.getTimeDigital().intValue());
            marketingStoreGiftCardMemberList.setEndTime(calendar.getTime());
        }
        if(new Date().getTime()>=marketingStoreGiftCardMemberList.getStartTime().getTime()&&new Date().getTime()<=marketingStoreGiftCardMemberList.getEndTime().getTime()){
            marketingStoreGiftCardMemberList.setStatus("1");
        }
        iMarketingStoreGiftCardMemberListService.save(marketingStoreGiftCardMemberList);
    }
}
