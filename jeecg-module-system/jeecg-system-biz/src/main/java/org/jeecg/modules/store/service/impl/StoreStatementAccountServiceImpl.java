package org.jeecg.modules.store.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.store.dto.StoreStatementAccountDTO;
import org.jeecg.modules.store.entity.StoreStatementAccount;
import org.jeecg.modules.store.mapper.StoreStatementAccountMapper;
import org.jeecg.modules.store.service.IStoreAccountCapitalService;
import org.jeecg.modules.store.service.IStoreStatementAccountService;
import org.jeecg.modules.store.vo.StoreStatementAccountVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: 店铺对账单
 * @Author: jeecg-boot
 * @Date:   2019-12-11
 * @Version: V1.0
 */
@Service
public class StoreStatementAccountServiceImpl extends ServiceImpl<StoreStatementAccountMapper, StoreStatementAccount> implements IStoreStatementAccountService {
  @Autowired
  private IStoreAccountCapitalService storeAccountCapitalService;
    /**
     * 监听器调用
     * 生成每日的对账单数据
     */
    @Override
    public void addStoreStatementAccount(){
        Calendar ca = Calendar.getInstance();//得到一个Calendar的实例
        ca.setTime(new Date()); //设置时间为当前时间
       //日期减一天（前一天数据）
        ca.add(Calendar.DATE, -1);
        Date lastMonth = ca.getTime(); //结果
        Integer year =  ca.get(Calendar.YEAR);
        Integer month  =  ca.get(Calendar.MONTH)+1;
        Integer  day = ca.get(Calendar.DATE);
        List<Map<String,Object>> mapList =  storeAccountCapitalService.findAccountCapitalSum(year,month,day);
         if(mapList.size()>0){
             for(Map<String,Object> map:mapList){
             //判断数据是否已存在
                 QueryWrapper<StoreStatementAccount> queryWrapperStoreStatementAccount =new QueryWrapper();
                 queryWrapperStoreStatementAccount.eq("store_manage_id",map.get("storeManageId").toString());
                 queryWrapperStoreStatementAccount.eq("year",year);
                 queryWrapperStoreStatementAccount.eq("month",month);
                 queryWrapperStoreStatementAccount.eq("day",day);
                 List<StoreStatementAccount>  storeStatementAccounts =  baseMapper.selectList(queryWrapperStoreStatementAccount);
               if(storeStatementAccounts.size()>0){
               //数据已存在
               }else{
                   //对账单保存数据
                   StoreStatementAccount storeStatementAccount = new StoreStatementAccount();
                   storeStatementAccount.setCreateBy("jeecg");
                   storeStatementAccount.setCreateTime(new Date());
                   storeStatementAccount.setYear(ca.get(Calendar.YEAR));
                   storeStatementAccount.setMonth(ca.get(Calendar.MONTH)+1);
                   storeStatementAccount.setDay(ca.get(Calendar.DATE));
                   storeStatementAccount.setDelFlag("0");
                   storeStatementAccount.setCalendarDate(lastMonth); //日期
                   storeStatementAccount.setEarning(new BigDecimal(map.get("earning").toString()) );//收入
                   storeStatementAccount.setIncomeNumber(new BigDecimal(map.get("incomeNumber").toString()));//收入笔数
                   storeStatementAccount.setExpenditure(new BigDecimal(map.get("expenditure").toString()));//支出
                   storeStatementAccount.setExpenditureNumber(new BigDecimal(map.get("expenditureNumber").toString()));//支出笔数
                   storeStatementAccount.setRevenue(storeStatementAccount.getEarning().subtract(storeStatementAccount.getExpenditure()));//收益
                   storeStatementAccount.setStartBalance(new BigDecimal(map.get("startBalance").toString()));//期初余额
                   storeStatementAccount.setEndBalance(new BigDecimal(map.get("endBalance").toString()));//期末余额
                   storeStatementAccount.setStoreManageId(map.get("storeManageId").toString());
                   baseMapper.insert(storeStatementAccount);
                   // this.save(storeStatementAccount);
               }
             }
         }
    }

    @Override
    public IPage<StoreStatementAccountVO> queryPageList(Page<StoreStatementAccount> page, StoreStatementAccountDTO storeStatementAccountDTO) {
        return baseMapper.queryPageList(page,storeStatementAccountDTO);
    }

}
