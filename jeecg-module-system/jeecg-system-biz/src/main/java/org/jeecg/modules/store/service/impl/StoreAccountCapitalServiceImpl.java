package org.jeecg.modules.store.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.store.dto.StoreAccountCapitalDTO;
import org.jeecg.modules.store.entity.StoreAccountCapital;
import org.jeecg.modules.store.entity.StoreStatementAccount;
import org.jeecg.modules.store.mapper.StoreAccountCapitalMapper;
import org.jeecg.modules.store.service.IStoreAccountCapitalService;
import org.jeecg.modules.store.service.IStoreStatementAccountService;
import org.jeecg.modules.store.vo.StoreAccountCapitalVO;
import org.jeecg.modules.store.vo.StoreWorkbenchVO;
import org.jeecg.modules.system.vo.SysWorkbenchVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 店铺资金流水
 * @Author: jeecg-boot
 * @Date:   2019-12-11
 * @Version: V1.0
 */
@Service
public class StoreAccountCapitalServiceImpl extends ServiceImpl<StoreAccountCapitalMapper, StoreAccountCapital> implements IStoreAccountCapitalService {
    @Autowired(required = false)
    private StoreAccountCapitalMapper storeAccountCapitalMapper;
    @Autowired
    @Lazy
    private IStoreStatementAccountService iStoreStatementAccountService;

    /**
     * 资金流水列表
     * @param page
     * @param storeAccountCapitalVO
     * @return
     */
    @Override
 public IPage<StoreAccountCapitalDTO> getStoreAccountCapitalList(Page<StoreAccountCapital> page, StoreAccountCapitalVO storeAccountCapitalVO){
    return storeAccountCapitalMapper.getStoreAccountCapitalList(page,storeAccountCapitalVO);
};
    /**
     * 根据日期查询，返回支出收入 金额跟交易笔数
     * @param year
     * @param month
     * @param day
     * @return
     */
    @Override
    public List<Map<String,Object>> findAccountCapitalSum(Integer year,Integer month,Integer day){

        List<Map<String,Object>> mapList= storeAccountCapitalMapper.findAccountCapitalSum(year,month,day);
        QueryWrapper<StoreAccountCapital> queryWrapper = new QueryWrapper();
        if(mapList.size()>0){
            for(Map<String,Object> map:mapList){
                queryWrapper = new QueryWrapper();
                queryWrapper.eq("store_manage_id",map.get("storeManageId"));
                queryWrapper.eq("year",year);
                queryWrapper.eq("month",month);
                queryWrapper.eq("day",day);
                queryWrapper.orderByDesc("create_time");
                List<StoreAccountCapital> list =  baseMapper.selectList(queryWrapper);
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
                    QueryWrapper<StoreStatementAccount>     queryWrapperStoreStatementAccount = new QueryWrapper();
                    queryWrapperStoreStatementAccount.eq("store_manage_id",map.get("storeManageId"));
                    queryWrapperStoreStatementAccount.last("limit 1");
                    queryWrapperStoreStatementAccount.orderByDesc("create_time");
                    StoreStatementAccount storeStatementAccount = iStoreStatementAccountService.getOne(queryWrapperStoreStatementAccount);
                    if(storeStatementAccount != null ){
                        //期初余额
                        map.put("startBalance",storeStatementAccount.getEndBalance());
                        //期末余额
                        map.put("endBalance",storeStatementAccount.getEndBalance());
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
    public List<StoreAccountCapitalDTO> storeAccountCapitalList(StoreAccountCapitalVO storeAccountCapitalVO) {
        return baseMapper.storeAccountCapitalList(storeAccountCapitalVO);
    }

    /**
     * 店铺工作台统计收益
     * @param sysWorkbenchVO
     * @return
     */
    @Override
    public StoreWorkbenchVO getEarningList(SysWorkbenchVO sysWorkbenchVO) throws ParseException {
        StoreWorkbenchVO storeWorkbenchVO = new StoreWorkbenchVO();
        ArrayList<Map<String, Object>> maps = new ArrayList<>();
        //造一组时间之间的数据
        ArrayList<String> arrayList = new ArrayList<>();
        Date earningTime_begin = sysWorkbenchVO.getEarningTime_begin();
        Date earningTime_end = sysWorkbenchVO.getEarningTime_end();
        if (oConvertUtils.isEmpty(earningTime_begin)){
            for (int i = 1; i < 31; i++) {
                //推算当前时间前一天日期
                Calendar calendar=Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_YEAR,-i);
                String s = calendar.get(Calendar.YEAR) + "-" + org.apache.commons.lang.StringUtils.leftPad((calendar.get(Calendar.MONTH) + 1)+"",2,"0") + "-" + org.apache.commons.lang.StringUtils.leftPad((calendar.get(Calendar.DAY_OF_MONTH))+"",2,"0");
                arrayList.add(s);
                if (i==1){
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    sysWorkbenchVO.setEarningTime_end(simpleDateFormat.parse(s));
                }
                if (i==30){
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    sysWorkbenchVO.setEarningTime_begin(simpleDateFormat.parse(s));
                }
            }
        }else {

            long day = (earningTime_end.getTime()-earningTime_begin.getTime())/(24*60*60*1000);
            for (int i = 0; i < day; i++) {
                //推算当前时间前一天日期
                Calendar calendar=Calendar.getInstance();
                calendar.setTime(earningTime_end);
                calendar.add(Calendar.DAY_OF_YEAR,-i);
                String s1 = calendar.get(Calendar.YEAR) + "-" + org.apache.commons.lang.StringUtils.leftPad((calendar.get(Calendar.MONTH) + 1)+"",2,"0") + "-" + org.apache.commons.lang.StringUtils.leftPad((calendar.get(Calendar.DAY_OF_MONTH))+"",2,"0");
                arrayList.add(s1);
            }
        }
        List<StoreAccountCapitalVO> storeAccountCapitalVOS = storeAccountCapitalMapper.forechX(sysWorkbenchVO);
        BigDecimal earningsPatch = storeAccountCapitalVOS.stream()
                .map(StoreAccountCapitalVO::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, Object> collect = storeAccountCapitalVOS.stream().collect(Collectors
                .toMap(StoreAccountCapitalVO::getMydate, StoreAccountCapitalVO::getTotalPrice));
        arrayList.forEach(arr -> {
            HashMap<String, Object> decimalHashMap = new HashMap<>();
            decimalHashMap.put("x", arr);
            decimalHashMap.put("y", collect.getOrDefault(arr, "0"));
            maps.add(decimalHashMap);
        });
        storeWorkbenchVO.setEarningsList(maps);
        storeWorkbenchVO.setEarningsPatch(earningsPatch);
        return storeWorkbenchVO;
    }
    /**
     * 查询店铺 日期收入金额总和
     * @param paramMap
     * @return
     */
    public BigDecimal getStoreIncomeSummation(Map<String,String> paramMap){
       return  baseMapper.getStoreIncomeSummation(paramMap);
    }

    @Override
    public IPage<Map<String, Object>> findStoreAccountCapitalInfo(Page<StoreAccountCapital> page, HashMap<String, Object> map) {
        return baseMapper.findStoreAccountCapitalInfo(page,map);
    }

    @Override
    public IPage<StoreAccountCapitalVO> getStoreAccountCapitalListList(Page<StoreAccountCapital> page, StoreStatementAccount storeStatementAccount) {
        return baseMapper.getStoreAccountCapitalListList(page,storeStatementAccount);
    }

    ;
}
