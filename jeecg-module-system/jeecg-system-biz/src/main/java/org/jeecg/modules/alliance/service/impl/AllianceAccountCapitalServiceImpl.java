package org.jeecg.modules.alliance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.agency.entity.AgencyAccountCapital;
import org.jeecg.modules.alliance.dto.AllianceAccountCapitalDTO;
import org.jeecg.modules.alliance.entity.AllianceAccountCapital;
import org.jeecg.modules.alliance.entity.AllianceStatementAccount;
import org.jeecg.modules.alliance.mapper.AllianceAccountCapitalMapper;
import org.jeecg.modules.alliance.service.IAllianceAccountCapitalService;
import org.jeecg.modules.alliance.service.IAllianceStatementAccountService;
import org.jeecg.modules.alliance.vo.AllianceAccountCapitalVO;
import org.jeecg.modules.alliance.vo.AllianceWorkbenchVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 加盟商资金管理
 * @Author: jeecg-boot
 * @Date:   2020-05-17
 * @Version: V1.0
 */
@Service
public class AllianceAccountCapitalServiceImpl extends ServiceImpl<AllianceAccountCapitalMapper, AllianceAccountCapital> implements IAllianceAccountCapitalService {

    @Autowired
    @Lazy
    private IAllianceStatementAccountService iAllianceStatementAccountService;

    @Override
    public IPage<AllianceAccountCapitalVO> queryPageList(Page<AllianceAccountCapital> page, AllianceAccountCapitalDTO allianceAccountCapitalDTO) {
        return baseMapper.queryPageList(page,allianceAccountCapitalDTO);
    }

    @Override
    public AllianceWorkbenchVO getEarningList(AllianceWorkbenchVO workbench) throws ParseException {
        ArrayList<Map<String, Object>> maps = new ArrayList<>();
        ArrayList<String> arrayList = new ArrayList<>();
        Date earningTime_begin = workbench.getEarningTime_begin();
        Date earningTime_end = workbench.getEarningTime_end();
        if (oConvertUtils.isEmpty(earningTime_begin)){
            for (int i = 1; i < 31; i++) {
                //推算当前时间前一天日期
                Calendar calendar=Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_YEAR,-i);
                String s1 = calendar.get(Calendar.YEAR) + "-" + StringUtils.leftPad((calendar.get(Calendar.MONTH) + 1)+"",2,"0") + "-" + StringUtils.leftPad((calendar.get(Calendar.DAY_OF_MONTH))+"",2,"0");
                arrayList.add(s1);
                if (i==1){
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    workbench.setEarningTime_end(simpleDateFormat.parse(s1));
                }
                if (i==30){
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    workbench.setEarningTime_begin(simpleDateFormat.parse(s1));
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
        List<AllianceAccountCapitalDTO> earningList = baseMapper.getEarningList(workbench);
        BigDecimal earningsPatch = earningList.stream()
                .map(AllianceAccountCapitalDTO::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, BigDecimal> collect = earningList.stream().collect(Collectors.
                toMap(AllianceAccountCapitalDTO::getMydate, AllianceAccountCapitalDTO::getTotalPrice));
        arrayList.forEach(arr->{
            HashMap<String, Object> map = new HashMap<>();
            map.put("x",arr);
            map.put("y",collect.getOrDefault(arr,new BigDecimal(0)));
            maps.add(map);
        });
        AllianceWorkbenchVO allianceWorkbenchVO = new AllianceWorkbenchVO();
        allianceWorkbenchVO.setEarningsList(maps);
        allianceWorkbenchVO.setEarningsPatch(earningsPatch);
        return allianceWorkbenchVO;
    }


    @Override
    public List<Map<String, Object>> findAccountCapitalSum(Integer year, Integer month, Integer day) {
        List<Map<String, Object>> mapList = baseMapper.findAccountCapitalSum( year,  month,  day);
        QueryWrapper<AllianceAccountCapital> queryWrapper = new QueryWrapper();
        if (mapList.size() > 0) {
            for (Map<String, Object> map : mapList) {
                queryWrapper = new QueryWrapper();
                queryWrapper.eq("sys_user_id", map.get("sysUserId"));
                queryWrapper.eq("year",year);
                queryWrapper.eq("month",month);
                queryWrapper.eq("day",day);
                queryWrapper.orderByDesc("create_time");
                List<AllianceAccountCapital> list = baseMapper.selectList(queryWrapper);
                if (list.size() > 0) {
                    if (StringUtils.isNotBlank(list.get(list.size() - 1).getGoAndCome())) {
                        //添加 期初余额 期末余额
                        if ("0".equals(list.get(list.size() - 1).getGoAndCome())) {
                            //收入（需要减去收入金额）
                            map.put("startBalance", list.get(list.size() - 1).getBalance().subtract(list.get(list.size() - 1).getAmount()));
                        } else {
                            //支出（需要加上支出金额）
                            map.put("startBalance", list.get(list.size() - 1).getBalance().add(list.get(list.size() - 1).getAmount()));
                        }
                    }
                    //期末余额
                    map.put("endBalance", list.get(0).getBalance());
                } else {
                    QueryWrapper<AllianceStatementAccount>     queryWrapperAllianceStatementAccount = new QueryWrapper();
                    queryWrapperAllianceStatementAccount.eq("sys_user_id",map.get("sysUserId"));
                    queryWrapperAllianceStatementAccount.last("limit 1");
                    queryWrapperAllianceStatementAccount.orderByDesc("create_time");
                    AllianceStatementAccount allianceStatementAccount = iAllianceStatementAccountService.getOne(queryWrapperAllianceStatementAccount);
                    if(allianceStatementAccount != null ){
                        //期初余额
                        map.put("startBalance",allianceStatementAccount.getEndBalance());
                        //期末余额
                        map.put("endBalance",allianceStatementAccount.getEndBalance());
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
    public IPage<AllianceAccountCapitalVO> findAccountCapitalById(Page<AgencyAccountCapital> page, AllianceStatementAccount allianceStatementAccount) {
        return baseMapper.findAccountCapitalById(page,allianceStatementAccount);
    }
}
