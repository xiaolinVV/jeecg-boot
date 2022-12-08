package org.jeecg.modules.alliance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.alliance.dto.AllianceStatementAccountDTO;
import org.jeecg.modules.alliance.entity.AllianceStatementAccount;
import org.jeecg.modules.alliance.mapper.AllianceStatementAccountMapper;
import org.jeecg.modules.alliance.service.IAllianceAccountCapitalService;
import org.jeecg.modules.alliance.service.IAllianceStatementAccountService;
import org.jeecg.modules.alliance.vo.AllianceStatementAccountVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: 加盟商对账单
 * @Author: jeecg-boot
 * @Date:   2020-05-17
 * @Version: V1.0
 */
@Service
public class AllianceStatementAccountServiceImpl extends ServiceImpl<AllianceStatementAccountMapper, AllianceStatementAccount> implements IAllianceStatementAccountService {

    @Autowired
    private IAllianceAccountCapitalService iAllianceAccountCapitalService;

    @Override
    public IPage<AllianceStatementAccountVO> queryPageList(Page<AllianceStatementAccount> page, AllianceStatementAccountDTO allianceStatementAccountDTO) {
        return baseMapper.queryPageList(page,allianceStatementAccountDTO);
    }


    /**
     * 监听器调用
     * 生成每日的对账单数据
     */
    @Override
    public void addAllianceStatementAccount(){
        Calendar ca = Calendar.getInstance();//得到一个Calendar的实例

        ca.setTime(new Date()); //设置时间为当前时间
        //日期减一天（前一天数据）
        ca.add(Calendar.DATE, -1);
        Date lastMonth = ca.getTime(); //结果
        Integer year =  ca.get(Calendar.YEAR);
        Integer month  =  ca.get(Calendar.MONTH)+1;
        Integer  day = ca.get(Calendar.DATE);

        List<Map<String,Object>> mapList =  iAllianceAccountCapitalService.findAccountCapitalSum(year,month,day);
        if(mapList.size()>0){
            for(Map<String,Object> map:mapList){
                //判断数据是否已存在
                QueryWrapper<AllianceStatementAccount> queryWrapperAllianceStatementAccount =new QueryWrapper();
                queryWrapperAllianceStatementAccount.eq("sys_user_id",map.get("sysUserId").toString());
                /*queryWrapperAllianceStatementAccount.eq("calendar_date",data);*/
                queryWrapperAllianceStatementAccount.eq("year",year);
                queryWrapperAllianceStatementAccount.eq("month",month);
                queryWrapperAllianceStatementAccount.eq("day",day);
                List<AllianceStatementAccount>  allianceStatementAccountList =  baseMapper.selectList(queryWrapperAllianceStatementAccount);
                if(allianceStatementAccountList.size()>0){
                    //数据已存在
                }else{
                    //对账单保存数据
                    AllianceStatementAccount allianceStatementAccount = new AllianceStatementAccount();
                    allianceStatementAccount.setCreateBy("jeecg");
                    allianceStatementAccount.setCreateTime(new Date());
                    allianceStatementAccount.setYear(ca.get(Calendar.YEAR));
                    allianceStatementAccount.setMonth(ca.get(Calendar.MONTH)+1);
                    allianceStatementAccount.setDay(ca.get(Calendar.DATE));
                    allianceStatementAccount.setDelFlag("0");
                    allianceStatementAccount.setCalendarDate(lastMonth); //日期
                    allianceStatementAccount.setEarning(new BigDecimal(map.get("earning").toString()) );//收入
                    allianceStatementAccount.setIncomeNumber(new BigDecimal(map.get("incomeNumber").toString()));//收入笔数
                    allianceStatementAccount.setExpenditure(new BigDecimal(map.get("expenditure").toString()));//支出
                    allianceStatementAccount.setExpenditureNumber(new BigDecimal(map.get("expenditureNumber").toString()));//支出笔数
                    allianceStatementAccount.setRevenue(allianceStatementAccount.getEarning().subtract(allianceStatementAccount.getExpenditure()));//收益
                    allianceStatementAccount.setStartBalance(new BigDecimal(map.get("startBalance").toString()));//期初余额
                    allianceStatementAccount.setEndBalance(new BigDecimal(map.get("endBalance").toString()));//期末余额
                    allianceStatementAccount.setSysUserId(map.get("sysUserId").toString());
                    baseMapper.insert(allianceStatementAccount);
                    // this.save(storeStatementAccount);
                }
            }
        }
    }
}
