package org.jeecg.modules.marketing.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.marketing.dto.MarketingActivityListRecordDTO;
import org.jeecg.modules.marketing.entity.MarketingActivityListRecord;
import org.jeecg.modules.marketing.service.IMarketingActivityListRecordService;
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
import java.util.List;
import java.util.Map;

 /**
 * @Description: 活动记录
 * @Author: jeecg-boot
 * @Date:   2021-05-17
 * @Version: V1.0
 */
@Slf4j
@Api(tags="活动记录")
@RestController
@RequestMapping("/marketing/marketingActivityListRecord")
public class MarketingActivityListRecordController {
	@Autowired
	private IMarketingActivityListRecordService marketingActivityListRecordService;


     /**
      * 获取活动记录列表
      *
      * @param pageNo
      * @param pageSize
      * @return
      */
	@RequestMapping("getMarketingActivityListRecordList")
    @ResponseBody
	public Result<?> getMarketingActivityListRecordList(MarketingActivityListRecordDTO marketingActivityListRecordDTO,@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                        @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize){


        return Result.ok(marketingActivityListRecordService.getMarketingActivityListRecordList(new Page<>(pageNo,pageSize),marketingActivityListRecordDTO));
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
      QueryWrapper<MarketingActivityListRecord> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              MarketingActivityListRecord marketingActivityListRecord = JSON.parseObject(deString, MarketingActivityListRecord.class);
              queryWrapper = QueryGenerator.initQueryWrapper(marketingActivityListRecord, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<MarketingActivityListRecord> pageList = marketingActivityListRecordService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "活动记录列表");
      mv.addObject(NormalExcelConstants.CLASS, MarketingActivityListRecord.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("活动记录列表数据", "导出人:Jeecg", "导出信息"));
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
              List<MarketingActivityListRecord> listMarketingActivityListRecords = ExcelImportUtil.importExcel(file.getInputStream(), MarketingActivityListRecord.class, params);
              marketingActivityListRecordService.saveBatch(listMarketingActivityListRecords);
              return Result.ok("文件导入成功！数据行数:" + listMarketingActivityListRecords.size());
          } catch (Exception e) {
              log.error(e.getMessage(),e);
              return Result.error("文件导入失败:"+e.getMessage());
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

}
