package org.jeecg.modules.marketing.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.marketing.dto.MarketingCertificateDTO;
import org.jeecg.modules.marketing.dto.MarketingCertificateGoodDTO;
import org.jeecg.modules.marketing.dto.MarketingCertificateStoreDTO;
import org.jeecg.modules.marketing.entity.MarketingCertificate;
import org.jeecg.modules.marketing.service.IMarketingCertificateGoodService;
import org.jeecg.modules.marketing.service.IMarketingCertificateService;
import org.jeecg.modules.marketing.service.IMarketingCertificateStoreService;
import org.jeecg.modules.marketing.vo.MarketingCertificateGoodVO;
import org.jeecg.modules.marketing.vo.MarketingCertificateStoreVO;
import org.jeecg.modules.marketing.vo.MarketingCertificateVO;
import org.jeecg.modules.store.service.IStoreManageService;
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
 * @Description: 兑换券
 * @Author: jeecg-boot
 * @Date: 2019-11-13
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "兑换券")
@RestController
@RequestMapping("/marketingCertificate/marketingCertificate")
public class MarketingCertificateController {
    @Autowired
    private IMarketingCertificateService marketingCertificateService;

    @Autowired
    private IMarketingCertificateGoodService marketingCertificateGoodService;
    @Autowired
    private IMarketingCertificateStoreService marketingCertificateStoreService;
    @Autowired
    private IStoreManageService iStoreManageService;

    /**
     * 分页列表查询
     *
     * @param marketingCertificate
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "兑换券-分页列表查询")
    @ApiOperation(value = "兑换券-分页列表查询", notes = "兑换券-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<MarketingCertificate>> queryPageList(MarketingCertificate marketingCertificate,
                                                             @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                             @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                             HttpServletRequest req) {
        Result<IPage<MarketingCertificate>> result = new Result<IPage<MarketingCertificate>>();
        QueryWrapper<MarketingCertificate> queryWrapper = QueryGenerator.initQueryWrapper(marketingCertificate, req.getParameterMap());
        Page<MarketingCertificate> page = new Page<MarketingCertificate>(pageNo, pageSize);
        IPage<MarketingCertificate> pageList = marketingCertificateService.page(page, queryWrapper);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }
    @AutoLog(value = "兑换券-new分页列表查询")
    @ApiOperation(value = "兑换券-new分页列表查询", notes = "兑换券-new分页列表查询")
    @RequestMapping(value = "findMarketingCertificate",method = RequestMethod.GET)
    public Result<IPage<MarketingCertificateDTO>>findMarketingCertificate(MarketingCertificateVO marketingCertificateVO,
                                                                          @RequestParam(value = "pageNo",defaultValue = "1")Integer pageNo,
                                                                          @RequestParam(value = "pageSize",defaultValue = "10")Integer pageSize){
        Result<IPage<MarketingCertificateDTO>> result = new Result<>();
        Page<MarketingCertificateDTO> page = new Page<MarketingCertificateDTO>(pageNo, pageSize);
        IPage<MarketingCertificateDTO> marketingCertificate = marketingCertificateService.findMarketingCertificate(page,marketingCertificateVO);
        result.setSuccess(true);
        result.setResult(marketingCertificate);
        return result;
    }
    /**
     * 添加
     *
     * @param marketingCertificateVO
     * @return
     */
    @AutoLog(value = "兑换券-添加")
    @ApiOperation(value = "兑换券-添加", notes = "兑换券-添加")
    @PostMapping(value = "/add")
    public Result<MarketingCertificate> add(@RequestBody MarketingCertificateVO marketingCertificateVO) {
        Result<MarketingCertificate> result = new Result<MarketingCertificate>();
        MarketingCertificate marketingCertificate = new MarketingCertificate();
        BeanUtils.copyProperties(marketingCertificateVO,marketingCertificate);
        if("1".equals(marketingCertificateVO.getVouchersWay())){
            marketingCertificate.setDisData(marketingCertificateVO.getToday());
        }
        if("2".equals(marketingCertificateVO.getVouchersWay())){
            marketingCertificate.setDisData(marketingCertificateVO.getTomorow());
        }
        marketingCertificate.setMainPicture(marketingCertificateVO.getMainPictures());
        marketingCertificate.setCoverPlan(marketingCertificateVO.getCoverPlans());
        String goodListIds = marketingCertificateVO.getGoodListIds();
        String sysUserIds = marketingCertificateVO.getSysUserIds();
        Result<MarketingCertificate> marketingCertificateResult = marketingCertificateService.saveMarketingCertificate(marketingCertificate, goodListIds, sysUserIds);
        return marketingCertificateResult;
    }

    /**
     * 编辑
     *
     * @param marketingCertificateVO
     * @return
     */
    @AutoLog(value = "兑换券-编辑")
    @ApiOperation(value = "兑换券-编辑", notes = "兑换券-编辑")
    @PostMapping(value = "/edit")
    public Result<MarketingCertificate> edit(@RequestBody MarketingCertificateVO marketingCertificateVO) {
        return marketingCertificateService.edit(marketingCertificateVO);
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "兑换券-通过id删除")
    @ApiOperation(value = "兑换券-通过id删除", notes = "兑换券-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        try {
            marketingCertificateService.removeById(id);
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
    @AutoLog(value = "兑换券-批量删除")
    @ApiOperation(value = "兑换券-批量删除", notes = "兑换券-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<MarketingCertificate> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        Result<MarketingCertificate> result = new Result<MarketingCertificate>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别！");
        } else {
            this.marketingCertificateService.removeByIds(Arrays.asList(ids.split(",")));
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
    @AutoLog(value = "兑换券-通过id查询")
    @ApiOperation(value = "兑换券-通过id查询", notes = "兑换券-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<MarketingCertificate> queryById(@RequestParam(name = "id", required = true) String id) {
        Result<MarketingCertificate> result = new Result<MarketingCertificate>();
        MarketingCertificate marketingCertificate = marketingCertificateService.getById(id);
        if (marketingCertificate == null) {
            result.error500("未找到对应实体");
        } else {
            result.setResult(marketingCertificate);
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
        QueryWrapper<MarketingCertificate> queryWrapper = null;
        try {
            String paramsStr = request.getParameter("paramsStr");
            if (oConvertUtils.isNotEmpty(paramsStr)) {
                String deString = URLDecoder.decode(paramsStr, "UTF-8");
                MarketingCertificate marketingCertificate = JSON.parseObject(deString, MarketingCertificate.class);
                queryWrapper = QueryGenerator.initQueryWrapper(marketingCertificate, request.getParameterMap());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //Step.2 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        List<MarketingCertificate> pageList = marketingCertificateService.list(queryWrapper);
        //导出文件名称
        mv.addObject(NormalExcelConstants.FILE_NAME, "兑换券列表");
        mv.addObject(NormalExcelConstants.CLASS, MarketingCertificate.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("兑换券列表数据", "导出人:Jeecg", "导出信息"));
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
                List<MarketingCertificate> listMarketingCertificates = ExcelImportUtil.importExcel(file.getInputStream(), MarketingCertificate.class, params);
                marketingCertificateService.saveBatch(listMarketingCertificates);
                return Result.ok("文件导入成功！数据行数:" + listMarketingCertificates.size());
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

    @AutoLog(value = "兑换券-通过id查询出对应的商品")
    @ApiOperation(value = "兑换券-通过id查询出对应的商品", notes = "兑换券-通过id查询出对应的商品")
    @RequestMapping(value = "findGoodByCertificateId", method = RequestMethod.GET)
    public Result<Map<String,Object>> findGoodByCertificateId(MarketingCertificateGoodVO marketingCertificateGoodVO) {
        Result<Map<String,Object>> result = new Result<>();
        String marketingCertificateId = marketingCertificateGoodVO.getMarketingCertificateId();
        List<MarketingCertificateGoodDTO> goodByCertificateId = marketingCertificateGoodService.findGoodByCertificateId(marketingCertificateId);
        HashMap<String, Object> map = new HashMap<>();
        map.put("records",goodByCertificateId);
        result.setSuccess(true);
        result.setResult(map);
        return result;
    }
    @AutoLog(value = "兑换券-通过id查询出核销门店")
    @ApiOperation(value = "兑换券-通过id查询出核销门店", notes = "兑换券-通过id查询出核销门店")
    @RequestMapping(value = "findStoreByCertificateId",method = RequestMethod.GET)
    public Result<Map<String,Object>> findStoreByCertificateId(MarketingCertificateStoreVO marketingCertificateStoreVO) {
        Result<Map<String,Object>> result = new Result<>();
        String marketingCertificateId = marketingCertificateStoreVO.getMarketingCertificateId();
        List<MarketingCertificateStoreDTO> storeByCertificateId = marketingCertificateStoreService.findStoreByCertificateId(marketingCertificateId);
        HashMap<String, Object> map = new HashMap<>();
        map.put("records",storeByCertificateId);
        result.setSuccess(true);
        result.setResult(map);
        return result;
    }
    @AutoLog(value = "兑换券-礼包添加兑换券调用接口")
    @ApiOperation(value = "兑换券-礼包添加兑换券调用接口", notes = "兑换券-礼包添加兑换券调用接口")
    @RequestMapping(value = "findCertificateVO",method = RequestMethod.GET)
    public Result<Map<String,Object>>findCertificateVO(MarketingCertificateVO marketingCertificateVO){
        Result<Map<String,Object>> result = new Result<>();
        List<MarketingCertificateVO> certificateVO = marketingCertificateService.findCertificateVO(marketingCertificateVO);
        HashMap<String, Object> map = new HashMap<>();
        map.put("records",certificateVO);
        result.setSuccess(true);
        result.setResult(map);
        return result;
    }
    @AutoLog(value = "兑换券-推荐兑换券调用接口")
    @ApiOperation(value = "兑换券-推荐兑换券调用接口", notes = "兑换券-推荐兑换券调用接口")
    @RequestMapping(value = "findCertificate",method = RequestMethod.GET)
    public Result<Map<String,Object>>findCertificate(MarketingCertificateVO marketingCertificateVO){
        Result<Map<String,Object>> result = new Result<>();
        List<MarketingCertificateVO> certificateVO = marketingCertificateService.findCertificate(marketingCertificateVO);
        HashMap<String, Object> map = new HashMap<>();
        map.put("records",certificateVO);
        result.setSuccess(true);
        result.setResult(map);
        return result;
    }
    @AutoLog(value = "兑换券-拼好券and限时抢券兑换券调用接口")
    @ApiOperation(value = "兑换券-拼好券and限时抢券兑换券调用接口", notes = "兑换券-拼好券and限时抢券兑换券调用接口")
    @RequestMapping(value = "findCertificateData",method = RequestMethod.GET)
    public Result<Map<String,Object>>findCertificateData(MarketingCertificateVO marketingCertificateVO){
        Result<Map<String,Object>> result = new Result<>();
        List<MarketingCertificateVO> certificateVO = null;
        if (marketingCertificateVO.getMarketingCertificateType().equals("0")){
             certificateVO = marketingCertificateService.findCertificateData(marketingCertificateVO);
        }else {
            certificateVO = marketingCertificateService.findCertificateDataByType(marketingCertificateVO);
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("records",certificateVO);
        result.setSuccess(true);
        result.setResult(map);
        return result;
    }
    @AutoLog(value = "启动停用")
    @ApiOperation(value = "启动停用", notes = "启动停用")
    @RequestMapping(value = "updateStatusById",method = RequestMethod.POST)
    public Result<MarketingCertificate> updateStatusById(@RequestBody MarketingCertificate marketingCertificate){
        Result<MarketingCertificate> result = new Result<>();
        boolean b = marketingCertificateService.updateById(marketingCertificate);
        if (b){
            result.setMessage("修改成功");
            result.setSuccess(true);
        }else {
            result.error500("修改失败");
        }
        return result;
    }

    /**
     * 复制优惠券地址
     * @param marketingCertificateId
     * @return
     */
    @AutoLog(value = "兑换券-复制优惠券地址")
    @ApiOperation(value = "兑换券-复制优惠券地址", notes = "兑换券-复制优惠券地址")
    @GetMapping(value = "/getMarketingCertificateUrl")
    public  Result<String> getMarketingCertificateUrl(String marketingCertificateId) {
        Result<String> result = new Result<>();
        //pages/index/coupon/couponDetail?id=b08262801d0f85d9240f6c608ef15468&path=normal&isPlatform=0
        String url ="pages/index/coupon/couponDetail?id="+marketingCertificateId+"&path=normalExchangeCoupon&isPlatform=1";
        result.setResult(url);
        result.setSuccess(true);
        return result;
    }
    @AutoLog(value = "兑换券-赠送兑换券集合")
    @ApiOperation(value = "兑换券-赠送兑换券集合", notes = "兑换券-赠送兑换券集合")
    @RequestMapping(value = "findGiveMarketingCertificateVO",method = RequestMethod.GET)
    public Result<Map<String,Object>> findGiveMarketingCertificateVO(MarketingCertificateVO marketingCertificateVO){
        Result<Map<String, Object>> result = new Result<>();
        List<MarketingCertificateVO> certificateVO = marketingCertificateService.findGiveMarketingCertificateVO(marketingCertificateVO);
        HashMap<String, Object> map = new HashMap<>();
        map.put("records",certificateVO);
        result.setSuccess(true);
        result.setResult(map);
        return result;
    }
}

