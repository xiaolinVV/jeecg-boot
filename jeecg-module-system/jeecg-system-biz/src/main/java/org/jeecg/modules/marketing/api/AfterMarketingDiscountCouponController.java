package org.jeecg.modules.marketing.api;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
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
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.modules.good.entity.GoodList;
import org.jeecg.modules.good.entity.GoodSpecification;
import org.jeecg.modules.good.entity.GoodStoreList;
import org.jeecg.modules.good.entity.GoodStoreSpecification;
import org.jeecg.modules.good.service.IGoodListService;
import org.jeecg.modules.good.service.IGoodSpecificationService;
import org.jeecg.modules.good.service.IGoodStoreListService;
import org.jeecg.modules.good.service.IGoodStoreSpecificationService;
import org.jeecg.modules.map.utils.TengxunMapUtils;
import org.jeecg.modules.marketing.entity.MarketingDiscountCoupon;
import org.jeecg.modules.marketing.entity.MarketingDiscountGood;
import org.jeecg.modules.marketing.service.IMarketingDiscountCouponService;
import org.jeecg.modules.marketing.service.IMarketingDiscountGoodService;
import org.jeecg.modules.marketing.service.IMarketingDiscountService;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.entity.MemberShoppingCart;
import org.jeecg.modules.member.service.IMemberListService;
import org.jeecg.modules.member.service.IMemberShoppingCartService;
import org.jeecg.modules.store.entity.StoreManage;
import org.jeecg.modules.store.service.IStoreManageService;
import org.jeecg.modules.system.entity.SysFrontSetting;
import org.jeecg.modules.system.service.ISysFrontSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 优惠券记录API
 *
 */
@RequestMapping("after/marketingDiscountCoupon")
@Controller
public class AfterMarketingDiscountCouponController {

    @Autowired
    private IMarketingDiscountCouponService iMarketingDiscountCouponService;

    @Autowired
    private IStoreManageService iStoreManageService;

    @Autowired
    private IMarketingDiscountGoodService iMarketingDiscountGoodService;

    @Autowired
    private IGoodListService iGoodListService;

    @Autowired
    private IGoodStoreListService iGoodStoreListService;

    @Autowired
    private IMarketingDiscountService iMarketingDiscountService;

    @Autowired
    private IMemberListService iMemberListService;


    @Autowired
    private TengxunMapUtils tengxunMapUtils;

    @Autowired
    private IMemberShoppingCartService iMemberShoppingCartService;

    @Autowired
    private IGoodStoreSpecificationService iGoodStoreSpecificationService;

    @Autowired
    private IGoodSpecificationService iGoodSpecificationService;
    @Autowired
    private ISysFrontSettingService iSysFrontSettingService;

    @Value(value = "${jeecg.path.upload}")
    private String uploadpath;

    /**
     * 赠送优惠券
     * @param id
     * @param request
     * @return
     */
    @RequestMapping("senderMarketingDiscountCoupon")
    @ResponseBody
    public Result<String> senderMarketingDiscountCoupon(String id,HttpServletRequest request){
        String memberId= request.getAttribute("memberId").toString();
        Result<String> result=new Result<>();
        if(StringUtils.isBlank(id)){
            result.setCode(369);
            result.setSuccess(false);
            result.setMessage("id不能为空！！！");
            return result;
        }
        MarketingDiscountCoupon marketingDiscountCoupon=iMarketingDiscountCouponService.getById(id);
        if(!memberId.equals(marketingDiscountCoupon.getMemberListId())){
            result.setCode(369);
            result.setSuccess(false);
            result.setMessage("非本人优惠券不可赠送");
            return result;
        }

        //改变券的状态为已赠送
        marketingDiscountCoupon.setStatus("5");
        marketingDiscountCoupon.setSendTime(new Date());

        iMarketingDiscountCouponService.saveOrUpdate(marketingDiscountCoupon);

        result.success("送出优惠券成功");
        return result;
    }


    /**
     * 领取优惠券
     *
     * @param giveMemberId
     * @param request
     * @return
     */
    @RequestMapping("giveAsMarketingDiscountCoupon")
    @ResponseBody
    public Result<String> giveAsMarketingDiscountCoupon(String id,String giveMemberId,HttpServletRequest request){
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

        MarketingDiscountCoupon marketingDiscountCoupon=iMarketingDiscountCouponService.getById(id);
        if(!giveMemberId.equals(marketingDiscountCoupon.getMemberListId())){
            result.setCode(369);
            result.setSuccess(false);
            result.setMessage("您的手慢了。该券已被其他用户领取了。");
            return result;
        }

        marketingDiscountCoupon.setMemberListId(memberId);

        marketingDiscountCoupon.setGiveMemberListId(giveMemberId);

        if(new Date().getTime()>=marketingDiscountCoupon.getStartTime().getTime()&&new Date().getTime()<=marketingDiscountCoupon.getEndTime().getTime()) {
            //设置生效
            marketingDiscountCoupon.setStatus("1");
        }else{
            marketingDiscountCoupon.setStatus("0");
        }

        iMarketingDiscountCouponService.saveOrUpdate(marketingDiscountCoupon);

        result.success("优惠券领取成功！！！");
        return result;
    }


    /**
     *  查询会员优惠券列表
     * @param pattern  优惠券状态；1：未使用；2：已使用；3：已过期；4：已失效;5：已赠送
     * @param pageNo
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping("findMarketingDiscountCouponByMemberId")
    @ResponseBody
    public Result<IPage<Map<String,Object>>> findMarketingDiscountCouponByMemberId( Integer pattern,@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,HttpServletRequest request){
        String memberId= request.getAttribute("memberId").toString();
        Result<IPage<Map<String,Object>>> result=new Result<>();
        //组织查询参数
        Page<Map<String,Object>> page = new Page<Map<String,Object>>(pageNo, pageSize);
        Map<String,Object> paramMap= Maps.newHashMap();
        paramMap.put("memberId",memberId);
        paramMap.put("pattern",pattern);
        IPage<Map<String, Object>> marketingDiscountCouponByMemberId = iMarketingDiscountCouponService.findMarketingDiscountCouponByMemberId(page, paramMap);
        String frontLogo = iSysFrontSettingService.getOne(new LambdaQueryWrapper<SysFrontSetting>().eq(SysFrontSetting::getDelFlag, "0")).getFrontLogo();
        marketingDiscountCouponByMemberId.getRecords().forEach(mdc->{
            if(mdc.get("logoAddr")!=null) {
                mdc.put("logoAddr", Convert.toStr(mdc.get("logoAddr")));
            }else {
                mdc.put("logoAddr", JSON.parseObject(frontLogo).get("0"));
            }
        });
        result.setResult(marketingDiscountCouponByMemberId);

        result.success("查询会员优惠券列表");
        return result;
    }


    /**
     * 根据id获取优惠券详情
     * @param id
     * @return
     */
    @RequestMapping("findMarketingDiscountCouponInfo")
    @ResponseBody
    public  Result<Map<String,Object>> findMarketingDiscountCouponInfo(String id,
                                                                       String location){
        Result<Map<String,Object>> result=new Result<>();
        //参数验证
        if(StringUtils.isBlank(id)){
            result.error500("id不能为空！！！");
            return result;
        }
        Map<String,Object> marketingDiscountCoupon =iMarketingDiscountCouponService.findMarketingDiscountCouponInfo(id);
        if(marketingDiscountCoupon.get("isPlatform").equals("0")&&marketingDiscountCoupon.get("sysUserId")!=null&&StringUtils.isNotBlank(marketingDiscountCoupon.get("sysUserId").toString())) {
            marketingDiscountCoupon.put("isViewStore","1");
            StoreManage storeManage = iStoreManageService.getStoreManageBySysUserId(marketingDiscountCoupon.get("sysUserId").toString());

            if(storeManage!=null) {

                if (storeManage.getSubStoreName() == null) {
                    marketingDiscountCoupon.put("storeName", storeManage.getStoreName());
                } else {
                    marketingDiscountCoupon.put("storeName", storeManage.getStoreName() + "(" + storeManage.getStoreName() + ")");
                }
                marketingDiscountCoupon.put("storeAddress", storeManage.getStoreAddress());
                marketingDiscountCoupon.put("takeOutPhone", storeManage.getTakeOutPhone());
                marketingDiscountCoupon.put("latitude", storeManage.getLatitude());
                marketingDiscountCoupon.put("longitude", storeManage.getLongitude());
                marketingDiscountCoupon.put("logoAddr", storeManage.getLogoAddr());
                if (StringUtils.isNotBlank(location)) {
                    JSONArray mapJsonArray = JSON.parseArray(tengxunMapUtils.findDistance(location, storeManage.getLatitude() + "," + storeManage.getLongitude()));

                    if (mapJsonArray != null) {
                        mapJsonArray.forEach(mj -> {
                            JSONObject jb = (JSONObject) mj;
                            BigDecimal dis = new BigDecimal(jb.getString("distance"));
                            if (dis.doubleValue() > 1000) {
                                marketingDiscountCoupon.put("distance", dis.divide(new BigDecimal(1000)).setScale(2, RoundingMode.DOWN) + "km");
                            } else {
                                marketingDiscountCoupon.put("distance", dis + "m");
                            }

                        });
                    }
                } else {
                    marketingDiscountCoupon.put("distance", "");
                }
            }

        }else{
            marketingDiscountCoupon.put("isViewStore","0");
        }

        //券二维码
        if(marketingDiscountCoupon.get("qrAddr")==null) {
            String ctxPath = uploadpath;
            String fileName = null;
            String bizPath = "files";
            String nowday = new SimpleDateFormat("yyyyMMdd").format(new Date());
            File file = new File(ctxPath + File.separator + bizPath + File.separator + nowday);
            if (!file.exists()) {
                file.mkdirs();// 创建文件根目录
            }

            String orgName = "qrMarketingDiscountCoupon.png";// 获取文件名
            fileName = orgName.substring(0, orgName.lastIndexOf(".")) + "_" + System.currentTimeMillis() + orgName.substring(orgName.indexOf("."));
            String savePath = file.getPath() + File.separator + fileName;

           QrCodeKit.encode(marketingDiscountCoupon.get("id").toString(), BarcodeFormat.QR_CODE, 3, ErrorCorrectionLevel.H, "png", 200, 200,
                    savePath);
            String dbpath = bizPath + File.separator + nowday + File.separator + fileName;
            //券二维码
            marketingDiscountCoupon.put("qrAddr", dbpath);
            MarketingDiscountCoupon marketingDiscountCoupon1=iMarketingDiscountCouponService.getById(marketingDiscountCoupon.get("id").toString());
            marketingDiscountCoupon1.setQrAddr(dbpath);
            iMarketingDiscountCouponService.saveOrUpdate(marketingDiscountCoupon1);
        }
        if (marketingDiscountCoupon.get("isPlatform").equals("0")){
            marketingDiscountCoupon.put("goodList",iMarketingDiscountGoodService.findMarketingDiscountStoreGoodById(String.valueOf(marketingDiscountCoupon.get("marketingDiscountId"))));
        }else if (marketingDiscountCoupon.get("isPlatform").equals("1")){
            marketingDiscountCoupon.put("goodList",iMarketingDiscountGoodService.findMarketingDiscountGoodById(String.valueOf(marketingDiscountCoupon.get("marketingDiscountId"))));
        }
        result.setResult(marketingDiscountCoupon);
        result.success("获取券详情信息");
        return result;
    }

    /**
     * 根据id获取优惠券商品列表
     * @param id
     * @return
     */
    @RequestMapping("findMarketingDiscountCouponGoods")
    @ResponseBody
    public  Result<Map<String,Object>> findMarketingDiscountCouponGoods(String id,
                                                                        String state,
                                                                        String location,
                                                                        HttpServletRequest request){
        String memberId=request.getAttribute("memberId").toString();
        Result<Map<String,Object>> result=new Result<>();
        //参数验证
        if(StringUtils.isBlank(id)){
            result.error500("id不能为空！！！");
            return result;
        }
        Map<String,Object> marketingDiscountCoupon =iMarketingDiscountCouponService.findMarketingDiscountCouponInfo(id);
        if(marketingDiscountCoupon.get("isPlatform").equals("0")&&marketingDiscountCoupon.get("sysUserId")!=null&&StringUtils.isNotBlank(marketingDiscountCoupon.get("sysUserId").toString())) {
            marketingDiscountCoupon.put("isViewStore","1");
            StoreManage storeManage = iStoreManageService.getStoreManageBySysUserId(marketingDiscountCoupon.get("sysUserId").toString());
            if (storeManage!=null){


                if (StringUtils.isNotBlank(location)) {
                    JSONArray mapJsonArray=JSON.parseArray(tengxunMapUtils.findDistance(location,storeManage.getLatitude()+","+storeManage.getLongitude()));

                    if(mapJsonArray!=null) {
                        mapJsonArray.stream().forEach(mj -> {
                            JSONObject jb = (JSONObject) mj;
                            BigDecimal dis = new BigDecimal(jb.getString("distance"));
                            if (dis.doubleValue() > 1000) {
                                marketingDiscountCoupon.put("distance", dis.divide(new BigDecimal(1000)).setScale(2,RoundingMode.DOWN) + "km");
                            } else {
                                marketingDiscountCoupon.put("distance", dis + "m");
                            }

                        });
                    }
                } else {
                    marketingDiscountCoupon.put("distance", "");
                }
            }else {
                return result.error500("店铺信息异常!");
            }

        }else{
            marketingDiscountCoupon.put("isViewStore","0");
        }

        if (StrUtil.equals(Convert.toStr(marketingDiscountCoupon.get("isNomal")),"2") && StrUtil.equals(state,"1")) {
            //折扣券不支持线下核销
            throw  new JeecgBootException( "折扣券不支持线下核销");
        }

        //券二维码
        if(marketingDiscountCoupon.get("qrAddr")==null) {
            String ctxPath = uploadpath;
            String fileName = null;
            String bizPath = "files";
            String nowday = new SimpleDateFormat("yyyyMMdd").format(new Date());
            File file = new File(ctxPath + File.separator + bizPath + File.separator + nowday);
            if (!file.exists()) {
                file.mkdirs();// 创建文件根目录
            }

            String orgName = "qrMarketingDiscountCoupon.png";// 获取文件名
            fileName = orgName.substring(0, orgName.lastIndexOf(".")) + "_" + System.currentTimeMillis() + orgName.substring(orgName.indexOf("."));
            String savePath = file.getPath() + File.separator + fileName;

            Boolean encode = QrCodeKit.encode(marketingDiscountCoupon.get("id").toString(), BarcodeFormat.QR_CODE, 3, ErrorCorrectionLevel.H, "png", 200, 200,
                    savePath);
            String dbpath = bizPath + File.separator + nowday + File.separator + fileName;
            //券二维码
            marketingDiscountCoupon.put("qrAddr", dbpath);
            MarketingDiscountCoupon marketingDiscountCoupon1=iMarketingDiscountCouponService.getById(marketingDiscountCoupon.get("id").toString());
            marketingDiscountCoupon1.setQrAddr(dbpath);
            iMarketingDiscountCouponService.saveOrUpdate(marketingDiscountCoupon1);
        }

        //查询商品信息
        QueryWrapper<MarketingDiscountGood> marketingDiscountGoodQueryWrapper=new QueryWrapper<>();
        marketingDiscountGoodQueryWrapper.eq("marketing_discount_id",marketingDiscountCoupon.get("marketingDiscountId").toString());
        List<MarketingDiscountGood> marketingDiscountGoods=iMarketingDiscountGoodService.list(marketingDiscountGoodQueryWrapper);
        List<Map<String,Object>> goodsMap= Lists.newArrayList();
        marketingDiscountCoupon.put("goodsMap",goodsMap);
        MemberList memberList=iMemberListService.getById(memberId);
        BigDecimal total=new BigDecimal(0);
        List<String> memberShoppingCartIds=Lists.newArrayList();
        for (MarketingDiscountGood mdg:marketingDiscountGoods ) {
            String isPlatform= mdg.getIsPlatform();
            Map<String,Object> gdMap=Maps.newHashMap();
            gdMap.put("isPlatform",isPlatform);
            if(isPlatform.equals("0")){
                //店铺商品
                GoodStoreList goodStoreList=iGoodStoreListService.getById(mdg.getGoodId());
                if(goodStoreList==null){
                    continue;
                }
                GoodStoreSpecification goodStoreSpecificationSmall=iGoodStoreSpecificationService.getSmallGoodSpecification(goodStoreList.getId());

                gdMap.put("goodName",goodStoreList.getGoodName());
                gdMap.put("mainPicture",goodStoreList.getMainPicture());
                gdMap.put("smallVipPrice", goodStoreSpecificationSmall.getVipPrice());
                gdMap.put("smallPrice", goodStoreSpecificationSmall.getPrice());
                gdMap.put("id",goodStoreList.getId());

                //查询购物车商品
                QueryWrapper<MemberShoppingCart> memberShoppingCartQueryWrapper=new QueryWrapper<>();
                memberShoppingCartQueryWrapper.eq("member_list_id",memberId);
                memberShoppingCartQueryWrapper.eq("is_view","1");
                memberShoppingCartQueryWrapper.eq("del_flag","0");
                memberShoppingCartQueryWrapper.eq("good_store_list_id",goodStoreList.getId());

                List<MemberShoppingCart> memberShoppingCarts=iMemberShoppingCartService.list(memberShoppingCartQueryWrapper);
                if(memberShoppingCarts.size()>0){
                    for (MemberShoppingCart msc:memberShoppingCarts) {
                        memberShoppingCartIds.add(msc.getId());
                        GoodStoreSpecification goodStoreSpecification = iGoodStoreSpecificationService.getById(msc.getGoodStoreSpecificationId());
                        if (memberList.getMemberType().equals("1")) {
                            total=total.add(goodStoreSpecification.getVipPrice().multiply(msc.getQuantity()));
                        } else {
                            total=total.add(goodStoreSpecification.getPrice().multiply(msc.getQuantity()));
                        }
                    }

                }
            }else{
                //平台商品
                GoodList goodList=iGoodListService.getById(mdg.getGoodId());
                if(goodList==null){
                    continue;
                }
                GoodSpecification goodSpecificationSmall=iGoodSpecificationService.getSmallGoodSpecification(goodList.getId());
                gdMap.put("goodName",goodList.getGoodName());
                gdMap.put("mainPicture",goodList.getMainPicture());
                gdMap.put("smallVipPrice", goodSpecificationSmall.getVipPrice());
                gdMap.put("smallPrice", goodSpecificationSmall.getPrice());
                gdMap.put("id",goodList.getId());
                //查询购物车商品
                QueryWrapper<MemberShoppingCart> memberShoppingCartQueryWrapper=new QueryWrapper<>();
                memberShoppingCartQueryWrapper.eq("member_list_id",memberId);
                memberShoppingCartQueryWrapper.eq("is_view","1");
                memberShoppingCartQueryWrapper.eq("del_flag","0");
                memberShoppingCartQueryWrapper.eq("good_list_id",goodList.getId());

                List<MemberShoppingCart> memberShoppingCarts=iMemberShoppingCartService.list(memberShoppingCartQueryWrapper);
                if(memberShoppingCarts.size()>0){
                    for (MemberShoppingCart msc:memberShoppingCarts) {
                        memberShoppingCartIds.add(msc.getId());
                        GoodSpecification goodSpecification = iGoodSpecificationService.getById(msc.getGoodSpecificationId());
                        if (memberList.getMemberType().equals("1")) {
                            total=total.add(goodSpecification.getVipPrice().multiply(msc.getQuantity()));
                        } else {
                            total=total.add(goodSpecification.getPrice().multiply(msc.getQuantity()));
                        }
                    }
                }
            }
            //组织查询参数
            Page<Map<String,Object>> page = new Page<Map<String,Object>>(1, 3);
            Map<String,Object> paramMap= Maps.newHashMap();
            paramMap.put("goodId",mdg.getGoodId());
            paramMap.put("isPlatform",isPlatform);
            gdMap.put("discounts",iMarketingDiscountService.findMarketingDiscountByGoodId(page,paramMap).getRecords());
            goodsMap.add(gdMap);
        }
        marketingDiscountCoupon.put("totalprice",total);
        marketingDiscountCoupon.put("memberShoppingCartIds",memberShoppingCartIds);
        result.setResult(marketingDiscountCoupon);
        result.success("获取券详情信息");
        return result;
    }



    /**
     * 根据id获取优惠券商品列表对应的价格
     * @param id
     * @return
     */
    @RequestMapping("findMarketingDiscountCouponGoodsPrice")
    @ResponseBody
    public Result<Map<String,Object>> findMarketingDiscountCouponGoodsPrice(String id,String location,HttpServletRequest request){
        String memberId=request.getAttribute("memberId").toString();
        Result<Map<String,Object>> result=new Result<>();
        Map<String,Object> objectMap=Maps.newHashMap();
        //参数验证
        if(StringUtils.isBlank(id)){
            result.error500("id不能为空！！！");
            return result;
        }

        Map<String,Object> marketingDiscountCoupon =iMarketingDiscountCouponService.findMarketingDiscountCouponInfo(id);

        //查询商品信息
        QueryWrapper<MarketingDiscountGood> marketingDiscountGoodQueryWrapper=new QueryWrapper<>();
        marketingDiscountGoodQueryWrapper.eq("marketing_discount_id",marketingDiscountCoupon.get("marketingDiscountId").toString());
        List<MarketingDiscountGood> marketingDiscountGoods=iMarketingDiscountGoodService.list(marketingDiscountGoodQueryWrapper);
        MemberList memberList=iMemberListService.getById(memberId);
        BigDecimal total=new BigDecimal(0);
        List<String> memberShoppingCartIds=Lists.newArrayList();
        for (MarketingDiscountGood mdg:marketingDiscountGoods ) {
            String isPlatform= mdg.getIsPlatform();
            Map<String,Object> gdMap=Maps.newHashMap();
            gdMap.put("isPlatform",isPlatform);
            if(isPlatform.equals("0")){
                //店铺商品
                GoodStoreList goodStoreList=iGoodStoreListService.getById(mdg.getGoodId());
                if(goodStoreList==null){
                    continue;
                }
                //查询购物车商品
                QueryWrapper<MemberShoppingCart> memberShoppingCartQueryWrapper=new QueryWrapper<>();
                memberShoppingCartQueryWrapper.eq("member_list_id",memberId);
                memberShoppingCartQueryWrapper.eq("is_view","1");
                memberShoppingCartQueryWrapper.eq("del_flag","0");
                memberShoppingCartQueryWrapper.eq("good_store_list_id",goodStoreList.getId());

                List<MemberShoppingCart> memberShoppingCarts=iMemberShoppingCartService.list(memberShoppingCartQueryWrapper);
                if(memberShoppingCarts.size()>0){
                    for (MemberShoppingCart msc:memberShoppingCarts) {
                        memberShoppingCartIds.add(msc.getId());
                        GoodStoreSpecification goodStoreSpecification = iGoodStoreSpecificationService.getById(msc.getGoodStoreSpecificationId());
                        if (memberList.getMemberType().equals("1")) {
                            total=total.add(goodStoreSpecification.getVipPrice().multiply(msc.getQuantity()));
                        } else {
                            total=total.add(goodStoreSpecification.getPrice().multiply(msc.getQuantity()));
                        }
                    }

                }
            }else{
                //平台商品
                GoodList goodList=iGoodListService.getById(mdg.getGoodId());
                if(goodList==null){
                    continue;
                }
                //查询购物车商品
                QueryWrapper<MemberShoppingCart> memberShoppingCartQueryWrapper=new QueryWrapper<>();
                memberShoppingCartQueryWrapper.eq("member_list_id",memberId);
                memberShoppingCartQueryWrapper.eq("is_view","1");
                memberShoppingCartQueryWrapper.eq("del_flag","0");
                memberShoppingCartQueryWrapper.eq("good_list_id",goodList.getId());

                List<MemberShoppingCart> memberShoppingCarts=iMemberShoppingCartService.list(memberShoppingCartQueryWrapper);
                if(memberShoppingCarts.size()>0){
                    for (MemberShoppingCart msc:memberShoppingCarts) {
                        memberShoppingCartIds.add(msc.getId());
                        GoodSpecification goodSpecification = iGoodSpecificationService.getById(msc.getGoodSpecificationId());
                        if (memberList.getMemberType().equals("1")) {
                            total=total.add(goodSpecification.getVipPrice().multiply(msc.getQuantity()));
                        } else {
                            total=total.add(goodSpecification.getPrice().multiply(msc.getQuantity()));
                        }
                    }
                }
            }
        }
        objectMap.put("totalprice",total);
        objectMap.put("memberShoppingCartIds",memberShoppingCartIds);
        result.setResult(objectMap);
        result.success("获取券详情价格信息");
        return result;
    }
}
