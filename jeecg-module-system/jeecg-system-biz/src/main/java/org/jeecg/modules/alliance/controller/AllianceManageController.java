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
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.config.jwt.utils.WeixinQRUtils;
import org.jeecg.modules.alliance.dto.AllianceManageDTO;
import org.jeecg.modules.alliance.entity.AllianceManage;
import org.jeecg.modules.alliance.service.IAllianceManageService;
import org.jeecg.modules.alliance.vo.AllianceManageVO;
import org.jeecg.modules.member.service.IMemberDesignationGroupService;
import org.jeecg.modules.store.entity.StoreManage;
import org.jeecg.modules.store.service.IStoreManageService;
import org.jeecg.modules.system.entity.SysArea;
import org.jeecg.modules.system.entity.SysSmallcode;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysAreaService;
import org.jeecg.modules.system.service.ISysSmallcodeService;
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
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 加盟商管理
 * @Author: jeecg-boot
 * @Date: 2020-05-17
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "加盟商管理")
@RestController
@RequestMapping("/allianceManage/allianceManage")
public class AllianceManageController {
    @Autowired
    private IAllianceManageService allianceManageService;
    @Autowired
    private ISysUserService iSysUserService;
    @Autowired
    private ISysSmallcodeService iSysSmallcodeService;
    @Autowired
    private WeixinQRUtils weixinQRUtils;
    @Autowired
    private ISysAreaService sysAreaService;
    @Autowired
    private IStoreManageService iStoreManageService;

    @Autowired
    private IMemberDesignationGroupService iMemberDesignationGroupService;


    /**
     * 修改统计状态
     *
     * @param allianceManageId
     * @return
     */
    @GetMapping("updateIsView")
    public Result<?> updateIsView(String allianceManageId){
        AllianceManage allianceManage=allianceManageService.getById(allianceManageId);
        if(allianceManage.getIsView().equals("0")){
            allianceManage.setIsView("1");
            allianceManageService.saveOrUpdate(allianceManage);
            return Result.ok();
        }
        if(allianceManage.getIsView().equals("1")){
            allianceManage.setIsView("0");
            allianceManageService.saveOrUpdate(allianceManage);
            return Result.ok();
        }
        return Result.ok();
    }

    /**
     * 分页列表查询
     *
     * @param
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "加盟商管理-分页列表查询")
    @ApiOperation(value = "加盟商管理-分页列表查询", notes = "加盟商管理-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<AllianceManageVO>> queryPageList(AllianceManageDTO allianceManageDTO,
                                                         @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                         @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                         HttpServletRequest req) {
        Result<IPage<AllianceManageVO>> result = new Result<IPage<AllianceManageVO>>();
        Page<AllianceManage> page = new Page<AllianceManage>(pageNo, pageSize);
        IPage<AllianceManageVO> pageList = allianceManageService.queryPageList(page, allianceManageDTO);
        //分配比例数据处理
        List<AllianceManageVO> records = pageList.getRecords();
        for (AllianceManageVO record : records) {

            if ("0".equals(record.getProfitType())) {

                record.setAllocationStr("-");

            } else {

                StringBuilder stringBuilder = new StringBuilder();
                if ("0".equals(record.getMutualAdvantages())) {
                    stringBuilder.append("加盟商比例:")
                            .append(record.getFranchiseeRatio()).append("%")
                            .append(",")
                            .append("县级代理比例:")
                            .append(record.getAgencyRatio()).append("%");

                } else {
                    StringBuilder address = new StringBuilder();
                    //获取地址信息
                    if (StringUtils.isNotEmpty(record.getProvinceId())) {
                        SysArea province = sysAreaService.getById(record.getProvinceId());
                        address.append(province.getName());
                    }
                    if (StringUtils.isNotEmpty(record.getCityId())) {
                        SysArea city = sysAreaService.getById(record.getCityId());
                        address.append(city.getName());
                    }
                    if (StringUtils.isNotEmpty(record.getCountyId())) {
                        SysArea county = sysAreaService.getById(record.getCountyId());
                        address.append(county.getName());
                    }
                    stringBuilder.append(address.toString()).append(",")
                            .append("加盟商比例:")
                            .append(record.getFranchiseeRatio()).append("%")
                            .append(",")
                            .append("县级代理比例:")
                            .append(record.getAgencyRatio()).append("%");
                }

                record.setAllocationStr(stringBuilder.toString());
            }


        }

        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 添加
     *
     * @param allianceManage
     * @return
     */
    @AutoLog(value = "加盟商管理-添加")
    @ApiOperation(value = "加盟商管理-添加", notes = "加盟商管理-添加")
    @PostMapping(value = "/add")
    public Result<AllianceManage> add(@RequestBody AllianceManage allianceManage) {
        Result<AllianceManage> result = new Result<AllianceManage>();
        try {
            allianceManageService.save(allianceManage);
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
     * @param allianceManage
     * @return
     */
    @AutoLog(value = "加盟商管理-编辑")
    @ApiOperation(value = "加盟商管理-编辑", notes = "加盟商管理-编辑")
    @PutMapping(value = "/edit")
    public Result<AllianceManage> edit(@RequestBody AllianceManage allianceManage) {
        Result<AllianceManage> result = new Result<AllianceManage>();
        AllianceManage allianceManageEntity = allianceManageService.getById(allianceManage.getId());
        if (allianceManageEntity == null) {
            result.error500("未找到对应实体");
        } else {
            boolean ok = allianceManageService.updateById(allianceManage);
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
    @AutoLog(value = "加盟商管理-通过id删除")
    @ApiOperation(value = "加盟商管理-通过id删除", notes = "加盟商管理-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        try {
            allianceManageService.removeById(id);
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
    @AutoLog(value = "加盟商管理-批量删除")
    @ApiOperation(value = "加盟商管理-批量删除", notes = "加盟商管理-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<AllianceManage> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        Result<AllianceManage> result = new Result<AllianceManage>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别！");
        } else {
            this.allianceManageService.removeByIds(Arrays.asList(ids.split(",")));
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
    @AutoLog(value = "加盟商管理-通过id查询")
    @ApiOperation(value = "加盟商管理-通过id查询", notes = "加盟商管理-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<AllianceManage> queryById(@RequestParam(name = "id", required = true) String id) {
        Result<AllianceManage> result = new Result<AllianceManage>();
        AllianceManage allianceManage = allianceManageService.getById(id);
        if (allianceManage == null) {
            result.error500("未找到对应实体");
        } else {
            result.setResult(allianceManage);
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
        QueryWrapper<AllianceManage> queryWrapper = null;
        try {
            String paramsStr = request.getParameter("paramsStr");
            if (oConvertUtils.isNotEmpty(paramsStr)) {
                String deString = URLDecoder.decode(paramsStr, "UTF-8");
                AllianceManage allianceManage = JSON.parseObject(deString, AllianceManage.class);
                queryWrapper = QueryGenerator.initQueryWrapper(allianceManage, request.getParameterMap());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //Step.2 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        List<AllianceManage> pageList = allianceManageService.list(queryWrapper);
        //导出文件名称
        mv.addObject(NormalExcelConstants.FILE_NAME, "加盟商管理列表");
        mv.addObject(NormalExcelConstants.CLASS, AllianceManage.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("加盟商管理列表数据", "导出人:Jeecg", "导出信息"));
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
                List<AllianceManage> listAllianceManages = ExcelImportUtil.importExcel(file.getInputStream(), AllianceManage.class, params);
                allianceManageService.saveBatch(listAllianceManages);
                return Result.ok("文件导入成功！数据行数:" + listAllianceManages.size());
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

    @AutoLog(value = "返回加盟商基本信息-通过id查询")
    @ApiOperation(value = "返回加盟商基本信息-通过id查询", notes = "返回加盟商基本信息-通过id查询")
    @GetMapping(value = "/findAllianceManageInfo")
    public Result<AllianceManageVO> findAllianceManageInfo() {
        Result<AllianceManageVO> result = new Result<>();
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        AllianceManage allianceManage = allianceManageService.getOne(new LambdaQueryWrapper<AllianceManage>()
                .eq(AllianceManage::getSysUserId, sysUser.getId()));
        if (oConvertUtils.isNotEmpty(allianceManage)) {
            if (StringUtils.isBlank(allianceManage.getSysSmallcodeId())) {
                SysSmallcode sysSmallcode = new SysSmallcode()
                        .setDelFlag("0")
                        .setSysUserId(allianceManage.getSysUserId())
                        .setCodeType("3")
                        .setPage("pages/openShop/shopPromotion");
                iSysSmallcodeService.save(sysSmallcode);
                allianceManage.setSysSmallcodeId(sysSmallcode.getId());
                allianceManageService.saveOrUpdate(allianceManage);
                sysSmallcode.setAddress(weixinQRUtils.getCommercialQrCodeByPage(sysSmallcode.getId(), "pages/openShop/shopPromotion"));
                iSysSmallcodeService.saveOrUpdate(sysSmallcode);
            }
        }
        AllianceManageVO allianceManageInfo = allianceManageService.findAllianceManageInfo(sysUser.getId());
        result.setResult(allianceManageInfo);
        result.success("返回加盟商基本信息");
        return result;
    }

    @AutoLog(value = "修改加盟商基本信息")
    @ApiOperation(value = "修改加盟商基本信息", notes = "修改加盟商基本信息")
    @PostMapping(value = "/updataAllianceManageInfo")
    public Result<AllianceManage> updataAllianceManageInfo(@RequestBody AllianceManageDTO allianceManageDTO) {
        SysUser user = iSysUserService.getById(allianceManageDTO.getSysUserId());
        if (StringUtils.isBlank(user.getAvatar()) || !allianceManageDTO.getAvatar().equals(user.getAvatar())) {
            iSysUserService.saveOrUpdate(user.setAvatar(allianceManageDTO.getAvatar()));
        }
        AllianceManage allianceManage = new AllianceManage();
        BeanUtils.copyProperties(allianceManageDTO, allianceManage);
        return edit(allianceManage);
    }

    @GetMapping("findAllianceManageByphone")
    public Result<List<Map<String, Object>>> findAllianceManageByphone(@RequestParam("phone") String phone) {
        Result<List<Map<String, Object>>> result = new Result<>();
        result.setResult(allianceManageService.findAllianceManageByphone(phone));
        return result;
    }
    /**
     * 加盟商基本数据
     * @return
     */
    @GetMapping("findAlianceManageBasicsInfo")
    public Result<?> findAlianceManageBasicsInfo(){
        LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        List<AllianceManage> allianceManageList = allianceManageService.list(new LambdaQueryWrapper<AllianceManage>()
                .eq(AllianceManage::getDelFlag, "0")
                .eq(AllianceManage::getSysUserId, user.getId())
                .orderByDesc(AllianceManage::getCreateTime)
        );
        if (allianceManageList.size()>0){
            AllianceManage allianceManage = allianceManageList.get(0);
            HashMap<String, Object> map = new HashMap<>();
            map.put("name",allianceManage.getName());
            long count = iStoreManageService.count(new LambdaQueryWrapper<StoreManage>()
                    .eq(StoreManage::getDelFlag, "0")
                    .eq(StoreManage::getAllianceUserId, user.getId())
                    .in(StoreManage::getPayStatus, "1", "2")
            );
            map.put("count",count);
            return Result.ok(map);
        }else {
            return Result.error("");
        }
    }

    /**
     * 全国合伙人总数
     * @return
     */
    @GetMapping("partnerSum")
    public Result<?> partnerSum(){
        return Result.ok(allianceManageService.partnerSum());
    }

    /**
     * 今日合伙人
     * @return
     */
    @GetMapping("toDayPartner")
    public Result<?> toDayPartner(){
        return Result.ok(allianceManageService.toDayPartner());
    }

    /**
     * 加盟体验馆列表
     * @return
     */
    @GetMapping("getAllianceStoreManage")
    public Result<?> getAllianceStoreManage(){
        LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        return Result.ok(allianceManageService.getAllianceStoreManage(user.getId()));
    }
    /**
     * 加盟体验馆新增合伙人展示
     */
    @GetMapping("getNewPartner")
    public Result<?> getNewPartner(){
        LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        return Result.ok(iMemberDesignationGroupService.getNewPartner(user.getId()));
    }

    /**
     * 全国业绩总和
     * @return
     */
    @GetMapping("getPerformanceSum")
    public Result<?> getPerformanceSum(){
        return Result.ok(allianceManageService.getPerformanceSum());
    }

    /**
     * 今日业绩
     * @return
     */
    @GetMapping("getToDayPerformance")
    public Result<?> getToDayPerformance(){
        return Result.ok(allianceManageService.getToDayPerformance());
    }
}
