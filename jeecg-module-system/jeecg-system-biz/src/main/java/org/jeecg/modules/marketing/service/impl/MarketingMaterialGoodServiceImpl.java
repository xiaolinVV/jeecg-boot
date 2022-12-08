package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.jeecg.modules.good.service.IGoodListService;
import org.jeecg.modules.good.vo.GoodListVo;
import org.jeecg.modules.marketing.dto.MarketingMaterialGoodDTO;
import org.jeecg.modules.marketing.entity.MarketingMaterialGood;
import org.jeecg.modules.marketing.mapper.MarketingMaterialGoodMapper;
import org.jeecg.modules.marketing.service.IMarketingMaterialGoodService;
import org.jeecg.modules.marketing.vo.MarketingMaterialListVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Description: 素材商品关系表
 * @Author: jeecg-boot
 * @Date:   2020-05-20
 * @Version: V1.0
 */
@Service
public class MarketingMaterialGoodServiceImpl extends ServiceImpl<MarketingMaterialGoodMapper, MarketingMaterialGood> implements IMarketingMaterialGoodService {

    @Autowired
    private IGoodListService goodListService;
    /**
     * 根据素材id查找商品
     * @param marketingMaterialListId
     * @return
     */
  public  List<MarketingMaterialGoodDTO> getMarketingMaterialGoodDTO(String marketingMaterialListId){
    return  baseMapper.getMarketingMaterialGoodDTO(marketingMaterialListId);
  };

    /**
     * 添加素材商品
     * @param goodListVo
     * @return
     */
    @Override
    public List<Map<String,Object>> postGoodListList(GoodListVo goodListVo){
        List<String> strList = Arrays.asList(goodListVo.getIds().split(","));
        //添加商品id查询
        goodListVo.setMarketingPrefectureGoodIds(strList);
        if(StringUtils.isBlank(goodListVo.getMarketingMaterialListId())){
            QueryWrapper<MarketingMaterialGood> queryWrapperMarketingMaterialGood = new QueryWrapper<>();
            queryWrapperMarketingMaterialGood.select("good_list_id").eq("marketing_material_list_id",goodListVo.getMarketingMaterialListId());
            //查询已经添加过的商品(需要过滤)
            List<Map<String,Object>> mapListGoodListId =  baseMapper.selectMaps(queryWrapperMarketingMaterialGood);
            List<String> alreadyExistIds = new ArrayList<>();
            //已存在的商品id
            mapListGoodListId.forEach(mpg->{
                alreadyExistIds.add(mpg.get("good_list_id").toString());
            });

            if(alreadyExistIds.size()>0){
                //过滤已存在专区商品
                goodListVo.setMarketingPrefectureGoodNotIds(alreadyExistIds);
            }
        }
        List<Map<String,Object >> goodListMap =  goodListService.getMarketingMaterialGood(goodListVo);
        goodListMap.forEach(glm->{
            glm.put("id","");
            glm.put("marketingMaterialListId","");
        });

        return goodListMap;
    }


    /**
     * 保存修改素材商品
     * @param marketingMaterialListVO
     * @return
     */
    @Override
    public void addMarketingMaterialGood(String marketingMaterialListId,MarketingMaterialListVO marketingMaterialListVO){
         //查询已保存的素材商品
        List<MarketingMaterialGood> marketingMaterialGoodList=baseMapper.selectList(new LambdaQueryWrapper<MarketingMaterialGood>()
                .eq(MarketingMaterialGood::getMarketingMaterialListId,marketingMaterialListId));
        //添加数据
        List<MarketingMaterialGoodDTO>  marketingMaterialGoodDTOList=  marketingMaterialListVO.getMarketingMaterialGoodDTOList();
        marketingMaterialGoodList.forEach(mmg->{
            Boolean bl = false;
            for(MarketingMaterialGoodDTO marketingMaterialGoodDTO:marketingMaterialGoodDTOList){
                 if( mmg.getGoodListId().equals(marketingMaterialGoodDTO.getGoodListId())){
                     bl = true;
                 }
            }
            //bl = false; 商品被移除素材
            if(!bl) {
                baseMapper.deleteById(mmg.getId());
            }

        });
        //添加素材商品
        marketingMaterialGoodDTOList.forEach(mmgDTO->{
            Boolean bll = true ;
            for(MarketingMaterialGood marketingMaterialGood:marketingMaterialGoodList){
                if( mmgDTO.getGoodListId().equals(marketingMaterialGood.getGoodListId())){
                    bll = false;
                }
            }
            //bl = true;  商品未被添加
            if(bll){
                MarketingMaterialGood marketingMaterialGood = new MarketingMaterialGood();
              //BeanUtils.copyProperties(mmgDTO,marketingMaterialGood);
                marketingMaterialGood.setMarketingMaterialListId(marketingMaterialListId);
                marketingMaterialGood.setGoodListId(mmgDTO.getGoodListId());
                baseMapper.insert(marketingMaterialGood);
            }

        });

    }

    /**
     * 获取素材商品信息
     * @param marketingMaterialListId
     * @return
     */
    @Override
    public List<Map<String,Object>> getMarketingMaterialGoodMap( String marketingMaterialListId){
        return baseMapper.getMarketingMaterialGoodMap(  marketingMaterialListId);
    };


}
