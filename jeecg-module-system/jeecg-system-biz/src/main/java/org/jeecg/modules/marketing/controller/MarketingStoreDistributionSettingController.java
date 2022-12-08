package org.jeecg.modules.marketing.controller;

import com.alibaba.fastjson.JSON;
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
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.marketing.entity.MarketingStoreDistributionSetting;
import org.jeecg.modules.marketing.service.IMarketingStoreDistributionSettingService;
import org.jeecg.modules.marketing.vo.MarketingStoreDistributionSettingVO;
import org.jeecg.modules.system.service.ISysUserRoleService;
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
 * @Description: 店铺分销设置
 * @Author: jeecg-boot
 * @Date: 2019-12-10
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "店铺分销设置")
@RestController
@RequestMapping("/marketingStoreDistributionSetting/marketingStoreDistributionSetting")
public class MarketingStoreDistributionSettingController {
    @Autowired
    private IMarketingStoreDistributionSettingService marketingStoreDistributionSettingService;

    /**
     * 分页列表查询
     *
     * @param marketingStoreDistributionSetting
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "店铺分销设置-分页列表查询")
    @ApiOperation(value = "店铺分销设置-分页列表查询", notes = "店铺分销设置-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<MarketingStoreDistributionSetting>> queryPageList(MarketingStoreDistributionSetting marketingStoreDistributionSetting,
                                                                          @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                                          HttpServletRequest req) {
        Result<IPage<MarketingStoreDistributionSetting>> result = new Result<IPage<MarketingStoreDistributionSetting>>();
        QueryWrapper<MarketingStoreDistributionSetting> queryWrapper = QueryGenerator.initQueryWrapper(marketingStoreDistributionSetting, req.getParameterMap());
        Page<MarketingStoreDistributionSetting> page = new Page<MarketingStoreDistributionSetting>(pageNo, pageSize);
        IPage<MarketingStoreDistributionSetting> pageList = marketingStoreDistributionSettingService.page(page, queryWrapper);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 添加
     *
     * @param marketingStoreDistributionSetting
     * @return
     */
    @AutoLog(value = "店铺分销设置-添加")
    @ApiOperation(value = "店铺分销设置-添加", notes = "店铺分销设置-添加")
    @PostMapping(value = "/add")
    public Result<MarketingStoreDistributionSetting> add(@RequestBody MarketingStoreDistributionSetting marketingStoreDistributionSetting) {
        Result<MarketingStoreDistributionSetting> result = new Result<MarketingStoreDistributionSetting>();
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        marketingStoreDistributionSetting.setSysUserId(sysUser.getId());
        try {
            marketingStoreDistributionSettingService.save(marketingStoreDistributionSetting);
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
     * @param marketingStoreDistributionSetting
     * @return
     */
    @AutoLog(value = "店铺分销设置-编辑")
    @ApiOperation(value = "店铺分销设置-编辑", notes = "店铺分销设置-编辑")
    @PutMapping(value = "/edit")
    public Result<MarketingStoreDistributionSetting> edit(@RequestBody MarketingStoreDistributionSetting marketingStoreDistributionSetting) {
        Result<MarketingStoreDistributionSetting> result = new Result<MarketingStoreDistributionSetting>();
        boolean ok = marketingStoreDistributionSettingService.saveOrUpdate(marketingStoreDistributionSetting);
            //TODO 返回false说明什么？
            if (ok) {
                result.success("修改成功!");
            }
        return result;
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "店铺分销设置-通过id删除")
    @ApiOperation(value = "店铺分销设置-通过id删除", notes = "店铺分销设置-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        try {
            marketingStoreDistributionSettingService.removeById(id);
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
    @AutoLog(value = "店铺分销设置-批量删除")
    @ApiOperation(value = "店铺分销设置-批量删除", notes = "店铺分销设置-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<MarketingStoreDistributionSetting> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        Result<MarketingStoreDistributionSetting> result = new Result<MarketingStoreDistributionSetting>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别！");
        } else {
            this.marketingStoreDistributionSettingService.removeByIds(Arrays.asList(ids.split(",")));
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
    @AutoLog(value = "店铺分销设置-通过id查询")
    @ApiOperation(value = "店铺分销设置-通过id查询", notes = "店铺分销设置-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<MarketingStoreDistributionSetting> queryById(@RequestParam(name = "id", required = true) String id) {
        Result<MarketingStoreDistributionSetting> result = new Result<MarketingStoreDistributionSetting>();
        MarketingStoreDistributionSetting marketingStoreDistributionSetting = marketingStoreDistributionSettingService.getById(id);
        if (marketingStoreDistributionSetting == null) {
            result.error500("未找到对应实体");
        } else {
            result.setResult(marketingStoreDistributionSetting);
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
        QueryWrapper<MarketingStoreDistributionSetting> queryWrapper = null;
        try {
            String paramsStr = request.getParameter("paramsStr");
            if (oConvertUtils.isNotEmpty(paramsStr)) {
                String deString = URLDecoder.decode(paramsStr, "UTF-8");
                MarketingStoreDistributionSetting marketingStoreDistributionSetting = JSON.parseObject(deString, MarketingStoreDistributionSetting.class);
                queryWrapper = QueryGenerator.initQueryWrapper(marketingStoreDistributionSetting, request.getParameterMap());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //Step.2 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        List<MarketingStoreDistributionSetting> pageList = marketingStoreDistributionSettingService.list(queryWrapper);
        //导出文件名称
        mv.addObject(NormalExcelConstants.FILE_NAME, "店铺分销设置列表");
        mv.addObject(NormalExcelConstants.CLASS, MarketingStoreDistributionSetting.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("店铺分销设置列表数据", "导出人:Jeecg", "导出信息"));
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
                List<MarketingStoreDistributionSetting> listMarketingStoreDistributionSettings = ExcelImportUtil.importExcel(file.getInputStream(), MarketingStoreDistributionSetting.class, params);
                marketingStoreDistributionSettingService.saveBatch(listMarketingStoreDistributionSettings);
                return Result.ok("文件导入成功！数据行数:" + listMarketingStoreDistributionSettings.size());
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

    @AutoLog(value = "店铺分销设置-返显")
    @ApiOperation(value = "店铺分销设置-返显", notes = "店铺分销设置-返显")
    @RequestMapping(value = "findStoreDistributionSetting", method = RequestMethod.GET)
    public Result<MarketingStoreDistributionSetting> findStoreDistributionSetting() {
        Result<MarketingStoreDistributionSetting> result = new Result<>();
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String userId = sysUser.getId();
        QueryWrapper<MarketingStoreDistributionSetting> marketingStoreDistributionSettingQueryWrapper = new QueryWrapper<>();
        marketingStoreDistributionSettingQueryWrapper.eq("sys_user_id", userId);
        marketingStoreDistributionSettingQueryWrapper.eq("del_flag","0");
        MarketingStoreDistributionSetting one = marketingStoreDistributionSettingService.getOne(marketingStoreDistributionSettingQueryWrapper);
        result.setSuccess(true);
        result.setResult(one);
        return result;
    }
    @AutoLog(value = "店铺分销设置-店铺分销列表")
    @ApiOperation(value = "店铺分销设置-店铺分销列表", notes = "店铺分销设置-店铺分销列表")
    @RequestMapping(value = "findGiftBagStore",method = RequestMethod.GET)
    public Result<IPage<MarketingStoreDistributionSettingVO>> findGiftBagStore(MarketingStoreDistributionSettingVO marketingStoreDistributionSettingVO,
                                                                               @RequestParam(value = "pageNo",defaultValue = "1")Integer pageNo,
                                                                               @RequestParam(value = "pageSize",defaultValue = "10")Integer pageSize){
        Result<IPage<MarketingStoreDistributionSettingVO>> result = new Result<>();
        Page<MarketingStoreDistributionSettingVO> page = new Page<>(pageNo, pageSize);
        IPage<MarketingStoreDistributionSettingVO> giftBagStore = marketingStoreDistributionSettingService.findGiftBagStore(page, marketingStoreDistributionSettingVO);
        result.setSuccess(true);
        result.setResult(giftBagStore);
        return result;
    }
    @AutoLog(value = "店铺分销设置-店铺分销列表")
    @ApiOperation(value = "店铺分销设置-店铺分销列表", notes = "店铺分销设置-店铺分销列表")
    @RequestMapping(value = "findStoreDistribution",method = RequestMethod.GET)
    public Result<IPage<MarketingStoreDistributionSettingVO>> findStoreDistribution(MarketingStoreDistributionSettingVO marketingStoreDistributionSettingVO,
                                                                               @RequestParam(value = "pageNo",defaultValue = "1")Integer pageNo,
                                                                               @RequestParam(value = "pageSize",defaultValue = "10")Integer pageSize){
        Result<IPage<MarketingStoreDistributionSettingVO>> result = new Result<>();
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String uId = sysUser.getId();
        ISysUserRoleService iSysUserRoleService = SpringContextUtils.getBean(ISysUserRoleService.class);
        List<String> roleByUserId = iSysUserRoleService.getRoleByUserId(uId);
        if (roleByUserId.contains("Merchant")){
            marketingStoreDistributionSettingVO.setUid(uId);
        }
        Page<MarketingStoreDistributionSettingVO> page = new Page<>(pageNo, pageSize);
        IPage<MarketingStoreDistributionSettingVO> giftBagStore = marketingStoreDistributionSettingService.findStoreDistribution(page, marketingStoreDistributionSettingVO);
        result.setSuccess(true);
        result.setResult(giftBagStore);
        return result;
    }
    @AutoLog(value = "店铺分销设置")
    @ApiOperation(value = "店铺分销设置", notes = "店铺分销设置")
    @RequestMapping(value = "savaStoreDistributionSetting",method = RequestMethod.PUT)
    public Result<MarketingStoreDistributionSetting>savaStoreDistributionSetting(@RequestBody MarketingStoreDistributionSetting marketingStoreDistributionSetting){
        Result<MarketingStoreDistributionSetting> result = new Result<>();
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String userId = sysUser.getId();
        System.out.println(marketingStoreDistributionSetting);
        if (StringUtils.isBlank(marketingStoreDistributionSetting.getSysUserId())){
            marketingStoreDistributionSetting.setSysUserId(userId);
            marketingStoreDistributionSettingService.save(marketingStoreDistributionSetting);
            result.setMessage("修改成功");
        }else {
            marketingStoreDistributionSettingService.updateById(marketingStoreDistributionSetting);
            result.setMessage("修改成功");
        }
        return result;
    }
}
