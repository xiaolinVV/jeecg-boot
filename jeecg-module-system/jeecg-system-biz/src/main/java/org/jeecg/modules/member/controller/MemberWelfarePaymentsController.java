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
import org.jeecg.modules.member.entity.MemberWelfarePayments;
import org.jeecg.modules.member.service.IMemberWelfarePaymentsService;
import org.jeecg.modules.member.vo.MemberWelfarePaymentsVO;
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
 * @Description: 会员福利金
 * @Author: jeecg-boot
 * @Date: 2019-11-16
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "会员福利金")
@RestController
@RequestMapping("/memberWelfarePayments/memberWelfarePayments")
public class MemberWelfarePaymentsController {
    @Autowired
    private IMemberWelfarePaymentsService memberWelfarePaymentsService;

    /**
     * 分页列表查询
     *
     * @param memberWelfarePayments
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "会员福利金-分页列表查询")
    @ApiOperation(value = "会员福利金-分页列表查询", notes = "会员福利金-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<MemberWelfarePayments>> queryPageList(MemberWelfarePayments memberWelfarePayments,
                                                              @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                              @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                              HttpServletRequest req) {
        Result<IPage<MemberWelfarePayments>> result = new Result<IPage<MemberWelfarePayments>>();
        QueryWrapper<MemberWelfarePayments> queryWrapper = QueryGenerator.initQueryWrapper(memberWelfarePayments, req.getParameterMap());
        Page<MemberWelfarePayments> page = new Page<MemberWelfarePayments>(pageNo, pageSize);
        IPage<MemberWelfarePayments> pageList = memberWelfarePaymentsService.page(page, queryWrapper);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 添加
     *
     * @param memberWelfarePayments
     * @return
     */
    @AutoLog(value = "会员福利金-添加")
    @ApiOperation(value = "会员福利金-添加", notes = "会员福利金-添加")
    @PostMapping(value = "/add")
    public Result<MemberWelfarePayments> add(@RequestBody MemberWelfarePayments memberWelfarePayments) {
        Result<MemberWelfarePayments> result = new Result<MemberWelfarePayments>();
        try {
            memberWelfarePaymentsService.save(memberWelfarePayments);
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
     * @param memberWelfarePayments
     * @return
     */
    @AutoLog(value = "会员福利金-编辑")
    @ApiOperation(value = "会员福利金-编辑", notes = "会员福利金-编辑")
    @PutMapping(value = "/edit")
    public Result<MemberWelfarePayments> edit(@RequestBody MemberWelfarePayments memberWelfarePayments) {
        Result<MemberWelfarePayments> result = new Result<MemberWelfarePayments>();
        MemberWelfarePayments memberWelfarePaymentsEntity = memberWelfarePaymentsService.getById(memberWelfarePayments.getId());
        if (memberWelfarePaymentsEntity == null) {
            result.error500("未找到对应实体");
        } else {
            boolean ok = memberWelfarePaymentsService.updateById(memberWelfarePayments);
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
    @AutoLog(value = "会员福利金-通过id删除")
    @ApiOperation(value = "会员福利金-通过id删除", notes = "会员福利金-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        try {
            memberWelfarePaymentsService.removeById(id);
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
    @AutoLog(value = "会员福利金-批量删除")
    @ApiOperation(value = "会员福利金-批量删除", notes = "会员福利金-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<MemberWelfarePayments> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        Result<MemberWelfarePayments> result = new Result<MemberWelfarePayments>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别！");
        } else {
            this.memberWelfarePaymentsService.removeByIds(Arrays.asList(ids.split(",")));
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
    @AutoLog(value = "会员福利金-通过id查询")
    @ApiOperation(value = "会员福利金-通过id查询", notes = "会员福利金-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<MemberWelfarePayments> queryById(@RequestParam(name = "id", required = true) String id) {
        Result<MemberWelfarePayments> result = new Result<MemberWelfarePayments>();
        MemberWelfarePayments memberWelfarePayments = memberWelfarePaymentsService.getById(id);
        if (memberWelfarePayments == null) {
            result.error500("未找到对应实体");
        } else {
            result.setResult(memberWelfarePayments);
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
        QueryWrapper<MemberWelfarePayments> queryWrapper = null;
        try {
            String paramsStr = request.getParameter("paramsStr");
            if (oConvertUtils.isNotEmpty(paramsStr)) {
                String deString = URLDecoder.decode(paramsStr, "UTF-8");
                MemberWelfarePayments memberWelfarePayments = JSON.parseObject(deString, MemberWelfarePayments.class);
                queryWrapper = QueryGenerator.initQueryWrapper(memberWelfarePayments, request.getParameterMap());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //Step.2 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        List<MemberWelfarePayments> pageList = memberWelfarePaymentsService.list(queryWrapper);
        //导出文件名称
        mv.addObject(NormalExcelConstants.FILE_NAME, "会员福利金列表");
        mv.addObject(NormalExcelConstants.CLASS, MemberWelfarePayments.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("会员福利金列表数据", "导出人:Jeecg", "导出信息"));
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
                List<MemberWelfarePayments> listMemberWelfarePaymentss = ExcelImportUtil.importExcel(file.getInputStream(), MemberWelfarePayments.class, params);
                memberWelfarePaymentsService.saveBatch(listMemberWelfarePaymentss);
                return Result.ok("文件导入成功！数据行数:" + listMemberWelfarePaymentss.size());
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

    @AutoLog(value = "会员福利金-福利金明细")
    @ApiOperation(value = "会员福利金-福利金明细", notes = "会员福利金-福利金明细")
    @GetMapping("findMemberWelfarePayments")
    public Result<IPage<MemberWelfarePaymentsVO>> findMemberWelfarePayments(MemberWelfarePaymentsVO memberWelfarePaymentsVO,
                                                                            @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                                                            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        Result<IPage<MemberWelfarePaymentsVO>> result = new Result<>();
        Page<MemberWelfarePaymentsVO> page = new Page<>(pageNo, pageSize);
        IPage<MemberWelfarePaymentsVO> memberWelfarePayments = memberWelfarePaymentsService.findMemberWelfarePayments(page, memberWelfarePaymentsVO);
        result.setSuccess(true);
        result.setResult(memberWelfarePayments);
        return result;
    }

    @AutoLog(value = "平台福利金支付记录")
    @ApiOperation(value = "平台福利金支付记录", notes = "平台福利金支付记录")
    @GetMapping("findMemberPayRecord")
    public Result<IPage<MemberWelfarePaymentsVO>> findMemberPayRecord(MemberWelfarePaymentsVO memberWelfarePaymentsVO,
                                                                            @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                                                            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        Result<IPage<MemberWelfarePaymentsVO>> result = new Result<>();
        Page<MemberWelfarePaymentsVO> page = new Page<>(pageNo, pageSize);
        IPage<MemberWelfarePaymentsVO> memberWelfarePayments = memberWelfarePaymentsService.findMemberPayRecord(page, memberWelfarePaymentsVO);
        result.setSuccess(true);
        result.setResult(memberWelfarePayments);
        return result;
    }
}
