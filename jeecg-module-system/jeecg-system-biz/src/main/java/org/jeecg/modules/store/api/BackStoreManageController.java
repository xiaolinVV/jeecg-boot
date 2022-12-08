package org.jeecg.modules.store.api;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.OrderNoUtils;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.weixin.WeixinUtils;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.service.IMemberListService;
import org.jeecg.modules.order.utils.WeixinPayUtils;
import org.jeecg.modules.pay.utils.NotifyUrlUtils;
import org.jeecg.modules.store.entity.StoreManage;
import org.jeecg.modules.store.entity.StoreRechargeRecord;
import org.jeecg.modules.store.entity.StoreWelfarePaymentsGathering;
import org.jeecg.modules.store.service.IStoreManageService;
import org.jeecg.modules.store.service.IStorePermissionUidService;
import org.jeecg.modules.store.service.IStoreRechargeRecordService;
import org.jeecg.modules.store.service.IStoreWelfarePaymentsGatheringService;
import org.jeecg.modules.store.vo.StoreManageVO;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysDictService;
import org.jeecg.modules.system.service.ISysUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("back/storeManage")
@Slf4j
public class BackStoreManageController {

    @Autowired
    private WeixinPayUtils weixinPayUtils;

    @Autowired
    private IStoreManageService iStoreManageService;
    @Autowired
    private ISysDictService iSysDictService;
    @Autowired
    private IStoreRechargeRecordService iStoreRechargeRecordService;
    @Autowired
    private ISysUserService iSysUserService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private IStorePermissionUidService storePermissionUidService;
    @Autowired
    private IMemberListService iMemberListService;

    @Value(value = "${jeecg.path.upload}")
    private String uploadpath;
    @Autowired
    private IStoreWelfarePaymentsGatheringService iStoreWelfarePaymentsGatheringService;

    @Autowired
    private NotifyUrlUtils notifyUrlUtils;

    /**
     * 我的(get)
     *
     * @param request
     * @return
     */
    @RequestMapping("getStoreManage")
    @ResponseBody
    public Result<Map<String, Object>> getStoreManage(HttpServletRequest request) {
        Result<Map<String, Object>> result = new Result<>();
        Map<String, Object> stringObjectMap = iStoreManageService.myStore(request);
        result.setResult(stringObjectMap);
        result.success("返回我的信息成功!");
        return result;
    }

    /**
     * 基本信息返显(get)
     *
     * @param request
     * @return
     */
    @RequestMapping("findStoreInfo")
    @ResponseBody
    public Result<Map<String, Object>> findStoreInfo(HttpServletRequest request) {
        Result<Map<String, Object>> result = new Result<>();
        Map<String, Object> storeInfo = iStoreManageService.findStoreInfo(request);
        result.success("返回店铺信息成功!");
        result.setResult(storeInfo);
        return result;
    }

    /**
     * 基本信息保存(post)
     *
     * @param
     * @param request
     * @return
     */
    @RequestMapping("setStoreInfo")
    @ResponseBody
    public Result<StoreManage> setStoreInfo(StoreManageVO storeManageVO, HttpServletRequest request) {
        Result<StoreManage> result = new Result<>();
        List<String> strings = Arrays.asList(StringUtils.split(storeManageVO.getLongitudeAndLatitude(),","));
        StoreManage storeManage = iStoreManageService.getById(storeManageVO.getId());
        BeanUtils.copyProperties(storeManageVO,storeManage);
        storeManage.setLatitude(new BigDecimal(strings.get(0)));
        storeManage.setLongitude(new BigDecimal(strings.get(1)));
        boolean b = iStoreManageService.saveOrUpdate(storeManage);
        if (b) {
            return result.success("基本信息保存成功!");
        } else {
            return result.error500("保存失败,请重试!");
        }
    }

    /**
     * 返回店铺二维码
     *
     * @param request
     * @return
     */
    @RequestMapping("returnSmallcode")
    @ResponseBody
    public Result<Map<String, Object>> returnSmallcode(HttpServletRequest request) {
        Result<Map<String, Object>> result = new Result<>();
        Map<String, Object> stringObjectMap = iStoreManageService.returnSmallcode(request);
        result.setResult(stringObjectMap);
        result.success("返回店铺二维码成功!");
        return result;
    }

    /**
     * 编辑店铺信息(认证)
     *
     * @param storeManage
     * @return
     */

    @RequestMapping("updateStoreManage")
    @ResponseBody
    public Result<StoreManage> updateStoreManage(StoreManage storeManage, HttpServletRequest request) {
        Result<StoreManage> result = new Result<StoreManage>();
        String sysUserId = request.getAttribute("sysUserId").toString();
        StoreManage storeManageEntity = iStoreManageService.getById(storeManage.getId());
        if (storeManageEntity == null) {
            result.error500("未找到对应实体");
            return result;
        }
        if (!sysUserId.equals(storeManageEntity.getSysUserId())) {
            result.error500("用户信息不匹配!");
            return result;
        }
        if (!"1".equals(storeManageEntity.getStatus())) {
            result.error500("该店铺已被停用");
            return result;
        }
        if ("0".equals(storeManageEntity.getPayStatus())) {
            result.error500("该店铺未支付费用");
            return result;
        }
        boolean ok = iStoreManageService.updateById(storeManage
                .setAttestationStatus("0")
                .setRemark(""));
        if (ok) {
            result.success("修改成功!");
        } else {
            result.error500("修改失败!");
        }

        return result;
    }

    /**
     * 认证信息返显(get)
     *
     * @param request
     * @return
     */
    @RequestMapping("returnAuthentication")
    @ResponseBody
    public Result<Map<String, Object>> returnAuthentication(HttpServletRequest request) {
        Result<Map<String, Object>> result = new Result<>();
        Map<String, Object> storeManage = iStoreManageService.returnAuthentication(request);
        result.success("店铺认证返显成功");
        result.setResult(storeManage);
        return result;
    }

    /**
     * 返回安全设置信息(get)
     *
     * @param request
     * @return
     */
    @RequestMapping("returnSecurity")
    @ResponseBody
    public Result<Map<String, Object>> returnSecurity(HttpServletRequest request) {
        Result<Map<String, Object>> result = new Result<>();
        Map<String, Object> returnSecurity = iStoreManageService.returnSecurity(request);
        result.success("返回安全设置信息!");
        result.setResult(returnSecurity);
        return result;
    }

    /**
     * 修改密码(post)
     * @param storeManageVO
     * @param request
     * @return
     */
    @RequestMapping("updateStorePassword")
    @ResponseBody
    public Result<Boolean> updateStorePassword(StoreManageVO storeManageVO, HttpServletRequest request) {
        Result<Boolean> result = new Result<>();
        if (StringUtils.isBlank(storeManageVO.getPassword())){
            return result.error500("密码不能为空");
        }
        if (!storeManageVO.getPassword().equals(storeManageVO.getConfirmPassword())) {
            return result.error500("两次密码不一致!");
        }
        Boolean storePassword = iStoreManageService.updateStorePassword(storeManageVO, request);
        if (storePassword) {
            result.success("保存成功!");
        } else {
            result.error500("保存失败,请重试!");
        }
        return result;
    }

    /**
     * 修改手机号(post)
     * @param storeManageVO
     * @param request
     * @return
     */
    @RequestMapping("updateStorePhone")
    @ResponseBody
    public Result<Boolean>updateStorePhone(StoreManageVO storeManageVO,HttpServletRequest request){
        Result<Boolean> result = new Result<>();
        if (StringUtils.isBlank(storeManageVO.getCode())){
            return result.error500("请数据验证码");
        }
        if (StringUtils.isBlank(storeManageVO.getNewPhone())){
            return result.error500("请输入新手机");
        }
        if (StringUtils.isBlank(storeManageVO.getNewPhoneCode())){
            return result.error500("请输入新手机验证码");
        }
        Object sbCode = redisUtil.get(storeManageVO.getPhone());
        if (!storeManageVO.getCode().equals(sbCode)) {
            return result.error500("验证码错误");
        }
        Object newSbCode = redisUtil.get(storeManageVO.getNewPhone());
        if (!storeManageVO.getNewPhoneCode().equals(newSbCode)) {
            return result.error500("新手机验证码错误");
        }
        String sysUserId = request.getAttribute("sysUserId").toString();
        iSysUserService.update(new SysUser().setPhone(storeManageVO.getNewPhone()),new LambdaUpdateWrapper<SysUser>().eq(SysUser::getId,sysUserId));
        boolean b = iStoreManageService.update(new StoreManage().setBossPhone(storeManageVO.getNewPhone()),new LambdaUpdateWrapper<StoreManage>().eq(StoreManage::getSysUserId, sysUserId));
        if (b){
            result.success("保存成功!");
        }else {
            result.error500("保存失败,请重试!");
        }
        return result;
    }
    /**
     * 店铺余额充值
     *
     * @param
     * @param
     * @return
     */
    @RequestMapping("payBalance")
    @ResponseBody
    public Result<Map<String, Object>> payBalance(BigDecimal price,String code, HttpServletRequest request) {

        Result<Map<String, Object>> result = new Result<>();
        String sysUserId = request.getAttribute("sysUserId").toString();
        Map<String, Object> objectMap = Maps.newHashMap();
        //查询店铺
        QueryWrapper<StoreManage> storeManageQueryWrapper = new QueryWrapper<>();
        storeManageQueryWrapper.eq("sys_user_id", sysUserId);
        storeManageQueryWrapper.in("pay_status", "1", "2");
        storeManageQueryWrapper.eq("status", "1");
        if (iStoreManageService.count(storeManageQueryWrapper) <= 0) {
            result.error500("查询不到店铺用户信息！！！");
            return result;
        }
        StoreManage storeManage = iStoreManageService.list(storeManageQueryWrapper).get(0);
        String orderNo = OrderNoUtils.getOrderNo();
        StoreRechargeRecord storeRechargeRecord = new StoreRechargeRecord();
        storeRechargeRecord.setDelFlag("0");
        storeRechargeRecord.setStoreManageId(storeManage.getId());
        storeRechargeRecord.setPayType("4");
        storeRechargeRecord.setGoAndCome("0");
        storeRechargeRecord.setAmount(price);
        storeRechargeRecord.setTradeStatus("0");
        storeRechargeRecord.setOrderNo(orderNo);
        storeRechargeRecord.setOperator(storeManage.getBossName());
        storeRechargeRecord.setBackTimes(new BigDecimal(0));
        storeRechargeRecord.setPayment("0");

        iStoreRechargeRecordService.save(storeRechargeRecord);

        //获取常量信息
         String appSecret= iSysDictService.queryTableDictTextByKey("sys_dict_item","item_value","item_text","AppSecret_store");

        String appid = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "AppID_store");

        //获取openid
        Map<String,Object> stringMap = WeixinUtils.getOpenId(appid,appSecret,code);
        String openid= (String) stringMap.get("openid");


        //设置回调地址

        String notifyUrl = notifyUrlUtils.getNotify("balance_notifyUrl_store");

        //官方微信支付调起
        Map<String,String> resultMap= weixinPayUtils.payWeixinStore(request,storeRechargeRecord.getId(),storeRechargeRecord.getAmount(),openid,notifyUrl);

        //保存支付日志
        iStoreRechargeRecordService.saveOrUpdate(storeRechargeRecord
                .setPayParam(resultMap.get("params")));
        objectMap.put("notifyUrl", notifyUrl+"?id="+storeRechargeRecord.getId());
        objectMap.put("jsonStr", resultMap.get("jsonStr"));
        result.setResult(objectMap);
        result.success("充值待支付成功");

        return result;
    }



    /**
     * 查询商家端菜单
     * @return
     */
    @RequestMapping("getStorePermissionUidMap")
    @ResponseBody
    public Result<List<Map<String,Object>>> getStorePermissionUidMap(HttpServletRequest request){
        Result<List<Map<String,Object>>> result = new Result();
        String sysUserId = request.getAttribute("sysUserId").toString();
        List<Map<String,Object>> mapList = storePermissionUidService.getStorePermissionUidMap(sysUserId);
        result.setResult(mapList);
        result.success("查询成功");
        return result;
    }

    @PostMapping(value = "upload")
    @ResponseBody
    public Result<?> upload(HttpServletRequest request) {
        Result<?> result = new Result<>();
        try {
            String ctxPath = uploadpath;
            String fileName = null;
            String bizPath = "files";
            String nowday = new SimpleDateFormat("yyyyMMdd").format(new Date());
            File file = new File(ctxPath + File.separator + bizPath + File.separator + nowday);
            if (!file.exists()) {
                file.mkdirs();// 创建文件根目录
            }
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            MultipartFile mf = multipartRequest.getFile("file");// 获取上传文件对象
            String orgName = mf.getOriginalFilename();// 获取文件名
            fileName = orgName.substring(0, orgName.lastIndexOf(".")) + "_" + System.currentTimeMillis() + orgName.substring(orgName.indexOf("."));
            String savePath = file.getPath() + File.separator + fileName;
            File savefile = new File(savePath);
            FileCopyUtils.copy(mf.getBytes(), savefile);
            String dbpath = bizPath + File.separator + nowday + File.separator + fileName;
            if (dbpath.contains("\\")) {
                dbpath = dbpath.replace("\\", "/");
            }
            result.setMessage(dbpath);
            result.setSuccess(true);
        } catch (IOException e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return result;
    }


    /**
     * 根据坐标获取地址列表
     * @param address
     * @return
     */
    @RequestMapping("getAddsress")
    @ResponseBody
    @Cacheable(value = "getAddsress",key ="#address+'_'+#pageSize+'_'+#pageIndex" )
    public Result<Object> getAddsress(String address, @RequestParam(defaultValue = "20",value = "pageSize") Integer pageSize, @RequestParam(defaultValue = "1",value = "pageIndex") Integer pageIndex){

        Result<Object> result=new Result<>();

        if(StringUtils.isBlank(address)){
            result.error500("address的位置信息不能为空");
            return result;
        }

        //获取配置常量
        String tencentMapsSecretKey=iSysDictService.queryTableDictTextByKey("sys_dict_item","item_value","item_text","tencent_maps_secret_key");
        String tencentMapsKey=iSysDictService.queryTableDictTextByKey("sys_dict_item","item_value","item_text","tencent_maps_key");

        //拼接加密字段
        String queryStriing=  "/ws/geocoder/v1/?address="+address+"&key="+tencentMapsKey;
        String sig= DigestUtils.md5DigestAsHex((queryStriing+tencentMapsSecretKey).getBytes());

        //行程请求的url
        String mapUrl="https://apis.map.qq.com"+queryStriing+"&sig="+sig;

        log.info(mapUrl);

        //请求访问
        RestTemplate restTemplate=new RestTemplate();
        String mapResult=restTemplate.getForObject(mapUrl,String.class);
        String lat=JSON.parseObject(mapResult).getJSONObject("result").getJSONObject("location").getString("lat");//纬度
        String lng=JSON.parseObject(mapResult).getJSONObject("result").getJSONObject("location").getString("lng");//经度
        String location=lat+","+lng;
        return geocoder(location,pageSize,pageIndex);
    }
    /**
     * 根据坐标获取地址列表
     * @param location
     * @return
     */
    @RequestMapping("geocoder")
    @ResponseBody
    @Cacheable(value = "geocoder",key = "#location+'_'+#pageSize+'_'+#pageIndex")
    public Result<Object> geocoder(String location, @RequestParam(defaultValue = "20",value = "pageSize") Integer pageSize, @RequestParam(defaultValue = "1",value = "pageIndex") Integer pageIndex){

        Result<Object> result=new Result<>();

        if(StringUtils.isBlank(location)){
            result.error500("location的位置信息不能为空");
            return result;
        }

        //获取配置常量
        String tencentMapsSecretKey=iSysDictService.queryTableDictTextByKey("sys_dict_item","item_value","item_text","tencent_maps_secret_key");
        String tencentMapsKey=iSysDictService.queryTableDictTextByKey("sys_dict_item","item_value","item_text","tencent_maps_key");

        //拼接加密字段
        String queryStriing=  "/ws/geocoder/v1/?get_poi=1&key="+tencentMapsKey+"&location="+location+"&poi_options=page_size="+pageSize+";page_index="+pageIndex+";policy=2";
        String sig=DigestUtils.md5DigestAsHex((queryStriing+tencentMapsSecretKey).getBytes());

        //行程请求的url
        String mapUrl="https://apis.map.qq.com"+queryStriing+"&sig="+sig;

        log.info(mapUrl);

        //请求访问
        RestTemplate restTemplate=new RestTemplate();
        String mapResult=restTemplate.getForObject(mapUrl,String.class);

        result.setResult(JSON.parseObject(mapResult).getJSONObject("result").getJSONArray("pois"));
        result.success("获取到地图的地址");
        return result;
    }
    /**
     * 返回通用信息(get)
     *
     * @param request
     * @return
     */
    @RequestMapping("findUseInfo")
    @ResponseBody
    public Result<Map<String, Object>> findUseInfo(HttpServletRequest request) {
        Result<Map<String, Object>> result = new Result<>();
        String sysUserId = request.getAttribute("sysUserId").toString();
        Map<String, Object> stringObjectMap = iStoreManageService.findUseInfo(sysUserId);
        if (StringUtils.isBlank(stringObjectMap.get("isOpenWelfarePayments").toString())){
            stringObjectMap.put("isViewOpenWelfarePayments","0");
        }else {
            if (stringObjectMap.get("isOpenWelfarePayments").toString().equals("0")){
                stringObjectMap.put("isViewOpenWelfarePayments","0");
            }else {
                LambdaQueryWrapper<StoreWelfarePaymentsGathering> storeWelfarePaymentsGatheringLambdaUpdateWrapper = new LambdaQueryWrapper<StoreWelfarePaymentsGathering>()
                        .eq(StoreWelfarePaymentsGathering::getStoreManageId, stringObjectMap.get("id").toString());
                if (iStoreWelfarePaymentsGatheringService.count(storeWelfarePaymentsGatheringLambdaUpdateWrapper)>0){
                    StoreWelfarePaymentsGathering storeWelfarePaymentsGathering = iStoreWelfarePaymentsGatheringService.list(storeWelfarePaymentsGatheringLambdaUpdateWrapper).get(0);
                    stringObjectMap.put("isViewOpenWelfarePayments","1");
                    stringObjectMap.put("subscriptionRatio",storeWelfarePaymentsGathering.getSubscriptionRatio());
                }else {
                    stringObjectMap.put("isViewOpenWelfarePayments","0");
                }
            }
        }
        //版本号
        String smallsoftStoreVersion = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "smallsoft_store_version");
        stringObjectMap.put("smallsoftStoreVersion",smallsoftStoreVersion);
        result.setResult(stringObjectMap);
        result.success("返回通用信息成功!");
        return result;
    }


    /**
     * 客户管理
     * @param request
     * @return
     */
    @RequestMapping("getCustomerManagementsCount")
    @ResponseBody
    public Result<Map<String, Object>> getCustomerManagementsCount(HttpServletRequest request){
        Result<Map<String, Object>> result = new Result<>();
        Map<String, Object> objectMap = Maps.newHashMap();
        String sysUserId = request.getAttribute("sysUserId").toString();
        //总会员
        long sumVIPCount = iMemberListService.count(new LambdaQueryWrapper<MemberList>().eq(MemberList::getSysUserId,sysUserId));
       //普通会员
        long commonVIPCount = iMemberListService.count(new LambdaQueryWrapper<MemberList>().eq(MemberList::getSysUserId,sysUserId)
        .eq(MemberList::getMemberType,'0'));
        //VIP会员
        long memberVIPCount = iMemberListService.count(new LambdaQueryWrapper<MemberList>().eq(MemberList::getSysUserId,sysUserId)
                .eq(MemberList::getMemberType,'1'));
        //性别未知
        long sexUnknownCount = iMemberListService.count(new LambdaQueryWrapper<MemberList>().eq(MemberList::getSysUserId,sysUserId)
                .eq(MemberList::getSex,'0'));
        //性别男
        long sexManCount = iMemberListService.count(new LambdaQueryWrapper<MemberList>().eq(MemberList::getSysUserId,sysUserId)
                .eq(MemberList::getSex,'1'));
        //性别女
        long sexGirlCount = iMemberListService.count(new LambdaQueryWrapper<MemberList>().eq(MemberList::getSysUserId,sysUserId)
                .eq(MemberList::getSex,'2'));
        objectMap.put("sumVIPCount",sumVIPCount);
        objectMap.put("commonVIPCount",commonVIPCount);
        objectMap.put("memberVIPCount",memberVIPCount);
        objectMap.put("sexUnknownCount",sexUnknownCount);
        objectMap.put("sexManCount",sexManCount);
        objectMap.put("sexGirlCount",sexGirlCount);
        result.setResult(objectMap);
        result.success("查询用户管理成功!");
        return result;
    }

    /**
     * 客户管理列表
     * @param request
     * @return
     */
    @RequestMapping("getCustomerManagementsList")
    @ResponseBody
    public Result<IPage<Map<String,Object>>> getCustomerManagementsCount(String searchNickNamePhone,
                                                                   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                                   HttpServletRequest request){
        Result<IPage<Map<String,Object>>> result = new Result<>();
        String sysUserId = request.getAttribute("sysUserId").toString();
        Page<MemberList> page = new Page<MemberList>(pageNo, pageSize);
        IPage<Map<String,Object>> mapIPage = iMemberListService.getMyMemberList(page,sysUserId,searchNickNamePhone);
        result.setResult(mapIPage);
        result.success("查询成功!");
        return result;
    }


    }
