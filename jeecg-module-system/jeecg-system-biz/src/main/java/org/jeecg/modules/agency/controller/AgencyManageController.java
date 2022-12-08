package org.jeecg.modules.agency.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.config.util.PermissionUtils;
import org.jeecg.modules.agency.dto.AgencyBalanceDTO;
import org.jeecg.modules.agency.dto.AgencyManageDTO;
import org.jeecg.modules.agency.entity.AgencyManage;
import org.jeecg.modules.agency.entity.AgencyRechargeRecord;
import org.jeecg.modules.agency.service.IAgencyManageService;
import org.jeecg.modules.agency.vo.AgencyBalanceVO;
import org.jeecg.modules.agency.vo.AgencyManageVO;
import org.jeecg.modules.system.entity.SysArea;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysAreaService;
import org.jeecg.modules.system.service.ISysUserService;
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
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Description: 代理列表
 * @Author: jeecg-boot
 * @Date: 2019-12-21
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "代理列表")
@RestController
@RequestMapping("/agencyManage/agencyManage")
public class AgencyManageController {
    @Autowired
    private IAgencyManageService agencyManageService;
    @Autowired
    private ISysAreaService sysAreaService;
    @Autowired
    private ISysUserService iSysUserService;

    /**
     * 分页列表查询
     *
     * @param agencyManageVO
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "代理列表-分页列表查询")
    @ApiOperation(value = "代理列表-分页列表查询", notes = "代理列表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<AgencyManageDTO>> queryPageList(AgencyManageVO agencyManageVO,
                                                        @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                        @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                        HttpServletRequest req) {
        Result<IPage<AgencyManageDTO>> result = new Result<IPage<AgencyManageDTO>>();
        Page<AgencyManage> page = new Page<AgencyManage>(pageNo, pageSize);
        IPage<AgencyManageDTO> pageList = agencyManageService.getAgencyManageList(page, agencyManageVO);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 添加
     *
     * @param agencyManage
     * @return
     */
    @AutoLog(value = "代理列表-添加")
    @ApiOperation(value = "代理列表-添加", notes = "代理列表-添加")
    @PostMapping(value = "/add")
    public Result<AgencyManage> add(@RequestBody AgencyManage agencyManage) {
        Result<AgencyManage> result = new Result<AgencyManage>();
        try {
            agencyManageService.save(agencyManage);
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
     * @param agencyManage
     * @return
     */
    @AutoLog(value = "代理列表-编辑")
    @ApiOperation(value = "代理列表-编辑", notes = "代理列表-编辑")
    @PutMapping(value = "/edit")
    public Result<AgencyManage> edit(@RequestBody AgencyManage agencyManage) {
        Result<AgencyManage> result = new Result<AgencyManage>();
        AgencyManage agencyManageEntity = agencyManageService.getById(agencyManage.getId());
        if (agencyManageEntity == null) {
            result.error500("未找到对应实体");
        } else {
            if (StringUtils.isBlank(agencyManage.getSysAreaId())) {
                result.error500("请选择代理区域");
                return result;
            } else {
                String[] sty = agencyManage.getSysAreaId().split(",");
                List<String> idss = Arrays.asList(sty);
                QueryWrapper<SysArea> queryWrapper = new QueryWrapper();
                queryWrapper.eq("agency_manage_id", agencyManage.getId());
                List<SysArea> sysAreaList = sysAreaService.list(queryWrapper);
                if (sysAreaList.size() > 0) {
                    //判断之前区域是否存在
                    Boolean bl = false;
                    for (SysArea sa : sysAreaList) {
                        bl = false;
                        for (String id : idss) {
                            if (id.equals(sa.getId())) {
                                bl = true;
                            }
                        }
                        if (!bl) {//之前的代理区域被修改
                            sa.setAgencyManageId("");
                            sysAreaService.updateById(sa);
                        }
                    }
                }
                idss.forEach(id -> {
                    SysArea sysArea = sysAreaService.getById(id);
                    if (sysArea != null) {
                        if (StringUtils.isBlank(sysArea.getAgencyManageId())) {
                            sysArea.setAgencyManageId(agencyManage.getId());
                        } else {
                            //已绑定，判断是不是本用户
                            if (sysArea.getAgencyManageId().equals(agencyManage.getId())) {

                            } else {
                                result.error500(sysArea.getName() + "区域已被使用");
                            }
                        }
                    }
                    //保存数据
                    sysAreaService.updateById(sysArea);
                });
            }
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            ParsePosition pos = new ParsePosition(0);
			/*agencyManage.setStartTime(formatter.parse(formatter.format(agencyManage.getStartTime()), pos));
			agencyManage.setEndTime(formatter.parse(formatter.format(agencyManage.getEndTime()), pos))	;*/
            boolean ok = agencyManageService.updateById(agencyManage);
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
    @AutoLog(value = "代理列表-通过id删除")
    @ApiOperation(value = "代理列表-通过id删除", notes = "代理列表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        try {
            agencyManageService.removeById(id);
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
    @AutoLog(value = "代理列表-批量删除")
    @ApiOperation(value = "代理列表-批量删除", notes = "代理列表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<AgencyManage> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        Result<AgencyManage> result = new Result<AgencyManage>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别！");
        } else {
            this.agencyManageService.removeByIds(Arrays.asList(ids.split(",")));
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
    @AutoLog(value = "代理列表-通过id查询")
    @ApiOperation(value = "代理列表-通过id查询", notes = "代理列表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<AgencyManage> queryById(@RequestParam(name = "id", required = true) String id) {
        Result<AgencyManage> result = new Result<AgencyManage>();
        AgencyManage agencyManage = agencyManageService.getById(id);
        if (agencyManage == null) {
            result.error500("未找到对应实体");
        } else {
            result.setResult(agencyManage);
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
        QueryWrapper<AgencyManage> queryWrapper = null;
        try {
            String paramsStr = request.getParameter("paramsStr");
            if (oConvertUtils.isNotEmpty(paramsStr)) {
                String deString = URLDecoder.decode(paramsStr, "UTF-8");
                AgencyManage agencyManage = JSON.parseObject(deString, AgencyManage.class);
                queryWrapper = QueryGenerator.initQueryWrapper(agencyManage, request.getParameterMap());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //Step.2 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        List<AgencyManage> pageList = agencyManageService.list(queryWrapper);
        //导出文件名称
        mv.addObject(NormalExcelConstants.FILE_NAME, "代理列表列表");
        mv.addObject(NormalExcelConstants.CLASS, AgencyManage.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("代理列表列表数据", "导出人:Jeecg", "导出信息"));
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
                List<AgencyManage> listAgencyManages = ExcelImportUtil.importExcel(file.getInputStream(), AgencyManage.class, params);
                agencyManageService.saveBatch(listAgencyManages);
                return Result.ok("文件导入成功！数据行数:" + listAgencyManages.size());
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
     * 批量修改:启用停用
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "代理列表-通过id查询")
    @ApiOperation(value = "代理列表-通过id查询", notes = "代理列表-通过id查询")
    @GetMapping(value = "/updateStatus")
    public Result<AgencyManage> updateStatus(@RequestParam(name = "ids", required = true) String ids, @RequestParam(name = "status") String status, @RequestParam(name = "closeExplain") String closeExplain) {
        Result<AgencyManage> result = new Result<AgencyManage>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别！");
        } else {
            AgencyManage agencyManage;
            try {
                List<String> listid = Arrays.asList(ids.split(","));
                for (String id : listid) {
                    agencyManage = agencyManageService.getById(id);
                    if (agencyManage == null) {
                        result.error500("未找到对应实体");
                    } else {
                        agencyManage.setCloseExplain(closeExplain);
                        agencyManage.setStatus(status);
                        agencyManageService.updateById(agencyManage);
                        if (status.equals("0")) {
                            //停用后清空所有代理区域
                            QueryWrapper<SysArea> queryWrapper = new QueryWrapper<>();
                            queryWrapper.eq("agency_manage_id", agencyManage.getId());
                            List<SysArea> areaList = sysAreaService.list(queryWrapper);
                            if (areaList.size() > 0) {
                                areaList.forEach(al -> {
                                    al.setAgencyManageId("");
                                    sysAreaService.updateById(al);
                                });
                            }

                        }
                    }
                }
                result.success("修改成功!");
            } catch (Exception e) {
                result.error500("修改失败！");
            }
        }
        return result;
    }

    /**
     * * 获取当前登录用户的代理信息 ，平台登录获取所有代理信息
     * * @param agencyRechargeRecord
     * * @param req
     * * @return
     */
    @AutoLog(value = "获取代理信息")
    @ApiOperation(value = "获取代理信息", notes = "获取代理信息")
    @GetMapping(value = "/queryPageListAndManage")
    public Result<Map<String, Object>> queryPageListAndManage(AgencyRechargeRecord agencyRechargeRecord, HttpServletRequest req) {
        Result<Map<String, Object>> result = new Result<Map<String, Object>>();
        QueryWrapper<AgencyManage> queryWrapperAgencyManage = new QueryWrapper();

        Map<String, Object> map = Maps.newHashMap();
        Map<String, Object> objectMap = Maps.newHashMap();
        //省市县权限添加权限
        String str = PermissionUtils.ifPlatformArea();
        if (str != null) {
            queryWrapperAgencyManage.eq("sys_user_id", str);
            AgencyManage agencyManage = agencyManageService.getOne(queryWrapperAgencyManage);
            map.put("id", agencyManage.getId());
            map.put("balance", agencyManage.getBalance());
            map.put("accountFrozen", agencyManage.getAccountFrozen());
            map.put("unusableFrozen", agencyManage.getUnusableFrozen());
            map.put("sysUserId", agencyManage.getSysUserId());
        } else {
            //平台登录
            BigDecimal balance = new BigDecimal(0);
            BigDecimal accountFrozen = new BigDecimal(0);
            BigDecimal unusableFrozen = new BigDecimal(0);

            QueryWrapper<AgencyManage> queryWrapperAgencyManageList = new QueryWrapper();
            queryWrapperAgencyManageList.eq("status", "1");
            List<AgencyManage> agencyManageList = agencyManageService.list(queryWrapperAgencyManageList);
            for (AgencyManage sm : agencyManageList) {
                balance = balance.add(sm.getBalance());
                accountFrozen = accountFrozen.add(sm.getAccountFrozen());
                unusableFrozen = unusableFrozen.add(sm.getUnusableFrozen());
            }
            map.put("id", "");
            map.put("balance", balance);
            map.put("accountFrozen", accountFrozen);
            map.put("unusableFrozen", unusableFrozen);
        }
        //objectMap.put("storeManage",map);
        result.setResult(map);
        result.setSuccess(true);

        return result;
    }

    /**
     * 代理下拉选择列表
     *
     * @return
     */
    @AutoLog(value = "代理下拉选择列表")
    @ApiOperation(value = "代理下拉选择列表", notes = "代理下拉选择列表")
    @GetMapping(value = "/getAgencyManageMap")
    public Result<List<Map<String, Object>>> getAgencyManageMap() {
        Result<List<Map<String, Object>>> result = new Result<List<Map<String, Object>>>();
        List<Map<String, Object>> mapList = agencyManageService.getAgencyManageMap();
        result.setResult(mapList);
        result.success("true");
        return result;
    }

    ;

    @AutoLog(value = "代理余额列表")
    @ApiOperation(value = "代理余额列表", notes = "代理余额列表")
    @GetMapping(value = "/findAgencyBalance")
    public Result<IPage<AgencyBalanceDTO>> findAgencyBalance(AgencyBalanceVO agencyBalanceVO,
                                                             @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                             @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                             HttpServletRequest req) {
        Result<IPage<AgencyBalanceDTO>> result = new Result<>();
        Page<AgencyBalanceDTO> page = new Page<AgencyBalanceDTO>(pageNo, pageSize);
        IPage<AgencyBalanceDTO> agencyBalance= agencyManageService.findAgencyBalance(page,agencyBalanceVO);
        result.setCode(200);
        result.setResult(agencyBalance);
        return result;
    }
    @AutoLog(value = "代理信息")
    @ApiOperation(value = "代理信息", notes = "代理信息")
    @GetMapping(value = "/getAgencyManageVO")
    public AgencyManageVO getAgencyManageVO(){
        return agencyManageService.getAgencyManageVO();
    }
    @AutoLog(value = "代理信息返显")
    @ApiOperation(value = "代理信息返显", notes = "代理信息返显")
    @GetMapping(value = "/returnAgencyInfo")
    public Result<AgencyManageVO> returnAgencyInfo(){
        Result<AgencyManageVO> result = new Result<>();
        AgencyManageVO agencyManageVO = agencyManageService.returnAgencyInfo();
        result.success("代理信息返显成功!");
        result.setResult(agencyManageVO);
        return result;
    }
    @AutoLog(value = "代理信息保存")
    @ApiOperation(value = "代理信息保存", notes = "代理信息保存")
    @PostMapping("updateAgencyInfo")
    public Result<AgencyManage> updateAgencyInfo(@RequestBody AgencyManageVO agencyManageVO){
        Result<AgencyManage> result = new Result<>();
        AgencyManage agencyManage = new AgencyManage();
        SysUser sysUser = iSysUserService.getById(agencyManageVO.getSysUserId());
        if (!sysUser.getAvatar().equals(agencyManageVO.getAvatar())){
            iSysUserService.saveOrUpdate(sysUser.setAvatar(agencyManageVO.getAvatar()));
        }
        BeanUtils.copyProperties(agencyManageVO,agencyManage);
        boolean b = agencyManageService.saveOrUpdate(agencyManage);
        if (b){
            return result.success("保存成功!");
        }else {
            return result.error500("保存失败!");
        }
    }
    //通过手机号模糊查询代理信息
    @GetMapping("findAgencyManageByPhone")
    public Result<List<Map<String,Object>>> findAgencyManageByPhone(@RequestParam("phone")String phone){
        Result<List<Map<String, Object>>> result = new Result<>();
        result.setResult(agencyManageService.findAgencyManageByPhone(phone));
        return result;
    }
}
