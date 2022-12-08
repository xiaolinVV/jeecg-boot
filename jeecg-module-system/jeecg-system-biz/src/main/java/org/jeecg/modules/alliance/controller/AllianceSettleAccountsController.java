package org.jeecg.modules.alliance.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
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
import org.jeecg.config.util.PermissionUtils;
import org.jeecg.modules.alliance.dto.AllianceSettleAccountsDTO;
import org.jeecg.modules.alliance.entity.AllianceBankCard;
import org.jeecg.modules.alliance.entity.AllianceManage;
import org.jeecg.modules.alliance.entity.AllianceSettleAccounts;
import org.jeecg.modules.alliance.service.IAllianceBankCardService;
import org.jeecg.modules.alliance.service.IAllianceManageService;
import org.jeecg.modules.alliance.service.IAllianceSettleAccountsService;
import org.jeecg.modules.alliance.vo.AllianceSettleAccountsVO;
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
import java.util.List;
import java.util.Map;

/**
 * @Description: 加盟商提现
 * @Author: jeecg-boot
 * @Date: 2020-05-18
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "加盟商提现")
@RestController
@RequestMapping("/allianceSettleAccounts/allianceSettleAccounts")
public class AllianceSettleAccountsController {
    @Autowired
    private IAllianceSettleAccountsService allianceSettleAccountsService;
    @Autowired
    private IAllianceBankCardService iAllianceBankCardService;
    @Autowired
    private IAllianceManageService iAllianceManageService;
    /**
     * 分页列表查询
     *
     * @param
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "加盟商提现-分页列表查询")
    @ApiOperation(value = "加盟商提现-分页列表查询", notes = "加盟商提现-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<AllianceSettleAccountsVO>> queryPageList(AllianceSettleAccountsDTO allianceSettleAccountsDTO,
                                                                 @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                                 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                                 HttpServletRequest req) {
        Result<IPage<AllianceSettleAccountsVO>> result = new Result<IPage<AllianceSettleAccountsVO>>();
        Page<AllianceSettleAccounts> page = new Page<AllianceSettleAccounts>(pageNo, pageSize);
        String role = PermissionUtils.ifAllianceRole();
        if (StringUtils.isNotBlank(role)) {
            allianceSettleAccountsDTO.setSysUserId(role);
        }
        IPage<AllianceSettleAccountsVO> pageList = allianceSettleAccountsService.queryPageList(page, allianceSettleAccountsDTO);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 添加
     *
     * @param allianceSettleAccounts
     * @return
     */
    @AutoLog(value = "加盟商提现-添加")
    @ApiOperation(value = "加盟商提现-添加", notes = "加盟商提现-添加")
    @PostMapping(value = "/add")
    public Result<AllianceSettleAccounts> add(@RequestBody AllianceSettleAccounts allianceSettleAccounts) {
        Result<AllianceSettleAccounts> result = new Result<AllianceSettleAccounts>();
        try {
            allianceSettleAccountsService.save(allianceSettleAccounts);
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
     * @param allianceSettleAccounts
     * @return
     */
    @AutoLog(value = "加盟商提现-编辑")
    @ApiOperation(value = "加盟商提现-编辑", notes = "加盟商提现-编辑")
    @PutMapping(value = "/edit")
    public Result<AllianceSettleAccounts> edit(@RequestBody AllianceSettleAccounts allianceSettleAccounts) {
        Result<AllianceSettleAccounts> result = new Result<AllianceSettleAccounts>();
        AllianceSettleAccounts allianceSettleAccountsEntity = allianceSettleAccountsService.getById(allianceSettleAccounts.getId());
        if (allianceSettleAccountsEntity == null) {
            result.error500("未找到对应实体");
        } else {
            boolean ok = allianceSettleAccountsService.updateById(allianceSettleAccounts);
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
    @AutoLog(value = "加盟商提现-通过id删除")
    @ApiOperation(value = "加盟商提现-通过id删除", notes = "加盟商提现-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        try {
            allianceSettleAccountsService.removeById(id);
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
    @AutoLog(value = "加盟商提现-批量删除")
    @ApiOperation(value = "加盟商提现-批量删除", notes = "加盟商提现-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<AllianceSettleAccounts> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        Result<AllianceSettleAccounts> result = new Result<AllianceSettleAccounts>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别！");
        } else {
            this.allianceSettleAccountsService.removeByIds(Arrays.asList(ids.split(",")));
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
    @AutoLog(value = "加盟商提现-通过id查询")
    @ApiOperation(value = "加盟商提现-通过id查询", notes = "加盟商提现-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<AllianceSettleAccounts> queryById(@RequestParam(name = "id", required = true) String id) {
        Result<AllianceSettleAccounts> result = new Result<AllianceSettleAccounts>();
        AllianceSettleAccounts allianceSettleAccounts = allianceSettleAccountsService.getById(id);
        if (allianceSettleAccounts == null) {
            result.error500("未找到对应实体");
        } else {
            result.setResult(allianceSettleAccounts);
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
        QueryWrapper<AllianceSettleAccounts> queryWrapper = null;
        try {
            String paramsStr = request.getParameter("paramsStr");
            if (oConvertUtils.isNotEmpty(paramsStr)) {
                String deString = URLDecoder.decode(paramsStr, "UTF-8");
                AllianceSettleAccounts allianceSettleAccounts = JSON.parseObject(deString, AllianceSettleAccounts.class);
                queryWrapper = QueryGenerator.initQueryWrapper(allianceSettleAccounts, request.getParameterMap());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //Step.2 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        List<AllianceSettleAccounts> pageList = allianceSettleAccountsService.list(queryWrapper);
        //导出文件名称
        mv.addObject(NormalExcelConstants.FILE_NAME, "加盟商提现列表");
        mv.addObject(NormalExcelConstants.CLASS, AllianceSettleAccounts.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("加盟商提现列表数据", "导出人:Jeecg", "导出信息"));
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
                List<AllianceSettleAccounts> listAllianceSettleAccountss = ExcelImportUtil.importExcel(file.getInputStream(), AllianceSettleAccounts.class, params);
                allianceSettleAccountsService.saveBatch(listAllianceSettleAccountss);
                return Result.ok("文件导入成功！数据行数:" + listAllianceSettleAccountss.size());
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

    @AutoLog(value = "加盟商提现-通过id查询")
    @ApiOperation(value = "加盟商提现-通过id查询", notes = "加盟商提现-通过id查询")
    @PostMapping("cashOut")
    public Result<String> cashOut(@RequestBody JSONObject jsonObject) {
        Result<String> result = new Result<>();
        String sysUserId = jsonObject.getString("sysUserId");//加盟商userId
        BigDecimal amount = jsonObject.getBigDecimal("amount");//交易金额
        String allianceBankCardId = jsonObject.getString("allianceBankCardId");//银行卡id
        String remark = jsonObject.getString("remark");//备注
        if (amount.doubleValue()<0){
            return result.error500("提现金额不能小于0");
        }
        AllianceBankCard allianceBankCard = iAllianceBankCardService.getById(allianceBankCardId);
        if (oConvertUtils.isEmpty(allianceBankCard)){
            return result.error500("银行卡信息异常,请先去查看银行卡!");
        }
        AllianceManage allianceManage = iAllianceManageService.getOne(new LambdaUpdateWrapper<AllianceManage>().eq(AllianceManage::getSysUserId, sysUserId));
        if (oConvertUtils.isEmpty(allianceManage)){
            return result.error500("加盟商信息异常,请联系管理员");
        }
        if (allianceManage.getBalance().doubleValue()<amount.doubleValue()){
            return result.error500("提现金额大于余额");
        }
        allianceSettleAccountsService.cashOut(allianceBankCard,allianceManage,amount,remark);
        result.success("提现成功,待审核!");
        return result;
    }
    @AutoLog(value = "加盟商提现-审核")
    @ApiOperation(value = "加盟商提现-审核", notes = "加盟商提现-审核")
    @PostMapping("audit")
    public Result<String> audit(@RequestBody AllianceSettleAccounts allianceSettleAccounts){
        Result<String> result = new Result<>();
        if (StringUtils.isBlank(allianceSettleAccounts.getId())){
            return result.error500("信息错误,请联系管理员");
        }
        allianceSettleAccountsService.audit(allianceSettleAccounts);
        result.success("操作成功");
        return result;
    }
    @AutoLog(value = "加盟商提现-打款")
    @ApiOperation(value = "加盟商提现-打款", notes = "加盟商提现-打款")
    @PostMapping("remit")
    public Result<String> remit(@RequestBody AllianceSettleAccounts allianceSettleAccounts){
        Result<String> result = new Result<>();
        if (StringUtils.isBlank(allianceSettleAccounts.getId())){
            return result.error500("信息错误,请联系管理员");
        }
        allianceSettleAccountsService.remit(allianceSettleAccounts);
        result.success("操作成功");
        return result;
    }
}
