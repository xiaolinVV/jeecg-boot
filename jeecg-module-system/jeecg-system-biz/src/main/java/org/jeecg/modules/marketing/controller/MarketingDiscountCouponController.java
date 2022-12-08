package org.jeecg.modules.marketing.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.OrderNoUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.good.dto.GoodListDto;
import org.jeecg.modules.good.dto.GoodStoreListDto;
import org.jeecg.modules.good.entity.GoodList;
import org.jeecg.modules.good.entity.GoodStoreList;
import org.jeecg.modules.good.service.IGoodListService;
import org.jeecg.modules.good.service.IGoodStoreListService;
import org.jeecg.modules.good.vo.GoodListVo;
import org.jeecg.modules.good.vo.GoodStoreListVo;
import org.jeecg.modules.marketing.dto.MarketingCertificateRecordDTO;
import org.jeecg.modules.marketing.dto.MarketingDiscountCouponDTO;
import org.jeecg.modules.marketing.entity.*;
import org.jeecg.modules.marketing.service.*;
import org.jeecg.modules.marketing.vo.MarketingDiscountCouponVO;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.service.IMemberListService;
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
import java.util.*;
/*

 *
 * @Description: 优惠券记录
 * @Author: jeecg-boot
 * @Date:   2019-11-12
 * @Version: V1.0
 */

@Slf4j
@Api(tags = "优惠券记录")
@RestController
@RequestMapping("/marketingDiscountCoupon/marketingDiscountCoupon")
public class MarketingDiscountCouponController {
	@Autowired
	private IMarketingDiscountCouponService marketingDiscountCouponService;
	@Autowired
	private IMarketingCertificateRecordService marketingCertificateRecordService;
	@Autowired
	private IMarketingDiscountGoodService marketingDiscountGoodService;
	@Autowired
	private IGoodStoreListService goodStoreListService;
	@Autowired
	private IMarketingCertificateGoodService marketingCertificateGoodService;
	@Autowired
	private IGoodListService goodListService;
	@Autowired
	private ISysUserRoleService iSysUserRoleService;
	@Autowired
	private IMemberListService iMemberListService;
	@Autowired
	private IMarketingDiscountService iMarketingDiscountService;

    @Autowired
    private IMarketingChannelService iMarketingChannelService;
/*
*
	  * 分页列表查询
	 * @param marketingDiscountCoupon
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
*/

    @AutoLog(value = "优惠券记录-分页列表查询")
    @ApiOperation(value = "优惠券记录-分页列表查询", notes = "优惠券记录-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<MarketingDiscountCoupon>> queryPageList(MarketingDiscountCoupon marketingDiscountCoupon,
                                                                @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                                @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                                HttpServletRequest req) {
        Result<IPage<MarketingDiscountCoupon>> result = new Result<IPage<MarketingDiscountCoupon>>();
        QueryWrapper<MarketingDiscountCoupon> queryWrapper = QueryGenerator.initQueryWrapper(marketingDiscountCoupon, req.getParameterMap());
        Page<MarketingDiscountCoupon> page = new Page<MarketingDiscountCoupon>(pageNo, pageSize);
        IPage<MarketingDiscountCoupon> pageList = marketingDiscountCouponService.page(page, queryWrapper);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }
    /*

     *
     *   添加
     * @param marketingDiscountCoupon
     * @return
     */

    @AutoLog(value = "优惠券记录-添加")
    @ApiOperation(value = "优惠券记录-添加", notes = "优惠券记录-添加")
    @PostMapping(value = "/add")
    public Result<MarketingDiscountCoupon> add(@RequestBody MarketingDiscountCoupon marketingDiscountCoupon) {
        Result<MarketingDiscountCoupon> result = new Result<MarketingDiscountCoupon>();
        try {
            marketingDiscountCouponService.save(marketingDiscountCoupon);
            result.success("添加成功！");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }
    /*

     *
     *  编辑
     * @param marketingDiscountCoupon
     * @return
     */

    @AutoLog(value = "优惠券记录-编辑")
    @ApiOperation(value = "优惠券记录-编辑", notes = "优惠券记录-编辑")
    @PutMapping(value = "/edit")
    public Result<MarketingDiscountCoupon> edit(@RequestBody MarketingDiscountCoupon marketingDiscountCoupon) {
        Result<MarketingDiscountCoupon> result = new Result<MarketingDiscountCoupon>();
        MarketingDiscountCoupon marketingDiscountCouponEntity = marketingDiscountCouponService.getById(marketingDiscountCoupon.getId());
        if (marketingDiscountCouponEntity == null) {
            result.error500("未找到对应实体");
        } else {
            boolean ok = marketingDiscountCouponService.updateById(marketingDiscountCoupon);
            //TODO 返回false说明什么？
            if (ok) {
                result.success("修改成功!");
            }
        }

        return result;
    }

    /*
     *
     *   通过id删除
     * @param id
     * @return

     */

    @AutoLog(value = "优惠券记录-通过id删除")
    @ApiOperation(value = "优惠券记录-通过id删除", notes = "优惠券记录-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        try {
            marketingDiscountCouponService.removeById(id);
        } catch (Exception e) {
            log.error("删除失败", e.getMessage());
            return Result.error("删除失败!");
        }
        return Result.ok("删除成功!");
    }

    /*
     *
     *  批量删除
     * @param ids
     * @return
     */

    @AutoLog(value = "优惠券记录-批量删除")
    @ApiOperation(value = "优惠券记录-批量删除", notes = "优惠券记录-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<MarketingDiscountCoupon> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        Result<MarketingDiscountCoupon> result = new Result<MarketingDiscountCoupon>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别！");
        } else {
            this.marketingDiscountCouponService.removeByIds(Arrays.asList(ids.split(",")));
            result.success("删除成功!");
        }
        return result;
    }

    /*
     *
     * 通过id查询
     * @param id
     * @return
     */

    @AutoLog(value = "优惠券记录-通过id查询")
    @ApiOperation(value = "优惠券记录-通过id查询", notes = "优惠券记录-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<MarketingDiscountCoupon> queryById(@RequestParam(name = "id", required = true) String id) {
        Result<MarketingDiscountCoupon> result = new Result<MarketingDiscountCoupon>();
        MarketingDiscountCoupon marketingDiscountCoupon = marketingDiscountCouponService.getById(id);
        if (marketingDiscountCoupon == null) {
            result.error500("未找到对应实体");
        } else {
            result.setResult(marketingDiscountCoupon);
            result.setSuccess(true);
        }
        return result;
    }

    /*
     *
     * 导出excel
     *
     * @param request
     * @param response

     */

    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, HttpServletResponse response) {
        // Step.1 组装查询条件
        QueryWrapper<MarketingDiscountCoupon> queryWrapper = null;
        try {
            String paramsStr = request.getParameter("paramsStr");
            if (oConvertUtils.isNotEmpty(paramsStr)) {
                String deString = URLDecoder.decode(paramsStr, "UTF-8");
                MarketingDiscountCoupon marketingDiscountCoupon = JSON.parseObject(deString, MarketingDiscountCoupon.class);
                queryWrapper = QueryGenerator.initQueryWrapper(marketingDiscountCoupon, request.getParameterMap());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //Step.2 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        List<MarketingDiscountCoupon> pageList = marketingDiscountCouponService.list(queryWrapper);
        //导出文件名称
        mv.addObject(NormalExcelConstants.FILE_NAME, "优惠券记录列表");
        mv.addObject(NormalExcelConstants.CLASS, MarketingDiscountCoupon.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("优惠券记录列表数据", "导出人:Jeecg", "导出信息"));
        mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
        return mv;
    }

    /*
     *
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
                List<MarketingDiscountCoupon> listMarketingDiscountCoupons = ExcelImportUtil.importExcel(file.getInputStream(), MarketingDiscountCoupon.class, params);
                marketingDiscountCouponService.saveBatch(listMarketingDiscountCoupons);
                return Result.ok("文件导入成功！数据行数:" + listMarketingDiscountCoupons.size());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return Result.error("文件导入失败:" + e.getMessage());
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
	 * 核销查询
	 * 根据券号查询
	 * @param qqzixuangu
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
  @GetMapping(value = "/couponVerification")
  public  Result<?> couponVerification(String qqzixuangu,
															   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
															   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
															   HttpServletRequest req) {
	  Result<IPage<MarketingDiscountCouponDTO>> result = new Result<IPage<MarketingDiscountCouponDTO>>();
	  try {
	  	//优惠券（先查询无优惠券，则查询兑换券）
		  Page<MarketingDiscountCouponVO> page = new Page<MarketingDiscountCouponVO>(pageNo, pageSize);
		  LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		  String userId = sysUser.getId();
		  IPage<MarketingDiscountCouponDTO> pageList = marketingDiscountCouponService.couponVerification(page,qqzixuangu,userId);
		  result.setResult(pageList);
		  if(pageList.getRecords().size()>0){

		  	if(pageList.getRecords().get(0).getStatus().equals("1")){
				result.setResult(pageList);
				result.setSuccess(true);
				//0:未生效；1：已生效（未使用）；2：已使用；3：已过期；4：已失效;
			}else if(pageList.getRecords().get(0).getStatus().equals("0")){
				result.error500("您的优惠券未生效！");
			}else if(pageList.getRecords().get(0).getStatus().equals("2")){
				result.error500("您的优惠券已使用！");
			}else if(pageList.getRecords().get(0).getStatus().equals("3")){
				result.error500("您的优惠券已过期！");
			}else if(pageList.getRecords().get(0).getStatus().equals("4")){
				result.error500("您的优惠券已失效！");
			}

		  }else{
		  	//兑换券（查询无优惠券，则查询兑换券）
			  Result<IPage<MarketingCertificateRecordDTO>> result1 = new Result<IPage<MarketingCertificateRecordDTO>>();
			  Page<MarketingCertificateRecord> page1 = new Page<MarketingCertificateRecord>(pageNo, pageSize);
			  IPage<MarketingCertificateRecordDTO> pageList1 =  marketingCertificateRecordService.couponVerification(page1,qqzixuangu,userId);
              if(pageList1.getRecords().size()>0){

				  if(pageList1.getRecords().get(0).getStatus().equals("1")){
					  result1.setResult(pageList1);
					  //0:未生效；1：已生效（未使用）；2：已使用；3：已过期；4：已失效;
				  }else if(pageList1.getRecords().get(0).getStatus().equals("0")){
					  result1.error500("您的兑换券未生效！");
				  }else if(pageList1.getRecords().get(0).getStatus().equals("2")){
					  result1.error500("您的兑换券已使用！");
				  }else if(pageList1.getRecords().get(0).getStatus().equals("3")){
					  result1.error500("您的兑换券已过期！");
				  }else if(pageList1.getRecords().get(0).getStatus().equals("4")){
					  result1.error500("您的兑换券已失效！");
				  }
				//  result1.setResult(pageList1);

			  }else{
				  result1.error500("本店铺未查到该优惠券/兑换券！");
			  }
			  return result1;
		  }
	  }catch (Exception e){
	  	e.printStackTrace();
		  result.error500("请求出错！");
	  }
	  return result;
  }




    /**
     * 核销优惠券或兑换券
     * @param id
     * @param isDiscount
     * @return
     */
    @GetMapping(value = "/cancelAfterVerification")
    public Result<?>  cancelAfterVerification(@RequestParam("id")String id,@RequestParam("isDiscount")String  isDiscount){
        Result<IPage<MarketingDiscountCouponDTO>> result = new Result<IPage<MarketingDiscountCouponDTO>>();
        if(isDiscount.equals("0") ){
            //优惠券
            MarketingDiscountCoupon  marketingDiscountCoupon=  marketingDiscountCouponService.getById(id);
            if(marketingDiscountCoupon == null){
                result.error500("未找到对应实体");
            }else{
                marketingDiscountCoupon.setStatus("2");
                marketingDiscountCoupon.setVerification("1");
                marketingDiscountCouponService.updateById(marketingDiscountCoupon);
                result.success("修改成功!");
            }

        }else if(isDiscount.equals("1")){
            //兑换券
            MarketingCertificateRecord	  marketingCertificateRecord =  marketingCertificateRecordService.getById(id);
            if(marketingCertificateRecord == null ){
                result.error500("未找到对应实体");
            }else{
                marketingCertificateRecord.setStatus("2");
                marketingCertificateRecord.setVerificationPeople("1");
                marketingCertificateRecord.setVerification("1");
                marketingCertificateRecord.setUserTime(new Date());
                marketingCertificateRecordService.updateById(marketingCertificateRecord);
                result.success("修改成功!");
            }

	  }
       return result;
  }
	@AutoLog(value = "优惠券适用商品记录-分页列表查询")
	@ApiOperation(value="优惠券适用商品记录-分页列表查询", notes="优惠券记录-分页列表查询")
	@GetMapping(value = "/cancelAfterVerificationGoodList")
public Result<?>  cancelAfterVerificationGoodList(GoodStoreListVo goodStoreListVo,
												  @RequestParam("marketingDiscountCouponId")String marketingDiscountCouponId,
												  @RequestParam("isDiscount")String  isDiscount,
												  String  goodName,
												  String goodStoreTypeIdOne,
												   String goodStoreTypeIdTwo,
												   String goodStoreTypeIdThree,
												  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
												  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
												  HttpServletRequest req){
        Result<IPage<T>> result1 = new Result<IPage<T>>();
        //优惠券商品
		if("0".equals(isDiscount)){
			Result<IPage<GoodStoreListDto>> result = new Result<IPage<GoodStoreListDto>>();
			MarketingDiscountCoupon marketingDiscountCoupon = marketingDiscountCouponService.getById(marketingDiscountCouponId);
			QueryWrapper<MarketingDiscountGood> queryWrapper = new QueryWrapper<MarketingDiscountGood>();
			queryWrapper.eq("marketing_discount_id",marketingDiscountCoupon.getMarketingDiscountId());
			List<MarketingDiscountGood> marketingDiscountGoodList = marketingDiscountGoodService.list(queryWrapper);
			if(marketingDiscountGoodList.size()>0){
				List<String> str =new ArrayList<>();
				for(MarketingDiscountGood mdg:marketingDiscountGoodList){
					str.add(mdg.getGoodId());
				}
				Page<GoodStoreList> page = new Page<GoodStoreList>(pageNo, pageSize);
			//	GoodStoreListVo goodStoreListVo =new GoodStoreListVo();
				//添加查询条件
				goodStoreListVo.setStrings(str);
				if(goodName!=null || !"".equals(goodName)){  goodStoreListVo.setGoodForm(goodName); }
				if(goodStoreTypeIdOne!=null || !"".equals(goodStoreTypeIdOne) ){  goodStoreListVo.setGoodStoreTypeIdOne(goodStoreTypeIdOne); }
				if(goodStoreTypeIdTwo!=null ||  !"".equals(goodStoreTypeIdTwo)){  goodStoreListVo.setGoodStoreTypeIdTwo(goodStoreTypeIdTwo); }
				IPage<GoodStoreListDto> pageList = goodStoreListService.getGoodListDto(page, goodStoreListVo, "0");
				result.setSuccess(true);
				result.setResult(pageList);
				return result;
			}
		 }else {
			//兑换券商品信息
			Result<IPage<GoodListDto>> result = new Result<IPage<GoodListDto>>();
			MarketingCertificateRecord marketingCertificateRecord = marketingCertificateRecordService.getById(marketingDiscountCouponId);
			QueryWrapper<MarketingCertificateGood> queryWrapper = new QueryWrapper<MarketingCertificateGood>();
			if(marketingCertificateRecord !=null){
                queryWrapper.eq("marketing_certificate_id",marketingCertificateRecord.getMarketingCertificateId());
                List<MarketingCertificateGood> marketingCertificateGoodList = marketingCertificateGoodService.list(queryWrapper);
                if(marketingCertificateGoodList.size()>0){
                    List<String> str =new ArrayList<>();
                    for(MarketingCertificateGood mcg:marketingCertificateGoodList){
                        str.add(mcg.getGoodListId());
                    }
                    Page<GoodList> page = new Page<GoodList>(pageNo, pageSize);
                    GoodListVo goodListVo =new GoodListVo();
                    //添加查询条件
                    goodListVo.setStrings(str);
                    if(goodName!=null || !"".equals(goodName)){  goodListVo.setGoodForm(goodName); }
                    if(goodStoreTypeIdOne!=null || !"".equals(goodStoreTypeIdOne) ){  goodListVo.setGoodTypeIdOne(goodStoreTypeIdOne); }
                    if(goodStoreTypeIdTwo!=null ||  !"".equals(goodStoreTypeIdTwo)){  goodListVo.setGoodTypeIdTwo(goodStoreTypeIdTwo); }
                    if(goodStoreTypeIdThree!=null ||  !"".equals(goodStoreTypeIdThree)){  goodListVo.setGoodTypeIdThree(goodStoreTypeIdThree); }
                    IPage<GoodListDto> pageList = goodListService.getGoodListDto(page, goodListVo, "0");
                    result.setSuccess(true);
                    result.setResult(pageList);
                    return result;
                }
            }

		}
	return result1;
}

    @AutoLog(value = "店铺优惠券记录表-分页列表查询")
    @ApiOperation(value = "优惠券记录表-分页列表查询", notes = "优惠券记录表-分页列表查询")
    @GetMapping(value = "/findDiscountCoupon")
    public Result<IPage<MarketingDiscountCouponVO>> findDiscountCoupon(MarketingDiscountCouponVO marketingDiscountCouponVO,
                                                                       @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                                                       @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        Result<IPage<MarketingDiscountCouponVO>> result = new Result<>();
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String userId = sysUser.getId();
        List<String> roleByUserId = iSysUserRoleService.getRoleByUserId(userId);
        if (roleByUserId.contains("Merchant")){
            marketingDiscountCouponVO.setUid(userId);
        }
        Page<MarketingDiscountCouponVO> page = new Page<>(pageNo, pageSize);
        IPage<MarketingDiscountCouponVO> discountCoupon = marketingDiscountCouponService.findDiscountCoupon(page, marketingDiscountCouponVO);
        result.setSuccess(true);
        result.setResult(discountCoupon);
        return result;
    }
    @AutoLog(value = "平台优惠券记录表-分页列表查询")
    @ApiOperation(value = "平台优惠券记录表-分页列表查询", notes = "平台优惠券记录表-分页列表查询")
    @GetMapping(value = "/findDiscountCouponTarrace")
    public Result<IPage<MarketingDiscountCouponVO>> findDiscountCouponTarrace(MarketingDiscountCouponVO marketingDiscountCouponVO,
                                                                       @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                                                       @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        Result<IPage<MarketingDiscountCouponVO>> result = new Result<>();
        Page<MarketingDiscountCouponVO> page = new Page<>(pageNo, pageSize);
        IPage<MarketingDiscountCouponVO> discountCoupon = marketingDiscountCouponService.findDiscountCouponTarrace(page, marketingDiscountCouponVO);
        result.setSuccess(true);
        result.setResult(discountCoupon);
        return result;
    }

    @PostMapping("giveMarketingDiscount")
    public Result<String> giveMarketingDiscount(@RequestBody MarketingDiscountCouponDTO marketingDiscountCouponDTO){
        Result<String> result = new Result<>();
        MemberList memberList = iMemberListService.getById(marketingDiscountCouponDTO.getMemberListId());
        if (oConvertUtils.isEmpty(memberList)){
            return result.error500("未找到该会员");
        }
        List<MarketingGiftBagDiscount> marketingGiftBagDiscountList = marketingDiscountCouponDTO.getMarketingGiftBagDiscountList();
        if (marketingGiftBagDiscountList.size()<=0){
            return result.error500("请选择优惠券!");
        }
        marketingGiftBagDiscountList.forEach(mdl->{
            MarketingDiscount marketingDiscount = iMarketingDiscountService.getById(mdl.getMarketingDiscountId());
            if (oConvertUtils.isNotEmpty(marketingDiscount)){
                int discountCount=mdl.getDistributedAmount().intValue();
                Calendar myCalendar = Calendar.getInstance();

                while (discountCount>0){
                    //优惠券量不足时候跳过领取
                    if (marketingDiscount.getTotal().subtract(marketingDiscount.getReleasedQuantity()).longValue() <= 0) {
                        continue;
                    }
                    MarketingDiscountCoupon marketingDiscountCoupon = new MarketingDiscountCoupon();
                    marketingDiscountCoupon.setDelFlag("0");
                    marketingDiscountCoupon.setPrice(marketingDiscount.getSubtract());
                    marketingDiscountCoupon.setName(marketingDiscount.getName());
                    marketingDiscountCoupon.setIsThreshold(marketingDiscount.getIsThreshold());
                    marketingDiscountCoupon.setMemberListId(memberList.getId());
                    marketingDiscountCoupon.setSysUserId(marketingDiscount.getSysUserId());
                    marketingDiscountCoupon.setQqzixuangu(OrderNoUtils.getOrderNo());
                    marketingDiscountCoupon.setMarketingDiscountId(marketingDiscount.getId());
                    marketingDiscountCoupon.setIsPlatform(marketingDiscount.getIsPlatform());
                    marketingDiscountCoupon.setCompletely(marketingDiscount.getCompletely());
                    marketingDiscountCoupon.setIsGive(marketingDiscount.getIsGive());
                    marketingDiscountCoupon.setIsWarn(marketingDiscount.getIsWarn());
                    marketingDiscountCoupon.setWarnDays(marketingDiscount.getWarnDays());
                    marketingDiscountCoupon.setUserRestrict(marketingDiscount.getUserRestrict());
                    marketingDiscountCoupon.setDiscountExplain(marketingDiscount.getDiscountExplain());
                    marketingDiscountCoupon.setCoverPlan(marketingDiscount.getCoverPlan());
                    marketingDiscountCoupon.setPosters(marketingDiscount.getPosters());
                    marketingDiscountCoupon.setMainPicture(marketingDiscount.getMainPicture());

                    //平台渠道判断
                    QueryWrapper<MarketingChannel> marketingChannelQueryWrapper = new QueryWrapper<>();
                    marketingChannelQueryWrapper.eq("english_name", "NORMAL_TO_GET");
                    MarketingChannel marketingChannel = iMarketingChannelService.getOne(marketingChannelQueryWrapper);
                    if (marketingChannel != null) {
                        marketingDiscountCoupon.setMarketingChannelId(marketingChannel.getId());
                        marketingDiscountCoupon.setTheChannel(marketingChannel.getName());
                    }
                    //标准用券方式
                    if (marketingDiscount.getVouchersWay().equals("0")) {
                        //优惠券的时间生成
                        marketingDiscountCoupon.setStartTime(marketingDiscount.getStartTime());
                        marketingDiscountCoupon.setEndTime(marketingDiscount.getEndTime());
                    }

                    //领券当日起
                    if (marketingDiscount.getVouchersWay().equals("1")) {
                        //优惠券的时间生成
                        if(mdl.getValidityType().equals("1")) {
                            myCalendar.setTime(new Date());
                        }
                        marketingDiscountCoupon.setStartTime(myCalendar.getTime());

                        if (marketingDiscount.getMonad().equals("天")) {
                            myCalendar.add(Calendar.DATE, marketingDiscount.getDisData().intValue());
                        }
                        if (marketingDiscount.getMonad().equals("周")) {
                            myCalendar.add(Calendar.WEEK_OF_MONTH, marketingDiscount.getDisData().intValue());
                        }
                        if (marketingDiscount.getMonad().equals("月")) {
                            myCalendar.add(Calendar.MONTH, marketingDiscount.getDisData().intValue());
                        }

                        marketingDiscountCoupon.setEndTime(myCalendar.getTime());
                    }
                    //领券次日起
                    if (marketingDiscount.getVouchersWay().equals("2")) {
                        //优惠券的时间生成
                        if(mdl.getValidityType().equals("1")) {
                            myCalendar.setTime(new Date());
                        }
                        myCalendar.add(Calendar.DATE, 1);
                        marketingDiscountCoupon.setStartTime(myCalendar.getTime());

                        if (marketingDiscount.getMonad().equals("天")) {
                            myCalendar.add(Calendar.DATE, marketingDiscount.getDisData().intValue());
                        }
                        if (marketingDiscount.getMonad().equals("周")) {
                            myCalendar.add(Calendar.WEEK_OF_MONTH, marketingDiscount.getDisData().intValue());
                        }
                        if (marketingDiscount.getMonad().equals("月")) {
                            myCalendar.add(Calendar.MONTH, marketingDiscount.getDisData().intValue());
                        }

                        marketingDiscountCoupon.setEndTime(myCalendar.getTime());
                    }

                    if (new Date().getTime() >= marketingDiscountCoupon.getStartTime().getTime() && new Date().getTime() <= marketingDiscountCoupon.getEndTime().getTime()) {
                        //设置生效
                        marketingDiscountCoupon.setStatus("1");
                    } else {
                        marketingDiscountCoupon.setStatus("0");
                    }
                    marketingDiscountCouponService.save(marketingDiscountCoupon);
                    marketingDiscount.setReleasedQuantity(marketingDiscount.getReleasedQuantity().add(new BigDecimal(1)));
                    iMarketingDiscountService.saveOrUpdate(marketingDiscount);
                    discountCount--;
                }
            }
        });

        return result.success("赠送成功!");
    }
}
