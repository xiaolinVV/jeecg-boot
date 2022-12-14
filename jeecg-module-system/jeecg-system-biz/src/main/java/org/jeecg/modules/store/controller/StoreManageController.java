package org.jeecg.modules.store.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
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
import org.jeecg.config.util.PermissionUtils;
import org.jeecg.modules.alliance.entity.AllianceManage;
import org.jeecg.modules.alliance.service.IAllianceManageService;
import org.jeecg.config.jwt.utils.WeixinQRUtils;
import org.jeecg.modules.map.api.AfterMapController;
import org.jeecg.modules.marketing.entity.MarketingWelfarePaymentsSetting;
import org.jeecg.modules.marketing.service.IMarketingWelfarePaymentsSettingService;
import org.jeecg.modules.member.utils.QrCodeUtils;
import org.jeecg.modules.store.dto.StoreManageDTO;
import org.jeecg.modules.store.entity.StoreManage;
import org.jeecg.modules.store.service.IStoreManageService;
import org.jeecg.modules.store.vo.StoreManageVO;
import org.jeecg.modules.system.entity.SysSmallcode;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysDictService;
import org.jeecg.modules.system.service.ISysSmallcodeService;
import org.jeecg.modules.system.service.ISysUserRoleService;
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
import java.util.*;

/**
 * @Description: 店铺
 * @Author: jeecg-boot
 * @Date: 2019-10-14
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "店铺")
@RestController
@RequestMapping("/storeManage/storeManage")
public class StoreManageController {
    @Autowired
    private IStoreManageService storeManageService;
    @Autowired
    private IMarketingWelfarePaymentsSettingService iMarketingWelfarePaymentsSettingService;
    @Autowired
    private ISysSmallcodeService iSysSmallcodeService;
    @Autowired
    private WeixinQRUtils weixinQRUtils;
    @Autowired
    private AfterMapController afterMapController;
    @Autowired
    private ISysUserRoleService iSysUserRoleService;
    @Autowired
    private IAllianceManageService iAllianceManageService;
    @Autowired
    private ISysUserService iSysUserService;

    @Autowired
    private ISysDictService iSysDictService;


    @Autowired
    private QrCodeUtils qrCodeUtils;


    /**
     * 获取所有的店铺列表
     *
     * @return
     */
    @GetMapping("getAllStoreList")
    public Result<?> getAllStoreList(){
        return Result.ok(storeManageService.getAllStoreList());
    }



    /**
     * 获取店铺收款码
     * @param storeManageId
     * @return
     */
    @RequestMapping("getMoneyReceivingCode")
    @ResponseBody
    public Result<?> getMoneyReceivingCode(String storeManageId){
        if(StringUtils.isBlank(storeManageId)){
            return Result.error("店铺id不能为空");
        }
        StoreManage storeManage=storeManageService.getById(storeManageId);
        if(storeManage==null){
            return Result.error("店铺不存在");
        }
        Map<String,Object> resultMap= Maps.newHashMap();
        resultMap.put("id",storeManage.getId());
        String address="";
        if(StringUtils.isBlank(storeManage.getMoneyReceivingCode())){
            address=qrCodeUtils.getMoneyReceivingCode(storeManageId);
            storeManage.setMoneyReceivingCode(address);
            storeManageService.saveOrUpdate(storeManage);
        }else{
            address=storeManage.getMoneyReceivingCode();
        }
        resultMap.put("address",address);
        return Result.ok(resultMap);
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
    @AutoLog(value = "店铺-分页列表查询")
    @ApiOperation(value = "店铺-分页列表查询", notes = "店铺-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<StoreManageDTO>> queryPageList(StoreManageVO storeManageVO,
                                                       @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                       @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                       HttpServletRequest req) {
        Result<IPage<StoreManageDTO>> result = new Result<IPage<StoreManageDTO>>();
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        List<String> userType = iSysUserRoleService.getRoleByUserId(sysUser.getId());
        userType.forEach(type -> {

            if (type.equals("Provincial_agents")) {
                storeManageVO.setProvincialId(sysUser.getId());
            }

            if (type.equals("Municipal_agent")) {
                storeManageVO.setMunicipalId(sysUser.getId());
            }

            if (type.equals("County_agent")) {
                storeManageVO.setCountyId(sysUser.getId());
            }
        });
        Page<StoreManageDTO> page = new Page<StoreManageDTO>(pageNo, pageSize);

        IPage<StoreManageDTO> pageList = storeManageService.queryStoreManagePage(page, storeManageVO);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    @AutoLog(value = "店铺-测试")
    @ApiOperation(value = "店铺-测试", notes = "店铺-测试")
    @GetMapping(value = "/testList")
    public Result<List<StoreManageDTO>> findPageList(StoreManageVO storeManageVO) {
        Result<List<StoreManageDTO>> result = new Result<>();
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        List<StoreManageDTO> pageList = storeManageService.findPageList(storeManageVO);
        List<String> user = iSysUserRoleService.getRoleByUserId(sysUser.getId());
        ArrayList<Object> objects = new ArrayList<>();
        objects.add("County_agent");
        objects.add("Provincial_agents");
        objects.add("Municipal_agent");
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 分页列表查询
     *
     * @param storeManage
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "店铺列表-分页列表查询")
    @ApiOperation(value = "店铺列表-分页列表查询", notes = "店铺列表-分页列表查询")
    @GetMapping(value = "/pickUpList")
    public Result<IPage<StoreManage>> pickUpList(StoreManage storeManage,
                                                 @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                 HttpServletRequest req) {
        Result<IPage<StoreManage>> result = new Result<IPage<StoreManage>>();
        QueryWrapper<StoreManage> queryWrapper = QueryGenerator.initQueryWrapper(storeManage, req.getParameterMap());
        PermissionUtils.accredit(queryWrapper);
        Page<StoreManage> page = new Page<StoreManage>(pageNo, pageSize);
        IPage<StoreManage> pageList = storeManageService.page(page, queryWrapper);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    @AutoLog(value = "礼包选择门店接口")
    @ApiOperation(value = "礼包选择门店接口", notes = "礼包选择门店接口")
    @GetMapping("findStoreManage")
    public Result<Map<String, Object>> findStoreManage(StoreManageDTO storeManageDTO) {
        Result<Map<String, Object>> result = new Result<>();
        List<StoreManageDTO> storeManage = storeManageService.findStoreManage(storeManageDTO);
        HashMap<String, Object> map = new HashMap<>();
        map.put("records", storeManage);
        result.setSuccess(true);
        result.setResult(map);
        return result;
    }

    /**
     * 同城配送列表
     *
     * @param
     * @return
     */
    @AutoLog(value = "店铺-同城配送列表")
    @ApiOperation(value = "店铺-同城配送列表", notes = "店铺-同城配送列表")
    @RequestMapping(value = "findStoreByAccountingRules", method = RequestMethod.GET)
    public Result<StoreManageVO> findStoreByAccountingRules() {
        Result<StoreManageVO> result = new Result<>();
        //获取当前登录人
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        //查出当前登录人对应的店铺
        QueryWrapper<StoreManage> storeManageQueryWrapper = new QueryWrapper<>();
        storeManageQueryWrapper.eq("sys_user_id", sysUser.getId());
        //获得当前登录人对应的店铺信息
        StoreManage one = storeManageService.getOne(storeManageQueryWrapper);
        //获取计费规则
        String accountingRules = one.getAccountingRules();
        StoreManageVO storeManageVO = new StoreManageVO();
        BeanUtils.copyProperties(one, storeManageVO);
        if (StringUtils.isBlank(accountingRules)) {
            storeManageVO.setAccountingRules(null);
        } else {
            JSONObject jsonObject = JSONObject.parseObject(accountingRules);
            String distributionFeeDistance = jsonObject.getString("distributionFeeDistance");
            String distributionFee = jsonObject.getString("distributionFee");
            String everyIncrease = jsonObject.getString("everyIncrease");
            String increaseInDeliveryFee = jsonObject.getString("increaseInDeliveryFee");
            //json拼接
            StringBuilder append = new StringBuilder().append("配送费:" + distributionFeeDistance).append("公里内" + distributionFee).append("元,每增加" + everyIncrease).append("公里配送费增加" + increaseInDeliveryFee + "元");
            storeManageVO.setAccountingName(append);
        }
        result.setSuccess(true);
        result.setResult(storeManageVO);
        return result;
    }

    @AutoLog(value = "店铺-商品调用接口")
    @ApiOperation(value = "店铺-商品调用接口", notes = "店铺-商品调用接口")
    @RequestMapping(value = "findGoodStoreById", method = RequestMethod.GET)
    public Result<StoreManage> findGoodStoreById(@RequestParam(value = "userId", required = true) String userId) {
        Result<StoreManage> result = new Result<>();
        QueryWrapper<StoreManage> storeManageQueryWrapper = new QueryWrapper<>();
        storeManageQueryWrapper.eq("sys_user_id", userId);
        StoreManage one = storeManageService.getOne(storeManageQueryWrapper);
        result.setSuccess(true);
        result.setResult(one);
        return result;
    }




    /**
     * 添加
     *
     * @param storeManage
     * @return
     */
    @AutoLog(value = "店铺-添加")
    @ApiOperation(value = "店铺-添加", notes = "店铺-添加")
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Result<StoreManage> add(@RequestBody StoreManage storeManage) {
        Result<StoreManage> result = new Result<StoreManage>();
        try {
            storeManageService.save(storeManage);
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
     * @param storeManage
     * @return
     */
    @AutoLog(value = "店铺-编辑")
    @ApiOperation(value = "店铺-编辑", notes = "店铺-编辑")
    @PutMapping(value = "/edit")
    public Result<StoreManage> edit(@RequestBody StoreManage storeManage) {
        Result<StoreManage> result = new Result<StoreManage>();
        StoreManage storeManageEntity = storeManageService.getById(storeManage.getId());
        if (storeManageEntity == null) {
            result.error500("未找到对应实体");
        } else {
            boolean ok = storeManageService.updateById(storeManage);
            if (ok) {
                result.success("修改成功!");
            } else {
                result.error500("修改失败!");
            }
        }

        return result;
    }

    /**
     * 认证
     *
     * @param
     * @return
     */
    @AutoLog(value = "店铺-认证返显")
    @ApiOperation(value = "店铺-认证返显", notes = "店铺-认证返显")
    @RequestMapping(value = "identification", method = RequestMethod.GET)
    public Result<StoreManage> identification() {
        Result<StoreManage> result = new Result<>();
        //获取当前登录人信息
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String userId = sysUser.getId();
        QueryWrapper<StoreManage> storeManageQueryWrapper = new QueryWrapper<>();
        storeManageQueryWrapper.eq("sys_user_id", userId);
        StoreManage one = storeManageService.getOne(storeManageQueryWrapper);
        result.setSuccess(true);
        result.setResult(one);
        return result;

    }

    /**
     * 审核
     *
     * @param storeManage
     * @return
     */
    @AutoLog(value = "店铺-审核")
    @ApiOperation(value = "店铺-审核", notes = "店铺-审核")
    @PutMapping(value = "/audit")
    public Result<StoreManage> audit(@RequestBody StoreManage storeManage) {
        Result<StoreManage> result = new Result<StoreManage>();
        StoreManage storeManageEntity = storeManageService.getById(storeManage.getId());
        if (storeManageEntity == null) {
            result.error500("未找到对应实体");
        } else {
            boolean ok = storeManageService.updateById(storeManage);
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
    @AutoLog(value = "店铺-通过id删除")
    @ApiOperation(value = "店铺-通过id删除", notes = "店铺-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        try {
            storeManageService.removeById(id);
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
    @AutoLog(value = "店铺-批量删除")
    @ApiOperation(value = "店铺-批量删除", notes = "店铺-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<StoreManage> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        Result<StoreManage> result = new Result<StoreManage>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别！");
        } else {
            this.storeManageService.removeByIds(Arrays.asList(ids.split(",")));
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
    @AutoLog(value = "店铺-通过id查询")
    @ApiOperation(value = "店铺-通过id查询", notes = "店铺-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<StoreManage> queryById(@RequestParam(name = "id", required = true) String id) {
        Result<StoreManage> result = new Result<StoreManage>();
        StoreManage storeManage = storeManageService.getById(id);
        if (storeManage == null) {
            result.error500("未找到对应实体");
        } else {
            result.setResult(storeManage);
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
        QueryWrapper<StoreManage> queryWrapper = null;
        try {
            String paramsStr = request.getParameter("paramsStr");
            if (oConvertUtils.isNotEmpty(paramsStr)) {
                String deString = URLDecoder.decode(paramsStr, "UTF-8");
                StoreManage storeManage = JSON.parseObject(deString, StoreManage.class);
                queryWrapper = QueryGenerator.initQueryWrapper(storeManage, request.getParameterMap());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //Step.2 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        List<StoreManage> pageList = storeManageService.list(queryWrapper);
        //导出文件名称
        mv.addObject(NormalExcelConstants.FILE_NAME, "店铺列表");
        mv.addObject(NormalExcelConstants.CLASS, StoreManage.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("店铺列表数据", "导出人:Jeecg", "导出信息"));
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
                List<StoreManage> listStoreManages = ExcelImportUtil.importExcel(file.getInputStream(), StoreManage.class, params);
                storeManageService.saveBatch(listStoreManages);
                return Result.ok("文件导入成功！数据行数:" + listStoreManages.size());
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
     * 通过id修改店铺列表状态
     */
    @AutoLog(value = "店铺-通过id修改店铺列表状态")
    @ApiOperation(value = "店铺-通过id修改店铺列表状态", notes = "店铺-通过id修改店铺列表状态")
    @RequestMapping(value = "/updateStatusById", method = RequestMethod.GET)
    public Result<StoreManage> updateStatusById(@RequestParam("id") String id,
                                                @RequestParam("status") String status,
                                                @RequestParam("closeExplain") String closeExplain) {
        Result<StoreManage> result = new Result<StoreManage>();
        StoreManage storeManage = new StoreManage();
        UpdateWrapper<StoreManage> storeManageUpdateWrapper = new UpdateWrapper<>();
        storeManageUpdateWrapper.eq("id", id);
        storeManage.setStatus(status);
        storeManage.setCloseExplain(closeExplain);
        boolean b = storeManageService.update(storeManage, storeManageUpdateWrapper);
        if (b) {
            result.success("操作成功");
        } else {
            result.error500("操作失败");
        }
        return result;
    }

    /**
     * 通过id修改店铺列表到店自提状态
     *
     * @param
     * @return
     */
    @AutoLog(value = "店铺-通过id修改店铺列表到店自提状态")
    @ApiOperation(value = "店铺-通过id修改店铺列表到店自提状态", notes = "店铺-通过id修改店铺列表到店自提状态")
    @RequestMapping(value = "/updatePickUpStatusById", method = RequestMethod.GET)
    public Result<StoreManage> updatePickUpStatusById(@RequestParam(name = "id", required = true) String id,
                                                      @RequestParam(name = "pickUpStopRemark", required = true) String pickUpStopRemark) {
        Result<StoreManage> result = new Result<StoreManage>();
        StoreManage storeManageById = storeManageService.getById(id);
        if (storeManageById == null) {
            result.error500("未找到对应实体");
        } else {
            if ("1".equals(storeManageById.getPickUpStatus())) {
                storeManageById.setPickUpStatus("0");
                storeManageById.setPickUpStopRemark(pickUpStopRemark);
            } else {
                storeManageById.setPickUpStatus("1");
                storeManageById.setPickUpStopRemark(" ");
            }
        }
        boolean ok = storeManageService.updateById(storeManageById);
        if (ok) {
            result.success("修改成功");
        } else {
            result.error500("修改失败");
        }
        return result;
    }

    /**
     * 通过id修改店铺列表配送状态
     *
     * @param
     * @return
     */
    @AutoLog(value = "店铺-通过id修改店铺列表配送状态")
    @ApiOperation(value = "店铺-通过id修改店铺列表配送状态", notes = "店铺-通过id修改店铺列表配送状态")
    @RequestMapping(value = "/updateDistributionStatusById", method = RequestMethod.GET)
    public Result<StoreManage> updateDistributionStatusById(@RequestParam(name = "id", required = true) String id,
                                                            @RequestParam(name = "distributionStopRemark", required = true) String distributionStopRemark) {
        Result<StoreManage> result = new Result<StoreManage>();
        StoreManage serviceById = storeManageService.getById(id);
        if (serviceById == null) {
            result.error500("找不到实体");
        } else {
            if ("1".equals(serviceById.getDistributionStatus())) {
                serviceById.setDistributionStatus("0");
                serviceById.setDistributionStopRemark(distributionStopRemark);
            } else {
                serviceById.setDistributionStatus("1");
                serviceById.setDistributionStopRemark(" ");
            }
        }
        boolean b = storeManageService.updateById(serviceById);
        if (b) {
            result.success("操作成功");
        } else {
            result.error500("操作失败");
        }
        return result;
    }

    /**
     * 通过id去修改自提备注
     *
     * @param id
     * @param pickUpRemark
     * @return
     */
    @AutoLog(value = "店铺-通过id去修改自提备注")
    @ApiOperation(value = "店铺-通过id去修改自提备注", notes = "店铺-通过id去修改自提备注")
    @RequestMapping(value = "/updatePickUpRemarkById", method = RequestMethod.GET)
    public Result<StoreManage> updatePickUpRemarkById(@RequestParam(name = "id", required = true) String id,
                                                      @RequestParam(name = "pickUpRemark", required = true) String pickUpRemark) {
        Result<StoreManage> result = new Result<StoreManage>();
        StoreManage storeManageById = storeManageService.getById(id);
        if (storeManageById == null) {
            result.error500("未找到对应实体");
        } else {
            storeManageById.setPickUpRemark(pickUpRemark);
        }
        boolean ok = storeManageService.updateById(storeManageById);
        if (ok) {
            result.success("修改成功");
        } else {
            result.error500("修改失败");
        }
        return result;
    }

    /**
     * 通过id修改配送计费规则
     *
     * @param jsonObject
     * @return
     */
    @AutoLog(value = "店铺-通过id修改配送计费规则")
    @ApiOperation(value = "店铺-通过id修改配送计费规则", notes = "店铺-通过id修改配送计费规则")
    @RequestMapping(value = "/updateAccountingRulesById", method = RequestMethod.POST)
    public Result<StoreManage> updateAccountingRulesById(@RequestBody JSONObject jsonObject) {
        Result<StoreManage> result = new Result<StoreManage>();
        UpdateWrapper<StoreManage> storeManageUpdateWrapper = new UpdateWrapper<>();
        storeManageUpdateWrapper.eq("id", jsonObject.getString("id"));
        StoreManage storeManage = new StoreManage();
        storeManage.setAccountingRules(jsonObject.getString("accountingRules"));
        boolean b = storeManageService.update(storeManage, storeManageUpdateWrapper);
        if (b) {
            result.success("修改成功");
        } else {
            result.error500("修改失败");
        }
        return result;
    }

    /**
     * 通过id修改审核认证状态
     *
     * @return
     */
    @AutoLog(value = "店铺-通过id修改审核认证状态")
    @ApiOperation(value = "店铺-通过id修改审核认证状态", notes = "店铺-通过id修改审核认证状态")
    @RequestMapping(value = "/updateAttestationStatusById", method = RequestMethod.POST)
    public Result<StoreManage> updateAttestationStatusById(@RequestBody JSONObject jsonObject) {
        Result<StoreManage> result = new Result<StoreManage>();
        try {
            String id = jsonObject.getString("id");
            String attestationStatus = jsonObject.getString("attestationStatus");
            String remark = jsonObject.getString("remark");
            if (oConvertUtils.isNotEmpty(id)) {
                StoreManage storeManage = storeManageService.getById(id);
                storeManage.setAttestationStatus(attestationStatus);
                storeManage.setRemark(remark);
                storeManageService.updateById(storeManage);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败" + e.getMessage());
        }
        result.success("操作成功");
        return result;
    }

    /**
     * 返回当前登录人福利金
     *
     * @return
     */
    @AutoLog(value = "店铺-返回当前登录人福利金")
    @ApiOperation(value = "店铺-返回当前登录人福利金", notes = "店铺-返回当前登录人福利金")
    @RequestMapping(value = "findBalanceByWelfarePayments", method = RequestMethod.GET)
    public Result<StoreManageDTO> findBalanceByWelfarePayments() {
        Result<StoreManageDTO> result = new Result<>();
        StoreManageDTO storeManageDTO = new StoreManageDTO();
        //获取当前登录人
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String userId = sysUser.getId();
        //获取当前登录人的店铺
        QueryWrapper<StoreManage> storeManageQueryWrapper = new QueryWrapper<>();
        storeManageQueryWrapper.eq("sys_user_id", userId);
        StoreManage one = storeManageService.getOne(storeManageQueryWrapper);

        //获取福利金设置比例
        QueryWrapper<MarketingWelfarePaymentsSetting> marketingWelfarePaymentsSettingQueryWrapper = new QueryWrapper<>();
        marketingWelfarePaymentsSettingQueryWrapper.eq("del_flag", "0");
        marketingWelfarePaymentsSettingQueryWrapper.eq("status", "1");
        MarketingWelfarePaymentsSetting paymentsSettingServiceOne = iMarketingWelfarePaymentsSettingService.getOne(marketingWelfarePaymentsSettingQueryWrapper);
        //复制店铺信息
        BeanUtils.copyProperties(one, storeManageDTO);
        //返回福利金比例
        storeManageDTO.setProportion(paymentsSettingServiceOne.getProportion());
        result.setResult(storeManageDTO);
        return result;
    }

    @AutoLog(value = "店铺-返回店铺信息")
    @ApiOperation(value = "店铺-返回店铺信息", notes = "店铺-返回店铺信息")
    @RequestMapping(value = "findStore", method = RequestMethod.GET)
    public Result<StoreManageVO> findStore() {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String userId = sysUser.getId();
        Result<StoreManageVO> result = new Result<>();
        QueryWrapper<StoreManage> storeManageQueryWrapper = new QueryWrapper<>();
        storeManageQueryWrapper.eq("sys_user_id", userId);
        storeManageQueryWrapper.eq("attestation_status", "1");
        StoreManage one = storeManageService.getOne(storeManageQueryWrapper);
        if (one!=null){
            if (StringUtils.isBlank(one.getId())) {
                return result.error500("待审核通过!");
            }
            String sysSmallcodeId = one.getSysSmallcodeId();
            if (StringUtils.isBlank(sysSmallcodeId)) {
                SysSmallcode sysSmallcode = new SysSmallcode();
                sysSmallcode.setDelFlag("0");
                sysSmallcode.setSysUserId(one.getSysUserId());
                sysSmallcode.setCodeType("0");
                iSysSmallcodeService.save(sysSmallcode);
                one.setSysSmallcodeId(sysSmallcode.getId());
                sysSmallcode.setAddress(weixinQRUtils.getQrCode(sysSmallcode.getId()));
                iSysSmallcodeService.saveOrUpdate(sysSmallcode);
                storeManageService.saveOrUpdate(one);
                StoreManageVO store = storeManageService.findStore(userId);
                result.setSuccess(true);
                result.setResult(store);
            } else {
                StoreManageVO store = storeManageService.findStore(userId);
                result.setSuccess(true);
                result.setResult(store);
            }

            return result;
        }else {
            return result.error500("未找到店铺信息");
        }

    }

    @AutoLog(value = "店铺-修改店铺信息")
    @ApiOperation(value = "店铺-修改店铺信息", notes = "店铺-修改店铺信息")
    @RequestMapping(value = "updateStore", method = RequestMethod.PUT)
    public Result<StoreManage> updateStore(@RequestBody StoreManage storeManage) {
        Result<StoreManage> result = new Result<>();
        boolean b = storeManageService.updateById(storeManage);
        if (b) {
            result.success("修改成功");
        } else {
            result.error500("修改失败");
        }
        return result;
    }

    @AutoLog(value = "店铺-模糊查询根据手机号")
    @ApiOperation(value = "店铺-模糊查询根据手机号", notes = "店铺-模糊查询根据手机号")
    @RequestMapping(value = "/getStoreByBossPhone", method = RequestMethod.GET)
    public List<Map<String,Object>> getStoreByBossPhone(@RequestParam(name = "bossPhone", required = true) String bossPhone) {
        return storeManageService.getStoreByBossPhone(bossPhone);
    }

    @AutoLog(value = "店铺-推荐分类接口")
    @ApiOperation(value = "店铺-推荐分类接口", notes = "店铺-推荐分类接口")
    @RequestMapping(value = "findStoreList", method = RequestMethod.GET)
    public Result<List<StoreManage>> findStoreList() {
        Result<List<StoreManage>> result = new Result<>();
        QueryWrapper<StoreManage> storeManageQueryWrapper = new QueryWrapper<>();
        storeManageQueryWrapper.eq("del_flag", "0");
        storeManageQueryWrapper.eq("status", "1");
        List<StoreManage> list = storeManageService.list(storeManageQueryWrapper);
        result.setSuccess(true);
        result.setResult(list);
        return result;
    }

    /**
     * 通过坐标获取经纬度
     *
     * @param address
     * @param pageSize
     * @param pageIndex
     * @return
     */
    @RequestMapping("getLngAndLat")
    public Result<Object> getLngAndLat(String address,
                                       @RequestParam(defaultValue = "20", value = "pageSize") Integer pageSize,
                                       @RequestParam(defaultValue = "1", value = "pageIndex") Integer pageIndex) {
        return afterMapController.getAddsress(address, pageSize, pageIndex);
    }

    @AutoLog(value = "店铺余额列表")
    @ApiOperation(value = "店铺余额列表", notes = "店铺余额列表")
    @RequestMapping(value = "findStoreBalance", method = RequestMethod.GET)
    public Result<IPage<StoreManageDTO>> findStoreBalance(StoreManageVO storeManageVO,
                                                          @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                          HttpServletRequest req) {
        Result<IPage<StoreManageDTO>> result = new Result<>();
        Page<StoreManageDTO> page = new Page<>(pageNo, pageSize);
        IPage<StoreManageDTO> storeBalance = storeManageService.findStoreBalance(page, storeManageVO);
        result.setCode(200);
        result.setResult(storeBalance);
        return result;
    }

    @AutoLog(value = "店铺-推荐分类接口")
    @ApiOperation(value = "店铺-推荐分类接口", notes = "店铺-推荐分类接口")
    @RequestMapping(value = "storeListAndName", method = RequestMethod.GET)
    public Result<List<StoreManage>> storeListAndName(String storeName,String sysUserId) {
        Result<List<StoreManage>> result = new Result<>();
        QueryWrapper<StoreManage> storeManageQueryWrapper = new QueryWrapper<>();
        storeManageQueryWrapper.eq("del_flag", "0");
        storeManageQueryWrapper.eq("status", "1");
        if(StringUtils.isNotBlank(storeName)){
            storeManageQueryWrapper.like("store_name", storeName);
        }
        if(StringUtils.isNotBlank(sysUserId) ){
            storeManageQueryWrapper.eq("sys_user_id", sysUserId);
        }
        storeManageQueryWrapper.last("limit 30");
        List<StoreManage> list = storeManageService.list(storeManageQueryWrapper);
        result.setSuccess(true);
        result.setResult(list);
        return result;
    }
    @AutoLog(value = "店铺-推广人返显")
    @ApiOperation(value = "店铺-推广人返显", notes = "店铺-推广人返显")
    @RequestMapping(value = "findStorePromoter", method = RequestMethod.GET)
    public Result<Map<String,Object>> findStorePromoter(@RequestParam(name = "id",required = true) String id){
        Result<Map<String, Object>> result = new Result<>();
        Map<String, Object> storePromoter = storeManageService.findStorePromoter(id);
        result.setResult(storePromoter);
        result.success("推广人返显成功");
        return result;
    }
    @AutoLog(value = "店铺-根据推广人类型和推广人id返回信息")
    @ApiOperation(value = "店铺-根据推广人类型和推广人id返回信息", notes = "店铺-根据推广人类型和推广人id返回信息")
    @RequestMapping(value = "findInfoByidAndType", method = RequestMethod.GET)
    public Result<Map<String,Object>> findInfoByidAndType(@RequestParam(name = "promoter",required = true)String promoter,
                                                          @RequestParam(name = "promoterType",required = true)String promoterType){
        Result<Map<String, Object>> result = new Result<>();
        Map<String, Object> infoByidAndType = storeManageService.findInfoByidAndType(promoter, promoterType);
        result.setResult(infoByidAndType);
        result.success("根据推广人类型和推广人id返回信息");
        return result;
    }
    @AutoLog(value = "店铺-修改店铺推广人")
    @ApiOperation(value = "店铺-修改店铺推广人", notes = "店铺-修改店铺推广人")
    @RequestMapping(value = "updateStorePromoter", method = RequestMethod.POST)
    public Result<String> updateStorePromoter(@RequestBody StoreManageDTO storeManageDTO){
        Result<String> result = new Result<>();
        String s = storeManageService.updateStorePromoter(storeManageDTO);
        result.setResult(s);
        return result;
    }
    @AutoLog(value = "店铺-加盟商店铺")
    @ApiOperation(value = "店铺-加盟商店铺", notes = "店铺-加盟商店铺")
    @RequestMapping(value = "findAllianceStoreList", method = RequestMethod.GET)
    public Result<IPage<StoreManageVO>> findAllianceStoreList(StoreManageDTO storeManageDTO,
                                                       @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                       @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize){
        Result<IPage<StoreManageVO>> result = new Result<>();
        Page<StoreManageVO> page = new Page<StoreManageVO>(pageNo, pageSize);
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        List<String> roleByUserId = iSysUserRoleService.getRoleByUserId(sysUser.getId());
        if (roleByUserId.contains("Franchisee")){
            storeManageDTO.setPromoter(sysUser.getId());
        }
        IPage<StoreManageVO> allianceStoreList = storeManageService.findAllianceStoreList(page, storeManageDTO);
        result.setResult(allianceStoreList);
        result.success("返回加盟商店铺");
        return result;
    }
    @AutoLog(value = "通过id获取加盟商信息")
    @ApiOperation(value = "通过id获取加盟商信息", notes = "通过id获取加盟商信息")
    @RequestMapping(value = "getAllianceInfo", method = RequestMethod.GET)
    public Result<Map<String,Object>> getAllianceInfo(@RequestParam(name = "allianceId",required = true)String allianceId){
        Result<Map<String, Object>> result = new Result<>();
        AllianceManage allianceManage = iAllianceManageService.getById(allianceId);
        HashMap<String, Object> map = new HashMap<>();
        if (oConvertUtils.isNotEmpty(allianceManage)){
            SysUser sysUser = iSysUserService.getById(allianceManage.getSysUserId());
            map.put("allianceInfo",sysUser.getRealname()+"("+allianceManage.getName()+")");
            result.setResult(map);
            result.success("返回加盟信息成功");
            return result;
        }else {
            map.put("allianceInfo","查无此人");
            result.setResult(map);
            result.setCode(200);
            return result;
        }
    }
    @AutoLog(value = "修改店铺归属加盟商")
    @ApiOperation(value = "修改店铺归属加盟商", notes = "修改店铺归属加盟商")
    @RequestMapping(value = "updateStoreAlliance", method = RequestMethod.POST)
    public Result<StoreManage> updateStoreAlliance(@RequestBody StoreManageDTO storeManageDTO){
        if (storeManageDTO.getIfAlliance().equals("1")){
            AllianceManage allianceManage = iAllianceManageService.getById(storeManageDTO.getAllianceId());
            return edit(new StoreManage()
                    .setId(storeManageDTO.getId())
                    .setAllianceUserId(allianceManage.getSysUserId())
                    .setRemark(storeManageDTO.getRemark()));
        }else {
            return edit(new StoreManage()
                    .setId(storeManageDTO.getId())
                    .setAllianceUserId("")
                    .setRemark(storeManageDTO.getRemark()));
        }

    }
    @GetMapping("findStoreInfoByStoreName")
    public Result<List<Map<String,Object>>>findStoreInfoByStoreName(@RequestParam("storeName")String storeName){
        Result<List<Map<String, Object>>> result = new Result<>();
        List<Map<String, Object>> s = storeManageService.findStoreInfoByStoreName(storeName);
        result.success("返回店铺列表信息");
        result.setResult(s);
        return result;
    }
    @GetMapping("findStoreByPhone")
    public Result<List<Map<String,Object>>> findStoreByPhone(@RequestParam("phone")String phone){
        Result<List<Map<String, Object>>> result = new Result<>();
        result.setResult(storeManageService.getStoreByBossPhone(phone));
        return result;
    }

    /**
     * 获取楼层列表
     *
     * @return
     */
    @RequestMapping("getFloorName")
    @ResponseBody
    public Result<?>  getFloorName(){
        String floorName= iSysDictService.queryTableDictTextByKey("sys_dict_item","item_value","item_text","floor_name");
        return Result.ok(Arrays.asList(StringUtils.split(floorName,",")));
    }


}
