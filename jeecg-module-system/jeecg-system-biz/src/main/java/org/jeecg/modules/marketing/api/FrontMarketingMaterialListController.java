package org.jeecg.modules.marketing.api;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.marketing.entity.MarketingMaterialBrowse;
import org.jeecg.modules.marketing.entity.MarketingMaterialComment;
import org.jeecg.modules.marketing.entity.MarketingMaterialDianzan;
import org.jeecg.modules.marketing.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RequestMapping("front/marketingMaterialList")
@Controller
public class FrontMarketingMaterialListController {

    @Autowired
    private IMarketingMaterialListService iMarketingMaterialListService;
    @Autowired
    private IMarketingMaterialDianzanService iMarketingMaterialDianzanService;
    @Autowired
    private IMarketingMaterialGoodService iMarketingMaterialGoodService;
    @Autowired
    private IMarketingMaterialBrowseService  iMarketingMaterialBrowseService;
    @Autowired
    private IMarketingMaterialRecommendService marketingMaterialRecommendService;
    @Autowired
    private IMarketingMaterialCommentService iMarketingMaterialCommentService;

    /**
     * 搜索框搜索素材列表
     * @param search 搜索框搜索
     * @param pattern 排序 0:综合；1：点赞数；2：浏览量;3:最新；
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("searchMarketingMaterialList")
    @ResponseBody
    public Result<IPage<Map<String,Object>>> searchMarketingMaterialList(String search,
                                                                        @RequestParam(name="pattern") Integer pattern,
                                                                        @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                                        @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                                  HttpServletRequest request){
        //登录会员id
        Object memberId= request.getAttribute("memberId");

        Result<IPage<Map<String,Object>>> result=new Result<>();
        Map<String,Object>  objectMap = Maps.newHashMap();

        Map<String,Object> paramMap = Maps.newHashMap();
        if(org.apache.commons.lang3.StringUtils.isBlank(search)){
            result.error500("查询  素材名称 不能为空！！！   ");
            return  result;
        }
        paramMap.put("search",search);
        paramMap.put("pattern",pattern);
        //组织查询参数
        Page<Map<String,Object>> page = new Page<Map<String,Object>>(pageNo, pageSize);
        //素材列表参数
        IPage<Map<String,Object>>  marketingMaterialPage  =  iMarketingMaterialListService.searchMarketingMaterial(page,paramMap);
        if(memberId!=null){
            marketingMaterialPage.getRecords().forEach(mmp->{
                    long count = iMarketingMaterialDianzanService.count(new LambdaQueryWrapper<MarketingMaterialDianzan>().eq(MarketingMaterialDianzan::getMarketingMaterialListId,mmp.get("id"))
                            .eq(MarketingMaterialDianzan::getMemberListId,memberId));
                    if(count>0){
                        mmp.put("isDianzan","1" );  //已点赞
                    }
            });
        }


        result.setResult(marketingMaterialPage);
        result.success("查询素材列表成功!");
   return result;
    }

    /**
     * 根据任何类型查询素材信息
     * @param marketingMaterialColumnId
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("findMarketingMaterialColumn")
    @ResponseBody
    public Result<IPage<Map<String,Object>>> findMarketingMaterialColumn(String marketingMaterialColumnId,
                                                                         @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                                       @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                                  HttpServletRequest request){
        Result<IPage<Map<String,Object>>> result=new Result<>();
        Map<String,Object> paramMap = Maps.newHashMap();

        //登录会员id
        Object memberId= request.getAttribute("memberId");
        IPage<Map<String,Object>>  marketingMaterialPage =null ;
        //组织查询参数
        Page<Map<String,Object>> page = new Page<Map<String,Object>>(pageNo, pageSize);
        page.setSearchCount(false);
//        if(StringUtils.isBlank(marketingMaterialColumnId)){
//            //推荐素材
//          List<String> stringList = marketingMaterialRecommendService.getMarketingMaterialListIdList();
//              paramMap.put("strings",stringList);
//              marketingMaterialPage  =  iMarketingMaterialListService.findMarketingMaterial(page,paramMap);
//        }else{
            paramMap.put("marketingMaterialColumnId",marketingMaterialColumnId);
            marketingMaterialPage  =  iMarketingMaterialListService.findMarketingMaterial(page,paramMap);

//        }

        if(memberId!=null){
            marketingMaterialPage.getRecords().forEach(mmp->{
                long count = iMarketingMaterialDianzanService.count(new LambdaQueryWrapper<MarketingMaterialDianzan>().eq(MarketingMaterialDianzan::getMarketingMaterialListId,mmp.get("id"))
                        .eq(MarketingMaterialDianzan::getMemberListId,memberId));
                if(count>0){
                    mmp.put("isDianzan","1" );  //已点赞
                }
            });
        }

        result.success("查询素材成功!");
        result.setResult(marketingMaterialPage);

       return result;
    }

    /**
     * 素材详情
     * @param id
     * @param request
     * @return
     */
    @RequestMapping("getMarketingMaterialById")
    @ResponseBody
    public Result<Map<String,Object>> getMarketingMaterialById(String id,HttpServletRequest request){
        Result<Map<String,Object>> result=new Result<>();
        Map<String,Object> paramMap = Maps.newHashMap();
        Map<String,Object> objectMap = Maps.newHashMap();
        if(StringUtils.isBlank(id)){
            return result.error500("素材id 不能为空!");
        }
        //登录会员id
        Object memberId= request.getAttribute("memberId");
        //详情信息
        Map<String,Object>  marketingMaterialMap =  iMarketingMaterialListService.getMarketingMaterialById(id);
        //推荐商品信息
        List<Map<String,Object>>  marketingMaterialGoodMapList = iMarketingMaterialGoodService.getMarketingMaterialGoodMap(marketingMaterialMap.get("id").toString());
        marketingMaterialMap.put("marketingMaterialGoodMapList",marketingMaterialGoodMapList);

        //评论总数
        marketingMaterialMap.put("commentCount",iMarketingMaterialCommentService.count(new LambdaQueryWrapper<MarketingMaterialComment>().eq(MarketingMaterialComment::getMarketingMaterialList,id)
                .ne(MarketingMaterialComment::getStatus,"2")));


       //添加点赞标识
        if(memberId!=null){
            long count = iMarketingMaterialDianzanService.count(new LambdaQueryWrapper<MarketingMaterialDianzan>().eq(MarketingMaterialDianzan::getMarketingMaterialListId,marketingMaterialMap.get("id").toString())
                        .eq(MarketingMaterialDianzan::getMemberListId,memberId));
                if(count>0){
                    marketingMaterialMap.put("isDianzan","1" );  //已点赞
                }
        }
      //添加素材浏览记录
        if(memberId!=null){
        MarketingMaterialBrowse  marketingMaterialBrowse = new MarketingMaterialBrowse();
        marketingMaterialBrowse.setMarketingMaterialListId(marketingMaterialMap.get("id").toString());
        marketingMaterialBrowse.setMemberListId(memberId.toString());
        marketingMaterialBrowse.setBrowseTime(new Date());
        iMarketingMaterialBrowseService.save(marketingMaterialBrowse);
        }
        result.setResult(marketingMaterialMap);
        result.success("获取素材详情信息成功!");
      return result;
    }


}