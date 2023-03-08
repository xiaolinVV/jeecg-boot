package org.jeecg.modules.provider.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.good.entity.GoodList;
import org.jeecg.modules.good.entity.GoodSpecification;
import org.jeecg.modules.good.service.IGoodListService;
import org.jeecg.modules.good.service.IGoodSpecificationService;
import org.jeecg.modules.order.entity.OrderProviderList;
import org.jeecg.modules.order.entity.OrderProviderTemplate;
import org.jeecg.modules.order.service.IOrderProviderTemplateService;
import org.jeecg.modules.provider.dto.ProviderTemplateDTO;
import org.jeecg.modules.provider.entity.ProviderTemplate;
import org.jeecg.modules.provider.mapper.ProviderTemplateMapper;
import org.jeecg.modules.provider.service.IProviderTemplateService;
import org.jeecg.modules.provider.vo.ProviderTemplateVO;
import org.jeecg.modules.system.entity.SysArea;
import org.jeecg.modules.system.mapper.SysUserRoleMapper;
import org.jeecg.modules.system.service.ISysAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Description: 供应商运费模板
 * @Author: jeecg-boot
 * @Date:   2019-10-26
 * @Version: V1.0
 */
@Service
public class ProviderTemplateServiceImpl extends ServiceImpl<ProviderTemplateMapper, ProviderTemplate> implements IProviderTemplateService {
   @Autowired(required = false)
    private ProviderTemplateMapper providerTemplateMapper;
   @Autowired(required = false)
   private SysUserRoleMapper sysUserRoleMapper;

   @Autowired
   @Lazy
   private IGoodListService iGoodListService;

   @Autowired
   private IGoodSpecificationService iGoodSpecificationService;
    @Autowired
    private  ISysAreaService iSysAreaService;
    @Autowired
    private IOrderProviderTemplateService iOrderProviderTemplateService;
    @Override
    public List<ProviderTemplateDTO> getlistProviderTemplate(String uId) {
        return  providerTemplateMapper.getlistProviderTemplate(uId);
    }

    /**
     * 判断默认运费模板唯一
     * @param providerTemplate
     * @return
     */
    @Override
    public boolean updateIsTemplate(ProviderTemplate providerTemplate) {
        try {
            if (providerTemplate.getSysUserId()==null||"".equals(providerTemplate.getSysUserId())){
                LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
                String userId = sysUser.getId();
                providerTemplate.setSysUserId(userId);
            }
            QueryWrapper<ProviderTemplate> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("sys_user_id",providerTemplate.getSysUserId());
            List<ProviderTemplate> providerTemplates = providerTemplateMapper.selectList(queryWrapper);
            for (ProviderTemplate template : providerTemplates) {
                if ("1".equals(template.getIsTemplate())){
                    String id = template.getSysUserId();
                    providerTemplateMapper.updateIsTemplateByid(id);
                }
            }
            this.saveOrUpdate(providerTemplate);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public IPage<ProviderTemplateDTO> getProviderTemplateList(Page<ProviderTemplateDTO> page, ProviderTemplateVO providerTemplateVO) {
       return providerTemplateMapper.getProviderTemplateList(page,providerTemplateVO);

    }

    @Override
    public BigDecimal calculateFreight(List<Map<String, Object>> goodsMap,String sysAreaId) {
        BigDecimal totalFreight=new BigDecimal(0);

         List<String> sysAreaIdList = Arrays.asList(sysAreaId.split(","));
        //获取省级id
         if(sysAreaIdList.size()>0){
               sysAreaId = sysAreaIdList.get(0);
           }

        //拆分运费模板
        List<Map<String,Object>> templateMaps= Lists.newArrayList();

        //设置配送
        boolean ifDistribution=false;

        for (Map<String,Object> g:goodsMap) {
            GoodList goodList=iGoodListService.getById(g.get("goodId").toString());
            Map<String,Object> templateMap=null;
            List<Map<String,Object>> templateGoodsList=null;

            //选取已有运费模板
            for (Map<String,Object> tm:templateMaps) {
                if(tm.get("id").toString().equals(goodList.getProviderTemplateId())){
                    templateMap=tm;
                    break;
                }
            }

            //加入供应商结构信息
            if(templateMap==null){
                templateMap= Maps.newHashMap();
                templateGoodsList=Lists.newArrayList();
                templateMap.put("templateGoodsList",templateGoodsList);
                templateMap.put("id",goodList.getProviderTemplateId());
                templateMaps.add(templateMap);
            }else{
                templateGoodsList=(List<Map<String,Object>>)templateMap.get("templateGoodsList");
            }
            //商品加入供应商中归类
            templateGoodsList.add(g);
        }



        //遍历运费模板
        for (Map<String,Object> tm:templateMaps) {
            //建立单模板运费
           BigDecimal freight=new BigDecimal(0);

           //判断是否包邮
            ProviderTemplate providerTemplate=this.getById(tm.get("id").toString());
            //运费模板为空即包邮
            if(providerTemplate==null){
                continue;
            }
            //免邮地区比较
            String exemptionPostage=providerTemplate.getExemptionPostage();
            List<String> stringList= Arrays.asList(StringUtils.split(exemptionPostage,","));
            boolean isFree=false;
            for (String s:stringList) {
                if(StringUtils.equals(s,sysAreaId)){
                    isFree=true;
                    break;
                }
            }
            if(isFree){
                continue;
            }

            //非包邮
            JSONArray areaTemplates= JSON.parseArray(providerTemplate.getMailDelivery());

            JSONObject mailDeliveryMap=null;

            for (Object a:areaTemplates) {
                JSONObject aJsonObject=(JSONObject)a;
                if(a!=null&aJsonObject.getString("id").equals(sysAreaId)){
                    mailDeliveryMap=aJsonObject;
                    break;
                }
            }

            //找不到区域地址
            if(mailDeliveryMap==null){
                ifDistribution=true;
            }else {
                //不配送
                if(mailDeliveryMap.getString("isDelivery").equals("2")){
                    ifDistribution=true;
                }else{
                    //配送
                    BigDecimal weight=new BigDecimal(0);//重量
                    BigDecimal quantity=new BigDecimal(0);//件数
                    BigDecimal price=new BigDecimal(0);//价格
                    List<Map<String,Object>> templateGoodsList= (List<Map<String,Object>>)tm.get("templateGoodsList");
                    for (Map<String,Object> tg:templateGoodsList) {
                        //计算件数
                        quantity=quantity.add((BigDecimal) tg.get("quantity"));
                        //计算价格
                        price=price.add(((BigDecimal)tg.get("quantity")).multiply((BigDecimal) tg.get("price")));

                        GoodSpecification goodSpecification=iGoodSpecificationService.getById(tg.get("goodSpecificationId").toString());
                        if(goodSpecification.getWeight()!=null){
                            weight=weight.add(goodSpecification.getWeight().multiply((BigDecimal)tg.get("quantity")));
                        }
                    }

                    if(mailDeliveryMap.getString("isConditionalPostage").equals("true")){
                        //指定包邮
                        if(mailDeliveryMap.getString("unit").equals("0")||mailDeliveryMap.getString("unit").equals("件")){
                            //按件指定
                            if(quantity.doubleValue()>=new BigDecimal(mailDeliveryMap.getString("satisfyPriceNumber")).doubleValue()){
                                continue;
                            }
                        }

                        if(mailDeliveryMap.getString("unit").equals("1")||mailDeliveryMap.getString("unit").equals("元")){
                            //按件指定
                            if(price.doubleValue()>=new BigDecimal(mailDeliveryMap.getString("satisfyPriceNumber")).doubleValue()){
                                continue;
                            }
                        }
                    }
                    //非指定包邮和指定不满足
                    //按照重量计算运费
                    if(providerTemplate.getChargeMode().equals("1")){
                        if(StringUtils.isNotBlank(mailDeliveryMap.getString("price"))) {
                            freight = new BigDecimal(mailDeliveryMap.getString("price"));
                        }
                        if(StringUtils.isNotBlank(mailDeliveryMap.getString("count"))) {
                            weight = weight.subtract(new BigDecimal(mailDeliveryMap.getString("count")));
                        }
                        if(weight.doubleValue()>0){
                            freight=freight.add(weight.divide(new BigDecimal(mailDeliveryMap.getString("everyIncreaseCount")),RoundingMode.UP).multiply(new BigDecimal(mailDeliveryMap.getString("everyIncreasePrice"))));
                        }
                    }

                    //按照件数计算运费
                    if(providerTemplate.getChargeMode().equals("0")){
                        freight=new BigDecimal(mailDeliveryMap.getString("price"));
                        quantity=quantity.subtract(new BigDecimal(mailDeliveryMap.getString("count")));
                        if(quantity.doubleValue()>0){
                            freight=freight.add(quantity.divide(new BigDecimal(mailDeliveryMap.getString("everyIncreaseCount")),RoundingMode.UP).multiply(new BigDecimal(mailDeliveryMap.getString("everyIncreasePrice"))));
                        }
                    }

                }
            }
            //不配送
            if(ifDistribution){
                totalFreight=new BigDecimal(-1);
                break;
            }

           totalFreight=totalFreight.add(freight);
        }
        return totalFreight;
    }

    /**
     * 提交订单添加运费模板信息
     * @param goodsMap
     * @param sysAreaId
     * @param orderProviderList
     * @return
     */
    @Override
    public String addOrderTemplate(List<Map<String, Object>> goodsMap, String sysAreaId, OrderProviderList orderProviderList) {


        List<String> sysAreaIdList = Arrays.asList(sysAreaId.split(","));
        //获取省级id
        if(sysAreaIdList.size()>0){
            sysAreaId = sysAreaIdList.get(0);

        }
        SysArea sysArea = iSysAreaService.getById(sysAreaId);
        if(sysArea == null){
            return "";
        }

        //拆分运费模板
        List<Map<String,Object>> templateMaps= Lists.newArrayList();

        //设置配送
        boolean ifDistribution=false;

        for (Map<String,Object> g:goodsMap) {
            GoodList goodList=iGoodListService.getById(g.get("goodId").toString());
            Map<String,Object> templateMap=null;
            List<Map<String,Object>> templateGoodsList=null;

            //选取已有运费模板
            for (Map<String,Object> tm:templateMaps) {
                if(tm.get("id").toString().equals(goodList.getProviderTemplateId())){
                    templateMap=tm;
                    break;
                }
            }

            //加入供应商结构信息
            if(templateMap==null){
                templateMap= Maps.newHashMap();
                templateGoodsList=Lists.newArrayList();
                templateMap.put("templateGoodsList",templateGoodsList);
                templateMap.put("id",goodList.getProviderTemplateId());
                templateMaps.add(templateMap);
            }else{
                templateGoodsList=(List<Map<String,Object>>)templateMap.get("templateGoodsList");
            }
            //商品加入供应商中归类
            templateGoodsList.add(g);
        }

        //遍历运费模板
        for (Map<String,Object> tm:templateMaps) {
            //建立单模板运费
            BigDecimal freight=new BigDecimal(0);


            //判断是否包邮
            ProviderTemplate providerTemplate=this.getById(tm.get("id").toString());
            //运费模板为空即包邮
            if(providerTemplate==null){
                continue;
            }
            OrderProviderTemplate orderProviderTemplate = new OrderProviderTemplate();
            //免邮地区比较
            String exemptionPostage=providerTemplate.getExemptionPostage();
            List<String> stringList= Arrays.asList(StringUtils.split(exemptionPostage,","));
            boolean isFree=false;
            for (String s:stringList) {
                if(StringUtils.equals(s,sysAreaId)){
                    isFree=true;
                    break;
                }
            }
            //添加订单运费模板参数
            if(providerTemplate.getChargeMode().equals("0")){
                orderProviderTemplate.setChargeMode("按件数计费");
            }else{
                orderProviderTemplate.setChargeMode("按重量计费");
            }
            orderProviderTemplate.setOrderProviderListId(orderProviderList.getId());//供应商订单id
            orderProviderTemplate.setOrderListId(orderProviderList.getOrderListId());//平台订单id
            orderProviderTemplate.setSysUserId(providerTemplate.getSysUserId());//供应商id
            orderProviderTemplate.setProviderTemplateId(providerTemplate.getId());//运费模板id
            orderProviderTemplate.setTemplateName(providerTemplate.getName());//运费模板名称

            //配送
            BigDecimal weight=new BigDecimal(0);//重量
            BigDecimal quantity=new BigDecimal(0);//件数
            BigDecimal price=new BigDecimal(0);//价格
            List<Map<String,Object>> templateGoodsList= (List<Map<String,Object>>)tm.get("templateGoodsList");
            for (Map<String,Object> tg:templateGoodsList) {
                //计算件数
                quantity=quantity.add((BigDecimal) tg.get("quantity"));
                //计算价格
                price=price.add(((BigDecimal)tg.get("quantity")).multiply((BigDecimal) tg.get("price")));

                GoodSpecification goodSpecification=iGoodSpecificationService.getById(tg.get("goodSpecificationId").toString());
                if(goodSpecification.getWeight()!=null){
                    weight=weight.add(goodSpecification.getWeight().multiply((BigDecimal)tg.get("quantity")));
                }
            }

            //添加订单运费模板数量
            orderProviderTemplate.setQuantity(quantity);
            //免邮
            if(isFree){
                orderProviderTemplate.setAccountingRules("包邮");
               // continue;
            }else{

                //非包邮
                JSONArray areaTemplates= JSON.parseArray(providerTemplate.getMailDelivery());

                JSONObject mailDeliveryMap=null;

                for (Object a:areaTemplates) {
                    JSONObject aJsonObject=(JSONObject)a;
                    if(a!=null&aJsonObject.getString("id").equals(sysAreaId)){
                        mailDeliveryMap=aJsonObject;
                        break;
                    }
                }

                //找不到区域地址
                if(mailDeliveryMap==null){
                    orderProviderTemplate.setAccountingRules(sysArea.getName()+"不配送");
                    orderProviderTemplate.setShipFee(new BigDecimal("0"));

                }else {
                    //不配送
                    if(mailDeliveryMap.getString("isDelivery").equals("2")){
                        // isDelivery: 是否配送(1,配送; 2,不配送)
                        orderProviderTemplate.setAccountingRules(sysArea.getName()+"不配送");
                        orderProviderTemplate.setShipFee(new BigDecimal("0"));
                        ifDistribution=true;
                    }else{


                        if(mailDeliveryMap.getString("isConditionalPostage").equals("true")){
                            //指定包邮
                            if(mailDeliveryMap.getString("unit").equals("0")||mailDeliveryMap.getString("unit").equals("件")){
                                //按件指定
                                orderProviderTemplate.setAccountingRules("指定包邮: "+sysArea.getName()+"每"+mailDeliveryMap.getString("count")+"件"+mailDeliveryMap.getString("price")+"元,满"+mailDeliveryMap.getString("satisfyPriceNumber")+"件,包邮");
                            }

                            if(mailDeliveryMap.getString("unit").equals("1")||mailDeliveryMap.getString("unit").equals("元")){
                                //满多少元包邮
                                orderProviderTemplate.setAccountingRules("指定包邮: "+sysArea.getName()+"每"+mailDeliveryMap.getString("count")+"件"+mailDeliveryMap.getString("price")+"元,满"+mailDeliveryMap.getString("satisfyPriceNumber")+"元,包邮");

                            }
                        }
                        //非指定包邮和指定不满足
                        //按照重量计算运费
                        if(providerTemplate.getChargeMode().equals("1")){
                            if(StringUtils.isNotBlank(mailDeliveryMap.getString("price"))) {
                                freight = new BigDecimal(mailDeliveryMap.getString("price"));
                            }
                            if(StringUtils.isNotBlank(mailDeliveryMap.getString("count"))) {
                                weight = weight.subtract(new BigDecimal(mailDeliveryMap.getString("count")));
                            }
                            orderProviderTemplate.setAccountingRules("指定: "+sysArea.getName()+"每增加 "+mailDeliveryMap.getString("everyIncreaseCount")+"KG,加"+mailDeliveryMap.getString("everyIncreasePrice")+"元");
                            if(weight.doubleValue()>0){
                                freight=freight.add(weight.divide(new BigDecimal(mailDeliveryMap.getString("everyIncreaseCount")),RoundingMode.UP).multiply(new BigDecimal(mailDeliveryMap.getString("everyIncreasePrice"))));

                            }
                        }

                        //按照件数计算运费
                        if(providerTemplate.getChargeMode().equals("0")){
                            freight=new BigDecimal(mailDeliveryMap.getString("price"));
                            quantity=quantity.subtract(new BigDecimal(mailDeliveryMap.getString("count")));
                            orderProviderTemplate.setAccountingRules("按照件数计算运费: "+sysArea.getName()+mailDeliveryMap.getString("count")+"件内"+mailDeliveryMap.getString("price")+"元, 每增加"+mailDeliveryMap.getString("everyIncreaseCount")+"件加"+mailDeliveryMap.getString("everyIncreasePrice")+"元");

                            if(quantity.doubleValue()>0){//香港1件内2元，每增加1件加5元

                                freight=freight.add(quantity.divide(new BigDecimal(mailDeliveryMap.getString("everyIncreaseCount")),RoundingMode.UP).multiply(new BigDecimal(mailDeliveryMap.getString("everyIncreasePrice"))));
                                //orderProviderTemplate.setAccountingRules("指定: "+sysArea.getName()+"每增加 "+mailDeliveryMap.getString("everyIncreaseCount")+"件,加"+mailDeliveryMap.getString("everyIncreasePrice")+"元");
                            }
                        }

                    }
                }

            }


            //添加运费
            orderProviderTemplate.setShipFee(freight);
            //保存订单运费模板信息
            iOrderProviderTemplateService.saveOrUpdate(orderProviderTemplate);

        }
        return "success";
    }

    @Override
    public boolean opinionCalculate(Map<String, Object> goodMap, String sysAreaId) {

        GoodList goodList=iGoodListService.getById(goodMap.get("goodId").toString());

        boolean ifDistribution=false;

        //判断是否包邮
        ProviderTemplate providerTemplate=this.getById(goodList.getProviderTemplateId());
        //运费模板为空即包邮
        if(providerTemplate==null){
            return false;
        }
        //免邮地区比较
        String exemptionPostage=providerTemplate.getExemptionPostage();
        List<String> stringList= Arrays.asList(StringUtils.split(exemptionPostage,","));
        boolean isFree=false;
        for (String s:stringList) {
            if(StringUtils.equals(s,sysAreaId)){
                isFree=true;
                break;
            }
        }
        if(isFree){
            return true;
        }

        //非包邮
        JSONArray areaTemplates= JSON.parseArray(providerTemplate.getMailDelivery());

        JSONObject mailDeliveryMap=null;

        for (Object a:areaTemplates) {
            JSONObject aJsonObject=(JSONObject)a;
            if(a!=null&aJsonObject.getString("id").equals(sysAreaId)){
                mailDeliveryMap=aJsonObject;
                break;
            }
        }

        //找不到区域地址
        if(mailDeliveryMap==null){
            ifDistribution=true;
        }else {
            //不配送
            if(mailDeliveryMap.getString("isDelivery").equals("2")){
                ifDistribution=true;
            }
        }
        //不配送
        if(ifDistribution){
            return false;
        }
        return true;
    }

    /**
     * 获取订单运费模板信息
     *
     * @param orderProviderIdList
     * @return
     */
    @Override
   public List<Map<String,Object>> getProviderTemplateMaps(List<String> orderProviderIdList){
       return  baseMapper.getProviderTemplateMaps(orderProviderIdList);
   }
}
