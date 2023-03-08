package org.jeecg.modules.provider.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.config.util.PermissionUtils;
import org.jeecg.modules.good.entity.GoodList;
import org.jeecg.modules.good.service.IGoodListService;
import org.jeecg.modules.provider.dto.ProviderTemplateDTO;
import org.jeecg.modules.provider.entity.ProviderTemplate;
import org.jeecg.modules.provider.service.IProviderTemplateService;
import org.jeecg.modules.provider.vo.ProviderTemplateVO;
import org.jeecg.modules.system.entity.SysArea;
import org.jeecg.modules.system.service.ISysAreaService;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Description: 供应商运费模板
 * @Author: jeecg-boot
 * @Date: 2019-10-26
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "供应商运费模板")
@RestController
@RequestMapping("/providerTemplate/providerTemplate")
public class ProviderTemplateController {
    @Autowired
    private IProviderTemplateService providerTemplateService;
    @Autowired
    private ISysAreaService sysAreaService;
    @Autowired
    private IGoodListService iGoodListService;


    /**
     * 根据供应商id获取运费模板
     *
     * @param sysUserId
     * @return
     */
    @GetMapping("getProviderTemplateBySysUserId")
    public Result<?> getProviderTemplateBySysUserId(String sysUserId){
        return Result.ok(providerTemplateService.list(new LambdaQueryWrapper<ProviderTemplate>().eq(ProviderTemplate::getSysUserId,sysUserId)));
    }


    /**
     * 分页列表查询
     *
     * @param providerTemplate
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "供应商运费模板-分页列表查询")
    @ApiOperation(value = "供应商运费模板-分页列表查询", notes = "供应商运费模板-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<ProviderTemplate>> queryPageList(ProviderTemplate providerTemplate,
                                                         @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                         @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                         HttpServletRequest req) {
        Result<IPage<ProviderTemplate>> result = new Result<IPage<ProviderTemplate>>();
        QueryWrapper<ProviderTemplate> queryWrapper = QueryGenerator.initQueryWrapper(providerTemplate, req.getParameterMap());
        PermissionUtils.accredit(queryWrapper);
        queryWrapper.orderByDesc("create_time");
        Page<ProviderTemplate> page = new Page<ProviderTemplate>(pageNo, pageSize);
        IPage<ProviderTemplate> pageList = providerTemplateService.page(page, queryWrapper);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }
    /**
     * 分页列表查询
     *
     * @param
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "供应商运费模板-分页列表查询")
    @ApiOperation(value = "供应商运费模板-分页列表查询", notes = "供应商运费模板-分页列表查询")
    @GetMapping(value = "/Test")
    public Result<IPage<ProviderTemplateDTO>> Test(ProviderTemplateVO providerTemplateVO,
                                                         @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                         @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                         HttpServletRequest req) {
        Result<IPage<ProviderTemplateDTO>> result = new Result<IPage<ProviderTemplateDTO>>();
        Page<ProviderTemplateDTO> page = new Page<ProviderTemplateDTO>(pageNo, pageSize);
        IPage<ProviderTemplateDTO> pageList = providerTemplateService.getProviderTemplateList(page, providerTemplateVO);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    @AutoLog(value = "运费模板-查询返回集合")
    @ApiOperation(value = "运费模板-查询返回集合", notes = "运费模板-查询返回集合")
    @RequestMapping(value = "/queryList", method = RequestMethod.GET)
    public Result<List<ProviderTemplateDTO>> queryList(ProviderTemplate providerTemplate,
                                                       String uId,
                                                       HttpServletRequest rep) {
        Result<List<ProviderTemplateDTO>> result = new Result<List<ProviderTemplateDTO>>();
        try {
            List<ProviderTemplateDTO> list = providerTemplateService.getlistProviderTemplate(uId);
            if (list.size() > 0) {
                SysArea sysArea;
                List<SysArea> listsysArea;
                for (ProviderTemplateDTO pt : list) {
                    listsysArea = new ArrayList<SysArea>();
                    String etp = pt.getExemptionPostage();
                    String[] strs = etp.split(",");
                    for (int i = 0; i < strs.length; i++) {
                        sysArea = sysAreaService.getById(strs[i]);
                        if (sysArea != null) {
                            listsysArea.add(sysArea);
                        }
                    }
                    pt.setListsysArea(listsysArea);
                }
            }
            result.setSuccess(true);
            result.setResult(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    @AutoLog(value = "运费模板-供应商新增编辑页面运费模板")
    @ApiOperation(value = "运费模板-供应商新增编辑页面运费模板", notes = "运费模板-供应商新增编辑页面运费模板")
    @RequestMapping(value = "findListByUserId",method = RequestMethod.GET)
    public Result<List<ProviderTemplateDTO>>findListByUserId(){
        Result<List<ProviderTemplateDTO>> result = new Result<>();
        try {
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            String uId = sysUser.getId();
            List<ProviderTemplateDTO> list = providerTemplateService.getlistProviderTemplate(uId);
            if (list.size() > 0) {
                SysArea sysArea;
                List<SysArea> listsysArea;
                for (ProviderTemplateDTO pt : list) {
                    listsysArea = new ArrayList<SysArea>();
                    String etp = pt.getExemptionPostage();
                    String[] strs = etp.split(",");
                    for (int i = 0; i < strs.length; i++) {
                        sysArea = sysAreaService.getById(strs[i]);
                        if (sysArea != null) {
                            listsysArea.add(sysArea);
                        }
                    }
                    pt.setListsysArea(listsysArea);
                }
            }
            result.setSuccess(true);
            result.setResult(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    /**
     * 添加
     *
     * @param providerTemplate
     * @return
     */
    @AutoLog(value = "供应商运费模板-添加")
    @ApiOperation(value = "供应商运费模板-添加", notes = "供应商运费模板-添加")
    @PostMapping(value = "/add")
    public Result<ProviderTemplate> add(@RequestBody ProviderTemplate providerTemplate,
                                        HttpServletRequest request) {
        Result<ProviderTemplate> result = new Result<>();
        try {
            providerTemplateService.updateIsTemplate(providerTemplate);
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
     * @param providerTemplate
     * @return
     */
    @AutoLog(value = "供应商运费模板-编辑")
    @ApiOperation(value = "供应商运费模板-编辑", notes = "供应商运费模板-编辑")
    @PutMapping(value = "/edit")
    public Result<ProviderTemplate> edit(@RequestBody ProviderTemplate providerTemplate) {
        Result<ProviderTemplate> result = new Result<>();
        ProviderTemplate providerTemplateEntity = providerTemplateService.getById(providerTemplate.getId());
        if (providerTemplateEntity == null) {
            result.error500("未找到对应实体");
        } else {
            boolean b = providerTemplateService.updateIsTemplate(providerTemplate);
            //TODO 返回false说明什么？
            if (b) {
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
    @AutoLog(value = "供应商运费模板-通过id删除")
    @ApiOperation(value = "供应商运费模板-通过id删除", notes = "供应商运费模板-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        try {
            providerTemplateService.removeById(id);
        } catch (Exception e) {
            log.error("删除失败", e.getMessage());
            return Result.error("删除失败!");
        }
        return Result.ok("删除成功!");
    }
    @RequestMapping(value = "deleteTemplate",method = RequestMethod.DELETE)
    public Result<?> deleteTemplate(@RequestParam(name = "id", required = true) String id){
        Result<?> result = new Result<>();
        if (iGoodListService.count(new LambdaQueryWrapper<GoodList>()
                .eq(GoodList::getDelFlag,"0")
                .eq(GoodList::getProviderTemplateId,id)
        )<=0){
            providerTemplateService.removeById(id);
            result.setMessage("删除成功");
        }else {
            result.error500("该运费模板绑定了商品不能删除!");
        }
        return result;
    }
    @GetMapping("ifDelete")
    public boolean ifDelete(@RequestParam(value = "id",required = true) String id){

            QueryWrapper<GoodList> goodListQueryWrapper = new QueryWrapper<>();
            goodListQueryWrapper.eq("provider_template_id",id);
            List<GoodList> list = iGoodListService.list(goodListQueryWrapper);
            if (list.size()==0){
                return true;
            }else {
                return false;
            }

    }
    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "供应商运费模板-批量删除")
    @ApiOperation(value = "供应商运费模板-批量删除", notes = "供应商运费模板-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<ProviderTemplate> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        Result<ProviderTemplate> result = new Result<ProviderTemplate>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别！");
        } else {
            this.providerTemplateService.removeByIds(Arrays.asList(ids.split(",")));
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
    @AutoLog(value = "供应商运费模板-通过id查询")
    @ApiOperation(value = "供应商运费模板-通过id查询", notes = "供应商运费模板-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<ProviderTemplate> queryById(@RequestParam(name = "id", required = true) String id) {
        Result<ProviderTemplate> result = new Result<ProviderTemplate>();
        ProviderTemplate providerTemplate = providerTemplateService.getById(id);
        if (providerTemplate == null) {
            result.error500("未找到对应实体");
        } else {
            result.setResult(providerTemplate);
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
        QueryWrapper<ProviderTemplate> queryWrapper = null;
        try {
            String paramsStr = request.getParameter("paramsStr");
            if (oConvertUtils.isNotEmpty(paramsStr)) {
                String deString = URLDecoder.decode(paramsStr, "UTF-8");
                ProviderTemplate providerTemplate = JSON.parseObject(deString, ProviderTemplate.class);
                queryWrapper = QueryGenerator.initQueryWrapper(providerTemplate, request.getParameterMap());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //Step.2 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        List<ProviderTemplate> pageList = providerTemplateService.list(queryWrapper);
        //导出文件名称
        mv.addObject(NormalExcelConstants.FILE_NAME, "供应商运费模板列表");
        mv.addObject(NormalExcelConstants.CLASS, ProviderTemplate.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("供应商运费模板列表数据", "导出人:Jeecg", "导出信息"));
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
                List<ProviderTemplate> listProviderTemplates = ExcelImportUtil.importExcel(file.getInputStream(), ProviderTemplate.class, params);
                providerTemplateService.saveBatch(listProviderTemplates);
                return Result.ok("文件导入成功！数据行数:" + listProviderTemplates.size());
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

}
