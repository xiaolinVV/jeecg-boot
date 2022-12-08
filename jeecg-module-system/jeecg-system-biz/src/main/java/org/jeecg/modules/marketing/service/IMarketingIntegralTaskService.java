package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.entity.MarketingIntegralTask;

import java.math.BigDecimal;

/**
 * @Description: 积分任务
 * @Author: jeecg-boot
 * @Date:   2021-04-29
 * @Version: V1.0
 */
public interface IMarketingIntegralTaskService extends IService<MarketingIntegralTask> {

    /**
     * 获取用户任务是否可领取
     *
     * @param marketingIntegralTaskId
     * @param memberId
     * @return
     */
    public int getIntegralTaskStatus(String marketingIntegralTaskId,String memberId);

    /**
     * 注册成功领取积分
     *
     * @param memberId
     * @return
     */
    public boolean registerSuccess(String memberId);

    /**
     * 交易密码设置
     *
     * @param memberId
     * @return
     */
    public boolean transactionPassword(String memberId);

    /**
     * 邀请签到
     *
     * @param memberId
     * @return
     */
    public boolean invitedSign(String memberId,String tMemberId);

    /**
     * 邀请注册
     *
     * @param memberId
     * @return
     */
    public boolean invitationRegister(String memberId);

    /**
     * 每日浏览
     *
     * @param memberId
     * @return
     */
    public BigDecimal dailyBrowsing(String memberId);


    /**
     * 每日多次任务
     *
     * @param memberId
     * @param taskType
     * @return
     */
    public boolean manyTimesDay(String memberId,String taskType);


    /**
     * 每日单次任务
     *
     * @param memberId
     * @param taskType
     * @return
     */
    public boolean singleDaily(String memberId,String taskType);



    /**
     * 每日单次任务
     *
     * @param memberId
     * @param taskType
     * @return
     */
    public boolean onlyTask(String memberId,String taskType);





}
