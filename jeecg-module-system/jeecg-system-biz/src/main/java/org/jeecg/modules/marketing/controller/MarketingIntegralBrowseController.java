package org.jeecg.modules.marketing.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.good.entity.GoodType;
import org.jeecg.modules.good.service.IGoodTypeService;
import org.jeecg.modules.marketing.entity.MarketingIntegralBrowse;
import org.jeecg.modules.marketing.service.IMarketingIntegralBrowseService;
import org.jeecg.modules.marketing.vo.MarketingIntegralBrowseVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

 /**
 * @Description: 每日浏览
 * @Author: jeecg-boot
 * @Date:   2021-04-29
 * @Version: V1.0
 */
@Slf4j
@Api(tags="每日浏览")
@RestController
@RequestMapping("/marketing/marketingIntegralBrowse")
public class MarketingIntegralBrowseController {
	@Autowired
	private IMarketingIntegralBrowseService marketingIntegralBrowseService;
	@Autowired
	private IGoodTypeService iGoodTypeService;
	/**
	  *   添加
	 * @param marketingIntegralBrowse
	 * @return
	 */
	@AutoLog(value = "每日浏览-添加")
	@ApiOperation(value="每日浏览-添加", notes="每日浏览-添加")
	@PostMapping(value = "/addOrEdit")
	public Result<MarketingIntegralBrowse> addOrEdit(@RequestBody MarketingIntegralBrowse marketingIntegralBrowse) {
		Result<MarketingIntegralBrowse> result = new Result<MarketingIntegralBrowse>();
		try {
			long count=marketingIntegralBrowseService.count();
			if(count==0) {
				marketingIntegralBrowseService.save(marketingIntegralBrowse);
			}else{
				marketingIntegralBrowseService.updateById(marketingIntegralBrowse);
			}
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}

	 /**
	  * 通过id查询
	  * @return
	  */
	 @AutoLog(value = "免单基础设置-查询")
	 @ApiOperation(value="免单基础设置-查询", notes="免单基础设置-查询")
	 @GetMapping(value = "/queryByOne")
	 public Result<?> queryByOne() {
		 LambdaQueryWrapper<MarketingIntegralBrowse> marketingIntegralBrowseLambdaQueryWrapper = new LambdaQueryWrapper<MarketingIntegralBrowse>().eq(MarketingIntegralBrowse::getDelFlag, "0");
		 if (marketingIntegralBrowseService.count(marketingIntegralBrowseLambdaQueryWrapper)>0){
			 MarketingIntegralBrowse integralBrowse = marketingIntegralBrowseService.getOne(marketingIntegralBrowseLambdaQueryWrapper);
			 MarketingIntegralBrowseVO marketingIntegralBrowseVO = new MarketingIntegralBrowseVO();

			 BeanUtils.copyProperties(integralBrowse,marketingIntegralBrowseVO);
			 if (StringUtils.isNotBlank(integralBrowse.getGoodTypeId())){
				 GoodType goodType = iGoodTypeService.getById(integralBrowse.getGoodTypeId());
				 GoodType goodType1 = iGoodTypeService.getById(goodType.getParentId());
				 marketingIntegralBrowseVO.setGoodTypeList(goodType1.getParentId()+","+goodType1.getId()+","+goodType.getId());
			 }
			 return Result.ok(integralBrowse);
		 }else {
		 	return Result.ok("");
		 }
	 }
}
