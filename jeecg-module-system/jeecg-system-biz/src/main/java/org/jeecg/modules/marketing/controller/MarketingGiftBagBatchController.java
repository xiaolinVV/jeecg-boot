package org.jeecg.modules.marketing.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.marketing.dto.MarketingGiftBagBatchDTO;
import org.jeecg.modules.marketing.entity.MarketingGiftBagBatch;
import org.jeecg.modules.marketing.entity.MarketingGiftBagStoreBatch;
import org.jeecg.modules.marketing.service.IMarketingGiftBagBatchService;
import org.jeecg.modules.marketing.service.IMarketingGiftBagStoreBatchService;
import org.jeecg.modules.marketing.vo.MarketingGiftBagBatchVO;
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
import java.util.Date;
import java.util.List;
import java.util.Map;

 /**
 * @Description: 采购礼包
 * @Author: jeecg-boot
 * @Date:   2020-08-29
 * @Version: V1.0
 */
@Slf4j
@Api(tags="采购礼包")
@RestController
@RequestMapping("/marketingGiftBagBatch/marketingGiftBagBatch")
public class MarketingGiftBagBatchController {
	@Autowired
	private IMarketingGiftBagBatchService marketingGiftBagBatchService;

	@Autowired
	private IMarketingGiftBagStoreBatchService iMarketingGiftBagStoreBatchService;

	/**
	  * 分页列表查询
	 * @param marketingGiftBagBatchDTO
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "采购礼包-分页列表查询")
	@ApiOperation(value="采购礼包-分页列表查询", notes="采购礼包-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<MarketingGiftBagBatchVO>> queryPageList(MarketingGiftBagBatchDTO marketingGiftBagBatchDTO,
															  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
															  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
															  HttpServletRequest req) {
		Result<IPage<MarketingGiftBagBatchVO>> result = new Result<IPage<MarketingGiftBagBatchVO>>();
		Page<MarketingGiftBagBatchVO> page = new Page<MarketingGiftBagBatchVO>(pageNo, pageSize);
		IPage<MarketingGiftBagBatchVO> pageList = marketingGiftBagBatchService.queryPageList(page, marketingGiftBagBatchDTO);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param marketingGiftBagBatchVO
	 * @return
	 */
	@AutoLog(value = "采购礼包-添加")
	@ApiOperation(value="采购礼包-添加", notes="采购礼包-添加")
	@PostMapping(value = "/add")
	public Result<MarketingGiftBagBatch> add(@RequestBody MarketingGiftBagBatchVO marketingGiftBagBatchVO) {
		Result<MarketingGiftBagBatch> result = new Result<MarketingGiftBagBatch>();
		MarketingGiftBagBatch marketingGiftBagBatch = new MarketingGiftBagBatch();
		BeanUtils.copyProperties(marketingGiftBagBatchVO,marketingGiftBagBatch);
		boolean b = marketingGiftBagBatchService.save(marketingGiftBagBatch);
		if (StringUtils.isNotBlank(marketingGiftBagBatchVO.getStoreIds())){
			Arrays.asList(StringUtils.split(marketingGiftBagBatchVO.getStoreIds(), ",")).forEach(mg->{
				iMarketingGiftBagStoreBatchService.save(new MarketingGiftBagStoreBatch()
						.setDelFlag("0")
						.setMarketingGiftBagBatchId(marketingGiftBagBatch.getId())
						.setSysUserId(mg)
				);
			});
		}
		if (b){
			result.success("修改成功!");
		}else {
			result.error500("修改失败!");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param marketingGiftBagBatchVO
	 * @return
	 */
	@AutoLog(value = "采购礼包-编辑")
	@ApiOperation(value="采购礼包-编辑", notes="采购礼包-编辑")
	@PutMapping(value = "/edit")
	public Result<MarketingGiftBagBatch> edit(@RequestBody MarketingGiftBagBatchVO marketingGiftBagBatchVO) {
		Result<MarketingGiftBagBatch> result = new Result<MarketingGiftBagBatch>();
		MarketingGiftBagBatch marketingGiftBagBatchEntity = marketingGiftBagBatchService.getById(marketingGiftBagBatchVO.getId());
		if(marketingGiftBagBatchEntity==null) {
			result.error500("未找到对应实体");
		}else {
			LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
			BeanUtils.copyProperties(marketingGiftBagBatchVO,marketingGiftBagBatchEntity);
			boolean ok = marketingGiftBagBatchService.updateById(marketingGiftBagBatchEntity
					.setUpdateBy(sysUser.getRealname())
					.setUpdateTime(new Date())
			);
			if (StringUtils.isBlank(marketingGiftBagBatchVO.getStoreIds())){
                iMarketingGiftBagStoreBatchService.remove(new LambdaQueryWrapper<MarketingGiftBagStoreBatch>()
                        .eq(MarketingGiftBagStoreBatch::getDelFlag,"0")
                        .eq(MarketingGiftBagStoreBatch::getMarketingGiftBagBatchId,marketingGiftBagBatchEntity.getId())
                );
                Arrays.asList(StringUtils.split(marketingGiftBagBatchVO.getStoreIds(), ",")).forEach(mg->{
                    iMarketingGiftBagStoreBatchService.save(new MarketingGiftBagStoreBatch()
                            .setDelFlag("0")
                            .setMarketingGiftBagBatchId(marketingGiftBagBatchEntity.getId())
                            .setSysUserId(mg)
                    );
                });
			}else {
			    iMarketingGiftBagStoreBatchService.remove(new LambdaQueryWrapper<MarketingGiftBagStoreBatch>()
                        .eq(MarketingGiftBagStoreBatch::getDelFlag,"0")
                        .eq(MarketingGiftBagStoreBatch::getMarketingGiftBagBatchId,marketingGiftBagBatchEntity.getId())
                );
            }
			if(ok) {
				result.success("修改成功!");
			}else {
				result.error500("修改失败!");
			}
		}
		
		return result;
	}
	
	/**
	  *   通过id删除
	 * @param id
	 * @return
	 */
	@AutoLog(value = "采购礼包-通过id删除")
	@ApiOperation(value="采购礼包-通过id删除", notes="采购礼包-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			marketingGiftBagBatchService.removeById(id);
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
	@AutoLog(value = "采购礼包-批量删除")
	@ApiOperation(value="采购礼包-批量删除", notes="采购礼包-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<MarketingGiftBagBatch> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<MarketingGiftBagBatch> result = new Result<MarketingGiftBagBatch>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.marketingGiftBagBatchService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "采购礼包-通过id查询")
	@ApiOperation(value="采购礼包-通过id查询", notes="采购礼包-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<MarketingGiftBagBatch> queryById(@RequestParam(name="id",required=true) String id) {
		Result<MarketingGiftBagBatch> result = new Result<MarketingGiftBagBatch>();
		MarketingGiftBagBatch marketingGiftBagBatch = marketingGiftBagBatchService.getById(id);
		if(marketingGiftBagBatch==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(marketingGiftBagBatch);
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
      QueryWrapper<MarketingGiftBagBatch> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              MarketingGiftBagBatch marketingGiftBagBatch = JSON.parseObject(deString, MarketingGiftBagBatch.class);
              queryWrapper = QueryGenerator.initQueryWrapper(marketingGiftBagBatch, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<MarketingGiftBagBatch> pageList = marketingGiftBagBatchService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "采购礼包列表");
      mv.addObject(NormalExcelConstants.CLASS, MarketingGiftBagBatch.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("采购礼包列表数据", "导出人:Jeecg", "导出信息"));
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
              List<MarketingGiftBagBatch> listMarketingGiftBagBatchs = ExcelImportUtil.importExcel(file.getInputStream(), MarketingGiftBagBatch.class, params);
              marketingGiftBagBatchService.saveBatch(listMarketingGiftBagBatchs);
              return Result.ok("文件导入成功！数据行数:" + listMarketingGiftBagBatchs.size());
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

  @PostMapping("updateMarketingGiftBagBatch")
  public Result<MarketingGiftBagBatch> updateMarketingGiftBagBatch(@RequestBody MarketingGiftBagBatch marketingGiftBagBatch){
      Result<MarketingGiftBagBatch> result = new Result<>();
      boolean b = marketingGiftBagBatchService.updateById(marketingGiftBagBatch);
      if (b){
          result.success("修改成功");
      }else {
          result.error500("修改失败");
      }
      return result;
  }

  @GetMapping("findMarketingGiftBagBatch")
  public Result<IPage<Map<String,Object>>> findMarketingGiftBagBatch(MarketingGiftBagBatchDTO marketingGiftBagBatchDTO,
																	 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
																	 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize){
	  Result<IPage<Map<String,Object>>> result = new Result<>();
	  Page<Map<String, Object>> page = new Page<Map<String, Object>>(pageNo, pageSize);
	  result.setResult(marketingGiftBagBatchService.findMarketingGiftBagBatch(page,marketingGiftBagBatchDTO));
	  return result;
  }
}
