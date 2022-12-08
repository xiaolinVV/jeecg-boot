package org.jeecg.modules.marketing.controller;

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
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.marketing.dto.MarketingCommingStoreDTO;
import org.jeecg.modules.marketing.entity.MarketingCommingStore;
import org.jeecg.modules.marketing.service.IMarketingCommingStoreService;
import org.jeecg.modules.marketing.vo.MarketingCommingStoreVO;
import org.jeecg.modules.store.entity.StoreManage;
import org.jeecg.modules.store.service.IStoreManageService;
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
 * @Description: 进店活动
 * @Author: jeecg-boot
 * @Date: 2020-08-20
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "进店活动")
@RestController
@RequestMapping("/marketingCommingStore/marketingCommingStore")
public class MarketingCommingStoreController {
    @Autowired
    private IMarketingCommingStoreService marketingCommingStoreService;
    @Autowired
    private ISysUserRoleService iSysUserRoleService;
    @Autowired
    private IStoreManageService iStoreManageService;

    /**
     * 分页列表查询
     *
     * @param
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "进店活动-分页列表查询")
    @ApiOperation(value = "进店活动-分页列表查询", notes = "进店活动-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<MarketingCommingStoreVO>> queryPageList(MarketingCommingStoreDTO marketingCommingStoreDTO,
                                                                @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                                @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                                HttpServletRequest req) {
        Result<IPage<MarketingCommingStoreVO>> result = new Result<IPage<MarketingCommingStoreVO>>();
        Page<MarketingCommingStoreVO> page = new Page<MarketingCommingStoreVO>(pageNo, pageSize);
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        List<String> roleByUserId = iSysUserRoleService.getRoleByUserId(sysUser.getId());
        if (roleByUserId.contains("Merchant")) {
            marketingCommingStoreDTO.setSysUserId(sysUser.getId());
        }
        IPage<MarketingCommingStoreVO> pageList = marketingCommingStoreService.queryPageList(page, marketingCommingStoreDTO);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 添加
     *
     * @param marketingCommingStore
     * @return
     */
    @AutoLog(value = "进店活动-添加")
    @ApiOperation(value = "进店活动-添加", notes = "进店活动-添加")
    @PostMapping(value = "/add")
    public Result<MarketingCommingStore> add(@RequestBody MarketingCommingStore marketingCommingStore) {
        Result<MarketingCommingStore> result = new Result<MarketingCommingStore>();
        try {
            if (StringUtils.isBlank(marketingCommingStore.getStoreManageId())) {
                LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
                StoreManage storeManage = iStoreManageService.getOne(new LambdaQueryWrapper<StoreManage>()
                        .eq(StoreManage::getSysUserId, sysUser.getId())
                        .eq(StoreManage::getDelFlag, "0")
                );
                marketingCommingStore.setStoreManageId(storeManage.getId());
            }
			/*if (ifMarketingCommingStoreTakeWay(marketingCommingStore).equals("1")){
				return result.error500("该活动类型已存在");
			}*/
            if (marketingCommingStore.getValidity().equals("0")) {
                marketingCommingStore.setEndTime(null);
            }
            marketingCommingStoreService.save(marketingCommingStore);
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
     * @param marketingCommingStore
     * @return
     */
    @AutoLog(value = "进店活动-编辑")
    @ApiOperation(value = "进店活动-编辑", notes = "进店活动-编辑")
    @PostMapping(value = "/edit")
    public Result<MarketingCommingStore> edit(@RequestBody MarketingCommingStore marketingCommingStore) {
        Result<MarketingCommingStore> result = new Result<MarketingCommingStore>();
        MarketingCommingStore marketingCommingStoreEntity = marketingCommingStoreService.getById(marketingCommingStore.getId());
        if (marketingCommingStoreEntity == null) {
            result.error500("未找到对应实体");
        } else {
			/*if (!marketingCommingStoreEntity.getTakeWay().equals(marketingCommingStore.getTakeWay())){
				if (ifMarketingCommingStoreTakeWay(marketingCommingStore).equals("1")){
					return result.error500("该活动类型已存在");
				}
			}*/
            if (StringUtils.isNotBlank(marketingCommingStore.getValidity())&&marketingCommingStore.getValidity().equals("0")) {
                marketingCommingStore.setEndTime(null);
            }

            boolean ok = marketingCommingStoreService.updateById(marketingCommingStore);
            if (ok) {
                result.success("修改成功!");
            } else {
                result.error500("修改失败!");
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
    @AutoLog(value = "进店活动-通过id删除")
    @ApiOperation(value = "进店活动-通过id删除", notes = "进店活动-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        try {
            marketingCommingStoreService.removeById(id);
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
    @AutoLog(value = "进店活动-批量删除")
    @ApiOperation(value = "进店活动-批量删除", notes = "进店活动-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<MarketingCommingStore> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        Result<MarketingCommingStore> result = new Result<MarketingCommingStore>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别！");
        } else {
            this.marketingCommingStoreService.removeByIds(Arrays.asList(ids.split(",")));
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
    @AutoLog(value = "进店活动-通过id查询")
    @ApiOperation(value = "进店活动-通过id查询", notes = "进店活动-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<MarketingCommingStore> queryById(@RequestParam(name = "id", required = true) String id) {
        Result<MarketingCommingStore> result = new Result<MarketingCommingStore>();
        MarketingCommingStore marketingCommingStore = marketingCommingStoreService.getById(id);
        if (marketingCommingStore == null) {
            result.error500("未找到对应实体");
        } else {
            result.setResult(marketingCommingStore);
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
        QueryWrapper<MarketingCommingStore> queryWrapper = null;
        try {
            String paramsStr = request.getParameter("paramsStr");
            if (oConvertUtils.isNotEmpty(paramsStr)) {
                String deString = URLDecoder.decode(paramsStr, "UTF-8");
                MarketingCommingStore marketingCommingStore = JSON.parseObject(deString, MarketingCommingStore.class);
                queryWrapper = QueryGenerator.initQueryWrapper(marketingCommingStore, request.getParameterMap());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //Step.2 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        List<MarketingCommingStore> pageList = marketingCommingStoreService.list(queryWrapper);
        //导出文件名称
        mv.addObject(NormalExcelConstants.FILE_NAME, "进店活动列表");
        mv.addObject(NormalExcelConstants.CLASS, MarketingCommingStore.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("进店活动列表数据", "导出人:Jeecg", "导出信息"));
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
                List<MarketingCommingStore> listMarketingCommingStores = ExcelImportUtil.importExcel(file.getInputStream(), MarketingCommingStore.class, params);
                marketingCommingStoreService.saveBatch(listMarketingCommingStores);
                return Result.ok("文件导入成功！数据行数:" + listMarketingCommingStores.size());
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

    private String ifMarketingCommingStoreTakeWay(MarketingCommingStore marketingCommingStore) {
        if (marketingCommingStoreService.count(new LambdaQueryWrapper<MarketingCommingStore>()
                .eq(MarketingCommingStore::getDelFlag, "0")
                .eq(MarketingCommingStore::getStoreManageId, marketingCommingStore.getStoreManageId())
                .eq(MarketingCommingStore::getTakeWay, marketingCommingStore.getTakeWay())
                .eq(MarketingCommingStore::getValidity, "0")
                .le(MarketingCommingStore::getStartTime, DateUtils.now())
        ) > 0) {
            return "1";
        } else {
            if (marketingCommingStoreService.count(new LambdaQueryWrapper<MarketingCommingStore>()
                    .eq(MarketingCommingStore::getDelFlag, "0")
                    .eq(MarketingCommingStore::getStoreManageId, marketingCommingStore.getStoreManageId())
                    .eq(MarketingCommingStore::getTakeWay, marketingCommingStore.getTakeWay())
                    .eq(MarketingCommingStore::getValidity, "1")
                    .le(MarketingCommingStore::getStartTime, DateUtils.now())
                    .ge(MarketingCommingStore::getEndTime, DateUtils.now())
            ) > 0) {
                return "1";
            } else {
                return "0";
            }
        }
    }

    @GetMapping("ifTheSameCommingStore")
    public Result<String> ifTheSameCommingStore(MarketingCommingStore marketingCommingStore) {
        Result<String> result = new Result<>();

        if (StringUtils.isNotBlank(marketingCommingStore.getId())){
            result.setResult(ifTheSameCommingStoreAdd(marketingCommingStore));
        }else {
            result.setResult(ifTheSameCommingStoreEdit(marketingCommingStore));
        }

        return result;
    }

    private String ifTheSameCommingStoreAdd(MarketingCommingStore marketingCommingStore){
        if (StringUtils.isNotBlank(marketingCommingStore.getStoreManageId())) {
            StoreManage storeManage = iStoreManageService.getById(marketingCommingStore.getStoreManageId());
            if (marketingCommingStoreService.count(new LambdaQueryWrapper<MarketingCommingStore>()
                    .eq(MarketingCommingStore::getDelFlag,"0")
                    .eq(MarketingCommingStore::getStoreManageId,storeManage.getId())
                    .eq(MarketingCommingStore::getStatus,"1")
                    .eq(MarketingCommingStore::getTakeWay,marketingCommingStore.getTakeWay())

            )>0){
                return "1";
            }else {
                return "0";
            }
        } else {
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            StoreManage storeManage = iStoreManageService.getOne(new LambdaQueryWrapper<StoreManage>()
                    .eq(StoreManage::getSysUserId, sysUser.getId())
                    .eq(StoreManage::getDelFlag, "0")
            );
            if (marketingCommingStoreService.count(new LambdaQueryWrapper<MarketingCommingStore>()
                    .eq(MarketingCommingStore::getDelFlag,"0")
                    .eq(MarketingCommingStore::getStoreManageId,storeManage.getId())
                    .eq(MarketingCommingStore::getTakeWay,marketingCommingStore.getTakeWay())
                    .eq(MarketingCommingStore::getStatus,"1")
            )>0){
                return "1";
            }else {
                return "0";
            }
        }
    }
    private String ifTheSameCommingStoreEdit(MarketingCommingStore marketingCommingStore){
        if (StringUtils.isNotBlank(marketingCommingStore.getStoreManageId())) {
            StoreManage storeManage = iStoreManageService.getById(marketingCommingStore.getStoreManageId());
            if (marketingCommingStoreService.count(new LambdaQueryWrapper<MarketingCommingStore>()
                    .eq(MarketingCommingStore::getDelFlag,"0")
                    .eq(MarketingCommingStore::getStoreManageId,storeManage.getId())
                    .eq(MarketingCommingStore::getStatus,"1")
                    .eq(MarketingCommingStore::getTakeWay,marketingCommingStore.getTakeWay())

            )>1){
                return "1";
            }else {
                return "0";
            }
        } else {
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            StoreManage storeManage = iStoreManageService.getOne(new LambdaQueryWrapper<StoreManage>()
                    .eq(StoreManage::getSysUserId, sysUser.getId())
                    .eq(StoreManage::getDelFlag, "0")
            );
            if (marketingCommingStoreService.count(new LambdaQueryWrapper<MarketingCommingStore>()
                    .eq(MarketingCommingStore::getDelFlag,"0")
                    .eq(MarketingCommingStore::getStoreManageId,storeManage.getId())
                    .eq(MarketingCommingStore::getTakeWay,marketingCommingStore.getTakeWay())
                    .eq(MarketingCommingStore::getStatus,"1")
            )>1){
                return "1";
            }else {
                return "0";
            }
        }
    }
}
