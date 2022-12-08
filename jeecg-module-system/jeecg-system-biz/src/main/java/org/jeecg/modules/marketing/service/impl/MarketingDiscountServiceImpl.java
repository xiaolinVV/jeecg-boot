package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.marketing.dto.MarketingDiscountDTO;
import org.jeecg.modules.marketing.entity.MarketingChannelDiscount;
import org.jeecg.modules.marketing.entity.MarketingDiscount;
import org.jeecg.modules.marketing.entity.MarketingDiscountGood;
import org.jeecg.modules.marketing.mapper.MarketingChannelDiscountMapper;
import org.jeecg.modules.marketing.mapper.MarketingDiscountGoodMapper;
import org.jeecg.modules.marketing.mapper.MarketingDiscountMapper;
import org.jeecg.modules.marketing.service.IMarketingChannelDiscountService;
import org.jeecg.modules.marketing.service.IMarketingDiscountGoodService;
import org.jeecg.modules.marketing.service.IMarketingDiscountService;
import org.jeecg.modules.marketing.vo.MarketingDiscountVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Description: 优惠券
 * @Author: jeecg-boot
 * @Date: 2019-11-11
 * @Version: V1.0
 */
@Service
public class MarketingDiscountServiceImpl extends ServiceImpl<MarketingDiscountMapper,MarketingDiscount> implements IMarketingDiscountService {
    @Autowired(required = false)
    private MarketingDiscountMapper marketingDiscountMapper;
    @Autowired(required = false)
    private MarketingDiscountGoodMapper marketingDiscountGoodMapper;
    @Autowired(required = false)
    private MarketingChannelDiscountMapper marketingChannelDiscountMapper;
    @Autowired
    private IMarketingDiscountGoodService iMarketingDiscountGoodService;

    @Autowired
    private IMarketingChannelDiscountService iMarketingChannelDiscountService;
    /**
     * 删除并添加删除说明
     * @param id
     * @param delExplain
     * @return
     */
    @Override
    public String renoveById(String id, String delExplain) {
        try {
            marketingDiscountMapper.updateMarketingDiscountById(id, delExplain);
            return "删除成功";
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @Override
    public IPage<Map<String, Object>> findMarketingDiscountByIsThreshold(Page<Map<String, Object>> page, String isThreshold,String name) {
        return marketingDiscountMapper.findMarketingDiscountByIsThreshold(page,isThreshold,name);
    }

    @Override
    public Map<String, Object> findMarketingDiscountById(String id) {
        return marketingDiscountMapper.findMarketingDiscountById(id);
    }

    /**
     * 优惠券根据商品id分页查询
     * @param page
     * @param paramMap
     * @return
     */
    @Override
    public IPage<Map<String, Object>> findMarketingDiscountByGoodId(Page<Map<String, Object>> page,Map<String,Object> paramMap) {
        return marketingDiscountMapper.findMarketingDiscountByGoodId(page, paramMap);
    }

    @Override
    public IPage<Map<String, Object>> findMarketingDiscountBySysUserId(Page<Map<String, Object>> page,   String sysUserId) {
        return marketingDiscountMapper.findMarketingDiscountBySysUserId(page,sysUserId);
    }

    /**
     * 优惠券新增

     */
    @Override
    public void savaMarketingDiscount(MarketingDiscount MarketingDiscount, String goodStoreListIds, String channelIds,String isPlatform) {
        //获取当前用户
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        //判断如果没有sysUserId就存入当前登录人id
        /*if (MarketingDiscount.getSysUserId()==null||"".equals(MarketingDiscount.getSysUserId())){
            MarketingDiscount.setSysUserId(sysUser.getId());
        }*/
        if (StringUtils.isBlank(MarketingDiscount.getSysUserId())){
            MarketingDiscount.setSysUserId(sysUser.getId());
        }
        //插入优惠券
        marketingDiscountMapper.insert(MarketingDiscount);
        //获取优惠券id
        String id = MarketingDiscount.getId();
        if (StringUtils.isNotBlank(goodStoreListIds)){
            List<String> goodStoreIds = Arrays.asList(StringUtils.split(goodStoreListIds, ","));
            //添加优惠券and商品中间表
            for (String goodStoreId : goodStoreIds) {
                MarketingDiscountGood marketingDiscountGood = new MarketingDiscountGood();
                marketingDiscountGood.setMarketingDiscountId(id);
                marketingDiscountGood.setGoodId(goodStoreId);
                marketingDiscountGood.setDelFlag("0");
                marketingDiscountGood.setIsPlatform(isPlatform);
                marketingDiscountGoodMapper.insert(marketingDiscountGood);
            }
        }

        if (StringUtils.isNotBlank(channelIds)) {
            List<String> mIds = Arrays.asList(StringUtils.split(channelIds, ","));
            //添加投放渠道and优惠券中间表
            for (String mId : mIds) {
                MarketingChannelDiscount marketingChannelDiscount = new MarketingChannelDiscount();
                marketingChannelDiscount.setMarketingDiscountId(id);
                marketingChannelDiscount.setMarketingChannelId(mId);
                marketingChannelDiscount.setDelFlag("0");
                marketingChannelDiscountMapper.insert(marketingChannelDiscount);
            }
        }
    }


    /**
     * 平台优惠券列表查询
     * @param page
     * @param marketingDiscountVO
     * @return
     */
    @Override
    public IPage<MarketingDiscountDTO> getMarketingDiscountList(Page<MarketingDiscountDTO> page, MarketingDiscountVO marketingDiscountVO) {
        return marketingDiscountMapper.getMarketingDiscountList(page, marketingDiscountVO);
    }

    @Override
    public MarketingDiscountDTO findMarketingDiscountDTO(String id) {
        return marketingDiscountMapper.findMarketingDiscountDTO(id);
    }

    @Override
    public List<MarketingDiscountVO> findMarketingDiscountVO(MarketingDiscountVO marketingDiscountVO) {
        return marketingDiscountMapper.findMarketingDiscountVO(marketingDiscountVO);
    }

    @Override
    public IPage<MarketingDiscountDTO> findMarketingDiscountStoreList(Page<MarketingDiscountDTO> page,MarketingDiscountVO marketingDiscountVO) {
        IPage<MarketingDiscountDTO> marketingDiscountStoreList = baseMapper.findMarketingDiscountStoreList(page,marketingDiscountVO);
        return marketingDiscountStoreList;
    }

    @Override
    public Result<MarketingDiscountDTO> edit(MarketingDiscountVO marketingDiscountVO) {
        Result<MarketingDiscountDTO> result = new Result<MarketingDiscountDTO>();
        String id = marketingDiscountVO.getId();
        MarketingDiscount MarketingDiscount = new MarketingDiscount();
        MarketingDiscount marketingDiscountId = this.getById(id);
        if (oConvertUtils.isEmpty(marketingDiscountId)) {
            result.error500("未找到对应实体");
        } else {
            String goodListIds = marketingDiscountVO.getGoodStoreListIds();
            String channelIds = marketingDiscountVO.getMarketingChannelId();
            String isPlatform = marketingDiscountVO.getIsPlatform();
            BeanUtils.copyProperties(marketingDiscountVO, MarketingDiscount);
            if("1".equals(marketingDiscountVO.getVouchersWay())){
                MarketingDiscount.setDisData(marketingDiscountVO.getToday());
            }
            if("2".equals(marketingDiscountVO.getVouchersWay())){
                MarketingDiscount.setDisData(marketingDiscountVO.getTomorow());
            }
            MarketingDiscount.setMainPicture(marketingDiscountVO.getMainPictures());
            MarketingDiscount.setCoverPlan(marketingDiscountVO.getCoverPlans());
            if (StringUtils.isNotBlank(goodListIds)) {
                //查询出对应商品id
                QueryWrapper<MarketingDiscountGood> marketingDiscountGoodQueryWrapper = new QueryWrapper<>();
                marketingDiscountGoodQueryWrapper.eq("marketing_discount_id", marketingDiscountId.getId());
                //删除对应商品
                iMarketingDiscountGoodService.remove(marketingDiscountGoodQueryWrapper);
                List<String> goodIds = Arrays.asList(StringUtils.split(goodListIds, ","));
                //重新添加对应商品
                for (String goodListId : goodIds) {
                    MarketingDiscountGood marketingDiscountGood = new MarketingDiscountGood();
                    marketingDiscountGood.setMarketingDiscountId(marketingDiscountId.getId());
                    marketingDiscountGood.setGoodId(goodListId);
                    marketingDiscountGood.setIsPlatform(isPlatform);
                    iMarketingDiscountGoodService.saveOrUpdate(marketingDiscountGood);
                }
            }else {
                //查询出对应商品id
                QueryWrapper<MarketingDiscountGood> marketingDiscountGoodQueryWrapper = new QueryWrapper<>();
                marketingDiscountGoodQueryWrapper.eq("marketing_discount_id", marketingDiscountId.getId());
                //删除对应商品
                iMarketingDiscountGoodService.remove(marketingDiscountGoodQueryWrapper);
            }

            if (StringUtils.isNotBlank(channelIds)) {
                List<String> mIds = Arrays.asList(StringUtils.split(channelIds, ","));
                //查询出对应投放渠道id
                QueryWrapper<MarketingChannelDiscount> marketingChannelDiscountQueryWrapper = new QueryWrapper<>();
                marketingChannelDiscountQueryWrapper.eq("marketing_discount_id", marketingDiscountId.getId());
                //删除对应投放渠道
                iMarketingChannelDiscountService.remove(marketingChannelDiscountQueryWrapper);

                for (String mId : mIds) {
                    //重新添加投放渠道
                    MarketingChannelDiscount marketingChannelDiscount = new MarketingChannelDiscount();
                    marketingChannelDiscount.setMarketingDiscountId(marketingDiscountId.getId());
                    marketingChannelDiscount.setMarketingChannelId(mId);
                    marketingChannelDiscount.setDelFlag("0");
                    iMarketingChannelDiscountService.saveOrUpdate(marketingChannelDiscount);
                }

            }else {
                //查询出对应投放渠道id
                QueryWrapper<MarketingChannelDiscount> marketingChannelDiscountQueryWrapper = new QueryWrapper<>();
                marketingChannelDiscountQueryWrapper.eq("marketing_discount_id", marketingDiscountId.getId());
                //删除对应投放渠道
                iMarketingChannelDiscountService.remove(marketingChannelDiscountQueryWrapper);
            }
            //编辑优惠券列表
            boolean ok = this.updateById(MarketingDiscount);
            if (ok) {
                result.success("修改成功!");
            } else {
                result.error500("修改失败");
            }
        }
        return result;
    }

    @Override
    public List<MarketingDiscountVO> findGiveMarketingDiscountVO(MarketingDiscountVO marketingDiscountVO) {
        return baseMapper.findGiveMarketingDiscountVO(marketingDiscountVO);
    }

    @Override
    public IPage<Map<String, Object>> findMarketingDiscountPage(Page<Map<String, Object>> page) {
        return baseMapper.findMarketingDiscountPage(page);
    }

}
