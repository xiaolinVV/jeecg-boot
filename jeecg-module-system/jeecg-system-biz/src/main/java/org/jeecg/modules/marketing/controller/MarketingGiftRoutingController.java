package org.jeecg.modules.marketing.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.marketing.entity.MarketingGiftRouting;
import org.jeecg.modules.marketing.service.IMarketingGiftRoutingService;
import org.jeecg.modules.pay.utils.HftxPayUtils;
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
 * @Description: 礼包分账
 * @Author: jeecg-boot
 * @Date:   2022-05-26
 * @Version: V1.0
 */
@Slf4j
@Api(tags="礼包分账")
@RestController
@RequestMapping("/marketing/marketingGiftRouting")
public class MarketingGiftRoutingController {
	@Autowired
	private IMarketingGiftRoutingService marketingGiftRoutingService;

	@Autowired
	private HftxPayUtils hftxPayUtils;
	
	/**
	  * 分页列表查询
	 * @param marketingGiftRouting
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "礼包分账-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<MarketingGiftRouting>> queryPageList(MarketingGiftRouting marketingGiftRouting,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<MarketingGiftRouting>> result = new Result<IPage<MarketingGiftRouting>>();
		QueryWrapper<MarketingGiftRouting> queryWrapper = QueryGenerator.initQueryWrapper(marketingGiftRouting, req.getParameterMap());
		Page<MarketingGiftRouting> page = new Page<MarketingGiftRouting>(pageNo, pageSize);
		IPage<MarketingGiftRouting> pageList = marketingGiftRoutingService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param marketingGiftRouting
	 * @return
	 */
	@AutoLog(value = "礼包分账-添加")
	@PostMapping(value = "/add")
	public Result<MarketingGiftRouting> add(@RequestBody MarketingGiftRouting marketingGiftRouting) {
		Result<MarketingGiftRouting> result = new Result<MarketingGiftRouting>();
		try {
			if(marketingGiftRoutingService.save(marketingGiftRouting)){
				//新增会员信息和结算账户对象
				if(hftxPayUtils.createMemberPrivate(marketingGiftRouting.getId(),marketingGiftRouting.getRealName())){
					//新增结算账户信息
					Map<String,Object> paramMap= Maps.newHashMap();
					paramMap.put("card_id",marketingGiftRouting.getBankCard());
					paramMap.put("card_name",marketingGiftRouting.getRealName());
					paramMap.put("cert_id",marketingGiftRouting.getIdNumber());
					paramMap.put("tel_no",marketingGiftRouting.getPhone());
					paramMap.put("bank_acct_type","2");
					Map<String,Object> resultMap=hftxPayUtils.createSettleAccountPrivate(marketingGiftRouting.getId(),paramMap);
					if(resultMap.get("status").toString().equals("succeeded")){
						marketingGiftRouting.setSettleAccount(resultMap.get("id").toString());
						marketingGiftRoutingService.saveOrUpdate(marketingGiftRouting);
					}else{
						result.error500("分账设置失败");
					}
				}else{
					result.error500("分账设置失败");
				}
			}
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param marketingGiftRouting
	 * @return
	 */
	@AutoLog(value = "礼包分账-编辑")
	@PutMapping(value = "/edit")
	public Result<MarketingGiftRouting> edit(@RequestBody MarketingGiftRouting marketingGiftRouting) {
		Result<MarketingGiftRouting> result = new Result<MarketingGiftRouting>();
		MarketingGiftRouting marketingGiftRoutingEntity = marketingGiftRoutingService.getById(marketingGiftRouting.getId());
		if(marketingGiftRoutingEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = marketingGiftRoutingService.updateById(marketingGiftRouting);
			//TODO 返回false说明什么？
			if(ok) {
				//修改结算账户对象
				Map<String,Object> paramMap= Maps.newHashMap();
				paramMap.put("card_id",marketingGiftRouting.getBankCard());
				paramMap.put("card_name",marketingGiftRouting.getRealName());
				paramMap.put("cert_id",marketingGiftRouting.getIdNumber());
				paramMap.put("tel_no",marketingGiftRouting.getPhone());
				paramMap.put("bank_acct_type","2");
				Map<String,Object> resultMap=hftxPayUtils.updateSettleAccountPrivate(marketingGiftRouting.getId(),paramMap,marketingGiftRouting.getSettleAccount());
				if(resultMap.get("status").toString().equals("succeeded")){
					marketingGiftRouting.setSettleAccount(resultMap.get("id").toString());
					marketingGiftRoutingService.saveOrUpdate(marketingGiftRouting);
				}else{
					result.error500("分账设置失败");
				}
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
	@AutoLog(value = "礼包分账-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			marketingGiftRoutingService.removeById(id);
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
	@AutoLog(value = "礼包分账-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<MarketingGiftRouting> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<MarketingGiftRouting> result = new Result<MarketingGiftRouting>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.marketingGiftRoutingService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "礼包分账-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<MarketingGiftRouting> queryById(@RequestParam(name="id",required=true) String id) {
		Result<MarketingGiftRouting> result = new Result<MarketingGiftRouting>();
		MarketingGiftRouting marketingGiftRouting = marketingGiftRoutingService.getById(id);
		if(marketingGiftRouting==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(marketingGiftRouting);
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
      QueryWrapper<MarketingGiftRouting> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              MarketingGiftRouting marketingGiftRouting = JSON.parseObject(deString, MarketingGiftRouting.class);
              queryWrapper = QueryGenerator.initQueryWrapper(marketingGiftRouting, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<MarketingGiftRouting> pageList = marketingGiftRoutingService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "礼包分账列表");
      mv.addObject(NormalExcelConstants.CLASS, MarketingGiftRouting.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("礼包分账列表数据", "导出人:Jeecg", "导出信息"));
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
              List<MarketingGiftRouting> listMarketingGiftRoutings = ExcelImportUtil.importExcel(file.getInputStream(), MarketingGiftRouting.class, params);
              marketingGiftRoutingService.saveBatch(listMarketingGiftRoutings);
              return Result.ok("文件导入成功！数据行数:" + listMarketingGiftRoutings.size());
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
