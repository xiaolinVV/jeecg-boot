package org.jeecg.modules.agency.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.agency.dto.AgencyStatementAccountDTO;
import org.jeecg.modules.agency.entity.AgencyStatementAccount;
import org.jeecg.modules.agency.mapper.AgencyStatementAccountMapper;
import org.jeecg.modules.agency.service.IAgencyAccountCapitalService;
import org.jeecg.modules.agency.service.IAgencyStatementAccountService;
import org.jeecg.modules.agency.vo.AgencyStatementAccountVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: 代理对账单
 * @Author: jeecg-boot
 * @Date:   2019-12-22
 * @Version: V1.0
 */
@Service
public class AgencyStatementAccountServiceImpl extends ServiceImpl<AgencyStatementAccountMapper, AgencyStatementAccount> implements IAgencyStatementAccountService {
@Autowired
private IAgencyAccountCapitalService agencyAccountCapitalService;

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

        List<Map<String,Object>> mapList =  agencyAccountCapitalService.findAccountCapitalSum(year,month,day);
        if(mapList.size()>0){
            for(Map<String,Object> map:mapList){
                //判断数据是否已存在
                QueryWrapper<AgencyStatementAccount> queryWrapperAgencyStatementAccount =new QueryWrapper();
                queryWrapperAgencyStatementAccount.eq("sys_user_id",map.get("sysUserId").toString());
//                /queryWrapperAgencyStatementAccount.eq("calendar_date",data);
                queryWrapperAgencyStatementAccount.eq("year",year);
                queryWrapperAgencyStatementAccount.eq("month",month);
                queryWrapperAgencyStatementAccount.eq("day",day);
                List<AgencyStatementAccount>  storeStatementAccounts =  baseMapper.selectList(queryWrapperAgencyStatementAccount);
                if(storeStatementAccounts.size()>0){
                    //数据已存在
                }else{
                    //对账单保存数据
                    AgencyStatementAccount agencyStatementAccount = new AgencyStatementAccount();
                    agencyStatementAccount.setCreateBy("jeecg");
                    agencyStatementAccount.setCreateTime(new Date());
                    agencyStatementAccount.setYear(ca.get(Calendar.YEAR));
                    agencyStatementAccount.setMonth(ca.get(Calendar.MONTH)+1);
                    agencyStatementAccount.setDay(ca.get(Calendar.DATE));
                    agencyStatementAccount.setDelFlag("0");
                    agencyStatementAccount.setCalendarDate(lastMonth); //日期
                    agencyStatementAccount.setEarning(new BigDecimal(map.get("earning").toString()) );//收入
                    agencyStatementAccount.setIncomeNumber(new BigDecimal(map.get("incomeNumber").toString()));//收入笔数
                    agencyStatementAccount.setExpenditure(new BigDecimal(map.get("expenditure").toString()));//支出
                    agencyStatementAccount.setExpenditureNumber(new BigDecimal(map.get("expenditureNumber").toString()));//支出笔数
                    agencyStatementAccount.setRevenue(agencyStatementAccount.getEarning().subtract(agencyStatementAccount.getExpenditure()));//收益
                    agencyStatementAccount.setStartBalance(new BigDecimal(map.get("startBalance").toString()));//期初余额
                    agencyStatementAccount.setEndBalance(new BigDecimal(map.get("endBalance").toString()));//期末余额
                    agencyStatementAccount.setSysUserId(map.get("sysUserId").toString());
                    baseMapper.insert(agencyStatementAccount);
                    // this.save(storeStatementAccount);
                }
            }
        }
    }

    @Override
    public IPage<AgencyStatementAccountVO> queryPageList(Page<AgencyStatementAccount> page, AgencyStatementAccountDTO agencyStatementAccountDTO) {
        return baseMapper.queryPageList(page,agencyStatementAccountDTO);
    }

}
