package org.jeecg.modules.searchHistory.service;

import java.util.List;

/**
 * @author 张少林
 * @date 2023年04月04日 10:52 上午
 */
public interface SearchHistoryService {
    //新增一条该userid用户在搜索栏的历史记录
    //searchkey 代表输入的关键词
    int addSearchHistoryByUserId(String userid, String searchkey);

    //删除个人历史数据
    Long delSearchHistoryByUserId(String userid, String searchkey);

    //获取个人历史数据列表
    List<String> getSearchHistoryByUserId(String userid);

    //新增一条热词搜索记录，将用户输入的热词存储下来
    int incrementScoreByUserId(String searchkey);

    //根据searchkey搜索其相关最热的前十名 (如果searchkey为null空，则返回redis存储的前十最热词条)
    List<String> getHotList(String searchkey);

    //每次点击给相关词searchkey热度 +1
    int incrementScore(String searchkey);

}
