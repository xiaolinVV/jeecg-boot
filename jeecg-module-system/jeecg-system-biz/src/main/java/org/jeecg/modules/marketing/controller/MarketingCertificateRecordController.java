package org.jeecg.modules.marketing.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.OrderNoUtils;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.marketing.dto.MarketingCertificateRecordDTO;
import org.jeecg.modules.marketing.entity.MarketingCertificate;
import org.jeecg.modules.marketing.entity.MarketingCertificateRecord;
import org.jeecg.modules.marketing.entity.MarketingChannel;
import org.jeecg.modules.marketing.entity.MarketingGiftBagCertificate;
import org.jeecg.modules.marketing.service.IMarketingCertificateRecordService;
import org.jeecg.modules.marketing.service.IMarketingCertificateService;
import org.jeecg.modules.marketing.service.IMarketingChannelService;
import org.jeecg.modules.marketing.vo.MarketingCertificateRecordVO;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.service.IMemberListService;
import org.jeecg.modules.system.service.ISysUserRoleService;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.*;

/**
 * @Description: 兑换券记录
 * @Author: jeecg-boot
 * @Date: 2019-11-13
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "兑换券记录")
@RestController
@RequestMapping("/marketingCertificateRecord/marketingCertificateRecord")
public class MarketingCertificateRecordController {
    @Autowired
    private IMarketingCertificateRecordService marketingCertificateRecordService;

    @Autowired
    private IMemberListService iMemberListService;

    @Autowired
    private IMarketingCertificateService iMarketingCertificateService;

    @Autowired
    private IMarketingChannelService iMarketingChannelService;

    /**
     * 分页列表查询
     *
     * @param marketingCertificateRecord
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "兑换券记录-分页列表查询")
    @ApiOperation(value = "兑换券记录-分页列表查询", notes = "兑换券记录-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<MarketingCertificateRecord>> queryPageList(MarketingCertificateRecord marketingCertificateRecord,
                                                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                                   HttpServletRequest req) {
        Result<IPage<MarketingCertificateRecord>> result = new Result<IPage<MarketingCertificateRecord>>();
        QueryWrapper<MarketingCertificateRecord> queryWrapper = QueryGenerator.initQueryWrapper(marketingCertificateRecord, req.getParameterMap());
        Page<MarketingCertificateRecord> page = new Page<MarketingCertificateRecord>(pageNo, pageSize);
        IPage<MarketingCertificateRecord> pageList = marketingCertificateRecordService.page(page, queryWrapper);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 添加
     *
     * @param marketingCertificateRecord
     * @return
     */
    @AutoLog(value = "兑换券记录-添加")
    @ApiOperation(value = "兑换券记录-添加", notes = "兑换券记录-添加")
    @PostMapping(value = "/add")
    public Result<MarketingCertificateRecord> add(@RequestBody MarketingCertificateRecord marketingCertificateRecord) {
        Result<MarketingCertificateRecord> result = new Result<MarketingCertificateRecord>();
        try {
            marketingCertificateRecordService.save(marketingCertificateRecord);
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
     * @param marketingCertificateRecord
     * @return
     */
    @AutoLog(value = "兑换券记录-编辑")
    @ApiOperation(value = "兑换券记录-编辑", notes = "兑换券记录-编辑")
    @PutMapping(value = "/edit")
    public Result<MarketingCertificateRecord> edit(@RequestBody MarketingCertificateRecord marketingCertificateRecord) {
        Result<MarketingCertificateRecord> result = new Result<MarketingCertificateRecord>();
        MarketingCertificateRecord marketingCertificateRecordEntity = marketingCertificateRecordService.getById(marketingCertificateRecord.getId());
        if (marketingCertificateRecordEntity == null) {
            result.error500("未找到对应实体");
        } else {
            boolean ok = marketingCertificateRecordService.updateById(marketingCertificateRecord);
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
    @AutoLog(value = "兑换券记录-通过id删除")
    @ApiOperation(value = "兑换券记录-通过id删除", notes = "兑换券记录-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        try {
            marketingCertificateRecordService.removeById(id);
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
    @AutoLog(value = "兑换券记录-批量删除")
    @ApiOperation(value = "兑换券记录-批量删除", notes = "兑换券记录-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<MarketingCertificateRecord> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        Result<MarketingCertificateRecord> result = new Result<MarketingCertificateRecord>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别！");
        } else {
            this.marketingCertificateRecordService.removeByIds(Arrays.asList(ids.split(",")));
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
    @AutoLog(value = "兑换券记录-通过id查询")
    @ApiOperation(value = "兑换券记录-通过id查询", notes = "兑换券记录-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<MarketingCertificateRecord> queryById(@RequestParam(name = "id", required = true) String id) {
        Result<MarketingCertificateRecord> result = new Result<MarketingCertificateRecord>();
        MarketingCertificateRecord marketingCertificateRecord = marketingCertificateRecordService.getById(id);
        if (marketingCertificateRecord == null) {
            result.error500("未找到对应实体");
        } else {
            result.setResult(marketingCertificateRecord);
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
        QueryWrapper<MarketingCertificateRecord> queryWrapper = null;
        try {
            String paramsStr = request.getParameter("paramsStr");
            if (oConvertUtils.isNotEmpty(paramsStr)) {
                String deString = URLDecoder.decode(paramsStr, "UTF-8");
                MarketingCertificateRecord marketingCertificateRecord = JSON.parseObject(deString, MarketingCertificateRecord.class);
                queryWrapper = QueryGenerator.initQueryWrapper(marketingCertificateRecord, request.getParameterMap());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //Step.2 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        List<MarketingCertificateRecord> pageList = marketingCertificateRecordService.list(queryWrapper);
        //导出文件名称
        mv.addObject(NormalExcelConstants.FILE_NAME, "兑换券记录列表");
        mv.addObject(NormalExcelConstants.CLASS, MarketingCertificateRecord.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("兑换券记录列表数据", "导出人:Jeecg", "导出信息"));
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
                List<MarketingCertificateRecord> listMarketingCertificateRecords = ExcelImportUtil.importExcel(file.getInputStream(), MarketingCertificateRecord.class, params);
                marketingCertificateRecordService.saveBatch(listMarketingCertificateRecords);
                return Result.ok("文件导入成功！数据行数:" + listMarketingCertificateRecords.size());
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

    @AutoLog(value = "兑换券记录-分页列表查询")
    @ApiOperation(value = "兑换券记录-分页列表查询", notes = "兑换券记录-分页列表查询")
    @GetMapping(value = "/findCertificateRecord")
    public Result<IPage<MarketingCertificateRecordVO>> findCertificateRecord(MarketingCertificateRecordVO marketingCertificateRecordVO,
                                                                             @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                                             @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                                             HttpServletRequest req) {
        Result<IPage<MarketingCertificateRecordVO>> result = new Result<IPage<MarketingCertificateRecordVO>>();
        Page<MarketingCertificateRecordVO> page = new Page<MarketingCertificateRecordVO>(pageNo, pageSize);
        IPage<MarketingCertificateRecordVO> pageList = marketingCertificateRecordService.findCertificateRecord(page, marketingCertificateRecordVO);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    @AutoLog(value = "店铺兑换券记录-分页列表查询")
    @ApiOperation(value = "店铺兑换券记录-分页列表查询", notes = "店铺兑换券记录-分页列表查询")
    @GetMapping(value = "/findStoreCertificateRecord")
    public Result<IPage<MarketingCertificateRecordVO>> findStoreCertificateRecord(MarketingCertificateRecordVO marketingCertificateRecordVO,
                                                                                  @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                                                  @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                                                  HttpServletRequest req) {
        Result<IPage<MarketingCertificateRecordVO>> result = new Result<IPage<MarketingCertificateRecordVO>>();
        Page<MarketingCertificateRecordVO> page = new Page<MarketingCertificateRecordVO>(pageNo, pageSize);
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String StoreUId = sysUser.getId();
        ISysUserRoleService iSysUserRoleService = SpringContextUtils.getBean(ISysUserRoleService.class);
        List<String> roleByUserId = iSysUserRoleService.getRoleByUserId(StoreUId);
        if (roleByUserId.contains("Merchant")) {
            marketingCertificateRecordVO.setStoreUId(StoreUId);
        }
        IPage<MarketingCertificateRecordVO> pageList = marketingCertificateRecordService.findStoreCertificateRecord(page, marketingCertificateRecordVO);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    @PostMapping("giveMarketingCertificate")
    public Result<String> giveMarketingCertificate(@RequestBody MarketingCertificateRecordDTO marketingCertificateRecordDTO) {
        Result<String> result = new Result<>();
        MemberList memberList = iMemberListService.getById(marketingCertificateRecordDTO.getMemberListId());
        if (oConvertUtils.isEmpty(memberList)){
            return result.error500("未找到该会员");
        }
        List<MarketingGiftBagCertificate> marketingGiftBagCertificateList = marketingCertificateRecordDTO.getMarketingGiftBagCertificateList();
        if (marketingGiftBagCertificateList.size()<=0){
            return result.error500("请选择兑换券!");
        }
        marketingGiftBagCertificateList.forEach(mcl->{
            MarketingCertificate marketingCertificate = iMarketingCertificateService.getById(mcl.getMarketingCertificateId());
            if (oConvertUtils.isNotEmpty(marketingCertificate)){
                //兑换券量不足时候跳过领取
                int certificateCount=mcl.getDistributedAmount().intValue();
                Calendar myCalendar = Calendar.getInstance();
                while(certificateCount>0) {
                    if (marketingCertificate.getTotal().subtract(marketingCertificate.getReleasedQuantity()).longValue() <= 0) {
                        continue;
                    }
                    MarketingCertificateRecord marketingCertificateRecord = new MarketingCertificateRecord();
                    marketingCertificateRecord.setDelFlag("0");
                    marketingCertificateRecord.setName(marketingCertificate.getName());
                    marketingCertificateRecord.setMemberListId(memberList.getId());
                    marketingCertificateRecord.setQqzixuangu(OrderNoUtils.getOrderNo());
                    marketingCertificateRecord.setMarketingCertificateId(marketingCertificate.getId());
                    marketingCertificateRecord.setIsPlatform("1");
                    marketingCertificateRecord.setIsGive(marketingCertificate.getIsGive());
                    marketingCertificateRecord.setIsWarn(marketingCertificate.getIsWarn());
                    marketingCertificateRecord.setWarnDays(marketingCertificate.getWarnDays());
                    marketingCertificateRecord.setUserRestrict(marketingCertificate.getUserRestrict());
                    marketingCertificateRecord.setDiscountExplain(marketingCertificate.getDiscountExplain());
                    marketingCertificateRecord.setTheReward(marketingCertificate.getTheReward());
                    marketingCertificateRecord.setMainPicture(marketingCertificate.getMainPicture());
                    marketingCertificateRecord.setRewardStore(marketingCertificate.getRewardStore());
                    marketingCertificateRecord.setIsNomal(marketingCertificate.getIsNomal());
                    marketingCertificateRecord.setMarketPrice(marketingCertificate.getMarketPrice());
                    marketingCertificateRecord.setPrice(marketingCertificate.getPrice());
                    marketingCertificateRecord.setCostPrice(marketingCertificate.getCostPrice());
                    marketingCertificateRecord.setSellRewardStore(marketingCertificate.getSellRewardStore());
                    marketingCertificateRecord.setRewardDayOne(marketingCertificate.getRewardDayOne());
                    marketingCertificateRecord.setCoverPlan(marketingCertificate.getCoverPlan());
                    marketingCertificateRecord.setPosters(marketingCertificate.getPosters());
                    marketingCertificateRecord.setCertificateType(marketingCertificate.getCertificateType());
                    marketingCertificateRecord.setIsBuyPlatform("1");
                    marketingCertificateRecord.setAboveUse(marketingCertificate.getAboveUse());
                    marketingCertificateRecord.setBelowUse(marketingCertificate.getBelowUse());
                    //平台渠道判断
                    QueryWrapper<MarketingChannel> marketingChannelQueryWrapper = new QueryWrapper<>();
                    marketingChannelQueryWrapper.eq("english_name", "NORMAL_TO_GET");
                    MarketingChannel marketingChannel = iMarketingChannelService.getOne(marketingChannelQueryWrapper);
                    if (marketingChannel != null) {
                        marketingCertificateRecord.setMarketingChannelId(marketingChannel.getId());
                        marketingCertificateRecord.setTheChannel(marketingChannel.getName());
                    }
                    //标准用券方式
                    if (marketingCertificate.getVouchersWay().equals("0")) {
                        //优惠券的时间生成
                        marketingCertificateRecord.setStartTime(marketingCertificate.getStartTime());
                        marketingCertificateRecord.setEndTime(marketingCertificate.getEndTime());
                    }

                    //领券当日起
                    if (marketingCertificate.getVouchersWay().equals("1")) {
                        //优惠券的时间生成
                        if(mcl.getValidityType().equals("1")) {
                            myCalendar.setTime(new Date());
                        }
                        marketingCertificateRecord.setStartTime(myCalendar.getTime());

                        if (marketingCertificate.getMonad().equals("天")) {
                            myCalendar.add(Calendar.DATE, marketingCertificate.getDisData().intValue());
                        }
                        if (marketingCertificate.getMonad().equals("周")) {
                            myCalendar.add(Calendar.WEEK_OF_MONTH, marketingCertificate.getDisData().intValue());
                        }
                        if (marketingCertificate.getMonad().equals("月")) {
                            myCalendar.add(Calendar.MONTH, marketingCertificate.getDisData().intValue());
                        }

                        marketingCertificateRecord.setEndTime(myCalendar.getTime());
                    }
                    //领券次日起
                    if (marketingCertificate.getVouchersWay().equals("2")) {
                        //优惠券的时间生成
                        if(mcl.getValidityType().equals("1")) {
                            myCalendar.setTime(new Date());
                        }
                        myCalendar.add(Calendar.DATE, 1);
                        marketingCertificateRecord.setStartTime(myCalendar.getTime());

                        if (marketingCertificate.getMonad().equals("天")) {
                            myCalendar.add(Calendar.DATE, marketingCertificate.getDisData().intValue());
                        }
                        if (marketingCertificate.getMonad().equals("周")) {
                            myCalendar.add(Calendar.WEEK_OF_MONTH, marketingCertificate.getDisData().intValue());
                        }
                        if (marketingCertificate.getMonad().equals("月")) {
                            myCalendar.add(Calendar.MONTH, marketingCertificate.getDisData().intValue());
                        }

                        marketingCertificateRecord.setEndTime(myCalendar.getTime());
                    }

                    if (new Date().getTime() >= marketingCertificateRecord.getStartTime().getTime() && new Date().getTime() <= marketingCertificateRecord.getEndTime().getTime()) {
                        //设置生效
                        marketingCertificateRecord.setStatus("1");
                    } else {
                        marketingCertificateRecord.setStatus("0");
                    }
                    marketingCertificateRecordService.save(marketingCertificateRecord);
                    marketingCertificate.setReleasedQuantity(marketingCertificate.getReleasedQuantity().add(new BigDecimal(1)));
                    iMarketingCertificateService.saveOrUpdate(marketingCertificate);
                    certificateCount--;
                }

            }
        });
        return  result.success("赠送成功");
    }
}
