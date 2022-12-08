package org.jeecg.modules.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.provider.dto.ProviderStatementAccountDTO;
import org.jeecg.modules.provider.entity.ProviderStatementAccount;
import org.jeecg.modules.provider.mapper.ProviderStatementAccountMapper;
import org.jeecg.modules.provider.service.IProviderAccountCapitalService;
import org.jeecg.modules.provider.service.IProviderStatementAccountService;
import org.jeecg.modules.provider.vo.ProviderStatementAccountVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: 供应商对账单
 * @Author: jeecg-boot
 * @Date:   2020-01-04
 * @Version: V1.0
 */
@Service
public class ProviderStatementAccountServiceImpl extends ServiceImpl<ProviderStatementAccountMapper, ProviderStatementAccount> implements IProviderStatementAccountService {

    @Autowired
    private IProviderAccountCapitalService providerAccountCapitalService;
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

        List<Map<String,Object>> mapList =  providerAccountCapitalService.findAccountCapitalSum(year,month,day);
        if(mapList.size()>0){
            for(Map<String,Object> map:mapList){
                //判断数据是否已存在
                QueryWrapper<ProviderStatementAccount> queryWrapperProviderStatementAccount =new QueryWrapper();
                queryWrapperProviderStatementAccount.eq("sys_user_id",map.get("sysUserId").toString());
                queryWrapperProviderStatementAccount.eq("year",year);
                queryWrapperProviderStatementAccount.eq("month",month);
                queryWrapperProviderStatementAccount.eq("day",day);
                List<ProviderStatementAccount>  storeStatementAccounts =  baseMapper.selectList(queryWrapperProviderStatementAccount);
                if(storeStatementAccounts.size()>0){
                    //数据已存在
                }else{
                    //对账单保存数据
                    ProviderStatementAccount providerStatementAccount = new ProviderStatementAccount();
                    providerStatementAccount.setCreateBy("jeecg");
                    providerStatementAccount.setCreateTime(new Date());
                    providerStatementAccount.setYear(ca.get(Calendar.YEAR));
                    providerStatementAccount.setMonth(ca.get(Calendar.MONTH)+1);
                    providerStatementAccount.setDay(ca.get(Calendar.DATE));
                    providerStatementAccount.setDelFlag("0");
                    providerStatementAccount.setCalendarDate(lastMonth); //日期
                    providerStatementAccount.setEarning(new BigDecimal(map.get("earning").toString()) );//收入
                    providerStatementAccount.setIncomeNumber(new BigDecimal(map.get("incomeNumber").toString()));//收入笔数
                    providerStatementAccount.setExpenditure(new BigDecimal(map.get("expenditure").toString()));//支出
                    providerStatementAccount.setExpenditureNumber(new BigDecimal(map.get("expenditureNumber").toString()));//支出笔数
                    providerStatementAccount.setRevenue(providerStatementAccount.getEarning().subtract(providerStatementAccount.getExpenditure()));//收益
                    providerStatementAccount.setStartBalance(new BigDecimal(map.get("startBalance").toString()));//期初余额
                    providerStatementAccount.setEndBalance(new BigDecimal(map.get("endBalance").toString()));//期末余额
                    providerStatementAccount.setSysUserId(map.get("sysUserId").toString());
                    baseMapper.insert(providerStatementAccount);
                    // this.save(storeStatementAccount);
                }
            }
        }
    }

    @Override
    public IPage<ProviderStatementAccountVO> queryPageList(Page<ProviderStatementAccount> page, ProviderStatementAccountDTO providerStatementAccountDTO) {
        return baseMapper.queryPageList(page,providerStatementAccountDTO);
    }

}
