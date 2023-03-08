package org.jeecg.modules.good.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.good.dto.GoodDiscountDTO;
import org.jeecg.modules.good.dto.GoodListDto;
import org.jeecg.modules.good.dto.SpecificationsPicturesDTO;
import org.jeecg.modules.good.entity.*;
import org.jeecg.modules.good.mapper.GoodListMapper;
import org.jeecg.modules.good.service.*;
import org.jeecg.modules.good.vo.GoodListSpecificationVO;
import org.jeecg.modules.good.vo.GoodListVo;
import org.jeecg.modules.good.vo.SearchTermsVO;
import org.jeecg.modules.good.vo.SpecificationsPicturesVO;
import org.jeecg.modules.marketing.entity.*;
import org.jeecg.modules.marketing.service.*;
import org.jeecg.modules.marketing.store.prefecture.service.IMarketingStorePrefectureGoodService;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.entity.MemberShoppingCart;
import org.jeecg.modules.member.service.IMemberGradeService;
import org.jeecg.modules.member.service.IMemberListService;
import org.jeecg.modules.member.service.IMemberShoppingCartService;
import org.jeecg.modules.store.entity.StoreManage;
import org.jeecg.modules.store.service.IStoreManageService;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description: 商品列表
 * @Author: jeecg-boot
 * @Date: 2019-10-18
 * @Version: V1.0
 */
@Service
@Slf4j
public class GoodListServiceImpl extends ServiceImpl<GoodListMapper, GoodList> implements IGoodListService {

    @Autowired(required = false)
    private GoodListMapper goodListMapper;
    @Autowired
    private IGoodSpecificationService goodSpecificationService;
    @Autowired
    @Lazy
    private IGoodTypeService goodTypeService;
    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private IMemberListService iMemberListService;

    @Autowired
    private IGoodStoreListService iGoodStoreListService;
    @Autowired
    private IStoreManageService iStoreManageService;
    @Autowired
    private IGoodStoreSpecificationService iGoodStoreSpecificationService;

    @Autowired
    private IGoodSpecificationService iGoodSpecificationService;

    @Autowired
    @Lazy
    private IMemberShoppingCartService iMemberShoppingCartService;

    @Autowired
    @Lazy
    private IMarketingPrefectureGoodService iMarketingPrefectureGoodService;

    @Autowired
    @Lazy
    private IMarketingPrefectureGoodSpecificationService iMarketingPrefectureGoodSpecificationService;

    @Autowired
    private IMarketingFreeGoodSpecificationService iMarketingFreeGoodSpecificationService;

    @Autowired
    private IMarketingGroupRecordService iMarketingGroupRecordService;

    @Autowired
    @Lazy
    private IMarketingPrefectureService iMarketingPrefectureService;
    @Autowired
    private IMemberGradeService iMemberGradeService;

    @Autowired
    private IMarketingLeagueSettingService iMarketingLeagueSettingService;


    @Autowired
    private IMarketingStorePrefectureGoodService iMarketingStorePrefectureGoodService;



    @Override
    public IPage<Map<String, Object>> queryPageList(Page<Map<String, Object>> page, Map<String, Object> paramMap, QueryWrapper wrapper) {
        return baseMapper.queryPageList(page,paramMap,wrapper);
    }



    ;

    @Override
    public GoodList getGoodListById(String id) {
        GoodList goodeList = goodListMapper.getGoodListById(id);
        return goodeList;
    }

    @Override
    public void updateDelFalg(GoodList goodList, String delFlag) {
        goodListMapper.updateDelFalg(goodList, delFlag);
    }


    /**
     * 根据id获取商品
     *
     * @param id
     * @return
     */
    @Override
    public GoodListDto selectById(String id) {
        GoodList goodList = goodListMapper.selectById(id);
        GoodListDto goodListDto = new GoodListDto();
        List<GoodSpecification> goodSpecifications = goodSpecificationService.getGoodSpecificationByGoodListId(id);
        BeanUtils.copyProperties(goodList, goodListDto);
        goodListDto.setGoodListSpecificationVOs(goodSpecifications);
        return goodListDto;
    }

    ;

    /**
     * 保存商品
     *
     * @param goodListVo
     * @return
     */
    @Override
    @Transactional
    public boolean saveOrUpdate(GoodListVo goodListVo) {
        //获取当前用户
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        GoodList goodList = new GoodList();
        //是否需要重新审核判断
        goodListVo = isReExamination(goodListVo,sysUser.getId());
        BeanUtils.copyProperties(goodListVo, goodList);
        List<GoodListSpecificationVO> goodListSpecificationVOs = null;
        if (oConvertUtils.isNotEmpty(goodListVo.getGoodListSpecificationVOs1())) {
            goodListSpecificationVOs = JSONObject.parseArray(goodListVo.getGoodListSpecificationVOs1()).toJavaList(GoodListSpecificationVO.class);
           //添加规格图
            if(oConvertUtils.isNotEmpty(goodListVo.getSpecificationsPictures())){
                List<SpecificationsPicturesVO> listSpecificationsPicturesVO = JSONObject.parseArray(goodListVo.getSpecificationsPictures()).toJavaList(SpecificationsPicturesVO.class);
                for(SpecificationsPicturesVO specificationsPicturesVO : listSpecificationsPicturesVO){
                  for(GoodListSpecificationVO goodListSpecificationVO:goodListSpecificationVOs){
                      if(goodListSpecificationVO.getSpecification().contains(specificationsPicturesVO.getName())){
                         if(specificationsPicturesVO.getUrl()!=null){
                             goodListSpecificationVO.setSpecificationPicture(specificationsPicturesVO.getUrl());
                         }
                      }
                  }
                }
            }
        }
        if (goodListVo.getIsSpecification().equals("0")){
            goodList.setSpecification("");
        }else {
            goodList.setSpecification(goodListVo.getSpecification());
        }
        int i = 0;
        int result1;
        Boolean isHaveId = true;
        //最低商品价格(需要修正或者删除代码)
        /*if (StringUtils.isNotBlank(goodListVo.getPrice())) {
            result1 = goodListVo.getPrice().indexOf("-");
            if (result1 != -1) {
                String smallPrice = goodListVo.getPrice().substring(0, goodListVo.getPrice().indexOf("-"));
                goodList.setSmallPrice(smallPrice);
            } else {
                goodList.setSmallPrice(goodListVo.getPrice());
            }
        }
        //最低vip价格
        if (StringUtils.isNotBlank(goodListVo.getVipPrice())) {
            result1 = goodListVo.getVipPrice().indexOf("-");
            if (result1 != -1) {
                String smallVipPrice = goodListVo.getVipPrice().substring(0, goodListVo.getVipPrice().indexOf("-"));
                goodList.setSmallVipPrice(smallVipPrice);
            } else {
                goodList.setSmallVipPrice(goodListVo.getVipPrice());
            }
        }
        //最低成本价
        if (StringUtils.isNotBlank(goodListVo.getCostPrice()) ) {
            result1 = goodListVo.getCostPrice().indexOf("-");
            if (result1 != -1) {
                String smallCostPrice = goodListVo.getCostPrice().substring(0, goodListVo.getCostPrice().indexOf("-"));
                goodList.setSmallCostPrice(smallCostPrice);
            } else {
                goodList.setSmallCostPrice(goodListVo.getCostPrice());
            }
        }
        //最低供货价
        if (StringUtils.isNotBlank(goodListVo.getSupplyPrice())) {
            result1 = goodListVo.getSupplyPrice().indexOf("-");
            if (result1 != -1) {
                String smallSupplyPrice = goodListVo.getSupplyPrice().substring(0, goodListVo.getSupplyPrice().indexOf("-"));
                goodList.setSmallSupplyPrice(smallSupplyPrice);
            } else {
                goodList.setSmallSupplyPrice(goodListVo.getSupplyPrice());
            }
        }*/
        //如果没有选择供应商就存入当前登录人id
        if (StringUtils.isBlank(goodListVo.getSysUserId())) {
            goodList.setSysUserId(sysUser.getId());
        }

        if (StringUtils.isBlank(goodList.getId())) {
            isHaveId = false;
            i = goodListMapper.insert(goodList);
        } else {

            i = goodListMapper.updateById(goodList);

            //判断修改商品后,专区商品是否符合条件
            updateMarketingPrefectureGood(goodList.getId());

        }
        if (i > 0) {
            saveOrupdateSpecification(goodList, goodListSpecificationVOs, isHaveId,goodListVo.getWeight());
            return true;
        }
        return false;
    }
    

    private void updatePrefectureGoodSpecification(String goodId){
        iMarketingPrefectureGoodService.update(new MarketingPrefectureGood().setStatus("3"),new LambdaQueryWrapper<MarketingPrefectureGood>()
                .eq(MarketingPrefectureGood::getDelFlag,"0")
                .eq(MarketingPrefectureGood::getGoodListId,goodId)
        );
    }
    /**
     * @param goodList                 商品
     * @param goodListSpecificationVOs 规格
     */
    public void saveOrupdateSpecification(GoodList goodList, List<GoodListSpecificationVO> goodListSpecificationVOs, Boolean isHaveId,String weight) {
        List<String> specifications = new ArrayList<>();
        if ("1".equals(goodList.getIsSpecification())) {
            //新增
            if (isHaveId == false) {
                goodListSpecificationVOs.forEach(g -> {
                    saveGoodSpecification(g, goodList.getId());
                });
            } else {
                //修改
                List<String> gfs = goodSpecificationService.selectByGoodId(goodList.getId());
                goodListSpecificationVOs.forEach(g -> {
                    if (gfs.size() > 0) {
                        boolean bl = false;
                        for (String str : gfs) {
                            if (str.equals(g.getSpecification())) {
                                bl = true;
                            }
                        }
                        if (bl) {//数据库有此规格
                            specifications.add(g.getSpecification());
                            //修改规格
                            goodSpecificationService.updateGoodSpecificationOne(g);

                        } else {//数据库无此规格
                            saveGoodSpecification(g, goodList.getId());
                            specifications.add(g.getSpecification());
                            updatePrefectureGoodSpecification(goodList.getId());
                        }
                    } else {
                        saveGoodSpecification(g, goodList.getId());
                        specifications.add(g.getSpecification());
                        //
                      //  goodList.setIsSpecification("0");
                     //   goodListMapper.updateById(goodList);
                    }
                });
                //删除不在范围内的规格数据
                goodSpecificationService.delpecification(goodList.getId(), specifications);
            }

        } else {
            if(StringUtils.isNotBlank(goodList.getId())){
                LambdaQueryWrapper<GoodSpecification> goodSpecificationLambdaQueryWrapper = new LambdaQueryWrapper<GoodSpecification>()
                        .eq(GoodSpecification::getGoodListId, goodList.getId())
                        .eq(GoodSpecification::getDelFlag, "0");
              //修改规格
             /* if(goodSpecificationService.count(goodSpecificationLambdaQueryWrapper)>1){
                  goodSpecificationService.remove(goodSpecificationLambdaQueryWrapper);
                  GoodSpecification gf = new GoodSpecification();
                  gf.setCostPrice(new BigDecimal(goodList.getCostPrice()));
                  gf.setGoodListId(goodList.getId());
                  gf.setRepertory(goodList.getRepertory());
                  if(weight!=null && !"".equals(weight) ){
                      gf.setWeight(new BigDecimal(weight));//BigDecimal.ZERO
                  }else {
                      gf.setWeight(BigDecimal.ZERO);
                  }
                  gf.setVipPrice(new BigDecimal(goodList.getVipPrice()));
                  gf.setSkuNo("无");
                  gf.setSpecification("无");
                  gf.setDelFlag("0");
                  gf.setSupplyPrice(new BigDecimal(goodList.getSupplyPrice()));
                  gf.setPrice(new BigDecimal(goodList.getPrice()));
                  goodSpecificationService.save(gf);
                  updatePrefectureGoodSpecification(goodList.getId());
              }else if (goodSpecificationService.count(goodSpecificationLambdaQueryWrapper)==1){
                  GoodSpecification goodSpecification = goodSpecificationService.list(goodSpecificationLambdaQueryWrapper).get(0);
                  goodSpecification.setCostPrice(new BigDecimal(goodList.getCostPrice()));
                  goodSpecification.setRepertory(goodList.getRepertory());
                  if(weight!=null && !"".equals(weight) ){
                      goodSpecification.setWeight(new BigDecimal(weight));//BigDecimal.ZERO
                  }else {
                      goodSpecification.setWeight(BigDecimal.ZERO);
                  }
                  goodSpecification.setVipPrice(new BigDecimal(goodList.getVipPrice()));
                  goodSpecification.setSupplyPrice(new BigDecimal(goodList.getSupplyPrice()));
                  goodSpecification.setPrice(new BigDecimal(goodList.getPrice()));
                  goodSpecificationService.saveOrUpdate(goodSpecification);
              }else {
                  //新增规格
                  GoodSpecification gf = new GoodSpecification();
                  gf.setCostPrice(new BigDecimal(goodList.getCostPrice()));
                  gf.setGoodListId(goodList.getId());
                  gf.setRepertory(goodList.getRepertory());
                  if(weight!=null && !"".equals(weight) ){
                      gf.setWeight(new BigDecimal(weight));//BigDecimal.ZERO
                  }else {
                      gf.setWeight(BigDecimal.ZERO);
                  }
                  gf.setVipPrice(new BigDecimal(goodList.getVipPrice()));
                  gf.setSkuNo("无");
                  gf.setSpecification("无");
                  gf.setDelFlag("0");
                  gf.setSupplyPrice(new BigDecimal(goodList.getSupplyPrice()));
                  gf.setPrice(new BigDecimal(goodList.getPrice()));
                  goodSpecificationService.save(gf);
              }*/
            }else{
                //新增规格
              /*  GoodSpecification gf = new GoodSpecification();
                gf.setCostPrice(new BigDecimal(goodList.getCostPrice()));
                gf.setGoodListId(goodList.getId());
                gf.setRepertory(goodList.getRepertory());
                if(weight!=null && !"".equals(weight) ){
                    gf.setWeight(new BigDecimal(weight));//BigDecimal.ZERO
                }else {
                    gf.setWeight(BigDecimal.ZERO);
                }
                gf.setVipPrice(new BigDecimal(goodList.getVipPrice()));
                gf.setSkuNo("无");
                gf.setSpecification("无");
                gf.setDelFlag("0");
                gf.setSupplyPrice(new BigDecimal(goodList.getSupplyPrice()));
                gf.setPrice(new BigDecimal(goodList.getPrice()));
                goodSpecificationService.delpecification(goodList.getId(), specifications);
                goodSpecificationService.save(gf);*/
            }

        }
    }

    public void saveGoodSpecification(GoodListSpecificationVO g, String goodListId) {
        GoodSpecification gf = new GoodSpecification();
        gf.setCostPrice(g.getCostPrice());
        gf.setGoodListId(goodListId);
        gf.setPrice(g.getPrice());
        gf.setRepertory(g.getRepertory());
        gf.setWeight(g.getWeight());
        gf.setVipPrice(g.getVipPrice());
        gf.setSkuNo(g.getSkuNo());
        gf.setDelFlag("0");
        gf.setSpecificationPicture(g.getSpecificationPicture());
        gf.setSupplyPrice(g.getSupplyPrice());
        gf.setSpecification(g.getSpecification());
        gf.setSalesVolume(g.getSalesVolume());
        goodSpecificationService.save(gf);
    }

    @Override
    public IPage<GoodListDto> getGoodListDto(Page<GoodList> page, GoodListVo goodListVo, String notauditStatus) {
        //查询添加goodTypeId 处理
        /*if(goodListVo!=null){
            if(goodListVo.getGoodTypeIdThreeLevel()!=null && !goodListVo.getGoodTypeIdThreeLevel().equals("")){
                goodListVo.setGoodTypeId(goodListVo.getGoodTypeIdThreeLevel());
            }else if(goodListVo.getGoodTypeIdTwoevel()!=null && !goodListVo.getGoodTypeIdTwoevel().equals("")){
                goodListVo.setGoodTypeId(goodListVo.getGoodTypeIdTwoevel());
            }
        }*/
        IPage<GoodListDto> pageList = baseMapper.getGoodListDto(page, goodListVo, notauditStatus);
        GoodType goodType1;
        GoodType goodType2;
        GoodType goodType3;
        String string;
        List<GoodSpecification> listGoodSpecification;
        QueryWrapper<GoodSpecification> queryWrapper1 = new QueryWrapper<>();
        for (GoodListDto gd : pageList.getRecords()) {
            //供应商名字
            if (StringUtils.isNotBlank(gd.getSysUserId())) {
                SysUser sysUser = sysUserService.getById(gd.getSysUserId());
                if (sysUser != null) {
                    gd.setRealname(sysUser.getRealname());
                }
            }
            queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("good_list_id", gd.getId());
            queryWrapper1.orderByDesc("create_time");
            listGoodSpecification = goodSpecificationService.list(queryWrapper1);
           List<SpecificationsPicturesDTO> listSpecificationPicture  = goodSpecificationService.selectByspecificationPicture(gd.getId());
           if(listSpecificationPicture.size()>0){
               //添加有规格图数据，去除无规格情况
               if(!listSpecificationPicture.get(0).getName().equals("无")){
               gd.setListSpecificationsPicturesDTO(listSpecificationPicture);
               }
           }
            if (listGoodSpecification.size() > 0) {
                gd.setGoodListSpecificationVOs(listGoodSpecification);
            }

        }
        return pageList;
    }

    @Override
    public IPage<GoodListDto> getGoodListDtoDelFlag(Page<GoodList> page, GoodListVo goodListVo) {
        //查询添加goodTypeId 处理
        if(goodListVo!=null){
            if(goodListVo.getGoodTypeIdThreeLevel()!=null && !goodListVo.getGoodTypeIdThreeLevel().equals("")){
                goodListVo.setGoodTypeId(goodListVo.getGoodTypeIdThreeLevel());
            }else if(goodListVo.getGoodTypeIdTwoevel()!=null && !goodListVo.getGoodTypeIdTwoevel().equals("")){
                goodListVo.setGoodTypeId(goodListVo.getGoodTypeIdTwoevel());
            }
        }
        IPage<GoodListDto> pageList = baseMapper.getGoodListDtoDelFlag(page, goodListVo);
        GoodType goodType1;
        GoodType goodType2;
        GoodType goodType3;
        String string;
        List<GoodSpecification> listGoodSpecification;
        QueryWrapper<GoodSpecification> queryWrapper1 = new QueryWrapper<>();

        for (GoodListDto gd : pageList.getRecords()) {
            //供应商名字
            if (gd.getSysUserId() != null) {
                SysUser sysUser = sysUserService.getById(gd.getSysUserId());
                if (sysUser != null) {
                    gd.setRealname(sysUser.getRealname());
                }
            }
            queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("good_list_id", gd.getId());
            listGoodSpecification = goodSpecificationService.list(queryWrapper1);
            if (listGoodSpecification.size() > 0) {
                gd.setGoodListSpecificationVOs(listGoodSpecification);
            }
            string = "";
            goodType1 = goodTypeService.getById(gd.getGoodTypeId());//三级
            if (goodType1 != null) {
                goodType2 = goodTypeService.getById(goodType1.getParentId());//二级
                if (goodType2 != null) {
                    goodType3 = goodTypeService.getById(goodType2.getParentId());//一级
                    if (goodType3 != null) {
                        string = goodType3.getName();
                    }
                    string = string + "-" + goodType2.getName();
                }
                string = string + "-" + goodType1.getName();
                gd.setGoodTypeNameSan(string);
            }
        }

        return pageList;
    }

    @Override
    public IPage<Map<String, Object>> findGoodListByGoodType(Page<Map<String, Object>> page, Map<String, Object> paramMap) {
        return goodListMapper.findGoodListByGoodType(page, paramMap);
    }

    @Override
    public IPage<Map<String, Object>> searchGoodList(Page<Map<String, Object>> page, Map<String, Object> paramMap) {
        return baseMapper.searchGoodList(page,paramMap);
    }

    @Override
    public IPage<Map<String, Object>> chooseGoodList(Page<Map<String, Object>> page, Map<String, Object> paramMap) {
        return baseMapper.chooseGoodList(page,paramMap);
    }

    @Override
    public IPage<Map<String, Object>> findGoodListByGoodTypeAndlevel(Page<Map<String, Object>> page, Map<String, Object> paramMap) {
        return goodListMapper.findGoodListByGoodTypeAndlevel(page,paramMap);
    }

    @Override
    public Map<String, Object> findGoodListByGoodId(String goodId) {
        return goodListMapper.findGoodListByGoodId(goodId);
    }

    @Override
    @Transactional
    public Map<String, Object> getCarGoodByMemberId(List<MemberShoppingCart> memberShoppingCarts, String memberId) {
        Map<String,Object> objectMap= Maps.newLinkedHashMap();

        //店铺商品
        List<Map<String,Object>> storeGoods= Lists.newArrayList();
        //平台商品
        List<Map<String,Object>> goods= Lists.newArrayList();
        //无效商品
        List<Map<String,Object>> disableGoods= Lists.newArrayList();
        //免单专区
        List<Map<String,Object>> marketingFreeGoods=Lists.newArrayList();

        for (MemberShoppingCart m:memberShoppingCarts){
            if (m.getGoodType().equals("0")) {
                //添加店铺名称
                GoodStoreList goodStoreList = iGoodStoreListService.getById(m.getGoodStoreListId());

                if(goodStoreList!=null) {
                    //判断店铺是否存在

                    Map<String, Object> storeInfo = null;

                    for (Map<String, Object> s : storeGoods) {
                        if (s.get("id").toString().equals(goodStoreList.getSysUserId())) {
                            storeInfo = s;
                            break;
                        }
                    }

                    List<Map<String, Object>> myStoreGoods = null;

                    if (storeInfo == null) {
                        //店铺商品
                        storeInfo = Maps.newHashMap();
                        StoreManage storeManage = iStoreManageService.getOne(new LambdaQueryWrapper<StoreManage>()
                                .eq(StoreManage::getSysUserId,goodStoreList.getSysUserId())
                                .in(StoreManage::getPayStatus,"1","2").last("limit 1"));
                        if(storeManage==null){
                            //删除店铺不存在的购物车商品
                            iMemberShoppingCartService.removeById(m.getId());
                            continue;
                        }
                        if (storeManage.getSubStoreName() == null) {
                            storeInfo.put("storeName", storeManage.getStoreName());
                        } else {
                            storeInfo.put("storeName", storeManage.getStoreName() + "(" + storeManage.getSubStoreName() + ")");
                        }
                        myStoreGoods = Lists.newArrayList();

                        storeInfo.put("id", goodStoreList.getSysUserId());
                        storeInfo.put("myStoreGoods", myStoreGoods);
                        storeGoods.add(storeInfo);
                    } else {
                        myStoreGoods = (List<Map<String, Object>>) storeInfo.get("myStoreGoods");
                    }

                    //设置商品信息

                    Map<String, Object> goodInfo = Maps.newHashMap();

                    GoodStoreSpecification goodStoreSpecification = iGoodStoreSpecificationService.getById(m.getGoodStoreSpecificationId());
                    if(goodStoreSpecification==null){
                        iMemberShoppingCartService.removeById(m.getId());
                        continue;
                    }
                    goodInfo.put("mainPicture", goodStoreList.getMainPicture());
                    goodInfo.put("goodName", goodStoreList.getGoodName());
                    goodInfo.put("specification", goodStoreSpecification.getSpecification());

                    MemberList memberList = iMemberListService.getById(memberId);
                    if (memberList.getMemberType().equals("1")) {
                        goodInfo.put("price", goodStoreSpecification.getVipPrice());
                    } else {
                        goodInfo.put("price", goodStoreSpecification.getPrice());
                    }
                    goodInfo.put("quantity", m.getQuantity());
                    goodInfo.put("repertory", goodStoreSpecification.getRepertory());
                    goodInfo.put("status", goodStoreList.getStatus());
                    goodInfo.put("id", m.getId());
                    goodInfo.put("isPlatform", "0");
                    goodInfo.put("goodId", goodStoreList.getId());
                    goodInfo.put("sysUserId", goodStoreList.getSysUserId());
                    goodInfo.put("goodSpecificationId", goodStoreSpecification.getId());
                    goodInfo.put("marketingCertificateRecordId",m.getMarketingCertificateRecordId());
                    goodInfo.put("marketingStoreGiftCardMemberListId",m.getMarketingStoreGiftCardMemberListId());
                    goodInfo.put("marketingStorePrefectureGoodId",m.getMarketingStorePrefectureGoodId());

                    /*店铺专区价格设置*/
                    if(StringUtils.isNotBlank(m.getMarketingStorePrefectureGoodId())){
                        iMarketingStorePrefectureGoodService.settingGetMemberShoppingCarInfo(goodInfo,m.getMarketingStorePrefectureGoodId(),goodStoreSpecification.getSpecification());
                    }


                    //兑换商品价格设置
                    if(StringUtils.isNotBlank(m.getMarketingCertificateRecordId())){
                        goodInfo.put("price", new BigDecimal(0));
                    }
                    //控制条件
                    if (goodStoreSpecification.getRepertory().intValue() <= 0 || goodStoreList.getStatus().equals("0") || goodStoreSpecification.getRepertory().subtract(m.getQuantity()).doubleValue() <= 0) {
                        disableGoods.add(goodInfo);
                    } else {
                        myStoreGoods.add(goodInfo);
                    }
                }else{
                    iMemberShoppingCartService.removeById(m.getId());
                }
            }else {
                //平台商品
                GoodList goodList = this.getById(m.getGoodListId());

                if (goodList != null) {
                    //设置商品信息
                    Map<String, Object> goodInfo = Maps.newHashMap();
                    GoodSpecification goodSpecification = iGoodSpecificationService.getById(m.getGoodSpecificationId());
                    if(goodSpecification==null){
                        iMemberShoppingCartService.removeById(m.getId());
                        continue;
                    }
                    goodInfo.put("mainPicture", goodList.getMainPicture());
                    goodInfo.put("goodName", goodList.getGoodName());
                    goodInfo.put("specification", goodSpecification.getSpecification());

                    //普通商品
                    if(StringUtils.isBlank(m.getMarketingPrefectureId())&&StringUtils.isBlank(m.getMarketingFreeGoodListId())&&StringUtils.isBlank(m.getMarketingGroupRecordId())&&StringUtils.isBlank(m.getMarketingLeagueGoodListId())) {
                        if(StringUtils.isBlank(m.getMarketingCertificateRecordId())) {
                            MemberList memberList = iMemberListService.getById(memberId);
                            goodInfo.put("label", "");
                            if (memberList.getMemberType().equals("1")) {
                                goodInfo.put("price", goodSpecification.getVipPrice());
                                //会员等级计算价格
                                iMemberGradeService.settingGetMemberShopCardinfo(goodInfo,goodSpecification,memberId);
                            } else {
                                goodInfo.put("price", goodSpecification.getPrice());
                            }

                        }
                    }
                    //专区商品
                    if(StringUtils.isNotBlank(m.getMarketingPrefectureId())){
                        MarketingPrefecture marketingPrefecture=iMarketingPrefectureService.getById(m.getMarketingPrefectureId());
                        if(marketingPrefecture==null){
                            iMemberShoppingCartService.removeById(m.getId());
                            continue;
                        }

                        //获取专区商品
                        MarketingPrefectureGood marketingPrefectureGood=iMarketingPrefectureGoodService.getOne(new LambdaQueryWrapper<MarketingPrefectureGood>()
                                .eq(MarketingPrefectureGood::getMarketingPrefectureId,m.getMarketingPrefectureId())
                                .eq(MarketingPrefectureGood::getGoodListId,m.getGoodListId()));
                        if(marketingPrefectureGood==null){
                            iMemberShoppingCartService.removeById(m.getId());
                            continue;
                        }
                        //获取专区规格
                        MarketingPrefectureGoodSpecification marketingPrefectureGoodSpecification=iMarketingPrefectureGoodSpecificationService.getOne(new LambdaQueryWrapper<MarketingPrefectureGoodSpecification>()
                                .eq(MarketingPrefectureGoodSpecification::getMarketingPrefectureGoodId,marketingPrefectureGood.getId())
                                .eq(MarketingPrefectureGoodSpecification::getGoodSpecificationId,goodSpecification.getId()));
                        if(marketingPrefectureGoodSpecification==null){
                            iMemberShoppingCartService.removeById(m.getId());
                            continue;
                        }

                        goodInfo.put("price", marketingPrefectureGoodSpecification.getPrefecturePrice());

                        goodInfo.put("label",marketingPrefecture.getPrefectureLabel());
                        goodInfo.put("marketingRushGroupId",StringUtils.defaultString(m.getMarketingRushGroupId(),""));
                    }
                    //免单商品
                    if(StringUtils.isNotBlank(m.getMarketingFreeGoodListId())){
                        //获取免单商品规格信息
                        MarketingFreeGoodSpecification marketingFreeGoodSpecification=iMarketingFreeGoodSpecificationService.getOne(new LambdaQueryWrapper<MarketingFreeGoodSpecification>()
                                .eq(MarketingFreeGoodSpecification::getMarketingFreeGoodListId,m.getMarketingFreeGoodListId())
                                .eq(MarketingFreeGoodSpecification::getGoodSpecificationId,goodSpecification.getId()));
                        if(marketingFreeGoodSpecification==null){
                            iMemberShoppingCartService.removeById(m.getId());
                            continue;
                        }
                        goodInfo.put("price", marketingFreeGoodSpecification.getFreePrice());
                        goodInfo.put("label",m.getPrefectureLabel());
                    }
                    //中奖拼团商品
                    if(StringUtils.isNotBlank(m.getMarketingGroupRecordId())){
                        MarketingGroupRecord marketingGroupRecord=iMarketingGroupRecordService.getById(m.getMarketingGroupRecordId());
                        goodInfo.put("price", marketingGroupRecord.getActivityPrice());
                        goodInfo.put("label",m.getPrefectureLabel());
                    }
                    //加盟专区
                    if(StringUtils.isNotBlank(m.getMarketingLeagueGoodListId())){
                        goodInfo.put("price", m.getAddPrice());
                        MarketingLeagueSetting  marketingLeagueSetting=iMarketingLeagueSettingService.getMarketingLeagueSetting();
                        goodInfo.put("label",marketingLeagueSetting.getLabel());
                    }
                    //兑换商品价格设置
                    if(StringUtils.isNotBlank(m.getMarketingCertificateRecordId())){
                        goodInfo.put("price", new BigDecimal(0));
                    }

                    goodInfo.put("quantity", m.getQuantity());
                    goodInfo.put("repertory", goodSpecification.getRepertory());
                    goodInfo.put("frameStatus", goodList.getFrameStatus());
                    goodInfo.put("status", goodList.getStatus());
                    goodInfo.put("id", m.getId());
                    goodInfo.put("isPlatform", "1");
                    goodInfo.put("goodId", goodList.getId());
                    goodInfo.put("sysUserId", goodList.getSysUserId());
                    goodInfo.put("goodSpecificationId", goodSpecification.getId());
                    goodInfo.put("marketingPrefectureId",m.getMarketingPrefectureId());
                    goodInfo.put("marketingCertificateRecordId",m.getMarketingCertificateRecordId());
                    goodInfo.put("marketingFreeGoodListId",m.getMarketingFreeGoodListId());
                    goodInfo.put("marketingGroupRecordId",m.getMarketingGroupRecordId());
                    goodInfo.put("marketingLeagueGoodListId",m.getMarketingLeagueGoodListId());

                    //控制条件
                    if (goodSpecification.getRepertory().intValue() <= 0 || goodList.getFrameStatus().equals("0") || goodList.getStatus().equals("0") || goodSpecification.getRepertory().subtract(m.getQuantity()).doubleValue() <= 0) {
                        disableGoods.add(goodInfo);
                    } else {
                        //免单专区加入
                        if(StringUtils.isNotBlank(m.getMarketingFreeGoodListId())){
                            marketingFreeGoods.add(goodInfo);
                        }else {
                            goods.add(goodInfo);
                        }
                    }
                }else{
                    iMemberShoppingCartService.removeById(m.getId());
                }
            }
        }
        objectMap.put("storeGoods",storeGoods);
        objectMap.put("goods",goods);
        objectMap.put("disableGoods",disableGoods);
        objectMap.put("marketingFreeGoods",marketingFreeGoods);
        return objectMap;
    }

    /**
     *每日上新返回goodTypeId 集合 根据个数倒叙
     * @param createTime
     * @param limit
     * @return
     */
    @Override
    public List<Map<String,Object>>  getEverydayGoodTypeId(String createTime, Integer limit){
        return goodListMapper.getEverydayGoodTypeId(createTime,limit);
    };


    @Override
    public IPage<GoodDiscountDTO> findGoodList(Page<GoodListVo> page,GoodListVo goodListVo) {
        return baseMapper.findGoodList(page,goodListVo);
    }


    /**
     * 查询出专区商品数据
     * @param goodListVo
     * @return
     */
    @Override
    public IPage<Map<String,Object>>   getMarketingPrefectureGood(Page<GoodList> page,GoodListVo goodListVo){
        IPage<Map<String,Object>> pageList = baseMapper.getMarketingPrefectureGood(page,goodListVo);

        return pageList;
    }

    /**
     * 选中专区商品数据
     * @param goodListVo
     * @return
     */
    @Override
    public List<Map<String,Object>>  getMarketingPrefectureGoodPitchOn(GoodListVo goodListVo){
        List<Map<String,Object>> mapList = baseMapper.getMarketingPrefectureGood(goodListVo);

        return mapList;
    };

    /**
     * 定时器下架0库存的商品
     * @return
     */
    @Override
     public void updateGoodListISRepertoryZero(){
         //平台商品零库存下架
         List<Map<String,Object>> mapList =goodListMapper.getGoodListIdAndRepertory();
         mapList.forEach(map->{
             //零库存的商品下架
             if("0".equals(map.get("repertory").toString())){
              GoodList goodList = goodListMapper.selectById(map.get("id").toString());
                 if(goodList!=null){
                     goodList.setFrameStatus("0");
                     goodListMapper.updateById(goodList) ;
                     log.info("平台商品:"+goodList.getGoodName()+" 无库存被系统下架");
                     //删除购物车商品
                  List<MemberShoppingCart>   memberShoppingCartList=iMemberShoppingCartService.list(new LambdaQueryWrapper<MemberShoppingCart>()
                             .eq(MemberShoppingCart::getGoodListId, map.get("id"))
                             .eq(MemberShoppingCart::getDelFlag,"0"));
                     memberShoppingCartList.forEach(msc->{
                         iMemberShoppingCartService.removeById(msc.getId());
                     });

                 }
             }
         });
        //店铺订单商品零库存下架
         List<Map<String,Object>> mapStoreList = iGoodStoreListService.getGoodStoreListIdAndRepertory();
         mapStoreList.forEach(mapStore->{
             //零库存的商品下架
             if("0".equals(mapStore.get("repertory").toString())){
                 GoodStoreList goodStoreList = iGoodStoreListService.getById(mapStore.get("id").toString());
                 if(goodStoreList!=null){
                     goodStoreList.setFrameStatus("0");
                     iGoodStoreListService.updateById(goodStoreList) ;
                     log.info("店铺商品:"+goodStoreList.getGoodName()+" 无库存被系统下架");
                     //删除购物车商品
                     List<MemberShoppingCart>   memberShoppingCartList=iMemberShoppingCartService.list(new LambdaQueryWrapper<MemberShoppingCart>()
                             .eq(MemberShoppingCart::getGoodStoreListId, mapStore.get("id"))
                             .eq(MemberShoppingCart::getDelFlag,"0"));
                     memberShoppingCartList.forEach(msc->{
                         iMemberShoppingCartService.removeById(msc.getId());
                     });

                 }
             }
         });
     }

    /**
     * 判断是否要重新审核
     * @param goodListVo
     * @return
     */
    @Override
    public GoodListVo isReExamination(GoodListVo goodListVo,String sysUserId){
       //新增商品做处理
        if(StringUtils.isBlank(goodListVo.getId())){
            return goodListVo;
        }
      if(StringUtils.isNotBlank(sysUserId)){
          //判断是否需要重审
          Map<String,Object> objectMap= sysUserService.getUserRoleCodeAndGoodAudit(sysUserId);
          if(objectMap.containsKey("goodAudit")){
              //需要重新审核
              if("0".equals(objectMap.get("goodAudit"))){
                  GoodList goodList = baseMapper.selectById(goodListVo.getId());
                  if(goodList!=null){
                      //商品名称修改,需要重审
                      if(!goodList.getGoodName().equals(goodListVo.getGoodName())){goodListVo.setAuditStatus("0");}
                      //商品描述修改,需要重审
                      if(!goodList.getGoodDescribe().equals(goodListVo.getGoodDescribe())){goodListVo.setAuditStatus("0");}
                      //商品主图修改,需要重审
                      if(!goodList.getMainPicture().equals(goodListVo.getMainPicture())){goodListVo.setAuditStatus("0");}
                      //商品主图修改,需要重审
                      if(!goodList.getDetailsGoods().equals(goodListVo.getDetailsGoods())){goodListVo.setAuditStatus("0");}
                      //商品分类修改,需要重审
                      if(!goodList.getGoodTypeId().equals(goodListVo.getGoodTypeId())){goodListVo.setAuditStatus("0");}
                      //规格处理
                      List<GoodListSpecificationVO> goodListSpecificationVOs = null;
                      if (goodListVo.getGoodListSpecificationVOs1() != null) {
                          goodListSpecificationVOs = JSONObject.parseArray(goodListVo.getGoodListSpecificationVOs1()).toJavaList(GoodListSpecificationVO.class);
                          List<SpecificationsPicturesVO> listSpecificationsPicturesVO = JSONObject.parseArray(goodListVo.getSpecificationsPictures()).toJavaList(SpecificationsPicturesVO.class);
                          for(GoodListSpecificationVO glspVO:goodListSpecificationVOs){
                              if(StringUtils.isNotBlank(glspVO.getId())){
                                  GoodSpecification goodSpecification = goodSpecificationService.getById(glspVO.getId());
                                  if(goodSpecification == null){
                                      //规格不存在( 需要审核 )
                                      goodListVo.setAuditStatus("0");
                                  }else{
                                      //规格存在对比规格值 /规格图
                                      //规格值修改
                                      if(!goodSpecification.getSpecification().equals(glspVO.getSpecification())){goodListVo.setAuditStatus("0");}
                                    //规格图
                                      for(SpecificationsPicturesVO specificationsPicturesVO : listSpecificationsPicturesVO){

                                              if( glspVO.getSpecification().contains(specificationsPicturesVO.getName())){
                                                  if(specificationsPicturesVO.getUrl()!=null){
                                                      if(!goodSpecification.getSpecificationPicture().equals(specificationsPicturesVO.getUrl())){
                                                          //规格图修改( 需要审核 )
                                                          goodListVo.setAuditStatus("0");
                                                      }
                                                  }
                                              }

                                      }

                                   }
                              }else{
                                 //新增规格处理( 需要审核 )
                                  goodListVo.setAuditStatus("0");
                              }

                          }
                      }
                  }


              }
          }

      }


            return goodListVo;
    }

    /**
     * 查询商品编号是否存在
     * @param goodId
     * @param isPlatform
     * @param goodNo
     * @return
     */
    @Override
    public long getGoodNoCount(String goodId,Integer isPlatform,String goodNo){
        long count = 0;
        if(isPlatform.intValue()==1){
            QueryWrapper<GoodList> queryWrapperGoodList = new QueryWrapper<>();
            queryWrapperGoodList.eq("good_no",goodNo);
            if(StringUtils.isNotBlank(goodId)){
                //排除修改的时候商品编号重复
                queryWrapperGoodList.ne("id",goodId);
            }
             count  = baseMapper.selectCount(queryWrapperGoodList);
        }else{
            QueryWrapper<GoodStoreList> queryWrapperGoodStoreList= new QueryWrapper<>();
            queryWrapperGoodStoreList.eq("good_no",goodNo);
            if(StringUtils.isNotBlank(goodId)){
                //排除修改的时候商品编号重复
                queryWrapperGoodStoreList.ne("id",goodId);
            }
            count  = iGoodStoreListService.count(queryWrapperGoodStoreList);
        }

     return count;
    }

    /**
     * 选中素材商品数据
     * @param goodListVo
     * @return
     */
    @Override
    public List<Map<String,Object>>  getMarketingMaterialGood( GoodListVo goodListVo){
        return baseMapper.getMarketingMaterialGood(goodListVo);
    }

    /**
     * 修改商品价格判断专区商品是否不符合要求
     *
     * @param goodId
     * @param
     * @return
     */
    @Override
    public void updateMarketingPrefectureGood(String goodId){

       //专区存在的商品
       List<MarketingPrefectureGood> marketingPrefectureGoodList = iMarketingPrefectureGoodService.list(new LambdaQueryWrapper<MarketingPrefectureGood>()
               .eq(MarketingPrefectureGood::getDelFlag,"0")
               .eq(MarketingPrefectureGood::getStatus,"1")
               .eq(MarketingPrefectureGood::getGoodListId,goodId));
        marketingPrefectureGoodList.forEach(mpg->{
            //获取对应专区
            MarketingPrefecture marketingPrefecture = iMarketingPrefectureService.getById(mpg.getMarketingPrefectureId());
            if(marketingPrefecture!=null){
                GoodListVo  goodListVo =new GoodListVo();
                if(marketingPrefecture.getAstrictGood().equals("1")){
                    //限制商品条件
                    goodListVo.setAstrictPriceProportion(marketingPrefecture.getAstrictPriceProportion());
                }
                goodListVo.setId(goodId);
                Page<GoodList> page = new Page<GoodList>(1, 10);

                IPage<Map<String,Object>> pageList = baseMapper.getMarketingPrefectureGood(page, goodListVo);

               if(pageList.getRecords().size() == 0){
                   //修改价格后,该商品不符合专区限制条件
                   mpg.setStatus("0");
                   mpg.setCloseExplian("修改价格后,该商品不符合专区限制条件");
                   iMarketingPrefectureGoodService.updateById(mpg);
                   log.info("修改价格后,商品不符合专区限制条件被停用的专区商品id:"+mpg.getId()+"商品id"+mpg.getGoodListId());
               }
            }

        });
    }



    /**
     * 商家端普通商品列表查询
     * @param page
     * @param searchTermsVO
     * @return
     */
    @Override
    public IPage<Map<String,Object>> searchGoodListStore(Page<Map<String,Object>> page,SearchTermsVO searchTermsVO){
        return baseMapper.searchGoodListStore( page, searchTermsVO);
    };


}
