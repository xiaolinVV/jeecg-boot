package org.jeecg.modules.good.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.good.dto.GoodStoreDiscountDTO;
import org.jeecg.modules.good.dto.GoodStoreListDto;
import org.jeecg.modules.good.dto.SpecificationsPicturesDTO;
import org.jeecg.modules.good.entity.GoodStoreList;
import org.jeecg.modules.good.entity.GoodStoreSpecification;
import org.jeecg.modules.good.entity.GoodStoreType;
import org.jeecg.modules.good.mapper.GoodStoreListMapper;
import org.jeecg.modules.good.service.IGoodStoreListService;
import org.jeecg.modules.good.service.IGoodStoreSpecificationService;
import org.jeecg.modules.good.service.IGoodStoreTypeService;
import org.jeecg.modules.good.vo.GoodStoreListSpecificationVO;
import org.jeecg.modules.good.vo.GoodStoreListVo;
import org.jeecg.modules.good.vo.SearchTermsVO;
import org.jeecg.modules.good.vo.SpecificationsPicturesVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description: 店铺商品列表
 * @Author: jeecg-boot
 * @Date:   2019-10-25
 * @Version: V1.0
 */
@Service
@Slf4j
public class GoodStoreListServiceImpl extends ServiceImpl<GoodStoreListMapper, GoodStoreList> implements IGoodStoreListService {
  @Autowired(required = false)
  private GoodStoreListMapper goodStoreListMapper;
  @Autowired
  private IGoodStoreSpecificationService goodStoreSpecificationService;
  @Autowired
  private IGoodStoreTypeService goodStoreTypeService;
    @Override
    public IPage<GoodStoreList> getGoodListdelFlagOrAuditStatus(Page<GoodStoreList> page, String delFlag, String auditStatus, QueryWrapper<GoodStoreList> queryWrapper){
        return  goodStoreListMapper.getGoodStoreListdelFlagOrAuditStatus(page,delFlag,auditStatus,queryWrapper);
    };

    @Override
    public GoodStoreList getGoodStoreListById(String id){

        List<GoodStoreList> listStoreGoodList=goodStoreListMapper.getGoodStoreListById(id);
        if(listStoreGoodList.size()>0){
            GoodStoreList goodStoreList=listStoreGoodList.get(0) ;
            return goodStoreList;
        }
        return null;
    };

    @Override
    public void updateDelFalg(GoodStoreList goodStoreList,String delFlag){
        goodStoreListMapper.updateDelFalg(goodStoreList.getId(),delFlag);
    };
    @Override
    public  List<GoodStoreList>  getGoodStoreListOK(QueryWrapper<GoodStoreList> queryWrapper){
        return goodStoreListMapper.getGoodStoreListOK(queryWrapper);
    }


    /**
     * 根据id获取商品
     * @param id
     * @return
     */
    @Override
    public GoodStoreListDto selectById(String id) {
        GoodStoreList goodList=goodStoreListMapper.selectById(id);
        GoodStoreListDto goodStoreListDto=new GoodStoreListDto();
        List<GoodStoreSpecification> goodStoreSpecifications=goodStoreSpecificationService.getGoodStoreSpecificationByGoodListId(id);
        BeanUtils.copyProperties(goodList,goodStoreListDto);
        goodStoreListDto.setGoodListSpecificationVOs(goodStoreSpecifications);
        return goodStoreListDto;
    }
    /**
     * 保存商品
     *
     * @param goodListVo
     * @return
     */
    @Override
    public boolean saveOrUpdate(GoodStoreListVo goodListVo) {
        //获取当前登录人信息
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        GoodStoreList goodStoreList=new GoodStoreList();
        BeanUtils.copyProperties(goodListVo,goodStoreList);
        List<GoodStoreListSpecificationVO> listGoodStoreListSpecificationVO=null;
        if(goodListVo.getGoodListSpecificationVOs1()!=null ){
            listGoodStoreListSpecificationVO= JSONObject.parseArray(goodListVo.getGoodListSpecificationVOs1()).toJavaList(GoodStoreListSpecificationVO.class);
            //添加规格图
            if(goodListVo.getSpecificationsPictures()!=null){
                List<SpecificationsPicturesVO> listSpecificationsPicturesVO = JSONObject.parseArray(goodListVo.getSpecificationsPictures()).toJavaList(SpecificationsPicturesVO.class);
                //List<Map> list= JSONObject.parseObject(goodListVo.getSpecificationsPictures(),List.class);
                for(SpecificationsPicturesVO specificationsPicturesVO : listSpecificationsPicturesVO){
                    for(GoodStoreListSpecificationVO goodStoreListSpecificationVO:listGoodStoreListSpecificationVO){
                        if(goodStoreListSpecificationVO.getSpecification().contains(specificationsPicturesVO.getName())){
                            if(specificationsPicturesVO.getUrl()!=null){
                                goodStoreListSpecificationVO.setSpecificationPicture(specificationsPicturesVO.getUrl());
                            }
                        }
                    }
                }
            }
        }
        goodStoreList.setSpecification(goodListVo.getSpecification());
        int i=0;
        int result1;
        Boolean isHaveId=true;
        // if (Objects.nonNull(goodList.getId())) {
        //最低商品价格
        if (StringUtils.isNotBlank(goodListVo.getPrice())) {
            result1 = goodListVo.getPrice().indexOf("-");
            if (result1 != -1) {
                String smallPrice = goodListVo.getPrice().substring(0, goodListVo.getPrice().indexOf("-"));
                goodStoreList.setSmallPrice(smallPrice);
            } else {
                goodStoreList.setSmallPrice(goodListVo.getPrice());
            }
        }
        //最低vip价格
        if (StringUtils.isNotBlank(goodListVo.getVipPrice())) {
            result1 = goodListVo.getVipPrice().indexOf("-");
            if (result1 != -1) {
                String smallVipPrice = goodListVo.getVipPrice().substring(0, goodListVo.getVipPrice().indexOf("-"));
                goodStoreList.setSmallVipPrice(smallVipPrice);
            } else {
                goodStoreList.setSmallVipPrice(goodListVo.getVipPrice());
            }
        }
        //最低成本价
        if (StringUtils.isNotBlank(goodListVo.getCostPrice())) {
            result1 = goodListVo.getCostPrice().indexOf("-");
            if (result1 != -1) {
                String smallCostPrice = goodListVo.getCostPrice().substring(0, goodListVo.getCostPrice().indexOf("-"));
                goodStoreList.setSmallCostPrice(smallCostPrice);
            } else {
                goodStoreList.setSmallCostPrice(goodListVo.getCostPrice());
            }
        }
        //如果没有选择供应商就存入当前登录人id
        if (StringUtils.isBlank(goodListVo.getSysUserId())) {
            goodStoreList.setSysUserId(sysUser.getId());
        }
        if (StringUtils.isBlank(goodStoreList.getId())) {
            isHaveId=false;
            i = goodStoreListMapper.insert(goodStoreList);
        }else{
            i= goodStoreListMapper.updateById(goodStoreList);
        }
        if (i > 0) {
            saveOrupdateSpecification(goodStoreList,listGoodStoreListSpecificationVO,isHaveId,goodListVo.getWeight());
            return true;
        }
        return false;
    }

    @Override
    public GoodStoreListDto selectGoodListById(String id) {
        return null;
    }

    @Override
    public IPage<GoodStoreListDto> getGoodListDto(Page<GoodStoreList> page, GoodStoreListVo goodListVo,String notauditStatus) {
        //查询添加goodTypeId 处理
       /* if(goodListVo!=null){
            if(goodListVo.getGoodTypeIdTwoevel()!=null && !goodListVo.getGoodTypeIdTwoevel().equals("")){
                goodListVo.setGoodStoreTypeId(goodListVo.getGoodTypeIdTwoevel());
            }
        }*/
        IPage<GoodStoreListDto> pageList = baseMapper.getGoodListDto(page,goodListVo,notauditStatus);
        GoodStoreType goodType1;
        GoodStoreType goodType2;
        String string;
        List<GoodStoreSpecification> listGoodSpecification;
        QueryWrapper<GoodStoreSpecification> queryWrapper1 = new QueryWrapper<>();
        try {
            for(GoodStoreListDto gd:pageList.getRecords()){

                queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.eq("good_store_list_id",gd.getId());
                listGoodSpecification=goodStoreSpecificationService.list(queryWrapper1);
               //规格图的数据
                List<SpecificationsPicturesDTO> listSpecificationPicture  = goodStoreSpecificationService.selectByspecificationPicture(gd.getId());
                if(listSpecificationPicture.size()>0){
                    //添加有规格图数据，去除无规格情况
                    if(!listSpecificationPicture.get(0).getName().equals("无")){
                        gd.setListSpecificationsPicturesDTO(listSpecificationPicture);
                    }
                }
                if(listGoodSpecification.size()>0){
                    gd.setGoodListSpecificationVOs(listGoodSpecification);
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return pageList;
    }
    @Override
    public IPage<GoodStoreListDto> getGoodListDtoDelFlag(Page<GoodStoreList> page, GoodStoreListVo goodListVo) {
        //查询添加goodTypeId 处理
        if(goodListVo!=null){
            if(goodListVo.getGoodTypeIdTwoevel()!=null && !goodListVo.getGoodTypeIdTwoevel().equals("")){
                goodListVo.setGoodStoreTypeId(goodListVo.getGoodTypeIdTwoevel());
            }
        }
        IPage<GoodStoreListDto> pageList = baseMapper.getGoodListDtoDelFlag(page,goodListVo);
        GoodStoreType goodType1;
        GoodStoreType goodType2;
        String string;
        List<GoodStoreSpecification> listGoodSpecification;
        QueryWrapper<GoodStoreSpecification> queryWrapper1 = new QueryWrapper<>();
        try {
            for(GoodStoreListDto gd:pageList.getRecords()){
                queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.eq("good_store_list_id",gd.getId());
                listGoodSpecification=goodStoreSpecificationService.list(queryWrapper1);
                if(listGoodSpecification.size()>0){
                    gd.setGoodListSpecificationVOs(listGoodSpecification);
                }
                string="";
                goodType1=goodStoreTypeService.getById(gd.getGoodStoreTypeId());//二级
                if(goodType1!=null){
                    goodType2=goodStoreTypeService.getById(goodType1.getParentId());//一级
                    if(goodType2!=null){
                        string=goodType2.getName();
                    }
                    string=string+"-"+goodType1.getName();
                    gd.setGoodTypeNameSan(string);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return pageList;
    }
    @Override
    public IPage<Map<String, Object>> findGoodListByGoodType(Page<Map<String, Object>> page, Map<String, Object> paramMap) {
        return goodStoreListMapper.findGoodListByGoodType(page,paramMap);
    }

    @Override
    public Map<String, Object> findGoodListByGoodId(String goodId) {
        return goodStoreListMapper.findGoodListByGoodId(goodId);
    }

    @Override
    public IPage<Map<String, Object>> findGoodListBySysUserId(Page<Map<String, Object>> page, String sysUserId) {
        return goodStoreListMapper.findGoodListBySysUserId(page,sysUserId);
    }

    @Override
    public IPage<Map<String, Object>> searchGoodList(Page<Map<String, Object>> page, Map<String, Object> paramMap) {
        return baseMapper.searchGoodList(page,paramMap);
    }


    /**
     *
     * @param goodStoreList 商品
     * @param goodListSpecificationVOs 规格
     */
    public void saveOrupdateSpecification(GoodStoreList goodStoreList,List<GoodStoreListSpecificationVO> goodListSpecificationVOs,Boolean isHaveId,String weight){
        List<String> specifications = new ArrayList<>();

        if ("1".equals(goodStoreList.getIsSpecification())) {
            //新增
            if(isHaveId==false) {
                goodListSpecificationVOs.forEach(g -> {
                    saveGoodSpecification(g, goodStoreList.getId());
                });
            }else{
                //修改
                List<String> gfs=goodStoreSpecificationService.selectByGoodId(goodStoreList.getId());
                goodListSpecificationVOs.forEach(g -> {
                    if(gfs.size()>0){
                        boolean bl=false;
                        for(String str :gfs){
                            if(str.equals(g.getSpecification())){
                                bl=true;
                            }
                        }
                        if(bl){//数据库有此规格
                            specifications.add(g.getSpecification());
                            //修改规格
                            goodStoreSpecificationService.updateGoodSpecificationOne(g);

                        }else{//数据库无此规格
                            saveGoodSpecification(g, goodStoreList.getId());
                            specifications.add(g.getSpecification());
                        }
                    }else{
                        saveGoodSpecification(g, goodStoreList.getId());
                        specifications.add(g.getSpecification());
                    }
                });
                //删除不在范围内的规格数据
                goodStoreSpecificationService.delpecification(goodStoreList.getId(),specifications);
            }

        } else {
            GoodStoreSpecification gf = new GoodStoreSpecification();
            gf.setCostPrice(new BigDecimal(goodStoreList.getCostPrice()));
            gf.setGoodStoreListId(goodStoreList.getId());
            gf.setRepertory(goodStoreList.getRepertory());
            if(weight!=null && !"".equals(weight) ){
                gf.setWeight(new BigDecimal(weight));//BigDecimal.ZERO
            }else {
                gf.setWeight(BigDecimal.ZERO);
            }
           // gf.setWeight(BigDecimal.ZERO);
            gf.setVipPrice(new BigDecimal(goodStoreList.getVipPrice()));
            gf.setSkuNo("无");
            gf.setSpecification("无");
            gf.setDelFlag("0");
            gf.setPrice(new BigDecimal(goodStoreList.getPrice()));
            goodStoreSpecificationService.delpecification(goodStoreList.getId(),specifications);
            goodStoreSpecificationService.save(gf);

        }
    }

    public void saveGoodSpecification(GoodStoreListSpecificationVO g,String goodListId){
        GoodStoreSpecification gf = new GoodStoreSpecification();
        gf.setCostPrice(g.getCostPrice());
        gf.setGoodStoreListId(goodListId);
        gf.setPrice(g.getPrice());
        gf.setRepertory(g.getRepertory());
        gf.setWeight(g.getWeight());
        gf.setVipPrice(g.getVipPrice());
        gf.setSkuNo(g.getSkuNo());
        gf.setSpecificationPicture(g.getSpecificationPicture());
        gf.setDelFlag("0");
        gf.setSpecification(g.getSpecification());
        gf.setSalesVolume(g.getSalesVolume());
        goodStoreSpecificationService.save(gf);
    }
    /**
     *每日上新返回goodTypeId 集合 根据个数倒叙
     * @param createTime
     * @param limit
     * @return
     */
    @Override
    public List<Map<String,Object>>  getEverydayGoodStoreTypeId(String sysUserId, String createTime,Integer limit){
        return  goodStoreListMapper.getEverydayGoodStoreTypeId(sysUserId,createTime,limit);
    };
    /**
     * 每周特惠查询
     * @param page
     * @param paramMap
     * @return
     */
    @Override
    public IPage<Map<String,Object>> getEveryWeekPreferential(Page<Map<String,Object>> page, Map<String,Object> paramMap){
        return  goodStoreListMapper.getEveryWeekPreferential(page,paramMap);
    }

    @Override
    public List<GoodStoreDiscountDTO > findStoreGoodList(GoodStoreListVo goodListVo) {
        List<GoodStoreDiscountDTO > pageList = goodStoreListMapper.findStoreGoodList(goodListVo);
        return pageList;
    }
    /**
     * 查询规格商品里有0库存的商品ID和目前总库存
     * @return
     */
    @Override
    public List<Map<String,Object>>  getGoodStoreListIdAndRepertory(){
        return goodStoreListMapper.getGoodStoreListIdAndRepertory();
    };

    /**
     * 店铺商品回收站商品个数
     * @param sysUserId
     * @return
     */
    @Override
    public Integer getGoodStoreListdelFlag(String sysUserId){
        return goodStoreListMapper.getGoodStoreListdelFlag( sysUserId);
    };

    /**
     * 商家端店铺商品列表
     * @param page
     * @param paramMap
     * @return
     */
    @Override
    public IPage<Map<String,Object>> getGoodStoreListMaps(Page<GoodStoreList> page,Map<String,Object> paramMap){
        return goodStoreListMapper.getGoodStoreListMaps(page,paramMap);
    };
    /**
     * 商家端普通商品查询列表
     * @param page
     * @param searchTermsVO
     * @return
     */
  public  IPage<Map<String,Object>> searchGoodListStore(Page<Map<String,Object>> page, SearchTermsVO searchTermsVO){
      return goodStoreListMapper.searchGoodListStore(page,searchTermsVO);
  };
}
