package org.jeecg.modules.taobao.job;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.taobao.service.IAli1688Service;
import org.jeecg.modules.taobao.utils.Ali1688Utils;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Description: 同步定时任务测试
 *
 * 此处的同步是指 当定时任务的执行时间大于任务的时间间隔时
 * 会等待第一个任务执行完成才会走第二个任务
 *
 * 定时上架 1688 商品数据
 *
 * @author: taoyan
 * @date: 2020年06月19日
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
@Slf4j
@Component
public class Sync1688GoodDataJob implements Job {

    @Autowired
    private Ali1688Utils ali1688Utils;

    @Autowired
    IAli1688Service ali1688Service;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        int pageNo = 1;
        while (true) {
            List<Map<String, Object>> userGroupsList = ali1688Utils.getUserGroups(pageNo + "", "1");
            if (userGroupsList.size() == 0) {
                break;
            }
            log.info("选品库信息：pageNo=" + pageNo + "行JSON" + JSON.toJSONString(userGroupsList));
            for (Map<String, Object> userGroupMap : userGroupsList) {

                int shopPageNo = 1;
                while (true) {
                    List<Long> goodsList = ali1688Utils.getUserGroupFeed(Long.parseLong(userGroupMap.get("userGroupId").toString()), shopPageNo, 10);
                    if (goodsList.size() == 0) {
                        break;
                    }
                    log.info("pano=" + shopPageNo + "选品库下面的商品信息：" + JSON.toJSONString(goodsList));
                    //商品处理
                    log.info("选品库信息：pageNo=" + pageNo + "行JSON" + JSON.toJSONString(userGroupsList));
                    goodsList.forEach(id -> {
                        //添加商品
                        ali1688Service.addShop(id, userGroupMap.get("title").toString());
                    });
                    log.info("选品库信息：pageNo=" + pageNo + "行JSON" + JSON.toJSONString(userGroupsList));
                    shopPageNo++;
                }

            }
            pageNo++;
//            if(pageNo>29){
//                break;
//            }
        }
    }

}
