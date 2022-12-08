package org.jeecg.modules.good.wapi;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import freemarker.core.ArithmeticEngine.BigDecimalEngine;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.OrderNoUtils;
import org.jeecg.common.util.UUIDGenerator;
import org.jeecg.modules.alliance.dto.AllianceAccountCapitalDTO;
import org.jeecg.modules.good.entity.GoodList;
import org.jeecg.modules.good.entity.GoodSpecification;
import org.jeecg.modules.good.entity.GoodType;
import org.jeecg.modules.good.service.IGoodListService;
import org.jeecg.modules.good.service.IGoodSpecificationService;
import org.jeecg.modules.good.service.IGoodTypeService;
import org.jeecg.modules.good.vo.GoodListSpecificationVO;
import org.jeecg.modules.good.vo.GoodListVo;
import org.jeecg.modules.provider.entity.ProviderManage;
import org.jeecg.modules.provider.entity.ProviderTemplate;
import org.jeecg.modules.provider.service.IProviderManageService;
import org.jeecg.modules.provider.service.IProviderTemplateService;
import org.jeecg.modules.system.service.ISysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

import javax.net.ssl.HttpsURLConnection;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

@RequestMapping("wapi/goodList")
@Controller
public class WapiGoodListController {
    @Value(value = "${jeecg.path.upload}")
    private String uploadpath;
    @Autowired
    private IGoodListService iGoodListService;

    @Autowired
    private IProviderManageService iProviderManageService;

    @Autowired
    private IGoodTypeService iGoodTypeService;

    @Autowired
    private IGoodSpecificationService iGoodSpecificationService;
    @Autowired
    private IProviderTemplateService iProviderTemplateService;
    @Autowired
    private ISysDictService iSysDictService;

    @RequestMapping("goodAdd")
    @ResponseBody
    public Result<?> goodAdd(@RequestBody GoodListVo goodListVo){
//        goodListVos.forEach(goodListVo->{
            List<ProviderManage> providerManageList = iProviderManageService.list(new LambdaQueryWrapper<ProviderManage>()
                    .eq(ProviderManage::getDelFlag, "0")
                    .like(ProviderManage::getLinkman, goodListVo.getRealname())
            );
            if (providerManageList.size()>0){
                ProviderManage providerManage = providerManageList.get(0);
                List<GoodList> goodListList = iGoodListService.list(new LambdaQueryWrapper<GoodList>()
                        .eq(GoodList::getDelFlag, "0")
                        .like(GoodList::getGoodName, goodListVo.getGoodName())
                );
                if (goodListList.size()<=0){
                    List<GoodType> goodTypeList = iGoodTypeService.list(new LambdaQueryWrapper<GoodType>()
                            .eq(GoodType::getDelFlag, "0")
                            .like(GoodType::getName, goodListVo.getGoodTypeThreeName())
                    );
                    if (goodTypeList.size()>0){
                        GoodType goodType = goodTypeList.get(0);
                        List<ProviderTemplate> providerTemplateList = iProviderTemplateService.list(new LambdaQueryWrapper<ProviderTemplate>()
                                .eq(ProviderTemplate::getDelFlag, "0")
                                .eq(ProviderTemplate::getSysUserId, providerManage.getSysUserId())
                                .eq(ProviderTemplate::getTemplateType, "0")
                        );
                        if (providerTemplateList.size()>0){
                            ProviderTemplate providerTemplate = providerTemplateList.get(0);
                            GoodList goodList = new GoodList();
                            //id
                            goodList.setId(UUIDGenerator.generate());
                            //供应链用户id
                            goodList.setSysUserId(providerManage.getSysUserId());
                            //平台商品分类id
                            goodList.setGoodTypeId(goodType.getId());
                            //商品主图相对地址（以json的形式存储多张)
                            if (StringUtils.isNotBlank(goodListVo.getMainPicture())){
                                JSONObject jsonObject = new JSONObject();
                                if (goodListVo.getMainPicture().contains(",")){
                                    List<String> strings = Arrays.asList(goodListVo.getMainPicture().split(","));
                                    for (int i = 0; i < strings.size(); i++) {
                                        String s = this.uploadAdd(goodListVo.getPictureType(), strings.get(i));
                                        jsonObject.put(String.valueOf(i),s);
                                    }
                                }else {
                                    jsonObject.put("0",this.uploadAdd(goodListVo.getPictureType(),goodListVo.getMainPicture()));
                                }
                                System.out.println(jsonObject);
                                goodList.setMainPicture(jsonObject.toJSONString());

                            }
                            //商品名称
                            goodList.setGoodName(goodListVo.getGoodName());
                            //商品别名（默认与商品名称相同
                            goodList.setNickName(goodListVo.getGoodName());
                            //商品销售价格
                            goodList.setPrice(String.valueOf(new BigDecimal(goodListVo.getPrice()).multiply(new BigDecimal(1.2)).setScale(2, BigDecimal.ROUND_DOWN)));
                            //商品成本价
                            goodList.setCostPrice(goodListVo.getCostPrice());
                            //供货价=成本价
                            BigDecimal price = new BigDecimal(goodListVo.getPrice());

//                            BigDecimal supplyPriceRatio = price.multiply(new BigDecimal(StringUtils.substringBefore(iSysDictService.queryDictItemsByCode("supply_price_ratio").get(0).getValue(), "%")).add(new BigDecimal(1)).divide(new BigDecimal(100)));

                            goodList.setSupplyPrice(String.valueOf(goodListVo.getCostPrice()));
                            //会员价 = 销售价*0.9
                            //会员价比例
//                            String membershipRate = StringUtils.substringBefore(iSysDictService.queryDictItemsByCode("membership_rate").get(0).getValue(), "%");
                            goodList.setVipPrice(String.valueOf(new BigDecimal(goodList.getPrice()).multiply(new BigDecimal(0.9)).setScale(2, BigDecimal.ROUND_HALF_UP)));
                            //市场价
                            goodList.setMarketPrice(String.valueOf(new BigDecimal(goodList.getPrice()).multiply(new BigDecimal(1.1)).setScale(2, BigDecimal.ROUND_HALF_UP)));
                            //状态
                            goodList.setStatus("1");
                            //商品详情图多张以json的形式存储
                            if (StringUtils.isNotBlank(goodListVo.getDetailsGoods())){
                                JSONObject object = new JSONObject();
                                if (goodListVo.getDetailsGoods().contains(",")){
                                    List<String> strings = Arrays.asList(goodListVo.getDetailsGoods().split(","));
                                    for (int i = 0; i < strings.size(); i++) {
                                        String s = this.uploadAdd(goodListVo.getPictureType(), strings.get(i));
                                        object.put(String.valueOf(i),s);
                                    }
                                }else {
                                    object.put("0",this.uploadAdd(goodListVo.getPictureType(),goodListVo.getDetailsGoods()));
                                }
                                goodList.setDetailsGoods(object.toJSONString());
                            }
                            List<GoodListSpecificationVO> goodListSpecificationVOs1 = goodListVo.getGoodListSpecificationVOs();

                            BigDecimal earningsPatch = goodListSpecificationVOs1.stream()
                                    .map(GoodListSpecificationVO::getRepertory).reduce(BigDecimal.ZERO, BigDecimal::add);
                            //库存
                            goodList.setRepertory(earningsPatch);
                            //上下架；0：下架；1：上架
                            goodList.setFrameStatus(goodListVo.getFrameStatus());
                            //商品编号
                            goodList.setGoodNo(OrderNoUtils.getOrderNo());
                            //规格说明按照json的形式保存
                            goodList.setSpecification(goodListVo.getSpecification());
                            //有无规格；0：无规格；1：有规格
                            goodList.setIsSpecification(goodListVo.getIsSpecification());
                            //运费模板id
                            goodList.setProviderTemplateId(providerTemplate.getId());
                            //审核状态：0:草稿；1：待审核；2：审核通过；3：审核不通过
                            goodList.setAuditStatus("2");
                            //商品形态；0：常规商品；1：特价商品
                            goodList.setGoodForm("0");
                            //有无销售价区间；0：无；1：有
                            goodList.setPriceRange("0");
                            //销量
                            goodList.setSalesVolume(new BigDecimal(goodListVo.getSalesVolume()));
                            goodList.setSmallPrice(goodList.getPrice());
                            goodList.setSmallVipPrice(goodList.getVipPrice());
                            goodList.setSmallCostPrice(goodList.getCostPrice());
                            goodList.setSmallSupplyPrice(goodList.getSupplyPrice());
                            List<GoodListSpecificationVO> goodListSpecificationVOs = goodListVo.getGoodListSpecificationVOs();
                            if (goodListSpecificationVOs.size()>0){
                                goodListSpecificationVOs.forEach(gsl->{
                                    GoodSpecification goodSpecification = new GoodSpecification();
                                    goodSpecification.setGoodListId(goodList.getId());
                                    goodSpecification.setPrice(new BigDecimal(goodList.getPrice()));
                                    goodSpecification.setVipPrice(new BigDecimal(goodList.getVipPrice()));
                                    goodSpecification.setCostPrice(new BigDecimal(goodList.getCostPrice()));
                                    goodSpecification.setSupplyPrice(new BigDecimal(goodList.getSupplyPrice()));
                                    goodSpecification.setRepertory(gsl.getRepertory());
                                    goodSpecification.setSpecification(gsl.getSpecification());
                                    if (StringUtils.isNotBlank(gsl.getSpecificationPicture())){
                                        goodSpecification.setSpecificationPicture(this.uploadAdd("1",gsl.getSpecificationPicture()));
                                    }
                                    iGoodSpecificationService.save(goodSpecification);
                                });
                            }
                            iGoodListService.save(goodList);
                        }
                    }
                }
            }
//        });
        return Result.ok("操作成功");
    }

    private String uploadAdd(String type,String url){
        try {
            String ctxPath = uploadpath;
            String bizPath = "files";
            String nowday = new SimpleDateFormat("yyyyMMdd").format(new Date());
            File file = new File(ctxPath + File.separator + bizPath + File.separator + nowday);
            if (!file.exists()) {
                file.mkdirs();// 创建文件根目录
            }
            if (type.equals("1")){
                url = "http://cdn.sztpsh.com/"+url;
            }
            String orderNo = OrderNoUtils.getOrderNo();
            String savePath = file.getPath() + File.separator + orderNo +".jpg";
            File savefile = new File(savePath);
            FileUtils.copyURLToFile(new URL(url),savefile);
            String dbpath = bizPath + File.separator + nowday + File.separator + orderNo+".jpg";
            if (dbpath.contains("\\")) {
                dbpath = dbpath.replace("\\", "/");
            }
            System.out.println(dbpath);
            return dbpath;
        } catch (IOException e) {
            return null;
        }
    }
}
