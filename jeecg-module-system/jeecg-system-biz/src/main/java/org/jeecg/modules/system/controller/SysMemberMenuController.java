package org.jeecg.modules.system.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.entity.SysMemberMenu;
import org.jeecg.modules.system.service.ISysMemberMenuService;
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
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* @Description: 菜单配置表
* @Author: jeecg-boot
* @Date:   2020-06-08
* @Version: V1.0
*/
@Slf4j
@Api(tags="菜单配置表")
@RestController
@RequestMapping("/sysMemberMenu/sysMemberMenu")
public class SysMemberMenuController {
   @Autowired
   private ISysMemberMenuService sysMemberMenuService;

   /**
     * 分页列表查询
    * @param sysMemberMenu
    * @param pageNo
    * @param pageSize
    * @param req
    * @return
    */
   @AutoLog(value = "菜单配置表-分页列表查询")
   @ApiOperation(value="菜单配置表-分页列表查询", notes="菜单配置表-分页列表查询")
   @GetMapping(value = "/list")
   public Result<IPage<SysMemberMenu>> queryPageList(SysMemberMenu sysMemberMenu,
                                     @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                     @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                     HttpServletRequest req) {
       Result<IPage<SysMemberMenu>> result = new Result<IPage<SysMemberMenu>>();
       QueryWrapper<SysMemberMenu> queryWrapper = QueryGenerator.initQueryWrapper(sysMemberMenu, req.getParameterMap());
       Page<SysMemberMenu> page = new Page<SysMemberMenu>(pageNo, pageSize);
       IPage<SysMemberMenu> pageList = sysMemberMenuService.page(page, queryWrapper);
       result.setSuccess(true);
       result.setResult(pageList);
       return result;
   }

   /**
     *   添加
    * @param sysMemberMenu
    * @return
    */
   @AutoLog(value = "菜单配置表-添加")
   @ApiOperation(value="菜单配置表-添加", notes="菜单配置表-添加")
   @PostMapping(value = "/add")
   public Result<SysMemberMenu> add(@RequestBody SysMemberMenu sysMemberMenu) {
       Result<SysMemberMenu> result = new Result<SysMemberMenu>();
       if(sysMemberMenuService.count(new LambdaQueryWrapper<SysMemberMenu>().eq(SysMemberMenu::getStatus,"1"))>=5){
           return result.error500("菜单启用失败，启用的菜单不得超过5个");
       }
       try {
           sysMemberMenuService.save(sysMemberMenu);
           result.success("添加成功！");
       } catch (Exception e) {
           log.error(e.getMessage(),e);
           result.error500("操作失败");
       }
       return result;
   }

   /**
     *  编辑
    * @param sysMemberMenu
    * @return
    */
   @AutoLog(value = "菜单配置表-编辑")
   @ApiOperation(value="菜单配置表-编辑", notes="菜单配置表-编辑")
   @PutMapping(value = "/edit")
   public Result<SysMemberMenu> edit(@RequestBody SysMemberMenu sysMemberMenu) {
       Result<SysMemberMenu> result = new Result<SysMemberMenu>();
       /*if(sysMemberMenu.getStatus().equals("1")){
           if(sysMemberMenuService.count(new LambdaQueryWrapper<SysMemberMenu>().eq(SysMemberMenu::getStatus,"1"))>5){
               return result.error500("菜单启用失败，启用的菜单不得超过5个");
           }
       }
*/
       SysMemberMenu sysMemberMenuEntity = sysMemberMenuService.getById(sysMemberMenu.getId());
       if(sysMemberMenuEntity==null) {
           result.error500("未找到对应实体");
       }else {
           boolean ok = sysMemberMenuService.updateById(sysMemberMenu);
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
   @AutoLog(value = "菜单配置表-通过id删除")
   @ApiOperation(value="菜单配置表-通过id删除", notes="菜单配置表-通过id删除")
   @DeleteMapping(value = "/delete")
   public Result<?> delete(@RequestParam(name="id",required=true) String id) {
       try {
           sysMemberMenuService.removeById(id);
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
   @AutoLog(value = "菜单配置表-批量删除")
   @ApiOperation(value="菜单配置表-批量删除", notes="菜单配置表-批量删除")
   @DeleteMapping(value = "/deleteBatch")
   public Result<SysMemberMenu> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
       Result<SysMemberMenu> result = new Result<SysMemberMenu>();
       if(ids==null || "".equals(ids.trim())) {
           result.error500("参数不识别！");
       }else {
           this.sysMemberMenuService.removeByIds(Arrays.asList(ids.split(",")));
           result.success("删除成功!");
       }
       return result;
   }

   /**
     * 通过id查询
    * @param id
    * @return
    */
   @AutoLog(value = "菜单配置表-通过id查询")
   @ApiOperation(value="菜单配置表-通过id查询", notes="菜单配置表-通过id查询")
   @GetMapping(value = "/queryById")
   public Result<SysMemberMenu> queryById(@RequestParam(name="id",required=true) String id) {
       Result<SysMemberMenu> result = new Result<SysMemberMenu>();
       SysMemberMenu sysMemberMenu = sysMemberMenuService.getById(id);
       if(sysMemberMenu==null) {
           result.error500("未找到对应实体");
       }else {
           result.setResult(sysMemberMenu);
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
     QueryWrapper<SysMemberMenu> queryWrapper = null;
     try {
         String paramsStr = request.getParameter("paramsStr");
         if (oConvertUtils.isNotEmpty(paramsStr)) {
             String deString = URLDecoder.decode(paramsStr, "UTF-8");
             SysMemberMenu sysMemberMenu = JSON.parseObject(deString, SysMemberMenu.class);
             queryWrapper = QueryGenerator.initQueryWrapper(sysMemberMenu, request.getParameterMap());
         }
     } catch (UnsupportedEncodingException e) {
         e.printStackTrace();
     }

     //Step.2 AutoPoi 导出Excel
     ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
     List<SysMemberMenu> pageList = sysMemberMenuService.list(queryWrapper);
     //导出文件名称
     mv.addObject(NormalExcelConstants.FILE_NAME, "菜单配置表列表");
     mv.addObject(NormalExcelConstants.CLASS, SysMemberMenu.class);
     mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("菜单配置表列表数据", "导出人:Jeecg", "导出信息"));
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
             List<SysMemberMenu> listSysMemberMenus = ExcelImportUtil.importExcel(file.getInputStream(), SysMemberMenu.class, params);
             sysMemberMenuService.saveBatch(listSysMemberMenus);
             return Result.ok("文件导入成功！数据行数:" + listSysMemberMenus.size());
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

    /**
     * 批量修改:启用停用
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "菜单配置表-批量修改:启用停用")
    @ApiOperation(value = "菜单配置表-批量修改:启用停用", notes = "菜单配置表-批量修改:启用停用")
    @GetMapping(value = "/updateStatus")
    public Result<SysMemberMenu> updateStatus(@RequestParam(name = "ids", required = true) String ids, @RequestParam(name = "status") String status, String closeExplain) {
        Result<SysMemberMenu> result = new Result<SysMemberMenu>();
        if (StringUtils.isEmpty(ids)) {
            return result.error500("参数不识别！");
        } else {
            SysMemberMenu sysMemberMenu;
            try {
                List<String> listid = Arrays.asList(ids.split(","));
                for (String id : listid) {
                    sysMemberMenu = sysMemberMenuService.getById(id);
                    if (sysMemberMenu == null) {
                        return result.error500("未找到对应实体");
                    } else {
                        if(status.equals("0")){
                            sysMemberMenu.setCloseTime(new Date());
                        }else if(status.equals("1")){
                            Long count = sysMemberMenuService.count(new LambdaQueryWrapper<SysMemberMenu>().eq(SysMemberMenu::getStatus,"1"));
                           if(sysMemberMenuService.count(new LambdaQueryWrapper<SysMemberMenu>().eq(SysMemberMenu::getStatus,"1"))>=5){
                               return result.error500("菜单启用失败，启用的菜单不得超过5个");
                           }
                        }

                        sysMemberMenu.setStatus(status);
                        sysMemberMenu.setCloseExplian(closeExplain);
                        sysMemberMenuService.updateById(sysMemberMenu);


                    }
                }
                result.success("修改成功!");
            } catch (Exception e) {
                result.error500("修改失败！");
            }
        }
        return result;
    }



}
