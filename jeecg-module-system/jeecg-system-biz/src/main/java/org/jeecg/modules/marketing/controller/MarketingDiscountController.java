package org.jeecg.modules.marketing.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
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
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.marketing.dto.MarketingDiscountDTO;
import org.jeecg.modules.marketing.dto.MarketingDisountGoodDTO;
import org.jeecg.modules.marketing.entity.MarketingChannelDiscount;
import org.jeecg.modules.marketing.entity.MarketingDiscount;
import org.jeecg.modules.marketing.service.IMarketingChannelDiscountService;
import org.jeecg.modules.marketing.service.IMarketingDiscountGoodService;
import org.jeecg.modules.marketing.service.IMarketingDiscountService;
import org.jeecg.modules.marketing.vo.MarketingDiscountVO;
import org.jeecg.modules.marketing.vo.MarketingDisountGoodVO;
import org.jeecg.modules.system.service.ISysUserRoleService;
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
 * @Description: 优惠券
 * @Author: jeecg-boot
 * @Date: 2019-11-11
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "优惠券")
@RestController
@RequestMapping("/marketingDiscount/marketingDiscount")
public class MarketingDiscountController {
    @Autowired()
    private IMarketingDiscountService marketingDiscountService;

    @Autowired()
    private IMarketingDiscountGoodService marketingDiscountGoodService;

    @Autowired()
    private IMarketingChannelDiscountService marketingChannelDiscountService;

    /**
     * 分页列表查询
     *
     * @param MarketingDiscount
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "优惠券-分页列表查询")
    @ApiOperation(value = "优惠券-分页列表查询", notes = "优惠券-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<MarketingDiscount>> queryPageList(MarketingDiscount MarketingDiscount,
                                                          @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                          HttpServletRequest req) {
        Result<IPage<MarketingDiscount>> result = new Result<IPage<MarketingDiscount>>();
        QueryWrapper<MarketingDiscount> queryWrapper = QueryGenerator.initQueryWrapper(MarketingDiscount, req.getParameterMap());
        Page<MarketingDiscount> page = new Page<MarketingDiscount>(pageNo, pageSize);
        IPage<MarketingDiscount> pageList = marketingDiscountService.page(page, queryWrapper);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    @AutoLog(value = "店铺优惠券-分页列表查询")
    @ApiOperation(value = "店铺优惠券-分页列表查询", notes = "店铺优惠券-分页列表查询")
    @GetMapping(value = "/getMarketingDiscountStoreList")
    public Result<IPage<MarketingDiscountDTO>> getMarketingDiscountStoreList(MarketingDiscountVO marketingDiscountVO,
                                                                             @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                                             @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                                             HttpServletRequest req) {
        Result<IPage<MarketingDiscountDTO>> result = new Result<IPage<MarketingDiscountDTO>>();
        Page<MarketingDiscountDTO> page = new Page<MarketingDiscountDTO>(pageNo, pageSize);
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String userId = sysUser.getId();
        ISysUserRoleService iSysUserRoleService = SpringContextUtils.getBean(ISysUserRoleService.class);
        List<String> roleByUserId = iSysUserRoleService.getRoleByUserId(userId);
        boolean b = roleByUserId.contains("Merchant");
        if (b){
            marketingDiscountVO.setUId(userId);
        }
        IPage<MarketingDiscountDTO> pageList = marketingDiscountService.findMarketingDiscountStoreList(page,marketingDiscountVO);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    @AutoLog(value = "平台优惠券-分页列表查询")
    @ApiOperation(value = "平台优惠券-分页列表查询", notes = "平台优惠券-分页列表查询")
    @GetMapping(value = "/getMarketingDiscountList")
    public Result<IPage<MarketingDiscountDTO>> getMarketingDiscountList(MarketingDiscountVO marketingDiscountVO,
                                                                        @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                                        @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                                        HttpServletRequest req) {
        Result<IPage<MarketingDiscountDTO>> result = new Result<IPage<MarketingDiscountDTO>>();
        Page<MarketingDiscountDTO> page = new Page<MarketingDiscountDTO>(pageNo, pageSize);
        IPage<MarketingDiscountDTO> pageList = marketingDiscountService.getMarketingDiscountList(page, marketingDiscountVO);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 添加
     *
     * @param
     * @return
     */
    @AutoLog(value = "优惠券-添加")
    @ApiOperation(value = "优惠券-添加", notes = "优惠券-添加")
    @PostMapping(value = "/add")
    public Result<MarketingDiscount> add(@RequestBody MarketingDiscountVO marketingDiscountVO) {
        Result<MarketingDiscount> result = new Result<MarketingDiscount>();
        MarketingDiscount MarketingDiscount = new MarketingDiscount();
        BeanUtils.copyProperties(marketingDiscountVO, MarketingDiscount);
        if("1".equals(marketingDiscountVO.getVouchersWay())){
            MarketingDiscount.setDisData(marketingDiscountVO.getToday());
        }
        if("2".equals(marketingDiscountVO.getVouchersWay())){
            MarketingDiscount.setDisData(marketingDiscountVO.getTomorow());
        }
        MarketingDiscount.setMainPicture(marketingDiscountVO.getMainPictures());
        MarketingDiscount.setCoverPlan(marketingDiscountVO.getCoverPlans());
        String goodStoreListIds = marketingDiscountVO.getGoodStoreListIds();
        String channelIds = marketingDiscountVO.getMarketingChannelId();
        String isPlatform = marketingDiscountVO.getIsPlatform();
        try {
            marketingDiscountService.savaMarketingDiscount(MarketingDiscount, goodStoreListIds, channelIds, isPlatform);
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
     * @param marketingDiscountVO
     * @return
     */
    @AutoLog(value = "优惠券-编辑")
    @ApiOperation(value = "优惠券-编辑", notes = "优惠券-编辑")
    @PostMapping(value = "/edit")
    public Result<MarketingDiscountDTO> edit(@RequestBody MarketingDiscountVO marketingDiscountVO) {
       return marketingDiscountService.edit(marketingDiscountVO);
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return*/
    /*@AutoLog(value = "优惠券-通过id删除")
    @ApiOperation(value = "优惠券-通过id删除", notes = "优惠券-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        try {
            marketingDiscountService.removeById(id);
        } catch (Exception e) {
            log.error("删除失败", e.getMessage());
            return Result.error("删除失败!");
        }
        return Result.ok("删除成功!");
    }
*/

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "优惠券-批量删除")
    @ApiOperation(value = "优惠券-批量删除", notes = "优惠券-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<MarketingDiscount> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        Result<MarketingDiscount> result = new Result<MarketingDiscount>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别！");
        } else {
            this.marketingDiscountService.removeByIds(Arrays.asList(ids.split(",")));
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
    @AutoLog(value = "优惠券-通过id查询")
    @ApiOperation(value = "优惠券-通过id查询", notes = "优惠券-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<MarketingDiscount> queryById(@RequestParam(name = "id", required = true) String id) {
        Result<MarketingDiscount> result = new Result<MarketingDiscount>();
        MarketingDiscount MarketingDiscount = marketingDiscountService.getById(id);
        if (MarketingDiscount == null) {
            result.error500("未找到对应实体");
        } else {
            result.setResult(MarketingDiscount);
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
        QueryWrapper<MarketingDiscount> queryWrapper = null;
        try {
            String paramsStr = request.getParameter("paramsStr");
            if (oConvertUtils.isNotEmpty(paramsStr)) {
                String deString = URLDecoder.decode(paramsStr, "UTF-8");
                MarketingDiscount MarketingDiscount = JSON.parseObject(deString, MarketingDiscount.class);
                queryWrapper = QueryGenerator.initQueryWrapper(MarketingDiscount, request.getParameterMap());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //Step.2 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        List<MarketingDiscount> pageList = marketingDiscountService.list(queryWrapper);
        //导出文件名称
        mv.addObject(NormalExcelConstants.FILE_NAME, "优惠券列表");
        mv.addObject(NormalExcelConstants.CLASS, MarketingDiscount.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("优惠券列表数据", "导出人:Jeecg", "导出信息"));
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
                List<MarketingDiscount> listMarketingDiscounts = ExcelImportUtil.importExcel(file.getInputStream(), MarketingDiscount.class, params);
                marketingDiscountService.saveBatch(listMarketingDiscounts);
                return Result.ok("文件导入成功！数据行数:" + listMarketingDiscounts.size());
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
     * 根据id修改启停用状态和说明
     *
     * @param id
     * @param stopExplain
     * @return
     */
    @AutoLog(value = "优惠券-修改停用状态")
    @ApiOperation(value = "优惠券-修改停用状态", notes = "优惠券-修改停用状态")
    @RequestMapping(value = "updateStatusById", method = RequestMethod.GET)
    public Result<MarketingDiscount> updateStatusById(@RequestParam(name = "id", required = true) String id,
                                                      @RequestParam(name = "stopExplain", required = true) String stopExplain) {

        Result<MarketingDiscount> marketingDiscountResult = new Result<>();
        MarketingDiscount byId = marketingDiscountService.getById(id);
        if (byId == null) {
            marketingDiscountResult.error500("未找到实体");
        } else {
            if ("1".equals(byId.getStatus())) {
                byId.setStatus("0");
                byId.setStopExplain(stopExplain);
            } else {
                byId.setStatus("1");
                byId.setStopExplain("");
            }
        }
        boolean b = marketingDiscountService.updateById(byId);
        if (b) {
            marketingDiscountResult.success("修改成功");
        } else {
            marketingDiscountResult.error500("修改失败");
        }

        return marketingDiscountResult;
    }

    /**
     * 删除并添加删除说明
     *
     * @param id
     * @param delExplain
     * @return
     */
    @AutoLog(value = "优惠券-删除and说明")
    @ApiOperation(value = "优惠券-删除and说明", notes = "优惠券-删除and说明")
    @RequestMapping(value = "delEXplainById", method = RequestMethod.DELETE)
    public Result delEXplainById(@RequestParam(name = "id", required = true) String id,
                                 @RequestParam(name = "delExplain", required = true) String delExplain) {
        return Result.ok(marketingDiscountService.renoveById(id, delExplain));
    }

    @AutoLog(value = "优惠券-根据id查询出对应的平台商品(返显)")
    @ApiOperation(value = "优惠券-根据id查询出对应的平台商品", notes = "优惠券-根据id查询出对应的平台商品")
    @RequestMapping(value = "findByMarketingDiscount", method = RequestMethod.GET)
    public Result<Map<String, Object>> findByMarketingDiscount(MarketingDisountGoodVO marketingDisountGoodVO) {
        Result<Map<String, Object>> result = new Result<>();
        String marketingDiscountId = marketingDisountGoodVO.getMarketingDiscountId();
        List<MarketingDisountGoodDTO> good = marketingDiscountGoodService.findGood(marketingDiscountId);
        HashMap<String, Object> map = new HashMap<>();
        map.put("records", good);
        result.setSuccess(true);
        result.setResult(map);
        return result;
    }

    @AutoLog(value = "优惠券-根据id查询出对应的店铺商品(返显)")
    @ApiOperation(value = "优惠券-根据id查询出对应的店铺商品", notes = "优惠券-根据id查询出对应的店铺商品")
    @RequestMapping(value = "findByMarketingDiscountStore", method = RequestMethod.GET)
    public Result<Map<String, Object>> findByMarketingDiscountStore(MarketingDisountGoodVO marketingDisountGoodVO) {
        Result<Map<String, Object>> result = new Result<>();
        String marketingDiscountId = marketingDisountGoodVO.getMarketingDiscountId();
        List<MarketingDisountGoodDTO> storeGood = marketingDiscountGoodService.findStoreGood(marketingDiscountId);
        HashMap<String, Object> map = new HashMap<>();
        map.put("records", storeGood);
        result.setSuccess(true);
        result.setResult(map);
        return result;
    }

    @AutoLog(value = "优惠券-返显")
    @ApiOperation(value = "优惠券-返显", notes = "优惠券-返显")
    @RequestMapping(value = "findMarketingDiscountDTO", method = RequestMethod.GET)
    public MarketingDiscountDTO findMarketingDiscountDTO(@RequestParam(value = "id", required = true) String id) {
        MarketingDiscountDTO marketingDiscountDTO = marketingDiscountService.findMarketingDiscountDTO(id);
        return marketingDiscountDTO;
    }

    @AutoLog(value = "优惠券-发券渠道返显")
    @ApiOperation(value = "优惠券-发券渠道返显", notes = "优惠券-发券渠道返显")
    @RequestMapping(value = "findChannelByDiscountId", method = RequestMethod.GET)
    public List<MarketingChannelDiscount> findChannelByDiscountId(@RequestParam(value = "id", required = true) String id) {
        QueryWrapper<MarketingChannelDiscount> marketingChannelDiscountQueryWrapper = new QueryWrapper<>();
        marketingChannelDiscountQueryWrapper.eq("marketing_discount_id", id);
        List<MarketingChannelDiscount> list = marketingChannelDiscountService.list(marketingChannelDiscountQueryWrapper);
        return list;
    }

    @AutoLog(value = "优惠券-礼包添加优惠券调用接口")
    @ApiOperation(value = "优惠券-礼包添加优惠券调用接口", notes = "优惠券-礼包添加优惠券调用接口")
    @RequestMapping(value = "findMarketingDiscountVO", method = RequestMethod.GET)
    public Result<Map<String, Object>> findMarketingDiscountVO(MarketingDiscountVO marketingDiscountVO) {
        Result<Map<String, Object>> result = new Result<>();
        List<MarketingDiscountVO> marketingDiscountVOList = marketingDiscountService.findMarketingDiscountVO(marketingDiscountVO);
        HashMap<String, Object> map = new HashMap<>();
        map.put("records", marketingDiscountVOList);
        result.setSuccess(true);
        result.setResult(map);
        return result;
    }


    /**
     * 优惠券选择
     *
     * @param marketingDiscountVO
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping("findMarketingDiscountVOPage")
    public Result<?> findMarketingDiscountVOPage(MarketingDiscountVO marketingDiscountVO,  @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
      return Result.ok(marketingDiscountService.findMarketingDiscountVO(new Page<>(pageNo,pageSize),marketingDiscountVO));
    }



    /**
     * 复制优惠券地址
     * @param marketingDiscountId
     * @return
     */
    @AutoLog(value = "优惠券-复制优惠券地址")
    @ApiOperation(value = "优惠券-复制优惠券地址", notes = "优惠券-复制优惠券地址")
    @GetMapping(value = "/getMarketingDiscountUrl")
    public  Result<String> getMarketingDiscountUrl(String marketingDiscountId) {
        Result<String> result = new Result<>();
        //pages/index/coupon/couponDetail?id=b08262801d0f85d9240f6c608ef15468&path=normal&isPlatform=0
        String url ="pages/index/coupon/couponDetail?id="+marketingDiscountId+"&path=normal";
        result.setResult(url);
        result.setSuccess(true);
        return result;
    }
    public Result<MarketingDiscountDTO>getMarketingDisount(@RequestParam(value = "id",required = true)String id){
        Result<MarketingDiscountDTO> result = new Result<>();
        MarketingDiscountDTO marketingDiscountDTO = new MarketingDiscountDTO();
        MarketingDiscount marketingDiscount = marketingDiscountService.getById(id);
        BeanUtils.copyProperties(marketingDiscount,marketingDiscountDTO);
        String mainPicture = marketingDiscount.getMainPicture();
        if (StringUtils.isNotBlank(mainPicture)){
            JSONArray objects = JSONArray.parseArray(mainPicture);
            for (int i = 0; i < objects.size(); i++) {
                String[] strings = new String[i];
            }
        }
        return result;
    }
    @AutoLog(value = "优惠券-赠送优惠券集合")
    @ApiOperation(value = "优惠券-赠送优惠券集合", notes = "优惠券-赠送优惠券集合")
    @RequestMapping(value = "findGiveMarketingDiscountVO", method = RequestMethod.GET)
    public Result<Map<String, Object>> findGiveMarketingDiscountVO(MarketingDiscountVO marketingDiscountVO) {
        Result<Map<String, Object>> result = new Result<>();
        List<MarketingDiscountVO> marketingDiscountVOList = marketingDiscountService.findGiveMarketingDiscountVO(marketingDiscountVO);
        HashMap<String, Object> map = new HashMap<>();
        map.put("records", marketingDiscountVOList);
        result.setSuccess(true);
        result.setResult(map);
        return result;
    }
    @PostMapping("updateById")
    public Result<?> updateById(@RequestBody MarketingDiscount marketingDiscount){
        marketingDiscountService.updateById(marketingDiscount);
        return Result.ok("操作成功!");
    }
}
