package org.jeecg.modules.system.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.agency.entity.AgencyManage;
import org.jeecg.modules.agency.service.IAgencyManageService;
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
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
* @Description: 省市区县
* @Author: jeecg-boot
* @Date:   2019-10-13
* @Version: V1.0
*/
@Slf4j
@Api(tags="省市区县")
@RestController
@RequestMapping("/sysArea/sysArea")
public class SysAreaController {
   @Autowired
   private ISysAreaService sysAreaService;
   @Autowired
   private IAgencyManageService agencyManageService;

   @AutoLog("省市区县-返回list集合")
   @ApiOperation(value = "省市区县-返回list集合",notes = "省市区县-返回list集合")
   public Result<List<SysArea>> queryList(SysArea sysArea){
       Result<List<SysArea>> listResult = new Result<>();
       return listResult;
   }
   /**
     * 分页列表查询
    * @param sysArea
    * @param pageNo
    * @param pageSize
    * @param req
    * @return
    */
   @AutoLog(value = "省市区县-分页列表查询")
   @ApiOperation(value="省市区县-分页列表查询", notes="省市区县-分页列表查询")
   @GetMapping(value = "/list")
   public Result<IPage<SysArea>> queryPageList(SysArea sysArea,
                                               @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                               @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                               HttpServletRequest req) {
       Result<IPage<SysArea>> result = new Result<IPage<SysArea>>();
       QueryWrapper<SysArea> queryWrapper = QueryGenerator.initQueryWrapper(sysArea, req.getParameterMap());
       Page<SysArea> page = new Page<SysArea>(pageNo, pageSize);
       IPage<SysArea> pageList = sysAreaService.page(page, queryWrapper);
       result.setSuccess(true);
       result.setResult(pageList);
       return result;
   }

   /**
     *   添加
    * @param sysArea
    * @return
    */
   @AutoLog(value = "省市区县-添加")
   @ApiOperation(value="省市区县-添加", notes="省市区县-添加")
   @PostMapping(value = "/add")
   public Result<SysArea> add(@RequestBody SysArea sysArea) {
       Result<SysArea> result = new Result<SysArea>();
       try {
           sysAreaService.save(sysArea);
           result.success("添加成功！");
       } catch (Exception e) {
           log.error(e.getMessage(),e);
           result.error500("操作失败");
       }
       return result;
   }

   /**
     *  编辑
    * @param sysArea
    * @return
    */
   @AutoLog(value = "省市区县-编辑")
   @ApiOperation(value="省市区县-编辑", notes="省市区县-编辑")
   @PutMapping(value = "/edit")
   public Result<SysArea> edit(@RequestBody SysArea sysArea) {
       Result<SysArea> result = new Result<SysArea>();
       SysArea sysAreaEntity = sysAreaService.getById(sysArea.getId());
       if(sysAreaEntity==null) {
           result.error500("未找到对应实体");
       }else {
           boolean ok = sysAreaService.updateById(sysArea);
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
   @AutoLog(value = "省市区县-通过id删除")
   @ApiOperation(value="省市区县-通过id删除", notes="省市区县-通过id删除")
   @DeleteMapping(value = "/delete")
   public Result<?> delete(@RequestParam(name="id",required=true) String id) {
       try {
           sysAreaService.removeById(id);
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
   @AutoLog(value = "省市区县-批量删除")
   @ApiOperation(value="省市区县-批量删除", notes="省市区县-批量删除")
   @DeleteMapping(value = "/deleteBatch")
   public Result<SysArea> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
       Result<SysArea> result = new Result<SysArea>();
       if(ids==null || "".equals(ids.trim())) {
           result.error500("参数不识别！");
       }else {
           this.sysAreaService.removeByIds(Arrays.asList(ids.split(",")));
           result.success("删除成功!");
       }
       return result;
   }

   /**
     * 通过id查询
    * @param id
    * @return
    */
   @AutoLog(value = "省市区县-通过id查询")
   @ApiOperation(value="省市区县-通过id查询", notes="省市区县-通过id查询")
   @GetMapping(value = "/queryById")
   public Result<SysArea> queryById(@RequestParam(name="id",required=true) String id) {
       Result<SysArea> result = new Result<SysArea>();
       SysArea sysArea = sysAreaService.getById(id);
       if(sysArea==null) {
           result.error500("未找到对应实体");
       }else {
           result.setResult(sysArea);
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
     QueryWrapper<SysArea> queryWrapper = null;
     try {
         String paramsStr = request.getParameter("paramsStr");
         if (oConvertUtils.isNotEmpty(paramsStr)) {
             String deString = URLDecoder.decode(paramsStr, "UTF-8");
             SysArea sysArea = JSON.parseObject(deString, SysArea.class);
             queryWrapper = QueryGenerator.initQueryWrapper(sysArea, request.getParameterMap());
         }
     } catch (UnsupportedEncodingException e) {
         e.printStackTrace();
     }

     //Step.2 AutoPoi 导出Excel
     ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
     List<SysArea> pageList = sysAreaService.list(queryWrapper);
     //导出文件名称
     mv.addObject(NormalExcelConstants.FILE_NAME, "省市区县列表");
     mv.addObject(NormalExcelConstants.CLASS, SysArea.class);
     mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("省市区县列表数据", "导出人:Jeecg", "导出信息"));
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
             List<SysArea> listSysAreas = ExcelImportUtil.importExcel(file.getInputStream(), SysArea.class, params);
             sysAreaService.saveBatch(listSysAreas);
             return Result.ok("文件导入成功！数据行数:" + listSysAreas.size());
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
     * 根据parentId查询列表
     * @param id
     * @return
     */
 @RequestMapping(value = "findByParentId" ,method = RequestMethod.GET)
 public Result<List<SysArea>>findByParentId(@RequestParam (name="id",required=true) Integer id){
     Result<List<SysArea>> listResult = new Result<>();
     List<SysArea> byParentId = sysAreaService.findByParentId(id);
             listResult.setResult(byParentId);
             listResult.setSuccess(true);
     return listResult;
 }

    /**
     *查询省级id
     * @return
     */
 @RequestMapping(value = "getList",method = RequestMethod.GET)
 public Result<List<SysArea>>getList(){
     Result<List<SysArea>> listResult = new Result<>();
     List<SysArea> list = sysAreaService.getList();
     listResult.setResult(list);
     return listResult;
 }

    /**
     *根据level查询列表
     * @return
     */
    @RequestMapping(value = "getAreaList",method = RequestMethod.GET)
    public Result<List<SysArea>>getAreaList(@RequestParam("leve") String leve){
        Result<List<SysArea>> listResult = new Result<>();
        QueryWrapper<SysArea> queryWrapper = new QueryWrapper();
        queryWrapper.eq("leve",leve);
        List<SysArea> list = sysAreaService.list(queryWrapper);
        listResult.setResult(list);
        listResult.setSuccess(true);
        return listResult;
    }

    /**
     * 查询未被代理的父级Id集合
     * @param parentId
     * @return
     */
    /***
     * 根据父级Id 查询区域，并是否过滤代理区域
     * @param parentIds
     * @param isAgency 是否需要过滤代理区域代理，：0 不需要 ，1：过滤
     * @return
     */
    @GetMapping(value = "/getAgencyAreaList")
    public Result<List<Map<String,Object>>> getAgencyAreaList(@RequestParam("parentIds") List<Integer> parentIds,
                                                              @RequestParam("isAgency")String isAgency,
                                                              @RequestParam("agencyManageId")String agencyManageId) {


        Result<List<Map<String,Object>>> listResult = new Result<>();
        List<String> Strings = Lists.newArrayList();
        if(parentIds.size() == 0){
            return listResult;
        }
        QueryWrapper<SysArea> queryWrapper = new QueryWrapper();
        queryWrapper.in("parent_id", parentIds);
        //List<SysArea> list = sysAreaService.list(queryWrapper);
        List<Map<String,Object>> list =  sysAreaService.listMaps(queryWrapper);
        //添加代理区域过滤
        if(isAgency.equals("1") ){


                //遍历是否被选过 isSelected: 0：未代理，1：已代理
                for(Map<String, Object> map:list ){
                   if(map.containsKey("agency_manage_id")){
                       if(map.get("agency_manage_id")!=null && !map.get("agency_manage_id").toString().isEmpty()){
                           if (map.get("agency_manage_id").toString().equals(agencyManageId)){
                               //不过滤当前登录用户的代理地区
                               map.put("isSelected","0");
                           }else{
                               map.put("isSelected","1");
                           }

                       }else{
                           map.put("isSelected","0");
                       }
                   }else{
                       map.put("isSelected","0");
                   }



            }


               }
        listResult.setResult(list);
        listResult.success("查询成功");
        return listResult;
    }

    /**
     * 代理列表回选用
     * @param agencyManageId
     * @return
     */
    @GetMapping(value = "/getparentIdList")
    public Result<Map<String,Object>> getparentIdList(@RequestParam("agencyManageId")String agencyManageId ){

        Result<Map<String,Object>> listResult = new Result<>();
        Map<String,Object> objectMap = Maps.newHashMap();
       AgencyManage agencyManage  = agencyManageService.getById(agencyManageId);
       Map<String,String> map = Maps.newHashMap();
       if(oConvertUtils.isEmpty(agencyManage)){
           listResult.error500("代理用户不存在;");
       return  listResult;
       }
        QueryWrapper<SysArea> queryWrapper = new QueryWrapper();
        queryWrapper.eq("agency_manage_id", agencyManage.getId());
        List<SysArea> list = sysAreaService.list(queryWrapper);
        Map<String,Object> provincialMapList = Maps.newHashMap();
        Map<String,Object> cityMapList = Maps.newHashMap();
        Map<String,Object> countyMapList = Maps.newHashMap();
        String arr = null;
        String arrName =null;
        String sysAreaId = null;
        if(list.size()>0){
            List<String> strlist = Lists.newArrayList();
            List<String> strNamelist = Lists.newArrayList();
            List<String> areaIdlist = Lists.newArrayList();
            //获取代理区域Id
          for(SysArea sa:list){
              strlist.add(sa.getParentId().toString());
              strNamelist.add(sa.getName().toString());
              areaIdlist.add(sa.getId().toString());
          }
          //是县级代理
          if(list.get(0).getLeve() == 2){
            //县级数据
             arr = String.join(",",  strlist.toArray(new String[strlist.size()]));
             arrName = String.join(",",  strNamelist.toArray(new String[strNamelist.size()]));
              sysAreaId = String.join(",",  areaIdlist.toArray(new String[areaIdlist.size()]));
              countyMapList.put("sysAreaParentId",arr);
              countyMapList.put("leve",list.get(0).getLeve());
              countyMapList.put("sysAreaName",arrName);
              countyMapList.put("sysAreaId",sysAreaId);
              //获取市级集合
            cityMapList = sysAreaService.getparentIdList(strlist);
              //省级集合
             if(cityMapList != null){
                 String[] county= cityMapList.get("sysAreaParentId").toString().split(",");
                 List<String> countyList = Arrays.asList(county);
                 provincialMapList = sysAreaService.getparentIdList(countyList);
             }
          }else if(list.get(0).getLeve() ==1 ){
              //市级数据
              arr = String.join(",",  strlist.toArray(new String[strlist.size()]));
              arrName = String.join(",",  strNamelist.toArray(new String[strNamelist.size()]));
              sysAreaId = String.join(",",  areaIdlist.toArray(new String[areaIdlist.size()]));
              cityMapList.put("sysAreaParentId",arr);
              cityMapList.put("leve",list.get(0).getLeve());
              cityMapList.put("sysAreaName",arrName);
              cityMapList.put("sysAreaId",sysAreaId);
              //省级集合
              if(cityMapList != null){
                  String[] county= cityMapList.get("sysAreaParentId").toString().split(",");
                  List<String> countyList = Arrays.asList(county);
                  provincialMapList = sysAreaService.getparentIdList(countyList);
              }
          }else{
              //省级数据
              arr = String.join(",",  strlist.toArray(new String[strlist.size()]));
              arrName = String.join(",",  strNamelist.toArray(new String[strNamelist.size()]));
              sysAreaId = String.join(",",  areaIdlist.toArray(new String[areaIdlist.size()]));
              provincialMapList.put("sysAreaParentId",arr);
              provincialMapList.put("leve",list.get(0).getLeve());
              provincialMapList.put("sysAreaName",arrName);
              provincialMapList.put("sysAreaId",sysAreaId);
          }
            //数据封装
            objectMap.put("leve",list.get(0).getLeve());
            objectMap.put("countyMapList",countyMapList);
            objectMap.put("cityMapList",cityMapList);
            objectMap.put("provincialMapList",provincialMapList);

        }else{
           // listResult.error500("未找到代理数据");
            return listResult;
        }

        listResult.setResult(objectMap);
        listResult.success("代理区域查询成功");
        return listResult;
    }

}
