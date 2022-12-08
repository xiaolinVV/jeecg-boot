package org.jeecg.modules.agency.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.agency.dto.AgencyAccountCapitalDTO;
import org.jeecg.modules.agency.entity.AgencyAccountCapital;
import org.jeecg.modules.agency.entity.AgencyStatementAccount;
import org.jeecg.modules.agency.mapper.AgencyAccountCapitalMapper;
import org.jeecg.modules.agency.service.IAgencyAccountCapitalService;
import org.jeecg.modules.agency.service.IAgencyStatementAccountService;
import org.jeecg.modules.agency.vo.AgencyAccountCapitalVO;
import org.jeecg.modules.agency.vo.AgencyWorkbenchVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 代理资金流水
 * @Author: jeecg-boot
 * @Date:   2019-12-21
 * @Version: V1.0
 */
@Service
public class AgencyAccountCapitalServiceImpl extends ServiceImpl<AgencyAccountCapitalMapper, AgencyAccountCapital> implements IAgencyAccountCapitalService {

     @Autowired
     @Lazy
     private IAgencyStatementAccountService iAgencyStatementAccountService;
    @Override
    public IPage<AgencyAccountCapitalDTO> getAgencyAccountCapitalList(Page<AgencyAccountCapital> page, AgencyAccountCapitalVO agencyAccountCapitalVO){
        return baseMapper.getAgencyAccountCapitalList(page,agencyAccountCapitalVO);
    };
    /**
     * 根据日期查询，返回支出收入 金额跟交易笔数
     * @param year
     *  @param month
     *  @param day
     * @return
     */
    @Override
    public List<Map<String,Object>> findAccountCapitalSum(Integer year,Integer month,Integer day){
        List<Map<String,Object>> mapList= baseMapper.findAccountCapitalSum( year, month, day);
        QueryWrapper<AgencyAccountCapital> queryWrapper = new QueryWrapper();
        if(mapList.size()>0){
            for(Map<String,Object> map:mapList){
                queryWrapper = new QueryWrapper();
                queryWrapper.eq("sys_user_id",map.get("sysUserId"));
                queryWrapper.eq("year",year);
                queryWrapper.eq("month",month);
                queryWrapper.eq("day",day);
                queryWrapper.orderByDesc("create_time");
                List<AgencyAccountCapital> list =  baseMapper.selectList(queryWrapper);
                if(list.size()>0){
                    if (StringUtils.isNotBlank(list.get(list.size() - 1).getGoAndCome())){
                        //添加 期初余额 期末余额
                        if("0".equals(list.get(list.size() - 1).getGoAndCome()) ){
                            //收入（需要减去收入金额）
                            map.put("startBalance",list.get(list.size() - 1).getBalance().subtract(list.get(list.size() - 1).getAmount()));
                        }else{
                            //支出（需要加上支出金额）
                            map.put("startBalance",list.get(list.size() - 1).getBalance().add(list.get(list.size() - 1).getAmount()));
                        }
                    }
                    //期末余额
                    map.put("endBalance",list.get(0).getBalance());
                }else{
                    QueryWrapper<AgencyStatementAccount>     queryWrapperAgencyStatementAccount = new QueryWrapper();
                    queryWrapperAgencyStatementAccount.eq("sys_user_id",map.get("sysUserId"));
                    queryWrapperAgencyStatementAccount.last("limit 1");
                    queryWrapperAgencyStatementAccount.orderByDesc("create_time");
                    AgencyStatementAccount agencyStatementAccount = iAgencyStatementAccountService.getOne(queryWrapperAgencyStatementAccount);
                   if(agencyStatementAccount != null ){
                       //期初余额
                       map.put("startBalance",agencyStatementAccount.getEndBalance());
                      //期末余额
                       map.put("endBalance",agencyStatementAccount.getEndBalance());
                   }else{
                       map.put("startBalance",0);
                       map.put("endBalance",0);
                   }

                }
            }
        }
        return mapList;
    }

    @Override
    public AgencyWorkbenchVO getEarningList(AgencyWorkbenchVO agencyWorkbenchVO) throws ParseException {
        ArrayList<Map<String, Object>> maps = new ArrayList<>();
        ArrayList<String> arrayList = new ArrayList<>();
        Date earningTime_begin = agencyWorkbenchVO.getEarningTime_begin();
        Date earningTime_end = agencyWorkbenchVO.getEarningTime_end();
        if (oConvertUtils.isEmpty(earningTime_begin)){
            for (int i = 1; i < 31; i++) {
                //推算当前时间前一天日期
                Calendar calendar=Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_YEAR,-i);
                String s1 = calendar.get(Calendar.YEAR) + "-" + StringUtils.leftPad((calendar.get(Calendar.MONTH) + 1)+"",2,"0") + "-" + StringUtils.leftPad((calendar.get(Calendar.DAY_OF_MONTH))+"",2,"0");
                arrayList.add(s1);
                if (i==1){
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    agencyWorkbenchVO.setEarningTime_end(simpleDateFormat.parse(s1));
                }
                if (i==30){
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    agencyWorkbenchVO.setEarningTime_begin(simpleDateFormat.parse(s1));
                }
            }

        }else {
            long day = (earningTime_end.getTime()-earningTime_begin.getTime())/(24*60*60*1000);
            for (int i = 0; i < day; i++) {
                //推算当前时间前一天日期
                Calendar calendar=Calendar.getInstance();
                calendar.setTime(earningTime_end);
                calendar.add(Calendar.DAY_OF_YEAR,-i);
                String s1 = calendar.get(Calendar.YEAR) + "-" + StringUtils.leftPad((calendar.get(Calendar.MONTH) + 1)+"",2,"0") + "-" + StringUtils.leftPad((calendar.get(Calendar.DAY_OF_MONTH))+"",2,"0");
                arrayList.add(s1);
            }
        }
        List<AgencyAccountCapitalDTO> earningList = baseMapper.getEarningList(agencyWorkbenchVO);
        BigDecimal earningsPatch = earningList.stream()
                .map(AgencyAccountCapitalDTO::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, BigDecimal> collect = earningList.stream().collect(Collectors.
                toMap(AgencyAccountCapitalDTO::getMydate, AgencyAccountCapitalDTO::getTotalPrice));
        arrayList.forEach(arr->{
            HashMap<String, Object> map = new HashMap<>();
            map.put("x",arr);
            map.put("y",collect.getOrDefault(arr,new BigDecimal(0)));
            maps.add(map);
        });
        AgencyWorkbenchVO agencyWorkbenchVO1 = new AgencyWorkbenchVO();
        agencyWorkbenchVO1.setEarningsList(maps);
        agencyWorkbenchVO1.setEarningsPatch(earningsPatch);
        return agencyWorkbenchVO1;
    }

    @Override
    public IPage<AgencyAccountCapitalVO> getAgencyAccountCapitalListList(Page<AgencyAccountCapital> page, AgencyStatementAccount agencyStatementAccount) {
        return baseMapper.getAgencyAccountCapitalListList(page,agencyStatementAccount);
    }

    ;
}
