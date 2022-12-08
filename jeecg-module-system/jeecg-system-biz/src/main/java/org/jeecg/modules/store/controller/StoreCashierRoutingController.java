package org.jeecg.modules.store.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.pay.utils.HftxPayUtils;
import org.jeecg.modules.store.dto.StoreCashierRoutingDTO;
import org.jeecg.modules.store.entity.StoreCashierRouting;
import org.jeecg.modules.store.service.IStoreCashierRoutingService;
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
 * @Description: 店铺收银分账设置
 * @Author: jeecg-boot
 * @Date:   2022-06-06
 * @Version: V1.0
 */
@Slf4j
@Api(tags="店铺收银分账设置")
@RestController
@RequestMapping("/store/storeCashierRouting")
public class StoreCashierRoutingController {
	@Autowired
	private IStoreCashierRoutingService storeCashierRoutingService;

	 @Autowired
	 private HftxPayUtils hftxPayUtils;

	/**
	  * 分页列表查询
	 * @param storeCashierRouting
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "店铺收银分账设置-分页列表查询")
	@ApiOperation(value="店铺收银分账设置-分页列表查询", notes="店铺收银分账设置-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(StoreCashierRouting storeCashierRouting,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<StoreCashierRoutingDTO>> result = new Result<IPage<StoreCashierRoutingDTO>>();
		QueryWrapper<StoreCashierRouting> queryWrapper = QueryGenerator.initQueryWrapper(storeCashierRouting, req.getParameterMap());
		Page<StoreCashierRoutingDTO> page = new Page<StoreCashierRoutingDTO>(pageNo, pageSize);
		IPage<StoreCashierRoutingDTO> pageList = storeCashierRoutingService.queryPageList(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param storeCashierRouting
	 * @return
	 */
	@AutoLog(value = "店铺收银分账设置-添加")
	@ApiOperation(value="店铺收银分账设置-添加", notes="店铺收银分账设置-添加")
	@PostMapping(value = "/add")
	public Result<StoreCashierRouting> add(@RequestBody StoreCashierRouting storeCashierRouting) {
		Result<StoreCashierRouting> result = new Result<StoreCashierRouting>();
		try {
			if(storeCashierRoutingService.save(storeCashierRouting)) {
				if (!storeCashierRouting.getAccountType().equals("2")) {
					//新增会员信息和结算账户对象
					if (hftxPayUtils.createMemberPrivate(storeCashierRouting.getId(), storeCashierRouting.getRealName())) {
						//新增结算账户信息
						Map<String, Object> paramMap = Maps.newHashMap();
						paramMap.put("card_id", storeCashierRouting.getBankCard());
						paramMap.put("card_name", storeCashierRouting.getRealName());
						paramMap.put("cert_id", storeCashierRouting.getIdNumber());
						paramMap.put("tel_no", storeCashierRouting.getPhone());
						paramMap.put("bank_acct_type", "2");
						Map<String, Object> resultMap = hftxPayUtils.createSettleAccountPrivate(storeCashierRouting.getId(), paramMap);
						if (resultMap.get("status").toString().equals("succeeded")) {
							if (StringUtils.isNotBlank(resultMap.get("id").toString())) {
								storeCashierRouting.setSettleAccount(resultMap.get("id").toString());
								storeCashierRoutingService.saveOrUpdate(storeCashierRouting);
							} else {
								result.error500("结算信息有问题");
							}
						} else {
							result.error500("分账设置失败");
						}
					} else {
						result.error500("分账设置失败");
					}
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
	 * @param storeCashierRouting
	 * @return
	 */
	@AutoLog(value = "店铺收银分账设置-编辑")
	@ApiOperation(value="店铺收银分账设置-编辑", notes="店铺收银分账设置-编辑")
	@PutMapping(value = "/edit")
	public Result<StoreCashierRouting> edit(@RequestBody StoreCashierRouting storeCashierRouting) {
		Result<StoreCashierRouting> result = new Result<StoreCashierRouting>();
		StoreCashierRouting storeCashierRoutingEntity = storeCashierRoutingService.getById(storeCashierRouting.getId());
		if(storeCashierRoutingEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = storeCashierRoutingService.updateById(storeCashierRouting);
			//TODO 返回false说明什么？
			if(ok) {
				if (!storeCashierRouting.getAccountType().equals("2")) {
					//修改结算账户对象
					Map<String, Object> paramMap = Maps.newHashMap();
					paramMap.put("card_id", storeCashierRouting.getBankCard());
					paramMap.put("card_name", storeCashierRouting.getRealName());
					paramMap.put("cert_id", storeCashierRouting.getIdNumber());
					paramMap.put("tel_no", storeCashierRouting.getPhone());
					paramMap.put("bank_acct_type", "2");
					Map<String, Object> resultMap = hftxPayUtils.updateSettleAccountPrivate(storeCashierRouting.getId(), paramMap, storeCashierRouting.getSettleAccount());
					if (resultMap.get("status").toString().equals("succeeded")) {
						storeCashierRouting.setSettleAccount(resultMap.get("id").toString());
						storeCashierRoutingService.saveOrUpdate(storeCashierRouting);
					} else {
						result.error500("分账设置失败");
					}
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
	@AutoLog(value = "店铺收银分账设置-通过id删除")
	@ApiOperation(value="店铺收银分账设置-通过id删除", notes="店铺收银分账设置-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			storeCashierRoutingService.removeById(id);
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
	@AutoLog(value = "店铺收银分账设置-批量删除")
	@ApiOperation(value="店铺收银分账设置-批量删除", notes="店铺收银分账设置-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<StoreCashierRouting> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<StoreCashierRouting> result = new Result<StoreCashierRouting>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.storeCashierRoutingService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "店铺收银分账设置-通过id查询")
	@ApiOperation(value="店铺收银分账设置-通过id查询", notes="店铺收银分账设置-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<StoreCashierRouting> queryById(@RequestParam(name="id",required=true) String id) {
		Result<StoreCashierRouting> result = new Result<StoreCashierRouting>();
		StoreCashierRouting storeCashierRouting = storeCashierRoutingService.getById(id);
		if(storeCashierRouting==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(storeCashierRouting);
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
      QueryWrapper<StoreCashierRouting> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              StoreCashierRouting storeCashierRouting = JSON.parseObject(deString, StoreCashierRouting.class);
              queryWrapper = QueryGenerator.initQueryWrapper(storeCashierRouting, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<StoreCashierRouting> pageList = storeCashierRoutingService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "店铺收银分账设置列表");
      mv.addObject(NormalExcelConstants.CLASS, StoreCashierRouting.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("店铺收银分账设置列表数据", "导出人:Jeecg", "导出信息"));
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
              List<StoreCashierRouting> listStoreCashierRoutings = ExcelImportUtil.importExcel(file.getInputStream(), StoreCashierRouting.class, params);
              storeCashierRoutingService.saveBatch(listStoreCashierRoutings);
              return Result.ok("文件导入成功！数据行数:" + listStoreCashierRoutings.size());
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
