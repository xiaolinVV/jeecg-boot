package org.jeecg.modules.store.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.store.dto.StoreTypeDTO;
import org.jeecg.modules.store.entity.StoreType;
import org.jeecg.modules.store.service.IStoreTypeService;
import org.jeecg.modules.store.vo.StoreTypeVO;
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
 * @Description: 店铺分类
 * @Author: jeecg-boot
 * @Date: 2020-08-18
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "店铺分类")
@RestController
@RequestMapping("/storeType/storeType")
public class StoreTypeController {
    @Autowired
    private IStoreTypeService storeTypeService;

    /**
     * 分页列表查询
     *
     * @param
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "店铺分类-分页列表查询")
    @ApiOperation(value = "店铺分类-分页列表查询", notes = "店铺分类-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<StoreTypeVO>> queryPageList(StoreTypeDTO storeTypeDTO,
                                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                    HttpServletRequest req) {
        Result<IPage<StoreTypeVO>> result = new Result<IPage<StoreTypeVO>>();
        Page<StoreTypeVO> page = new Page<StoreTypeVO>(pageNo, pageSize);
        IPage<StoreTypeVO> pageList = storeTypeService.queryPageList(page, storeTypeDTO);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 添加
     *
     * @param storeType
     * @return
     */
    @AutoLog(value = "店铺分类-添加")
    @ApiOperation(value = "店铺分类-添加", notes = "店铺分类-添加")
    @PostMapping(value = "/add")
    public Result<StoreType> add(@RequestBody StoreType storeType) {
        Result<StoreType> result = new Result<StoreType>();
        try {
            storeTypeService.save(storeType);
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
     * @param storeType
     * @return
     */
    @AutoLog(value = "店铺分类-编辑")
    @ApiOperation(value = "店铺分类-编辑", notes = "店铺分类-编辑")
    @PutMapping(value = "/edit")
    public Result<StoreType> edit(@RequestBody StoreType storeType) {
        Result<StoreType> result = new Result<StoreType>();
        StoreType storeTypeEntity = storeTypeService.getById(storeType.getId());
        if (storeTypeEntity == null) {
            result.error500("未找到对应实体");
        } else {
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

            boolean ok = storeTypeService.updateById(storeType
                    .setUpdateBy(sysUser.getUsername())
                    .setUpdateTime(new Date())
            );

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
    @AutoLog(value = "店铺分类-通过id删除")
    @ApiOperation(value = "店铺分类-通过id删除", notes = "店铺分类-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        try {
            storeTypeService.removeById(id);
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
    @AutoLog(value = "店铺分类-批量删除")
    @ApiOperation(value = "店铺分类-批量删除", notes = "店铺分类-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<StoreType> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        Result<StoreType> result = new Result<StoreType>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别！");
        } else {
            this.storeTypeService.removeByIds(Arrays.asList(ids.split(",")));
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
    @AutoLog(value = "店铺分类-通过id查询")
    @ApiOperation(value = "店铺分类-通过id查询", notes = "店铺分类-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<StoreType> queryById(@RequestParam(name = "id", required = true) String id) {
        Result<StoreType> result = new Result<StoreType>();
        StoreType storeType = storeTypeService.getById(id);
        if (storeType == null) {
            result.error500("未找到对应实体");
        } else {
            result.setResult(storeType);
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
        QueryWrapper<StoreType> queryWrapper = null;
        try {
            String paramsStr = request.getParameter("paramsStr");
            if (oConvertUtils.isNotEmpty(paramsStr)) {
                String deString = URLDecoder.decode(paramsStr, "UTF-8");
                StoreType storeType = JSON.parseObject(deString, StoreType.class);
                queryWrapper = QueryGenerator.initQueryWrapper(storeType, request.getParameterMap());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //Step.2 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        List<StoreType> pageList = storeTypeService.list(queryWrapper);
        //导出文件名称
        mv.addObject(NormalExcelConstants.FILE_NAME, "店铺分类列表");
        mv.addObject(NormalExcelConstants.CLASS, StoreType.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("店铺分类列表数据", "导出人:Jeecg", "导出信息"));
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
                List<StoreType> listStoreTypes = ExcelImportUtil.importExcel(file.getInputStream(), StoreType.class, params);
                storeTypeService.saveBatch(listStoreTypes);
                return Result.ok("文件导入成功！数据行数:" + listStoreTypes.size());
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

    /**
     * 添加分类and添加子分类
     *
     * @param storeType
     * @return
     */
    @PostMapping("setStoreType")
    public Result<StoreType> setStoreType(@RequestBody StoreType storeType) {
        if (StringUtils.isBlank(storeType.getPid())) {
            storeType.setPid("0");
            storeType.setLevel(new BigDecimal(1));
        } else {
            storeType.setLevel(new BigDecimal(2));
            StoreType pStoreType = storeTypeService.getById(storeType.getPid());
            if (pStoreType.getHasChild().equals("0")) {
                storeTypeService.saveOrUpdate(pStoreType.setHasChild("1"));
            }
        }
        return this.add(storeType);
    }

    /**
     * 获取下级分类
     *
     * @param id
     * @return
     */
    @GetMapping("getUnderlingList")
    public Result<List<StoreTypeVO>> getUnderlingList(@RequestParam(name = "id", required = true) String id) {
        Result<List<StoreTypeVO>> result = new Result<>();
        result.setResult(storeTypeService.getUnderlingList(id));
        return result;
    }
    @PostMapping("delAndExplain")
    public Result<?> delAndExplain(@RequestBody StoreType storeType){
        StoreType storeType1 = storeTypeService.getById(storeType.getId());
        if (storeType1.getLevel().doubleValue()!=1){
            StoreType pStoreType = storeTypeService.getById(storeType1.getPid());
            if (storeTypeService.count(new LambdaQueryWrapper<StoreType>()
                    .eq(StoreType::getDelFlag,"0")
                    .eq(StoreType::getPid,pStoreType.getId())
            )==1){
                storeTypeService.saveOrUpdate(pStoreType.setHasChild("0"));
            }
        }
        storeTypeService.updateById(storeType);
        boolean b = storeTypeService.removeById(storeType1.getId());
        if (b){
            return Result.ok("删除成功");
        }else {
            return Result.error("删除失败");
        }

    }
    //获取下一级分类
    @GetMapping("getStoreTypeMap")
    public Result<List<Map<String,Object>>> getStoreTypeMap(@RequestParam("pId")String pId){
        Result<List<Map<String, Object>>> result = new Result<>();
        result.setResult(storeTypeService.getStoreTypeMap(pId));
        return result;
    }
}
