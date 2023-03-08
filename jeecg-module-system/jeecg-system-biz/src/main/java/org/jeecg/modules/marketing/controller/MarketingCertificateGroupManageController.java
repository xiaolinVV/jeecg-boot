package org.jeecg.modules.marketing.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.marketing.entity.MarketingCertificateGroupList;
import org.jeecg.modules.marketing.entity.MarketingCertificateGroupManage;
import org.jeecg.modules.marketing.entity.MarketingCertificateGroupRecord;
import org.jeecg.modules.marketing.entity.MarketingCertificateRecord;
import org.jeecg.modules.marketing.service.*;
import org.jeecg.modules.marketing.vo.MarketingCertificateGroupManageVO;
import org.jeecg.modules.member.service.IMemberListService;
import org.jeecg.modules.member.service.IMemberWelfarePaymentsService;
import org.jeecg.modules.order.utils.PayUtils;
import org.jeecg.modules.pay.entity.PayCertificateLog;
import org.jeecg.modules.pay.service.IPayCertificateLogService;
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
 * @Description: 拼好券管理
 * @Author: jeecg-boot
 * @Date: 2021-03-29
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "拼好券管理")
@RestController
@RequestMapping("/marketingCertificateGroupManage/marketingCertificateGroupManage")
public class MarketingCertificateGroupManageController {
    @Autowired
    private IMarketingCertificateGroupManageService marketingCertificateGroupManageService;
    @Autowired
    private IMarketingCertificateGroupRecordService iMarketingCertificateGroupRecordService;
    @Autowired
    private IMarketingCertificateRecordService iMarketingCertificateRecordService;
    @Autowired
    private IMarketingCertificateService iMarketingCertificateService;
    @Autowired
    private IMarketingCertificateGroupListService iMarketingCertificateGroupListService;
    @Autowired
    private IPayCertificateLogService iPayCertificateLogService;
    @Autowired
    private IMemberListService iMemberListService;
    @Autowired
    private IMemberWelfarePaymentsService iMemberWelfarePaymentsService;
    @Autowired
    private PayUtils payUtils;
    /**
     * 分页列表查询
     *
     * @param marketingCertificateGroupManage
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "拼好券管理-分页列表查询")
    @ApiOperation(value = "拼好券管理-分页列表查询", notes = "拼好券管理-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<MarketingCertificateGroupManageVO>> queryPageList(MarketingCertificateGroupManage marketingCertificateGroupManage,
                                                                          @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                                          HttpServletRequest req) {
        Result<IPage<MarketingCertificateGroupManageVO>> result = new Result<IPage<MarketingCertificateGroupManageVO>>();
        QueryWrapper<MarketingCertificateGroupManage> queryWrapper = QueryGenerator.initQueryWrapper(marketingCertificateGroupManage, req.getParameterMap());
        Page<MarketingCertificateGroupManageVO> page = new Page<MarketingCertificateGroupManageVO>(pageNo, pageSize);
        IPage<MarketingCertificateGroupManageVO> pageList = marketingCertificateGroupManageService.queryPageList(page, queryWrapper);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 添加
     *
     * @param marketingCertificateGroupManage
     * @return
     */
    @AutoLog(value = "拼好券管理-添加")
    @ApiOperation(value = "拼好券管理-添加", notes = "拼好券管理-添加")
    @PostMapping(value = "/add")
    public Result<MarketingCertificateGroupManage> add(@RequestBody MarketingCertificateGroupManage marketingCertificateGroupManage) {
        Result<MarketingCertificateGroupManage> result = new Result<MarketingCertificateGroupManage>();
        try {
            marketingCertificateGroupManageService.save(marketingCertificateGroupManage);
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
     * @param marketingCertificateGroupManage
     * @return
     */
    @AutoLog(value = "拼好券管理-编辑")
    @ApiOperation(value = "拼好券管理-编辑", notes = "拼好券管理-编辑")
    @PutMapping(value = "/edit")
    public Result<MarketingCertificateGroupManage> edit(@RequestBody MarketingCertificateGroupManage marketingCertificateGroupManage) {
        Result<MarketingCertificateGroupManage> result = new Result<MarketingCertificateGroupManage>();
        MarketingCertificateGroupManage marketingCertificateGroupManageEntity = marketingCertificateGroupManageService.getById(marketingCertificateGroupManage.getId());
        if (marketingCertificateGroupManageEntity == null) {
            result.error500("未找到对应实体");
        } else {
            boolean ok = marketingCertificateGroupManageService.updateById(marketingCertificateGroupManage);
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
    @AutoLog(value = "拼好券管理-通过id删除")
    @ApiOperation(value = "拼好券管理-通过id删除", notes = "拼好券管理-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        try {
            marketingCertificateGroupManageService.removeById(id);
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
    @AutoLog(value = "拼好券管理-批量删除")
    @ApiOperation(value = "拼好券管理-批量删除", notes = "拼好券管理-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<MarketingCertificateGroupManage> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        Result<MarketingCertificateGroupManage> result = new Result<MarketingCertificateGroupManage>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别！");
        } else {
            this.marketingCertificateGroupManageService.removeByIds(Arrays.asList(ids.split(",")));
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
    @AutoLog(value = "拼好券管理-通过id查询")
    @ApiOperation(value = "拼好券管理-通过id查询", notes = "拼好券管理-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<MarketingCertificateGroupManage> queryById(@RequestParam(name = "id", required = true) String id) {
        Result<MarketingCertificateGroupManage> result = new Result<MarketingCertificateGroupManage>();
        MarketingCertificateGroupManage marketingCertificateGroupManage = marketingCertificateGroupManageService.getById(id);
        if (marketingCertificateGroupManage == null) {
            result.error500("未找到对应实体");
        } else {
            result.setResult(marketingCertificateGroupManage);
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
        QueryWrapper<MarketingCertificateGroupManage> queryWrapper = null;
        try {
            String paramsStr = request.getParameter("paramsStr");
            if (oConvertUtils.isNotEmpty(paramsStr)) {
                String deString = URLDecoder.decode(paramsStr, "UTF-8");
                MarketingCertificateGroupManage marketingCertificateGroupManage = JSON.parseObject(deString, MarketingCertificateGroupManage.class);
                queryWrapper = QueryGenerator.initQueryWrapper(marketingCertificateGroupManage, request.getParameterMap());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //Step.2 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        List<MarketingCertificateGroupManage> pageList = marketingCertificateGroupManageService.list(queryWrapper);
        //导出文件名称
        mv.addObject(NormalExcelConstants.FILE_NAME, "拼好券管理列表");
        mv.addObject(NormalExcelConstants.CLASS, MarketingCertificateGroupManage.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("拼好券管理列表数据", "导出人:Jeecg", "导出信息"));
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
                List<MarketingCertificateGroupManage> listMarketingCertificateGroupManages = ExcelImportUtil.importExcel(file.getInputStream(), MarketingCertificateGroupManage.class, params);
                marketingCertificateGroupManageService.saveBatch(listMarketingCertificateGroupManages);
                return Result.ok("文件导入成功！数据行数:" + listMarketingCertificateGroupManages.size());
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
     * 详情
     * @param id
     * @return
     */
    @GetMapping("findParticulars")
    public Result<?> findParticulars(@RequestParam(name = "id", required = true) String id) {
        HashMap<String, Object> map = new HashMap<>();
        MarketingCertificateGroupManage marketingCertificateGroupManage = marketingCertificateGroupManageService.getById(id);
        map.put("marketingCertificateGroupManage",marketingCertificateGroupManage);
        map.put("marketingCertificateGroupRecordList",iMarketingCertificateGroupRecordService.getMarketingCertificateGroupManageRecordList(marketingCertificateGroupManage.getId()));
        return Result.ok(map);
    }

    /**
     * 拼团控制
     * @param marketingCertificateGroupManage
     * @return
     */
    @PostMapping("setMarketingCertificateGroupManage")
    public Result<?> setMarketingCertificateGroupManage(@RequestBody MarketingCertificateGroupManage marketingCertificateGroupManage){

        MarketingCertificateGroupManage marketingCertificateGroupManageServiceById = marketingCertificateGroupManageService.getById(marketingCertificateGroupManage.getId());

        List<MarketingCertificateGroupRecord> marketingCertificateGroupRecordList = iMarketingCertificateGroupRecordService.list(new LambdaQueryWrapper<MarketingCertificateGroupRecord>()
                .eq(MarketingCertificateGroupRecord::getDelFlag, "0")
                .eq(MarketingCertificateGroupRecord::getMarketingCertificateGroupManageId, marketingCertificateGroupManageServiceById.getId())
        );

        MarketingCertificateGroupList marketingCertificateGroupList = iMarketingCertificateGroupListService.getById(marketingCertificateGroupManageServiceById.getMarketingCertificateGroupListId());

        marketingCertificateGroupRecordList.forEach(ma->{

            ma.setStatus(marketingCertificateGroupManage.getStatus());
            if (marketingCertificateGroupManage.getStatus().equals("1")){

                MarketingCertificateRecord marketingCertificateRecord = iMarketingCertificateService.generate(marketingCertificateGroupList.getMarketingCertificateId(),
                        "",
                        new BigDecimal(1),
                        ma.getMemberListId(),
                        false).get(0);
                ma.setMarketingCertificateRecordId(marketingCertificateRecord.getId());
                ma.setCloudsTime(new Date());

                iMarketingCertificateRecordService.updateById(marketingCertificateRecord
                        .setRecordType("2")
                        .setActivePrice(ma.getActivityPrice())
                );
            }else {
                PayCertificateLog payCertificateLog = iPayCertificateLogService.getById(ma.getPayCertificateLogId());
                //订单款项退回
                Map<String, Object> response =payUtils.refund(payCertificateLog.getPayPrice(),payCertificateLog.getSerialNumber(),payCertificateLog.getSerialNumber());
                if (payCertificateLog.getBalance().doubleValue()>0){
                    //计算退还资金
                    iMemberListService.addBlance(payCertificateLog.getMemberListId(),payCertificateLog.getBalance(),payCertificateLog.getId(),"14");
                }
                if (payCertificateLog.getWelfarePayments().doubleValue()>0){
                    //退还福利金
                    iMemberWelfarePaymentsService.addWelfarePayments(payCertificateLog.getMemberListId(),payCertificateLog.getWelfarePayments(),"22",payCertificateLog.getId(),"");
                }
            }
            iMarketingCertificateGroupRecordService.updateById(ma);
        });
        if (marketingCertificateGroupManage.getStatus().equals("1")){
            marketingCertificateGroupManage.setCloudsTime(new Date());
        }

        marketingCertificateGroupManageService.updateById(marketingCertificateGroupManage);
        return Result.ok("操作成功");
    }
}
