package org.jeecg.modules.marketing.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.OrderNoUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.marketing.dto.MarketingGiftBagRecordBatchDTO;
import org.jeecg.modules.marketing.entity.MarketingGiftBagBatch;
import org.jeecg.modules.marketing.entity.MarketingGiftBagRecordBatch;
import org.jeecg.modules.marketing.entity.MarketingGiftBagRecordBatchDelivery;
import org.jeecg.modules.marketing.service.IMarketingGiftBagBatchService;
import org.jeecg.modules.marketing.service.IMarketingGiftBagRecordBatchDeliveryService;
import org.jeecg.modules.marketing.service.IMarketingGiftBagRecordBatchService;
import org.jeecg.modules.marketing.vo.MarketingGiftBagRecordBatchVO;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

 /**
 * @Description: 采购礼包记录
 * @Author: jeecg-boot
 * @Date:   2020-09-01
 * @Version: V1.0
 */
@Slf4j
@Api(tags="采购礼包记录")
@RestController
@RequestMapping("/marketingGiftBagRecordBatch/marketingGiftBagRecordBatch")
public class MarketingGiftBagRecordBatchController {
	@Autowired
	private IMarketingGiftBagRecordBatchService marketingGiftBagRecordBatchService;

	@Autowired
	private IMarketingGiftBagBatchService iMarketingGiftBagBatchService;
	@Autowired
	@Lazy
	private IMarketingGiftBagRecordBatchDeliveryService iMarketingGiftBagRecordBatchDeliveryService;
	/**
	  * 分页列表查询
	 * @param marketingGiftBagRecordBatchDTO
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "采购礼包记录-分页列表查询")
	@ApiOperation(value="采购礼包记录-分页列表查询", notes="采购礼包记录-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<MarketingGiftBagRecordBatchVO>> queryPageList(MarketingGiftBagRecordBatchDTO marketingGiftBagRecordBatchDTO,
																	  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
																	  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
																	  HttpServletRequest req) {
		Result<IPage<MarketingGiftBagRecordBatchVO>> result = new Result<IPage<MarketingGiftBagRecordBatchVO>>();
		Page<MarketingGiftBagRecordBatchVO> page = new Page<MarketingGiftBagRecordBatchVO>(pageNo, pageSize);
		IPage<MarketingGiftBagRecordBatchVO> pageList = marketingGiftBagRecordBatchService.queryPageList(page,marketingGiftBagRecordBatchDTO);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param marketingGiftBagRecordBatch
	 * @return
	 */
	@AutoLog(value = "采购礼包记录-添加")
	@ApiOperation(value="采购礼包记录-添加", notes="采购礼包记录-添加")
	@PostMapping(value = "/add")
	public Result<MarketingGiftBagRecordBatch> add(@RequestBody MarketingGiftBagRecordBatch marketingGiftBagRecordBatch) {
		Result<MarketingGiftBagRecordBatch> result = new Result<MarketingGiftBagRecordBatch>();
		try {
			MarketingGiftBagBatch marketingGiftBagBatch = iMarketingGiftBagBatchService.getById(marketingGiftBagRecordBatch.getMarketingGiftBagBatchId());
			marketingGiftBagRecordBatch
					.setGiftName(marketingGiftBagBatch.getGiftName())
					.setPrice(marketingGiftBagBatch.getPrice())
					.setSendTimes(marketingGiftBagBatch.getSendTimes())
					.setBuyLimit(marketingGiftBagBatch.getBuyLimit())
					.setBuyVipMemberGradeId(marketingGiftBagBatch.getBuyVipMemberGradeId())
					.setViewScope(marketingGiftBagBatch.getViewScope())
					.setStartTime(marketingGiftBagBatch.getStartTime())
					.setEndTime(marketingGiftBagBatch.getEndTime())
					.setMainPicture(marketingGiftBagBatch.getMainPicture())
					.setGiftDeals(marketingGiftBagBatch.getGiftDeals())
					.setCoverPlan(marketingGiftBagBatch.getCoverPlan())
					.setPosters(marketingGiftBagBatch.getPosters())
					.setGiftExplain(marketingGiftBagBatch.getGiftExplain())
					.setGiftNo(OrderNoUtils.getOrderNo())
					.setCustomaryDues(marketingGiftBagRecordBatch.getBuyCount().multiply(marketingGiftBagBatch.getPrice()).add(marketingGiftBagRecordBatch.getShipFee()))
					.setPayStatus("1");
			if (StringUtils.isBlank(marketingGiftBagRecordBatch.getPromoter())){
				marketingGiftBagRecordBatch.setPromoterType("2");
			}
			if (StringUtils.isBlank(marketingGiftBagRecordBatch.getPromoterTwo())){
				marketingGiftBagRecordBatch.setPromoterTypeTwo("2");
			}
			marketingGiftBagRecordBatchService.save(marketingGiftBagRecordBatch);
			ArrayList<MarketingGiftBagRecordBatchDelivery> marketingGiftBagRecordBatchDeliveryArrayList = new ArrayList<>();

			for (int i = 0; i < marketingGiftBagRecordBatch.getSendTimes().intValue(); i++) {
				marketingGiftBagRecordBatchDeliveryArrayList
						.add(new MarketingGiftBagRecordBatchDelivery()
								.setBatchNumber("pc"+i)
								.setPid("0")
								.setMarketingGiftBagRecordBatchId(marketingGiftBagRecordBatch.getId())
						);
			}
			iMarketingGiftBagRecordBatchDeliveryService.saveBatch(marketingGiftBagRecordBatchDeliveryArrayList);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param marketingGiftBagRecordBatch
	 * @return
	 */
	@AutoLog(value = "采购礼包记录-编辑")
	@ApiOperation(value="采购礼包记录-编辑", notes="采购礼包记录-编辑")
	@PutMapping(value = "/edit")
	public Result<MarketingGiftBagRecordBatch> edit(@RequestBody MarketingGiftBagRecordBatch marketingGiftBagRecordBatch) {
		Result<MarketingGiftBagRecordBatch> result = new Result<MarketingGiftBagRecordBatch>();
		MarketingGiftBagRecordBatch marketingGiftBagRecordBatchEntity = marketingGiftBagRecordBatchService.getById(marketingGiftBagRecordBatch.getId());
		if(marketingGiftBagRecordBatchEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = marketingGiftBagRecordBatchService.updateById(marketingGiftBagRecordBatch);
			//TODO 返回false说明什么？
			if(ok) {
				result.success("修改成功!");
			}
		}
		
		return result;
	}
	
	/**
	  *   通过id删除
	 * @param id
	 * @return
	 */
	@AutoLog(value = "采购礼包记录-通过id删除")
	@ApiOperation(value="采购礼包记录-通过id删除", notes="采购礼包记录-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			marketingGiftBagRecordBatchService.removeById(id);
		} catch (Exception e) {
			log.error("删除失败",e.getMessage());
			return Result.error("删除失败!");
		}
		return Result.ok("删除成功!");
	}
	
	/**
	  *  批量删除
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "采购礼包记录-批量删除")
	@ApiOperation(value="采购礼包记录-批量删除", notes="采购礼包记录-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<MarketingGiftBagRecordBatch> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<MarketingGiftBagRecordBatch> result = new Result<MarketingGiftBagRecordBatch>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.marketingGiftBagRecordBatchService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "采购礼包记录-通过id查询")
	@ApiOperation(value="采购礼包记录-通过id查询", notes="采购礼包记录-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<MarketingGiftBagRecordBatch> queryById(@RequestParam(name="id",required=true) String id) {
		Result<MarketingGiftBagRecordBatch> result = new Result<MarketingGiftBagRecordBatch>();
		MarketingGiftBagRecordBatch marketingGiftBagRecordBatch = marketingGiftBagRecordBatchService.getById(id);
		if(marketingGiftBagRecordBatch==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(marketingGiftBagRecordBatch);
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
      QueryWrapper<MarketingGiftBagRecordBatch> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              MarketingGiftBagRecordBatch marketingGiftBagRecordBatch = JSON.parseObject(deString, MarketingGiftBagRecordBatch.class);
              queryWrapper = QueryGenerator.initQueryWrapper(marketingGiftBagRecordBatch, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<MarketingGiftBagRecordBatch> pageList = marketingGiftBagRecordBatchService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "采购礼包记录列表");
      mv.addObject(NormalExcelConstants.CLASS, MarketingGiftBagRecordBatch.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("采购礼包记录列表数据", "导出人:Jeecg", "导出信息"));
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
              List<MarketingGiftBagRecordBatch> listMarketingGiftBagRecordBatchs = ExcelImportUtil
					  .importExcel(file.getInputStream(), MarketingGiftBagRecordBatch.class, params);
              marketingGiftBagRecordBatchService.saveBatch(listMarketingGiftBagRecordBatchs);
              return Result.ok("文件导入成功！数据行数:" + listMarketingGiftBagRecordBatchs.size());
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
