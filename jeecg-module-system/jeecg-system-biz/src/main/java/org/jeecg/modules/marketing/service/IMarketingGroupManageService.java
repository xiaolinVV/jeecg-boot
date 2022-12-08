package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.entity.MarketingGroupManage;

import java.util.List;
import java.util.Map;

/**
 * @Description: 拼团管理
 * @Author: jeecg-boot
 * @Date:   2021-03-23
 * @Version: V1.0
 */
public interface IMarketingGroupManageService extends IService<MarketingGroupManage> {

    /**
     * 超时退款拼团
     */
    public void closeMarketingGroupManage();

    /**
     * 退款
     */
    public void closeMarketingGroupRecord(MarketingGroupManage marketingGroupManage);

    /**
     * 获取成团进行中的成团商品数据
     *
     * 张靠勤  2021-4-8
     *
     * @return
     */
    public List<Map<String,Object>> getMarketingGroupManageByGood();


    /**
     * 处理拼团成功
     *
     * 张靠勤   2021-4-9
     *
     * @param marketingGroupManageId
     * @param marketingGroupJson:
     *                          [
     *     {
     *         "marketingGroupRecordId":"拼团记录id",
     *         "winning":"0:未中奖，1：中奖",
     *         "rewardNumber":"中奖数量，中奖的可以不填，未中奖的需要填写中奖积分数量"
     *     }
     * ]
     */
    public void successMarketingGroupManage(String marketingGroupManageId,String marketingGroupJson);

}
