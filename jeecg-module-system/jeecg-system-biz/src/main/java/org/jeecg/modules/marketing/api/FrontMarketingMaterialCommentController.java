package org.jeecg.modules.marketing.api;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.marketing.entity.MarketingMaterialComment;
import org.jeecg.modules.marketing.service.IMarketingMaterialCommentService;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.service.IMemberListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@RequestMapping("front/marketingMaterialComment")
@Controller
public class FrontMarketingMaterialCommentController {


    @Autowired
    private IMarketingMaterialCommentService iMarketingMaterialCommentService;
    @Autowired
    private IMemberListService iMemberListService;
    /**
     * 根据素材id查询评论数据
     * @param marketingMaterialListId
     * @param pageNo
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping("getMarketingMaterialCommentList")
    @ResponseBody
    public Result<Map<String,Object>> getMarketingMaterialCommentList(String marketingMaterialListId,
                                                                      @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                                      @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                                      HttpServletRequest request ){




        Result<Map<String,Object>> result =new Result<>();
        Map<String,Object> objectMap = Maps.newHashMap();
        if(StringUtils.isBlank(marketingMaterialListId) ){
            return result.error500("素材id不能为空!");
        }

        //登录会员id
        Object memberId= request.getAttribute("memberId");
        Map<String,Object> member = null;
        //用户信息
        if(memberId!=null){
            QueryWrapper<MemberList> queryWrapperMemberList =new QueryWrapper<MemberList>();
            queryWrapperMemberList.select("id,head_portrait as headPortrait,nick_name AS nickName");
            queryWrapperMemberList.eq("id",memberId);
            member = iMemberListService.getMap(queryWrapperMemberList);
        }


        //评论总数
        long commentCount = iMarketingMaterialCommentService.count(new LambdaQueryWrapper<MarketingMaterialComment>()
                .ne(MarketingMaterialComment::getStatus,"2")
                .eq(MarketingMaterialComment::getMarketingMaterialList,marketingMaterialListId));
        Map<String,Object> paramMap = Maps.newHashMap();
        paramMap.put("marketingMaterialListId",marketingMaterialListId);
        //组织查询参数
        Page<Map<String,Object>> page = new Page<Map<String,Object>>(pageNo, pageSize);
        //一级评论
        IPage<Map<String,Object>> marketingMaterialCommentPage = iMarketingMaterialCommentService.getMarketingMaterialCommentMaps(page,paramMap);

        marketingMaterialCommentPage.getRecords().forEach(mmc->{
            paramMap.put("parentId",mmc.get("id"));
            Page<Map<String,Object>> page1 = new Page<Map<String,Object>>(1, 2);
            //二级评论
            IPage<Map<String,Object>> marketingMaterialCommentTwoPage = iMarketingMaterialCommentService.getMarketingMaterialCommentTwoMaps(page1,paramMap);
            mmc.put("marketingMaterialCommentTwo",marketingMaterialCommentTwoPage.getRecords());
           //二级评论数量
            mmc.put("commentTwoCount",marketingMaterialCommentTwoPage.getTotal());
            if(marketingMaterialCommentTwoPage.getRecords().size()>2){
                mmc.put("isUnfold",1);//展开 : 0 :不需要展开 1:需展开
            }else{
                mmc.put("isUnfold",0);//展开 : 0 :不需要展开 1:需展开
            }
        });


        //发送数据处理
        objectMap.put("marketingMaterialCommentPage",marketingMaterialCommentPage);//评论一二级数据
        //评论总是大于一万
        if(commentCount>10000){
            objectMap.put("commentCount",(new BigDecimal(commentCount).divide(new BigDecimal(10000),2, RoundingMode.HALF_UP))+"万");//评论总数   ;

        }else{
            objectMap.put("commentCount",commentCount);//评论总数   ;
        }

        objectMap.put("member",member);//会员信息
        result.success("查询评论成功!");
        result.setResult(objectMap);
     return result;
    }

    /**
     * 评论数据展开数据
     * @param marketingMaterialListId 素材id
     * @param commentId 评论id 父级id
     * @param pageNo
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping("getMarketingMaterialCommentUnfold")
    @ResponseBody
    public Result<Map<String,Object>> getMarketingMaterialCommentUnfold(String marketingMaterialListId,
                                                                      String commentId,
                                                                      @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                                      @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                                      HttpServletRequest request ){

        Result<Map<String,Object>> result =new Result<>();
        Map<String,Object> objectMap = Maps.newHashMap();
        Map<String,Object> paramMap = Maps.newHashMap();
        if(StringUtils.isBlank(marketingMaterialListId)){
           return  result.error500("素材id不能为空!") ;
        }
        if(StringUtils.isBlank(commentId)){
            return  result.error500("评论 id 不能为空!") ;
        }
        paramMap.put("marketingMaterialListId",marketingMaterialListId);
        paramMap.put("parentId",commentId);
        Page<Map<String,Object>> page1 = new Page<Map<String,Object>>(pageNo, pageSize);
        //二级评论
        IPage<Map<String,Object>> marketingMaterialCommentTwoPage = iMarketingMaterialCommentService.getMarketingMaterialCommentTwoMaps(page1,paramMap);
        objectMap.put("marketingMaterialCommentTwo",marketingMaterialCommentTwoPage.getRecords());//评论一二级数据

        result.setResult(objectMap);
        result.success("查询成功!");
        return result;
    }

}
