package org.jeecg.modules.agency.controller;

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
import org.jeecg.modules.agency.dto.AgencyBankCardDTO;
import org.jeecg.modules.agency.entity.AgencyBankCard;
import org.jeecg.modules.agency.service.IAgencyBankCardService;
import org.jeecg.modules.agency.vo.AgencyBankCardVO;
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
 * @Description: 代理银行卡
 * @Author: jeecg-boot
 * @Date: 2020-03-05
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "代理银行卡")
@RestController
@RequestMapping("/agencyBankCard/agencyBankCard")
public class AgencyBankCardController {
    @Autowired
    private IAgencyBankCardService agencyBankCardService;

    /**
     * 分页列表查询
     *
     * @param
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "代理银行卡-分页列表查询")
    @ApiOperation(value = "代理银行卡-分页列表查询", notes = "代理银行卡-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<AgencyBankCardVO>> queryPageList(AgencyBankCardDTO agencyBankCardDTO,
                                                       @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                       @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                       HttpServletRequest req) {
        Result<IPage<AgencyBankCardVO>> result = new Result<IPage<AgencyBankCardVO>>();
        Page<AgencyBankCardVO> page = new Page<AgencyBankCardVO>(pageNo, pageSize);
        IPage<AgencyBankCardVO> pageList = agencyBankCardService.queryPageList(page, agencyBankCardDTO);
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
     * @param agencyBankCard
     * @return
     */
    @AutoLog(value = "代理银行卡-添加")
    @ApiOperation(value = "代理银行卡-添加", notes = "代理银行卡-添加")
    @PostMapping(value = "/add")
    public Result<AgencyBankCard> add(@RequestBody AgencyBankCard agencyBankCard) {
        return agencyBankCardService.add(agencyBankCard);
    }

    /**
     * 编辑
     *
     * @param agencyBankCard
     * @return
     */
    @AutoLog(value = "代理银行卡-编辑")
    @ApiOperation(value = "代理银行卡-编辑", notes = "代理银行卡-编辑")
    @PostMapping(value = "/edit")
    public Result<AgencyBankCard> edit(@RequestBody AgencyBankCard agencyBankCard) {
        return agencyBankCardService.edit(agencyBankCard);
    }

    /**
     * 设置
     *
     * @param agencyBankCardVO
     * @return
     */
    @AutoLog(value = "代理银行卡-设置")
    @ApiOperation(value = "代理银行卡-设置", notes = "代理银行卡-设置")
    @PostMapping(value = "/agencyBankCardAudit")
    public Result<AgencyBankCard> agencyBankCardAudit(@RequestBody AgencyBankCardVO agencyBankCardVO) {
        return agencyBankCardService.agencyBankCardAudit(agencyBankCardVO);
    }

    @AutoLog(value = "代理银行卡-返显")
    @ApiOperation(value = "代理银行卡-返显", notes = "代理银行卡-返显")
    @GetMapping(value = "/findAgencyBankCard")
    public Map<String, Object> findAgencyBankCard() {
        return agencyBankCardService.findAgencyBankCard();
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "代理银行卡-通过id删除")
    @ApiOperation(value = "代理银行卡-通过id删除", notes = "代理银行卡-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        try {
            agencyBankCardService.removeById(id);
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
    @AutoLog(value = "代理银行卡-批量删除")
    @ApiOperation(value = "代理银行卡-批量删除", notes = "代理银行卡-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<AgencyBankCard> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        Result<AgencyBankCard> result = new Result<AgencyBankCard>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别！");
        } else {
            this.agencyBankCardService.removeByIds(Arrays.asList(ids.split(",")));
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
    @AutoLog(value = "代理银行卡-通过id查询")
    @ApiOperation(value = "代理银行卡-通过id查询", notes = "代理银行卡-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<AgencyBankCard> queryById(@RequestParam(name = "id", required = true) String id) {
        Result<AgencyBankCard> result = new Result<AgencyBankCard>();
        AgencyBankCard agencyBankCard = agencyBankCardService.getById(id);
        if (agencyBankCard == null) {
            result.error500("未找到对应实体");
        } else {
            result.setResult(agencyBankCard);
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
        QueryWrapper<AgencyBankCard> queryWrapper = null;
        try {
            String paramsStr = request.getParameter("paramsStr");
            if (oConvertUtils.isNotEmpty(paramsStr)) {
                String deString = URLDecoder.decode(paramsStr, "UTF-8");
                AgencyBankCard agencyBankCard = JSON.parseObject(deString, AgencyBankCard.class);
                queryWrapper = QueryGenerator.initQueryWrapper(agencyBankCard, request.getParameterMap());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //Step.2 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        List<AgencyBankCard> pageList = agencyBankCardService.list(queryWrapper);
        //导出文件名称
        mv.addObject(NormalExcelConstants.FILE_NAME, "代理银行卡列表");
        mv.addObject(NormalExcelConstants.CLASS, AgencyBankCard.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("代理银行卡列表数据", "导出人:Jeecg", "导出信息"));
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
                List<AgencyBankCard> listAgencyBankCards = ExcelImportUtil.importExcel(file.getInputStream(), AgencyBankCard.class, params);
                agencyBankCardService.saveBatch(listAgencyBankCards);
                return Result.ok("文件导入成功！数据行数:" + listAgencyBankCards.size());
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

    @AutoLog(value = "代理银行卡(提现调用下拉)")
    @ApiOperation(value = "代理银行卡(提现调用下拉)", notes = "代理银行卡(提现调用下拉)")
    @GetMapping(value = "/findBankCardById")
    public List<AgencyBankCardDTO> findBankCardById(@RequestParam(value = "sysUserId", required = true) String sysUserId) {
        return agencyBankCardService.findBankCardById(sysUserId);
    }
}
