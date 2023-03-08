package org.jeecg.modules.good.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.aspect.annotation.PermissionData;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.config.util.PermissionUtils;
import org.jeecg.modules.good.dto.GoodStoreDiscountDTO;
import org.jeecg.modules.good.dto.GoodStoreListDto;
import org.jeecg.modules.good.dto.GoodStoreListNewDTO;
import org.jeecg.modules.good.entity.GoodList;
import org.jeecg.modules.good.entity.GoodStoreList;
import org.jeecg.modules.good.entity.GoodStoreSpecification;
import org.jeecg.modules.good.service.IGoodStoreListService;
import org.jeecg.modules.good.service.IGoodStoreSpecificationService;
import org.jeecg.modules.good.service.IGoodStoreTypeService;
import org.jeecg.modules.good.util.GoodUtils;
import org.jeecg.modules.good.vo.GoodStoreListVo;
import org.jeecg.modules.store.entity.StoreManage;
import org.jeecg.modules.store.service.IStoreManageService;
import org.jeecg.modules.system.service.ISysUserRoleService;
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
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

 /**
 * @Description: 店铺商品列表
 * @Author: jeecg-boot
 * @Date:   2019-10-25
 * @Version: V1.0
 */
@Slf4j
@Api(tags="店铺商品列表")
@RestController
@RequestMapping("/goodStoreList/goodStoreList")
public class GoodStoreListController {
	@Autowired
	private IGoodStoreListService goodStoreListService;

	@Autowired
	private IGoodStoreSpecificationService iGoodStoreSpecificationService;

	@Autowired
	private IStoreManageService iStoreManageService;

	@Autowired
	private IGoodStoreTypeService iGoodStoreTypeService;


	@Autowired
	private GoodUtils goodUtils;


	 /**
	  * 商品选择组件
	  *
	  * @param goodName
	  * @param goodTypeId
	  * @param pageNo
	  * @param pageSize
	  * @return
	  */
	@GetMapping("selectGood")
	public Result<?> selectGood(@RequestParam(defaultValue = "",name = "goodName",required = false) String goodName,
								@RequestParam(defaultValue = "",name = "goodTypeId",required = false) String goodTypeId,
								String storeManageId,
								@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								 @RequestParam(name="pageSize", defaultValue="5") Integer pageSize){
		Map<String,Object> paramMap= Maps.newHashMap();
		paramMap.put("goodName",goodName);
		paramMap.put("goodTypeId",goodTypeId);
		paramMap.put("sysUserId",iStoreManageService.getById(storeManageId).getSysUserId());
		log.info("查询条件："+JSON.toJSONString(paramMap));
		IPage<Map<String, Object>> iPage= goodStoreListService.selectGood(new Page<>(pageNo,pageSize),paramMap);
		iPage.getRecords().forEach(g->{
			g.put("storeSpecifications",iGoodStoreSpecificationService.listMaps(new QueryWrapper<GoodStoreSpecification>()
					.select("specification","price","repertory")
					.lambda()
					.eq(GoodStoreSpecification::getGoodStoreListId,g.get("id"))));
		});
		return Result.ok(iPage);
	}




	 /**
	  * 分页列表查询
	  *
	  * @param goodList
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "店铺商品列表-分页列表查询")
	 @ApiOperation(value="店铺商品列表-分页列表查询", notes="店铺商品列表-分页列表查询")
	 @GetMapping(value = "/listNew")
	 public Result<?> queryPageListNew(GoodList goodList,
									@RequestParam(name = "level",defaultValue = "-1",required = false) String level,
									@RequestParam(name = "typeId",defaultValue = "",required = false) String typeId,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									HttpServletRequest req) {
		 QueryWrapper<GoodList> queryWrapper = QueryGenerator.initQueryWrapper(goodList, req.getParameterMap());
		 Map<String,Object> paramMap= Maps.newHashMap();
		 paramMap.put("level",level);
		 paramMap.put("typeId",typeId);
		 IPage<Map<String,Object>> pageList = goodStoreListService.queryPageList(new Page<>(pageNo,pageSize),paramMap, queryWrapper);
		 //组织返回的数据
		 pageList.getRecords().forEach(g->{
			 //图片数据格式转换
			 if(g.get("mainPicture")!=null){
				 g.put("mainPicture",goodUtils.imgTransition(g.get("mainPicture").toString()));
			 }
			 if(g.get("detailsGoods")!=null){
				 g.put("detailsGoods",goodUtils.imgTransition(g.get("detailsGoods").toString()));
			 }


			 if(g.get("specifications")==null){
				 if(g.get("specification")!=null){
					 g.put("specifications",goodUtils.oldSpecificationToNew(g.get("specification").toString(),false));
				 }else{
					 g.put("specifications",goodUtils.oldSpecificationToNew("",false));
				 }
			 }
			 List<Map<String,Object>> shopInfo= Lists.newArrayList();
			 List<Map<String,Object>> specificationsDecribes=Lists.newArrayList();

			 List<GoodStoreSpecification> goodSpecifications=iGoodStoreSpecificationService.list(new LambdaQueryWrapper<GoodStoreSpecification>()
					 .eq(GoodStoreSpecification::getGoodStoreListId,g.get("id"))
					 .orderByDesc(GoodStoreSpecification::getCreateTime));

			 Map<String,Object> shopInfoMap=Maps.newHashMap();

			 if(goodSpecifications.size()>0) {

				 if (g.get("isSpecification").toString().equals("0")) {
					 GoodStoreSpecification goodSpecification = goodSpecifications.get(0);
					 shopInfoMap.put("salesPrice", goodSpecification.getPrice());
					 shopInfoMap.put("vipPrice", goodSpecification.getVipPrice());
					 shopInfoMap.put("costPrice", goodSpecification.getCostPrice());
					 shopInfoMap.put("repertory", goodSpecification.getRepertory());
					 shopInfoMap.put("weight", goodSpecification.getWeight());
					 shopInfoMap.put("skuNo", goodSpecification.getSkuNo());
				 }
				 if (g.get("isSpecification").toString().equals("1")) {

					 BigDecimal repertory = new BigDecimal(0);

					 for (GoodStoreSpecification goodSpecification : goodSpecifications) {
						 Map<String, Object> specificationsDecribeMap = Maps.newHashMap();

						 specificationsDecribeMap.put("pName", goodSpecification.getSpecification());
						 specificationsDecribeMap.put("salesPrice", goodSpecification.getPrice());
						 specificationsDecribeMap.put("vipPrice", goodSpecification.getVipPrice());
						 specificationsDecribeMap.put("costPrice", goodSpecification.getCostPrice());
						 specificationsDecribeMap.put("weight", goodSpecification.getWeight());
						 specificationsDecribeMap.put("repertory", goodSpecification.getRepertory());
						 specificationsDecribeMap.put("skuNo", goodSpecification.getSkuNo());
						 specificationsDecribeMap.put("imgUrl", goodSpecification.getSpecificationPicture());
						 repertory = repertory.add(goodSpecification.getRepertory());
						 specificationsDecribes.add(specificationsDecribeMap);
					 }

					 shopInfoMap.put("salesPrice", g.get("minPrice") + "-" + g.get("maxPrice"));
					 shopInfoMap.put("vipPrice", g.get("minVipPrice") + "-" + g.get("maxVipPrice"));
					 shopInfoMap.put("costPrice", g.get("minCostPrice") + "-" + g.get("maxCostPrice"));
					 shopInfoMap.put("repertory", repertory);
					 shopInfoMap.put("weight", "0");
					 shopInfoMap.put("skuNo", "");
				 }
			 }

			 shopInfo.add(shopInfoMap);


			 g.put("shopInfo",JSON.toJSONString(shopInfo));
			 g.put("specificationsDecribes",JSON.toJSONString(specificationsDecribes));

			 StoreManage storeManage=iStoreManageService.getStoreManageBySysUserId(g.get("sysUserId").toString());
			 g.put("storeManageId",storeManage.getId());
			 if (storeManage.getSubStoreName() == null) {
				 g.put("storeName", storeManage.getStoreName());
			 } else {
				 g.put("storeName", storeManage.getStoreName() + "(" + storeManage.getSubStoreName() + ")");
			 }
		 });
		 return Result.ok(pageList);
	 }


	
	/**
	  * 分页列表查询
	 * @param goodStoreListVo
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "店铺商品列表-分页列表查询")
	@ApiOperation(value="店铺商品列表-分页列表查询", notes="店铺商品列表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<GoodStoreListDto>> queryPageList(GoodStoreListVo goodStoreListVo,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="5") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<GoodStoreListDto>> result = new Result<IPage<GoodStoreListDto>>();
		Page<GoodStoreList> page = new Page<GoodStoreList>(pageNo, pageSize);
		//判断当前用户是否是品台，是 STR=NULL 不是STR= UserId
		String str = PermissionUtils.ifPlatform();
		if(StringUtils.isNotBlank(str)){
			goodStoreListVo.setSysUserId(str);
		}
		//goodStoreListVo.setAuditStatus("1");
		IPage<GoodStoreListDto> pageList= goodStoreListService.getGoodListDto(page, goodStoreListVo,"0");
		result.setSuccess(true);
		result.setResult(pageList);
		return result;

	}

	 @AutoLog(value = "店铺商品列表-分页列表查询")
	 @ApiOperation(value="店铺商品列表-分页列表查询", notes="店铺商品列表-分页列表查询")
	 @GetMapping(value = "/findStoreList")
	 public Result<List<GoodStoreDiscountDTO >> findStoreList(GoodStoreListVo goodStoreListVo) {
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 ISysUserRoleService iSysUserRoleService = SpringContextUtils.getBean(ISysUserRoleService.class);
		 List<String> roleByUserId = iSysUserRoleService.getRoleByUserId(sysUser.getId());
		 if (roleByUserId.contains("Merchant")){
		 	goodStoreListVo.setSysUserId(sysUser.getId());
		 }
		 Result<List<GoodStoreDiscountDTO >> result = new Result<List<GoodStoreDiscountDTO >>();
		 List<GoodStoreDiscountDTO > goodById = goodStoreListService.findStoreGoodList(goodStoreListVo);
		 result.setSuccess(true);
		 result.setResult(goodById);
		 return result;
	 }

	 /**
	  * 分页草稿箱列表查询
	  * @param goodStoreListVo
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "商品列表-分页列表查询")
	 @ApiOperation(value="商品列表-分页列表查询", notes="商品列表-分页列表查询")
	 @RequestMapping(value = "/GoodStoreDraftListList",method = RequestMethod.GET)
	 @PermissionData(pageComponent="good/GoodStoreDraftListList")
	 public Result<IPage<GoodStoreListDto>> queryPageDraftList(GoodStoreListVo goodStoreListVo,
													   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
													   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
													   HttpServletRequest req) {
		 Result<IPage<GoodStoreListDto>> result = new Result<IPage<GoodStoreListDto>>();
		 Page<GoodStoreList> page = new Page<GoodStoreList>(pageNo, pageSize);
		 //判断当前用户是否是品台，是 STR=NULL 不是STR= UserId
		 String str = PermissionUtils.ifPlatform();
		 if(StringUtils.isNotBlank(str)){
			 goodStoreListVo.setSysUserId(str);
		 }
		 goodStoreListVo.setAuditStatus("0");
		 IPage<GoodStoreListDto> pageList= goodStoreListService.getGoodListDto(page, goodStoreListVo,null);
		 result.setSuccess(true);
		 result.setResult(pageList);
		 return result;
	 }
	 /**
	  * 分页回收站列表查询
	  * @param goodStoreListVo
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "商品列表-分页列表查询")
	 @ApiOperation(value="商品列表-分页列表查询", notes="商品列表-分页列表查询")
	 @GetMapping(value = "/GoodStoreListRecycleList")
	 @PermissionData(pageComponent="good/GoodStoreListRecycleList")
	 public Result<IPage<GoodStoreListDto>> queryPageRecycleList(GoodStoreListVo goodStoreListVo,
														 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
														 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
														 HttpServletRequest req) {
		 Result<IPage<GoodStoreListDto>> result = new Result<IPage<GoodStoreListDto>>();
		 Page<GoodStoreList> page = new Page<GoodStoreList>(pageNo, pageSize);
		 //判断当前用户是否是品台，是 STR=NULL 不是STR= UserId
		 String str = PermissionUtils.ifPlatform();
		 if(StringUtils.isNotBlank(str)){
			 goodStoreListVo.setSysUserId(str);
		 }
		 goodStoreListVo.setDelFlag("1");
		 IPage<GoodStoreListDto> pageList= goodStoreListService.getGoodListDtoDelFlag(page, goodStoreListVo);
		 result.setSuccess(true);
		 result.setResult(pageList);
		 return result;
	 }

	 /**
	  * 分页待审核列表查询
	  *
	  * @param goodStoreListVo
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "商品列表-分页列表查询")
	 @ApiOperation(value = "商品列表-分页列表查询", notes = "商品列表-分页列表查询")
	 @GetMapping(value = "/GoodStoreAuditListList")
	 @PermissionData(pageComponent = "good/GoodStoreAuditListList")
	 public Result<IPage<GoodStoreListDto>> queryPageAuditList(GoodStoreListVo goodStoreListVo,
														  @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
														  @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
														  HttpServletRequest req) {
		 Result<IPage<GoodStoreListDto>> result = new Result<IPage<GoodStoreListDto>>();
		 Page<GoodStoreList> page = new Page<GoodStoreList>(pageNo, pageSize);

		 goodStoreListVo.setAuditStatus("1");
		 //判断当前用户是否是品台，是 STR=NULL 不是STR= UserId
		 String str = PermissionUtils.ifPlatform();
		 if(str!=null){
			 goodStoreListVo.setSysUserId(str);
		 }
		 IPage<GoodStoreListDto> pageList = goodStoreListService.getGoodListDto(page, goodStoreListVo, "0");
		 result.setSuccess(true);
		 result.setResult(pageList);
		 return result;
	 }



	 /**
	  *   添加
	 * @param goodStoreListNewDTO
	 * @return
	 */
	@AutoLog(value = "店铺商品列表-添加")
	@ApiOperation(value="店铺商品列表-添加", notes="店铺商品列表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody GoodStoreListNewDTO goodStoreListNewDTO) {
		log.info(JSON.toJSONString(goodStoreListNewDTO));
		GoodStoreList goodList=new GoodStoreList();

		/*上下架状态*/
		goodList.setFrameStatus(goodStoreListNewDTO.getFrameStatus());

		//审核状态
		goodList.setAuditStatus("2");

		//店铺
		if(StringUtils.isBlank(goodStoreListNewDTO.getStoreManageId())){
			return Result.error("请选择店铺");
		}

		StoreManage storeManage=iStoreManageService.getById(goodStoreListNewDTO.getStoreManageId());
		
		goodList.setSysUserId(storeManage.getSysUserId());


		//请选择运费模板
		if(StringUtils.isBlank(goodStoreListNewDTO.getStoreTemplateId())){
			return Result.error("请选择运费模板");
		}

		goodList.setStoreTemplateId(goodStoreListNewDTO.getStoreTemplateId());

		//市场价
		if(goodStoreListNewDTO.getMarketPrice()==null || goodStoreListNewDTO.getMarketPrice().doubleValue()==0){
			return Result.error("请填写市场价或者市场价必须高于0");
		}
		goodList.setMarketPrice(goodStoreListNewDTO.getMarketPrice().toString());
		

		//商品编号
		JSONArray shopInfo=JSON.parseArray(goodStoreListNewDTO.getShopInfo());
		JSONObject shopInfoo=(JSONObject)shopInfo.get(0);
		if(StringUtils.isNotBlank(goodStoreListNewDTO.getGoodNo())&&goodStoreListService.count(new LambdaQueryWrapper<GoodStoreList>().eq(GoodStoreList::getSysUserId,storeManage.getSysUserId()).eq(GoodStoreList::getGoodNo,goodStoreListNewDTO.getGoodNo()))>0){
			return Result.error("商品编号不能重复，请重新编写");
		}
		//商品编码
		goodList.setGoodNo(goodStoreListNewDTO.getGoodNo());
		//商品名称
		if(StringUtils.isBlank(goodStoreListNewDTO.getGoodName())){
			return Result.error("商品名称不能为空");
		}
		goodList.setGoodName(goodStoreListNewDTO.getGoodName());
		//商品分类
		if(StringUtils.isBlank(goodStoreListNewDTO.getGoodTypeId())){
			return Result.error("请选择商品分类");
		}
		goodList.setGoodStoreTypeId(goodStoreListNewDTO.getGoodTypeId());
		goodList.setGoodDescribe(goodStoreListNewDTO.getGoodDescribe());
		goodList.setMainPicture(goodStoreListNewDTO.getMainImages());
		goodList.setDetailsGoods(goodStoreListNewDTO.getDetailsImages());
		goodList.setCommitmentCustomers(goodStoreListNewDTO.getCommitmentCustomers());
		//是否有规格
		JSONArray specifications= JSON.parseArray(goodStoreListNewDTO.getSpecifications());
		goodList.setSpecifications(goodStoreListNewDTO.getSpecifications());
		goodList.setStatus(goodStoreListNewDTO.getStatus());


		/*添加搜索内容*/
		List<String> searchInfos=Lists.newArrayList();
		searchInfos.add(StringUtils.defaultString(goodList.getGoodName()));
		searchInfos.add(StringUtils.defaultString(goodList.getNickName()));
		searchInfos.add(StringUtils.defaultString(goodList.getGoodDescribe()));
		searchInfos.add(iGoodStoreTypeService.getById(goodList.getGoodStoreTypeId()).getName());


		if(specifications.size()==0){
			//无规格
			goodList.setIsSpecification("0");
		}else{
			//有规格
			goodList.setIsSpecification("1");
		}

		goodList.setSearchInfo(JSON.toJSONString(searchInfos));

		//保存数据
		if(goodStoreListService.save(goodList)){
			if(specifications.size()==0){
				//无规格
				GoodStoreSpecification goodSpecification=new GoodStoreSpecification();
				goodSpecification.setGoodStoreListId(goodList.getId());
				goodSpecification.setSpecification("无");
				goodSpecification.setPrice(shopInfoo.getBigDecimal("salesPrice"));
				goodSpecification.setVipPrice(shopInfoo.getBigDecimal("vipPrice"));
				goodSpecification.setCostPrice(shopInfoo.getBigDecimal("costPrice"));
				goodSpecification.setRepertory(shopInfoo.getBigDecimal("repertory"));
				goodSpecification.setSkuNo(shopInfoo.getString("skuNo"));
				goodSpecification.setWeight(new BigDecimal(shopInfoo.getString("weight")));
				iGoodStoreSpecificationService.save(goodSpecification);
			}else{
				//有规格
				JSONArray specificationsDecribes= JSON.parseArray(goodStoreListNewDTO.getSpecificationsDecribes());
				for (Object s :specificationsDecribes){
					JSONObject jsonObject=(JSONObject)s;
					GoodStoreSpecification goodSpecification=new GoodStoreSpecification();
					goodSpecification.setGoodStoreListId(goodList.getId());
					goodSpecification.setSpecification(jsonObject.getString("pName"));
					goodSpecification.setPrice(jsonObject.getBigDecimal("salesPrice"));
					goodSpecification.setVipPrice(jsonObject.getBigDecimal("vipPrice"));
					goodSpecification.setCostPrice(jsonObject.getBigDecimal("costPrice"));
					goodSpecification.setRepertory(jsonObject.getBigDecimal("repertory"));
					goodSpecification.setSkuNo(jsonObject.getString("skuNo"));
					goodSpecification.setWeight(new BigDecimal(jsonObject.getString("weight")));
					goodSpecification.setSpecificationPicture(jsonObject.getString("imgUrl"));
					iGoodStoreSpecificationService.save(goodSpecification);
				}
			}
		}else{
			return Result.error("未知错误，请联系管理员");
		}
		return Result.ok("添加成功！");
	}
	
	/**
	  *  编辑
	 * @param goodStoreListNewDTO
	 * @return
	 */
	@AutoLog(value = "店铺商品列表-编辑")
	@ApiOperation(value="店铺商品列表-编辑", notes="店铺商品列表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody GoodStoreListNewDTO goodStoreListNewDTO) {
		log.info(JSON.toJSONString(goodStoreListNewDTO));
		GoodStoreList goodList=goodStoreListService.getById(goodStoreListNewDTO.getId());


		/*上下架状态*/
		goodList.setFrameStatus(goodStoreListNewDTO.getFrameStatus());

		//审核状态
		goodList.setAuditStatus("2");


		//店铺
		if(StringUtils.isBlank(goodStoreListNewDTO.getStoreManageId())){
			return Result.error("请选择店铺");
		}

		StoreManage storeManage=iStoreManageService.getById(goodStoreListNewDTO.getStoreManageId());

		goodList.setSysUserId(storeManage.getSysUserId());



		//请选择运费模板
		if(StringUtils.isBlank(goodStoreListNewDTO.getStoreTemplateId())){
			return Result.error("请选择运费模板");
		}

		goodList.setStoreTemplateId(goodStoreListNewDTO.getStoreTemplateId());


		//市场价
		if(goodStoreListNewDTO.getMarketPrice()==null || goodStoreListNewDTO.getMarketPrice().doubleValue()==0){
			return Result.error("请填写市场价或者市场价必须高于0");
		}
		goodList.setMarketPrice(goodStoreListNewDTO.getMarketPrice().toString());



		//商品编号
		JSONArray shopInfo=JSON.parseArray(goodStoreListNewDTO.getShopInfo());
		JSONObject shopInfoo=(JSONObject)shopInfo.get(0);
		if(StringUtils.isNotBlank(goodStoreListNewDTO.getGoodNo())&&goodStoreListService.count(new LambdaQueryWrapper<GoodStoreList>().eq(GoodStoreList::getGoodNo,goodStoreListNewDTO.getGoodNo()).eq(GoodStoreList::getSysUserId,storeManage.getSysUserId()).ne(GoodStoreList::getId,goodStoreListNewDTO.getId()))>0){
			return Result.error("商品编号不能重复，请重新编写");
		}
		//商品编码
		goodList.setGoodNo(goodStoreListNewDTO.getGoodNo());
		//商品名称
		if(StringUtils.isBlank(goodStoreListNewDTO.getGoodName())){
			return Result.error("商品名称不能为空");
		}
		goodList.setGoodName(goodStoreListNewDTO.getGoodName());
		//商品分类
		if(StringUtils.isBlank(goodStoreListNewDTO.getGoodTypeId())){
			return Result.error("请选择商品分类");
		}
		goodList.setGoodStoreTypeId(goodStoreListNewDTO.getGoodTypeId());
		goodList.setGoodDescribe(goodStoreListNewDTO.getGoodDescribe());
		goodList.setMainPicture(goodStoreListNewDTO.getMainImages());
		goodList.setDetailsGoods(goodStoreListNewDTO.getDetailsImages());
		goodList.setCommitmentCustomers(goodStoreListNewDTO.getCommitmentCustomers());
		//是否有规格
		JSONArray specifications= JSON.parseArray(goodStoreListNewDTO.getSpecifications());
		goodList.setSpecifications(goodStoreListNewDTO.getSpecifications());
		goodList.setStatus(goodStoreListNewDTO.getStatus());


		/*添加搜索内容*/
		List<String> searchInfos=Lists.newArrayList();
		searchInfos.add(StringUtils.defaultString(goodList.getGoodName()));
		searchInfos.add(StringUtils.defaultString(goodList.getNickName()));
		searchInfos.add(StringUtils.defaultString(goodList.getGoodDescribe()));
		searchInfos.add(iGoodStoreTypeService.getById(goodList.getGoodStoreTypeId()).getName());


		if(specifications.size()==0){
			//无规格
			goodList.setIsSpecification("0");
		}else{
			//有规格
			goodList.setIsSpecification("1");
		}


		goodList.setSearchInfo(JSON.toJSONString(searchInfos));

		//保存数据
		if(goodStoreListService.saveOrUpdate(goodList)){
			//查询所有商品的规格信息
			Map<String,GoodStoreSpecification> goodSpecificationMap=Maps.newHashMap();
			iGoodStoreSpecificationService.list(new LambdaQueryWrapper<GoodStoreSpecification>().eq(GoodStoreSpecification::getGoodStoreListId,goodList.getId())).forEach(gs->{
				goodSpecificationMap.put(gs.getSpecification(),gs);
			});

			if(specifications.size()==0){
				//无规格
				GoodStoreSpecification goodSpecification=new GoodStoreSpecification();
				if(goodSpecificationMap.containsKey("无")){
					goodSpecification=goodSpecificationMap.get("无");
					goodSpecificationMap.remove("无");
				}
				goodSpecification.setGoodStoreListId(goodList.getId());
				goodSpecification.setSpecification("无");
				goodSpecification.setPrice(shopInfoo.getBigDecimal("salesPrice"));
				goodSpecification.setVipPrice(shopInfoo.getBigDecimal("vipPrice"));
				goodSpecification.setCostPrice(shopInfoo.getBigDecimal("costPrice"));
				goodSpecification.setRepertory(shopInfoo.getBigDecimal("repertory"));
				goodSpecification.setSkuNo(shopInfoo.getString("skuNo"));
				goodSpecification.setWeight(new BigDecimal(shopInfoo.getString("weight")));
				iGoodStoreSpecificationService.saveOrUpdate(goodSpecification);
			}else{
				//有规格
				JSONArray specificationsDecribes= JSON.parseArray(goodStoreListNewDTO.getSpecificationsDecribes());
				for (Object s :specificationsDecribes){
					JSONObject jsonObject=(JSONObject)s;
					GoodStoreSpecification goodSpecification=new GoodStoreSpecification();
					if(goodSpecificationMap.containsKey(jsonObject.getString("pName"))){
						goodSpecification=goodSpecificationMap.get(jsonObject.getString("pName"));
						goodSpecificationMap.remove(jsonObject.getString("pName"));
					}
					goodSpecification.setGoodStoreListId(goodList.getId());
					goodSpecification.setSpecification(jsonObject.getString("pName"));
					goodSpecification.setPrice(jsonObject.getBigDecimal("salesPrice"));
					goodSpecification.setVipPrice(jsonObject.getBigDecimal("vipPrice"));
					goodSpecification.setCostPrice(jsonObject.getBigDecimal("costPrice"));
					goodSpecification.setRepertory(jsonObject.getBigDecimal("repertory"));
					goodSpecification.setSkuNo(jsonObject.getString("skuNo"));
					goodSpecification.setWeight(new BigDecimal(jsonObject.getString("weight")));
					goodSpecification.setSpecificationPicture(jsonObject.getString("imgUrl"));
					iGoodStoreSpecificationService.saveOrUpdate(goodSpecification);
				}
			}
			//删除规格信息
			if(!goodSpecificationMap.isEmpty()){
				goodSpecificationMap.entrySet().forEach(gs->{
					iGoodStoreSpecificationService.removeById(gs.getValue().getId());
				});
			}
		}else{
			return Result.error("未知错误，请联系管理员");
		}
		return Result.ok("编辑成功!");
	}
	
	/**
	  *   通过id删除
	 * @param id
	 * @return
	 */
	@AutoLog(value = "店铺商品列表-通过id删除")
	@ApiOperation(value="店铺商品列表-通过id删除", notes="店铺商品列表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			goodStoreListService.removeById(id);
		} catch (Exception e) {
			log.error("删除失败",e.getMessage());
			return Result.error("删除失败!");
		}
		return Result.ok("删除成功!");
	}

	 /**
	  * 批量id查询修改删除状态：
	  * @param ids
	  * @return
	  */
	 @AutoLog(value = "商品管理-通过id查询")
	 @ApiOperation(value="商品管理-通过id查询", notes="商品管理-通过id查询")
	 @GetMapping(value = "/updateDelFlags")
	 public Result<GoodStoreList> updateDelFlags(@RequestParam(name="ids",required=true) String ids) {
		 Result<GoodStoreList> result = new Result<GoodStoreList>();
		 if(ids==null || "".equals(ids.trim())) {
			 result.error500("参数不识别！");
		 }else {
			 GoodStoreList goodStoreList;
			 try {
				 List<String> listid= Arrays.asList(ids.split(","));
				 //goodList.setDelFlag("0");
				 for (String id: listid){
					 goodStoreList = goodStoreListService.getGoodStoreListById(id);
					 if(goodStoreList==null){
						 result.error500("未找到对应实体");
					 }else{
						 goodStoreListService.updateDelFalg(goodStoreList,"0");
					 }
				 }
				 result.success("修改成功!");
			 }catch (Exception e){
				 result.error500("修改失败！");
			 }
		 }
		 return result;
	 }

	/**
	  *  批量删除
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "店铺商品列表-批量删除")
	@ApiOperation(value="店铺商品列表-批量删除", notes="店铺商品列表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<GoodStoreList> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<GoodStoreList> result = new Result<GoodStoreList>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.goodStoreListService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "店铺商品列表-通过id查询")
	@ApiOperation(value="店铺商品列表-通过id查询", notes="店铺商品列表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<GoodStoreList> queryById(@RequestParam(name="id",required=true) String id) {
		Result<GoodStoreList> result = new Result<GoodStoreList>();
		GoodStoreList goodStoreList = goodStoreListService.getById(id);
		if(goodStoreList==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(goodStoreList);
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
      QueryWrapper<GoodStoreList> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              GoodStoreList goodStoreList = JSON.parseObject(deString, GoodStoreList.class);
              queryWrapper = QueryGenerator.initQueryWrapper(goodStoreList, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<GoodStoreList> pageList = goodStoreListService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "店铺商品列表列表");
      mv.addObject(NormalExcelConstants.CLASS, GoodStoreList.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("店铺商品列表列表数据", "导出人:Jeecg", "导出信息"));
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
              List<GoodStoreList> listGoodStoreLists = ExcelImportUtil.importExcel(file.getInputStream(), GoodStoreList.class, params);
              goodStoreListService.saveBatch(listGoodStoreLists);
              return Result.ok("文件导入成功！数据行数:" + listGoodStoreLists.size());
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
	  * 通过id查询修改审核状态：
	  * @param id
	  * @return
	  */
	 @AutoLog(value = "商品管理-通过id查询")
	 @ApiOperation(value="商品管理-通过id查询", notes="广告管理-通过id查询")
	 @GetMapping(value = "/updateAuditStatus")
	 public Result<GoodStoreList> updateAuditStatus(@RequestParam(name="id",required=true) String id,@RequestParam(name="auditStatus") String auditStatus,String auditExplain) {
		 Result<GoodStoreList> result = new Result<GoodStoreList>();
		 GoodStoreList goodStoreList = goodStoreListService.getById(id);
		 if(goodStoreList==null) {
			 result.error500("未找到对应实体");
		 }else {
			 goodStoreList.setAuditExplain(auditExplain) ;
			 goodStoreList.setAuditStatus(auditStatus);
			 boolean ok = goodStoreListService.updateById(goodStoreList);
			 //TODO 返回false说明什么？
			 if(ok) {
				 result.success("修改成功!");
			 }else{
				 result.error500("修改失败！");
			 }
		 }
		 return result;
	 }

	 /**
	  * 通过id查询修改删除状态：
	  * @param id
	  * @return
	  */
	 @AutoLog(value = "商品管理-通过id查询")
	 @ApiOperation(value="商品管理-通过id查询", notes="商品管理-通过id查询")
	 @GetMapping(value = "/updateDelFlag")
	 public Result<GoodStoreList> updateDelFlag(@RequestParam(name="id",required=true) String id, String delExplain) {
		 Result<GoodStoreList> result = new Result<GoodStoreList>();
		 GoodStoreList goodList = goodStoreListService.getGoodStoreListById(id);
		 if(goodList==null) {
			 result.error500("未找到对应实体");
		 }else {
			 try{
				 goodList.setDelExplain(delExplain);
				 goodList.setDelTime(new Date());
				 //修改
				 goodStoreListService.updateById(goodList);
				 //删除
				 goodStoreListService.removeById(goodList.getId());
				 result.success("修改成功!");
			 }catch (Exception e){
				 result.error500("修改失败！");
			 }
		 }
		 return result;
	 }

	 /**
	  *  批量修改上机架
	  * @param ids
	  * @return
	  */
	 @AutoLog(value = "商品管理-通过id查询")
	 @ApiOperation(value="商品管理-通过id查询", notes="商品管理-通过id查询")
	 @GetMapping(value = "/updateFrameStatus")
	 public  Result<GoodStoreList> updateFrameStatus(@RequestParam(name="ids",required=true) String ids,@RequestParam(name="frameStatus",required=true)String frameStatus,String frameExplain){
		 Result<GoodStoreList> result = new Result<GoodStoreList>();
		 if(ids==null || "".equals(ids.trim())) {
			 result.error500("参数不识别！");
		 }else {
			 GoodStoreList goodStoreList;
			 try {
				 List<String> listid= Arrays.asList(ids.split(","));
				 for (String id: listid){
					 goodStoreList = goodStoreListService.getGoodStoreListById(id);
					 if(goodStoreList==null){
						 result.error500("未找到对应实体");
					 }else{
						 goodStoreList.setFrameExplain(frameExplain);
						 goodStoreList.setFrameStatus(frameStatus);
						 goodStoreListService.updateById(goodStoreList);
					 }
				 }
				 result.success("修改成功!");
			 }catch (Exception e){
				 result.error500("修改失败！");
			 }
		 }
		 return result;
	 }

	 /**
	  * 通过id查询修改审核状态：
	  * @param
	  * @return
	  */
	 /**
	  *
	  * @param json ID +AuditStatus
	  * @return
	  */
	 @AutoLog(value = "商品管理-通过id查询")
	 @ApiOperation(value="商品管理-通过id查询", notes="商品管理-通过id查询")
	 // @GetMapping(value = "/updateAuditStatus")
	 @RequestMapping(value = "/updateAuditStatusPL", method = RequestMethod.PUT)
	 public Result<GoodStoreList> updateAuditStatusPL(@RequestBody JSONObject json) {
		 Result<GoodStoreList> result = new Result<GoodStoreList>();
		 String ids = json.getString("id");
		 String auditStatus = json.getString("auditStatus");
		 String auditExplain = json.getString("auditExplain");
		 if (StringUtils.isBlank(auditStatus)) {
			 result.error500("审核状态不能为空！");
			 return result;
		 }
		 if (StringUtils.isEmpty(ids)) {
			 result.error500("用户id不能为空！");
			 return result;
		 }
		 GoodStoreList goodStoreList;
		 try {
			 List<String> listid = Arrays.asList(ids.split(","));
			 for (String id : listid) {
				 goodStoreList = goodStoreListService.getById(id);
				 if (goodStoreList == null) {
					 result.error500("未找到对应实体");
				 } else {

					 goodStoreList.setAuditStatus(auditStatus);
					 goodStoreList.setAuditExplain(auditExplain);
					 goodStoreListService.updateById(goodStoreList);
					 result.success("修改成功!");

				 }
			 }
		 }catch (Exception e){
			 e.printStackTrace();
			 result.error500("修改失败！");
		 }
		 return result;
	 }
	 /**
	  * 通过id查询修改状态:启用停用
	  * @param id
	  * @return
	  */
	 @AutoLog(value = "商品管理-通过id查询")
	 @ApiOperation(value="商品管理-通过id查询", notes="商品管理-通过id查询")
	 @GetMapping(value = "/updateStatus")
	 public Result<GoodStoreList> updateStatus(@RequestParam(name="id",required=true) String id,@RequestParam(name="statusExplain") String statusExplain) {
		 Result<GoodStoreList> result = new Result<GoodStoreList>();
		 GoodStoreList goodStoreList = goodStoreListService.getById(id);
		 if(goodStoreList==null) {
			 result.error500("未找到对应实体");
		 }else {
			 if("1".equals(goodStoreList.getStatus())){
				 goodStoreList.setStatus("0");
				 goodStoreList.setStatusExplain(statusExplain);
			 }else{
				 goodStoreList.setStatus("1");
				 goodStoreList.setStatusExplain(statusExplain);
			 }
			 boolean ok = goodStoreListService.updateById(goodStoreList);
			 //TODO 返回false说明什么？
			 if(ok) {
				 result.success("修改成功!");
			 }else{
				 result.error500("修改失败！");
			 }
		 }
		 return result;
	 }
	 /**
	  *   通过id删除,添加删除原因
	  * @param id
	  * @return
	  */
	 @AutoLog(value = "商品列表-通过id删除")
	 @ApiOperation(value="商品列表-通过id删除", notes="商品列表-通过id删除")
	 @DeleteMapping(value = "/deleteAndDelExplain")
	 public Result<?> deleteAndDelExplain(@RequestParam(name="id",required=true) String id,@RequestParam(name="delExplain",required=true) String delExplain) {
		 try {
			 GoodStoreList goodStoreList = goodStoreListService.getById(id);
			 goodStoreList.setDelExplain(delExplain);
			 Date date = new Date();
			 goodStoreList.setDelTime(date);
			 //goodList.setDelFlag("1");
			 goodStoreListService.updateById(goodStoreList);
			 goodStoreListService.removeById(id);
		 } catch (Exception e) {
			 log.error("删除失败",e.getMessage());
			 return Result.error("删除失败!");
		 }
		 return Result.ok("删除成功!");
	 }

	 /**
	  * 返回商品列表
	  * @param goodStoreList
	  * @param rep
	  * @return
	  */
	 @AutoLog(value = "商品列表-放回list集合")
	 @ApiOperation(value="商品列表-放回list集合", notes="商品列表-放回list集合")
	 @RequestMapping(value = "queryList",method = RequestMethod.GET)
	 public List<GoodStoreList> queryList(GoodStoreList goodStoreList,
										  HttpServletRequest rep){
		 QueryWrapper<GoodStoreList> queryWrapper = QueryGenerator.initQueryWrapper(goodStoreList, rep.getParameterMap());
		 List<GoodStoreList> list = goodStoreListService.list(queryWrapper);
		 return list;
	 }


	 /**
	  * 复制商品地址
	  * @param goodId
	  * @return
	  */
	 @AutoLog(value = "商品列表-分页列表查询")
	 @ApiOperation(value = "商品列表-分页列表查询", notes = "商品列表-分页列表查询")
	 @GetMapping(value = "/getGoodUrl")
	 public  Result<Map<String,Object>> getGoodUrl(String goodId) {
		 Result<Map<String,Object>> result = new Result<>();
		 Map<String,String> map = Maps.newHashMap();
		 map.put("goodId",goodId);
		 map.put("isPlatform","0");
		 String url = "goodAction/pages/product/product?info=";
		 Map<String,Object> mapObject = Maps.newHashMap();
		 mapObject.put("url",url);
		 mapObject.put("parameter",JSONObject.toJSONString(map));
		 result.setResult(mapObject);
		 result.setSuccess(true);
		 return result;
	 }
}
