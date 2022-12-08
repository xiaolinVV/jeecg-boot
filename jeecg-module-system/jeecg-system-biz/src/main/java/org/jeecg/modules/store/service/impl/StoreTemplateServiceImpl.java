package org.jeecg.modules.store.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.good.entity.GoodStoreList;
import org.jeecg.modules.good.entity.GoodStoreSpecification;
import org.jeecg.modules.good.service.IGoodStoreListService;
import org.jeecg.modules.good.service.IGoodStoreSpecificationService;
import org.jeecg.modules.order.entity.OrderStoreSubList;
import org.jeecg.modules.order.entity.OrderStoreTemplate;
import org.jeecg.modules.order.service.IOrderStoreTemplateService;
import org.jeecg.modules.store.dto.StoreTemplateDTO;
import org.jeecg.modules.store.entity.StoreTemplate;
import org.jeecg.modules.store.mapper.StoreTemplateMapper;
import org.jeecg.modules.store.service.IStoreTemplateService;
import org.jeecg.modules.system.entity.SysArea;
import org.jeecg.modules.system.mapper.SysUserMapper;
import org.jeecg.modules.system.service.ISysAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Description: 运费模板
 * @Author: jeecg-boot
 * @Date:   2019-10-21
 * @Version: V1.0
 */
@Service
public class StoreTemplateServiceImpl extends ServiceImpl<StoreTemplateMapper, StoreTemplate> implements IStoreTemplateService {
    @Autowired(required = false)
    private StoreTemplateMapper storeTemplateMapper;
    @Autowired(required = false)
    private SysUserMapper sysUserMapper;


    @Autowired
    private IGoodStoreListService iGoodStoreListService;

    @Autowired
    private IGoodStoreSpecificationService iGoodStoreSpecificationService;
    @Autowired
    private ISysAreaService iSysAreaService;
    @Autowired
    private IOrderStoreTemplateService iOrderStoreTemplateService;

    @Override
    public boolean updateIsTemplateById(StoreTemplate storeTemplate) {
        try {
            if (storeTemplate.getSysUserId()==null||"".equals(storeTemplate.getSysUserId())){
                LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
                String userId = sysUser.getId();
                storeTemplate.setSysUserId(userId);
            }
            QueryWrapper<StoreTemplate> storeTemplateQueryWrapper = new QueryWrapper<>();
            storeTemplateQueryWrapper.eq("sys_user_id",storeTemplate.getSysUserId());
            List<StoreTemplate> storeTemplates = storeTemplateMapper.selectList(storeTemplateQueryWrapper);
            for (StoreTemplate template : storeTemplates) {
                if ("1".equals(template.getIsTemplate())){
                    String id = template.getSysUserId();
                    storeTemplateMapper.updateIsTemplateById(id);
                }
            }
            this.saveOrUpdate(storeTemplate);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<StoreTemplateDTO> getlistTemplate(String uId) {
        return storeTemplateMapper.getListStoreTemplate(uId);

    }

    @Override
    public BigDecimal calculateFreight(List<Map<String, Object>> goodsMap, String sysAreaId) {

        List<String> sysAreaIdList = Arrays.asList(sysAreaId.split(","));
        //获取省级id
        if(sysAreaIdList.size()>0){
            sysAreaId = sysAreaIdList.get(0);

        }
        BigDecimal totalFreight=new BigDecimal(0);
        //拆分运费模板
        List<Map<String,Object>> templateMaps= Lists.newArrayList();

        //设置配送
        boolean ifDistribution=false;

        for (Map<String,Object> g:goodsMap) {
            GoodStoreList goodStoreList=iGoodStoreListService.getById(g.get("goodId").toString());
            Map<String,Object> templateMap=null;
            List<Map<String,Object>> templateGoodsList=null;

            //选取已有运费模板
            for (Map<String,Object> tm:templateMaps) {
                if(tm.get("id")!=null&&tm.get("id").toString().equals(goodStoreList.getStoreTemplateId())){
                    templateMap=tm;
                    break;
                }
            }

            //加入供应商结构信息
            if(templateMap==null){
                templateMap= Maps.newHashMap();
                templateGoodsList=Lists.newArrayList();
                templateMap.put("templateGoodsList",templateGoodsList);
                templateMap.put("id",goodStoreList.getStoreTemplateId());
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

            if(tm.get("id")==null){
                continue;
            }

            //判断是否包邮
            StoreTemplate storeTemplate=this.getById(tm.get("id").toString());
            //运费模板为空即包邮
            if(storeTemplate==null){
                continue;
            }
            //免邮地区比较
            String exemptionPostage=storeTemplate.getExemptionPostage();
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
            JSONArray areaTemplates= JSON.parseArray(storeTemplate.getMailDelivery());

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

                        GoodStoreSpecification goodStoreSpecification=iGoodStoreSpecificationService.getById(tg.get("goodSpecificationId").toString());
                        if(goodStoreSpecification.getWeight()!=null){
                            weight=weight.add(goodStoreSpecification.getWeight().multiply((BigDecimal)tg.get("quantity")));
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
                    if(storeTemplate.getChargeMode().equals("1")){
                        if(StringUtils.isNotBlank(mailDeliveryMap.getString("price"))) {
                            freight = new BigDecimal(mailDeliveryMap.getString("price"));
                        }
                        if(StringUtils.isNotBlank(mailDeliveryMap.getString("count"))) {
                            weight = weight.subtract(new BigDecimal(mailDeliveryMap.getString("count")));
                        }
                        if(weight.doubleValue()>0&&new BigDecimal(mailDeliveryMap.getString("everyIncreaseCount")).doubleValue()>0&&new BigDecimal(mailDeliveryMap.getString("everyIncreasePrice")).doubleValue()>0){
                            freight=freight.add(weight.divide(new BigDecimal(mailDeliveryMap.getString("everyIncreaseCount")), RoundingMode.UP).multiply(new BigDecimal(mailDeliveryMap.getString("everyIncreasePrice"))));
                        }
                    }

                    //按照件数计算运费
                    if(storeTemplate.getChargeMode().equals("0")){
                        freight=new BigDecimal(mailDeliveryMap.getString("price"));
                        quantity=quantity.subtract(new BigDecimal(mailDeliveryMap.getString("count")));
                        if(quantity.doubleValue()>0&&new BigDecimal(mailDeliveryMap.getString("everyIncreaseCount")).doubleValue()>0&&new BigDecimal(mailDeliveryMap.getString("everyIncreasePrice")).doubleValue()>0){
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

    @Override
    public boolean opinionCalculate(Map<String, Object> goodMap, String sysAreaId) {
        GoodStoreList goodList=iGoodStoreListService.getById(goodMap.get("goodId").toString());

        boolean ifDistribution=false;

        //判断是否包邮
        StoreTemplate storeTemplate=this.getById(goodList.getStoreTemplateId());
        //运费模板为空即包邮
        if(storeTemplate==null){
            return false;
        }
        //免邮地区比较
        String exemptionPostage=storeTemplate.getExemptionPostage();
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
        JSONArray areaTemplates= JSON.parseArray(storeTemplate.getMailDelivery());

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
     * 提交店铺订单添加运费模板信息
     * @param goodsMap
     * @param sysAreaId
     * @param orderStoreSubList
     * @return
     */

    public String addOrderStoreTemplate(List<Map<String, Object>> goodsMap, String sysAreaId, OrderStoreSubList orderStoreSubList){

        List<String> sysAreaIdList = Arrays.asList(sysAreaId.split(","));
        //获取省级id
        if(sysAreaIdList.size()>0){
            sysAreaId = sysAreaIdList.get(0);

        }
        SysArea sysArea = iSysAreaService.getById(sysAreaId);
        if(sysArea == null){
            return "";
        }
        BigDecimal totalFreight=new BigDecimal(0);
        //拆分运费模板
        List<Map<String,Object>> templateMaps= Lists.newArrayList();

        //设置配送
        boolean ifDistribution=false;

        for (Map<String,Object> g:goodsMap) {
            GoodStoreList goodStoreList=iGoodStoreListService.getById(g.get("goodId").toString());
            Map<String,Object> templateMap=null;
            List<Map<String,Object>> templateGoodsList=null;

            //选取已有运费模板
            for (Map<String,Object> tm:templateMaps) {
                if(tm.get("id")!=null&&tm.get("id").toString().equals(goodStoreList.getStoreTemplateId())){
                    templateMap=tm;
                    break;
                }
            }

            //加入供应商结构信息
            if(templateMap==null){
                templateMap= Maps.newHashMap();
                templateGoodsList=Lists.newArrayList();
                templateMap.put("templateGoodsList",templateGoodsList);
                templateMap.put("id",goodStoreList.getStoreTemplateId());
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

            if(tm.get("id")==null){
                continue;
            }
            OrderStoreTemplate orderStoreTemplate = new OrderStoreTemplate();
            //判断是否包邮
            StoreTemplate storeTemplate=this.getById(tm.get("id").toString());
            //运费模板为空即包邮
            if(storeTemplate==null){
                continue;
            }
            //免邮地区比较
            String exemptionPostage=storeTemplate.getExemptionPostage();
            List<String> stringList= Arrays.asList(StringUtils.split(exemptionPostage,","));
            boolean isFree=false;
            for (String s:stringList) {
                if(StringUtils.equals(s,sysAreaId)){
                    isFree=true;
                    break;
                }
            }

            //添加订单运费模板参数
            if(storeTemplate.getChargeMode().equals("0")){
                orderStoreTemplate.setChargeMode("按件数计费");
            }else{
                orderStoreTemplate.setChargeMode("按重量计费");
            }
            orderStoreTemplate.setOrderStoreSubListId(orderStoreSubList.getId());//店铺子订单id
            orderStoreTemplate.setOrderStoreListId(orderStoreSubList.getOrderStoreListId());//店铺订单id
            orderStoreTemplate.setSysUserId(storeTemplate.getSysUserId());//供应商id
            orderStoreTemplate.setStoreTemplateId(storeTemplate.getId());//运费模板id
            orderStoreTemplate.setTemplateName(storeTemplate.getName());//运费模板名称
            BigDecimal weight=new BigDecimal(0);//重量
            BigDecimal quantity=new BigDecimal(0);//件数
            BigDecimal price=new BigDecimal(0);//价格
            List<Map<String,Object>> templateGoodsList= (List<Map<String,Object>>)tm.get("templateGoodsList");
            for (Map<String,Object> tg:templateGoodsList) {
                //计算件数
                quantity=quantity.add((BigDecimal) tg.get("quantity"));
                //计算价格
                price=price.add(((BigDecimal)tg.get("quantity")).multiply((BigDecimal) tg.get("price")));

                GoodStoreSpecification goodStoreSpecification=iGoodStoreSpecificationService.getById(tg.get("goodSpecificationId").toString());
                if(goodStoreSpecification.getWeight()!=null){
                    weight=weight.add(goodStoreSpecification.getWeight().multiply((BigDecimal)tg.get("quantity")));
                }
            }
           //添加订单运费模板数量
            orderStoreTemplate.setQuantity(quantity);

            //免邮
            if(isFree){
                orderStoreTemplate.setAccountingRules("免邮");

                // continue;
            }else{
                //非包邮
                JSONArray areaTemplates= JSON.parseArray(storeTemplate.getMailDelivery());

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
                    orderStoreTemplate.setAccountingRules(sysArea.getName()+"不配送");
                    orderStoreTemplate.setShipFee(new BigDecimal("0"));
                    ifDistribution=true;
                }else {
                    //不配送
                    if(mailDeliveryMap.getString("isDelivery").equals("2")){
                        orderStoreTemplate.setAccountingRules(sysArea.getName()+"不配送");
                        orderStoreTemplate.setShipFee(new BigDecimal("0"));
                        ifDistribution=true;
                    }else{
                        //配送


                        if(mailDeliveryMap.getString("isConditionalPostage").equals("true")){
                            //指定包邮
                            if(mailDeliveryMap.getString("unit").equals("0")||mailDeliveryMap.getString("unit").equals("件")){
                                //按件指定
                                orderStoreTemplate.setAccountingRules("指定包邮: "+sysArea.getName()+"每"+mailDeliveryMap.getString("count")+"件"+mailDeliveryMap.getString("price")+"元,满"+mailDeliveryMap.getString("satisfyPriceNumber")+"件,包邮");
                            }

                            if(mailDeliveryMap.getString("unit").equals("1")||mailDeliveryMap.getString("unit").equals("元")){
                                //满多少元包邮
                                orderStoreTemplate.setAccountingRules("指定包邮: "+sysArea.getName()+"每"+mailDeliveryMap.getString("count")+"件"+mailDeliveryMap.getString("price")+"元,满"+mailDeliveryMap.getString("satisfyPriceNumber")+"元,包邮");

                            }


                        }
                        //非指定包邮和指定不满足
                        //按照重量计算运费
                        if(storeTemplate.getChargeMode().equals("1")){
                            if(StringUtils.isNotBlank(mailDeliveryMap.getString("price"))) {
                                freight = new BigDecimal(mailDeliveryMap.getString("price"));
                            }
                            if(StringUtils.isNotBlank(mailDeliveryMap.getString("count"))) {
                                weight = weight.subtract(new BigDecimal(mailDeliveryMap.getString("count")));
                            }
                            orderStoreTemplate.setAccountingRules("指定: "+sysArea.getName()+"每增加 "+mailDeliveryMap.getString("everyIncreaseCount")+"KG,加"+mailDeliveryMap.getString("everyIncreasePrice")+"元");

                            if(weight.doubleValue()>0&&new BigDecimal(mailDeliveryMap.getString("everyIncreaseCount")).doubleValue()>0&&new BigDecimal(mailDeliveryMap.getString("everyIncreasePrice")).doubleValue()>0){
                                freight=freight.add(weight.divide(new BigDecimal(mailDeliveryMap.getString("everyIncreaseCount")), RoundingMode.UP).multiply(new BigDecimal(mailDeliveryMap.getString("everyIncreasePrice"))));
                            }
                        }

                        //按照件数计算运费
                        if(storeTemplate.getChargeMode().equals("0")){
                            freight=new BigDecimal(mailDeliveryMap.getString("price"));
                            quantity=quantity.subtract(new BigDecimal(mailDeliveryMap.getString("count")));
                            orderStoreTemplate.setAccountingRules("按照件数计算运费: "+sysArea.getName()+mailDeliveryMap.getString("count")+"件内"+mailDeliveryMap.getString("price")+"元, 每增加"+mailDeliveryMap.getString("everyIncreaseCount")+"件加"+mailDeliveryMap.getString("everyIncreasePrice")+"元");

                            if(quantity.doubleValue()>0&&new BigDecimal(mailDeliveryMap.getString("everyIncreaseCount")).doubleValue()>0&&new BigDecimal(mailDeliveryMap.getString("everyIncreasePrice")).doubleValue()>0){
                                freight=freight.add(quantity.divide(new BigDecimal(mailDeliveryMap.getString("everyIncreaseCount")),RoundingMode.UP).multiply(new BigDecimal(mailDeliveryMap.getString("everyIncreasePrice"))));
                            }
                        }

                    }
                }
            }



            //添加运费
            orderStoreTemplate.setShipFee(freight);
            //保存订单运费模板信息
            iOrderStoreTemplateService.saveOrUpdate(orderStoreTemplate);
        }
        return "success";
    }
    /**
     * 查询店铺运费模板信息
     * @param orderStoreSubListIdList
     * @return
     */
    public List<Map<String,Object>> getStoreTemplateMaps(@Param("orderStoreSubListIdList") List<String> orderStoreSubListIdList){
      return baseMapper.getStoreTemplateMaps(orderStoreSubListIdList);
    }
}
