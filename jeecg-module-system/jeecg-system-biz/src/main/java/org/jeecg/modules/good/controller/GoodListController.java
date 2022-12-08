package org.jeecg.modules.good.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.aspect.annotation.PermissionData;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.config.util.PermissionUtils;
import org.jeecg.modules.good.dto.GoodDiscountDTO;
import org.jeecg.modules.good.dto.GoodListDto;
import org.jeecg.modules.good.dto.GoodTypeDto;
import org.jeecg.modules.good.entity.GoodList;
import org.jeecg.modules.good.entity.GoodSpecification;
import org.jeecg.modules.good.entity.GoodStoreList;
import org.jeecg.modules.good.entity.GoodStoreSpecification;
import org.jeecg.modules.good.service.*;
import org.jeecg.modules.good.vo.GoodListVo;
import org.jeecg.modules.marketing.entity.MarketingMaterialGood;
import org.jeecg.modules.marketing.entity.MarketingPrefecture;
import org.jeecg.modules.marketing.entity.MarketingPrefectureGood;
import org.jeecg.modules.marketing.service.IMarketingMaterialGoodService;
import org.jeecg.modules.marketing.service.IMarketingPrefectureGoodService;
import org.jeecg.modules.marketing.service.IMarketingPrefectureService;
import org.jeecg.modules.order.entity.OrderEvaluate;
import org.jeecg.modules.order.entity.OrderEvaluateStore;
import org.jeecg.modules.order.service.IOrderEvaluateService;
import org.jeecg.modules.order.service.IOrderEvaluateStoreService;
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
import java.math.RoundingMode;
import java.net.URLDecoder;
import java.util.*;

/**
 * @Description: 商品列表
 * @Author: jeecg-boot
 * @Date: 2019-10-18
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "商品列表")
@RestController
@RequestMapping("/goodList/goodList")
public class GoodListController {
    @Autowired
    private IGoodListService goodListService;
    @Autowired
    private IGoodTypeService iGoodTypeService;
    @Autowired
    private IMarketingPrefectureService marketingPrefectureService;
    @Autowired
    private IMarketingPrefectureGoodService marketingPrefectureGoodService;
    @Autowired
    private IGoodStoreListService goodStoreListService;
    @Autowired
    private IGoodStoreSpecificationService iGoodStoreSpecificationService;
    @Autowired
    private IOrderEvaluateStoreService iOrderEvaluateStoreService;
    @Autowired
    private IMarketingPrefectureService iMarketingPrefectureService;
    @Autowired
    private IMarketingPrefectureGoodService iMarketingPrefectureGoodService;
    @Autowired
    private IGoodSpecificationService iGoodSpecificationService;
    @Autowired
    private IOrderEvaluateService iOrderEvaluateService;
    @Autowired
    private IMarketingMaterialGoodService iMarketingMaterialGoodService;


    /**
     * 修改商品排序
     *
     * @param goodListId
     * @param sort
     * @return
     */
    @RequestMapping("updateSort")
    @ResponseBody
    public Result<?> updateSort(String goodListId,
                                @RequestParam(required = false,defaultValue = "0") BigDecimal sort){
        if(org.apache.commons.lang3.StringUtils.isBlank(goodListId)){
            return Result.error("商品id不能为空");
        }
        GoodList goodList= goodListService.getById(goodListId);
        goodList.setSort(sort);
        goodListService.saveOrUpdate(goodList);
        return Result.ok("修改排序成功！！！");
    }


    /**
     * 分页列表查询
     *
     * @param goodListVo
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "商品列表-分页列表查询")
    @ApiOperation(value = "商品列表-分页列表查询", notes = "商品列表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<GoodListDto>> queryPageList(GoodListVo goodListVo,
                                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                    HttpServletRequest req) {
        Result<IPage<GoodListDto>> result = new Result<IPage<GoodListDto>>();
        Page<GoodList> page = new Page<GoodList>(pageNo, pageSize);
        //判断当前用户是否是品台，是 STR=NULL 不是STR= UserId
        String str = PermissionUtils.ifPlatform();
        if (StringUtils.isNotBlank(str)) {
            goodListVo.setSysUserId(str);
        }
        IPage<GoodListDto> pageList = goodListService.getGoodListDto(page, goodListVo, "0");
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    @AutoLog(value = "商品列表-分页列表查询")
    @ApiOperation(value = "商品列表-分页列表查询", notes = "商品列表-分页列表查询")
    @GetMapping(value = "/PageList")
    public Result<IPage<GoodListDto>> PageList(GoodListVo goodListVo,
                                               @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                               @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                               HttpServletRequest req) {
        Result<IPage<GoodListDto>> result = new Result<IPage<GoodListDto>>();
        Page<GoodList> page = new Page<GoodList>(pageNo, pageSize);
        //判断当前用户是否是品台，是 STR=NULL 不是STR= UserId
        String str = PermissionUtils.ifPlatform();
        if (StringUtils.isNotBlank(str)) {
            goodListVo.setSysUserId(str);
        }
        IPage<GoodListDto> pageList = goodListService.getGoodListDto(page, goodListVo, "0");
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    @AutoLog(value = "商品列表-分页列表查询(给阿碧优惠券选择弹窗的接口)")
    @ApiOperation(value = "商品列表-分页列表查询(给阿碧优惠券选择弹窗的接口)", notes = "商品列表-分页列表查询(给阿碧优惠券选择弹窗的接口)")
    @GetMapping(value = "/findGoodList")
    public Result<IPage<GoodDiscountDTO>> findGoodList(GoodListVo goodListVo, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                       @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Result<IPage<GoodDiscountDTO>> result = new Result<>();
        Page<GoodListVo> page = new Page<>(pageNo, pageSize);
        IPage<GoodDiscountDTO> goodList = goodListService.findGoodList(page, goodListVo);
        result.setSuccess(true);
        result.setResult(goodList);
        return result;
    }


    /**
     * 分页待审核列表查询
     *
     * @param goodListVo
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "商品列表-分页列表查询")
    @ApiOperation(value = "商品列表-分页列表查询", notes = "商品列表-分页列表查询")
    @GetMapping(value = "/GoodAuditListList")
    @PermissionData(pageComponent = "good/GoodAuditListList")
    public Result<IPage<GoodListDto>> queryPageAuditList(GoodListVo goodListVo,
                                                         @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                         @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                         HttpServletRequest req) {
        Result<IPage<GoodListDto>> result = new Result<IPage<GoodListDto>>();
        Page<GoodList> page = new Page<GoodList>(pageNo, pageSize);

        goodListVo.setAuditStatus("1");
        //判断当前用户是否是品台，是 STR=NULL 不是STR= UserId
        String str = PermissionUtils.ifPlatform();
        if (str != null) {
            goodListVo.setSysUserId(str);
        }
        IPage<GoodListDto> pageList = goodListService.getGoodListDto(page, goodListVo, "0");
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 分页草稿箱列表查询
     *
     * @param goodListVo
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "商品列表-分页列表查询")
    @ApiOperation(value = "商品列表-分页列表查询", notes = "商品列表-分页列表查询")
    @RequestMapping(value = "/GoodDraftListList", method = RequestMethod.GET)
    @PermissionData(pageComponent = "good/GoodDraftListList")
    public Result<IPage<GoodListDto>> queryPageDraftList(GoodListVo goodListVo,
                                                         @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                         @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                         HttpServletRequest req) {
        Result<IPage<GoodListDto>> result = new Result<IPage<GoodListDto>>();
        Page<GoodList> page = new Page<GoodList>(pageNo, pageSize);
        goodListVo.setAuditStatus("0");
        //判断当前用户是否是品台，是 STR=NULL 不是STR= UserId
        String str = PermissionUtils.ifPlatform();
        if (StringUtils.isNotBlank(str)) {
            goodListVo.setSysUserId(str);
        }
        IPage<GoodListDto> pageList = goodListService.getGoodListDto(page, goodListVo, null);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 分页回收站列表查询
     *
     * @param goodListVo
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "商品列表-分页列表查询")
    @ApiOperation(value = "商品列表-分页列表查询", notes = "商品列表-分页列表查询")
    @GetMapping(value = "/GoodRecycleListList")
    @PermissionData(pageComponent = "good/GoodRecycleListList")
    public Result<IPage<GoodListDto>> queryPageRecycleList(GoodListVo goodListVo,
                                                           @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                           @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                           HttpServletRequest req) {
        Result<IPage<GoodListDto>> result = new Result<IPage<GoodListDto>>();
        Page<GoodList> page = new Page<GoodList>(pageNo, pageSize);
        goodListVo.setDelFlag("1");
        IPage<GoodListDto> pageList = goodListService.getGoodListDtoDelFlag(page, goodListVo);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /***********************供应商*****************************/
    /**
     * 供应商分页列表查询
     *
     * @param goodListVo
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "商品列表-分页列表查询")
    @ApiOperation(value = "商品列表-分页列表查询", notes = "商品列表-分页列表查询")
    @GetMapping(value = "/GoodProviderList")
    @PermissionData(pageComponent = "good/GoodProviderListList")
    public Result<IPage<GoodListDto>> queryPageProviderList(GoodListVo goodListVo,
                                                            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                            HttpServletRequest req) {
        Result<IPage<GoodListDto>> result = new Result<IPage<GoodListDto>>();
        Page<GoodList> page = new Page<GoodList>(pageNo, pageSize);
        //判断当前用户是否是品台，是 STR=NULL 不是STR= UserId
        String str = PermissionUtils.ifPlatform();
        if (StringUtils.isNotBlank(str)) {
            goodListVo.setSysUserId(str);
        }
        IPage<GoodListDto> pageList = goodListService.getGoodListDto(page, goodListVo, "0");
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 分页草稿箱列表查询
     *
     * @param goodListVo
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "商品列表-分页列表查询")
    @ApiOperation(value = "商品列表-分页列表查询", notes = "商品列表-分页列表查询")
    @RequestMapping(value = "/GoodProviderDraftListList", method = RequestMethod.GET)
    @PermissionData(pageComponent = "good/GoodProviderDraftListList")
    public Result<IPage<GoodListDto>> queryPageProviderDraftList(GoodListVo goodListVo,
                                                                 @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                                 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                                 HttpServletRequest req) {
        Result<IPage<GoodListDto>> result = new Result<IPage<GoodListDto>>();
        Page<GoodList> page = new Page<GoodList>(pageNo, pageSize);
        goodListVo.setAuditStatus("0");
        //判断当前用户是否是品台，是 STR=NULL 不是STR= UserId
        String str = PermissionUtils.ifPlatform();
        if (StringUtils.isNotBlank(str)) {
            goodListVo.setSysUserId(str);
        }
        IPage<GoodListDto> pageList = goodListService.getGoodListDto(page, goodListVo, null);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 分页回收站列表查询
     *
     * @param goodListVo
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "商品列表-分页列表查询")
    @ApiOperation(value = "商品列表-分页列表查询", notes = "商品列表-分页列表查询")
    @GetMapping(value = "/GoodProviderRecycleListList")
    @PermissionData(pageComponent = "good/GoodProviderRecycleListList")
    public Result<IPage<GoodListDto>> queryPageProviderRecycleList(GoodListVo goodListVo,
                                                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                                   HttpServletRequest req) {
        Result<IPage<GoodListDto>> result = new Result<IPage<GoodListDto>>();
        Page<GoodList> page = new Page<GoodList>(pageNo, pageSize);
        goodListVo.setDelFlag("1");
        String str = PermissionUtils.ifPlatform();
        if (StringUtils.isNotBlank(str)) {
            goodListVo.setSysUserId(str);
        }
        IPage<GoodListDto> pageList = goodListService.getGoodListDtoDelFlag(page, goodListVo);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 添加
     *
     * @param goodListVo
     * @return
     */
    @AutoLog(value = "商品列表-添加")
    @ApiOperation(value = "商品列表-添加", notes = "商品列表-添加")
    @PostMapping(value = "/add")
    public Result<GoodListVo> add(@RequestBody GoodListVo goodListVo) {// @RequestBody GoodListVo goodListVo
        Result<GoodListVo> result = new Result<GoodListVo>();
        try {
            goodListService.saveOrUpdate(goodListVo);
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
     * @param goodList
     * @return
     */
    @AutoLog(value = "商品列表-编辑")
    @ApiOperation(value = "商品列表-编辑", notes = "商品列表-编辑")
    @PutMapping(value = "/edit")
    public Result<GoodList> edit(@RequestBody GoodList goodList) {
        Result<GoodList> result = new Result<GoodList>();
        GoodList goodListEntity = goodListService.getById(goodList.getId());
        if (oConvertUtils.isEmpty(goodListEntity)) {
            result.error500("未找到对应实体");
        } else {
            boolean ok = goodListService.updateById(goodList);
            //TODO 返回false说明什么？
            if (ok) {
                result.success("修改成功!");
            }
        }

        return result;
    }

    /**
     * 批量修改多端显示状态
     *
     * @param ids   商品id多的用逗号隔开
     * @param pointsDisplay  分端显示；0：全部；1：小程序；2：app
     * @return
     */
    @RequestMapping("updatePointsDisplayByIds")
    @ResponseBody
    public Result<?> updatePointsDisplayByIds(String ids,String pointsDisplay){
        //参数判断
        if(StringUtils.isNotBlank(ids)&&StringUtils.isNotBlank(pointsDisplay)){
            Result.error("参数为空！！！");
        }
        List<GoodList> goodLists= Lists.newArrayList();
        Arrays.asList(StringUtils.split(ids,",")).forEach(gId->{
            goodLists.add(goodListService.getById(gId).setPointsDisplay(pointsDisplay));
        });
        goodListService.updateBatchById(goodLists);
        return Result.ok("修改状态成功");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "商品列表-通过id删除")
    @ApiOperation(value = "商品列表-通过id删除", notes = "商品列表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        try {
            goodListService.removeById(id);
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
    @AutoLog(value = "商品列表-批量删除")
    @ApiOperation(value = "商品列表-批量删除", notes = "商品  列表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<GoodList> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        Result<GoodList> result = new Result<GoodList>();
        if (StringUtils.isEmpty(ids)) {
            result.error500("参数不识别！");
        } else {
            this.goodListService.removeByIds(Arrays.asList(ids.split(",")));
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
    @AutoLog(value = "商品列表-通过id查询")
    @ApiOperation(value = "商品列表-通过id查询", notes = "商品列表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<Map<String, Object>> queryById(@RequestParam(name = "id", required = true) String id) {
        Result<Map<String, Object>> result = new <Map<String, Object>>Result();
        Map<String, Object> map = new HashMap<String, Object>();
        GoodListDto goodList = goodListService.selectById(id);
        GoodTypeDto goodTypeDto = iGoodTypeService.getGoodTypeByGoodTyeId3(goodList.getGoodTypeId());
        map.put("goodList", goodList);
        map.put("goodTypeDto", goodTypeDto);
        if (oConvertUtils.isEmpty(goodList)) {
            result.error500("未找到对应实体");
        } else {
            result.setResult(map);
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
        QueryWrapper<GoodList> queryWrapper = null;
        try {
            String paramsStr = request.getParameter("paramsStr");
            if (oConvertUtils.isNotEmpty(paramsStr)) {
                String deString = URLDecoder.decode(paramsStr, "UTF-8");
                GoodList goodList = JSON.parseObject(deString, GoodList.class);
                queryWrapper = QueryGenerator.initQueryWrapper(goodList, request.getParameterMap());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //Step.2 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        List<GoodList> pageList = goodListService.list(queryWrapper);
        //导出文件名称
        mv.addObject(NormalExcelConstants.FILE_NAME, "商品列表列表");
        mv.addObject(NormalExcelConstants.CLASS, GoodList.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("商品列表列表数据", "导出人:Jeecg", "导出信息"));
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
                List<GoodList> listGoodLists = ExcelImportUtil.importExcel(file.getInputStream(), GoodList.class, params);
                goodListService.saveBatch(listGoodLists);
                return Result.ok("文件导入成功！数据行数:" + listGoodLists.size());
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
     * 通过id查询修改审核状态：
     *
     * @param id
     * @return
     */
    @AutoLog(value = "商品列表-通过id修改审核状态")
    @ApiOperation(value = "商品列表-通过id修改审核状态", notes = "商品列表-通过id修改审核状态")
    @GetMapping(value = "/updateAuditStatus")
    public Result<GoodList> updateAuditStatus(
            @RequestParam(name = "id", required = true) String id,
            @RequestParam(name = "auditStatus") String auditStatus,
            String auditExplain) {
        Result<GoodList> result = new Result<GoodList>();
        Arrays.asList(StringUtils.split(id,",")).forEach(gid->{
            GoodList goodList = goodListService.getById(gid);
            if (oConvertUtils.isEmpty(goodList)) {
                result.error500("未找到对应实体");
            } else {
                System.out.println("goodList.getStatus()+++++++++++++" + goodList.getStatus());
                goodList.setAuditStatus(auditStatus);
                goodList.setAuditExplain(auditExplain);
                if ("3".equals(auditStatus)) {
                    //审核不通过下架商品
                    goodList.setFrameStatus("0");
                    //专区商品处理
                    //停用后修改专区商品商品
                    QueryWrapper<MarketingPrefectureGood> queryWrapper = new QueryWrapper();
                    queryWrapper.eq("del_flag", "0");
                    queryWrapper.eq("good_list_id", goodList.getId());
                    List<MarketingPrefectureGood> marketingPrefectureGoodList = marketingPrefectureGoodService.list(queryWrapper);
                    for (MarketingPrefectureGood mpg : marketingPrefectureGoodList) {
                        mpg.setSrcStatus("0");
                        marketingPrefectureGoodService.updateById(mpg);
                    }
                }
                goodListService.updateById(goodList);
            }
        });
        result.success("修改成功");
        return result;
    }

    /**
     * 通过id查询修改删除状态：
     *
     * @param id
     * @return
     */
    @AutoLog(value = "商品管理-通过id查询")
    @ApiOperation(value = "商品管理-通过id查询", notes = "商品管理-通过id查询")
    @GetMapping(value = "/updateDelFlag")
    public Result<GoodList> updateDelFlag(@RequestParam(name = "id", required = true) String id) {
        Result<GoodList> result = new Result<GoodList>();
        GoodList goodList = goodListService.getGoodListById(id);
        if (oConvertUtils.isEmpty(goodList)) {
            result.error500("未找到对应实体");
        } else {
            try {
                //goodList.setDelFlag("0");
                goodListService.updateDelFalg(goodList, "0");
                result.success("修改成功!");
            } catch (Exception e) {
                result.error500("修改失败！");
            }
        }
        return result;
    }

    /**
     * 批量id查询修改删除状态：
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "商品管理-通过id查询")
    @ApiOperation(value = "商品管理-通过id查询", notes = "商品管理-通过id查询")
    @GetMapping(value = "/updateDelFlags")
    public Result<GoodList> updateDelFlags(@RequestParam(name = "ids", required = true) String ids) {
        Result<GoodList> result = new Result<GoodList>();
        if (StringUtils.isEmpty(ids)) {// ids == null || "".equals(ids.trim())
            result.error500("参数不识别！");
        } else {
            GoodList goodList;
            try {
                List<String> listid = Arrays.asList(ids.split(","));
                //goodList.setDelFlag("0");
                for (String id : listid) {
                    goodList = goodListService.getGoodListById(id);
                    if (goodList == null) {
                        result.error500("未找到对应实体");
                    } else {
                        goodListService.updateDelFalg(goodList, "0");
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
     * 批量修改上下架
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "商品管理-通过id查询")
    @ApiOperation(value = "商品管理-通过id查询", notes = "商品管理-通过id查询")
    @GetMapping(value = "/updateFrameStatus")
    public Result<GoodList> updateFrameStatus(@RequestParam(name = "ids", required = true) String ids,
                                              @RequestParam(name = "frameStatus", required = true) String frameStatus,
                                              @RequestParam(name = "frameExplain", required = true) String frameExplain) {
        Result<GoodList> result = new Result<GoodList>();
        if (StringUtils.isEmpty(ids)) {// ids == null || "".equals(ids.trim())
            result.error500("参数不识别！");
        } else {
            GoodList goodList;
            try {
                List<String> listid = Arrays.asList(ids.split(","));
                for (String id : listid) {
                    goodList = goodListService.getGoodListById(id);
                    if (oConvertUtils.isEmpty(goodList)) {
                        result.error500("未找到对应实体");
                    } else {
                        if (frameStatus.equals("1") && goodList.getRepertory().intValue() <= 0) {
                            return result.error500(goodList.getGoodName() + "该商品库存为零，请补充库存后再上架");
                        } else {
                            goodList.setFrameExplain(frameExplain);
                            goodList.setFrameStatus(frameStatus);
                            goodListService.updateById(goodList);
                            if (frameStatus.equals("0")) {
                                //停用后修改专区商品商品
                                QueryWrapper<MarketingPrefectureGood> queryWrapper = new QueryWrapper();
                                queryWrapper.eq("del_flag", "0");
                                queryWrapper.eq("good_list_id", goodList.getId());
                                List<MarketingPrefectureGood> marketingPrefectureGoodList = marketingPrefectureGoodService.list(queryWrapper);
                                for (MarketingPrefectureGood mpg : marketingPrefectureGoodList) {
                                    mpg.setSrcStatus("0");
                                    marketingPrefectureGoodService.updateById(mpg);
                                }
                            }
                            if (frameStatus.equals("1")) {
                                //停用后修改专区商品商品
                                QueryWrapper<MarketingPrefectureGood> queryWrapper = new QueryWrapper();
                                queryWrapper.eq("del_flag", "0");
                                queryWrapper.eq("good_list_id", goodList.getId());
                                List<MarketingPrefectureGood> marketingPrefectureGoodList = marketingPrefectureGoodService.list(queryWrapper);
                                for (MarketingPrefectureGood mpg : marketingPrefectureGoodList) {
                                    mpg.setSrcStatus("1");
                                    marketingPrefectureGoodService.updateById(mpg);
                                }
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
     * 通过id查询修改审核状态：
     * @param
     * @return
     */
    /**
     * @param json ID +AuditStatus
     * @return
     */
    @AutoLog(value = "商品管理-通过id查询")
    @ApiOperation(value = "商品管理-通过id查询", notes = "商品管理-通过id查询")
    // @GetMapping(value = "/updateAuditStatus")
    @RequestMapping(value = "/updateAuditStatusPL", method = RequestMethod.PUT)
    public Result<GoodList> updateAuditStatusPL(@RequestBody JSONObject json) {
        Result<GoodList> result = new Result<GoodList>();
        String ids = json.getString("id");
        String auditStatus = json.getString("auditStatus");
        String auditExplain = json.getString("auditExplain");
        if (StringUtils.isBlank(auditStatus)) {
            result.error500("审核状态不能为空！");
            return result;
        }
        if (StringUtils.isEmpty(ids)) {
            result.error500("用户id不能为空！");
            return result;
        }
        GoodList goodList;
        try {
            List<String> listid = Arrays.asList(ids.split(","));
            for (String id : listid) {
                goodList = goodListService.getGoodListById(id);
                if (goodList == null) {
                    result.error500("未找到对应实体");
                } else {

                    goodList.setAuditStatus(auditStatus);
                    goodList.setAuditExplain(auditExplain);
                    goodListService.updateById(goodList);
                    result.success("修改成功!");

                    if ("0".equals(goodList.getFrameStatus()) || "0".equals(goodList.getStatus()) || !goodList.getAuditStatus().equals("2")) {
                        //停用后修改专区商品商品
                        QueryWrapper<MarketingPrefectureGood> queryWrapper = new QueryWrapper();
                        queryWrapper.eq("del_flag", "0");
                        queryWrapper.eq("good_list_id", goodList.getId());
                        List<MarketingPrefectureGood> marketingPrefectureGoodList = marketingPrefectureGoodService.list(queryWrapper);
                        for (MarketingPrefectureGood mpg : marketingPrefectureGoodList) {
                            mpg.setSrcStatus("0");
                            marketingPrefectureGoodService.updateById(mpg);
                        }
                    }


                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.error500("修改失败！");
        }
        return result;
    }


    /**
     * 批量修改:启用停用
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "商品管理-通过id查询")
    @ApiOperation(value = "商品管理-通过id查询", notes = "商品管理-通过id查询")
    @GetMapping(value = "/updateStatus")
    public Result<GoodList> updateStatus(@RequestParam(name = "ids", required = true) String ids,
                                         @RequestParam(name = "status") String status,
                                         @RequestParam(name = "statusExplain") String statusExplain) {
        Result<GoodList> result = new Result<GoodList>();
        if (StringUtils.isEmpty(ids)) {
            result.error500("参数不识别！");
        } else {
            GoodList goodList;
            try {
                List<String> listid = Arrays.asList(ids.split(","));
                for (String id : listid) {
                    goodList = goodListService.getGoodListById(id);
                    if (goodList == null) {
                        result.error500("未找到对应实体");
                    } else {
                        goodList.setStatusExplain(statusExplain);
                        goodList.setStatus(status);
                        goodListService.updateById(goodList);
                        //专区商品处理
                        if (status.equals("0")) {
                            //停用后修改专区商品商品
                            QueryWrapper<MarketingPrefectureGood> queryWrapper = new QueryWrapper();
                            queryWrapper.eq("del_flag", "0");
                            queryWrapper.eq("good_list_id", goodList.getId());
                            List<MarketingPrefectureGood> marketingPrefectureGoodList = marketingPrefectureGoodService.list(queryWrapper);
                            for (MarketingPrefectureGood mpg : marketingPrefectureGoodList) {
                                mpg.setSrcStatus("0");
                                marketingPrefectureGoodService.updateById(mpg);
                            }
                        }
                        if (status.equals("1")) {
                            //停用后修改专区商品商品
                            QueryWrapper<MarketingPrefectureGood> queryWrapper = new QueryWrapper();
                            queryWrapper.eq("del_flag", "0");
                            queryWrapper.eq("good_list_id", goodList.getId());
                            List<MarketingPrefectureGood> marketingPrefectureGoodList = marketingPrefectureGoodService.list(queryWrapper);
                            for (MarketingPrefectureGood mpg : marketingPrefectureGoodList) {
                                mpg.setSrcStatus("1");
                                marketingPrefectureGoodService.updateById(mpg);
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
     * 修改:比价链接
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "商品管理-通过id查询")
    @ApiOperation(value = "商品管理-通过id查询", notes = "商品管理-通过id查询")
    @GetMapping(value = "/updateSkuUrl")
    public Result<GoodList> updateSkuUrl(@RequestParam(name = "ids", required = true) String ids, @RequestParam(name = "skuUrl") String skuUrl) {
        Result<GoodList> result = new Result<GoodList>();
        if (StringUtils.isEmpty(ids)) {
            result.error500("参数不识别！");
        } else {
            GoodList goodList;
            try {
                List<String> listid = Arrays.asList(ids.split(","));
                for (String id : listid) {
                    goodList = goodListService.getGoodListById(id);
                    if (goodList == null) {
                        result.error500("未找到对应实体");
                    } else {
                        goodList.setSkuUrl(skuUrl);
                        goodListService.updateById(goodList);
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
     * 通过id删除,添加删除原因
     *
     * @param id
     * @return
     */
    @AutoLog(value = "商品列表-通过id删除")
    @ApiOperation(value = "商品列表-通过id删除", notes = "商品列表-通过id删除")
    @DeleteMapping(value = "/deleteAndDelExplain")
    public Result<?> deleteAndDelExplain(@RequestParam(name = "id", required = true) String id, String delExplain) {
        try {
            GoodList goodList = goodListService.getById(id);
            goodList.setDelExplain(delExplain);
            goodList.setDelTime(new Date());
            //修改
            goodListService.updateById(goodList);
            //删除
            goodListService.removeById(goodList.getId());
        } catch (Exception e) {
            log.error("删除失败", e.getMessage());
            return Result.error("删除失败!");
        }
        return Result.ok("删除成功!");
    }

    /**
     * 复制商品地址
     *
     * @param goodId
     * @return
     */
    @AutoLog(value = "商品列表-分页列表查询")
    @ApiOperation(value = "商品列表-分页列表查询", notes = "商品列表-分页列表查询")
    @GetMapping(value = "/getGoodUrl")
    public Result<Map<String, Object>> getGoodUrl(String goodId) {
        Result<Map<String, Object>> result = new Result<>();
        Map<String, String> map = Maps.newHashMap();
        map.put("goodId", goodId);
        map.put("isPlatform", "1");
        String url = "goodAction/pages/product/product?info=";
        Map<String, Object> mapObject = Maps.newHashMap();
        mapObject.put("url", url);
        mapObject.put("parameter", JSONObject.toJSONString(map));
        result.setResult(mapObject);
        result.setSuccess(true);
        return result;
    }


    /**
     * 专区商品-选择商品弹窗
     *
     * @param goodListVo
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "专区商品-选择商品弹窗")
    @ApiOperation(value = "专区商品-选择商品弹窗", notes = "专区商品-选择商品弹窗")
    @GetMapping(value = "/getMarketingPrefectureGood")
    public Result<IPage<Map<String, Object>>> getMarketingPrefectureGood(GoodListVo goodListVo,
                                                                         @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                                         @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                                         HttpServletRequest req) {
        Result<IPage<Map<String, Object>>> result = new Result<IPage<Map<String, Object>>>();
        if (StringUtils.isBlank(goodListVo.getMarketingPrefectureId())) {
            result.error500("专区id不能为空");
            return result;
        }
        //专区
        MarketingPrefecture marketingPrefecture = marketingPrefectureService.getById(goodListVo.getMarketingPrefectureId());
        if (marketingPrefecture == null) {
            result.error500("未找到专区!");
            return result;
        }
        if (marketingPrefecture.getAstrictGood().equals("1")) {
            //限制商品条件
            goodListVo.setAstrictPriceProportion(marketingPrefecture.getAstrictPriceProportion());
        }
        //已添加到专区商品过滤
        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("validTime", marketingPrefecture.getValidTime());
        paramMap.put("startTime", marketingPrefecture.getStartTime());
        paramMap.put("endTime", marketingPrefecture.getEndTime());
        paramMap.put("marketingPrefectureId", marketingPrefecture.getId());
        List<Map<String, Object>> marketingPrefectureGood = marketingPrefectureService.getFiltrationGoodIds(paramMap);
        List<String> marketingPrefectureGoodNotIds = new ArrayList<>();
        marketingPrefectureGood.forEach(mpg -> {
            marketingPrefectureGoodNotIds.add(mpg.get("good_list_id").toString());
        });
        if (marketingPrefectureGoodNotIds.size() > 0) {
            //过滤已存在专区商品
            goodListVo.setMarketingPrefectureGoodNotIds(marketingPrefectureGoodNotIds);
        }

        Page<GoodList> page = new Page<GoodList>(pageNo, pageSize);
        IPage<Map<String, Object>> pageList = goodListService.getMarketingPrefectureGood(page, goodListVo);
        result.setSuccess(true);
        result.setCode(200);
        result.setResult(pageList);
        return result;
    }


    /**
     * 分页列表查询
     *
     * @param goodListVo
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "商品列表-分页列表查询")
    @ApiOperation(value = "商品列表-分页列表查询", notes = "商品列表-分页列表查询")
    @GetMapping(value = "/getMarketingMarKarKetingGood")
    public Result<IPage<Map<String, Object>>> getMarketingMarKarKetingGood(GoodListVo goodListVo,
                                                                           @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                                           @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                                           HttpServletRequest req) {
        Result<IPage<Map<String, Object>>> result = new Result<IPage<Map<String, Object>>>();


        //已添加到专区商品过滤
        if (StringUtils.isNotBlank(goodListVo.getMarketingMaterialListId())) {
            List<MarketingMaterialGood> marketingMaterialGoodList = iMarketingMaterialGoodService.list(new LambdaQueryWrapper<MarketingMaterialGood>().eq(MarketingMaterialGood::getMarketingMaterialListId, goodListVo.getMarketingMaterialListId()));
            List<String> marketingPrefectureGoodNotIds = new ArrayList<>();
            marketingMaterialGoodList.forEach(mpg -> {
                marketingPrefectureGoodNotIds.add(mpg.getId());
            });
            if (marketingPrefectureGoodNotIds.size() > 0) {
                //过滤已存在素材列表商品
                goodListVo.setMarketingPrefectureGoodNotIds(marketingPrefectureGoodNotIds);
            }
        }
        Page<GoodList> page = new Page<GoodList>(pageNo, pageSize);
        IPage<Map<String, Object>> pageList = goodListService.getMarketingPrefectureGood(page, goodListVo);
        result.setSuccess(true);
        result.setCode(200);
        result.setResult(pageList);
        return result;
    }


    @AutoLog(value = "商品添加编辑-查询商品编号是否存在")
    @ApiOperation(value = "商品添加编辑-查询商品编号是否存在", notes = "商品添加编辑-查询商品编号是否存在")
    @GetMapping(value = "/getGoodNoCount")
    public Result<Long> getGoodNoCount(String goodId, Integer isPlatform, String goodNo) {
        Result<Long> result = new Result<>();
        //参数判断
        if (isPlatform == null) {
            result.error500("isPlatform  是否平台类型参数不能为空！！！   ");
            return result;
        }

        if (StringUtils.isBlank(goodNo)) {
            result.error500("商品编号不能为空！！！   ");
            return result;
        }
        //查询相同商品编号个数
        long count = goodListService.getGoodNoCount(goodId, isPlatform, goodNo);

        result.setResult(count);
        result.success("查询成功");
        return result;

    }


    /**
     * 根据商品id获取商品详情需要的信息(商品预览)
     *
     * @param goodId
     * @param isPlatform
     * @param request
     * @return
     */
    @RequestMapping("findGoodListByGoodId")
    @ResponseBody
    public Result<Map<String, Object>> findGoodListByGoodId(String goodId, Integer isPlatform, @RequestParam(value = "marketingPrefectureId", defaultValue = "", required = false) String marketingPrefectureId, HttpServletRequest request) {
        Result<Map<String, Object>> result = new Result<>();
        Map<String, Object> resultMap = Maps.newHashMap();
        //参数判断
        if (isPlatform == null) {
            result.error500("isPlatform  是否平台类型参数不能为空！！！   ");
            return result;
        }

        if (org.apache.commons.lang3.StringUtils.isBlank(goodId)) {
            result.error500("goodId  商品id不能为空！！！   ");
            return result;
        }

        //组织查询参数
        Map<String, Object> paramObjectMap = Maps.newHashMap();
        paramObjectMap.put("goodId", goodId);
        paramObjectMap.put("isPlatform", isPlatform);


        //查询店铺商品详情
        if (isPlatform.intValue() == 0) {
            //判断商品是否被收藏
            resultMap.put("isCollect", 0);

            //获取商品信息
            QueryWrapper<GoodStoreList> queryWrapperGoodStoreList = new QueryWrapper();

            queryWrapperGoodStoreList.select("id AS id,main_picture AS mainPicture,0 AS isPlatform,good_name AS goodName,small_price AS  smallPrice,good_describe AS goodDescribe,\n" +
                    "market_price AS marketPrice,small_vip_price AS smallVipPrice,specification AS specification,activity_price AS activityPrice,\n" +
                    "good_video AS goodVideo,details_goods AS detailsGoods,repertory AS repertory,frame_status AS frameStatus,good_store_type_id AS goodTypeId,activities_type AS activitiesType");
            queryWrapperGoodStoreList.eq("id", goodId);
            Map<String, Object> goodStoreListMap = goodStoreListService.getMap(queryWrapperGoodStoreList);
            //获取最便宜的规格组合信息
            QueryWrapper<GoodStoreSpecification> goodStoreSpecificationQueryWrapper = new QueryWrapper<>();
            goodStoreSpecificationQueryWrapper.eq("good_store_list_id", goodId);
            goodStoreSpecificationQueryWrapper.orderByAsc("price");
            GoodStoreSpecification goodStoreSpecification = iGoodStoreSpecificationService.list(goodStoreSpecificationQueryWrapper).get(0);
            ;
            goodStoreListMap.put("smallSpecification", goodStoreSpecification.getSpecification());
            resultMap.put("goodinfo", goodStoreListMap);

            //获取商品最新的一条评论信息
            Page<Map<String, Object>> evaluatepage = new Page<Map<String, Object>>(1, 1);
            Map<String, Object> paramevaluateMap = Maps.newHashMap();
            paramevaluateMap.put("goodId", goodId);
            paramevaluateMap.put("pattern", 0);

            resultMap.put("evaluateInfo", iOrderEvaluateStoreService.findOrderEvaluateByGoodId(evaluatepage, paramevaluateMap).getRecords());
            //评价总数
            resultMap.put("evaluateCount", iOrderEvaluateStoreService.count(new LambdaQueryWrapper<OrderEvaluateStore>().eq(OrderEvaluateStore::getGoodStoreListId, goodId).eq(OrderEvaluateStore::getStatus, "1")));
            //获取同类型9条推荐商品列表信息
            Page<Map<String, Object>> page = new Page<Map<String, Object>>(1, 9);
            Map<String, Object> paramGoodsMap = Maps.newHashMap();
            paramObjectMap.put("goodTypeId", goodStoreListMap.get("goodTypeId"));
            paramObjectMap.put("pattern", 3);
            resultMap.put("recommendGoods", goodStoreListService.findGoodListByGoodType(page, paramGoodsMap).getRecords());
        } else
            //查询平台商品详情
            if (isPlatform.intValue() == 1) {
                //判断商品是否被收藏
                resultMap.put("isCollect", 0);

                //获取商品信息
                QueryWrapper<GoodList> queryWrapperGoodList = new QueryWrapper();
                queryWrapperGoodList.select("id as id,main_picture as mainPicture,1 as isPlatform,good_name as goodName,small_price as  smallPrice,good_describe as goodDescribe,\n" +
                        "market_price as marketPrice,small_vip_price as smallVipPrice,specification as specification,activity_price as activityPrice,good_form as goodForm,\n" +
                        "good_video as goodVideo,details_goods as detailsGoods,repertory as repertory,frame_status as frameStatus,good_type_id as goodTypeId,activities_type as activitiesType,source_type as sourceType,sku_url as skuUrl,supply_price as supplyPrice");
                queryWrapperGoodList.eq("id", goodId);
                Map<String, Object> goodlistMap = goodListService.getMap(queryWrapperGoodList);

                //专区商品信息
                if (StringUtils.isNotBlank(marketingPrefectureId)) {
                    //不显示vip价格
                    goodlistMap.put("isViewVipPrice", "0");
                    goodlistMap.put("isPrefectureGood", "1");
                    MarketingPrefecture marketingPrefecture = iMarketingPrefectureService.getById(marketingPrefectureId);
                    if (marketingPrefecture == null) {
                        result.error500("专区id找不到相关的专区信息");
                        return result;
                    }
                    goodlistMap.put("prefectureLabel", marketingPrefecture.getPrefectureLabel());
                    //获取专区商品
                    QueryWrapper<MarketingPrefectureGood> marketingPrefectureGoodQueryWrapper = new QueryWrapper<>();
                    marketingPrefectureGoodQueryWrapper.eq("marketing_prefecture_id", marketingPrefecture.getId());
                    marketingPrefectureGoodQueryWrapper.eq("good_list_id", goodId);
                    MarketingPrefectureGood marketingPrefectureGood = iMarketingPrefectureGoodService.getOne(marketingPrefectureGoodQueryWrapper);
                    if (marketingPrefectureGood == null) {
                        result.error500("此专区商品不存在！！！");
                        return result;
                    }
                    goodlistMap.put("smallPrefecturePrice", marketingPrefectureGood.getSmallPrefecturePrice());
                    //是否支持福利金抵扣
                    goodlistMap.put("isWelfare", marketingPrefectureGood.getIsWelfare());
                    if (marketingPrefectureGood.getIsWelfare().equals("1")) {
                        goodlistMap.put("welfareProportionPrice", marketingPrefectureGood.getSmallPrefecturePrice());
                    } else if (marketingPrefectureGood.getIsWelfare().equals("2")) {
                        if (org.apache.commons.lang3.StringUtils.indexOf(marketingPrefectureGood.getWelfareProportion(), "-") > -1) {
                            goodlistMap.put("welfareProportionPrice", marketingPrefectureGood.getSmallPrefecturePrice().multiply(new BigDecimal(org.apache.commons.lang3.StringUtils.trim(org.apache.commons.lang3.StringUtils.substringAfterLast(marketingPrefectureGood.getWelfareProportion(), "-"))).divide(new BigDecimal(100))));

                        } else {
                            goodlistMap.put("welfareProportionPrice", marketingPrefectureGood.getSmallPrefecturePrice().multiply(new BigDecimal(marketingPrefectureGood.getWelfareProportion()).divide(new BigDecimal(100))));

                        }
                    } else if (marketingPrefectureGood.getIsWelfare().equals("3")) {
                        if (org.apache.commons.lang3.StringUtils.indexOf(goodlistMap.get("supplyPrice").toString(), "-") > -1) {
                            if (org.apache.commons.lang3.StringUtils.indexOf(marketingPrefectureGood.getWelfareProportion(), "-") > -1) {
                                goodlistMap.put("welfareProportionPrice", marketingPrefectureGood.getSmallPrefecturePrice().subtract(new BigDecimal(org.apache.commons.lang3.StringUtils.trim(org.apache.commons.lang3.StringUtils.substringBefore(goodlistMap.get("supplyPrice").toString(), "-")))).multiply(new BigDecimal(org.apache.commons.lang3.StringUtils.trim(org.apache.commons.lang3.StringUtils.substringAfterLast(marketingPrefectureGood.getWelfareProportion(), "-"))).divide(new BigDecimal(100))).setScale(2, RoundingMode.DOWN));
                            } else {
                                goodlistMap.put("welfareProportionPrice", marketingPrefectureGood.getSmallPrefecturePrice().subtract(new BigDecimal(org.apache.commons.lang3.StringUtils.trim(org.apache.commons.lang3.StringUtils.substringBefore(goodlistMap.get("supplyPrice").toString(), "-")))).multiply(new BigDecimal(marketingPrefectureGood.getWelfareProportion()).divide(new BigDecimal(100))).setScale(2, RoundingMode.DOWN));
                            }
                        } else {

                            if (org.apache.commons.lang3.StringUtils.indexOf(marketingPrefectureGood.getWelfareProportion(), "-") > -1) {
                                goodlistMap.put("welfareProportionPrice", marketingPrefectureGood.getSmallPrefecturePrice().subtract(new BigDecimal(goodlistMap.get("supplyPrice").toString())).multiply(new BigDecimal(org.apache.commons.lang3.StringUtils.trim(org.apache.commons.lang3.StringUtils.substringAfterLast(marketingPrefectureGood.getWelfareProportion(), "-"))).divide(new BigDecimal(100))).setScale(2, RoundingMode.DOWN));
                            } else {
                                goodlistMap.put("welfareProportionPrice", marketingPrefectureGood.getSmallPrefecturePrice().subtract(new BigDecimal(goodlistMap.get("supplyPrice").toString())).multiply(new BigDecimal(marketingPrefectureGood.getWelfareProportion()).divide(new BigDecimal(100))).setScale(2,RoundingMode.DOWN));
                            }


                        }
                    } else {
                        goodlistMap.put("welfareProportionPrice", 0);
                    }

                    //是否会员免福利金
                    goodlistMap.put("isVipLower", marketingPrefecture.getIsVipLower());

                    //是否显示市场价格
                    goodlistMap.put("isViewMarketPrice", marketingPrefecture.getIsViewMarketPrice());

                    //赠送福利金
                    goodlistMap.put("isGiveWelfare", marketingPrefectureGood.getIsGiveWelfare());
                    if (marketingPrefectureGood.getIsGiveWelfare().equals("1")) {
                        if (org.apache.commons.lang3.StringUtils.indexOf(marketingPrefectureGood.getGiveWelfareProportion(), "-") > -1) {
                            goodlistMap.put("giveWelfareProportion", marketingPrefectureGood.getSmallPrefecturePrice().multiply(new BigDecimal(org.apache.commons.lang3.StringUtils.substringAfterLast(marketingPrefectureGood.getGiveWelfareProportion(), "-")).divide(new BigDecimal(100))));
                        } else {
                            goodlistMap.put("giveWelfareProportion", marketingPrefectureGood.getSmallPrefecturePrice().multiply(new BigDecimal(marketingPrefectureGood.getGiveWelfareProportion()).divide(new BigDecimal(100))));
                        }
                    } else {
                        goodlistMap.put("giveWelfareProportion", 0);
                    }

                    goodlistMap.put("marketingPrefectureId", marketingPrefectureId);

                    goodlistMap.put("isDiscount", marketingPrefecture.getIsDiscount());

                } else {
                    //显示vip价格
                    goodlistMap.put("isViewVipPrice", "1");
                    goodlistMap.put("isPrefectureGood", "0");
                }


                //获取最便宜的规格组合信息

                QueryWrapper<GoodSpecification> goodSpecificationQueryWrapper = new QueryWrapper<>();
                goodSpecificationQueryWrapper.eq("good_list_id", goodId);
                goodSpecificationQueryWrapper.orderByAsc("price");
                GoodSpecification goodSpecification = iGoodSpecificationService.list(goodSpecificationQueryWrapper).get(0);
                goodlistMap.put("smallSpecification", goodSpecification.getSpecification());
                resultMap.put("goodinfo", goodlistMap);
                //获取商品最新的一条评论信息

                Page<Map<String, Object>> evaluatepage = new Page<Map<String, Object>>(1, 1);
                Map<String, Object> paramevaluateMap = Maps.newHashMap();
                paramevaluateMap.put("goodId", goodId);
                paramevaluateMap.put("pattern", 0);

                resultMap.put("evaluateInfo", iOrderEvaluateService.findOrderEvaluateByGoodId(evaluatepage, paramevaluateMap).getRecords());
                //评价总数
                resultMap.put("evaluateCount", iOrderEvaluateService.count(new LambdaQueryWrapper<OrderEvaluate>().eq(OrderEvaluate::getGoodListId, goodId).eq(OrderEvaluate::getStatus, "1")));

                //获取同类型9条推荐商品列表信息
                Page<Map<String, Object>> page = new Page<Map<String, Object>>(1, 9);
                Map<String, Object> paramGoodsMap = Maps.newHashMap();
                paramObjectMap.put("goodTypeId", goodlistMap.get("goodTypeId"));
                paramObjectMap.put("pattern", 3);
                resultMap.put("recommendGoods", goodListService.findGoodListByGoodType(page, paramGoodsMap).getRecords());
            } else {
                result.error500("isPlatform  参数不正确请联系平台管理员！！！  ");
                return result;
            }
        resultMap.put("carGoods", "0");


        result.setResult(resultMap);
        result.success("查询商品详情成功");
        return result;
    }


}
