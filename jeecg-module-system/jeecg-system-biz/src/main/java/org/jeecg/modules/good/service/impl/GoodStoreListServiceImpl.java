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

  @Autowired
  private IGoodStoreSpecificationService goodStoreSpecificationService;
  @Autowired
  private IGoodStoreTypeService goodStoreTypeService;


    @Override
    public IPage<Map<String, Object>> queryPageList(Page<Map<String, Object>> page, Map<String, Object> paramMap, QueryWrapper wrapper) {
        return baseMapper.queryPageList(page,paramMap,wrapper);
    }

    @Override
    public IPage<Map<String, Object>> selectGood(Page<Map<String, Object>> page, Map<String, Object> paramMap) {
        return baseMapper.selectGood(page,paramMap);
    }

    @Override
    public GoodStoreList getGoodStoreListById(String id){

        List<GoodStoreList> listStoreGoodList=baseMapper.getGoodStoreListById(id);
        if(listStoreGoodList.size()>0){
            GoodStoreList goodStoreList=listStoreGoodList.get(0) ;
            return goodStoreList;
        }
        return null;
    };

    @Override
    public void updateDelFalg(GoodStoreList goodStoreList,String delFlag){
        baseMapper.updateDelFalg(goodStoreList.getId(),delFlag);
    };



    /**
     * 根据id获取商品
     * @param id
     * @return
     */
    @Override
    public GoodStoreListDto selectById(String id) {
        GoodStoreList goodList=baseMapper.selectById(id);
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
        //如果没有选择供应商就存入当前登录人id
        if (StringUtils.isBlank(goodListVo.getSysUserId())) {
            goodStoreList.setSysUserId(sysUser.getId());
        }
        if (StringUtils.isBlank(goodStoreList.getId())) {
            isHaveId=false;
            i = baseMapper.insert(goodStoreList);
        }else{
            i= baseMapper.updateById(goodStoreList);
        }
        if (i > 0) {
            return true;
        }
        return false;
    }

    @Override
    public IPage<GoodStoreListDto> getGoodListDto(Page<GoodStoreList> page, GoodStoreListVo goodListVo,String notauditStatus) {
        IPage<GoodStoreListDto> pageList = baseMapper.getGoodListDto(page,goodListVo,notauditStatus);
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
        return baseMapper.findGoodListByGoodType(page,paramMap);
    }

    @Override
    public Map<String, Object> findGoodListByGoodId(String goodId) {
        return baseMapper.findGoodListByGoodId(goodId);
    }

    @Override
    public IPage<Map<String, Object>> findGoodListBySysUserId(Page<Map<String, Object>> page, String sysUserId) {
        return baseMapper.findGoodListBySysUserId(page,sysUserId);
    }

    @Override
    public IPage<Map<String, Object>> searchGoodList(Page<Map<String, Object>> page, Map<String, Object> paramMap) {
        return baseMapper.searchGoodList(page,paramMap);
    }

    /**
     *每日上新返回goodTypeId 集合 根据个数倒叙
     * @param createTime
     * @param limit
     * @return
     */
    @Override
    public List<Map<String,Object>>  getEverydayGoodStoreTypeId(String sysUserId, String createTime,Integer limit){
        return  baseMapper.getEverydayGoodStoreTypeId(sysUserId,createTime,limit);
    };
    /**
     * 每周特惠查询
     * @param page
     * @param paramMap
     * @return
     */
    @Override
    public IPage<Map<String,Object>> getEveryWeekPreferential(Page<Map<String,Object>> page, Map<String,Object> paramMap){
        return  baseMapper.getEveryWeekPreferential(page,paramMap);
    }

    @Override
    public List<GoodStoreDiscountDTO > findStoreGoodList(GoodStoreListVo goodListVo) {
        List<GoodStoreDiscountDTO > pageList = baseMapper.findStoreGoodList(goodListVo);
        return pageList;
    }
    /**
     * 查询规格商品里有0库存的商品ID和目前总库存
     * @return
     */
    @Override
    public List<Map<String,Object>>  getGoodStoreListIdAndRepertory(){
        return baseMapper.getGoodStoreListIdAndRepertory();
    };

    /**
     * 店铺商品回收站商品个数
     * @param sysUserId
     * @return
     */
    @Override
    public Integer getGoodStoreListdelFlag(String sysUserId){
        return baseMapper.getGoodStoreListdelFlag( sysUserId);
    };

    /**
     * 商家端店铺商品列表
     * @param page
     * @param paramMap
     * @return
     */
    @Override
    public IPage<Map<String,Object>> getGoodStoreListMaps(Page<GoodStoreList> page,Map<String,Object> paramMap){
        return baseMapper.getGoodStoreListMaps(page,paramMap);
    };
    /**
     * 商家端普通商品查询列表
     * @param page
     * @param searchTermsVO
     * @return
     */
  public  IPage<Map<String,Object>> searchGoodListStore(Page<Map<String,Object>> page, SearchTermsVO searchTermsVO){
      return baseMapper.searchGoodListStore(page,searchTermsVO);
  };
}
