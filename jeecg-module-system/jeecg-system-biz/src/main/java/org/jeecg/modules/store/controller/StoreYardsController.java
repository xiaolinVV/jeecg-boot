package org.jeecg.modules.store.controller;

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
import org.jeecg.common.util.OrderNoUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.config.jwt.utils.WeixinQRUtils;
import org.jeecg.modules.store.entity.StoreYards;
import org.jeecg.modules.store.service.IStoreYardsService;
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
 * @Description: 店铺码
 * @Author: jeecg-boot
 * @Date:   2022-06-25
 * @Version: V1.0
 */
@Slf4j
@Api(tags="店铺码")
@RestController
@RequestMapping("/store/storeYards")
public class StoreYardsController {
	@Autowired
	private IStoreYardsService storeYardsService;

	 @Autowired
	 private WeixinQRUtils weixinQRUtils;
	
	/**
	  * 分页列表查询
	 * @param storeYards
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<?> queryPageList(StoreYards storeYards,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<Map<String,Object>>> result = new Result<IPage<Map<String,Object>>>();
		QueryWrapper<StoreYards> queryWrapper = QueryGenerator.initQueryWrapper(storeYards, req.getParameterMap());
		IPage<Map<String,Object>> pageList = storeYardsService.queryPageList(new Page<>(pageNo,pageSize), queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}

	/**
	  *   添加
	 * @param qrCodeCount
	 * @return
	 */
	@GetMapping(value = "/add")
	public Result<StoreYards> add(int qrCodeCount) {
		Result<StoreYards> result = new Result<StoreYards>();
		try {
				while (qrCodeCount>0) {
					StoreYards storeYards = new StoreYards();
					storeYards.setSerialNumber(OrderNoUtils.getOrderNo());
					if(storeYardsService.save(storeYards)){
						storeYards.setQrCode(weixinQRUtils.getQrCodeByPage(storeYards.getId(),"activeAction/pages/shopIndex/shopIndex"));
						storeYardsService.saveOrUpdate(storeYards);
					}
					log.info("生成店铺二维码："+JSON.toJSONString(storeYards));
					qrCodeCount--;
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
	 * @param storeYards
	 * @return
	 */
	@AutoLog(value = "店铺码-编辑")
	@ApiOperation(value="店铺码-编辑", notes="店铺码-编辑")
	@PutMapping(value = "/edit")
	public Result<StoreYards> edit(@RequestBody StoreYards storeYards) {
		Result<StoreYards> result = new Result<StoreYards>();
		StoreYards storeYardsEntity = storeYardsService.getById(storeYards.getId());
		if(storeYardsEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = storeYardsService.updateById(storeYards);
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
	@AutoLog(value = "店铺码-通过id删除")
	@ApiOperation(value="店铺码-通过id删除", notes="店铺码-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			storeYardsService.removeById(id);
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
	@AutoLog(value = "店铺码-批量删除")
	@ApiOperation(value="店铺码-批量删除", notes="店铺码-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<StoreYards> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<StoreYards> result = new Result<StoreYards>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.storeYardsService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "店铺码-通过id查询")
	@ApiOperation(value="店铺码-通过id查询", notes="店铺码-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<StoreYards> queryById(@RequestParam(name="id",required=true) String id) {
		Result<StoreYards> result = new Result<StoreYards>();
		StoreYards storeYards = storeYardsService.getById(id);
		if(storeYards==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(storeYards);
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
      QueryWrapper<StoreYards> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              StoreYards storeYards = JSON.parseObject(deString, StoreYards.class);
              queryWrapper = QueryGenerator.initQueryWrapper(storeYards, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<StoreYards> pageList = storeYardsService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "店铺码列表");
      mv.addObject(NormalExcelConstants.CLASS, StoreYards.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("店铺码列表数据", "导出人:Jeecg", "导出信息"));
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
              List<StoreYards> listStoreYardss = ExcelImportUtil.importExcel(file.getInputStream(), StoreYards.class, params);
              storeYardsService.saveBatch(listStoreYardss);
              return Result.ok("文件导入成功！数据行数:" + listStoreYardss.size());
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
