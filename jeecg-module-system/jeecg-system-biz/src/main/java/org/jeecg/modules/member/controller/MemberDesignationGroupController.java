package org.jeecg.modules.member.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
import org.jeecg.modules.member.entity.MemberDesignation;
import org.jeecg.modules.member.entity.MemberDesignationCount;
import org.jeecg.modules.member.entity.MemberDesignationGroup;
import org.jeecg.modules.member.entity.MemberDesignationMemberList;
import org.jeecg.modules.member.service.*;
import org.jeecg.modules.member.vo.MemberDesignationGroupVO;
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
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: 团队管理
 * @Author: jeecg-boot
 * @Date: 2021-01-07
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "团队管理")
@RestController
@RequestMapping("/memberDesignationGroup/memberDesignationGroup")
public class MemberDesignationGroupController {
    @Autowired
    private IMemberDesignationGroupService memberDesignationGroupService;
    @Autowired
    private IMemberDesignationService iMemberDesignationService;
    @Autowired
    private IMemberListService iMemberListService;
    @Autowired
    private IMemberDesignationMemberListService iMemberDesignationMemberListService;
    @Autowired
    private IMemberDesignationCountService iMemberDesignationCountService;

    /**
     * 分页列表查询
     *
     * @param memberDesignationGroupVO
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "团队管理-分页列表查询")
    @ApiOperation(value = "团队管理-分页列表查询", notes = "团队管理-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<MemberDesignationGroupVO>> queryPageList(MemberDesignationGroupVO memberDesignationGroupVO,
                                                                 @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                                 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                                 HttpServletRequest req) {
        Result<IPage<MemberDesignationGroupVO>> result = new Result<IPage<MemberDesignationGroupVO>>();
        QueryWrapper<MemberDesignationGroupVO> queryWrapper = QueryGenerator.initQueryWrapper(memberDesignationGroupVO, req.getParameterMap());
        Page<MemberDesignationGroupVO> page = new Page<MemberDesignationGroupVO>(pageNo, pageSize);
        IPage<MemberDesignationGroupVO> pageList = memberDesignationGroupService.queryPageList(page, queryWrapper);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 添加
     *
     * @param memberDesignationGroup
     * @return
     */
    @AutoLog(value = "团队管理-添加")
    @ApiOperation(value = "团队管理-添加", notes = "团队管理-添加")
    @PostMapping(value = "/add")
    public Result<MemberDesignationGroup> add(@RequestBody MemberDesignationGroup memberDesignationGroup) {
        Result<MemberDesignationGroup> result = new Result<MemberDesignationGroup>();
        if (StringUtils.isBlank(memberDesignationGroup.getMemberId())) {
            return result.error500("请选择团队创始人!");
        } else {
            try {
                memberDesignationGroupService.save(memberDesignationGroup);
                MemberDesignation memberDesignation = new MemberDesignation()
                        .setDelFlag("0")
                        .setName("创始人")
                        .setParticipation(new BigDecimal(0))
                        .setStatus("1")
                        .setBalance(new BigDecimal(0))
                        .setSort(new BigDecimal(0))
                        .setIsAverage("0")
                        .setLowLevelDividends("0")
                        .setCustomRemark("团队创始人")
                        .setIsOpenMoney("0")
                        .setMemberDesignationGroupId(memberDesignationGroup.getId())
                        .setIsDefault("1");
                iMemberDesignationService.save(memberDesignation);
                iMemberDesignationMemberListService.save(new MemberDesignationMemberList()
                        .setDelFlag("0")
                        .setMemberListId(memberDesignationGroup.getMemberId())
                        .setMemberDesignationId(memberDesignation.getId())
                        .setIsBuyGift("0")
                        .setMemberJoinTime(new Date())
                        .setMemberDesignationGroupId(memberDesignationGroup.getId())
                );
                LambdaQueryWrapper<MemberDesignation> memberDesignationLambdaQueryWrapper = new LambdaQueryWrapper<MemberDesignation>()
                        .eq(MemberDesignation::getDelFlag, "0")
                        .eq(MemberDesignation::getIsDefault, "1")
                        .isNull(MemberDesignation::getMemberDesignationGroupId);
                MemberDesignation memberDesignations = iMemberDesignationService.list(memberDesignationLambdaQueryWrapper).get(0);
                LambdaQueryWrapper<MemberDesignationMemberList> designationMemberListLambdaQueryWrapper = new LambdaQueryWrapper<MemberDesignationMemberList>()
                        .eq(MemberDesignationMemberList::getDelFlag, "0")
                        .eq(MemberDesignationMemberList::getMemberListId, memberDesignationGroup.getMemberId())
                        .eq(MemberDesignationMemberList::getMemberDesignationId, memberDesignations.getId());
                if (iMemberDesignationMemberListService.count(designationMemberListLambdaQueryWrapper) > 0) {
                    iMemberDesignationMemberListService.remove(designationMemberListLambdaQueryWrapper);
                }
                iMemberDesignationCountService.save(new MemberDesignationCount()
                        .setDelFlag("0")
                        .setMemberListId(memberDesignationGroup.getMemberId())
                        .setMemberDesignationId(memberDesignation.getId())
                        .setTotalMembers(new BigDecimal(1))
                );
                result.success("称号团队创建成功,请前往设置称号!");

            } catch (Exception e) {
                log.error(e.getMessage(), e);
                result.error500("操作失败");
            }
        }
        return result;
    }

    /**
     * 编辑
     *
     * @param memberDesignationGroup
     * @return
     */
    @AutoLog(value = "团队管理-编辑")
    @ApiOperation(value = "团队管理-编辑", notes = "团队管理-编辑")
    @PostMapping(value = "/edit")
    public Result<MemberDesignationGroup> edit(@RequestBody MemberDesignationGroup memberDesignationGroup) {
        Result<MemberDesignationGroup> result = new Result<MemberDesignationGroup>();
        if (StringUtils.isBlank(memberDesignationGroup.getId())) {
            return result.error500("id未传递!");
        }
        MemberDesignationGroup memberDesignationGroupEntity = memberDesignationGroupService.getById(memberDesignationGroup.getId());
        if (memberDesignationGroupEntity == null) {
            result.error500("未找到对应实体");
        } else {
            boolean ok = memberDesignationGroupService.updateById(memberDesignationGroup);
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
    @AutoLog(value = "团队管理-通过id删除")
    @ApiOperation(value = "团队管理-通过id删除", notes = "团队管理-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        try {
            memberDesignationGroupService.removeById(id);
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
    @AutoLog(value = "团队管理-批量删除")
    @ApiOperation(value = "团队管理-批量删除", notes = "团队管理-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<MemberDesignationGroup> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        Result<MemberDesignationGroup> result = new Result<MemberDesignationGroup>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别！");
        } else {
            this.memberDesignationGroupService.removeByIds(Arrays.asList(ids.split(",")));
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
    @AutoLog(value = "团队管理-通过id查询")
    @ApiOperation(value = "团队管理-通过id查询", notes = "团队管理-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<MemberDesignationGroup> queryById(@RequestParam(name = "id", required = true) String id) {
        Result<MemberDesignationGroup> result = new Result<MemberDesignationGroup>();
        MemberDesignationGroup memberDesignationGroup = memberDesignationGroupService.getById(id);
        if (memberDesignationGroup == null) {
            result.error500("未找到对应实体");
        } else {
            result.setResult(memberDesignationGroup);
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
        QueryWrapper<MemberDesignationGroup> queryWrapper = null;
        try {
            String paramsStr = request.getParameter("paramsStr");
            if (oConvertUtils.isNotEmpty(paramsStr)) {
                String deString = URLDecoder.decode(paramsStr, "UTF-8");
                MemberDesignationGroup memberDesignationGroup = JSON.parseObject(deString, MemberDesignationGroup.class);
                queryWrapper = QueryGenerator.initQueryWrapper(memberDesignationGroup, request.getParameterMap());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //Step.2 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        List<MemberDesignationGroup> pageList = memberDesignationGroupService.list(queryWrapper);
        //导出文件名称
        mv.addObject(NormalExcelConstants.FILE_NAME, "团队管理列表");
        mv.addObject(NormalExcelConstants.CLASS, MemberDesignationGroup.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("团队管理列表数据", "导出人:Jeecg", "导出信息"));
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
                List<MemberDesignationGroup> listMemberDesignationGroups = ExcelImportUtil.importExcel(file.getInputStream(), MemberDesignationGroup.class, params);
                memberDesignationGroupService.saveBatch(listMemberDesignationGroups);
                return Result.ok("文件导入成功！数据行数:" + listMemberDesignationGroups.size());
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

    @GetMapping("/findMemberDesignationGroupByName")
    public Result<?> findMemberDesignationGroupByName(String name) {
        return Result.ok(memberDesignationGroupService.findMemberDesignationGroupByName(name));
    }

    @GetMapping("/findMemberDesignationGroupById")
    public Result<?> findMemberDesignationGroupById(String id) {
        MemberDesignation memberDesignation = iMemberDesignationService.getById(id);
        return Result.ok(memberDesignationGroupService.getById(memberDesignation.getMemberDesignationGroupId()));
    }

    /**
     * 判断是否可以删除 1:不能删除 0 :可以删除
     * @param id
     * @return
     */
    @GetMapping("/ifDel")
    public Result<?> ifDel(String id) {
        if (StringUtils.isBlank(id)) {
            return Result.error("id未传递!");
        }
        long count = iMemberDesignationMemberListService.count(new LambdaQueryWrapper<MemberDesignationMemberList>()
                .eq(MemberDesignationMemberList::getDelFlag, "0")
                .eq(MemberDesignationMemberList::getMemberDesignationGroupId, id)
        );
        if (count > 1) {
            return Result.ok("1");
        } else {
            return Result.ok("0");
        }
    }

    /**
     * 删除and说明
     * @param memberDesignationGroup
     * @return
     */
    @PostMapping("delAndexplain")
    public Result<?> delAndexplain(@RequestBody MemberDesignationGroup memberDesignationGroup){
        if (StringUtils.isBlank(memberDesignationGroup.getId())){
            return Result.error("id未传递!");
        }
        memberDesignationGroupService.updateById(memberDesignationGroup);
        memberDesignationGroupService.removeById(memberDesignationGroup.getId());
        return Result.ok("删除成功!");
    }
}
