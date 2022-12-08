package org.jeecg.modules.alliance.controller;

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
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.alliance.dto.AllianceBankCardDTO;
import org.jeecg.modules.alliance.entity.AllianceBankCard;
import org.jeecg.modules.alliance.service.IAllianceBankCardService;
import org.jeecg.modules.alliance.vo.AllianceBankCardVO;
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
import java.util.*;

/**
 * @Description: 加盟商银行卡
 * @Author: jeecg-boot
 * @Date: 2020-05-17
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "加盟商银行卡")
@RestController
@RequestMapping("/allianceBankCard/allianceBankCard")
public class AllianceBankCardController {
    @Autowired
    private IAllianceBankCardService allianceBankCardService;
    @Autowired
    private RedisUtil redisUtil;
    /**
     * 分页列表查询
     *
     * @param
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "加盟商银行卡-分页列表查询")
    @ApiOperation(value = "加盟商银行卡-分页列表查询", notes = "加盟商银行卡-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<AllianceBankCardVO>> queryPageList(AllianceBankCardDTO allianceBankCardDTO,
                                                           @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                           @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                           HttpServletRequest req) {
        Result<IPage<AllianceBankCardVO>> result = new Result<IPage<AllianceBankCardVO>>();

        Page<AllianceBankCardVO> page = new Page<AllianceBankCardVO>(pageNo, pageSize);
        IPage<AllianceBankCardVO> pageList = allianceBankCardService.queryPageList(page, allianceBankCardDTO);
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
     * @param allianceBankCard
     * @return
     */
    @AutoLog(value = "加盟商银行卡-添加")
    @ApiOperation(value = "加盟商银行卡-添加", notes = "加盟商银行卡-添加")
    @PostMapping(value = "/add")
    public Result<AllianceBankCard> add(@RequestBody AllianceBankCard allianceBankCard) {
        Result<AllianceBankCard> result = new Result<AllianceBankCard>();
        try {
            allianceBankCardService.save(allianceBankCard);
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
     * @param allianceBankCard
     * @return
     */
    @AutoLog(value = "加盟商银行卡-编辑")
    @ApiOperation(value = "加盟商银行卡-编辑", notes = "加盟商银行卡-编辑")
    @PostMapping(value = "/edit")
    public Result<AllianceBankCard> edit(@RequestBody AllianceBankCard allianceBankCard) {
        Result<AllianceBankCard> result = new Result<AllianceBankCard>();
        AllianceBankCard allianceBankCardEntity = allianceBankCardService.getById(allianceBankCard.getId());
        if (allianceBankCardEntity == null) {
            result.error500("未找到对应实体");
        } else {
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            boolean ok = allianceBankCardService.updateById(allianceBankCard
                    .setUpdateBy(sysUser.getUsername())
                    .setUpdateTime(new Date()));
            if (ok) {
                result.success("修改成功!");
            }else {
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
    @AutoLog(value = "加盟商银行卡-通过id删除")
    @ApiOperation(value = "加盟商银行卡-通过id删除", notes = "加盟商银行卡-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        try {
            allianceBankCardService.removeById(id);
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
    @AutoLog(value = "加盟商银行卡-批量删除")
    @ApiOperation(value = "加盟商银行卡-批量删除", notes = "加盟商银行卡-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<AllianceBankCard> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        Result<AllianceBankCard> result = new Result<AllianceBankCard>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别！");
        } else {
            this.allianceBankCardService.removeByIds(Arrays.asList(ids.split(",")));
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
    @AutoLog(value = "加盟商银行卡-通过id查询")
    @ApiOperation(value = "加盟商银行卡-通过id查询", notes = "加盟商银行卡-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<AllianceBankCard> queryById(@RequestParam(name = "id", required = true) String id) {
        Result<AllianceBankCard> result = new Result<AllianceBankCard>();
        AllianceBankCard allianceBankCard = allianceBankCardService.getById(id);
        if (allianceBankCard == null) {
            result.error500("未找到对应实体");
        } else {
            result.setResult(allianceBankCard);
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
        QueryWrapper<AllianceBankCard> queryWrapper = null;
        try {
            String paramsStr = request.getParameter("paramsStr");
            if (oConvertUtils.isNotEmpty(paramsStr)) {
                String deString = URLDecoder.decode(paramsStr, "UTF-8");
                AllianceBankCard allianceBankCard = JSON.parseObject(deString, AllianceBankCard.class);
                queryWrapper = QueryGenerator.initQueryWrapper(allianceBankCard, request.getParameterMap());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //Step.2 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        List<AllianceBankCard> pageList = allianceBankCardService.list(queryWrapper);
        //导出文件名称
        mv.addObject(NormalExcelConstants.FILE_NAME, "加盟商银行卡列表");
        mv.addObject(NormalExcelConstants.CLASS, AllianceBankCard.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("加盟商银行卡列表数据", "导出人:Jeecg", "导出信息"));
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
                List<AllianceBankCard> listAllianceBankCards = ExcelImportUtil.importExcel(file.getInputStream(), AllianceBankCard.class, params);
                allianceBankCardService.saveBatch(listAllianceBankCards);
                return Result.ok("文件导入成功！数据行数:" + listAllianceBankCards.size());
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

    @AutoLog(value = "加盟商银行卡-返显")
    @ApiOperation(value = "加盟商银行卡-返显", notes = "加盟商银行卡-返显")
    @GetMapping(value = "/returnBankCard")
    public Result<Map<String, Object>> returnBankCard() {
        Result<Map<String, Object>> result = new Result<>();
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        HashMap<String, Object> map = new HashMap<>();
        LambdaQueryWrapper<AllianceBankCard> allianceBankCardLambdaQueryWrapper = new LambdaQueryWrapper<AllianceBankCard>()
                .eq(AllianceBankCard::getSysUserId, sysUser.getId())
                .eq(AllianceBankCard::getCarType,"0")
                ;
        if (allianceBankCardService.count(allianceBankCardLambdaQueryWrapper)>0){
            AllianceBankCard allianceBankCard = allianceBankCardService
                    .getOne(allianceBankCardLambdaQueryWrapper);
            map.put("bankCard",allianceBankCard);
        }else {
            map.put("bankCard","未绑定");
        }
        result.setResult(map);
        result.success("银行卡返显");
        return result;
    }
    @AutoLog(value = "加盟商银行卡-修改and新增")
    @ApiOperation(value = "加盟商银行卡-修改and新增", notes = "加盟商银行卡-修改and新增")
    @PostMapping("allianceCardAudit")
    public Result<AllianceBankCard> allianceCardAudit(@RequestBody AllianceBankCardDTO allianceBankCardDTO){
        Result<AllianceBankCard> result = new Result<>();
        Object code = redisUtil.get(allianceBankCardDTO.getPhone());
        if (!allianceBankCardDTO.getSbCode().equals(code)){
            result.error500("验证码错误!");
        }else {
            AllianceBankCard allianceBankCard = new AllianceBankCard();
            BeanUtils.copyProperties(allianceBankCardDTO,allianceBankCard);
            if (StringUtils.isBlank(allianceBankCard.getId())){
                LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
                allianceBankCard.setSysUserId(sysUser.getId());
                return add(allianceBankCard);
            }else {
                return edit(allianceBankCard);
            }
        }
        return result;
    }
    @AutoLog(value = "返回加盟商银行卡信息(下拉列表)")
    @ApiOperation(value = "返回加盟商银行卡信息(下拉列表)", notes = "返回加盟商银行卡信息(下拉列表)")
    @GetMapping("findBankCardById")
    public List<AllianceBankCardDTO> findBankCardById (@RequestParam(value = "sysUserId", required = true) String sysUserId){
        return allianceBankCardService.findBankCardById(sysUserId);
    }
}
