package org.jeecg.modules.marketing.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.OrderNoUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.marketing.entity.MarketingLiveStreaming;
import org.jeecg.modules.marketing.service.IMarketingLiveStreamingService;
import org.jeecg.utils.tengxun.LiveStreamingUtils;
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
 * @Description: 直播管理
 * @Author: jeecg-boot
 * @Date: 2021-04-24
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "直播管理")
@RestController
@RequestMapping("/marketing/marketingLiveStreaming")
public class MarketingLiveStreamingController {
    @Autowired
    private IMarketingLiveStreamingService marketingLiveStreamingService;

    @Autowired
    private LiveStreamingUtils liveStreamingUtils;

    /**
     * 分页列表查询
     *
     * @param marketingLiveStreaming
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "直播管理-分页列表查询")
    @ApiOperation(value = "直播管理-分页列表查询", notes = "直播管理-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<MarketingLiveStreaming>> queryPageList(MarketingLiveStreaming marketingLiveStreaming,
                                                               @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                               @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                               HttpServletRequest req) {
        Result<IPage<MarketingLiveStreaming>> result = new Result<IPage<MarketingLiveStreaming>>();
        QueryWrapper<MarketingLiveStreaming> queryWrapper = QueryGenerator.initQueryWrapper(marketingLiveStreaming, req.getParameterMap());
        Page<MarketingLiveStreaming> page = new Page<MarketingLiveStreaming>(pageNo, pageSize);
        IPage<MarketingLiveStreaming> pageList = marketingLiveStreamingService.page(page, queryWrapper);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 添加
     *
     * @param marketingLiveStreaming
     * @return
     */
    @AutoLog(value = "直播管理-添加")
    @ApiOperation(value = "直播管理-添加", notes = "直播管理-添加")
    @PostMapping(value = "/add")
    public Result<MarketingLiveStreaming> add(@RequestBody MarketingLiveStreaming marketingLiveStreaming) {
        Result<MarketingLiveStreaming> result = new Result<MarketingLiveStreaming>();
        try {
            if (marketingLiveStreaming.getEndTime() == null) {
                return result.error500("结束时间不能为空");
            }
            //生成直播编号
            marketingLiveStreaming.setStreamNumber(OrderNoUtils.getOrderNo());
            //生成播放地址
            Map<String, String> pullMap = liveStreamingUtils.getPullAddess(marketingLiveStreaming.getStreamNumber());
            marketingLiveStreaming.setRtmpBroadcastAddress(pullMap.get("rtmpBroadcastAddress"));
            marketingLiveStreaming.setFlvBroadcastAddress(pullMap.get("flvBroadcastAddress"));
            marketingLiveStreaming.setHlsBroadcastAddress(pullMap.get("hlsBroadcastAddress"));
            marketingLiveStreaming.setUdpBroadcastAddress(pullMap.get("udpBroadcastAddress"));
            //生成推流地址
            Map<String, String> pushMap = liveStreamingUtils.getPushAddress(marketingLiveStreaming.getStreamNumber(), marketingLiveStreaming.getEndTime().getTime());
            marketingLiveStreaming.setPushStreamAddress(pushMap.get("pushStreamAddress"));
            marketingLiveStreaming.setObsStreamAddress(pushMap.get("obsStreamAddress"));
            marketingLiveStreaming.setObsStreamName(pushMap.get("obsStreamName"));
            marketingLiveStreaming.setStreamEndTime(marketingLiveStreaming.getEndTime());
            marketingLiveStreamingService.save(marketingLiveStreaming);
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
     * @param marketingLiveStreaming
     * @return
     */
    @AutoLog(value = "直播管理-编辑")
    @ApiOperation(value = "直播管理-编辑", notes = "直播管理-编辑")
    @PutMapping(value = "/edit")
    public Result<MarketingLiveStreaming> edit(@RequestBody MarketingLiveStreaming marketingLiveStreaming) {
        Result<MarketingLiveStreaming> result = new Result<MarketingLiveStreaming>();
        MarketingLiveStreaming marketingLiveStreamingEntity = marketingLiveStreamingService.getById(marketingLiveStreaming.getId());
        if (marketingLiveStreamingEntity == null) {
            result.error500("未找到对应实体");
        } else {
            if (marketingLiveStreaming.getEndTime() == null) {
                return result.error500("结束时间不能为空");
            }
            if (marketingLiveStreaming.getEndTime() != null && marketingLiveStreamingEntity.getEndTime().getTime() != marketingLiveStreaming.getEndTime().getTime()) {
                //生成推流地址
                log.info("修改了结束时间，重新生成地址");
                Map<String, String> pushMap = liveStreamingUtils.getPushAddress(marketingLiveStreaming.getStreamNumber(), marketingLiveStreaming.getEndTime().getTime());
                marketingLiveStreaming.setPushStreamAddress(pushMap.get("pushStreamAddress"));
                marketingLiveStreaming.setObsStreamAddress(pushMap.get("obsStreamAddress"));
                marketingLiveStreaming.setObsStreamName(pushMap.get("obsStreamName"));
                marketingLiveStreaming.setStreamEndTime(marketingLiveStreaming.getEndTime());
            }
            boolean ok = marketingLiveStreamingService.updateById(marketingLiveStreaming);
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
    @AutoLog(value = "直播管理-通过id删除")
    @ApiOperation(value = "直播管理-通过id删除", notes = "直播管理-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        try {
            marketingLiveStreamingService.removeById(id);
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
    @AutoLog(value = "直播管理-批量删除")
    @ApiOperation(value = "直播管理-批量删除", notes = "直播管理-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<MarketingLiveStreaming> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        Result<MarketingLiveStreaming> result = new Result<MarketingLiveStreaming>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别！");
        } else {
            this.marketingLiveStreamingService.removeByIds(Arrays.asList(ids.split(",")));
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
    @AutoLog(value = "直播管理-通过id查询")
    @ApiOperation(value = "直播管理-通过id查询", notes = "直播管理-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<MarketingLiveStreaming> queryById(@RequestParam(name = "id", required = true) String id) {
        Result<MarketingLiveStreaming> result = new Result<MarketingLiveStreaming>();
        MarketingLiveStreaming marketingLiveStreaming = marketingLiveStreamingService.getById(id);
        if (marketingLiveStreaming == null) {
            result.error500("未找到对应实体");
        } else {
            result.setResult(marketingLiveStreaming);
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
        QueryWrapper<MarketingLiveStreaming> queryWrapper = null;
        try {
            String paramsStr = request.getParameter("paramsStr");
            if (oConvertUtils.isNotEmpty(paramsStr)) {
                String deString = URLDecoder.decode(paramsStr, "UTF-8");
                MarketingLiveStreaming marketingLiveStreaming = JSON.parseObject(deString, MarketingLiveStreaming.class);
                queryWrapper = QueryGenerator.initQueryWrapper(marketingLiveStreaming, request.getParameterMap());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //Step.2 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        List<MarketingLiveStreaming> pageList = marketingLiveStreamingService.list(queryWrapper);
        //导出文件名称
        mv.addObject(NormalExcelConstants.FILE_NAME, "直播管理列表");
        mv.addObject(NormalExcelConstants.CLASS, MarketingLiveStreaming.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("直播管理列表数据", "导出人:Jeecg", "导出信息"));
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
                List<MarketingLiveStreaming> listMarketingLiveStreamings = ExcelImportUtil.importExcel(file.getInputStream(), MarketingLiveStreaming.class, params);
                marketingLiveStreamingService.saveBatch(listMarketingLiveStreamings);
                return Result.ok("文件导入成功！数据行数:" + listMarketingLiveStreamings.size());
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
     * 通过编号获取直播间
     * @param streamingStreamNumber
     * @return
     */
    @GetMapping("getMarketingLiveStreamingByStreamingStreamNumber")
    public Result<?> getMarketingLiveStreamingById(@RequestParam(name = "streamingStreamNumber", required = true) String streamingStreamNumber) {
            return Result.ok(marketingLiveStreamingService.getOne(new LambdaQueryWrapper<MarketingLiveStreaming>()
                    .eq(MarketingLiveStreaming::getStreamNumber,streamingStreamNumber)
                    .last("limit 1")
            ));
    }
}
