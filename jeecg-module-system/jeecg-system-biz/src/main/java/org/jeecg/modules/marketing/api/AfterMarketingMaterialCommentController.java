package org.jeecg.modules.marketing.api;

import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.marketing.entity.MarketingMaterialComment;
import org.jeecg.modules.marketing.service.IMarketingMaterialCommentService;
import org.jeecg.utils.aliyun.TextContentUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.Map;

@RequestMapping("after/marketingMaterialComment")
@Controller
public class AfterMarketingMaterialCommentController {
    @Autowired
    private IMarketingMaterialCommentService iMarketingMaterialCommentService;

    @Autowired
    private TextContentUtils textContentUtils;

    /**
     * 添加评论信息
     * @param marketingMaterialListId 素材id
     * @param id  父级id,对素材评论 不传
     * @param byReplyId  回复评论id
     * @param content 评论内容
     * @return
     */
    @RequestMapping("addMarketingMaterialComment")
    @ResponseBody
 public Result<Map<String,Object>> addMarketingMaterialComment(String marketingMaterialListId,
                                                               String id,
                                                               String byReplyId,
                                                               String content,
                                                               @RequestAttribute(required = false,name = "memberId")String memberId){

     Result<Map<String,Object>> result =new Result<>();
     Map<String,Object> objectMap = Maps.newHashMap();
     if(StringUtils.isBlank(marketingMaterialListId) ){
         return result.error500("素材id不能为空!");
     }
     if(StringUtils.isBlank(content) ){
         return result.error500("评论内容不能为空!");
     }

        try {
            if(!textContentUtils.sensitiveWord(content)){
                return result.error500("内容存在敏感词，请修改后再提交！！！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //登录会员id
     MarketingMaterialComment marketingMaterialComment = new MarketingMaterialComment();
     marketingMaterialComment.setMarketingMaterialList(marketingMaterialListId);//素材id
     marketingMaterialComment.setMemberListId(memberId);//会员id
     if(StringUtils.isNotBlank(id)){
         marketingMaterialComment.setParentId(id);//父级id
     }
     if(StringUtils.isNotBlank(byReplyId)){
         marketingMaterialComment.setByReplyId(byReplyId);//回复谁的评论id
     }
     marketingMaterialComment.setContent(content);//评论内容
     marketingMaterialComment.setCommentTime(new Date());//评论时间
     marketingMaterialComment.setStatus("0");//审核状态:0：待审核；1：审核通过；2：审核不通过',
     iMarketingMaterialCommentService.save(marketingMaterialComment) ;
     result.success("评论成功!");

     return result;
 }

}
