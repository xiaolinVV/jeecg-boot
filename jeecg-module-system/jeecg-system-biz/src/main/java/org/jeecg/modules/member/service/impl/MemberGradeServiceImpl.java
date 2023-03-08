package org.jeecg.modules.member.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.modules.good.entity.GoodSetting;
import org.jeecg.modules.good.entity.GoodSpecification;
import org.jeecg.modules.good.service.IGoodSettingService;
import org.jeecg.modules.good.service.IGoodSpecificationService;
import org.jeecg.modules.member.dto.MemberGradeDTO;
import org.jeecg.modules.member.entity.MemberGrade;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.entity.MemberShoppingCart;
import org.jeecg.modules.member.mapper.MemberGradeMapper;
import org.jeecg.modules.member.service.IMemberGradeService;
import org.jeecg.modules.member.service.IMemberListService;
import org.jeecg.modules.member.vo.MemberGradeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Description: 会员等级
 * @Author: jeecg-boot
 * @Date:   2020-06-11
 * @Version: V1.0
 */
@Service
public class MemberGradeServiceImpl extends ServiceImpl<MemberGradeMapper, MemberGrade> implements IMemberGradeService {

    @Autowired
    private IMemberListService iMemberListService;

    @Autowired
    private IGoodSettingService iGoodSettingService;

    @Autowired
    private IGoodSpecificationService iGoodSpecificationService;

    @Override
    public IPage<MemberGradeVO> queryPageList(Page<MemberGrade> page, MemberGradeDTO memberGradeDTO) {
        return baseMapper.queryPageList(page,memberGradeDTO);
    }

    @Override
    public List<Map<String, Object>> findMemberGradeListMap() {
        return baseMapper.findMemberGradeListMap();
    }

    @Override
    public List<Map<String, String>> getReferMemberGradeList() {
        return baseMapper.getReferMemberGradeList();
    }

    @Override
    public void settingGoodInfo(Map<String, Object> resultMap, GoodSpecification goodSpecification,String memberId) {
        if(StringUtils.isBlank(memberId)){
            return;
        }
        GoodSetting goodSetting=iGoodSettingService.getGoodSetting();
        if(goodSetting.getViewVip().equals("0")){
            return;
        }
        if(goodSetting.getOpenMemberGrade().equals("0")){
            return;
        }
        MemberList memberList=iMemberListService.getById(memberId);
        if(memberList.getMemberType().equals("0")){
            return;
        }
        if(StringUtils.isBlank(memberList.getMemberGradeId())){
            return;
        }
        MemberGrade memberGrade=this.getById(memberList.getMemberGradeId());
        if(memberGrade==null){
            return;
        }
        int sort=memberGrade.getSort().intValue();
        if(sort==0){
            resultMap.put("smallPrice",goodSpecification.getVipPrice());
            resultMap.put("label",memberGrade.getGradeName());
            resultMap.put("isViewVipPrice","0");
        }
        if(sort==1){
            resultMap.put("smallPrice",goodSpecification.getVipTwoPrice());
            resultMap.put("label",memberGrade.getGradeName());
            resultMap.put("isViewVipPrice","0");
        }
        if(sort==2){
            resultMap.put("smallPrice",goodSpecification.getVipThirdPrice());
            resultMap.put("label",memberGrade.getGradeName());
            resultMap.put("isViewVipPrice","0");
        }
        if(sort>=3){
            resultMap.put("smallPrice",goodSpecification.getVipFouthPrice());
            resultMap.put("label",memberGrade.getGradeName());
            resultMap.put("isViewVipPrice","0");
        }

    }

    @Override
    public void settingGoodListInfo(Map<String, Object> resultMap, String goodId,String memberId) {
        if(StringUtils.isBlank(memberId)){
            return;
        }
        GoodSetting goodSetting=iGoodSettingService.getGoodSetting();
        if(goodSetting.getViewVip().equals("0")){
            return;
        }
        if(goodSetting.getOpenMemberGrade().equals("0")){
            return;
        }
        MemberList memberList=iMemberListService.getById(memberId);
        if(memberList.getMemberType().equals("0")){
            return;
        }
        if(StringUtils.isBlank(memberList.getMemberGradeId())){
            return;
        }
        MemberGrade memberGrade=this.getById(memberList.getMemberGradeId());
        if(memberGrade==null){
            return;
        }
        int sort=memberGrade.getSort().intValue();
        GoodSpecification goodSpecification=iGoodSpecificationService.getSmallGoodSpecification(goodId);
        if(sort==0){
            resultMap.put("smallPrice",goodSpecification.getVipPrice());
            resultMap.put("prefectureLabel",memberGrade.getGradeName());
            resultMap.put("isViewVipPrice","0");
        }
        if(sort==1){
            resultMap.put("smallPrice",goodSpecification.getVipTwoPrice());
            resultMap.put("prefectureLabel",memberGrade.getGradeName());
            resultMap.put("isViewVipPrice","0");
        }
        if(sort==2){
            resultMap.put("smallPrice",goodSpecification.getVipThirdPrice());
            resultMap.put("prefectureLabel",memberGrade.getGradeName());
            resultMap.put("isViewVipPrice","0");
        }
        if(sort>=3){
            resultMap.put("smallPrice",goodSpecification.getVipFouthPrice());
            resultMap.put("prefectureLabel",memberGrade.getGradeName());
            resultMap.put("isViewVipPrice","0");
        }
    }

    @Override
    public void settingGoodSpecificationInfo(Map<String, Object> resultMap, GoodSpecification goodSpecification, String memberId) {
        if(StringUtils.isBlank(memberId)){
            return;
        }
        GoodSetting goodSetting=iGoodSettingService.getGoodSetting();
        if(goodSetting.getViewVip().equals("0")){
            return;
        }
        if(goodSetting.getOpenMemberGrade().equals("0")){
            return;
        }
        MemberList memberList=iMemberListService.getById(memberId);
        if(memberList.getMemberType().equals("0")){
            return;
        }
        if(StringUtils.isBlank(memberList.getMemberGradeId())){
            return;
        }
        MemberGrade memberGrade=this.getById(memberList.getMemberGradeId());
        if(memberGrade==null){
            return;
        }
        int sort=memberGrade.getSort().intValue();
        if(sort==0){
            resultMap.put("price",goodSpecification.getVipPrice());
        }
        if(sort==1){
            resultMap.put("price",goodSpecification.getVipTwoPrice());
        }
        if(sort==2){
            resultMap.put("price",goodSpecification.getVipThirdPrice());
        }
        if(sort>=3){
            resultMap.put("price",goodSpecification.getVipFouthPrice());
        }

    }

    @Override
    public void settingMemberShopCardinfo(MemberShoppingCart memberShoppingCart, GoodSpecification goodSpecification, String memberId) {
        if(StringUtils.isBlank(memberId)){
            return;
        }
        GoodSetting goodSetting=iGoodSettingService.getGoodSetting();
        if(goodSetting.getViewVip().equals("0")){
            return;
        }
        if(goodSetting.getOpenMemberGrade().equals("0")){
            return;
        }
        MemberList memberList=iMemberListService.getById(memberId);
        if(memberList.getMemberType().equals("0")){
            return;
        }
        if(StringUtils.isBlank(memberList.getMemberGradeId())){
            return;
        }
        MemberGrade memberGrade=this.getById(memberList.getMemberGradeId());
        if(memberGrade==null){
            return;
        }
        int sort=memberGrade.getSort().intValue();
        if(sort==0){
            memberShoppingCart.setAddPrice(goodSpecification.getVipPrice());
            memberShoppingCart.setPrefectureLabel(memberGrade.getGradeName());
        }
        if(sort==1){
            memberShoppingCart.setAddPrice(goodSpecification.getVipTwoPrice());
            memberShoppingCart.setPrefectureLabel(memberGrade.getGradeName());
        }
        if(sort==2){
            memberShoppingCart.setAddPrice(goodSpecification.getVipThirdPrice());
            memberShoppingCart.setPrefectureLabel(memberGrade.getGradeName());
        }
        if(sort>=3){
            memberShoppingCart.setAddPrice(goodSpecification.getVipFouthPrice());
            memberShoppingCart.setPrefectureLabel(memberGrade.getGradeName());
        }
    }

    @Override
    public void settingGetMemberShopCardinfo(Map<String, Object> resultMap, GoodSpecification goodSpecification, String memberId) {
        if(StringUtils.isBlank(memberId)){
            return;
        }
        GoodSetting goodSetting=iGoodSettingService.getGoodSetting();
        if(goodSetting.getViewVip().equals("0")){
            return;
        }
        if(goodSetting.getOpenMemberGrade().equals("0")){
            return;
        }
        MemberList memberList=iMemberListService.getById(memberId);
        if(memberList.getMemberType().equals("0")){
            return;
        }
        if(StringUtils.isBlank(memberList.getMemberGradeId())){
            return;
        }
        MemberGrade memberGrade=this.getById(memberList.getMemberGradeId());
        if(memberGrade==null){
            return;
        }
        int sort=memberGrade.getSort().intValue();
        if(sort==0){
            resultMap.put("price",goodSpecification.getVipPrice());
            resultMap.put("label",memberGrade.getGradeName());
        }
        if(sort==1){
            resultMap.put("price",goodSpecification.getVipTwoPrice());
            resultMap.put("label",memberGrade.getGradeName());
        }
        if(sort==2){
            resultMap.put("price",goodSpecification.getVipThirdPrice());
            resultMap.put("label",memberGrade.getGradeName());
        }
        if(sort>=3){
            resultMap.put("price",goodSpecification.getVipFouthPrice());
            resultMap.put("label",memberGrade.getGradeName());
        }
    }
}
