package org.jeecg.modules.marketing.api;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.ijpay.core.kit.QrCodeKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.UUIDGenerator;
import org.jeecg.modules.good.entity.GoodList;
import org.jeecg.modules.good.entity.GoodSpecification;
import org.jeecg.modules.good.entity.GoodStoreList;
import org.jeecg.modules.good.entity.GoodStoreSpecification;
import org.jeecg.modules.good.service.IGoodListService;
import org.jeecg.modules.good.service.IGoodSpecificationService;
import org.jeecg.modules.good.service.IGoodStoreListService;
import org.jeecg.modules.good.service.IGoodStoreSpecificationService;
import org.jeecg.modules.map.utils.TengxunMapUtils;
import org.jeecg.modules.marketing.entity.*;
import org.jeecg.modules.marketing.service.*;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.service.IMemberListService;
import org.jeecg.modules.store.entity.StoreManage;
import org.jeecg.modules.store.service.IStoreManageService;
import org.jeecg.modules.system.entity.SysFrontSetting;
import org.jeecg.modules.system.service.ISysFrontSettingService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 兑换券api
 */
@RequestMapping("after/marketingCertificateRecord")
@Controller
@Slf4j
public class AfterMarketingCertificateRecordController {

    @Autowired
    private IMarketingCertificateRecordService iMarketingCertificateRecordService;

    @Autowired
    private IStoreManageService iStoreManageService;

    @Autowired
    private IMarketingCertificateGoodService iMarketingCertificateGoodService;

    @Autowired
    private IMemberListService iMemberListService;

    @Autowired
    private IGoodStoreListService iGoodStoreListService;
    @Autowired
    private IGoodListService iGoodListService;
    @Autowired
    private IMarketingDiscountService iMarketingDiscountService;

    @Autowired
    private IMarketingCertificateStoreService iMarketingCertificateStoreService;

    @Autowired
    private IGoodStoreSpecificationService iGoodStoreSpecificationService;


    @Autowired
    private IGoodSpecificationService iGoodSpecificationService;


    @Autowired
    private TengxunMapUtils tengxunMapUtils;

    @Value(value = "${jeecg.path.upload}")
    private String uploadpath;
    @Autowired
    private ISysFrontSettingService iSysFrontSettingService;
    @Autowired
    private IMarketingCertificateSeckillBaseSettingService iMarketingCertificateSeckillBaseSettingService;
    @Autowired
    private IMarketingCertificateGroupBaseSettingService iMarketingCertificateGroupBaseSettingService;
    /**
     * 挂起赠送接口
     * @param id
     * @param request
     * @return
     */
    @RequestMapping("senderMarketingCertificateRecord")
    @ResponseBody
    public Result<String> senderMarketingCertificateRecord(String id,
                                                           HttpServletRequest request){
        String memberId= request.getAttribute("memberId").toString();
        Result<String> result=new Result<>();
        if(StringUtils.isBlank(id)){
            result.setCode(369);
            result.setSuccess(false);
            result.setMessage("id不能为空！！！");
            return result;
        }
        MarketingCertificateRecord marketingCertificateRecord=iMarketingCertificateRecordService.getById(id);
        if(!memberId.equals(marketingCertificateRecord.getMemberListId())){
            result.setCode(369);
            result.setSuccess(false);
            result.setMessage("非本人兑换券不可赠送");
            return result;
        }
        if (marketingCertificateRecord.getStatus().equals("3")
                ||marketingCertificateRecord.getStatus().equals("4")
                ||marketingCertificateRecord.getStatus().equals("5")
                ||marketingCertificateRecord.getStatus().equals("6")){
            result.setCode(369);
            result.setSuccess(false);
            result.setMessage("该券状态不支持赠送");
            return result;
        }
        //改变券的状态为已赠送
        /*marketingCertificateRecord.setStatus("5");
        marketingCertificateRecord.setSendTime(new Date());

        iMarketingCertificateRecordService.saveOrUpdate(marketingCertificateRecord);*/

        result.success("送出兑换券成功");
        return result;
    }


    /**
     * 赠送兑换券
     *
     * @param giveMemberId
     * @param request
     * @return
     */
    @RequestMapping("giveAsMarketingCertificateRecord")
    @ResponseBody
    public Result<String> giveAsMarketingCertificateRecord(String id,
                                                           String giveMemberId,
                                                           HttpServletRequest request){
        String memberId= request.getAttribute("memberId").toString();
        Result<String> result=new Result<>();
        //参数校验
        if(StringUtils.isBlank(giveMemberId)){
            result.setCode(369);
            result.setSuccess(false);
            result.setMessage("giveMemberId不能为空！！！");
            return result;
        }

        if(StringUtils.isBlank(id)){
            result.setCode(369);
            result.setSuccess(false);
            result.setMessage("id不能为空！！！");
            return result;
        }

        MarketingCertificateRecord marketingCertificateRecord=iMarketingCertificateRecordService.getById(id);
        if(!giveMemberId.equals(marketingCertificateRecord.getMemberListId())){
            result.setCode(369);
            result.setSuccess(false);
            result.setMessage("您的手慢了。该券已被其他用户领取了。");
            return result;
        }
        boolean b = false;
        if (iMarketingCertificateRecordService.count(new LambdaQueryWrapper<MarketingCertificateRecord>()
                .eq(MarketingCertificateRecord::getMemberListId,memberId)
                .eq(MarketingCertificateRecord::getQqzixuangu,marketingCertificateRecord.getQqzixuangu())
        )<=0){
            MarketingCertificateRecord receiveMarketingCertificateRecord = new MarketingCertificateRecord();
            BeanUtils.copyProperties(marketingCertificateRecord,receiveMarketingCertificateRecord);
            receiveMarketingCertificateRecord.setId(UUIDGenerator.generate());
            receiveMarketingCertificateRecord.setCreateTime(new Date());
            String ctxPath = uploadpath;
            String fileName = null;
            String bizPath = "files";
            String nowday = new SimpleDateFormat("yyyyMMdd").format(new Date());
            File file = new File(ctxPath + File.separator + bizPath + File.separator + nowday);
            if (!file.exists()) {
                file.mkdirs();// 创建文件根目录
            }

            String orgName = "qrMarketingCertificate.png";// 获取文件名
            fileName = orgName.substring(0, orgName.lastIndexOf(".")) + "_" + System.currentTimeMillis() + orgName.substring(orgName.indexOf("."));
            String savePath = file.getPath() + File.separator + fileName;

            Boolean encode = QrCodeKit.encode(receiveMarketingCertificateRecord.getId(), BarcodeFormat.QR_CODE, 3, ErrorCorrectionLevel.H, "png", 200, 200,
                    savePath);
            String dbpath = bizPath + File.separator + nowday + File.separator + fileName;
            //券二维码
            receiveMarketingCertificateRecord.setQrAddr(dbpath);
            receiveMarketingCertificateRecord.setMemberListId(memberId);
            b = iMarketingCertificateRecordService.save(receiveMarketingCertificateRecord);
        }
        if (b){
            marketingCertificateRecord.setGiveMemberListId(memberId);
            marketingCertificateRecord.setStatus("5");
            marketingCertificateRecord.setSendTime(new Date());
            iMarketingCertificateRecordService.saveOrUpdate(marketingCertificateRecord);
            result.success("领取成功!");
        }else {
            result.error500("领取失败!");
        }

        return result;
    }


    /**
     *
     * @param pattern  兑换券状态；1：未使用；2：已使用；3：已过期；4：已失效;
     * @param pageNo
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping("findMarketingCertificateRecordByMemberId")
    @ResponseBody
    public Result<IPage<Map<String,Object>>> findMarketingCertificateRecordByMemberId(Integer pattern,
                                                                                      @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                                                      @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                                                      HttpServletRequest request){
        String memberId= request.getAttribute("memberId").toString();
        Result<IPage<Map<String,Object>>> result=new Result<>();
        //组织查询参数
        Page<Map<String,Object>> page = new Page<Map<String,Object>>(pageNo, pageSize);
        Map<String,Object> paramMap= Maps.newHashMap();
        paramMap.put("memberId",memberId);
        paramMap.put("pattern",pattern);
        IPage<Map<String, Object>> marketingCertificateRecordByMemberId = iMarketingCertificateRecordService.findMarketingCertificateRecordByMemberId(page, paramMap);

        String frontLogo = iSysFrontSettingService.list(new LambdaQueryWrapper<SysFrontSetting>()
                .eq(SysFrontSetting::getDelFlag, "0")
        ).get(0).getFrontLogo();

        marketingCertificateRecordByMemberId.getRecords().forEach(mcrb->{
            if (StringUtils.isBlank(mcrb.get("mainPicture").toString())){
                mcrb.put("mainPicture",frontLogo);
            }
        });
        result.setResult(marketingCertificateRecordByMemberId);

        result.success("查询会员兑换券列表");
        return result;
    }


    /**
     * 根据id获取优惠券详情
     * @param id
     * @return
     */
    @RequestMapping("findMarketingCertificateRecordInfo")
    @ResponseBody
    public  Result<Map<String,Object>> findMarketingCertificateRecordInfo(String id
            ,String location){
        Result<Map<String,Object>> result=new Result<>();

        //参数验证
        if(StringUtils.isBlank(id)){
            result.error500("id不能为空！！！");
            return result;
        }
        Map<String,Object> marketingCertificateRecord =iMarketingCertificateRecordService.findMarketingCertificateRecordInfo(id);
        log.info("会员经纬度: " + location);
        List<Map<String,Object>> stores=Lists.newArrayList();
        Map<String,Object> mapsMap=Maps.newHashMap();
        marketingCertificateRecord.put("stores",stores);
        if (marketingCertificateRecord.get("isBuyPlatform").equals("0")){
            LambdaQueryWrapper<StoreManage> storeManageLambdaQueryWrapper = new LambdaQueryWrapper<StoreManage>()
                    .eq(StoreManage::getSysUserId, marketingCertificateRecord.get("sysUserId"))
                    .in(StoreManage::getPayStatus, "1", "2");
            if (iStoreManageService.count(storeManageLambdaQueryWrapper)>0){
                Map<String,Object> storeMap=Maps.newHashMap();
                StoreManage storeManage = iStoreManageService.list(storeManageLambdaQueryWrapper).get(0);
                storeMap.put("sysUserId", storeManage.getSysUserId());
                if (storeManage.getSubStoreName() == null) {
                    storeMap.put("storeName", storeManage.getStoreName());
                } else {
                    storeMap.put("storeName", storeManage.getStoreName() + "(" + storeManage.getStoreName() + ")");
                }
                storeMap.put("storeAddress", storeManage.getStoreAddress());
                storeMap.put("takeOutPhone", storeManage.getTakeOutPhone());
                storeMap.put("logoAddr", storeManage.getLogoAddr());
                storeMap.put("latitude",storeManage.getLatitude());
                storeMap.put("longitude",storeManage.getLongitude());
                stores.add(storeMap);
                mapsMap.put(storeManage.getLatitude() + "," + storeManage.getLongitude(), storeMap);
            }
        }else {
            QueryWrapper<MarketingCertificateStore> marketingCertificateStoreQueryWrapper=new QueryWrapper<>();
            marketingCertificateStoreQueryWrapper.eq("marketing_certificate_id",marketingCertificateRecord.get("marketingCertificateId"));
            List<MarketingCertificateStore> marketingCertificateStores=iMarketingCertificateStoreService.list(marketingCertificateStoreQueryWrapper);

            for (MarketingCertificateStore mcf:marketingCertificateStores) {
                Map<String,Object> storeMap=Maps.newHashMap();
                QueryWrapper<StoreManage> storeManageQueryWrapper = new QueryWrapper<>();
                storeManageQueryWrapper.eq("sys_user_id", mcf.getSysUserId());
                storeManageQueryWrapper.in("pay_status", "1","2");
                if(iStoreManageService.count(storeManageQueryWrapper)>0) {
                    StoreManage storeManage = iStoreManageService.list(storeManageQueryWrapper).get(0);
                    storeMap.put("sysUserId", mcf.getSysUserId());
                    if (storeManage.getSubStoreName() == null) {
                        storeMap.put("storeName", storeManage.getStoreName());
                    } else {
                        storeMap.put("storeName", storeManage.getStoreName() + "(" + storeManage.getStoreName() + ")");
                    }
                    storeMap.put("storeAddress", storeManage.getStoreAddress());
                    storeMap.put("takeOutPhone", storeManage.getTakeOutPhone());
                    storeMap.put("logoAddr", storeManage.getLogoAddr());
                    storeMap.put("latitude",storeManage.getLatitude());
                    storeMap.put("longitude",storeManage.getLongitude());
                    stores.add(storeMap);
                    mapsMap.put(storeManage.getLatitude() + "," + storeManage.getLongitude(), storeMap);
                }
            }
        }

        long goods = iMarketingCertificateGoodService.count(new LambdaQueryWrapper<MarketingCertificateGood>()
                .eq(MarketingCertificateGood::getMarketingCertificateId, marketingCertificateRecord.get("marketingCertificateId")));
        marketingCertificateRecord.put("goods",goods);
        //兑换方式
        if (marketingCertificateRecord.get("certificateType").equals("0")) {
            marketingCertificateRecord.put("certificate", "可兑换以下商品");
        } else if (marketingCertificateRecord.get("certificateType").equals("1")) {
            marketingCertificateRecord.put("certificate", "以下商品任选其一");
        }
        if (goods < 1) {
            marketingCertificateRecord.put("certificate", "");
        }
        marketingCertificateRecord.put("goodList",iMarketingCertificateGoodService.findMarketingCertificateSeckillListGoodByCertificateId(String.valueOf(marketingCertificateRecord.get("marketingCertificateId"))));
        if (marketingCertificateRecord.get("recordType").equals("0")){
            marketingCertificateRecord.put("anotherName","");
            marketingCertificateRecord.put("label","");
        }else if (marketingCertificateRecord.get("recordType").equals("1")){
            MarketingCertificateSeckillBaseSetting marketingCertificateSeckillBaseSetting = iMarketingCertificateSeckillBaseSettingService.getOne(new LambdaQueryWrapper<MarketingCertificateSeckillBaseSetting>()
                    .eq(MarketingCertificateSeckillBaseSetting::getDelFlag, "0"));
            marketingCertificateRecord.put("anotherName",marketingCertificateSeckillBaseSetting.getAnotherName());
            marketingCertificateRecord.put("label",marketingCertificateSeckillBaseSetting.getLabel());
        }else if (marketingCertificateRecord.get("recordType").equals("2")){
            MarketingCertificateGroupBaseSetting marketingCertificateGroupBaseSetting = iMarketingCertificateGroupBaseSettingService.getOne(new LambdaQueryWrapper<MarketingCertificateGroupBaseSetting>()
                    .eq(MarketingCertificateGroupBaseSetting::getDelFlag, "0"));
            marketingCertificateRecord.put("anotherName",marketingCertificateGroupBaseSetting.getAnotherName());
            marketingCertificateRecord.put("label",marketingCertificateGroupBaseSetting.getLabel());
        }
        if(StringUtils.isNotBlank(location)){
            JSONArray mapJsonArray= JSON.parseArray(tengxunMapUtils.findDistance(location,StringUtils.join(mapsMap.keySet(),";")));
            if(mapJsonArray!=null) {
                mapJsonArray.stream().forEach(mj -> {
                    JSONObject jb = (JSONObject) mj;
                    Map<String, Object> s = (Map<String, Object>) mapsMap.get(StringUtils.substringBefore(jb.getJSONObject("to").getString("lat"), ".") + "."
                                    + StringUtils.rightPad(StringUtils.substringAfter(jb.getJSONObject("to").getString("lat"), "."), 6, "0") + ","
                            + StringUtils.substringBefore(jb.getJSONObject("to").getString("lng"), ".") + "."
                            + StringUtils.rightPad(StringUtils.substringAfter(jb.getJSONObject("to").getString("lng"), "."), 6, "0"));
                    BigDecimal dis=new BigDecimal(jb.getString("distance"));
                    if(dis.doubleValue()>1000){
                        s.put("distance", dis.divide(new BigDecimal(1000),2) + "km");
                    }else{
                        s.put("distance", dis + "m");
                    }
                });
            }
        }

        if(marketingCertificateRecord.get("qrAddr")==null) {
            String ctxPath = uploadpath;
            String fileName = null;
            String bizPath = "files";
            String nowday = new SimpleDateFormat("yyyyMMdd").format(new Date());
            File file = new File(ctxPath + File.separator + bizPath + File.separator + nowday);
            if (!file.exists()) {
                file.mkdirs();// 创建文件根目录
            }

            String orgName = "qrMarketingCertificate.png";// 获取文件名
            fileName = orgName.substring(0, orgName.lastIndexOf(".")) + "_" + System.currentTimeMillis() + orgName.substring(orgName.indexOf("."));
            String savePath = file.getPath() + File.separator + fileName;

            Boolean encode = QrCodeKit.encode(marketingCertificateRecord.get("id").toString(), BarcodeFormat.QR_CODE, 3, ErrorCorrectionLevel.H, "png", 200, 200,
                    savePath);
            String dbpath = bizPath + File.separator + nowday + File.separator + fileName;
            //券二维码
            marketingCertificateRecord.put("qrAddr", dbpath);
            MarketingCertificateRecord marketingCertificateRecord1=iMarketingCertificateRecordService.getById(marketingCertificateRecord.get("id").toString());
            marketingCertificateRecord1.setQrAddr(dbpath);
            iMarketingCertificateRecordService.saveOrUpdate(marketingCertificateRecord1);
        }
        result.setResult(marketingCertificateRecord);
        result.success("获取券详情信息");
        return result;
    }


    /**
     * 根据id获取兑换券券详情
     * @param id
     * @return
     */
    @RequestMapping("findMarketingCertificateRecordInfoNew")
    @ResponseBody
    public  Result<Map<String,Object>> findMarketingCertificateRecordInfoNew(String id,
                                                                             String location,
                                                                             @RequestHeader(defaultValue = "") String sysUserId,
                                                                             @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                                             @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize){
        Result<Map<String,Object>> result=new Result<>();
        //参数验证
        if(StringUtils.isBlank(id)){
            result.error500("id不能为空！！！");
            return result;
        }
        //获取兑换券记录
        Map<String,Object> marketingCertificateRecord =iMarketingCertificateRecordService.findMarketingCertificateRecordInfo(id);
        Page<Map<String, Object>> page = new Page<>(pageNo,pageSize);
        HashMap<String, Object> map = new HashMap<>();
        if (StringUtils.isNotBlank(location)){
            List<String> locations = Arrays.asList(StringUtils.split(location, ","));
            map.put("longitude",locations.get(1));
            map.put("latitude",locations.get(0));
        }else {
            map.put("longitude",0);
            map.put("latitude",0);
        }
        if (marketingCertificateRecord.get("rewardStore").equals("0")){
            map.put("id","");
        }
        if (marketingCertificateRecord.get("rewardStore").equals("1")){
            map.put("id",marketingCertificateRecord.get("marketingCertificateId"));
        }
        if (marketingCertificateRecord.get("sellRewardStore").equals("1")&&marketingCertificateRecord.get("sysUserId")!=null){
            map.put("sysUserId",marketingCertificateRecord.get("sysUserId").toString());
        }else {
            map.put("sysUserId","");
        }
        IPage<Map<String, Object>> marketingCertificateId = iMarketingCertificateStoreService.findstoreById(page,map);
        Map<String,Object> mapsMap=Maps.newHashMap();
        marketingCertificateId.getRecords().forEach(mcs->{
            mapsMap.put(mcs.get("latitude")+","+mcs.get("longitude"),mcs);
        });
        marketingCertificateRecord.put("stores",marketingCertificateId);
        if(StringUtils.isNotBlank(location)){
            JSONArray mapJsonArray=JSON.parseArray(tengxunMapUtils.findDistance(location,StringUtils.join(mapsMap.keySet(),";")));
            if(mapJsonArray!=null) {
                mapJsonArray.forEach(mj -> {
                    JSONObject jb = (JSONObject) mj;
                    Map<String, Object> s = (Map<String, Object>) mapsMap.get(StringUtils.substringBefore(jb.getJSONObject("to").getString("lat"), ".") + "."
                            + StringUtils.rightPad(StringUtils.substringAfter(jb.getJSONObject("to").getString("lat"), "."), 6, "0") + ","
                            + StringUtils.substringBefore(jb.getJSONObject("to").getString("lng"), ".") + "."
                            + StringUtils.rightPad(StringUtils.substringAfter(jb.getJSONObject("to").getString("lng"), "."), 6, "0"));
                    BigDecimal dis=new BigDecimal(jb.getString("distance"));
                    if(dis.doubleValue()>1000){
                        s.put("distance", dis.divide(new BigDecimal(1000),2) + "km");
                    }else{
                        s.put("distance", dis + "m");
                    }

                });
            }
        }
        long goods = iMarketingCertificateGoodService.count(new LambdaQueryWrapper<MarketingCertificateGood>()
                .eq(MarketingCertificateGood::getMarketingCertificateId, marketingCertificateRecord.get("marketingCertificateId")));
        marketingCertificateRecord.put("goods",goods);

        //兑换方式
        if (marketingCertificateRecord.get("certificateType").equals("0")) {
            marketingCertificateRecord.put("certificate", "可兑换以下商品");
        } else if (marketingCertificateRecord.get("certificateType").equals("1")) {
            marketingCertificateRecord.put("certificate", "以下商品任选其一");
        }
        if (goods < 1) {
            marketingCertificateRecord.put("certificate", "");
        }

        marketingCertificateRecord.put("goodList",iMarketingCertificateGoodService.findMarketingCertificateSeckillListGoodByCertificateId(String.valueOf(marketingCertificateRecord.get("marketingCertificateId"))));

        if (marketingCertificateRecord.get("recordType").equals("0")){
            if (marketingCertificateRecord.get("isNomal").equals("0")){
                marketingCertificateRecord.put("anotherName","活动券");
                marketingCertificateRecord.put("label","活动券");
            }else {
                marketingCertificateRecord.put("anotherName","");
                marketingCertificateRecord.put("label","");
            }
        }else if (marketingCertificateRecord.get("recordType").equals("1")){
            MarketingCertificateSeckillBaseSetting marketingCertificateSeckillBaseSetting = iMarketingCertificateSeckillBaseSettingService.getOne(new LambdaQueryWrapper<MarketingCertificateSeckillBaseSetting>()
                    .eq(MarketingCertificateSeckillBaseSetting::getDelFlag, "0"));
            marketingCertificateRecord.put("anotherName",marketingCertificateSeckillBaseSetting.getAnotherName());
            marketingCertificateRecord.put("label",marketingCertificateSeckillBaseSetting.getLabel());
        }else if (marketingCertificateRecord.get("recordType").equals("2")){
            MarketingCertificateGroupBaseSetting marketingCertificateGroupBaseSetting = iMarketingCertificateGroupBaseSettingService.getOne(new LambdaQueryWrapper<MarketingCertificateGroupBaseSetting>()
                    .eq(MarketingCertificateGroupBaseSetting::getDelFlag, "0"));
            marketingCertificateRecord.put("anotherName",marketingCertificateGroupBaseSetting.getAnotherName());
            marketingCertificateRecord.put("label",marketingCertificateGroupBaseSetting.getLabel());
        }
        if(marketingCertificateRecord.get("qrAddr")==null) {

            String ctxPath = uploadpath;
            String fileName = null;
            String bizPath = "files";
            String nowday = new SimpleDateFormat("yyyyMMdd").format(new Date());
            File file = new File(ctxPath + File.separator + bizPath + File.separator + nowday);
            if (!file.exists()) {
                file.mkdirs();// 创建文件根目录
            }

            String orgName = "qrMarketingCertificate.png";// 获取文件名
            fileName = orgName.substring(0, orgName.lastIndexOf(".")) + "_" + System.currentTimeMillis() + orgName.substring(orgName.indexOf("."));
            String savePath = file.getPath() + File.separator + fileName;

            Boolean encode = QrCodeKit.encode(marketingCertificateRecord.get("id").toString(), BarcodeFormat.QR_CODE, 3, ErrorCorrectionLevel.H, "png", 200, 200,
                    savePath);
            String dbpath = bizPath + File.separator + nowday + File.separator + fileName;
            //券二维码
            marketingCertificateRecord.put("qrAddr", dbpath);
            MarketingCertificateRecord marketingCertificateRecord1=iMarketingCertificateRecordService.getById(marketingCertificateRecord.get("id").toString());
            marketingCertificateRecord1.setQrAddr(dbpath);
            iMarketingCertificateRecordService.saveOrUpdate(marketingCertificateRecord1);
        }
        result.setResult(marketingCertificateRecord);
        result.success("获取券详情信息");
        return result;
    }
    /**
     * 根据id获取优惠券商品列表
     * @param id
     * @return
     */
    @RequestMapping("findMarketingCertificateRecordGoods")
    @ResponseBody
    public  Result<Map<String,Object>> findMarketingCertificateRecordGoods(String id,String location,
                                                                           HttpServletRequest request){
        String memberId=request.getAttribute("memberId").toString();
        Result<Map<String,Object>> result=new Result<>();
        //参数验证
        if(StringUtils.isBlank(id)){
            result.error500("id不能为空！！！");
            return result;
        }
        Map<String,Object> marketingCertificateRecord =iMarketingCertificateRecordService.findMarketingCertificateRecordInfo(id);
        if(marketingCertificateRecord.get("sysUserId")!=null&&StringUtils.isNotBlank(marketingCertificateRecord.get("sysUserId").toString())) {
            marketingCertificateRecord.put("isViewStore","1");
            QueryWrapper<StoreManage> storeManageQueryWrapper = new QueryWrapper<>();
            storeManageQueryWrapper.eq("sys_user_id", marketingCertificateRecord.get("sysUserId"));
            storeManageQueryWrapper.in("pay_status", "1","2");
            StoreManage storeManage = iStoreManageService.list(storeManageQueryWrapper).get(0);
            if (storeManage.getSubStoreName() == null) {
                marketingCertificateRecord.put("storeName", storeManage.getStoreName());
            } else {
                marketingCertificateRecord.put("storeName", storeManage.getStoreName() + "(" + storeManage.getStoreName() + ")");
            }
            marketingCertificateRecord.put("logoAddr",storeManage.getLogoAddr());
            if (StringUtils.isNotBlank(location)) {
                JSONArray mapJsonArray=JSON.parseArray(tengxunMapUtils.findDistance(location,storeManage.getLatitude()+","+storeManage.getLongitude()));

                if(mapJsonArray!=null) {
                    mapJsonArray.stream().forEach(mj -> {
                        JSONObject jb = (JSONObject) mj;
                        BigDecimal dis = new BigDecimal(jb.getString("distance"));
                        if (dis.doubleValue() > 1000) {
                            marketingCertificateRecord.put("distance", dis.divide(new BigDecimal(1000), 2) + "km");
                        } else {
                            marketingCertificateRecord.put("distance", dis + "m");
                        }

                    });
                }
            } else {
                marketingCertificateRecord.put("distance", "");
            }
        }else{
            marketingCertificateRecord.put("isViewStore","0");
        }

        if(marketingCertificateRecord.get("qrAddr")==null) {
            String ctxPath = uploadpath;
            String fileName = null;
            String bizPath = "files";
            String nowday = new SimpleDateFormat("yyyyMMdd").format(new Date());
            File file = new File(ctxPath + File.separator + bizPath + File.separator + nowday);
            if (!file.exists()) {
                file.mkdirs();// 创建文件根目录
            }

            String orgName = "qrMarketingCertificate.png";// 获取文件名
            fileName = orgName.substring(0, orgName.lastIndexOf(".")) + "_" + System.currentTimeMillis() + orgName.substring(orgName.indexOf("."));
            String savePath = file.getPath() + File.separator + fileName;

            Boolean encode = QrCodeKit.encode(marketingCertificateRecord.get("id").toString(), BarcodeFormat.QR_CODE, 3, ErrorCorrectionLevel.H, "png", 200, 200,
                    savePath);
            String dbpath = bizPath + File.separator + nowday + File.separator + fileName;
            //券二维码
            marketingCertificateRecord.put("qrAddr", dbpath);
            MarketingCertificateRecord marketingCertificateRecord1=iMarketingCertificateRecordService.getById(marketingCertificateRecord.get("id").toString());
            marketingCertificateRecord1.setQrAddr(dbpath);
            iMarketingCertificateRecordService.saveOrUpdate(marketingCertificateRecord1);
        }

        //查询商品信息
        QueryWrapper<MarketingCertificateGood> marketingCertificateGoodQueryWrapper=new QueryWrapper<>();
        marketingCertificateGoodQueryWrapper.eq("marketing_certificate_id",marketingCertificateRecord.get("marketingCertificateId").toString());
        List<MarketingCertificateGood> marketingCertificateGoods=iMarketingCertificateGoodService.list(marketingCertificateGoodQueryWrapper);
        List<Map<String,Object>> goodsMap= Lists.newArrayList();
        marketingCertificateRecord.put("goodsMap",goodsMap);
        MemberList memberList=iMemberListService.getById(memberId);
        BigDecimal total=new BigDecimal(0);
        for (MarketingCertificateGood mcg:marketingCertificateGoods ) {

            //对兑换券的商品进行过滤

            if(!mcg.getCanMonth().equals("0")){
                Calendar calendar=Calendar.getInstance();
                int month=calendar.get(Calendar.MONTH)+1;
                if(!mcg.getCanMonth().equals(month+"")){
                    continue;
                }
            }
            String isPlatform= marketingCertificateRecord.get("isPlatform").toString();
            Map<String,Object> gdMap=Maps.newHashMap();
            gdMap.put("isPlatform",isPlatform);
            gdMap.put("marketingCertificateGoodId",mcg.getId());
            gdMap.put("quantity",mcg.getQuantity());
            if(isPlatform.equals("0")){
                //店铺商品
                GoodStoreList goodStoreList=iGoodStoreListService.getById(mcg.getGoodListId());
                gdMap.put("goodName",goodStoreList.getGoodName());
                gdMap.put("mainPicture",goodStoreList.getMainPicture());
                gdMap.put("smallVipPrice", goodStoreList.getSmallVipPrice());
                gdMap.put("smallPrice", goodStoreList.getSmallPrice());
                gdMap.put("id",goodStoreList.getId());
                if (memberList.getMemberType().equals("1")) {
                    total=total.add(new BigDecimal(goodStoreList.getSmallVipPrice()));
                } else {
                    total=total.add(new BigDecimal(goodStoreList.getSmallPrice()));
                }
                GoodStoreSpecification goodStoreSpecification=iGoodStoreSpecificationService.getById(mcg.getGoodSpecificationId());
                gdMap.put("specification",goodStoreSpecification.getSpecification());
            }else{
                //平台商品
                GoodList goodList=iGoodListService.getById(mcg.getGoodListId());
                gdMap.put("goodName",goodList.getGoodName());
                gdMap.put("mainPicture",goodList.getMainPicture());
                gdMap.put("smallVipPrice", goodList.getSmallVipPrice());
                gdMap.put("smallPrice", goodList.getSmallPrice());
                gdMap.put("id",goodList.getId());
                if (memberList.getMemberType().equals("1")) {
                    total=total.add(new BigDecimal(goodList.getSmallVipPrice()));
                } else {
                    total=total.add(new BigDecimal(goodList.getSmallPrice()));
                }
                GoodSpecification goodSpecification=iGoodSpecificationService.getById(mcg.getGoodSpecificationId());
                gdMap.put("specification",goodSpecification.getSpecification());
            }
            //组织查询参数
            Page<Map<String,Object>> page = new Page<Map<String,Object>>(1, 3);
            Map<String,Object> paramMap= Maps.newHashMap();
            paramMap.put("goodId",mcg.getGoodListId());
            paramMap.put("isPlatform",isPlatform);
            gdMap.put("discounts",iMarketingDiscountService.findMarketingDiscountByGoodId(page,paramMap).getRecords());
            goodsMap.add(gdMap);
        }
        marketingCertificateRecord.put("totalprice",total);
        result.setResult(marketingCertificateRecord);
        result.success("获取券详情信息");
        return result;
    }
}
