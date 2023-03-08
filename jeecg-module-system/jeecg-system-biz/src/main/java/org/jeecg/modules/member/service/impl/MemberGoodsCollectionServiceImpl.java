package org.jeecg.modules.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.modules.good.entity.GoodSpecification;
import org.jeecg.modules.good.entity.GoodStoreSpecification;
import org.jeecg.modules.good.service.IGoodSpecificationService;
import org.jeecg.modules.good.service.IGoodStoreSpecificationService;
import org.jeecg.modules.marketing.entity.MarketingPrefecture;
import org.jeecg.modules.marketing.service.IMarketingPrefectureService;
import org.jeecg.modules.member.dto.MemberGoodsCollectionDto;
import org.jeecg.modules.member.entity.MemberGoodsCollection;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.mapper.MemberGoodsCollectionMapper;
import org.jeecg.modules.member.service.IMemberGoodsCollectionService;
import org.jeecg.modules.member.service.IMemberListService;
import org.jeecg.modules.member.vo.MemberGoodsCollectionVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: 商品收藏
 * @Author: jeecg-boot
 * @Date:   2019-10-29
 * @Version: V1.0
 */
@Slf4j
@Service
public class MemberGoodsCollectionServiceImpl extends ServiceImpl<MemberGoodsCollectionMapper, MemberGoodsCollection> implements IMemberGoodsCollectionService {

    @Autowired
    private IMemberListService iMemberListService;
    @Autowired
    private IMarketingPrefectureService iMarketingPrefectureService;

    @Autowired
    private IGoodSpecificationService iGoodSpecificationService;

    @Autowired
    private IGoodStoreSpecificationService iGoodStoreSpecificationService;

    @Override
    public IPage<MemberGoodsCollectionDto> getMemberGoodsCollectionVo(Page<MemberGoodsCollectionDto> page, MemberGoodsCollectionVo memberGoodsCollectionVo){
        IPage<MemberGoodsCollectionDto> page1 = baseMapper.getMemberGoodsCollectionVo(page, memberGoodsCollectionVo);
        try {
            if(page1.getSize()>0){
                List<MemberGoodsCollectionDto> list=  page1.getRecords();
                if(list.size()>0){
                    String[]  strs;
                    DecimalFormat df = new DecimalFormat("0.00");
                    for(MemberGoodsCollectionDto memberGoodsCollectionDto:list){
                        if(memberGoodsCollectionDto.getPrice()==null  ||"".equals(memberGoodsCollectionDto.getPrice())){
                        }else{
                            strs =memberGoodsCollectionDto.getPrice().split("-");
                            memberGoodsCollectionDto.setPrice(strs[0]);
                            if(memberGoodsCollectionDto.getCollectPrice()!=null){
                                Double browsingPrice = Double.valueOf(memberGoodsCollectionDto.getCollectPrice());
                                Double price=Double.valueOf(memberGoodsCollectionDto.getPrice());
                                Double ss=browsingPrice-price;
                                if(ss>0){
                                    memberGoodsCollectionDto.setDepreciate(df.format(ss) );
                                }else {
                                    memberGoodsCollectionDto.setDepreciate("--");
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
    public Boolean addMemberGoodsCollectionByGoodId(String goodId, Integer isPlatform, String memberId,String marketingPrefectureId) {


        MemberGoodsCollection memberGoodsCollection=null;
        MemberList memberList=iMemberListService.getById(memberId);
        //店铺商品收藏
        if(isPlatform.intValue()==0){

            QueryWrapper<MemberGoodsCollection> memberGoodsCollectionQueryWrapper=new QueryWrapper<>();
            memberGoodsCollectionQueryWrapper.eq("member_list_id",memberId);
            memberGoodsCollectionQueryWrapper.eq("good_type","0");
            memberGoodsCollectionQueryWrapper.eq("good_store_list_id",goodId);
            memberGoodsCollection=this.getOne(memberGoodsCollectionQueryWrapper);
            GoodStoreSpecification goodStoreSpecificationSmall=iGoodStoreSpecificationService.getSmallGoodSpecification(goodId);
            if(memberGoodsCollection!=null){

                //商品存在
                memberGoodsCollection.setCollectionTime(new Date());
                if(memberList.getMemberType().equals("0")) {
                    memberGoodsCollection.setCollectPrice(goodStoreSpecificationSmall.getPrice().toString());
                }else{
                    memberGoodsCollection.setCollectPrice(goodStoreSpecificationSmall.getVipPrice().toString());
                }
            }else{
                memberGoodsCollection=new MemberGoodsCollection();
                memberGoodsCollection.setCollectionTime(new Date());
                if(memberList.getMemberType().equals("0")) {
                    memberGoodsCollection.setCollectPrice(goodStoreSpecificationSmall.getPrice().toString());
                }else{
                    memberGoodsCollection.setCollectPrice(goodStoreSpecificationSmall.getVipPrice().toString());
                }
                memberGoodsCollection.setDelFlag("0");
                memberGoodsCollection.setMemberListId(memberId);
                memberGoodsCollection.setGoodType("0");
                memberGoodsCollection.setGoodStoreListId(goodId);
            }

            this.saveOrUpdate(memberGoodsCollection);

        }else
            //平台商品收藏
            if(isPlatform.intValue()==1){

                QueryWrapper<MemberGoodsCollection> memberGoodsCollectionQueryWrapper=new QueryWrapper<>();
                memberGoodsCollectionQueryWrapper.eq("member_list_id",memberId);
                memberGoodsCollectionQueryWrapper.eq("good_type","1");
                memberGoodsCollectionQueryWrapper.eq("good_list_id",goodId);
                if(StringUtils.isNotBlank(marketingPrefectureId)){
                    memberGoodsCollectionQueryWrapper.eq("marketing_prefecture_id",marketingPrefectureId);
                }else{
                    memberGoodsCollectionQueryWrapper.isNull("marketing_prefecture_id");
                }
                memberGoodsCollection=this.getOne(memberGoodsCollectionQueryWrapper);
                GoodSpecification goodSpecificationSmall=iGoodSpecificationService.getSmallGoodSpecification(goodId);
                if(memberGoodsCollection!=null){

                    //商品存在
                    memberGoodsCollection.setCollectionTime(new Date());
                    
                    if(memberList.getMemberType().equals("0")) {
                        memberGoodsCollection.setCollectPrice(goodSpecificationSmall.getPrice().toString());
                    }else{
                        memberGoodsCollection.setCollectPrice(goodSpecificationSmall.getVipPrice().toString());
                    }
                    //保存专区数据
                    MarketingPrefecture marketingPrefecture = iMarketingPrefectureService.getById(marketingPrefectureId);
                    if(marketingPrefecture!=null){
                        memberGoodsCollection.setMarketingPrefectureId(marketingPrefectureId);
                        //专区标签
                        memberGoodsCollection.setPrefectureLabel(marketingPrefecture.getPrefectureLabel());
                    }


                }else{
                    memberGoodsCollection=new MemberGoodsCollection();
                    memberGoodsCollection.setCollectionTime(new Date());
                    if(memberList.getMemberType().equals("0")) {
                        memberGoodsCollection.setCollectPrice(goodSpecificationSmall.getPrice().toString());
                    }else{
                        memberGoodsCollection.setCollectPrice(goodSpecificationSmall.getVipPrice().toString());
                    }
                    memberGoodsCollection.setDelFlag("0");
                    memberGoodsCollection.setMemberListId(memberId);
                    memberGoodsCollection.setGoodType("1");
                    memberGoodsCollection.setGoodListId(goodId);
                    //保存专区数据
                    MarketingPrefecture marketingPrefecture = iMarketingPrefectureService.getById(marketingPrefectureId);
                    if(marketingPrefecture!=null){
                        memberGoodsCollection.setMarketingPrefectureId(marketingPrefectureId);
                        //专区标签
                        memberGoodsCollection.setPrefectureLabel(marketingPrefecture.getPrefectureLabel());
                    }
                }

                this.saveOrUpdate(memberGoodsCollection);
            }else{
                return false;
            }
        return true;
    }

    @Override
    public IPage<Map<String, Object>> findMemberGoodsCollections(Page<Map<String, Object>> page, Map<String, Object> paramMap) {
        return baseMapper.findMemberGoodsCollections(page,paramMap);
    }

}
