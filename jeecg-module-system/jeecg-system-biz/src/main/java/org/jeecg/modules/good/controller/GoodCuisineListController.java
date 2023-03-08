package org.jeecg.modules.good.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
import org.jeecg.modules.good.dto.GoodCuisineListDTO;
import org.jeecg.modules.good.entity.GoodCuisineList;
import org.jeecg.modules.good.entity.GoodCuisineSpecification;
import org.jeecg.modules.good.service.IGoodCuisineListService;
import org.jeecg.modules.good.service.IGoodCuisineSpecificationService;
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
 * @Description: 菜品列表
 * @Author: jeecg-boot
 * @Date:   2022-06-09
 * @Version: V1.0
 */
@Slf4j
@Api(tags="菜品列表")
@RestController
@RequestMapping("/good/goodCuisineList")
public class GoodCuisineListController {
	@Autowired
	private IGoodCuisineListService goodCuisineListService;

	@Autowired
	private IGoodCuisineSpecificationService iGoodCuisineSpecificationService;
	
	/**
	  * 分页列表查询
	 * @param goodCuisineList
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@AutoLog(value = "菜品列表-分页列表查询")
	@ApiOperation(value="菜品列表-分页列表查询", notes="菜品列表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(GoodCuisineList goodCuisineList,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {
		return Result.ok(goodCuisineListService.queryPageList(new Page<>(pageNo,pageSize),goodCuisineList));
	}
	
	/**
	  *   添加
	 * @param goodCuisineListDTO
	 * @return
	 */
	@AutoLog(value = "菜品列表-添加")
	@ApiOperation(value="菜品列表-添加", notes="菜品列表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody GoodCuisineListDTO goodCuisineListDTO) {
		Result<String> result = new Result<String>();
		try {
			log.info(JSON.toJSONString(goodCuisineListDTO));
			GoodCuisineList goodCuisineList=new GoodCuisineList();
			//商品编号
			JSONArray shopInfo=JSON.parseArray(goodCuisineListDTO.getShopInfo());
			JSONObject shopInfoo=(JSONObject)shopInfo.get(0);
			if(StringUtils.isBlank(shopInfoo.getString("serialNumber"))){
				return Result.error("商品编号不能为空");
			}
			//商品名称
			if(StringUtils.isBlank(goodCuisineListDTO.getGoodName())){
				return Result.error("商品名称不能为空");
			}
			goodCuisineList.setGoodName(goodCuisineListDTO.getGoodName());
			//菜单分类
			if(StringUtils.isBlank(goodCuisineListDTO.getGoodCuisineTypeId())){
				return Result.error("请选择菜单分类");
			}
			goodCuisineList.setGoodCuisineTypeId(goodCuisineListDTO.getGoodCuisineTypeId());
			//店铺管理id
			if(StringUtils.isBlank(goodCuisineListDTO.getStoreManageId())){
				return Result.error("店铺id不能为空");
			}
			goodCuisineList.setStoreManageId(goodCuisineListDTO.getStoreManageId());
			goodCuisineList.setGoodDescribe(goodCuisineListDTO.getDescribe());
			goodCuisineList.setMainPicture(goodCuisineListDTO.getMainImages());
			goodCuisineList.setVideo(goodCuisineListDTO.getVideoImages());
			goodCuisineList.setDetailsFigure(goodCuisineListDTO.getDetailsImages());
			//是否有规格
			JSONArray specifications= JSON.parseArray(goodCuisineListDTO.getSpecifications());
			goodCuisineList.setSpecifications(goodCuisineListDTO.getSpecifications());
			goodCuisineList.setShopInfo(goodCuisineListDTO.getShopInfo());
			goodCuisineList.setSpecificationsDecribes(goodCuisineListDTO.getSpecificationsDecribes());
			goodCuisineList.setFrameStatus("1");
			goodCuisineList.setStatus("1");
			if(specifications.size()==0){
				//无规格

			}else{
				//有规格
				goodCuisineList.setIsSpecification("1");
			}
			goodCuisineList.setSerialNumber(OrderNoUtils.getOrderNo());
			if(goodCuisineListService.save(goodCuisineList)){
				if(specifications.size()==0){
					//无规格
					GoodCuisineSpecification goodCuisineSpecification=new GoodCuisineSpecification();
					goodCuisineSpecification.setGoodCuisineListId(goodCuisineList.getId());
					goodCuisineSpecification.setSpecification("无");
					goodCuisineSpecification.setSalesPrice(shopInfoo.getBigDecimal("salesPrice"));
					goodCuisineSpecification.setCostPrice(shopInfoo.getBigDecimal("costPrice"));
					goodCuisineSpecification.setRepertory(shopInfoo.getBigDecimal("repertory"));
					goodCuisineSpecification.setSku(shopInfoo.getString("sku"));
					iGoodCuisineSpecificationService.save(goodCuisineSpecification);
				}else{
					//有规格
					JSONArray specificationsDecribes=JSON.parseArray(goodCuisineListDTO.getSpecificationsDecribes());
					specificationsDecribes.forEach(s->{
						JSONObject jsonObject=(JSONObject)s;
						GoodCuisineSpecification goodCuisineSpecification=new GoodCuisineSpecification();
						goodCuisineSpecification.setGoodCuisineListId(goodCuisineList.getId());
						goodCuisineSpecification.setSpecification(jsonObject.getString("pName"));
						goodCuisineSpecification.setSalesPrice(jsonObject.getBigDecimal("salesPrice"));
						goodCuisineSpecification.setCostPrice(jsonObject.getBigDecimal("costPrice"));
						goodCuisineSpecification.setRepertory(jsonObject.getBigDecimal("repertory"));
						goodCuisineSpecification.setSku(jsonObject.getString("sku"));
						goodCuisineSpecification.setImg(jsonObject.getString("imgUrl"));
						iGoodCuisineSpecificationService.save(goodCuisineSpecification);
					});
				}
				result.success("添加成功");
			}else{
				return Result.error("未知错误，请联系管理员");
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param goodCuisineListDTO
	 * @return
	 */
	@AutoLog(value = "菜品列表-编辑")
	@ApiOperation(value="菜品列表-编辑", notes="菜品列表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody GoodCuisineListDTO goodCuisineListDTO) {
		Result<String> result = new Result<String>();
		log.info(JSON.toJSONString(goodCuisineListDTO));
		GoodCuisineList goodCuisineList=new GoodCuisineList();
		goodCuisineList.setId(goodCuisineListDTO.getId());
		//商品编号
		JSONArray shopInfo=JSON.parseArray(goodCuisineListDTO.getShopInfo());
		JSONObject shopInfoo=(JSONObject)shopInfo.get(0);
		if(StringUtils.isBlank(shopInfoo.getString("serialNumber"))){
			return Result.error("商品编号不能为空");
		}
		if(goodCuisineListService.count(new LambdaQueryWrapper<GoodCuisineList>().eq(GoodCuisineList::getSerialNumber,shopInfoo.getString("serialNumber")))>0){
			return Result.error("商品编号不能重复，请重新编写");
		}
		//商品名称
		if(StringUtils.isBlank(goodCuisineListDTO.getGoodName())){
			return Result.error("商品名称不能为空");
		}
		goodCuisineList.setGoodName(goodCuisineListDTO.getGoodName());
		//菜单分类
		if(StringUtils.isBlank(goodCuisineListDTO.getGoodCuisineTypeId())){
			return Result.error("请选择菜单分类");
		}
		goodCuisineList.setGoodCuisineTypeId(goodCuisineListDTO.getGoodCuisineTypeId());
		//店铺管理id
		if(StringUtils.isBlank(goodCuisineListDTO.getStoreManageId())){
			return Result.error("店铺id不能为空");
		}
		goodCuisineList.setStoreManageId(goodCuisineListDTO.getStoreManageId());
		goodCuisineList.setGoodDescribe(goodCuisineListDTO.getDescribe());
		goodCuisineList.setMainPicture(goodCuisineListDTO.getMainImages());
		goodCuisineList.setVideo(goodCuisineListDTO.getVideoImages());
		goodCuisineList.setDetailsFigure(goodCuisineListDTO.getDetailsImages());
		//是否有规格
		JSONArray specifications= JSON.parseArray(goodCuisineListDTO.getSpecifications());
		goodCuisineList.setSpecifications(goodCuisineListDTO.getSpecifications());
		goodCuisineList.setShopInfo(goodCuisineListDTO.getShopInfo());
		goodCuisineList.setSpecificationsDecribes(goodCuisineListDTO.getSpecificationsDecribes());
		goodCuisineList.setFrameStatus("1");
		goodCuisineList.setStatus("1");
		if(specifications.size()==0){
			//无规格

		}else{
			//有规格
			goodCuisineList.setIsSpecification("1");
		}
		goodCuisineList.setSerialNumber(OrderNoUtils.getOrderNo());
		if(goodCuisineListService.saveOrUpdate(goodCuisineList)){
			iGoodCuisineSpecificationService.remove(new LambdaQueryWrapper<GoodCuisineSpecification>().eq(GoodCuisineSpecification::getGoodCuisineListId,goodCuisineList.getId()));
			if(specifications.size()==0){
				//无规格
				GoodCuisineSpecification goodCuisineSpecification=new GoodCuisineSpecification();
				goodCuisineSpecification.setGoodCuisineListId(goodCuisineList.getId());
				goodCuisineSpecification.setSpecification("无");
				goodCuisineSpecification.setSalesPrice(shopInfoo.getBigDecimal("salesPrice"));
				goodCuisineSpecification.setCostPrice(shopInfoo.getBigDecimal("costPrice"));
				goodCuisineSpecification.setRepertory(shopInfoo.getBigDecimal("repertory"));
				goodCuisineSpecification.setSku(shopInfoo.getString("sku"));
				iGoodCuisineSpecificationService.save(goodCuisineSpecification);
			}else{
				//有规格
				JSONArray specificationsDecribes=JSON.parseArray(goodCuisineListDTO.getSpecificationsDecribes());
				specificationsDecribes.forEach(s->{
					JSONObject jsonObject=(JSONObject)s;
					GoodCuisineSpecification goodCuisineSpecification=new GoodCuisineSpecification();
					goodCuisineSpecification.setGoodCuisineListId(goodCuisineList.getId());
					goodCuisineSpecification.setSpecification(jsonObject.getString("pName"));
					goodCuisineSpecification.setSalesPrice(jsonObject.getBigDecimal("salesPrice"));
					goodCuisineSpecification.setCostPrice(jsonObject.getBigDecimal("costPrice"));
					goodCuisineSpecification.setRepertory(jsonObject.getBigDecimal("repertory"));
					goodCuisineSpecification.setSku(jsonObject.getString("sku"));
					goodCuisineSpecification.setImg(jsonObject.getString("imgUrl"));
					iGoodCuisineSpecificationService.save(goodCuisineSpecification);
				});
			}
			return Result.ok("修改成功");
		}else{
			return Result.error("未知错误，请联系管理员");
		}
	}
	
	/**
	  *   通过id删除
	 * @param id
	 * @return
	 */
	@AutoLog(value = "菜品列表-通过id删除")
	@ApiOperation(value="菜品列表-通过id删除", notes="菜品列表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			goodCuisineListService.removeById(id);
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
	@AutoLog(value = "菜品列表-批量删除")
	@ApiOperation(value="菜品列表-批量删除", notes="菜品列表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<GoodCuisineList> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<GoodCuisineList> result = new Result<GoodCuisineList>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.goodCuisineListService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "菜品列表-通过id查询")
	@ApiOperation(value="菜品列表-通过id查询", notes="菜品列表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<GoodCuisineList> queryById(@RequestParam(name="id",required=true) String id) {
		Result<GoodCuisineList> result = new Result<GoodCuisineList>();
		GoodCuisineList goodCuisineList = goodCuisineListService.getById(id);
		if(goodCuisineList==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(goodCuisineList);
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
      QueryWrapper<GoodCuisineList> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              GoodCuisineList goodCuisineList = JSON.parseObject(deString, GoodCuisineList.class);
              queryWrapper = QueryGenerator.initQueryWrapper(goodCuisineList, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<GoodCuisineList> pageList = goodCuisineListService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "菜品列表列表");
      mv.addObject(NormalExcelConstants.CLASS, GoodCuisineList.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("菜品列表列表数据", "导出人:Jeecg", "导出信息"));
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
              List<GoodCuisineList> listGoodCuisineLists = ExcelImportUtil.importExcel(file.getInputStream(), GoodCuisineList.class, params);
              goodCuisineListService.saveBatch(listGoodCuisineLists);
              return Result.ok("文件导入成功！数据行数:" + listGoodCuisineLists.size());
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
