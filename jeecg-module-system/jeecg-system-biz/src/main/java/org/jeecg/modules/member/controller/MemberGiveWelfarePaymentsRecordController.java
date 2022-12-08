package org.jeecg.modules.member.controller;

import com.alibaba.fastjson.JSON;
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
import org.jeecg.modules.member.entity.MemberGiveWelfarePaymentsRecord;
import org.jeecg.modules.member.service.IMemberGiveWelfarePaymentsRecordService;
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
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Description: 福利金可获赠数量记录
 * @Author: jeecg-boot
 * @Date: 2021-08-17
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "福利金可获赠数量记录")
@RestController
@RequestMapping("/memberGiveWelfarePaymentsRecord/memberGiveWelfarePaymentsRecord")
public class MemberGiveWelfarePaymentsRecordController {
    @Autowired
    private IMemberGiveWelfarePaymentsRecordService memberGiveWelfarePaymentsRecordService;



    /**
     * 分页列表查询
     *
     * @param memberGiveWelfarePaymentsRecord
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "福利金可获赠数量记录-分页列表查询")
    @ApiOperation(value = "福利金可获赠数量记录-分页列表查询", notes = "福利金可获赠数量记录-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<MemberGiveWelfarePaymentsRecord>> queryPageList(MemberGiveWelfarePaymentsRecord memberGiveWelfarePaymentsRecord,
                                                                        @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                                        @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                                        HttpServletRequest req) {
        Result<IPage<MemberGiveWelfarePaymentsRecord>> result = new Result<IPage<MemberGiveWelfarePaymentsRecord>>();
        QueryWrapper<MemberGiveWelfarePaymentsRecord> queryWrapper = QueryGenerator.initQueryWrapper(memberGiveWelfarePaymentsRecord, req.getParameterMap());
        Page<MemberGiveWelfarePaymentsRecord> page = new Page<MemberGiveWelfarePaymentsRecord>(pageNo, pageSize);
        IPage<MemberGiveWelfarePaymentsRecord> pageList = memberGiveWelfarePaymentsRecordService.page(page, queryWrapper);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 添加
     *
     * @param memberGiveWelfarePaymentsRecord
     * @return
     */
    @AutoLog(value = "福利金可获赠数量记录-添加")
    @ApiOperation(value = "福利金可获赠数量记录-添加", notes = "福利金可获赠数量记录-添加")
    @PostMapping(value = "/add")
    public Result<MemberGiveWelfarePaymentsRecord> add(@RequestBody MemberGiveWelfarePaymentsRecord memberGiveWelfarePaymentsRecord) {
        Result<MemberGiveWelfarePaymentsRecord> result = new Result<MemberGiveWelfarePaymentsRecord>();
        try {
            memberGiveWelfarePaymentsRecordService.save(memberGiveWelfarePaymentsRecord);
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
     * @param memberGiveWelfarePaymentsRecord
     * @return
     */
    @AutoLog(value = "福利金可获赠数量记录-编辑")
    @ApiOperation(value = "福利金可获赠数量记录-编辑", notes = "福利金可获赠数量记录-编辑")
    @PutMapping(value = "/edit")
    public Result<MemberGiveWelfarePaymentsRecord> edit(@RequestBody MemberGiveWelfarePaymentsRecord memberGiveWelfarePaymentsRecord) {
        Result<MemberGiveWelfarePaymentsRecord> result = new Result<MemberGiveWelfarePaymentsRecord>();
        MemberGiveWelfarePaymentsRecord memberGiveWelfarePaymentsRecordEntity = memberGiveWelfarePaymentsRecordService.getById(memberGiveWelfarePaymentsRecord.getId());
        if (memberGiveWelfarePaymentsRecordEntity == null) {
            result.error500("未找到对应实体");
        } else {
            boolean ok = memberGiveWelfarePaymentsRecordService.updateById(memberGiveWelfarePaymentsRecord);
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
    @AutoLog(value = "福利金可获赠数量记录-通过id删除")
    @ApiOperation(value = "福利金可获赠数量记录-通过id删除", notes = "福利金可获赠数量记录-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        try {
            memberGiveWelfarePaymentsRecordService.removeById(id);
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
    @AutoLog(value = "福利金可获赠数量记录-批量删除")
    @ApiOperation(value = "福利金可获赠数量记录-批量删除", notes = "福利金可获赠数量记录-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<MemberGiveWelfarePaymentsRecord> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        Result<MemberGiveWelfarePaymentsRecord> result = new Result<MemberGiveWelfarePaymentsRecord>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别！");
        } else {
            this.memberGiveWelfarePaymentsRecordService.removeByIds(Arrays.asList(ids.split(",")));
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
    @AutoLog(value = "福利金可获赠数量记录-通过id查询")
    @ApiOperation(value = "福利金可获赠数量记录-通过id查询", notes = "福利金可获赠数量记录-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<MemberGiveWelfarePaymentsRecord> queryById(@RequestParam(name = "id", required = true) String id) {
        Result<MemberGiveWelfarePaymentsRecord> result = new Result<MemberGiveWelfarePaymentsRecord>();
        MemberGiveWelfarePaymentsRecord memberGiveWelfarePaymentsRecord = memberGiveWelfarePaymentsRecordService.getById(id);
        if (memberGiveWelfarePaymentsRecord == null) {
            result.error500("未找到对应实体");
        } else {
            result.setResult(memberGiveWelfarePaymentsRecord);
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
        QueryWrapper<MemberGiveWelfarePaymentsRecord> queryWrapper = null;
        try {
            String paramsStr = request.getParameter("paramsStr");
            if (oConvertUtils.isNotEmpty(paramsStr)) {
                String deString = URLDecoder.decode(paramsStr, "UTF-8");
                MemberGiveWelfarePaymentsRecord memberGiveWelfarePaymentsRecord = JSON.parseObject(deString, MemberGiveWelfarePaymentsRecord.class);
                queryWrapper = QueryGenerator.initQueryWrapper(memberGiveWelfarePaymentsRecord, request.getParameterMap());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //Step.2 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        List<MemberGiveWelfarePaymentsRecord> pageList = memberGiveWelfarePaymentsRecordService.list(queryWrapper);
        //导出文件名称
        mv.addObject(NormalExcelConstants.FILE_NAME, "福利金可获赠数量记录列表");
        mv.addObject(NormalExcelConstants.CLASS, MemberGiveWelfarePaymentsRecord.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("福利金可获赠数量记录列表数据", "导出人:Jeecg", "导出信息"));
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
                List<MemberGiveWelfarePaymentsRecord> listMemberGiveWelfarePaymentsRecords = ExcelImportUtil.importExcel(file.getInputStream(), MemberGiveWelfarePaymentsRecord.class, params);
                memberGiveWelfarePaymentsRecordService.saveBatch(listMemberGiveWelfarePaymentsRecords);
                return Result.ok("文件导入成功！数据行数:" + listMemberGiveWelfarePaymentsRecords.size());
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

    @PostMapping("MemberGiveWelfarePaymentsRecordAdd")
    public Result<?> MemberGiveWelfarePaymentsRecordAdd(@RequestBody MemberGiveWelfarePaymentsRecord memberGiveWelfarePaymentsRecord) {
        if (memberGiveWelfarePaymentsRecord.getWelfarePayments().doubleValue()<=0){
            return Result.error("交易额必须大于0!");
        }
        if (memberGiveWelfarePaymentsRecordService.setMemberGiveWelfarePaymentsRecord(memberGiveWelfarePaymentsRecord)){
            return Result.ok("成功");
        }else {
            return Result.error("失败");
        }
    }
}
