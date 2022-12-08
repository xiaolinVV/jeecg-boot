package org.jeecg.modules.store.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.store.dto.StoreWelfarePaymentsGatheringDTO;
import org.jeecg.modules.store.entity.StoreManage;
import org.jeecg.modules.store.entity.StoreWelfarePaymentsGathering;
import org.jeecg.modules.store.service.IStoreManageService;
import org.jeecg.modules.store.service.IStoreWelfarePaymentsGatheringService;
import org.jeecg.modules.store.vo.StoreWelfarePaymentsGatheringVO;
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
import java.util.List;
import java.util.Map;

 /**
 * @Description: 福利金收款商家
 * @Author: jeecg-boot
 * @Date:   2020-06-18
 * @Version: V1.0
 */
@Slf4j
@Api(tags="福利金收款商家")
@RestController
@RequestMapping("/storeWelfarePaymentsGathering/storeWelfarePaymentsGathering")
public class StoreWelfarePaymentsGatheringController {
	@Autowired
	private IStoreWelfarePaymentsGatheringService storeWelfarePaymentsGatheringService;
	@Autowired
	private IStoreManageService iStoreManageService;
	
	/**
	  * 分页列表查询
	 * @param
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "福利金收款商家-分页列表查询")
	@ApiOperation(value="福利金收款商家-分页列表查询", notes="福利金收款商家-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<StoreWelfarePaymentsGatheringVO>> queryPageList(StoreWelfarePaymentsGatheringDTO storeWelfarePaymentsGatheringDTO,
																		@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
																		@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
																		HttpServletRequest req) {
		Result<IPage<StoreWelfarePaymentsGatheringVO>> result = new Result<IPage<StoreWelfarePaymentsGatheringVO>>();
		Page<StoreWelfarePaymentsGathering> page = new Page<StoreWelfarePaymentsGathering>(pageNo, pageSize);
		IPage<StoreWelfarePaymentsGatheringVO> pageList = storeWelfarePaymentsGatheringService.queryPageList(page, storeWelfarePaymentsGatheringDTO);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param storeWelfarePaymentsGatheringDTO
	 * @return
	 */
	@AutoLog(value = "福利金收款商家-添加")
	@ApiOperation(value="福利金收款商家-添加", notes="福利金收款商家-添加")
	@PostMapping(value = "/add")
	public Result<StoreWelfarePaymentsGathering> add(@RequestBody StoreWelfarePaymentsGatheringDTO storeWelfarePaymentsGatheringDTO) {
		Result<StoreWelfarePaymentsGathering> result = new Result<StoreWelfarePaymentsGathering>();
		try {
			StoreWelfarePaymentsGathering storeWelfarePaymentsGathering = new StoreWelfarePaymentsGathering();
			BeanUtils.copyProperties(storeWelfarePaymentsGatheringDTO,storeWelfarePaymentsGathering);
			storeWelfarePaymentsGatheringService.save(storeWelfarePaymentsGathering);
			iStoreManageService.update(new StoreManage()
					.setIsOpenWelfarePayments(storeWelfarePaymentsGatheringDTO.getStatus())
					.setStoreTypeId(storeWelfarePaymentsGatheringDTO.getStoreTypeId())
					,new LambdaUpdateWrapper<StoreManage>()
					.eq(StoreManage::getId,storeWelfarePaymentsGathering.getStoreManageId()));
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param storeWelfarePaymentsGatheringDTO
	 * @return
	 */
	@AutoLog(value = "福利金收款商家-编辑")
	@ApiOperation(value="福利金收款商家-编辑", notes="福利金收款商家-编辑")
	@PostMapping(value = "/edit")
	public Result<StoreWelfarePaymentsGathering> edit(@RequestBody StoreWelfarePaymentsGatheringDTO storeWelfarePaymentsGatheringDTO) {
		Result<StoreWelfarePaymentsGathering> result = new Result<StoreWelfarePaymentsGathering>();
		StoreWelfarePaymentsGathering storeWelfarePaymentsGatheringEntity = storeWelfarePaymentsGatheringService.getById(storeWelfarePaymentsGatheringDTO.getId());
		if(storeWelfarePaymentsGatheringEntity==null) {
			result.error500("未找到对应实体");
		}else {
			StoreWelfarePaymentsGathering storeWelfarePaymentsGathering = new StoreWelfarePaymentsGathering();
			BeanUtils.copyProperties(storeWelfarePaymentsGatheringDTO,storeWelfarePaymentsGathering);
			iStoreManageService.update(new StoreManage()
					.setIsOpenWelfarePayments(storeWelfarePaymentsGatheringDTO.getStatus())
					.setStoreTypeId(storeWelfarePaymentsGatheringDTO.getStoreTypeId())
					,new LambdaUpdateWrapper<StoreManage>()
					.eq(StoreManage::getId,storeWelfarePaymentsGathering.getStoreManageId()));
			boolean ok = storeWelfarePaymentsGatheringService.updateById(storeWelfarePaymentsGathering);
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
	@AutoLog(value = "福利金收款商家-通过id删除")
	@ApiOperation(value="福利金收款商家-通过id删除", notes="福利金收款商家-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			storeWelfarePaymentsGatheringService.removeById(id);
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
	@AutoLog(value = "福利金收款商家-批量删除")
	@ApiOperation(value="福利金收款商家-批量删除", notes="福利金收款商家-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<StoreWelfarePaymentsGathering> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<StoreWelfarePaymentsGathering> result = new Result<StoreWelfarePaymentsGathering>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.storeWelfarePaymentsGatheringService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "福利金收款商家-通过id查询")
	@ApiOperation(value="福利金收款商家-通过id查询", notes="福利金收款商家-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<StoreWelfarePaymentsGathering> queryById(@RequestParam(name="id",required=true) String id) {
		Result<StoreWelfarePaymentsGathering> result = new Result<StoreWelfarePaymentsGathering>();
		StoreWelfarePaymentsGathering storeWelfarePaymentsGathering = storeWelfarePaymentsGatheringService.getById(id);
		if(storeWelfarePaymentsGathering==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(storeWelfarePaymentsGathering);
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
      QueryWrapper<StoreWelfarePaymentsGathering> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              StoreWelfarePaymentsGathering storeWelfarePaymentsGathering = JSON.parseObject(deString, StoreWelfarePaymentsGathering.class);
              queryWrapper = QueryGenerator.initQueryWrapper(storeWelfarePaymentsGathering, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<StoreWelfarePaymentsGathering> pageList = storeWelfarePaymentsGatheringService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "福利金收款商家列表");
      mv.addObject(NormalExcelConstants.CLASS, StoreWelfarePaymentsGathering.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("福利金收款商家列表数据", "导出人:Jeecg", "导出信息"));
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
              List<StoreWelfarePaymentsGathering> listStoreWelfarePaymentsGatherings = ExcelImportUtil.importExcel(file.getInputStream(), StoreWelfarePaymentsGathering.class, params);
              storeWelfarePaymentsGatheringService.saveBatch(listStoreWelfarePaymentsGatherings);
              return Result.ok("文件导入成功！数据行数:" + listStoreWelfarePaymentsGatherings.size());
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
