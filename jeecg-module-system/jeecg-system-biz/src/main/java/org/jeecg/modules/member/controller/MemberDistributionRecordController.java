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
import org.jeecg.modules.marketing.dto.MemberDistributionDTO;
import org.jeecg.modules.member.entity.MemberDistributionRecord;
import org.jeecg.modules.member.service.IMemberDistributionRecordService;
import org.jeecg.modules.member.vo.MemberDistributionRecordVO;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 会员分销明细
 * @Author: jeecg-boot
 * @Date: 2020-01-07
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "会员分销明细")
@RestController
@RequestMapping("/memberDistributionRecord/memberDistributionRecord")
public class MemberDistributionRecordController {
    @Autowired
    private IMemberDistributionRecordService memberDistributionRecordService;

    /**
     * 分页列表查询
     *
     * @param memberDistributionRecord
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "会员分销明细-分页列表查询")
    @ApiOperation(value = "会员分销明细-分页列表查询", notes = "会员分销明细-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<MemberDistributionRecord>> queryPageList(MemberDistributionRecord memberDistributionRecord,
                                                                 @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                                 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                                 HttpServletRequest req) {
        Result<IPage<MemberDistributionRecord>> result = new Result<IPage<MemberDistributionRecord>>();
        QueryWrapper<MemberDistributionRecord> queryWrapper = QueryGenerator.initQueryWrapper(memberDistributionRecord, req.getParameterMap());
        Page<MemberDistributionRecord> page = new Page<MemberDistributionRecord>(pageNo, pageSize);
        IPage<MemberDistributionRecord> pageList = memberDistributionRecordService.page(page, queryWrapper);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 添加
     *
     * @param memberDistributionRecord
     * @return
     */
    @AutoLog(value = "会员分销明细-添加")
    @ApiOperation(value = "会员分销明细-添加", notes = "会员分销明细-添加")
    @PostMapping(value = "/add")
    public Result<MemberDistributionRecord> add(@RequestBody MemberDistributionRecord memberDistributionRecord) {
        Result<MemberDistributionRecord> result = new Result<MemberDistributionRecord>();
        try {
            memberDistributionRecordService.save(memberDistributionRecord);
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
     * @param memberDistributionRecord
     * @return
     */
    @AutoLog(value = "会员分销明细-编辑")
    @ApiOperation(value = "会员分销明细-编辑", notes = "会员分销明细-编辑")
    @PutMapping(value = "/edit")
    public Result<MemberDistributionRecord> edit(@RequestBody MemberDistributionRecord memberDistributionRecord) {
        Result<MemberDistributionRecord> result = new Result<MemberDistributionRecord>();
        MemberDistributionRecord memberDistributionRecordEntity = memberDistributionRecordService.getById(memberDistributionRecord.getId());
        if (memberDistributionRecordEntity == null) {
            result.error500("未找到对应实体");
        } else {
            boolean ok = memberDistributionRecordService.updateById(memberDistributionRecord);
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
    @AutoLog(value = "会员分销明细-通过id删除")
    @ApiOperation(value = "会员分销明细-通过id删除", notes = "会员分销明细-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        try {
            memberDistributionRecordService.removeById(id);
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
    @AutoLog(value = "会员分销明细-批量删除")
    @ApiOperation(value = "会员分销明细-批量删除", notes = "会员分销明细-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<MemberDistributionRecord> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        Result<MemberDistributionRecord> result = new Result<MemberDistributionRecord>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别！");
        } else {
            this.memberDistributionRecordService.removeByIds(Arrays.asList(ids.split(",")));
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
    @AutoLog(value = "会员分销明细-通过id查询")
    @ApiOperation(value = "会员分销明细-通过id查询", notes = "会员分销明细-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<MemberDistributionRecord> queryById(@RequestParam(name = "id", required = true) String id) {
        Result<MemberDistributionRecord> result = new Result<MemberDistributionRecord>();
        MemberDistributionRecord memberDistributionRecord = memberDistributionRecordService.getById(id);
        if (memberDistributionRecord == null) {
            result.error500("未找到对应实体");
        } else {
            result.setResult(memberDistributionRecord);
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
        QueryWrapper<MemberDistributionRecord> queryWrapper = null;
        try {
            String paramsStr = request.getParameter("paramsStr");
            if (oConvertUtils.isNotEmpty(paramsStr)) {
                String deString = URLDecoder.decode(paramsStr, "UTF-8");
                MemberDistributionRecord memberDistributionRecord = JSON.parseObject(deString, MemberDistributionRecord.class);
                queryWrapper = QueryGenerator.initQueryWrapper(memberDistributionRecord, request.getParameterMap());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //Step.2 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        List<MemberDistributionRecord> pageList = memberDistributionRecordService.list(queryWrapper);
        //导出文件名称
        mv.addObject(NormalExcelConstants.FILE_NAME, "会员分销明细列表");
        mv.addObject(NormalExcelConstants.CLASS, MemberDistributionRecord.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("会员分销明细列表数据", "导出人:Jeecg", "导出信息"));
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
                List<MemberDistributionRecord> listMemberDistributionRecords = ExcelImportUtil.importExcel(file.getInputStream(), MemberDistributionRecord.class, params);
                memberDistributionRecordService.saveBatch(listMemberDistributionRecords);
                return Result.ok("文件导入成功！数据行数:" + listMemberDistributionRecords.size());
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

    @AutoLog(value = "分销记录-佣金明细")
    @ApiOperation(value = "分销记录-佣金明细", notes = "分销记录-佣金明细")
    @GetMapping("findMemberDistributionRecordVOById")
    public Result<Map<String, Object>> findMemberDistributionRecordVOById(@RequestParam(value = "mId", required = true) String mId) {
        Result<Map<String, Object>> result = new Result<>();
        List<MemberDistributionRecordVO> memberDistributionRecordVOById = memberDistributionRecordService.findMemberDistributionRecordVOById(mId);
        HashMap<String, Object> map = new HashMap<>();
        map.put("records", memberDistributionRecordVOById);
        result.setSuccess(true);
        result.setResult(map);
        return result;
    }
    @AutoLog(value = "分销记录-成员")
    @ApiOperation(value = "分销记录-成员", notes = "分销记录-成员")
    @GetMapping("findById")
    public Result<Map<String,Object>>findById(@RequestParam(value = "mId",required = true) String mId){
        Result<Map<String, Object>> result = new Result<>();
        List<MemberDistributionDTO> byId = memberDistributionRecordService.findById(mId);
        HashMap<String, Object> map = new HashMap<>();
        map.put("records",byId);
        result.setSuccess(true);
        result.setResult(map);
        return result;
    }
}
