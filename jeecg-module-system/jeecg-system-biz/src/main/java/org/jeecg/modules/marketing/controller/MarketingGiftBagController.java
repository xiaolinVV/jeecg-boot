package org.jeecg.modules.marketing.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
import org.jeecg.modules.marketing.dto.MarketingGiftBagCertificateDTO;
import org.jeecg.modules.marketing.dto.MarketingGiftBagDTO;
import org.jeecg.modules.marketing.dto.MarketingGiftBagDiscountDTO;
import org.jeecg.modules.marketing.entity.MarketingDistributionSetting;
import org.jeecg.modules.marketing.entity.MarketingGiftBag;
import org.jeecg.modules.marketing.service.*;
import org.jeecg.modules.marketing.vo.MarketingGiftBagStoreVO;
import org.jeecg.modules.marketing.vo.MarketingGiftBagVO;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.service.IMemberListService;
import org.jeecg.modules.store.dto.StoreManageDTO;
import org.jeecg.modules.system.service.ISysDictService;
import org.jeecg.modules.system.service.ISysSmallcodeService;
import org.jeecg.modules.weixin.api.AfterWeixinController;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 礼包管理
 * @Author: jeecg-boot
 * @Date: 2019-12-09
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "礼包管理")
@RestController
@RequestMapping("/marketingGiftBag/marketingGiftBag")
public class MarketingGiftBagController {
    @Autowired
    private IMarketingGiftBagService marketingGiftBagService;
    @Autowired
    private IMarketingGiftBagCertificateService iMarketingGiftBagCertificateService;
    @Autowired
    private IMarketingGiftBagDiscountService iMarketingGiftBagDiscountService;
    @Autowired
    private IMarketingGiftBagStoreService iMarketingGiftBagStoreService;
    @Autowired
    private IMarketingDistributionSettingService iMarketingDistributionSettingService;
    @Autowired
    private ISysDictService iSysDictService;

    @Autowired
    private IMarketingStoreGiftCardListService iMarketingStoreGiftCardListService;


    @Autowired
    private ISysSmallcodeService iSysSmallcodeService;

    @Autowired
    private AfterWeixinController afterWeixinController;

    @Autowired
    private IMemberListService iMemberListService;


    /**
     *
     *  获取礼包记录分享码
     *
     * @param marketingGiftBagId
     * @return
     */
    @RequestMapping("getShareAddress")
    @ResponseBody
    public Result<?> getShareAddress(String marketingGiftBagId,String phone){

        MemberList memberList=iMemberListService.getOne(new LambdaQueryWrapper<MemberList>()
                .eq(MemberList::getPhone,phone)
                .orderByDesc(MemberList::getCreateTime)
                .last("limit 1"));

        if(memberList==null){
            return Result.error("会员不存在");
        }

        String address=iSysSmallcodeService.getShareAddress(marketingGiftBagId,memberList.getId());
        if(StringUtils.isBlank(address)){
            Map<String,Object> paraMap= Maps.newHashMap();
            paraMap.put("state","1");
            paraMap.put("id",marketingGiftBagId);
            paraMap.put("giftBagType","0");
            Map<String,Object> memberInfoMap=Maps.newHashMap();
            memberInfoMap.put("TmemberName",memberList.getNickName());
            memberInfoMap.put("TmemberHeadPortrait",memberList.getHeadPortrait());
            memberInfoMap.put("sysUserId",memberList.getSysUserId());
            paraMap.put("tMemberId",memberList.getId());
            paraMap.put("tphone",memberList.getPhone());
//            paraMap.put("sysUserId",);
            paraMap.put("TmemberInfo",memberInfoMap);
            String param=JSON.toJSONString(paraMap);
            afterWeixinController.getQrCodeByPage("userActiFn/pages/vipMember/vipMember",param,memberList.getSysUserId(),memberList.getId());
        }
        return Result.ok(iSysSmallcodeService.getShareAddress(marketingGiftBagId,memberList.getId()));
    }


    /**
     * 礼包选中的礼品卡
     *
     * @param marketingGiftBagId
     * @return
     */
    @RequestMapping("getGiftCarList")
    @ResponseBody
    public Result<?> getGiftCarList(@RequestParam(name = "marketingGiftBagId",required = true)String marketingGiftBagId){
        return Result.ok(iMarketingStoreGiftCardListService.getGiftCarList(marketingGiftBagId));
    }



    /**
     * 分页列表查询
     *
     * @param marketingGiftBag
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "礼包管理-分页列表查询")
    @ApiOperation(value = "礼包管理-分页列表查询", notes = "礼包管理-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<MarketingGiftBag>> queryPageList(MarketingGiftBag marketingGiftBag,
                                                         @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                         @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                         HttpServletRequest req) {
        Result<IPage<MarketingGiftBag>> result = new Result<IPage<MarketingGiftBag>>();
        QueryWrapper<MarketingGiftBag> queryWrapper = QueryGenerator.initQueryWrapper(marketingGiftBag, req.getParameterMap());
        Page<MarketingGiftBag> page = new Page<MarketingGiftBag>(pageNo, pageSize);
        IPage<MarketingGiftBag> pageList = marketingGiftBagService.page(page, queryWrapper);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }
    @AutoLog(value = "礼包列表弹窗")
    @ApiOperation(value = "礼包列表弹窗", notes = "礼包列表弹窗")
    @GetMapping(value = "giftContent")
    public Result<Map<String, Object>> giftContent(@RequestParam(name = "marketingGiftBagId",required = true)String marketingGiftBagId) {
        Result<Map<String, Object>> result = new Result<>();
        Result<Map<String, Object>> certificateById = this.findCertificateById(marketingGiftBagId);
        Result<Map<String, Object>> discountById = this.findDiscountById(marketingGiftBagId);
        HashMap<String, Object> map = new HashMap<>();
        map.put("Certificates",certificateById);
        map.put("Discounts",discountById);
        result.setSuccess(true);
        result.setResult(map);
        return result;
    }

    @AutoLog(value = "礼包管理-分页列表查询")
    @ApiOperation(value = "礼包管理-分页列表查询", notes = "礼包管理-分页列表查询")
    @GetMapping(value = "/findMarketingGifiBagPageList")
    public Result<IPage<MarketingGiftBagVO>> findMarketingGifiBagPageList(MarketingGiftBagVO MarketingGiftBagVO,
                                                                          @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                                          HttpServletRequest req) {
        Result<IPage<MarketingGiftBagVO>> result = new Result<IPage<MarketingGiftBagVO>>();
        Page<MarketingGiftBagVO> page = new Page<MarketingGiftBagVO>(pageNo, pageSize);
        IPage<MarketingGiftBagVO> pageList = marketingGiftBagService.findMarketingGifiBagPageList(page, MarketingGiftBagVO);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 添加
     *
     * @param marketingGiftBagVO
     * @return
     */
    @AutoLog(value = "礼包管理-添加")
    @ApiOperation(value = "礼包管理-添加", notes = "礼包管理-添加")
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Result<MarketingGiftBag> add(@RequestBody MarketingGiftBagVO marketingGiftBagVO) {
        return marketingGiftBagService.add(marketingGiftBagVO);
    }

    /**
     * 编辑
     *
     * @param
     * @return
     */
    @AutoLog(value = "礼包管理-编辑")
    @ApiOperation(value = "礼包管理-编辑", notes = "礼包管理-编辑")
    @RequestMapping(value = "edit", method = RequestMethod.PUT)
    public Result<MarketingGiftBagDTO> edit(@RequestBody MarketingGiftBagVO marketingGiftBagVO) {
        return marketingGiftBagService.edit(marketingGiftBagVO);
    }

    /**
     * 通过id删除
     *
     * @param
     * @return
     */
    @AutoLog(value = "礼包管理-通过id删除and说明")
    @ApiOperation(value = "礼包管理-通过id删除and说明", notes = "礼包管理-通过id删除and说明")
    @RequestMapping(value = "/deleteAndDelExplain", method = RequestMethod.DELETE)
    public Result deleteAndDelExplain(@RequestParam(value = "id", required = true) String id,
                                      @RequestParam(value = "delExplain", required = true) String delExplain) {
        return Result.ok(marketingGiftBagService.deleteAndDelExplain(id, delExplain));
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "礼包管理-批量删除")
    @ApiOperation(value = "礼包管理-批量删除", notes = "礼包管理-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<MarketingGiftBag> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        Result<MarketingGiftBag> result = new Result<MarketingGiftBag>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别！");
        } else {
            this.marketingGiftBagService.removeByIds(Arrays.asList(ids.split(",")));
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
    @AutoLog(value = "礼包管理-通过id查询")
    @ApiOperation(value = "礼包管理-通过id查询", notes = "礼包管理-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<MarketingGiftBag> queryById(@RequestParam(name = "id", required = true) String id) {
        Result<MarketingGiftBag> result = new Result<MarketingGiftBag>();
        MarketingGiftBag marketingGiftBag = marketingGiftBagService.getById(id);
        if (marketingGiftBag == null) {
            result.error500("未找到对应实体");
        } else {
            result.setResult(marketingGiftBag);
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
        QueryWrapper<MarketingGiftBag> queryWrapper = null;
        try {
            String paramsStr = request.getParameter("paramsStr");
            if (oConvertUtils.isNotEmpty(paramsStr)) {
                String deString = URLDecoder.decode(paramsStr, "UTF-8");
                MarketingGiftBag marketingGiftBag = JSON.parseObject(deString, MarketingGiftBag.class);
                queryWrapper = QueryGenerator.initQueryWrapper(marketingGiftBag, request.getParameterMap());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //Step.2 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        List<MarketingGiftBag> pageList = marketingGiftBagService.list(queryWrapper);
        //导出文件名称
        mv.addObject(NormalExcelConstants.FILE_NAME, "礼包管理列表");
        mv.addObject(NormalExcelConstants.CLASS, MarketingGiftBag.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("礼包管理列表数据", "导出人:Jeecg", "导出信息"));
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
                List<MarketingGiftBag> listMarketingGiftBags = ExcelImportUtil.importExcel(file.getInputStream(), MarketingGiftBag.class, params);
                marketingGiftBagService.saveBatch(listMarketingGiftBags);
                return Result.ok("文件导入成功！数据行数:" + listMarketingGiftBags.size());
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

    @AutoLog(value = "礼包管理-返回编辑兑换券")
    @ApiOperation(value = "礼包管理-返回编辑兑换券", notes = "礼包管理-返回编辑兑换券")
    @RequestMapping(value = "findCertificateById", method = RequestMethod.GET)
    @ResponseBody
    public Result<Map<String, Object>> findCertificateById(@RequestParam(name = "marketingGiftBagId",required = true)String marketingGiftBagId) {
        Result<Map<String, Object>> result = new Result<>();
        List<MarketingGiftBagCertificateDTO> certificateById = iMarketingGiftBagCertificateService.findCertificateById(marketingGiftBagId);
        HashMap<String, Object> map = new HashMap<>();
        map.put("records", certificateById);
        result.setSuccess(true);
        result.setResult(map);
        return result;
    }

    @AutoLog(value = "礼包管理-返回编辑优惠券券")
    @ApiOperation(value = "礼包管理-返回编辑优惠券券", notes = "礼包管理-返回编辑优惠券券")
    @RequestMapping(value = "findDiscountById", method = RequestMethod.GET)
    @ResponseBody
    public Result<Map<String, Object>> findDiscountById(@RequestParam(name = "marketingGiftBagId",required = true)String marketingGiftBagId) {
        Result<Map<String, Object>> result = new Result<>();
        List<MarketingGiftBagDiscountDTO> discountById = iMarketingGiftBagDiscountService.findDiscountById(marketingGiftBagId);
        HashMap<String, Object> map = new HashMap<>();
        map.put("records", discountById);
        result.setSuccess(true);
        result.setResult(map);
        return result;
    }

    @AutoLog(value = "礼包管理-返回编辑店铺")
    @ApiOperation(value = "礼包管理-返回编辑店铺", notes = "礼包管理-返回编辑店铺")
    @RequestMapping(value = "findStoreById", method = RequestMethod.GET)
    @ResponseBody
    public Result<Map<String, Object>> findStoreById(MarketingGiftBagStoreVO marketingGiftBagStoreVO) {
        Result<Map<String, Object>> result = new Result<>();
        String marketingGiftBagId = marketingGiftBagStoreVO.getMarketingGiftBagId();
        List<StoreManageDTO> storeById = iMarketingGiftBagStoreService.findStoreById(marketingGiftBagId);
        HashMap<String, Object> map = new HashMap<>();
        map.put("records", storeById);
        result.setSuccess(true);
        result.setResult(map);
        return result;
    }

    @AutoLog(value = "礼包管理-启动停用")
    @ApiOperation(value = "礼包管理-启动停用", notes = "礼包管理-启动停用")
    @PostMapping("updateStatus")
    public Result<MarketingGiftBag> updateStatus(@RequestBody MarketingGiftBag marketingGiftBag) {
        Result<MarketingGiftBag> result = new Result<>();
        boolean b = marketingGiftBagService.updateById(marketingGiftBag);
        if (b) {
            result.setCode(200);
            result.setMessage("修改成功");
        } else {
            result.error500("修改失败");
        }
        return result;
    }

    /**
     * 复制礼包地址
     *
     * @param marketingGiftBagId
     * @return
     */
    @AutoLog(value = "礼包管理-复制礼包地址")
    @ApiOperation(value = "礼包管理-复制礼包地址", notes = "礼包管理-复制礼包地址")
    @GetMapping(value = "/getMarketingGiftBagUrl")
    public Result<String> getMarketingGiftBagUrl(String marketingGiftBagId) {
        Result<String> result = new Result<>();
        String url = "pages/vipMember/vipMember?state=1&id=" + marketingGiftBagId;
        result.setResult(url);
        result.setSuccess(true);
        return result;
    }
    @AutoLog(value = "前置礼包弹窗")
    @ApiOperation(value = "前置礼包弹窗", notes = "前置礼包弹窗")
    @GetMapping(value = "/isPrepositionList")
    public Result<IPage<Map<String,Object>>> isPrepositionList(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                               @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                               MarketingGiftBagDTO marketingGiftBagDTO){
        Result<IPage<Map<String, Object>>> result = new Result<>();
        Page<Map<String,Object>> page = new Page<>(pageNo, pageSize);
        result.setResult(marketingGiftBagService.isPrepositionList(page,marketingGiftBagDTO));
        return result;
    }

    /**
     * 判断是否展示分销特权
     * @return
     */
    @GetMapping("ifViewDistributionPrivileges")
    public Result<Map<String,Object>> ifViewDistributionPrivileges(){
        Result<Map<String, Object>> result = new Result<>();
        HashMap<String, Object> map = new HashMap<>();
        MarketingDistributionSetting marketingDistributionSetting = iMarketingDistributionSettingService.list(new LambdaQueryWrapper<MarketingDistributionSetting>()
                .eq(MarketingDistributionSetting::getDelFlag, "0")).get(0);
        String bindingTeamRelationshipCondition = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "binding_team_relationship_condition");
        map.put("isThreshold",marketingDistributionSetting.getIsThreshold());
        map.put("bindingTeamRelationshipCondition",bindingTeamRelationshipCondition);
        result.setResult(map);
        return result;
    }
}
