package org.jeecg.modules.member.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.member.dto.MemberGradeDTO;
import org.jeecg.modules.member.entity.MemberGrade;
import org.jeecg.modules.member.service.IMemberGradeService;
import org.jeecg.modules.member.vo.MemberGradeVO;
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
 * @Description: 会员等级
 * @Author: jeecg-boot
 * @Date: 2020-06-11
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "会员等级")
@RestController
@RequestMapping("/memberGrade/memberGrade")
public class MemberGradeController {
    @Autowired
    private IMemberGradeService memberGradeService;

    /**
     * 分页列表查询
     *
     * @param
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "会员等级-分页列表查询")
    @ApiOperation(value = "会员等级-分页列表查询", notes = "会员等级-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<MemberGradeVO>> queryPageList(MemberGradeDTO memberGradeDTO,
                                                      @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                      @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                      HttpServletRequest req) {
        Result<IPage<MemberGradeVO>> result = new Result<IPage<MemberGradeVO>>();
        Page<MemberGrade> page = new Page<MemberGrade>(pageNo, pageSize);
        IPage<MemberGradeVO> pageList = memberGradeService.queryPageList(page, memberGradeDTO);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 添加
     *
     * @param memberGrade
     * @return
     */
    @AutoLog(value = "会员等级-添加")
    @ApiOperation(value = "会员等级-添加", notes = "会员等级-添加")
    @PostMapping(value = "/add")
    public Result<MemberGrade> add(@RequestBody MemberGrade memberGrade) {
        Result<MemberGrade> result = new Result<MemberGrade>();
        try {
            memberGradeService.save(memberGrade);
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
     * @param memberGrade
     * @return
     */
    @AutoLog(value = "会员等级-编辑")
    @ApiOperation(value = "会员等级-编辑", notes = "会员等级-编辑")
    @PutMapping(value = "/edit")
    public Result<MemberGrade> edit(@RequestBody MemberGrade memberGrade) {
        Result<MemberGrade> result = new Result<MemberGrade>();
        MemberGrade memberGradeEntity = memberGradeService.getById(memberGrade.getId());
        if (memberGradeEntity == null) {
            result.error500("未找到对应实体");
        } else {
            boolean ok = memberGradeService.updateById(memberGrade);
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
    @AutoLog(value = "会员等级-通过id删除")
    @ApiOperation(value = "会员等级-通过id删除", notes = "会员等级-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        try {
            memberGradeService.removeById(id);
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
    @AutoLog(value = "会员等级-批量删除")
    @ApiOperation(value = "会员等级-批量删除", notes = "会员等级-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<MemberGrade> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        Result<MemberGrade> result = new Result<MemberGrade>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别！");
        } else {
            this.memberGradeService.removeByIds(Arrays.asList(ids.split(",")));
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
    @AutoLog(value = "会员等级-通过id查询")
    @ApiOperation(value = "会员等级-通过id查询", notes = "会员等级-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<MemberGrade> queryById(@RequestParam(name = "id", required = true) String id) {
        Result<MemberGrade> result = new Result<MemberGrade>();
        MemberGrade memberGrade = memberGradeService.getById(id);
        if (memberGrade == null) {
            result.error500("未找到对应实体");
        } else {
            result.setResult(memberGrade);
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
        QueryWrapper<MemberGrade> queryWrapper = null;
        try {
            String paramsStr = request.getParameter("paramsStr");
            if (oConvertUtils.isNotEmpty(paramsStr)) {
                String deString = URLDecoder.decode(paramsStr, "UTF-8");
                MemberGrade memberGrade = JSON.parseObject(deString, MemberGrade.class);
                queryWrapper = QueryGenerator.initQueryWrapper(memberGrade, request.getParameterMap());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //Step.2 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        List<MemberGrade> pageList = memberGradeService.list(queryWrapper);
        //导出文件名称
        mv.addObject(NormalExcelConstants.FILE_NAME, "会员等级列表");
        mv.addObject(NormalExcelConstants.CLASS, MemberGrade.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("会员等级列表数据", "导出人:Jeecg", "导出信息"));
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
                List<MemberGrade> listMemberGrades = ExcelImportUtil.importExcel(file.getInputStream(), MemberGrade.class, params);
                memberGradeService.saveBatch(listMemberGrades);
                return Result.ok("文件导入成功！数据行数:" + listMemberGrades.size());
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

    @GetMapping("findMemberGradeList")
    public Result<List<Map<String, Object>>> findMemberGradeListMap() {
        Result<List<Map<String, Object>>> result = new Result<>();
        result.setResult(memberGradeService.findMemberGradeListMap());
        result.success("返回会员等级");
        return result;
    }

    @PostMapping("deleteAndExplain")
    public Result<String> deleteAndExplain(@RequestBody MemberGrade memberGrade) {
        Result<String> result = new Result<>();
        if (StringUtils.isBlank(memberGrade.getId())) {
            result.error500("参数错误");
        }
        boolean b = memberGradeService.update(new MemberGrade()
                .setDelExplain(memberGrade.getDelExplain())
                .setDelFlag("1"), new LambdaUpdateWrapper<MemberGrade>()
                .eq(MemberGrade::getId, memberGrade.getId()));
        if (b) {
            return result.success("删除成功");
        } else {
            return result.error500("删除失败");
        }
    }
}
