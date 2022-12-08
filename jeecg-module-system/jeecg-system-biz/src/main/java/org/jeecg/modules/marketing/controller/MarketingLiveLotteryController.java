package org.jeecg.modules.marketing.controller;

import com.alibaba.fastjson.JSON;
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
import org.jeecg.common.util.MapHandleUtils;
import org.jeecg.common.util.OrderNoUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.marketing.entity.MarketingLiveLottery;
import org.jeecg.modules.marketing.entity.MarketingLivePrize;
import org.jeecg.modules.marketing.service.IMarketingLiveLotteryService;
import org.jeecg.modules.marketing.service.IMarketingLivePrizeService;
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
 * @Description: 直播管理-直播抽奖
 * @Author: jeecg-boot
 * @Date:   2021-09-15
 * @Version: V1.0
 */
@Slf4j
@Api(tags="直播管理-直播抽奖")
@RestController
@RequestMapping("/marketingLiveLottery/marketingLiveLottery")
public class MarketingLiveLotteryController {
	@Autowired
	private IMarketingLiveLotteryService marketingLiveLotteryService;
	@Autowired
	private IMarketingLivePrizeService iMarketingLivePrizeService;
	/**
	  * 分页列表查询
	 * @param marketingLiveLottery
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "直播管理-直播抽奖-分页列表查询")
	@ApiOperation(value="直播管理-直播抽奖-分页列表查询", notes="直播管理-直播抽奖-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(MarketingLiveLottery marketingLiveLottery,
															 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
															 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
															 HttpServletRequest req) {
		return Result.ok(marketingLiveLotteryService
				.queryPageList(new Page<Map<String,Object>>(pageNo, pageSize),
						QueryGenerator.initQueryWrapper(marketingLiveLottery, req.getParameterMap()).eq("del_flag","0"),
						MapHandleUtils.handleRequestMap(req.getParameterMap()))
		);
	}
	
	/**
	  *   添加
	 * @param marketingLiveLottery
	 * @return
	 */
	@AutoLog(value = "直播管理-直播抽奖-添加")
	@ApiOperation(value="直播管理-直播抽奖-添加", notes="直播管理-直播抽奖-添加")
	@PostMapping(value = "/add")
	public Result<MarketingLiveLottery> add(@RequestBody MarketingLiveLottery marketingLiveLottery) {
		Result<MarketingLiveLottery> result = new Result<MarketingLiveLottery>();
		if (StringUtils.isBlank(marketingLiveLottery.getMarketingLiveStreamingId())){
			return result.error500("直播间id未传递!");
		}
		if (marketingLiveLotteryService.count(new LambdaQueryWrapper<MarketingLiveLottery>()
				.eq(MarketingLiveLottery::getDelFlag,"0")
				.eq(MarketingLiveLottery::getMarketingLiveStreamingId,marketingLiveLottery.getMarketingLiveStreamingId())
				.eq(MarketingLiveLottery::getLotteryNumber,marketingLiveLottery.getLotteryNumber())
		)>0){
			return result.error500("抽奖轮次不能重复");
		}
		if (StringUtils.isBlank(marketingLiveLottery.getLotteryPrizeId())){
			return result.error500("中奖商品id不能为空");
		}
		if (marketingLiveLottery.getLotteryPrizeTotal()==null){
			return result.error500("中奖商品总量不能为空");
		}
		MarketingLivePrize marketingLivePrize = iMarketingLivePrizeService.getById(marketingLiveLottery.getLotteryPrizeId());
		if (marketingLivePrize!=null){
			if (marketingLivePrize.getSuperInventory().equals("0")&&marketingLiveLottery.getLotteryPrizeTotal().longValue()>marketingLivePrize.getRepertory().longValue()){
				return result.error500("中奖奖品总量不能大于奖品实际库存");
			}
		}else {
			return result.error500("中奖商品id传递有误,未找到实体");
		}
		if (marketingLiveLottery.getLosingLotteryPrizeType().equals("1")){
			if (StringUtils.isBlank(marketingLiveLottery.getLosingLotteryPrizeId())){
				return result.error500("未中奖商品id未传递");
			}
		}
		try {
			marketingLiveLotteryService.save(marketingLiveLottery.
					setStreamNumber(OrderNoUtils.getOrderNo()));
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param marketingLiveLottery
	 * @return
	 */
	@AutoLog(value = "直播管理-直播抽奖-编辑")
	@ApiOperation(value="直播管理-直播抽奖-编辑", notes="直播管理-直播抽奖-编辑")
	@PostMapping(value = "/edit")
	public Result<MarketingLiveLottery> edit(@RequestBody MarketingLiveLottery marketingLiveLottery) {
		Result<MarketingLiveLottery> result = new Result<MarketingLiveLottery>();
		if (StringUtils.isBlank(marketingLiveLottery.getId())){
			return result.error500("前端id未传递!");
		}
		MarketingLiveLottery marketingLiveLotteryEntity = marketingLiveLotteryService.getById(marketingLiveLottery.getId());
		if(marketingLiveLotteryEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean b = false;
			if (marketingLiveLotteryEntity.getLotteryNumber().doubleValue()==marketingLiveLottery.getLotteryNumber().doubleValue()){
				MarketingLivePrize marketingLivePrize = iMarketingLivePrizeService.getById(marketingLiveLottery.getLotteryPrizeId());
				if (marketingLivePrize!=null){
					if (marketingLivePrize.getSuperInventory().equals("0")&&marketingLiveLottery.getLotteryPrizeTotal().longValue()>marketingLivePrize.getRepertory().longValue()){
						return result.error500("中奖奖品总量不能大于奖品实际库存");
					}
				}else {
					return result.error500("中奖商品id传递有误,未找到实体");
				}
				b = marketingLiveLotteryService.updateById(marketingLiveLottery);
			}else {
				if (marketingLiveLotteryService.count(new LambdaQueryWrapper<MarketingLiveLottery>()
						.eq(MarketingLiveLottery::getDelFlag,"0")
						.eq(MarketingLiveLottery::getMarketingLiveStreamingId,marketingLiveLottery.getMarketingLiveStreamingId())
						.eq(MarketingLiveLottery::getLotteryNumber,marketingLiveLottery.getLotteryNumber())
				)>0){
					return result.error500("抽奖轮次不能重复");
				}else {
					MarketingLivePrize marketingLivePrize = iMarketingLivePrizeService.getById(marketingLiveLottery.getLotteryPrizeId());
					if (marketingLivePrize!=null){
						if (marketingLivePrize.getSuperInventory().equals("0")&&marketingLiveLottery.getLotteryPrizeTotal().longValue()>marketingLivePrize.getRepertory().longValue()){
							return result.error500("中奖奖品总量不能大于奖品实际库存");
						}
					}else {
						return result.error500("中奖商品id传递有误,未找到实体");
					}
					b = marketingLiveLotteryService.updateById(marketingLiveLottery);
				}
			}

			if(b) {
				result.success("修改成功!");
			}else {
				result.error500("修改失败!");
			}
		}
		
		return result;
	}
	@PostMapping("LiveLotteryCancel")
	public Result<?>LiveLotteryCancel(@RequestBody MarketingLiveLottery marketingLiveLottery){
		if (StringUtils.isBlank(marketingLiveLottery.getId())){
			return Result.error("前端id未传递!");
		}
		MarketingLiveLottery marketingLiveLotteryEntity = marketingLiveLotteryService.getById(marketingLiveLottery.getId());
		if(marketingLiveLotteryEntity==null) {
			return Result.error("未找到对应实体");
		}else {
			boolean b = marketingLiveLotteryService.updateById(marketingLiveLottery);
			if(b) {
				return Result.ok("修改成功!");
			}else {
				return Result.error("修改失败!");
			}
		}
	}
	/**
	  *   通过id删除
	 * @param marketingLiveLottery
	 * @return
	 */
	@AutoLog(value = "直播管理-直播抽奖-通过id删除")
	@ApiOperation(value="直播管理-直播抽奖-通过id删除", notes="直播管理-直播抽奖-通过id删除")
	@PostMapping(value = "/delete")
	public Result<?> delete(@RequestBody MarketingLiveLottery marketingLiveLottery) {
		if (StringUtils.isBlank(marketingLiveLottery.getId())){
			return Result.error("前端id未传递!");
		}
		try {
			marketingLiveLotteryService.updateById(marketingLiveLottery);
			marketingLiveLotteryService.removeById(marketingLiveLottery.getId());
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
	@AutoLog(value = "直播管理-直播抽奖-批量删除")
	@ApiOperation(value="直播管理-直播抽奖-批量删除", notes="直播管理-直播抽奖-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<MarketingLiveLottery> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<MarketingLiveLottery> result = new Result<MarketingLiveLottery>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.marketingLiveLotteryService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "直播管理-直播抽奖-通过id查询")
	@ApiOperation(value="直播管理-直播抽奖-通过id查询", notes="直播管理-直播抽奖-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<MarketingLiveLottery> queryById(@RequestParam(name="id",required=true) String id) {
		Result<MarketingLiveLottery> result = new Result<MarketingLiveLottery>();
		MarketingLiveLottery marketingLiveLottery = marketingLiveLotteryService.getById(id);
		if(marketingLiveLottery==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(marketingLiveLottery);
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
      QueryWrapper<MarketingLiveLottery> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              MarketingLiveLottery marketingLiveLottery = JSON.parseObject(deString, MarketingLiveLottery.class);
              queryWrapper = QueryGenerator.initQueryWrapper(marketingLiveLottery, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<MarketingLiveLottery> pageList = marketingLiveLotteryService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "直播管理-直播抽奖列表");
      mv.addObject(NormalExcelConstants.CLASS, MarketingLiveLottery.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("直播管理-直播抽奖列表数据", "导出人:Jeecg", "导出信息"));
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
              List<MarketingLiveLottery> listMarketingLiveLotterys = ExcelImportUtil.importExcel(file.getInputStream(), MarketingLiveLottery.class, params);
              marketingLiveLotteryService.saveBatch(listMarketingLiveLotterys);
              return Result.ok("文件导入成功！数据行数:" + listMarketingLiveLotterys.size());
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

  /*private MarketingLiveLottery transition(MarketingLiveLotteryVO marketingLiveLotteryVO){
	  MarketingLiveLottery marketingLiveLottery = new MarketingLiveLottery();
	  BeanUtils.copyProperties(marketingLiveLotteryVO,marketingLiveLottery);
	  JSONObject lotteryPrize = new JSONObject();
	  lotteryPrize.put("PrizeType",marketingLiveLotteryVO.getLotteryPrizeType());
	  lotteryPrize.put("prizeId",marketingLiveLotteryVO.getLotteryprizeId());
	  lotteryPrize.put("quantity",marketingLiveLotteryVO.getLotteryPrizequantity());
	  lotteryPrize.put("prizeTotal",marketingLiveLotteryVO.getLotteryprizeTotal());
	  marketingLiveLottery.setLotteryPrize(lotteryPrize.toJSONString());
	  JSONObject losingLotteryPrize = new JSONObject();
	  losingLotteryPrize.put("PrizeType",marketingLiveLotteryVO.getLosingLotteryPrizeType());
	  losingLotteryPrize.put("prizeId",marketingLiveLotteryVO.getLosingLotteryPrizeId());
	  losingLotteryPrize.put("quantity",marketingLiveLotteryVO.getLosingLotteryPrizequantity());
	  losingLotteryPrize.put("prizeTotal",marketingLiveLotteryVO.getLosingLotteryPrizeTotal());
	  marketingLiveLottery.setLosingLotteryPrize(losingLotteryPrize.toJSONString());
	  return marketingLiveLottery;
  }*/
}
