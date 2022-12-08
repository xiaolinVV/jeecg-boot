package org.jeecg.modules.marketing.controller;

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
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.marketing.entity.MarketingChannel;
import org.jeecg.modules.marketing.service.IMarketingChannelService;
import org.jeecg.modules.marketing.vo.MarketingChannelVO;
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
 * @Description: 发券渠道
 * @Author: jeecg-boot
 * @Date:   2019-11-12
 * @Version: V1.0
 */
@Slf4j
@Api(tags="发券渠道")
@RestController
@RequestMapping("/marketingChannel/marketingChannel")
public class MarketingChannelController {
	@Autowired
	private IMarketingChannelService marketingChannelService;
	
	/**
	  * 分页列表查询
	 * @param marketingChannel
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "发券渠道-分页列表查询")
	@ApiOperation(value="发券渠道-分页列表查询", notes="发券渠道-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<MarketingChannel>> queryPageList(MarketingChannel marketingChannel,
														 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
														 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
														 HttpServletRequest req) {
		Result<IPage<MarketingChannel>> result = new Result<IPage<MarketingChannel>>();
		QueryWrapper<MarketingChannel> queryWrapper = QueryGenerator.initQueryWrapper(marketingChannel, req.getParameterMap());
		Page<MarketingChannel> page = new Page<MarketingChannel>(pageNo, pageSize);
		IPage<MarketingChannel> pageList = marketingChannelService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param marketingChannel
	 * @return
	 */
	@AutoLog(value = "发券渠道-添加")
	@ApiOperation(value="发券渠道-添加", notes="发券渠道-添加")
	@PostMapping(value = "/add")
	public Result<MarketingChannel> add(@RequestBody MarketingChannel marketingChannel) {
		Result<MarketingChannel> result = new Result<MarketingChannel>();
		try {
			marketingChannelService.save(marketingChannel);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param marketingChannel
	 * @return
	 */
	@AutoLog(value = "发券渠道-编辑")
	@ApiOperation(value="发券渠道-编辑", notes="发券渠道-编辑")
	@PutMapping(value = "/edit")
	public Result<MarketingChannel> edit(@RequestBody MarketingChannel marketingChannel) {
		Result<MarketingChannel> result = new Result<MarketingChannel>();
		MarketingChannel marketingChannelEntity = marketingChannelService.getById(marketingChannel.getId());
		if(marketingChannelEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = marketingChannelService.updateById(marketingChannel);
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
	@AutoLog(value = "发券渠道-通过id删除")
	@ApiOperation(value="发券渠道-通过id删除", notes="发券渠道-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			marketingChannelService.removeById(id);
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
	@AutoLog(value = "发券渠道-批量删除")
	@ApiOperation(value="发券渠道-批量删除", notes="发券渠道-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<MarketingChannel> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<MarketingChannel> result = new Result<MarketingChannel>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.marketingChannelService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "发券渠道-通过id查询")
	@ApiOperation(value="发券渠道-通过id查询", notes="发券渠道-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<MarketingChannel> queryById(@RequestParam(name="id",required=true) String id) {
		Result<MarketingChannel> result = new Result<MarketingChannel>();
		MarketingChannel marketingChannel = marketingChannelService.getById(id);
		if(marketingChannel==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(marketingChannel);
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
      QueryWrapper<MarketingChannel> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              MarketingChannel marketingChannel = JSON.parseObject(deString, MarketingChannel.class);
              queryWrapper = QueryGenerator.initQueryWrapper(marketingChannel, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<MarketingChannel> pageList = marketingChannelService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "发券渠道列表");
      mv.addObject(NormalExcelConstants.CLASS, MarketingChannel.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("发券渠道列表数据", "导出人:Jeecg", "导出信息"));
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
              List<MarketingChannel> listMarketingChannels = ExcelImportUtil.importExcel(file.getInputStream(), MarketingChannel.class, params);
              marketingChannelService.saveBatch(listMarketingChannels);
              return Result.ok("文件导入成功！数据行数:" + listMarketingChannels.size());
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
	  * 启动停用
	  * @param id
	  * @param statusExplain
	  * @return
	  */
	 @AutoLog(value = "发券渠道-通过id改变状态")
	 @ApiOperation(value="发券渠道-通过id改变状态", notes="发券渠道-通过id改变状态")
  @RequestMapping(value = "updateStatusById",method = RequestMethod.GET)
  public Result<MarketingChannel>updateStatusById(@RequestParam(name = "id",required = true) String id,
												  @RequestParam(name = "statusExplain",required = true) String statusExplain){
	  Result<MarketingChannel> result = new Result<>();
	  MarketingChannel byId = marketingChannelService.getById(id);
	  if (oConvertUtils.isEmpty(byId)){
	  	result.error500("未找到实体");
	  }else {
		 if ("1".equals(byId.getStatus())){
		 	byId.setStatus("0");
		 	byId.setStatusExplain(statusExplain);
		 }else {
		 	byId.setStatus("1");
		 	byId.setStatusExplain(" ");
		 }}
		 boolean b = marketingChannelService.updateById(byId);
		 if (b){
		 	result.success("修改成功");
		 }else {
		 	result.error500("修改失败");
		 }
		 return result;
	 }
	 @AutoLog(value = "发券渠道-返回list集合")
	 @ApiOperation(value="发券渠道-返回list集合", notes="发券渠道-返回list集合")
	 @RequestMapping(value = "queryList",method = RequestMethod.GET)
	 public List<MarketingChannel> queryList(){
		 QueryWrapper<MarketingChannel> marketingChannelQueryWrapper = new QueryWrapper<>();
		 marketingChannelQueryWrapper.eq("del_flag","0");
		 List<MarketingChannel> list = marketingChannelService.list(marketingChannelQueryWrapper);
		 return list;
	 }
	 @AutoLog(value = "发券渠道-发券渠道记录列表")
	 @ApiOperation(value="发券渠道-发券渠道记录列表", notes="发券渠道-发券渠道记录列表")
	 @RequestMapping(value = "findMarkeingChannel",method = RequestMethod.GET)
	 public Result<IPage<MarketingChannelVO>> findMarkeingChannel(MarketingChannelVO marketingChannelVO,
																  @RequestParam(value = "pageNo",defaultValue = "1")Integer pagaNo,
																  @RequestParam(value = "pageSize",defaultValue = "10")Integer pageSize){
		 Result<IPage<MarketingChannelVO>> result = new Result<>();
		 Page<MarketingChannelVO> page = new Page<>(pagaNo, pageSize);
		 IPage<MarketingChannelVO> markeingChannel = marketingChannelService.findMarkeingChannel(page, marketingChannelVO);
		 result.setSuccess(true);
		 result.setResult(markeingChannel);
		 return result;
	 }
}