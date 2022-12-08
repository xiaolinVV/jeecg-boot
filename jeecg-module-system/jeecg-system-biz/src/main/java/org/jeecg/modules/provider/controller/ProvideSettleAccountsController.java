package org.jeecg.modules.provider.controller;

import com.alibaba.fastjson.JSON;
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
import org.jeecg.modules.provider.dto.ProvideSettleAccountsDTO;
import org.jeecg.modules.provider.entity.ProvideSettleAccounts;
import org.jeecg.modules.provider.entity.ProviderAccountCapital;
import org.jeecg.modules.provider.entity.ProviderManage;
import org.jeecg.modules.provider.service.IProvideSettleAccountsService;
import org.jeecg.modules.provider.service.IProviderAccountCapitalService;
import org.jeecg.modules.provider.service.IProviderManageService;
import org.jeecg.modules.provider.vo.ProvideSettleAccountsVO;
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
 * @Description: 供应商待付款
 * @Author: jeecg-boot
 * @Date: 2019-12-23
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "供应商待付款")
@RestController
@RequestMapping("/provideSettleAccounts/provideSettleAccounts")
public class ProvideSettleAccountsController {
    @Autowired
    private IProvideSettleAccountsService provideSettleAccountsService;
    @Autowired
    private IProviderManageService providerManageService;
    @Autowired
    private IProviderAccountCapitalService providerAccountCapitalService;
    @Autowired
    private ISysUserRoleService iSysUserRoleService;

    /**
     * 分页列表查询
     *
     * @param provideSettleAccountsVO
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "供应商待付款-分页列表查询")
    @ApiOperation(value = "供应商待付款-分页列表查询", notes = "供应商待付款-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<ProvideSettleAccountsDTO>> queryPageList(ProvideSettleAccountsVO provideSettleAccountsVO,
                                                                 @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                                 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                                 HttpServletRequest req) {
        Result<IPage<ProvideSettleAccountsDTO>> result = new Result<IPage<ProvideSettleAccountsDTO>>();
        Page<ProvideSettleAccounts> page = new Page<ProvideSettleAccounts>(pageNo, pageSize);
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        List<String> roleByUserId = iSysUserRoleService.getRoleByUserId(sysUser.getId());
        if (roleByUserId.contains("Supplier")){
            provideSettleAccountsVO.setSysUserId(sysUser.getId());
        }
        IPage<ProvideSettleAccountsDTO> pageList = provideSettleAccountsService.getProvideSettleAccountsList(page, provideSettleAccountsVO);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 添加
     *
     * @param provideSettleAccounts
     * @return
     */
    @AutoLog(value = "供应商待付款-添加")
    @ApiOperation(value = "供应商待付款-添加", notes = "供应商待付款-添加")
    @PostMapping(value = "/add")
    public Result<ProvideSettleAccounts> add(@RequestBody ProvideSettleAccounts provideSettleAccounts) {
        Result<ProvideSettleAccounts> result = new Result<ProvideSettleAccounts>();
        try {
            provideSettleAccountsService.save(provideSettleAccounts);
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
     * @param provideSettleAccounts
     * @return
     */
    @AutoLog(value = "供应商待付款-编辑")
    @ApiOperation(value = "供应商待付款-编辑", notes = "供应商待付款-编辑")
    @PutMapping(value = "/edit")
    public Result<ProvideSettleAccounts> edit(@RequestBody ProvideSettleAccounts provideSettleAccounts) {
        Result<ProvideSettleAccounts> result = new Result<ProvideSettleAccounts>();
        ProvideSettleAccounts provideSettleAccountsEntity = provideSettleAccountsService.getById(provideSettleAccounts.getId());
        if (provideSettleAccountsEntity == null) {
            result.error500("未找到对应实体");
        } else {
            boolean ok = provideSettleAccountsService.updateById(provideSettleAccounts);
            //TODO 返回false说明什么？
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
    @AutoLog(value = "供应商待付款-通过id删除")
    @ApiOperation(value = "供应商待付款-通过id删除", notes = "供应商待付款-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        try {
            provideSettleAccountsService.removeById(id);
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
    @AutoLog(value = "供应商待付款-批量删除")
    @ApiOperation(value = "供应商待付款-批量删除", notes = "供应商待付款-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<ProvideSettleAccounts> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        Result<ProvideSettleAccounts> result = new Result<ProvideSettleAccounts>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别！");
        } else {
            this.provideSettleAccountsService.removeByIds(Arrays.asList(ids.split(",")));
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
    @AutoLog(value = "供应商待付款-通过id查询")
    @ApiOperation(value = "供应商待付款-通过id查询", notes = "供应商待付款-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<ProvideSettleAccounts> queryById(@RequestParam(name = "id", required = true) String id) {
        Result<ProvideSettleAccounts> result = new Result<ProvideSettleAccounts>();
        ProvideSettleAccounts provideSettleAccounts = provideSettleAccountsService.getById(id);
        if (provideSettleAccounts == null) {
            result.error500("未找到对应实体");
        } else {
            result.setResult(provideSettleAccounts);
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
        QueryWrapper<ProvideSettleAccounts> queryWrapper = null;
        try {
            String paramsStr = request.getParameter("paramsStr");
            if (oConvertUtils.isNotEmpty(paramsStr)) {
                String deString = URLDecoder.decode(paramsStr, "UTF-8");
                ProvideSettleAccounts provideSettleAccounts = JSON.parseObject(deString, ProvideSettleAccounts.class);
                queryWrapper = QueryGenerator.initQueryWrapper(provideSettleAccounts, request.getParameterMap());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //Step.2 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        List<ProvideSettleAccounts> pageList = provideSettleAccountsService.list(queryWrapper);
        //导出文件名称
        mv.addObject(NormalExcelConstants.FILE_NAME, "供应商待付款列表");
        mv.addObject(NormalExcelConstants.CLASS, ProvideSettleAccounts.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("供应商待付款列表数据", "导出人:Jeecg", "导出信息"));
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
                List<ProvideSettleAccounts> listProvideSettleAccountss = ExcelImportUtil.importExcel(file.getInputStream(), ProvideSettleAccounts.class, params);
                provideSettleAccountsService.saveBatch(listProvideSettleAccountss);
                return Result.ok("文件导入成功！数据行数:" + listProvideSettleAccountss.size());
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
     * 通过id查询修改审核状态  弃用
     *
     * @param id
     * @return
     */
    @AutoLog(value = "会员提现审批-通过id查询")
    @ApiOperation(value = "会员提现审批-通过id查询", notes = "会员提现审批-通过id查询")
    @GetMapping(value = "/updateAuditStatus")
    public Result<ProvideSettleAccounts> updateAuditStatus(@RequestParam(name = "id", required = true) String id,
                                                           @RequestParam(name = "status") String status,
                                                           @RequestParam(name = "closeExplain") String closeExplain) {
        Result<ProvideSettleAccounts> result = new Result<ProvideSettleAccounts>();
        ProvideSettleAccounts provideSettleAccounts = provideSettleAccountsService.getById(id);
        if (provideSettleAccounts == null) {
            result.error500("未找到对应实体");
        } else {
            provideSettleAccounts.setStatus(status);
            provideSettleAccounts.setCloseExplain(closeExplain);
            boolean ok = provideSettleAccountsService.updateById(provideSettleAccounts);
            //TODO 返回false说明什么？
            if (ok) {
                result.success("修改成功!");
            } else {
                result.error500("修改失败！");
            }
        }
        return result;
    }

    /* *
     * 通过id查询修改状态 打款： 弃用
     *
     * @param id
     * @return
     */
    @AutoLog(value = "会员打款-通过id查询")
    @ApiOperation(value = "会员打款-通过id查询", notes = "会员打款-通过id查询")
    @GetMapping(value = "/updateStatusRemit")
    public Result<ProvideSettleAccounts> updateStatusRemit(@RequestParam(name = "id", required = true) String id,
                                                           @RequestParam(name = "status") String status,
                                                           @RequestParam(name = "remark") String remark) {
        Result<ProvideSettleAccounts> result = new Result<ProvideSettleAccounts>();
        ProvideSettleAccounts provideSettleAccounts = provideSettleAccountsService.getById(id);
        if (provideSettleAccounts == null) {
            result.error500("未找到对应实体");
        } else {
            //已付款
            if (status.equals("2")) {
                //获取代理商信息
                QueryWrapper<ProviderManage> queryWrapperProviderManage = new QueryWrapper();
                queryWrapperProviderManage.eq("sys_user_id", provideSettleAccounts.getSysUserId());
                ProviderManage providerManage = providerManageService.getOne(queryWrapperProviderManage);
                if (providerManage == null) {
                    result.error500("用户不存在");
                } else {
                    //比较大小：
                    // int a = bigdemical.compareTo(bigdemical2)
                    //a = -1,表示bigdemical小于bigdemical2
                    int a = providerManage.getBalance().compareTo(provideSettleAccounts.getMoney());
                    if (a == -1) {
                        result.error500("提现金额大于，余额!");
                    } else {
                        //修改打款状态
                        provideSettleAccounts.setStatus(status);
                        provideSettleAccounts.setRemark(remark);
                        boolean ok = provideSettleAccountsService.updateById(provideSettleAccounts);
                        //会员余额减去提现值     减法  bignum3 = bignum1.subtract(bignum2);
                        providerManage.setBalance(providerManage.getBalance().subtract(provideSettleAccounts.getMoney()));
                        providerManageService.updateById(providerManage);
                        //生成资金明细数据
                        ProviderAccountCapital providerAccountCapital = new ProviderAccountCapital();
                        providerAccountCapital.setDelFlag("0");
                        providerAccountCapital.setSysUserId(provideSettleAccounts.getSysUserId());
                        providerAccountCapital.setPayType("1");
                        providerAccountCapital.setGoAndCome("1");
                        providerAccountCapital.setAmount(provideSettleAccounts.getMoney());
                        providerAccountCapital.setOrderNo(provideSettleAccounts.getOrderNo());
                        providerAccountCapital.setBalance(providerManage.getBalance());
                        providerAccountCapitalService.save(providerAccountCapital);
                        //TODO 返回false说明什么？
                        if (ok) {
                            result.success("提现成功!");
                        }

                    }
                }
            }

        }
        return result;
    }

    @AutoLog(value = "审批")
    @ApiOperation(value = "审批", notes = "审批")
    @PostMapping("/audit")
    public Result<ProvideSettleAccounts> audit(@RequestBody ProvideSettleAccountsVO provideSettleAccountsVO) {
        return provideSettleAccountsService.audit(provideSettleAccountsVO);
    }

    @AutoLog(value = "汇款")
    @ApiOperation(value = "汇款", notes = "汇款")
    @PostMapping("/remit")
    public Result<ProvideSettleAccounts> remit(@RequestBody ProvideSettleAccountsVO provideSettleAccountsVO) {
        return provideSettleAccountsService.remit(provideSettleAccountsVO);
    }

}
