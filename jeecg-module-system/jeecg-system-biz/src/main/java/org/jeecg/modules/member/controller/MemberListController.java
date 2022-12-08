package org.jeecg.modules.member.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
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
import org.jeecg.config.util.PermissionUtils;
import org.jeecg.modules.marketing.dto.MarketingDisountGoodDTO;
import org.jeecg.modules.marketing.service.IMarketingDiscountGoodService;
import org.jeecg.modules.marketing.vo.MarketingDiscountCouponVO;
import org.jeecg.modules.member.dto.MemberListDTO;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.service.IMemberListService;
import org.jeecg.modules.member.vo.MemberCertificateVO;
import org.jeecg.modules.member.vo.MemberDiscountVO;
import org.jeecg.modules.member.vo.MemberListVO;
import org.jeecg.modules.store.entity.StoreManage;
import org.jeecg.modules.store.service.IStoreManageService;
import org.jeecg.modules.store.vo.StoreManageVO;
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
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 会员列表
 * @Author: jeecg-boot
 * @Date: 2019-10-24
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "会员列表")
@RestController
@RequestMapping("/memberList/memberList")
public class MemberListController {
    @Autowired
    private IMemberListService memberListService;
    @Autowired
    private IMarketingDiscountGoodService iMarketingDiscountGoodService;
    @Autowired
    private IStoreManageService iStoreManageService;
    @Autowired
    private ISysUserRoleService iSysUserRoleService;



    /**
     * 系统扣减
     *
     * @param memberId
     * @param balance
     * @return
     */
    @RequestMapping("systemDeduction")
    @ResponseBody
    public Result<?> systemDeduction(String memberId,BigDecimal balance){
        memberListService.subtractBlance(memberId,balance,memberId,"25");
        return Result.ok("系统扣减成功");
    }

    /**
     * 系统补发
     *
     * @param memberId
     * @param balance
     * @return
     */
    @RequestMapping("systemTopUp")
    @ResponseBody
    public Result<?> systemTopUp(String memberId,BigDecimal balance){
        memberListService.addBlance(memberId,balance,memberId,"24");
        return Result.ok("系统补发成功");
    }

    /**
     * 分页列表查询
     *
     * @param memberList
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "会员列表-分页列表查询")
    @ApiOperation(value = "会员列表-分页列表查询", notes = "会员列表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<MemberList>> queryPageList(MemberList memberList,
                                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                   HttpServletRequest req) {
        Result<IPage<MemberList>> result = new Result<IPage<MemberList>>();
        QueryWrapper<MemberList> queryWrapper = QueryGenerator.initQueryWrapper(memberList, req.getParameterMap());
        PermissionUtils.accredit(queryWrapper);
        Page<MemberList> page = new Page<MemberList>(pageNo, pageSize);
        IPage<MemberList> pageList = memberListService.page(page, queryWrapper);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 添加
     *
     * @param memberList
     * @return
     */
    @AutoLog(value = "会员列表-添加")
    @ApiOperation(value = "会员列表-添加", notes = "会员列表-添加")
    @PostMapping(value = "/add")
    public Result<MemberList> add(@RequestBody MemberList memberList) {
        Result<MemberList> result = new Result<MemberList>();
        try {
            memberListService.save(memberList);
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
     * @param memberList
     * @return
     */
    @AutoLog(value = "会员列表-编辑")
    @ApiOperation(value = "会员列表-编辑", notes = "会员列表-编辑")
    @PutMapping(value = "/edit")
    public Result<MemberList> edit(@RequestBody MemberList memberList) {
        Result<MemberList> result = new Result<MemberList>();
        MemberList memberListEntity = memberListService.getById(memberList.getId());
        if (memberListEntity == null) {
            result.error500("未找到对应实体");
        } else {
            boolean ok = memberListService.updateById(memberList);
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
    @AutoLog(value = "会员列表-通过id删除")
    @ApiOperation(value = "会员列表-通过id删除", notes = "会员列表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        try {
            memberListService.removeById(id);
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
    @AutoLog(value = "会员列表-批量删除")
    @ApiOperation(value = "会员列表-批量删除", notes = "会员列表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<MemberList> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        Result<MemberList> result = new Result<MemberList>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别！");
        } else {
            this.memberListService.removeByIds(Arrays.asList(ids.split(",")));
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
    @AutoLog(value = "会员列表-通过id查询")
    @ApiOperation(value = "会员列表-通过id查询", notes = "会员列表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<MemberList> queryById(@RequestParam(name = "id", required = true) String id) {
        Result<MemberList> result = new Result<MemberList>();
        MemberList memberList = memberListService.getById(id);
        if (memberList == null) {
            result.error500("未找到对应实体");
        } else {
            result.setResult(memberList);
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
        QueryWrapper<MemberList> queryWrapper = null;
        try {
            String paramsStr = request.getParameter("paramsStr");
            if (oConvertUtils.isNotEmpty(paramsStr)) {
                String deString = URLDecoder.decode(paramsStr, "UTF-8");
                MemberList memberList = JSON.parseObject(deString, MemberList.class);
                queryWrapper = QueryGenerator.initQueryWrapper(memberList, request.getParameterMap());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //Step.2 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        List<MemberList> pageList = memberListService.list(queryWrapper);
        //导出文件名称
        mv.addObject(NormalExcelConstants.FILE_NAME, "会员列表列表");
        mv.addObject(NormalExcelConstants.CLASS, MemberList.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("会员列表列表数据", "导出人:Jeecg", "导出信息"));
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
                List<MemberList> listMemberLists = ExcelImportUtil.importExcel(file.getInputStream(), MemberList.class, params);
                memberListService.saveBatch(listMemberLists);
                return Result.ok("文件导入成功！数据行数:" + listMemberLists.size());
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
     * 通过id修改店铺列表到店自提状态
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/updateStatusById", method = RequestMethod.GET)
    public Result<StoreManage> updateStatusById(@RequestParam(name = "id", required = true) String id,
                                                @RequestParam(name = "stopRemark", required = true) String stopRemark) {
        Result<StoreManage> result = new Result<StoreManage>();
        MemberList serviceById = memberListService.getById(id);
        if (serviceById == null) {
            result.error500("未找到对应实体");
        } else {
            if ("1".equals(serviceById.getStatus())) {
                serviceById.setStatus("0");
                serviceById.setStopRemark(stopRemark);
            } else {
                serviceById.setStatus("1");
                serviceById.setStopRemark(" ");
            }
        }
        boolean ok = memberListService.updateById(serviceById);
        if (ok) {
            result.success("修改成功");
        } else {
            result.error500("修改失败");
        }
        return result;
    }

    /**
     * 模糊查询根据手机号
     *
     * @param phone
     * @return
     */
    @AutoLog(value = "会员列表-模糊查询根据手机号")
    @ApiOperation(value = "会员列表-模糊查询根据手机号", notes = "会员列表-模糊查询根据手机号")
    @RequestMapping(value = "/likeMemberByPhone", method = RequestMethod.GET)
    public List<Map<String,Object>> likeMemberByPhone(@RequestParam("phone") String phone) {
        /*Result<List<Map<String, Object>>> result = new Result<>();*/
        List<Map<String, Object>> maps = memberListService.likeMemberByPhone(phone);
        /*result.setResult(maps);
        result.success("返回会员信息");*/
        return maps;
    }


    @AutoLog(value = "会员列表-列表展示")
    @ApiOperation(value = "会员列表-列表展示", notes = "会员列表-列表展示")
    @RequestMapping(value = "findMemberList", method = RequestMethod.GET)
    public Result<IPage<MemberListVO>> findMemberList(MemberListVO memberListVO,
                                                      @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                                      @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        Result<IPage<MemberListVO>> result = new Result<>();
        Page<MemberListVO> page = new Page<>(pageNo, pageSize);
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String userId = sysUser.getId();
        List<String> roleByUserId = iSysUserRoleService.getRoleByUserId(userId);
        if (roleByUserId.contains("Merchant")) {
            memberListVO.setSysUserId(userId);
        }
        IPage<MemberListVO> memberList = memberListService.findMemberList(page, memberListVO);
        result.setSuccess(true);
        result.setResult(memberList);
        return result;
    }

    @AutoLog(value = "代理会员列表")
    @ApiOperation(value = "代理会员列表", notes = "代理会员列表")
    @RequestMapping(value = "findAgencyMemberList", method = RequestMethod.GET)
    public Result<IPage<MemberListVO>> findAgencyMemberList(MemberListVO memberListVO,
                                                            @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                                            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        Result<IPage<MemberListVO>> result = new Result<>();
        Page<MemberListVO> page = new Page<>(pageNo, pageSize);
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String userId = sysUser.getId();
        List<String> roleByUserId = iSysUserRoleService.getRoleByUserId(userId);
        roleByUserId.forEach(role -> {
            if (role.equals("Provincial_agents")) {
                memberListVO.setProvincialAreaId(userId);
            }
            if (role.equals("Municipal_agent")) {
                memberListVO.setMunicipalAreaId(userId);
            }
            if (role.equals("County_agent")) {
                memberListVO.setCountyAreaId(userId);
            }
            if (role.equals("Franchisee")) {
                memberListVO.setStorePromoter(userId);
            }
        });
        IPage<MemberListVO> memberList = null;
        try {
            memberList = memberListService.findAgencyMemberList(page, memberListVO);
        } catch (Exception e) {
            e.printStackTrace();
        }
        result.setSuccess(true);
        result.setResult(memberList);
        return result;
    }

    @AutoLog(value = "会员优惠券列表")
    @ApiOperation(value = "会员优惠券列表", notes = "会员优惠券列表")
    @GetMapping(value = "/findMemberDiscount")
    public Result<IPage<MarketingDiscountCouponVO>> findMemberDiscount(MemberDiscountVO memberDiscountVO,
                                                                       @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                                                       @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                                                       HttpServletRequest request) {
        Result<IPage<MarketingDiscountCouponVO>> result = new Result<>();
        memberDiscountVO.setPageNo((pageNo-1)*pageSize);
        memberDiscountVO.setPageSize(pageSize);
        Page<MarketingDiscountCouponVO> page = new Page<>(pageNo, pageSize);
        IPage<MarketingDiscountCouponVO> memberDiscount = memberListService.findMemberDiscount(page,memberDiscountVO);
        result.setResult(memberDiscount);
        result.setCode(200);
        return result;
    }

    @AutoLog(value = "会员兑换券")
    @ApiOperation(value = "会员兑换券", notes = "会员兑换券")
    @RequestMapping(value = "findMemberCertificate", method = RequestMethod.GET)
    public Result<IPage<MemberCertificateVO>> findMemberCertificate(MemberCertificateVO memberCertificateVO,
                                                                    @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                                                    @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        Result<IPage<MemberCertificateVO>> result = new Result<>();
        Page<MemberCertificateVO> page = new Page<>(pageNo, pageSize);
        IPage<MemberCertificateVO> memberCertificate = memberListService.findMemberCertificate(page, memberCertificateVO);
        result.setSuccess(true);
        result.setResult(memberCertificate);
        return result;
    }

    @AutoLog(value = "会员优惠券适用商品")
    @ApiOperation(value = "会员优惠券适用商品", notes = "会员优惠券适用商品")
    @RequestMapping(value = "getMemberDiscountGood", method = RequestMethod.GET)
    public Result<Map<String, Object>> getMemberDiscountGood(@RequestParam(value = "marketingDiscountId", required = true) String marketingDiscountId,
                                                             @RequestParam(value = "isPlatform", required = true) String isPlatform) {
        Result<Map<String, Object>> result = new Result<>();
        if ("0".equals(isPlatform)) {
            List<MarketingDisountGoodDTO> storeGood = iMarketingDiscountGoodService.findStoreGood(marketingDiscountId);
            HashMap<String, Object> map = new HashMap<>();
            map.put("records", storeGood);
            result.setSuccess(true);
            result.setResult(map);
        } else {
            List<MarketingDisountGoodDTO> good = iMarketingDiscountGoodService.findGood(marketingDiscountId);
            HashMap<String, Object> map = new HashMap<>();
            map.put("records", good);
            result.setSuccess(true);
            result.setResult(map);
        }
        return result;
    }

    @AutoLog(value = "返回推广人字段")
    @ApiOperation(value = "返回推广人字段", notes = "返回推广人字段")
    @RequestMapping(value = "returnPromoter", method = RequestMethod.GET)
    public Result<Map<String, Object>> returnPromoter(@RequestParam(value = "id", required = true) String id) {
        Result<Map<String, Object>> result = new Result<>();
        if (StringUtils.isNotBlank(id)) {
            Map<String, Object> map = memberListService.returnPromoter(id);
            result.setResult(map);
            result.success("返回推广人成功");
        } else {
            result.error500("会员状态异常,请联系管理员");
        }
        return result;
    }

    @AutoLog(value = "根据id返回会员信息")
    @ApiOperation(value = "根据id返回会员信息", notes = "根据id返回会员信息")
    @RequestMapping(value = "returnMemberNameById", method = RequestMethod.GET)
    public Result<Map<String, Object>> returnMemberNameById(@RequestParam(value = "promoter", required = true) String promoter,
                                                            @RequestParam(value = "promoterType", required = true) String promoterType) {
        Result<Map<String, Object>> result = new Result<>();
        Map<String, Object> stringObjectMap = memberListService.returnMemberNameById(promoter, promoterType);
        result.setResult(stringObjectMap);
        result.success("成功");
        return result;
    }

    @AutoLog(value = "修改会员推广人")
    @ApiOperation(value = "修改会员推广人", notes = "修改会员推广人")
    @RequestMapping(value = "updatePromoter", method = RequestMethod.POST)
    public Result<MemberList> updatePromoter(@RequestBody MemberList memberList) {
        if (memberList.getPromoterType().equals("0")) {
            StoreManage storeManage = iStoreManageService.getById(memberList.getPromoter());
            memberList.setPromoter(storeManage.getSysUserId());
        }
        return edit(memberList);
    }

    @AutoLog(value = "修改归属店铺")
    @ApiOperation(value = "修改归属店铺", notes = "修改归属店铺")
    @RequestMapping(value = "updateSysUserId", method = RequestMethod.POST)
    public Result<MemberList> updateSysUserId(@RequestBody MemberListDTO memberListDTO) {

        if (memberListDTO.getIsHaveStore().equals("1")) {
            StoreManage storeManage = iStoreManageService.getById(memberListDTO.getSysUserId());
            return edit(new MemberList()
                    .setId(memberListDTO.getId())
                    .setSysUserId(storeManage.getSysUserId())
                    .setRemark(memberListDTO.getRemark())
            );
        } else {
            return edit(new MemberList()
                    .setId(memberListDTO.getId())
                    .setSysUserId("")
                    .setRemark("")
            );
        }

    }

    @AutoLog(value = "返回归属店铺字段")
    @ApiOperation(value = "返回归属店铺字段", notes = "返回归属店铺字段")
    @RequestMapping(value = "returnMemberStore", method = RequestMethod.GET)
    public Result<Map<String, Object>> returnMemberStore(@RequestParam(value = "id", required = true) String id) {
        Result<Map<String, Object>> result = new Result<>();
        HashMap<String, Object> map = new HashMap<>();
        MemberList member = memberListService.getById(id);
        if (StringUtils.isBlank(member.getSysUserId())) {
            map.put("storeName", "无");
        } else {
            StoreManage storeManage = iStoreManageService.getOne(new LambdaQueryWrapper<StoreManage>()
                    .eq(StoreManage::getSysUserId, member.getSysUserId()));

            if (oConvertUtils.isNotEmpty(storeManage)) {
                if (StringUtils.isNotBlank(storeManage.getSubStoreName())) {
                    map.put("storeName", storeManage.getStoreName() + "(" + storeManage.getSubStoreName() + ")");
                } else {
                    map.put("storeName", storeManage.getStoreName());
                }
            } else {
                map.put("storeName", "无");
            }
        }
        result.setResult(map);
        result.success("返回归属店铺字段");
        return result;
    }

    @AutoLog(value = "会员-加盟商会员")
    @ApiOperation(value = "会员-加盟商会员", notes = "会员-加盟商会员")
    @RequestMapping(value = "findAllianceStoreList", method = RequestMethod.GET)
    public Result<IPage<MemberListVO>> findAllianceMemberlist(MemberListDTO memberListDTO,
                                                              @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                              @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Result<IPage<MemberListVO>> result = new Result<>();
        Page<StoreManageVO> page = new Page<StoreManageVO>(pageNo, pageSize);
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        List<String> roleByUserId = iSysUserRoleService.getRoleByUserId(sysUser.getId());
        if (roleByUserId.contains("Franchisee")) {
            memberListDTO.setPromoter(sysUser.getId());
        }
        IPage<MemberListVO> allianceMemberlist = memberListService.findAllianceMemberlist(page, memberListDTO);
        result.setResult(allianceMemberlist);
        result.success("返回加盟商会员");
        return result;
    }

    @AutoLog(value = "会员-返回会员金额")
    @ApiOperation(value = "会员-返回会员金额", notes = "会员-返回会员金额")
    @RequestMapping(value = "queryPageListAndManage", method = RequestMethod.GET)
    public Result<Map<String, Object>> queryPageListAndManage() {
        Result<Map<String, Object>> result = new Result<>();
        HashMap<String, Object> map = new HashMap<>();
        List<MemberList> memberLists = memberListService.list(new LambdaUpdateWrapper<MemberList>()
                .eq(MemberList::getDelFlag, "0"));
        map.put("balance", memberLists.stream()
                .map(MemberList::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        map.put("accountFrozen", memberLists.stream()
                .map(MemberList::getAccountFrozen)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        map.put("unusableFrozen", memberLists.stream()
                .map(MemberList::getUnusableFrozen)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        result.setResult(map);
        result.success("返回会员金额");
        return result;
    }
    //团队管理(弃用)
    @GetMapping("memberDesignationPageList")
    public Result<IPage<MemberListVO>> memberDesignationPageList(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                                 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                                 MemberListDTO memberListDTO){
        Result<IPage<MemberListVO>> result = new Result<IPage<MemberListVO>>();
        Page<MemberListVO> page = new Page<MemberListVO>(pageNo, pageSize);
        IPage<MemberListVO> memberListVOIPage = memberListService.memberDesignationPageList(page, memberListDTO);
        result.setResult(memberListVOIPage);
        result.success("团队管理");
        return result;
    }
    //获取下级会员
    @GetMapping("getUnderlingList")
    public Result<List<MemberListVO>> getUnderlingList(@RequestParam(name = "id",required = true)String id){
        Result<List<MemberListVO>> result = new Result<>();
        List<MemberListVO> underlingList = memberListService.getUnderlingList(id);
        result.setResult(underlingList);
        return result;
    }

    @GetMapping("findMemberListByPhone")
    public Result<List<Map<String,Object>>> findMemberListByPhone(@RequestParam("phone")String phone){
        Result<List<Map<String, Object>>> result = new Result<>();
        result.setResult(memberListService.likeMemberByPhone(phone));
        return result;
    }
}
