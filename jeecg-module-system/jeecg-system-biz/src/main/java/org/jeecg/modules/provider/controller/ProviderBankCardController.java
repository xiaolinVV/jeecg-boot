package org.jeecg.modules.provider.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
import org.jeecg.modules.provider.dto.ProviderBankCardDTO;
import org.jeecg.modules.provider.entity.ProviderBankCard;
import org.jeecg.modules.provider.service.IProviderBankCardService;
import org.jeecg.modules.provider.vo.ProviderBankCardVO;
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
 * @Description: 提现信息
 * @Author: jeecg-boot
 * @Date: 2020-01-04
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "提现信息")
@RestController
@RequestMapping("/providerBankCard/providerBankCard")
public class ProviderBankCardController {
    @Autowired
    private IProviderBankCardService providerBankCardService;

    /**
     * 分页列表查询
     *
     * @param
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "提现信息-分页列表查询")
    @ApiOperation(value = "提现信息-分页列表查询", notes = "提现信息-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<ProviderBankCardVO>> queryPageList(ProviderBankCardDTO providerBankCardDTO,
                                                         @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                         @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                         HttpServletRequest req) {
        Result<IPage<ProviderBankCardVO>> result = new Result<IPage<ProviderBankCardVO>>();

        Page<ProviderBankCardVO> page = new Page<ProviderBankCardVO>(pageNo, pageSize);
        IPage<ProviderBankCardVO> pageList = providerBankCardService.queryPageList(page, providerBankCardDTO);
        pageList.getRecords().forEach(pl->{
            if (StringUtils.isNotBlank(pl.getUpdateCertificate())){
                pl.setUpdateCertificateOne((String) JSON.parseObject(pl.getUpdateCertificate()).get("0"));
            }
        });
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 添加
     *
     * @param providerBankCard
     * @return
     */
    @AutoLog(value = "提现信息-添加")
    @ApiOperation(value = "提现信息-添加", notes = "提现信息-添加")
    @PostMapping(value = "/add")
    public Result<ProviderBankCard> add(@RequestBody ProviderBankCard providerBankCard) {
        return providerBankCardService.add(providerBankCard);
    }

    /**
     * 编辑
     *
     * @param providerBankCard
     * @return
     */
    @AutoLog(value = "提现信息-编辑")
    @ApiOperation(value = "提现信息-编辑", notes = "提现信息-编辑")
    @PostMapping(value = "/edit")
    public Result<ProviderBankCard> edit(@RequestBody ProviderBankCard providerBankCard) {
        return providerBankCardService.edit(providerBankCard);
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "提现信息-通过id删除")
    @ApiOperation(value = "提现信息-通过id删除", notes = "提现信息-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        try {
            providerBankCardService.removeById(id);
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
    @AutoLog(value = "提现信息-批量删除")
    @ApiOperation(value = "提现信息-批量删除", notes = "提现信息-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<ProviderBankCard> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        Result<ProviderBankCard> result = new Result<ProviderBankCard>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别！");
        } else {
            this.providerBankCardService.removeByIds(Arrays.asList(ids.split(",")));
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
    @AutoLog(value = "提现信息-通过id查询")
    @ApiOperation(value = "提现信息-通过id查询", notes = "提现信息-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<ProviderBankCard> queryById(@RequestParam(name = "id", required = true) String id) {
        Result<ProviderBankCard> result = new Result<ProviderBankCard>();
        ProviderBankCard providerBankCard = providerBankCardService.getById(id);
        if (providerBankCard == null) {
            result.error500("未找到对应实体");
        } else {
            result.setResult(providerBankCard);
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
        QueryWrapper<ProviderBankCard> queryWrapper = null;
        try {
            String paramsStr = request.getParameter("paramsStr");
            if (oConvertUtils.isNotEmpty(paramsStr)) {
                String deString = URLDecoder.decode(paramsStr, "UTF-8");
                ProviderBankCard providerBankCard = JSON.parseObject(deString, ProviderBankCard.class);
                queryWrapper = QueryGenerator.initQueryWrapper(providerBankCard, request.getParameterMap());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //Step.2 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        List<ProviderBankCard> pageList = providerBankCardService.list(queryWrapper);
        //导出文件名称
        mv.addObject(NormalExcelConstants.FILE_NAME, "提现信息列表");
        mv.addObject(NormalExcelConstants.CLASS, ProviderBankCard.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("提现信息列表数据", "导出人:Jeecg", "导出信息"));
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
                List<ProviderBankCard> listProviderBankCards = ExcelImportUtil.importExcel(file.getInputStream(), ProviderBankCard.class, params);
                providerBankCardService.saveBatch(listProviderBankCards);
                return Result.ok("文件导入成功！数据行数:" + listProviderBankCards.size());
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

    @AutoLog(value = "提现信息-返显")
    @ApiOperation(value = "提现信息-返显", notes = "提现信息-返显")
    @GetMapping("returnBankCard")
    public Map<String,Object> returnBankCard() {
        return providerBankCardService.returnBankCard();
    }

    @AutoLog(value = "提现信息-设置银行卡or支付宝")
    @ApiOperation(value = "提现信息-设置银行卡or支付宝", notes = "提现信息-设置银行卡or支付宝")
    @PostMapping("updateOrSaveBankCard")
    public Result<ProviderBankCard> updateOrSaveBankCard(@RequestBody ProviderBankCardVO providerBankCardVO){
        return providerBankCardService.updateOrSaveBankCard(providerBankCardVO);
    }
}
