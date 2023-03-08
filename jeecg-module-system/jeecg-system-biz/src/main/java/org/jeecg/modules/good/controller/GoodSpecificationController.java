package org.jeecg.modules.good.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.good.entity.GoodList;
import org.jeecg.modules.good.entity.GoodSpecification;
import org.jeecg.modules.good.service.IGoodListService;
import org.jeecg.modules.good.service.IGoodSpecificationService;
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
import java.util.*;

 /**
 * @Description: 商品规格
 * @Author: jeecg-boot
 * @Date:   2019-10-23
 * @Version: V1.0
 */
@Slf4j
@Api(tags="商品规格")
@RestController
@RequestMapping("/goodSpecification/goodSpecification")
public class GoodSpecificationController {
	@Autowired
	private IGoodSpecificationService goodSpecificationService;
	@Autowired
	private IGoodListService goodListService;

	/**
	  * 分页列表查询
	 * @param goodSpecification
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "商品规格-分页列表查询")
	@ApiOperation(value="商品规格-分页列表查询", notes="商品规格-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<GoodSpecification>> queryPageList(GoodSpecification goodSpecification,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<GoodSpecification>> result = new Result<IPage<GoodSpecification>>();
		QueryWrapper<GoodSpecification> queryWrapper = QueryGenerator.initQueryWrapper(goodSpecification, req.getParameterMap());

		Page<GoodSpecification> page = new Page<GoodSpecification>(pageNo, pageSize);
		IPage<GoodSpecification> pageList = goodSpecificationService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param goodSpecification
	 * @return
	 */
	@AutoLog(value = "商品规格-添加")
	@ApiOperation(value="商品规格-添加", notes="商品规格-添加")
	@PostMapping(value = "/add")
	public Result<GoodSpecification> add(@RequestBody GoodSpecification goodSpecification) {
		Result<GoodSpecification> result = new Result<GoodSpecification>();
		try {
			goodSpecificationService.save(goodSpecification);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param goodSpecification
	 * @return
	 */
	@AutoLog(value = "商品规格-编辑")
	@ApiOperation(value="商品规格-编辑", notes="商品规格-编辑")
	@PutMapping(value = "/edit")
	public Result<GoodSpecification> edit(@RequestBody GoodSpecification goodSpecification) {
		Result<GoodSpecification> result = new Result<GoodSpecification>();
		GoodSpecification goodSpecificationEntity = goodSpecificationService.getById(goodSpecification.getId());
		if(goodSpecificationEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = goodSpecificationService.updateById(goodSpecification);
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
	@AutoLog(value = "商品规格-通过id删除")
	@ApiOperation(value="商品规格-通过id删除", notes="商品规格-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			goodSpecificationService.removeById(id);
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
	@AutoLog(value = "商品规格-批量删除")
	@ApiOperation(value="商品规格-批量删除", notes="商品规格-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<GoodSpecification> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<GoodSpecification> result = new Result<GoodSpecification>();
		if(StringUtils.isBlank(ids)) {
			result.error500("参数不识别！");
		}else {
			this.goodSpecificationService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "商品规格-通过id查询")
	@ApiOperation(value="商品规格-通过id查询", notes="商品规格-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<GoodSpecification> queryById(@RequestParam(name="id",required=true) String id) {
		Result<GoodSpecification> result = new Result<GoodSpecification>();
		GoodSpecification goodSpecification = goodSpecificationService.getById(id);
		if(goodSpecification==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(goodSpecification);
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
      QueryWrapper<GoodSpecification> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              GoodSpecification goodSpecification = JSON.parseObject(deString, GoodSpecification.class);
              queryWrapper = QueryGenerator.initQueryWrapper(goodSpecification, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<GoodSpecification> pageList = goodSpecificationService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "商品规格列表");
      mv.addObject(NormalExcelConstants.CLASS, GoodSpecification.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("商品规格列表数据", "导出人:Jeecg", "导出信息"));
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
              List<GoodSpecification> listGoodSpecifications = ExcelImportUtil.importExcel(file.getInputStream(), GoodSpecification.class, params);
              goodSpecificationService.saveBatch(listGoodSpecifications);
              return Result.ok("文件导入成功！数据行数:" + listGoodSpecifications.size());
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


	 /**
	  *修改价格获取参数列表
	  * goodList 对应商品
	  * listgoodSpecification 规格集合
	  * @param  goodListid
	  * @return
	  */
	 @AutoLog(value = "商品管理-通过id查询")
	 @ApiOperation(value="商品管理-通过id查询", notes="商品管理-通过id查询")
	 @RequestMapping(value = "/listChangePrice", method = RequestMethod.GET)
	 public Result<Map<String,Object>> listChangePrice(@RequestParam(name="goodListid",required=true) String goodListid) {
		 Result<Map<String,Object>> result = new Result<Map<String,Object>>();
		 GoodList goodList= goodListService.getById(goodListid);

		 List<GoodSpecification> listgoodSpecification = goodSpecificationService.getGoodSpecificationByGoodListId(goodListid);
		 if(goodList==null) {
			 result.error500("未找到对应实体");
		 }else {
			 try{
				 ArrayList<GoodList> listGoodList = new ArrayList<GoodList>();
				 listGoodList.add(goodList);
             Map<String,Object> map=new HashMap<>();
				 map.put("listGoodList",listGoodList);
				 map.put("listgoodSpecification",listgoodSpecification);
				 result.setResult(map);
			 }catch (Exception e){
				 result.error500("访问失败！");
			 }
		 }
		 return result;
	 }

	 /**
	  * 修改价格
	  * @param json listgoodSpecification:规格集合
	  * @return listGoodList：商品信息
	  */
	 @RequestMapping(value = "/updatePrice", method = RequestMethod.PUT)
	 public Result<String> updatePrice(@RequestBody JSONObject json) {
		 Result<String> result = new Result<String>();
		 try {
			 JSONArray listgoodSpecification= json.getJSONArray("listgoodSpecification");
			 JSONArray listGoodList= json.getJSONArray("listGoodList");
			 List<GoodSpecification> listgoodSpecification1 = listgoodSpecification.toJavaList(GoodSpecification.class);
			 List<GoodList> listGoodList1 = listGoodList.toJavaList(GoodList.class);
			 if(listgoodSpecification1.size()>0){

				 goodSpecificationService.updateBatchById((Collection<GoodSpecification>) listgoodSpecification1);

			 }
			 if(listGoodList1.size()>0){
				 int result1;


				 //需要修正或者删除
				/*for(GoodList goodList:listGoodList1){
					//最低商品价格
					if (goodList.getPrice() != null && !"".equals(goodList.getPrice())) {
						result1 = goodList.getPrice().indexOf("-");
						if (result1 != -1) {
							String smallPrice = goodList.getPrice().substring(0, goodList.getPrice().indexOf("-"));
							goodList.setSmallPrice(smallPrice);
						} else {
							goodList.setSmallPrice(goodList.getPrice());
						}
					}
					//最低vip价格
					if (goodList.getVipPrice() != null && !"".equals(goodList.getVipPrice())) {
						result1 = goodList.getVipPrice().indexOf("-");
						if (result1 != -1) {
							String smallVipPrice = goodList.getVipPrice().substring(0, goodList.getVipPrice().indexOf("-"));
							goodList.setSmallVipPrice(smallVipPrice);
						} else {
							goodList.setSmallVipPrice(goodList.getVipPrice());
						}
					}
					//最低成本价
					if (goodList.getCostPrice() != null && !"".equals(goodList.getCostPrice())) {
						result1 = goodList.getCostPrice().indexOf("-");
						if (result1 != -1) {
							String smallCostPrice = goodList.getCostPrice().substring(0, goodList.getCostPrice().indexOf("-"));
							goodList.setSmallCostPrice(smallCostPrice);
						} else {
							goodList.setSmallCostPrice(goodList.getCostPrice());
						}
					}
					//最低供货价
					if (goodList.getSupplyPrice() != null && !"".equals(goodList.getSupplyPrice())) {
						result1 = goodList.getSupplyPrice().indexOf("-");
						if (result1 != -1) {
							String smallSupplyPrice = goodList.getSupplyPrice().substring(0, goodList.getSupplyPrice().indexOf("-"));
							goodList.setSmallSupplyPrice(smallSupplyPrice);
						} else {
							goodList.setSmallSupplyPrice(goodList.getSupplyPrice());
						}
					}
				}*/

				 goodListService.updateBatchById((Collection<GoodList>)listGoodList1);

			 }

			 result.success("修改完成！");
		 }catch (Exception e){
			 log.error(e.getMessage(), e);
			 result.setSuccess(false);
			 result.setMessage("出错了: " + e.getMessage());
			 return result;
		 }

		 return result;
	 }

	 /**
	  * 通过商品id获取规格
	  * @param id
	  * @return
	  */
	 @GetMapping("getGoodSpecificationByGoodId")
	 public Result<List<Map<String,Object>>> getGoodSpecificationByGoodId(@RequestParam(name="id",required=true) String id){
		 Result<List<Map<String, Object>>> result = new Result<>();
		 result.setResult(goodSpecificationService.getGoodSpecificationByGoodId(id));
		 result.success("商品规格");
		 return result;
	 }

 }
