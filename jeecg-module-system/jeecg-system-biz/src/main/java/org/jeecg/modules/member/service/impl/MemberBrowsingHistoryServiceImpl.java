package org.jeecg.modules.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.modules.good.entity.GoodList;
import org.jeecg.modules.good.entity.GoodStoreList;
import org.jeecg.modules.good.service.IGoodListService;
import org.jeecg.modules.good.service.IGoodStoreListService;
import org.jeecg.modules.marketing.entity.MarketingPrefecture;
import org.jeecg.modules.marketing.service.IMarketingPrefectureService;
import org.jeecg.modules.member.dto.MemberBrowsingHistoryDto;
import org.jeecg.modules.member.entity.MemberBrowsingHistory;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.mapper.MemberBrowsingHistoryMapper;
import org.jeecg.modules.member.service.IMemberBrowsingHistoryService;
import org.jeecg.modules.member.service.IMemberListService;
import org.jeecg.modules.member.vo.MemberBrowsingHistoryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: 浏览记录
 * @Author: jeecg-boot
 * @Date:   2019-10-29
 * @Version: V1.0
 */
@Service
public class MemberBrowsingHistoryServiceImpl extends ServiceImpl<MemberBrowsingHistoryMapper, MemberBrowsingHistory> implements IMemberBrowsingHistoryService {

    @Autowired
    private IGoodStoreListService iGoodStoreListService;

    @Autowired
    private IGoodListService iGoodListService;

    @Autowired
    private IMemberListService iMemberListService;
    @Autowired
    @Lazy
    private IMarketingPrefectureService iMarketingPrefectureService;
    @Override
    public IPage<MemberBrowsingHistoryDto> getMemberGoodsCollectionVo(Page<MemberBrowsingHistoryDto> page, MemberBrowsingHistoryVo memberBrowsingHistoryVo ){
        IPage<MemberBrowsingHistoryDto> page1 = baseMapper.getMemberGoodsCollectionVo(page,memberBrowsingHistoryVo);
     try {
         if(page1.getSize()>0){
             List<MemberBrowsingHistoryDto> list=  page1.getRecords();
             if(list.size()>0){
                 String[]  strs;
                 DecimalFormat df = new DecimalFormat("0.00");
                 for(MemberBrowsingHistoryDto memberBrowsingHistoryDto:list){
                     if(memberBrowsingHistoryDto.getPrice()==null  ||"".equals(memberBrowsingHistoryDto.getPrice())){
                     }else{
                         strs =memberBrowsingHistoryDto.getPrice().split("-");
                         memberBrowsingHistoryDto.setPrice(strs[0]);
                         if(memberBrowsingHistoryDto.getBrowsingPrice()!=null){
                             Double browsingPrice = Double.valueOf(memberBrowsingHistoryDto.getBrowsingPrice());
                             Double price=Double.valueOf(memberBrowsingHistoryDto.getPrice());
                             Double ss=browsingPrice-price;
                             if(ss>0){
                                 memberBrowsingHistoryDto.setDepreciate(df.format(ss) );
                             }else {
                                 memberBrowsingHistoryDto.setDepreciate("--");
                             }
                         }
                     }
                 }
             }
         }
     }catch (Exception e){
         e.printStackTrace();
     }
        return page1;
    }


    @Override
    @Transactional
    public Boolean addGoodToBrowsingHistory(Integer isPlatform, String goodId, String memberId,String marketingPrefectureId,String marketingFreeGoodListId) {

        MemberBrowsingHistory memberBrowsingHistory=null;
        MemberList memberList=iMemberListService.getById(memberId);
        if(isPlatform.intValue()==0){
            //店铺商品
            //查询是否有商品
            QueryWrapper<MemberBrowsingHistory> memberBrowsingHistoryQueryWrapper=new QueryWrapper<>();
            memberBrowsingHistoryQueryWrapper.eq("member_list_id",memberId);
            memberBrowsingHistoryQueryWrapper.eq("good_store_list_id",goodId);
            memberBrowsingHistoryQueryWrapper.eq("good_type","0");
            memberBrowsingHistory=this.getOne(memberBrowsingHistoryQueryWrapper);
            GoodStoreList goodStoreList=iGoodStoreListService.getById(goodId);
            //根据情况添加数据
            if(memberBrowsingHistory!=null){
                memberBrowsingHistory.setQuantity(memberBrowsingHistory.getQuantity().add(new BigDecimal(1)));
                if(memberList.getMemberType().equals("0")) {
                    memberBrowsingHistory.setBrowsingPrice(goodStoreList.getSmallPrice());
                }else{
                    memberBrowsingHistory.setBrowsingPrice(goodStoreList.getSmallVipPrice());
                }
            }else{
                memberBrowsingHistory=new MemberBrowsingHistory();
                memberBrowsingHistory.setDelFlag("0");
                memberBrowsingHistory.setMemberListId(memberId);
                memberBrowsingHistory.setGoodType("0");
                memberBrowsingHistory.setGoodStoreListId(goodId);
                memberBrowsingHistory.setBrowsingTime(new Date());
                if(memberList.getMemberType().equals("0")) {
                    memberBrowsingHistory.setBrowsingPrice(goodStoreList.getSmallPrice());
                }else{
                    memberBrowsingHistory.setBrowsingPrice(goodStoreList.getSmallVipPrice());
                }
                memberBrowsingHistory.setQuantity(new BigDecimal(1));
            }
        }else
        if(isPlatform.intValue()==1){
            //平台商品
            //查询是否有商品
            QueryWrapper<MemberBrowsingHistory> memberBrowsingHistoryQueryWrapper=new QueryWrapper<>();
            memberBrowsingHistoryQueryWrapper.eq("member_list_id",memberId);
            memberBrowsingHistoryQueryWrapper.eq("good_list_id",goodId);
            memberBrowsingHistoryQueryWrapper.eq("good_type","1");
            if(StringUtils.isNotBlank(marketingPrefectureId)){
                memberBrowsingHistoryQueryWrapper.eq("marketing_prefecture_id",marketingPrefectureId);
            }else{
                memberBrowsingHistoryQueryWrapper.isNull("marketing_prefecture_id");
            }

            memberBrowsingHistory=this.getOne(memberBrowsingHistoryQueryWrapper);
            GoodList goodList=iGoodListService.getById(goodId);
            //根据情况添加数据
            if(memberBrowsingHistory!=null){
                memberBrowsingHistory.setQuantity(memberBrowsingHistory.getQuantity().add(new BigDecimal(1)));
                if(memberList.getMemberType().equals("0")||goodList.getGoodForm().equals("1")) {
                    memberBrowsingHistory.setBrowsingPrice(goodList.getSmallPrice());
                }else{
                    memberBrowsingHistory.setBrowsingPrice(goodList.getSmallVipPrice());
                }

                //免单商品
                if(StringUtils.isNotBlank(marketingFreeGoodListId)){
                    memberBrowsingHistory.setMarketingFreeGoodListId(marketingFreeGoodListId);
                    memberBrowsingHistory.setPrefectureLabel("免单活动");
                }

                //保存专区数据
                MarketingPrefecture marketingPrefecture = iMarketingPrefectureService.getById(marketingPrefectureId);
                if(marketingPrefecture!=null){
                    memberBrowsingHistory.setMarketingPrefectureId(marketingPrefectureId);
                    //专区标签
                    memberBrowsingHistory.setPrefectureLabel(marketingPrefecture.getPrefectureLabel());
                }
            }else{
                memberBrowsingHistory=new MemberBrowsingHistory();
                memberBrowsingHistory.setDelFlag("0");
                memberBrowsingHistory.setMemberListId(memberId);
                memberBrowsingHistory.setGoodType("1");
                memberBrowsingHistory.setGoodListId(goodId);
                memberBrowsingHistory.setBrowsingTime(new Date());
                if(memberList.getMemberType().equals("0")||goodList.getGoodForm().equals("1")) {
                    memberBrowsingHistory.setBrowsingPrice(goodList.getSmallPrice());
                }else{
                    memberBrowsingHistory.setBrowsingPrice(goodList.getSmallVipPrice());
                }
                memberBrowsingHistory.setQuantity(new BigDecimal(1));
                //免单商品
                if(StringUtils.isNotBlank(marketingFreeGoodListId)){
                    memberBrowsingHistory.setMarketingFreeGoodListId(marketingFreeGoodListId);
                    memberBrowsingHistory.setPrefectureLabel("免单活动");
                }
                //保存专区数据
                MarketingPrefecture marketingPrefecture = iMarketingPrefectureService.getById(marketingPrefectureId);
                if(marketingPrefecture!=null){
                    memberBrowsingHistory.setMarketingPrefectureId(marketingPrefectureId);
                    //专区标签
                    memberBrowsingHistory.setPrefectureLabel(marketingPrefecture.getPrefectureLabel());
                }
            }
        }else{
            return false;
        }

        return this.saveOrUpdate(memberBrowsingHistory);
    }

    @Override
    public IPage<Map<String, Object>> findBrowsingHistoryBySort(Page<Map<String, Object>> page, String memberId) {
        return baseMapper.findBrowsingHistoryBySort(page,memberId);
    }
}
