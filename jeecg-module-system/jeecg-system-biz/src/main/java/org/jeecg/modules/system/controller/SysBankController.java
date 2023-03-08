package org.jeecg.modules.system.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.entity.SysBank;
import org.jeecg.modules.system.service.ISysBankService;
import java.util.Date;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

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
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
* @Description: 银行列表
* @Author: jeecg-boot
* @Date:   2022-08-19
* @Version: V1.0
*/
@Slf4j
@Api(tags="银行列表")
@RestController
@RequestMapping("/system/sysBank")
public class SysBankController {
   @Autowired
   private ISysBankService sysBankService;

   /**
     * 分页列表查询
    * @param sysBank
    * @param pageNo
    * @param pageSize
    * @param req
    * @return
    */
   @AutoLog(value = "银行列表-分页列表查询")
   @ApiOperation(value="银行列表-分页列表查询", notes="银行列表-分页列表查询")
   @GetMapping(value = "/list")
   public Result<IPage<SysBank>> queryPageList(SysBank sysBank,
                                     @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                     @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                     HttpServletRequest req) {
       Result<IPage<SysBank>> result = new Result<IPage<SysBank>>();
       QueryWrapper<SysBank> queryWrapper = QueryGenerator.initQueryWrapper(sysBank, req.getParameterMap());
       Page<SysBank> page = new Page<SysBank>(pageNo, pageSize);
       IPage<SysBank> pageList = sysBankService.page(page, queryWrapper);
       result.setSuccess(true);
       result.setResult(pageList);
       return result;
   }

   /**
     *   添加
    * @param sysBank
    * @return
    */
   @AutoLog(value = "银行列表-添加")
   @ApiOperation(value="银行列表-添加", notes="银行列表-添加")
   @PostMapping(value = "/add")
   public Result<SysBank> add(@RequestBody SysBank sysBank) {
       Result<SysBank> result = new Result<SysBank>();
       try {
           sysBankService.save(sysBank);
           result.success("添加成功！");
       } catch (Exception e) {
           log.error(e.getMessage(),e);
           result.error500("操作失败");
       }
       return result;
   }

   /**
     *  编辑
    * @param sysBank
    * @return
    */
   @AutoLog(value = "银行列表-编辑")
   @ApiOperation(value="银行列表-编辑", notes="银行列表-编辑")
   @PutMapping(value = "/edit")
   public Result<SysBank> edit(@RequestBody SysBank sysBank) {
       Result<SysBank> result = new Result<SysBank>();
       SysBank sysBankEntity = sysBankService.getById(sysBank.getId());
       if(sysBankEntity==null) {
           result.error500("未找到对应实体");
       }else {
           boolean ok = sysBankService.updateById(sysBank);
           //TODO 返回false说明什么？
           if(ok) {
               result.success("修改成功!");
           }
       }

       return result;
   }

   /**
     *   通过id删除
    * @param id
    * @return
    */
   @AutoLog(value = "银行列表-通过id删除")
   @ApiOperation(value="银行列表-通过id删除", notes="银行列表-通过id删除")
   @DeleteMapping(value = "/delete")
   public Result<?> delete(@RequestParam(name="id",required=true) String id) {
       try {
           sysBankService.removeById(id);
       } catch (Exception e) {
           log.error("删除失败",e.getMessage());
           return Result.error("删除失败!");
       }
       return Result.ok("删除成功!");
   }

   /**
     *  批量删除
    * @param ids
    * @return
    */
   @AutoLog(value = "银行列表-批量删除")
   @ApiOperation(value="银行列表-批量删除", notes="银行列表-批量删除")
   @DeleteMapping(value = "/deleteBatch")
   public Result<SysBank> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
       Result<SysBank> result = new Result<SysBank>();
       if(ids==null || "".equals(ids.trim())) {
           result.error500("参数不识别！");
       }else {
           this.sysBankService.removeByIds(Arrays.asList(ids.split(",")));
           result.success("删除成功!");
       }
       return result;
   }

   /**
     * 通过id查询
    * @param id
    * @return
    */
   @AutoLog(value = "银行列表-通过id查询")
   @ApiOperation(value="银行列表-通过id查询", notes="银行列表-通过id查询")
   @GetMapping(value = "/queryById")
   public Result<SysBank> queryById(@RequestParam(name="id",required=true) String id) {
       Result<SysBank> result = new Result<SysBank>();
       SysBank sysBank = sysBankService.getById(id);
       if(sysBank==null) {
           result.error500("未找到对应实体");
       }else {
           result.setResult(sysBank);
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
     QueryWrapper<SysBank> queryWrapper = null;
     try {
         String paramsStr = request.getParameter("paramsStr");
         if (oConvertUtils.isNotEmpty(paramsStr)) {
             String deString = URLDecoder.decode(paramsStr, "UTF-8");
             SysBank sysBank = JSON.parseObject(deString, SysBank.class);
             queryWrapper = QueryGenerator.initQueryWrapper(sysBank, request.getParameterMap());
         }
     } catch (UnsupportedEncodingException e) {
         e.printStackTrace();
     }

     //Step.2 AutoPoi 导出Excel
     ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
     List<SysBank> pageList = sysBankService.list(queryWrapper);
     //导出文件名称
     mv.addObject(NormalExcelConstants.FILE_NAME, "银行列表列表");
     mv.addObject(NormalExcelConstants.CLASS, SysBank.class);
     mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("银行列表列表数据", "导出人:Jeecg", "导出信息"));
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
             List<SysBank> listSysBanks = ExcelImportUtil.importExcel(file.getInputStream(), SysBank.class, params);
             sysBankService.saveBatch(listSysBanks);
             return Result.ok("文件导入成功！数据行数:" + listSysBanks.size());
         } catch (Exception e) {
             log.error(e.getMessage(),e);
             return Result.error("文件导入失败:"+e.getMessage());
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
