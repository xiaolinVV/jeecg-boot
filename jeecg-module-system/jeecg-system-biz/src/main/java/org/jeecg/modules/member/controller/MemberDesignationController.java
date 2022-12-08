package org.jeecg.modules.member.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
import org.jeecg.modules.member.dto.MemberDesignationDTO;
import org.jeecg.modules.member.entity.MemberDesignation;
import org.jeecg.modules.member.service.IMemberDesignationService;
import org.jeecg.modules.member.vo.MemberDesignationVO;
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
 * @Description: 称号管理
 * @Author: jeecg-boot
 * @Date: 2020-06-16
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "称号管理")
@RestController
@RequestMapping("/memberDesignation/memberDesignation")
public class MemberDesignationController {
    @Autowired
    private IMemberDesignationService memberDesignationService;

    /**
     * 分页列表查询
     *
     * @param
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "称号管理-分页列表查询")
    @ApiOperation(value = "称号管理-分页列表查询", notes = "称号管理-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<MemberDesignationVO>> queryPageList(MemberDesignationDTO memberDesignationDTO,
                                                            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                            HttpServletRequest req) {
        Result<IPage<MemberDesignationVO>> result = new Result<IPage<MemberDesignationVO>>();
        Page<MemberDesignation> page = new Page<MemberDesignation>(pageNo, pageSize);
        IPage<MemberDesignationVO> pageList = memberDesignationService.queryPageList(page, memberDesignationDTO);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 添加
     *
     * @param memberDesignation
     * @return
     */
    @AutoLog(value = "称号管理-添加")
    @ApiOperation(value = "称号管理-添加", notes = "称号管理-添加")
    @PostMapping(value = "/add")
    public Result<MemberDesignation> add(@RequestBody MemberDesignation memberDesignation) {
        Result<MemberDesignation> result = new Result<MemberDesignation>();
        if (memberDesignationService.count(new LambdaQueryWrapper<MemberDesignation>()
                .eq(MemberDesignation::getDelFlag,"0")
                .eq(MemberDesignation::getSort,memberDesignation.getSort())
                .eq(MemberDesignation::getMemberDesignationGroupId,memberDesignation.getMemberDesignationGroupId())
        )>0){
            return result.error500("级别已存在");
        }
        if (memberDesignation.getIsDefault().equals("1")&&memberDesignationService.count(new LambdaQueryWrapper<MemberDesignation>()
                .eq(MemberDesignation::getDelFlag,"0")
                .eq(MemberDesignation::getIsDefault,memberDesignation.getIsDefault())
                .eq(MemberDesignation::getMemberDesignationGroupId,memberDesignation.getMemberDesignationGroupId())
        )>0){
            return result.error500("默认称号已存在!");
        }
        try {
            if (StringUtils.isBlank(memberDesignation.getMemberDesignationGroupId())&&memberDesignation.getIsDefault().equals("1")){
                memberDesignation.setMemberDesignationGroupId(null);
            }
            memberDesignationService.save(memberDesignation);
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
     * @param memberDesignation
     * @return
     */
    @AutoLog(value = "称号管理-编辑")
    @ApiOperation(value = "称号管理-编辑", notes = "称号管理-编辑")
    @PutMapping(value = "/edit")
    public Result<MemberDesignation> edit(@RequestBody MemberDesignation memberDesignation) {
        Result<MemberDesignation> result = new Result<MemberDesignation>();
        MemberDesignation memberDesignationEntity = memberDesignationService.getById(memberDesignation.getId());
        if (memberDesignationEntity == null) {
            return result.error500("未找到对应实体");
        } else {
            if (StringUtils.isBlank(memberDesignationEntity.getMemberDesignationGroupId())&&memberDesignationEntity.getIsDefault().equals("1")){
                memberDesignation.setMemberDesignationGroupId(null);
            }
            boolean ok = memberDesignationService.updateById(memberDesignation);
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
    @AutoLog(value = "称号管理-通过id删除")
    @ApiOperation(value = "称号管理-通过id删除", notes = "称号管理-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        try {
            memberDesignationService.removeById(id);
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
    @AutoLog(value = "称号管理-批量删除")
    @ApiOperation(value = "称号管理-批量删除", notes = "称号管理-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<MemberDesignation> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        Result<MemberDesignation> result = new Result<MemberDesignation>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别！");
        } else {
            this.memberDesignationService.removeByIds(Arrays.asList(ids.split(",")));
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
    @AutoLog(value = "称号管理-通过id查询")
    @ApiOperation(value = "称号管理-通过id查询", notes = "称号管理-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<MemberDesignation> queryById(@RequestParam(name = "id", required = true) String id) {
        Result<MemberDesignation> result = new Result<MemberDesignation>();
        MemberDesignation memberDesignation = memberDesignationService.getById(id);
        if (memberDesignation == null) {
            result.error500("未找到对应实体");
        } else {
            result.setResult(memberDesignation);
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
        QueryWrapper<MemberDesignation> queryWrapper = null;
        try {
            String paramsStr = request.getParameter("paramsStr");
            if (oConvertUtils.isNotEmpty(paramsStr)) {
                String deString = URLDecoder.decode(paramsStr, "UTF-8");
                MemberDesignation memberDesignation = JSON.parseObject(deString, MemberDesignation.class);
                queryWrapper = QueryGenerator.initQueryWrapper(memberDesignation, request.getParameterMap());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //Step.2 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        List<MemberDesignation> pageList = memberDesignationService.list(queryWrapper);
        //导出文件名称
        mv.addObject(NormalExcelConstants.FILE_NAME, "称号管理列表");
        mv.addObject(NormalExcelConstants.CLASS, MemberDesignation.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("称号管理列表数据", "导出人:Jeecg", "导出信息"));
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
                List<MemberDesignation> listMemberDesignations = ExcelImportUtil.importExcel(file.getInputStream(), MemberDesignation.class, params);
                memberDesignationService.saveBatch(listMemberDesignations);
                return Result.ok("文件导入成功！数据行数:" + listMemberDesignations.size());
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

    @PostMapping("deleteAndExplain")
    public Result<String> deleteAndExplain(@RequestBody MemberDesignation memberDesignation) {
        Result<String> result = new Result<>();
        if (StringUtils.isBlank(memberDesignation.getId())) {
            result.error500("参数错误");
        }
        boolean b = memberDesignationService.update(new MemberDesignation()
                .setDelExplain(memberDesignation.getDelExplain())
                .setDelFlag("1"), new LambdaUpdateWrapper<MemberDesignation>()
                .eq(MemberDesignation::getId, memberDesignation.getId()));
        if (b) {
            return result.success("删除成功");
        } else {
            return result.error500("删除失败");
        }
    }

    @GetMapping("memberDesignationNameList")
    public Result<List<Map<String, Object>>> memberDesignationNameList(String id) {
        Result<List<Map<String, Object>>> result = new Result<>();
        List<Map<String, Object>> maps = memberDesignationService.memberDesignationNameList(id);
        result.success("返回称号名称andId");
        result.setResult(maps);
        return result;
    }
    @GetMapping("getMemberDesignationListBySort")
    public Result<List<Map<String,Object>>> getMemberDesignationListBySort(@RequestParam(name = "sort",required = true)String sort,
                                                                           @RequestParam(name = "memberDesignationGroupId",required = true)String memberDesignationGroupId){
        Result<List<Map<String, Object>>> result = new Result<>();
        result.setResult(memberDesignationService.getMemberDesignationListBySort(sort,memberDesignationGroupId));
        result.success("返回称号名称andId根据级别");
        return result;
    }

}
