package org.jeecg.modules.store.api;

import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.member.service.IMemberListService;
import org.jeecg.modules.store.entity.StoreType;
import org.jeecg.modules.store.service.IStoreManageService;
import org.jeecg.modules.store.service.IStoreTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 店铺分类
 */

@Controller
@RequestMapping("front/storeType")
@Slf4j
public class FrontStoreTypeController {

    @Autowired
    private IMemberListService iMemberListService;

    @Autowired
    private IStoreTypeService iStoreTypeService;

    @Autowired
    private IStoreManageService iStoreManageService;

    @RequestMapping("findStoreTypeByid")
    @ResponseBody
    public Result<List<Map<String,Object>>> getStoreTypeListMapById(@RequestParam("id") String id,
                                                              HttpServletRequest request){
        Result<List<Map<String,Object>>> result = new Result<>();
        HashMap<String, Object> map = new HashMap<>();
        if (StringUtils.isNotBlank(id)){
            map.put("level","2");
        }else {
            map.put("level","1");
        }
        map.put("id",id);
        result.setResult(iStoreTypeService.getStoreTypeListMapById(map));
        result.success("返回店铺分类");
        return result;
    }
    @RequestMapping("getStoreTypeMapListOne")
    @ResponseBody
    public Result<List<Map<String,Object>>> getStoreTypeMapListOne(HttpServletRequest request){
        Result<List<Map<String, Object>>> result = new Result<>();
        result.setResult(iStoreTypeService.getStoreTypeMapListOne());
        result.success("店铺一级分类");
        return result;
    }
    @RequestMapping("getStoreTypeMapListTwo")
    @ResponseBody
    public Result<List<Map<String,String>>> getStoreTypeMapListTwo(String id,
                                                                   HttpServletRequest request){
        Result<List<Map<String, String>>> result = new Result<>();
        result.setResult(iStoreTypeService.getStoreTypeMapListTwo(id));
        result.success("店铺二级分类");
        return result;
    }
    @RequestMapping("findStoreTypeList")
    @ResponseBody
    public Result<Dict> findStoreTypeList(HttpServletRequest request){
        Result<Dict> result = new Result<>();
        List<Map<String, Object>> maps = iStoreTypeService.listMaps(new QueryWrapper<StoreType>()
                .select("id,type_name as typeName")
                .eq("del_flag","0")
                .eq("status","1")
                .eq("level","1")
                .eq("pid","0")
                .orderByAsc("sort")
        );
        List<Map<String, Object>> twoStoreTypeList = maps.stream().peek(m -> {
            List<Map<String, Object>> getstoreTypeListById = iStoreTypeService.getstoreTypeListById(m.get("id").toString());
            long i = 0;
            for (Map<String, Object> stringObjectMap : getstoreTypeListById) {
                i += (Long) stringObjectMap.get("storeSum");
            }
            m.put("storeTypeList", getstoreTypeListById);
            m.put("storeSum", i);
        }).flatMap(stringObjectMap -> {
            List<Map<String, Object>> storeTypeList = (List<Map<String, Object>>) stringObjectMap.get("storeTypeList");
            return storeTypeList.stream();
        }).collect(Collectors.toList());
        result.setResult(Dict.create().set("twoStoreTypeList",twoStoreTypeList).set("oneStoreTypeList",maps));
        result.success("返回城市生活分类");
        return result;
    }
}
