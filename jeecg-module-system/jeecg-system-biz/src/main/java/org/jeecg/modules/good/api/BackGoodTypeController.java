package org.jeecg.modules.good.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.good.entity.GoodList;
import org.jeecg.modules.good.entity.GoodStoreType;
import org.jeecg.modules.good.entity.GoodType;
import org.jeecg.modules.good.service.IGoodListService;
import org.jeecg.modules.good.service.IGoodStoreListService;
import org.jeecg.modules.good.service.IGoodStoreTypeService;
import org.jeecg.modules.good.service.IGoodTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 商品类型控制器
 */
@Controller
@RequestMapping("back/goodType")
public class BackGoodTypeController {
    @Autowired
    private IGoodTypeService iGoodTypeService;
    @Autowired
    private IGoodStoreTypeService iGoodStoreTypeService;
    @Autowired
    private IGoodStoreListService iGoodStoreListService;
    @Autowired
    private IGoodListService iGoodListService;
    /**
     * 获取自选商品分类
     *
     * @return
     */
    @RequestMapping("findOpentionGoodTypes")
    @ResponseBody
    public Result<List<Map<String,Object>>> findOpentionGoodTypes(){
        Result<List<Map<String,Object>>> result=new Result<>();

        result.setResult(iGoodTypeService.findTopGoodType());

        result.success("查询商品类型数据成功");
        return  result;
    }

    /**
     * 获取第一级分类id和名称
     * @return
     */
    @RequestMapping("findTopGoodType")
    @ResponseBody
    public Result<List<Map<String,Object>>> findTopGoodType(@RequestHeader(defaultValue = "") String sysUserId){
        Result<List<Map<String,Object>>> result=new Result<>();

        List<Map<String,Object>> mapList= Lists.newArrayList();


        //加上店铺信息
        Map<String, Object> objectMap = Maps.newHashMap();
        objectMap.put("name", "推荐分类");
        objectMap.put("isPlatform", "0");
        objectMap.put("id", sysUserId);
        mapList.add(objectMap);

        //加入平台商品
        CollectionUtils.addAll(mapList,  iGoodTypeService.findTopGoodType().iterator());

        result.setResult(mapList);

        result.success("查询商品类型数据成功");
        return  result;
    }

    /**
     * 根据分类的信息拿到分类子信息
     * @param goodTypeId
     * @param isPlatform
     * @return
     */
    @RequestMapping("findMoreGoodType")
    @ResponseBody
    public Result<List<Map<String,Object>>>   findMoreGoodType(String goodTypeId,Integer isPlatform){
        Result<List<Map<String,Object>>> result=new Result<>();

        //判断数据不为空
        if(isPlatform==null){
            result.error500("isPlatform  是否平台类型参数不能为空！！！   ");
            return  result;
        }

        if(StringUtils.isBlank(goodTypeId)){
            result.error500("goodTypeId  商品类型id不能为空！！！   ");
            return  result;
        }


        //查询店铺店铺商品类型
        if(isPlatform.intValue()==0){
            List<Map<String,Object>> goodMapList=iGoodStoreTypeService.findGoodTypeBySysUserId(goodTypeId);
            goodMapList.stream().forEach(map -> {
                map.put("chlidGoodType",iGoodStoreTypeService.findGoodTypeByParentId(map.get("id")+""));
            });
            result.setResult(goodMapList);

            //人气推荐（二期开发）

            //热门分类（二期开发）


        }else
            //查询平台商品类型
            if(isPlatform.intValue()==1){
                List<Map<String,Object>> goodMapList= iGoodTypeService.findGoodTypeByParentId(goodTypeId);
                goodMapList.stream().forEach(map -> {
                    map.put("chlidGoodType",iGoodTypeService.findGoodTypeByParentId(map.get("id")+""));
                });
                result.setResult(goodMapList);
            }else{
                result.error500("isPlatform  参数不正确请联系平台管理员！！！  ");
                return  result;
            }
        result.success("查询商品类型数据成功");
        return  result;
    }

    /**
     * 分类推荐查询9条
     * @return
     */
    @RequestMapping("classificationOfRecommendation")
    @ResponseBody
    public Result<Map<String,Object>>  classificationOfRecommendation(@RequestHeader(defaultValue = "") String sysUserId){
        Result<Map<String,Object>> result=new Result<>();
        Map<String,Object> objectMap = Maps.newHashMap();
        /*if(StringUtils.isBlank(sysUserId)){
            result.error500("店铺信息为空！");
            return result;
        }*/
        try{
            //人气推荐（二期开发）
            List<Map<String,Object>> popularityGoodStoreTypeMapList = iGoodStoreTypeService.getPopularityRecommended(sysUserId,3);
            if(popularityGoodStoreTypeMapList.size() == 3){
                //3条数据
                // result.setResult(popularityGoodStoreTypeMapList);
            }else{
                if(popularityGoodStoreTypeMapList.size()>0){
                    Integer limitPopularity = 9-popularityGoodStoreTypeMapList.size();
                    List<Map<String,Object>>  goodTypeMapList = iGoodTypeService.getPopularityRecommended(limitPopularity);
                    for(Map<String,Object> goodTypemap:goodTypeMapList){
                        popularityGoodStoreTypeMapList.add(goodTypemap);
                    }
                    //result.setResult(popularityGoodStoreTypeMapList);
                }else{
                    popularityGoodStoreTypeMapList = iGoodTypeService.getPopularityRecommended(3);
                    // result.setResult(goodTypeMapList);
                }
            }
            //热门分类（二期开发）
            List<Map<String,Object>> hotgoodStoreTypeMapList = iGoodStoreTypeService.getHotRecommended(sysUserId,9);
            if(hotgoodStoreTypeMapList.size() == 9){
                //九条数据
                // result.setResult(hotgoodStoreTypeMapList);
            }else{
                if(hotgoodStoreTypeMapList.size()>0){
                    Integer limitHOT = 9-hotgoodStoreTypeMapList.size();
                    List<Map<String,Object>>  goodTypeMapList = iGoodTypeService.getHotRecommended(limitHOT);
                    for(Map<String,Object> goodTypemap:goodTypeMapList){
                        hotgoodStoreTypeMapList.add(goodTypemap);
                    }
                    //   result.setResult(hotgoodStoreTypeMapList);
                }else{
                    hotgoodStoreTypeMapList = iGoodTypeService.getHotRecommended(9);
                    //  result.setResult(hotgoodStoreTypeMapList);
                }
            }
            objectMap.put("popularityGoodStoreTypeMapList",popularityGoodStoreTypeMapList);
            objectMap.put("hotgoodStoreTypeMapList",hotgoodStoreTypeMapList);
            result.setResult(objectMap);
            result.success("分类推荐查询成功");
            return  result;
        }catch (Exception e){
            e.printStackTrace();
        }



        return  result;
    }
    //人气推荐
    @RequestMapping("getPopularityGoodStoreType")
    @ResponseBody
    public Result<Map<String,Object>>  getPopularityGoodStoreType(@RequestHeader(defaultValue = "") String sysUserId){
        Result<Map<String,Object>> result=new Result<>();
        Map<String,Object> objectMap = Maps.newHashMap();
        if(StringUtils.isBlank(sysUserId)){
            List<Map<String,Object>>   popularityGoodStoreTypeMapList = iGoodTypeService.getGoodTypeNumberAndAvgDesc(3);
            objectMap.put("popularityGoodStoreTypeMapList",popularityGoodStoreTypeMapList);
            result.setResult(objectMap);
            result.success("分类推荐查询成功");
            return  result;
        }
        //人气推荐（二期开发）
        List<Map<String,Object>> popularityGoodStoreTypeMapList = iGoodStoreTypeService.getGoodStoreTypeNumberAndAvgDesc(sysUserId,3);
        if(popularityGoodStoreTypeMapList.size() == 3){
            //3条数据
            // result.setResult(popularityGoodStoreTypeMapList);
        }else{
            if(popularityGoodStoreTypeMapList.size()>0){
                Integer limitPopularity = 3-popularityGoodStoreTypeMapList.size();
                List<Map<String,Object>>  goodTypeMapList = iGoodTypeService.getGoodTypeNumberAndAvgDesc(limitPopularity);
                for(Map<String,Object> goodTypemap:goodTypeMapList){
                    popularityGoodStoreTypeMapList.add(goodTypemap);
                }
                //result.setResult(popularityGoodStoreTypeMapList);
            }else{
                popularityGoodStoreTypeMapList = iGoodTypeService.getGoodTypeNumberAndAvgDesc(3);
                // result.setResult(goodTypeMapList);
            }
        }
        objectMap.put("popularityGoodStoreTypeMapList",popularityGoodStoreTypeMapList);
        result.setResult(objectMap);
        result.success("分类推荐查询成功");
        return  result;
    }
    //热门分类
    @RequestMapping("getHotgoodStoreType")
    @ResponseBody
    public  Result<Map<String,Object>> getHotgoodStoreType(@RequestHeader(defaultValue = "") String sysUserId) {
        Result<Map<String, Object>> result = new Result<>();
        Map<String, Object> objectMap = Maps.newHashMap();
        if(StringUtils.isBlank(sysUserId)){
            List<Map<String,Object>>    hotgoodStoreTypeMapList = iGoodTypeService.getGoodTypeNumberDesc(9);
            objectMap.put("hotgoodStoreTypeMapList",hotgoodStoreTypeMapList);
            result.setResult(objectMap);
            result.success("分类推荐查询成功");
            return  result;
        }
        //热门分类（二期开发）
        List<Map<String,Object>> hotgoodStoreTypeMapList = iGoodStoreTypeService.getGoodStoreTypeNumberDesc(sysUserId,9);
        if(hotgoodStoreTypeMapList.size() == 9){
        }else{
            if(hotgoodStoreTypeMapList.size()>0){
                Integer limitHOT = 9-hotgoodStoreTypeMapList.size();
                List<Map<String,Object>>  goodTypeMapList = iGoodTypeService.getGoodTypeNumberDesc(limitHOT);
                for(Map<String,Object> goodTypemap:goodTypeMapList){
                    hotgoodStoreTypeMapList.add(goodTypemap);
                }

            }else{
                hotgoodStoreTypeMapList = iGoodTypeService.getGoodTypeNumberDesc(9);
            }
        }
        objectMap.put("hotgoodStoreTypeMapList",hotgoodStoreTypeMapList);
        result.setResult(objectMap);
        result.success("分类推荐查询成功");
        return  result;
    }
    /**
     * 好货上新查询分类
     * @return
     */
    @RequestMapping("getEverydayGoodList")
    @ResponseBody
    public Result<Map<String,Object>> getEverydayGoodList(@RequestHeader(defaultValue = "") String sysUserId){
        Result<Map<String,Object>> result=new Result<>();
       /* if(StringUtils.isBlank(sysUserId)){
            result.error500("店铺信息为空！");
            return result;
        }*/
        Map<String,Object> objectMap = Maps.newHashMap();
        List<Map<String,Object>> listStoreTypeMap =Lists.newArrayList();
        List<Map<String,Object>> listTypeMap = Lists.newArrayList();
        Date date = new Date();
        //ClassificationOfRecommendation
        //获取当前时间的前一天数据
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -1);
        Date start = c.getTime();
        String qyt= df.format(start);
        List<Map<String,Object>> listStoreMap = iGoodStoreListService.getEverydayGoodStoreTypeId(sysUserId,qyt,6);
        List<String> strings = new ArrayList<>();
        List<String> stringGoods = new ArrayList<>();
        listStoreMap.forEach(ls ->{
            strings.add(ls.get("id").toString());
        });
        QueryWrapper<GoodStoreType> queryWrapper = new QueryWrapper<GoodStoreType>();
        if(strings.size()>0){
            queryWrapper.in("id",strings);
            queryWrapper.eq("sys_user_id",sysUserId);
            listStoreTypeMap = iGoodStoreTypeService.listMaps(queryWrapper);
            listStoreTypeMap.forEach(ls ->{
                ls.put("isPlatform","0");
            });
        }
        if(listStoreTypeMap.size()>0){
            if(listStoreTypeMap.size() > 6){
                //店铺商品6条
                //添加判断店铺：0 平台：1
                listStoreTypeMap.forEach(ls ->{
                    ls.put("isPlatform","0");
                });
            }else{
                //不足补充平台商品
                List<Map<String,Object>> listMap = iGoodListService.getEverydayGoodTypeId(qyt,(6-listStoreTypeMap.size()));
                listMap.forEach(ls ->{
                    stringGoods.add(ls.get("id").toString());
                });
                QueryWrapper<GoodType> queryWrapperGoodType = new QueryWrapper<GoodType>();
                if(stringGoods.size()>0){
                    queryWrapperGoodType.in("id",stringGoods);
                    //queryWrapperGoodType.in("id",String.valueOf(stringGoods));
                    listTypeMap = iGoodTypeService.listMaps(queryWrapperGoodType);
                    //添加判断店铺：0 平台：1
                    listTypeMap.forEach(ls ->{
                        ls.put("isPlatform","1");
                    });
                }else{
                    //都无商品时处理，最近创建商品的前六个分类
                    queryWrapperGoodType.orderByDesc("create_time");
                    queryWrapperGoodType.eq("status","1");
                    queryWrapperGoodType.last("limit 6");
                    listTypeMap = iGoodTypeService.listMaps(queryWrapperGoodType);
                    //添加判断店铺：0 平台：1
                    listTypeMap.forEach(ls ->{
                        ls.put("isPlatform","1");
                    });
                }
                for(Map<String,Object>  mapS: listTypeMap){
                    listStoreTypeMap.add(mapS);
                }

            }

        }else{
            //店铺没有上新，所有都取平台数据
            List<Map<String,Object>> listMap = iGoodListService.getEverydayGoodTypeId(qyt,6);
            listMap.forEach(ls ->{
                stringGoods.add(ls.get("id").toString());
            });
            QueryWrapper<GoodType> queryWrapperGoodType = new QueryWrapper<GoodType>();
            if(stringGoods.size()>0){
                queryWrapperGoodType.in("id",stringGoods);
                listTypeMap = iGoodTypeService.listMaps(queryWrapperGoodType);
                //添加判断店铺：0 平台：1
                listTypeMap.forEach(ls ->{
                    ls.put("isPlatform","1");
                });
            }else{
                QueryWrapper<GoodList> queryWrapperGoodList = new QueryWrapper<GoodList>();
                //都无商品时处理，最近创建商品的前六个分类
                queryWrapperGoodList.orderByDesc("create_time");
                queryWrapperGoodList.groupBy("good_type_id");
                queryWrapperGoodList.eq("status","1");
                queryWrapperGoodList.eq("good_form","0");
                queryWrapperGoodList.last("limit 6");
                listMap = iGoodListService.listMaps(queryWrapperGoodList);
                listMap.forEach(ls ->{
                    stringGoods.add(ls.get("good_type_id").toString());
                });
                queryWrapperGoodType.in("id",stringGoods);
                listTypeMap = iGoodTypeService.listMaps(queryWrapperGoodType);
                //添加判断店铺：0 平台：1
                listTypeMap.forEach(ls ->{
                    ls.put("isPlatform","1");
                });
            }
            for(Map<String,Object>  mapS: listTypeMap){
                listStoreTypeMap.add(mapS);
            }
        }
        objectMap.put("listStoreTypeMap",listStoreTypeMap);
        result.setResult(objectMap);
        result.success("好货上新查询分类查询成功");
        return  result;
    }

    /**
     * 查询店铺商品分类
     * @param parentId 父级id 为空查询一级分类
     * @param request
     * @return
     */
    @RequestMapping("getGoodStoreTypeMaps")
    @ResponseBody
    public Result<List<Map<String,Object>>> getGoodStoreTypeMaps(String parentId,HttpServletRequest request){
        Result<List<Map<String,Object>>> result = new Result<>();
        String sysUserId=request.getAttribute("sysUserId").toString();
        QueryWrapper queryWrapperGoodStoreType =new QueryWrapper();
        queryWrapperGoodStoreType.select("id,name,status,type_picture as typePicture,0 as isPlatform");
        queryWrapperGoodStoreType.eq("sys_user_id",sysUserId );
        if(StringUtils.isNotBlank(parentId)){
            //第二级店铺商品分类
            queryWrapperGoodStoreType.eq("parent_id",parentId);
            queryWrapperGoodStoreType.eq("level","2");
        }else{
            //第一级店铺商品分类
            queryWrapperGoodStoreType.eq("parent_id","0");
            queryWrapperGoodStoreType.eq("level","1");
        }
        List<Map<String,Object>> goodStoreTypeMapList = iGoodStoreTypeService.listMaps(queryWrapperGoodStoreType);

        result.success("查询分类成功");
        result.setResult(goodStoreTypeMapList);
       return result;
    }


}
