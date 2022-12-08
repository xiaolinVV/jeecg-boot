package org.jeecg.modules.marketing.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.ijpay.core.kit.QrCodeKit;
import com.ijpay.core.kit.WxPayKit;
import com.ijpay.wxpay.WxPayApiConfig;
import com.ijpay.wxpay.WxPayApiConfigKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.marketing.entity.MarketingWelfarePayments;
import org.jeecg.modules.marketing.service.IMarketingWelfarePaymentsService;
import org.jeecg.modules.marketing.vo.MarketingWelfarePaymentsVO;
import org.jeecg.modules.member.service.IMemberListService;
import org.jeecg.modules.member.service.IMemberWelfarePaymentsService;
import org.jeecg.modules.store.entity.StoreAccountCapital;
import org.jeecg.modules.store.entity.StoreManage;
import org.jeecg.modules.store.entity.StoreRechargeRecord;
import org.jeecg.modules.store.service.IStoreAccountCapitalService;
import org.jeecg.modules.store.service.IStoreManageService;
import org.jeecg.modules.store.service.IStoreRechargeRecordService;
import org.jeecg.modules.system.service.ISysUserRoleService;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Description: 店铺福利金
 * @Author: jeecg-boot
 * @Date: 2019-11-16
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "店铺福利金")
@RestController
@RequestMapping("/marketingWelfarePayments/marketingWelfarePayments")
public class MarketingWelfarePaymentsController {
    @Autowired
    private IMarketingWelfarePaymentsService marketingWelfarePaymentsService;
    @Autowired
    private IStoreManageService storeManageService;
    @Autowired
    private IMemberListService iMemberListService;
    @Autowired
    private IMemberWelfarePaymentsService iMemberWelfarePaymentsService;
    @Autowired
    private IStoreRechargeRecordService iStoreRechargeRecordService;
    @Autowired
    private IStoreAccountCapitalService iStoreAccountCapitalService;
    @Autowired
    private IStoreManageService iStoreManageService;
    /**
     * 分页列表查询
     *
     * @param marketingWelfarePayments
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "店铺福利金-分页列表查询")
    @ApiOperation(value = "店铺福利金-分页列表查询", notes = "店铺福利金-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<MarketingWelfarePayments>> queryPageList(MarketingWelfarePayments marketingWelfarePayments,
                                                                 @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                                 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                                 HttpServletRequest req) {
        Result<IPage<MarketingWelfarePayments>> result = new Result<IPage<MarketingWelfarePayments>>();
        QueryWrapper<MarketingWelfarePayments> queryWrapper = QueryGenerator.initQueryWrapper(marketingWelfarePayments, req.getParameterMap());
        Page<MarketingWelfarePayments> page = new Page<MarketingWelfarePayments>(pageNo, pageSize);
        IPage<MarketingWelfarePayments> pageList = marketingWelfarePaymentsService.page(page, queryWrapper);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }
    @AutoLog(value = "平台福利金赠送记录")
    @ApiOperation(value = "平台福利金赠送记录", notes = "平台福利金赠送记录")
    @RequestMapping(value = "findWelfarePaymentsTotal",method = RequestMethod.GET)
    public Result<IPage<MarketingWelfarePaymentsVO>> findWelfarePaymentsTotal(MarketingWelfarePaymentsVO marketingWelfarePaymentsVO,
                                                                       @RequestParam(value = "pageNo",defaultValue = "1")Integer pageNo,
                                                                       @RequestParam(value = "pageSize",defaultValue = "10")Integer pageSize){
        Result<IPage<MarketingWelfarePaymentsVO>> result = new Result<>();
        Page<MarketingWelfarePaymentsVO> page = new Page<>(pageNo, pageSize);
        IPage<MarketingWelfarePaymentsVO> welfarePaymentsTotal = marketingWelfarePaymentsService.findWelfarePaymentsTotal(page, marketingWelfarePaymentsVO);
        result.setSuccess(true);
        result.setResult(welfarePaymentsTotal);
        return result;
    }
    @AutoLog(value = "平台福利金购买记录")
    @ApiOperation(value = "平台福利金购买记录", notes = "平台福利金购买记录")
    @RequestMapping(value = "findWelfarePaymentsBuy",method = RequestMethod.GET)
    public Result<IPage<MarketingWelfarePaymentsVO>> findWelfarePaymentsBuy(MarketingWelfarePaymentsVO marketingWelfarePaymentsVO,
                                                                              @RequestParam(value = "pageNo",defaultValue = "1")Integer pageNo,
                                                                              @RequestParam(value = "pageSize",defaultValue = "10")Integer pageSize){
        Result<IPage<MarketingWelfarePaymentsVO>> result = new Result<>();
        Page<MarketingWelfarePaymentsVO> page = new Page<>(pageNo, pageSize);
        IPage<MarketingWelfarePaymentsVO> welfarePaymentsTotal = marketingWelfarePaymentsService.findWelfarePaymentsBuy(page, marketingWelfarePaymentsVO);
        result.setSuccess(true);
        result.setResult(welfarePaymentsTotal);
        return result;
    }
    @AutoLog(value = "店铺福利金记录表")
    @ApiOperation(value = "店铺福利金记录表", notes = "店铺福利金记录表")
    @RequestMapping(value = "findStoreWelfarePayments",method = RequestMethod.GET)
    public Result<IPage<MarketingWelfarePaymentsVO>>findStoreWelfarePayments(MarketingWelfarePaymentsVO marketingWelfarePaymentsVO,
                                                                             @RequestParam(value = "pageNo",defaultValue = "1")Integer pageNo,
                                                                             @RequestParam(value = "pageSize",defaultValue = "10")Integer pageSize,
                                                                             HttpServletRequest req){
        Result<IPage<MarketingWelfarePaymentsVO>> result = new Result<>();
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String userId = sysUser.getId();
        ISysUserRoleService iSysUserRoleService = SpringContextUtils.getBean(ISysUserRoleService.class);
        List<String> roleByUserId = iSysUserRoleService.getRoleByUserId(userId);
        if (roleByUserId.contains("Merchant")){
            marketingWelfarePaymentsVO.setUId(userId);
        }
        Page<MarketingWelfarePaymentsVO> page = new Page<>(pageNo, pageSize);
        IPage<MarketingWelfarePaymentsVO> storeWelfarePayments = marketingWelfarePaymentsService.findStoreWelfarePayments(page,marketingWelfarePaymentsVO);
        result.setSuccess(true);
        result.setResult(storeWelfarePayments);
        return result;
    }
    @AutoLog(value = "店铺福利金明细")
    @ApiOperation(value = "店铺福利金明细", notes = "店铺福利金明细")
    @GetMapping(value = "/findStoreWelfarePaymentsList")
    public Result<IPage<MarketingWelfarePaymentsVO>> findStoreWelfarePaymentsList(MarketingWelfarePaymentsVO marketingWelfarePaymentsVO,
                                                                 @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                                 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                                 HttpServletRequest req) {
        Result<IPage<MarketingWelfarePaymentsVO>> result = new Result<IPage<MarketingWelfarePaymentsVO>>();
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        ISysUserRoleService iSysUserRoleService = SpringContextUtils.getBean(ISysUserRoleService.class);
        List<String> roleByUserId = iSysUserRoleService.getRoleByUserId(sysUser.getId());
        if (roleByUserId.contains("Merchant")){
            marketingWelfarePaymentsVO.setUId(sysUser.getId());
        }
        Page<MarketingWelfarePaymentsVO> page = new Page<MarketingWelfarePaymentsVO>(pageNo, pageSize);
        IPage<MarketingWelfarePaymentsVO> pageList = marketingWelfarePaymentsService.findStoreWelfarePaymentsList(page, marketingWelfarePaymentsVO);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }
    /**
     * 添加
     *
     * @param marketingWelfarePayments
     * @return
     */
    @AutoLog(value = "店铺福利金-添加")
    @ApiOperation(value = "店铺福利金-添加", notes = "店铺福利金-添加")
    @PostMapping(value = "/add")
    public Result<MarketingWelfarePayments> add(@RequestBody MarketingWelfarePayments marketingWelfarePayments) {
        Result<MarketingWelfarePayments> result = new Result<MarketingWelfarePayments>();
        try {
            marketingWelfarePaymentsService.save(marketingWelfarePayments);
            result.success("添加成功！");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }

    /**
     * 编辑
     *
     * @param marketingWelfarePayments
     * @return
     */
    @AutoLog(value = "店铺福利金-编辑")
    @ApiOperation(value = "店铺福利金-编辑", notes = "店铺福利金-编辑")
    @PutMapping(value = "/edit")
    public Result<MarketingWelfarePayments> edit(@RequestBody MarketingWelfarePayments marketingWelfarePayments) {
        Result<MarketingWelfarePayments> result = new Result<MarketingWelfarePayments>();
        MarketingWelfarePayments marketingWelfarePaymentsEntity = marketingWelfarePaymentsService.getById(marketingWelfarePayments.getId());
        if (marketingWelfarePaymentsEntity == null) {
            result.error500("未找到对应实体");
        } else {
            boolean ok = marketingWelfarePaymentsService.updateById(marketingWelfarePayments);
            //TODO 返回false说明什么？
            if (ok) {
                result.success("修改成功!");
            }
        }

        return result;
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "店铺福利金-通过id删除")
    @ApiOperation(value = "店铺福利金-通过id删除", notes = "店铺福利金-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        try {
            marketingWelfarePaymentsService.removeById(id);
        } catch (Exception e) {
            log.error("删除失败", e.getMessage());
            return Result.error("删除失败!");
        }
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "店铺福利金-批量删除")
    @ApiOperation(value = "店铺福利金-批量删除", notes = "店铺福利金-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<MarketingWelfarePayments> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        Result<MarketingWelfarePayments> result = new Result<MarketingWelfarePayments>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别！");
        } else {
            this.marketingWelfarePaymentsService.removeByIds(Arrays.asList(ids.split(",")));
            result.success("删除成功!");
        }
        return result;
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "店铺福利金-通过id查询")
    @ApiOperation(value = "店铺福利金-通过id查询", notes = "店铺福利金-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<MarketingWelfarePayments> queryById(@RequestParam(name = "id", required = true) String id) {
        Result<MarketingWelfarePayments> result = new Result<MarketingWelfarePayments>();
        MarketingWelfarePayments marketingWelfarePayments = marketingWelfarePaymentsService.getById(id);
        if (marketingWelfarePayments == null) {
            result.error500("未找到对应实体");
        } else {
            result.setResult(marketingWelfarePayments);
            result.setSuccess(true);
        }
        return result;
    }

    /**
     * 导出excel
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, HttpServletResponse response) {
        // Step.1 组装查询条件
        QueryWrapper<MarketingWelfarePayments> queryWrapper = null;
        try {
            String paramsStr = request.getParameter("paramsStr");
            if (oConvertUtils.isNotEmpty(paramsStr)) {
                String deString = URLDecoder.decode(paramsStr, "UTF-8");
                MarketingWelfarePayments marketingWelfarePayments = JSON.parseObject(deString, MarketingWelfarePayments.class);
                queryWrapper = QueryGenerator.initQueryWrapper(marketingWelfarePayments, request.getParameterMap());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //Step.2 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        List<MarketingWelfarePayments> pageList = marketingWelfarePaymentsService.list(queryWrapper);
        //导出文件名称
        mv.addObject(NormalExcelConstants.FILE_NAME, "店铺福利金列表");
        mv.addObject(NormalExcelConstants.CLASS, MarketingWelfarePayments.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("店铺福利金列表数据", "导出人:Jeecg", "导出信息"));
        mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
        return mv;
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();// 获取上传文件对象
            ImportParams params = new ImportParams();
            params.setTitleRows(2);
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                List<MarketingWelfarePayments> listMarketingWelfarePaymentss = ExcelImportUtil.importExcel(file.getInputStream(), MarketingWelfarePayments.class, params);
                marketingWelfarePaymentsService.saveBatch(listMarketingWelfarePaymentss);
                return Result.ok("文件导入成功！数据行数:" + listMarketingWelfarePaymentss.size());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return Result.error("文件导入失败:" + e.getMessage());
            } finally {
                try {
                    file.getInputStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return Result.ok("文件导入失败！");
    }

    /**
     * 扫码
     *
     * @param request
     * @param response
     * @param productId
     * @return
     */
    @RequestMapping(value = "balance", method = {RequestMethod.GET, RequestMethod.POST})
    public Result balance(HttpServletRequest request, HttpServletResponse response,
                          @RequestParam("productId") String productId) {
        Result<Object> result = new Result<>();
        try {

            if (StringUtils.isBlank(productId)) {
                 result.error500("productId is null");
            }
            WxPayApiConfig config = WxPayApiConfigKit.getWxPayApiConfig();
            //获取扫码支付url
            String qrCodeUrl = WxPayKit.bizPayUrl(config.getPartnerKey(), config.getAppId(), config.getMchId(), productId);
            log.info(qrCodeUrl);
            //生成二维码保存的路径
            String name = "payQRCode.png";
            log.info(ResourceUtils.getURL("classpath:").getPath());
            Boolean encode = QrCodeKit.encode(qrCodeUrl, BarcodeFormat.QR_CODE, 3, ErrorCorrectionLevel.H,
                    "png", 200, 200,
                    ResourceUtils.getURL("classpath:").getPath().concat("static").concat(File.separator).concat(name));
            if (encode) {
                //在页面上显示
                return result.success(name);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return result.error500("网络异常" + e.getMessage());
        }
        return result;
    }

    /**
     * 赠送福利金
     */
    @AutoLog(value = "店铺-赠送福利金")
    @ApiOperation(value = "店铺-赠送福利金", notes = "店铺-赠送福利金")
    @RequestMapping(value = "updateWelfarePaymentsById", method = RequestMethod.POST)
    public Result<MarketingWelfarePayments> updateWelfarePaymentsById(@RequestBody JSONObject jsonObject) {
        return marketingWelfarePaymentsService.updateWelfarePaymentsByBoosPhone(jsonObject);
    }

    @AutoLog(value = "店铺-平台赠送店铺福利金")
    @ApiOperation(value = "店铺-平台赠送店铺福利金", notes = "店铺-平台赠送店铺福利金")
    @RequestMapping(value = "updateStoreWelfarePayments", method = RequestMethod.POST)
    public Result<StoreManage> updateStoreWelfarePayments(@RequestBody JSONObject jsonObject) {
        return marketingWelfarePaymentsService.updateStoreWelfarePayments(jsonObject);
    }
    @AutoLog(value = "根据类型和手机号码查出信息")
    @ApiOperation(value = "根据类型和手机号码查出信息", notes = "根据类型和手机号码查出信息")
    @RequestMapping(value = "findByPhoneAndType", method = RequestMethod.GET)
    public List<Map<String,Object>> findByPhoneAndType(@RequestParam("userType")String userType,
                                                       @RequestParam("phone")String phone){
        if (userType.equals("0")){
            return iMemberListService.likeMemberByPhone(phone);
        }else if (userType.equals("1")){
            return iStoreManageService.getStoreByBossPhone(phone);
        }else {
            return new ArrayList<Map<String,Object>>();
        }

    }
    /**
     * 福利金充值轮询接口
     * @param oldBalance
     * @param storeManageId
     * @param amount
     * @return
     */
    @GetMapping(value = "/prepaidPhoneCallback")
    @ResponseBody
    public Result<String>  prepaidPhoneCallback(BigDecimal oldBalance,String storeManageId, BigDecimal amount){
        Result<String> result = new Result<String>();
        if(StringUtils.isBlank(storeManageId)){
            result.error500("店铺Id不能为空");
            return result;
        }
        StoreManage storeManage = storeManageService.getById(storeManageId);
        if(oConvertUtils.isEmpty(storeManage)){
            result.error500("店铺不存在");
            return result;
        }
        BigDecimal balance = storeManage.getBalance();
        //
        if(balance.subtract(amount).doubleValue()==oldBalance.doubleValue()){
            //支付成功
            //修改余额记录信息
            QueryWrapper<StoreRechargeRecord> queryWrapper = new QueryWrapper();
            queryWrapper.eq("store_manage_id",storeManageId );
            queryWrapper.eq("amount",amount);
            queryWrapper.eq("pay_type","4");
            queryWrapper.eq("trade_status","0");
            queryWrapper.eq("back_times","0");
            queryWrapper.orderByDesc("create_time");
            List<StoreRechargeRecord>  storeRechargeRecordS = iStoreRechargeRecordService.list(queryWrapper);
            if(storeRechargeRecordS.size()>0){
                StoreRechargeRecord storeRechargeRecord = storeRechargeRecordS.get(0);
                //修改余额记录数据
                storeRechargeRecord.setTradeStatus("5");
                storeRechargeRecord.setBackStatus("1");
                storeRechargeRecord.setBackTimes(storeRechargeRecord.getBackTimes().add(BigDecimal.valueOf(1)) );
                //生成资金流水数据
                StoreAccountCapital storeAccountCapital = new StoreAccountCapital();
                storeAccountCapital.setDelFlag("0");
                storeAccountCapital.setStoreManageId(storeManageId);
                storeAccountCapital.setPayType("4");
                storeAccountCapital.setGoAndCome("0");
                storeAccountCapital.setAmount(amount);
                storeAccountCapital.setOrderNo(storeRechargeRecord.getOrderNo());
                storeAccountCapital.setBalance(storeManage.getBalance());
                iStoreRechargeRecordService.updateById(storeRechargeRecord);
                iStoreAccountCapitalService.save(storeAccountCapital);
                result.setResult("支付成功！");
                result.success("充值成功！");
            }else{
                result.error500("未找到余额记录数据");
                return result;
            }
        }else{
            //还未支付
            result.error500("未支付");
        }

        return result;

    }

}
