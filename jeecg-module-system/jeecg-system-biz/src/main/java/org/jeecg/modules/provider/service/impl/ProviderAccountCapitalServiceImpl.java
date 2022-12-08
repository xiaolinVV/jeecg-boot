package org.jeecg.modules.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.provider.dto.ProviderAccountCapitalDTO;
import org.jeecg.modules.provider.entity.ProviderAccountCapital;
import org.jeecg.modules.provider.entity.ProviderStatementAccount;
import org.jeecg.modules.provider.mapper.ProviderAccountCapitalMapper;
import org.jeecg.modules.provider.service.IProviderAccountCapitalService;
import org.jeecg.modules.provider.service.IProviderStatementAccountService;
import org.jeecg.modules.provider.vo.ProviderAccountCapitalVO;
import org.jeecg.modules.provider.vo.ProviderWorkbenchVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 供应商资金流水
 * @Author: jeecg-boot
 * @Date: 2019-12-18
 * @Version: V1.0
 */
@Service
public class ProviderAccountCapitalServiceImpl extends ServiceImpl<ProviderAccountCapitalMapper, ProviderAccountCapital> implements IProviderAccountCapitalService {
     @Autowired
     @Lazy
     private IProviderStatementAccountService iProviderStatementAccountService;


    @Override
    public IPage<ProviderAccountCapitalDTO> getProviderAccountCapitalList(Page<ProviderAccountCapital> page, ProviderAccountCapitalVO providerAccountCapitalVO) {
        return baseMapper.getProviderAccountCapitalList(page, providerAccountCapitalVO);
    }

    ;

    @Override
    public List<Map<String, Object>> findAccountCapitalSum(Integer year,Integer month,Integer day) {
        List<Map<String, Object>> mapList = baseMapper.findAccountCapitalSum( year, month, day);
        QueryWrapper<ProviderAccountCapital> queryWrapper = new QueryWrapper();
        if (mapList.size() > 0) {
            for (Map<String, Object> map : mapList) {
                queryWrapper = new QueryWrapper();
                queryWrapper.eq("sys_user_id", map.get("sysUserId"));
                queryWrapper.eq("year",year);
                queryWrapper.eq("month",month);
                queryWrapper.eq("day",day);
                queryWrapper.orderByDesc("create_time");
                List<ProviderAccountCapital> list = baseMapper.selectList(queryWrapper);
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
                    QueryWrapper<ProviderStatementAccount>     queryWrapperProviderStatementAccount = new QueryWrapper();
                    queryWrapperProviderStatementAccount.eq("sys_user_id",map.get("sysUserId"));
                    queryWrapperProviderStatementAccount.last("limit 1");
                    queryWrapperProviderStatementAccount.orderByDesc("create_time");
                    ProviderStatementAccount providerStatementAccount = iProviderStatementAccountService.getOne(queryWrapperProviderStatementAccount);
                    if(providerStatementAccount != null ){
                        //期初余额
                        map.put("startBalance",providerStatementAccount.getEndBalance());
                        //期末余额
                        map.put("endBalance",providerStatementAccount.getEndBalance());
                    }else{
                        map.put("startBalance",0);
                        map.put("endBalance",0);
                    }
                }
            }
        }
        return mapList;
    }

    /**
     * 统计工作台累计收益
     *
     * @return
     */
    @Override
    public ProviderWorkbenchVO getEarningList(ProviderWorkbenchVO providerWorkbenchVO) throws ParseException {
        ArrayList<Map<String, Object>> maps = new ArrayList<>();
        //定义传入后台时间list
        ArrayList<String> arrayList = new ArrayList<>();
        Date earningTime_begin = providerWorkbenchVO.getEarningTime_begin();
        Date earningTime_end = providerWorkbenchVO.getEarningTime_end();
        if (oConvertUtils.isEmpty(earningTime_begin)){
            for (int i = 1; i < 31; i++) {
                //推算当前时间前一天日期
                Calendar calendar=Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_YEAR,-i);
                String s1 = calendar.get(Calendar.YEAR) + "-" + StringUtils.leftPad((calendar.get(Calendar.MONTH) + 1)+"",2,"0") + "-" + StringUtils.leftPad((calendar.get(Calendar.DAY_OF_MONTH))+"",2,"0");
                arrayList.add(s1);
                if (i==1){
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    providerWorkbenchVO.setEarningTime_end(simpleDateFormat.parse(s1));
                }
                if (i==30){
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    providerWorkbenchVO.setEarningTime_begin(simpleDateFormat.parse(s1));
                }
            }
        }else {
            long day = (earningTime_end.getTime()-earningTime_begin.getTime())/(24*60*60*1000);
            for (int i = 0; i < day; i++) {
                //推算当前时间前一天日期
                Calendar calendar=Calendar.getInstance();
                calendar.setTime(earningTime_end);
                calendar.add(Calendar.DAY_OF_YEAR,-i);
                String s1 = calendar.get(Calendar.YEAR) + "-"
                        + StringUtils.leftPad((calendar.get(Calendar.MONTH) + 1)+"",2,"0") + "-"
                        + StringUtils.leftPad((calendar.get(Calendar.DAY_OF_MONTH))+"",2,"0");
                arrayList.add(s1);
            }
        }
        List<ProviderAccountCapitalDTO> earningList = baseMapper.getEarningList(providerWorkbenchVO);
        BigDecimal earningsPatch = earningList.stream()
                .map(ProviderAccountCapitalDTO::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, BigDecimal> collect = earningList.stream().collect(Collectors.
                toMap(ProviderAccountCapitalDTO::getMydate, ProviderAccountCapitalDTO::getTotalPrice));
        arrayList.forEach(arr->{
            HashMap<String, Object> map = new HashMap<>();
            map.put("x",arr);
            map.put("y",collect.getOrDefault(arr,new BigDecimal(0)));
            maps.add(map);
        });
        ProviderWorkbenchVO workbenchVO = new ProviderWorkbenchVO();
        workbenchVO.setEarningsList(maps);
        workbenchVO.setEarningsPatch(earningsPatch);
        return workbenchVO;
    }

    @Override
    public IPage<ProviderAccountCapitalVO> getProviderAccountCapitalListList(Page<ProviderAccountCapital> page, ProviderStatementAccount providerStatementAccount) {
        return baseMapper.getProviderAccountCapitalListList(page,providerStatementAccount);
    }

    ;

}
