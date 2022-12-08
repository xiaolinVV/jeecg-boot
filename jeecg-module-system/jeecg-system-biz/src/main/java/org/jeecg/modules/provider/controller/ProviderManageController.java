package org.jeecg.modules.provider.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
import org.jeecg.modules.order.entity.OrderProviderList;
import org.jeecg.modules.order.service.IOrderProviderListService;
import org.jeecg.modules.provider.dto.ProviderManageDTO;
import org.jeecg.modules.provider.entity.ProviderManage;
import org.jeecg.modules.provider.service.IProviderManageService;
import org.jeecg.modules.provider.vo.ProviderManageVO;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.BeanUtils;
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
 * @Description: 供应商列表
 * @Author:
 * @Date: 2020-01-02
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "供应商列表")
@RestController
@RequestMapping("/providerManage/providerManage")
public class ProviderManageController {
    @Autowired
    private IProviderManageService providerManageService;
    @Autowired
    private IOrderProviderListService iOrderProviderListService;
    /**
     * 分页列表查询
     *
     * @param
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "供应商列表-分页列表查询")
    @ApiOperation(value = "供应商列表-分页列表查询", notes = "供应商列表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<ProviderManageVO>> queryPageList(ProviderManageDTO providerManageDTO,
                                                       @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                       @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                       HttpServletRequest req) {
        Result<IPage<ProviderManageVO>> result = new Result<IPage<ProviderManageVO>>();
        Page<ProviderManage> page = new Page<ProviderManage>(pageNo, pageSize);
        IPage<ProviderManageVO> pageList = providerManageService.queryPageList(page,providerManageDTO);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 添加
     *
     * @param providerManage
     * @return
     */
    @AutoLog(value = "供应商列表-添加")
    @ApiOperation(value = "供应商列表-添加", notes = "供应商列表-添加")
    @PostMapping(value = "/add")
    public Result<ProviderManage> add(@RequestBody ProviderManage providerManage) {
        Result<ProviderManage> result = new Result<ProviderManage>();
        try {
            providerManageService.save(providerManage);
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
     * @param providerManageVO
     * @return
     */
    @AutoLog(value = "供应商列表-编辑")
    @ApiOperation(value = "供应商列表-编辑", notes = "供应商列表-编辑")
    @PutMapping(value = "/edit")
    public Result<ProviderManageDTO> edit(@RequestBody ProviderManageVO providerManageVO) {
        Result<ProviderManageDTO> result = new Result<ProviderManageDTO>();
        ProviderManage providerManageEntity = providerManageService.getById(providerManageVO.getId());
        if (providerManageEntity == null) {
            result.error500("未找到对应实体");
        } else {
            ProviderManage providerManage = new ProviderManage();
            BeanUtils.copyProperties(providerManageVO,providerManage);
            boolean ok = providerManageService.updateById(providerManage);
            if (ok) {
                result.success("修改成功!");
            }else {
                result.error500("修改失败!");
            }
        }

        return result;
    }
    /**
     * 编辑
     *
     * @param providerManageVO
     * @return
     */
    @AutoLog(value = "供应商信息-编辑")
    @ApiOperation(value = "供应商信息-编辑", notes = "供应商信息-编辑")
    @PutMapping(value = "/editProviderManage")
    public Result<ProviderManageDTO> editProviderManage(@RequestBody ProviderManageVO providerManageVO) {
        return providerManageService.editProviderManage(providerManageVO);
    }
    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "供应商列表-通过id删除")
    @ApiOperation(value = "供应商列表-通过id删除", notes = "供应商列表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        try {
            long count = iOrderProviderListService.count(new LambdaQueryWrapper<OrderProviderList>()
                    .eq(OrderProviderList::getSysUserId, providerManageService.list(new LambdaQueryWrapper<ProviderManage>()
                            .eq(ProviderManage::getId, id)).get(0).getSysUserId()));
            if (count>0){
                return Result.error("该供应商存在未完成的订单、上架的商品及可用余额,请先处理!");
            }else {
                providerManageService.removeById(id);
            }

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
    @AutoLog(value = "供应商列表-批量删除")
    @ApiOperation(value = "供应商列表-批量删除", notes = "供应商列表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<ProviderManage> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        Result<ProviderManage> result = new Result<ProviderManage>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别！");
        } else {
            this.providerManageService.removeByIds(Arrays.asList(ids.split(",")));
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
    @AutoLog(value = "供应商列表-通过id查询")
    @ApiOperation(value = "供应商列表-通过id查询", notes = "供应商列表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<ProviderManage> queryById(@RequestParam(name = "id", required = true) String id) {
        Result<ProviderManage> result = new Result<ProviderManage>();
        ProviderManage providerManage = providerManageService.getById(id);
        if (providerManage == null) {
            result.error500("未找到对应实体");
        } else {
            result.setResult(providerManage);
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
        QueryWrapper<ProviderManage> queryWrapper = null;
        try {
            String paramsStr = request.getParameter("paramsStr");
            if (oConvertUtils.isNotEmpty(paramsStr)) {
                String deString = URLDecoder.decode(paramsStr, "UTF-8");
                ProviderManage providerManage = JSON.parseObject(deString, ProviderManage.class);
                queryWrapper = QueryGenerator.initQueryWrapper(providerManage, request.getParameterMap());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //Step.2 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        List<ProviderManage> pageList = providerManageService.list(queryWrapper);
        //导出文件名称
        mv.addObject(NormalExcelConstants.FILE_NAME, "供应商列表列表");
        mv.addObject(NormalExcelConstants.CLASS, ProviderManage.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("供应商列表列表数据", "导出人:Jeecg", "导出信息"));
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
                List<ProviderManage> listProviderManages = ExcelImportUtil.importExcel(file.getInputStream(), ProviderManage.class, params);
                providerManageService.saveBatch(listProviderManages);
                return Result.ok("文件导入成功！数据行数:" + listProviderManages.size());
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

    @AutoLog(value = "供应商列表")
    @ApiOperation(value = "供应商列表", notes = "供应商列表")
    @GetMapping(value = "/findProviderManage")
    public Result<IPage<ProviderManageDTO>> findProviderManage(ProviderManageVO ProviderManageVO,
                                                               @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                               @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                               HttpServletRequest req) {
        Result<IPage<ProviderManageDTO>> result = new Result<IPage<ProviderManageDTO>>();
        Page<ProviderManageDTO> page = new Page<ProviderManageDTO>(pageNo, pageSize);
        IPage<ProviderManageDTO> skr = providerManageService.findProviderManage(page, ProviderManageVO);
        result.setSuccess(true);
        result.setResult(skr);
        return result;
    }

    @AutoLog(value = "返回供应商信息")
    @ApiOperation(value = "返回供应商信息", notes = "返回供应商信息")
    @GetMapping(value = "/returnProvider")
    public Result<ProviderManageVO> returnProvider() {
        return providerManageService.returnProvider();
    }

	 @AutoLog(value = "店铺-商品调用接口")
	 @ApiOperation(value = "店铺-商品调用接口", notes = "店铺-商品调用接口")
	 @RequestMapping(value = "findGoodProviderById",method = RequestMethod.GET)
	 public Result<ProviderManage>findGoodProviderById(@RequestParam(value = "userId",required = true)String userId){
		 Result<ProviderManage> result = new Result<>();
		 QueryWrapper<ProviderManage> providerManageQueryWrapper = new QueryWrapper<>();
		 providerManageQueryWrapper.eq("sys_user_id",userId);
		 ProviderManage one = providerManageService.getOne(providerManageQueryWrapper);
		 result.setSuccess(true);
		 result.setResult(one);
		 return result;
	 }
    @AutoLog(value = "启动停用")
    @ApiOperation(value = "启动停用", notes = "启动停用")
    @RequestMapping(value = "updateStatusById",method = RequestMethod.POST)
    public Result<ProviderManage> updateStatusById(@RequestBody ProviderManage providerManage){
        Result<ProviderManage> result = new Result<>();
        boolean b = providerManageService.updateById(providerManage);
        if (b){
            result.setMessage("修改成功");
            result.setSuccess(true);
        }else {
            result.error500("修改失败");
        }
        return result;
    }

    @AutoLog(value = "供应商余额列表")
    @ApiOperation(value = "供应商余额列表", notes = "供应商余额列表")
    @GetMapping("findProviderBalance")
        public Result<IPage<ProviderManageDTO>>findProviderBalance(ProviderManageVO providerManageVO,
                                                               @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                               @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                               HttpServletRequest req){
        Result<IPage<ProviderManageDTO>> result = new Result<>();
        Page<ProviderManageDTO> page = new Page<>(pageNo, pageSize);
        IPage<ProviderManageDTO> providerBalance = providerManageService.findProviderBalance(page, providerManageVO);
        result.setCode(200);
        result.setResult(providerBalance);
        return result;
    }
}
