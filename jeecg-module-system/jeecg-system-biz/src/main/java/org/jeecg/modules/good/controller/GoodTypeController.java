package org.jeecg.modules.good.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.good.dto.GoodListDto;
import org.jeecg.modules.good.dto.GoodTypeDto;
import org.jeecg.modules.good.entity.GoodList;
import org.jeecg.modules.good.entity.GoodType;
import org.jeecg.modules.good.service.IGoodListService;
import org.jeecg.modules.good.service.IGoodTypeService;
import org.jeecg.modules.good.vo.GoodListVo;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: 商品分类
 * @Author: jeecg-boot
 * @Date:   2019-10-16
 * @Version: V1.0
 */
@RestController
@RequestMapping("/goodType/goodType")
@Slf4j
public class GoodTypeController {
    @Autowired
    private IGoodTypeService goodTypeService;
    @Autowired
    private IGoodListService goodListService;

    /**
     * 根据分类id获取三级分类的id
     * @param id
     * @return
     */
    @GetMapping("getGoodTypeByThirdId")
    @Cacheable(value = "getGoodTypeByThirdId",key = "#id")
    public Result<?> getGoodTypeByThirdId(String id){
        Map<String,Object> resultMap= Maps.newHashMap();
        GoodType goodType3=goodTypeService.getById(id);
        GoodType goodType2=goodTypeService.getById(goodType3.getParentId());
        GoodType goodType=goodTypeService.getById(goodType2.getParentId());
        resultMap.put("3",goodType3);
        resultMap.put("2",goodType2);
        resultMap.put("1",goodType);
        return Result.ok(resultMap);
    }



    /**
     * 获取树状所有商品类型数据
     *
     * @return
     */
    @GetMapping("getGoodTypeByTree")
    @Cacheable(value = "getGoodTypeByTree")
    public Result<?> getGoodTypeByTree(){
        List<GoodType> goodTypes=goodTypeService.list(new LambdaQueryWrapper<GoodType>().eq(GoodType::getLevel,"1").eq(GoodType::getStatus,"1").orderByAsc(GoodType::getSort));
        goodTypes.stream().forEach(g-> {
            g.setChildren(goodTypeService.list(new LambdaQueryWrapper<GoodType>().eq(GoodType::getParentId,g.getId()).eq(GoodType::getStatus,"1").orderByAsc(GoodType::getSort)));
            g.getChildren().stream().forEach(g1 ->{
                g1.setChildren(goodTypeService.list(new LambdaQueryWrapper<GoodType>().eq(GoodType::getParentId,g1.getId()).eq(GoodType::getStatus,"1").orderByAsc(GoodType::getSort)));
            } );
        });
        return Result.ok(goodTypes);
    }



    /**
     * 分页列表查询
     * @param goodType
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/rootList")
    public Result<IPage<GoodType>> queryPageList(GoodType goodType,
                                                 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                 HttpServletRequest req) {
        if(oConvertUtils.isEmpty(goodType.getParentId())){
            goodType.setParentId("0");
        }
        Result<IPage<GoodType>> result = new Result<IPage<GoodType>>();
        QueryWrapper<GoodType> queryWrapper = QueryGenerator.initQueryWrapper(goodType, req.getParameterMap());
        queryWrapper.eq("parent_id","0");
        Page<GoodType> page = new Page<GoodType>(pageNo, pageSize);
        IPage<GoodType> pageList = goodTypeService.page(page, queryWrapper);
        List<GoodType> listGoodType =goodTypeService.getGoodTypeOrParentIdListtwo("0",null);
        Map<String,Object> map=new HashMap<>();
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    @GetMapping(value = "/childList")
    public Result<List<GoodType>> queryPageList(GoodType goodType,HttpServletRequest req) {
        Result<List<GoodType>> result = new Result<List<GoodType>>();
        QueryWrapper<GoodType> queryWrapper = QueryGenerator.initQueryWrapper(goodType, req.getParameterMap());
        List<GoodType> list = goodTypeService.list(queryWrapper);
        result.setSuccess(true);
        result.setResult(list);
        return result;
    }


    /**
     *   添加
     * @param goodType
     * @return
     */
    @PostMapping(value = "/add")
    @CacheEvict(value= "getGoodTypeByTree", allEntries=true)
    public Result<GoodType> add(@RequestBody GoodType goodType) {
        Result<GoodType> result = new Result<GoodType>();
        try {
            goodTypeService.addGoodType(goodType);
            result.success("添加成功！");
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            result.error500("操作失败");
        }
        return result;
    }

    /**
     *  编辑
     * @param goodType
     * @return
     */
    @PutMapping(value = "/edit")
    @CacheEvict(value= "getGoodTypeByTree", allEntries=true)
    public Result<GoodType> edit(@RequestBody GoodType goodType) {
        Result<GoodType> result = new Result<GoodType>();
        try {
            //goodTypeService.updateGoodType(goodType);
            goodTypeService.updateById(goodType);
            result.success("修改成功!");
        } catch (Exception e) {
            result.error500(e.getMessage());
        }
        return result;
    }

    /**
     *   通过id删除
     * @param id
     * @return
     */
    @DeleteMapping(value = "/delete")
    @CacheEvict(value= "getGoodTypeByTree", allEntries=true)
    public Result<GoodType> delete(@RequestParam(name="id",required=true) String id) {
        Result<GoodType> result = new Result<GoodType>();
        try {
            //添加删除判断是否可删除
            List<GoodType>  listGoodType=goodTypeService.getGoodTypeOrParentIdListtwo(id,null);
            Page<GoodList> page = new Page<GoodList>(1, 10);
            GoodListVo goodListVo =new GoodListVo();
            goodListVo.setGoodTypeIdOne(id);
            goodListVo.setGoodTypeIdTwo(id);
            goodListVo.setGoodTypeIdThree(id);
            IPage<GoodListDto> pageList = goodListService.getGoodListDto(page, goodListVo, null);
            if(pageList.getRecords().size()>0){
                result.error500("你若要删除该分类，请先删除子分类或者该分类底下的商品！");
                return result;
            }
             if(listGoodType.size()>0){
                 result.error500("该分类底下存在子分类或者商品，无法删除！");
                // result.setMessage("你若要删除该分类，请先删除子分类或者该分类底下的商品！");
                 return result;
             }
            goodTypeService.removeById(id);
            result.success("删除成功!");
        } catch (Exception e) {
            result.error500(e.getMessage());
        }
        return result;
    }

    /**
     *  批量删除
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatch")
    @CacheEvict(value= "getGoodTypeByTree", allEntries=true)
    public Result<GoodType> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
        Result<GoodType> result = new Result<GoodType>();
        if(ids==null || "".equals(ids.trim())) {
            result.error500("参数不识别！");
        }else {
            this.goodTypeService.removeByIds(Arrays.asList(ids.split(",")));
            result.success("删除成功!");
        }
        return result;
    }

    /**
     * 通过id查询
     * @param id
     * @return
     */
    @GetMapping(value = "/queryById")
    public Result<GoodType> queryById(@RequestParam(name="id",required=true) String id) {
        Result<GoodType> result = new Result<GoodType>();
        GoodType goodType = goodTypeService.getById(id);
        if(goodType==null) {
            result.error500("未找到对应实体");
        }else {
            result.setResult(goodType);
            result.setSuccess(true);
        }
        return result;
    }

    /**
     * 导出excel
     *
     * @param request
     * @param goodType
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GoodType goodType) {
        // Step.1 组装查询条件查询数据
        QueryWrapper<GoodType> queryWrapper = QueryGenerator.initQueryWrapper(goodType, request.getParameterMap());
        List<GoodType> pageList = goodTypeService.list(queryWrapper);
        // Step.2 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        // 过滤选中数据
        String selections = request.getParameter("selections");
        if(oConvertUtils.isEmpty(selections)) {
            mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
        }else {
            List<String> selectionList = Arrays.asList(selections.split(","));
            List<GoodType> exportList = pageList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
            mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
        }
        //导出文件名称
        mv.addObject(NormalExcelConstants.FILE_NAME, "商品分类列表");
        mv.addObject(NormalExcelConstants.CLASS, GoodType.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("商品分类列表数据", "导出人:Jeecg", "导出信息"));
        return mv;
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();// 获取上传文件对象
            ImportParams params = new ImportParams();
            params.setTitleRows(2);
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                List<GoodType> listGoodTypes = ExcelImportUtil.importExcel(file.getInputStream(), GoodType.class, params);
                goodTypeService.saveBatch(listGoodTypes);
                return Result.ok("文件导入成功！数据行数:" + listGoodTypes.size());
            } catch (Exception e) {
                log.error(e.getMessage(),e);
                return Result.error("文件导入失败:"+e.getMessage());
            } finally {
                try {
                    file.getInputStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return Result.ok("文件导入失败！");
    }

    /**
     * 修改状态及其关联分类状态修改
     * @param id
     * @return
     */

    @GetMapping(value = "/updateStatus")
    @CacheEvict(value= "getGoodTypeByTree", allEntries=true)
    public Result<GoodType> updateStatus(@RequestParam(name="id",required=true) String id,@RequestParam(name="status",required=true)String status,@RequestParam(name="stopRemark") String stopRemark) {
        Result<GoodType> result = new Result<GoodType>();
        GoodType goodType = goodTypeService.getById(id);
        QueryWrapper<GoodList> queryWrapper =new QueryWrapper<GoodList>();
        List<GoodList>  listGoodList;
        if(goodType==null) {
            result.error500("未找到对应实体");
        }else {

            //修改为停用
                goodType.setStatus(status);
                goodType.setStopRemark(stopRemark);
                //修改关联分类
                relevanceType(goodType.getId(),status);

                queryWrapper =new QueryWrapper<GoodList>();
                //该分类的商品修改
                /*（添加商品查询修改代码）*/
               if(goodType.getLevel().equals("2")){
                   queryWrapper.eq("good_type_id",goodType.getId());
                   listGoodList=goodListService.list(queryWrapper);
                   if(listGoodList.size()>0){
                       for( GoodList goodList:listGoodList){
                           goodList.setStatus(status);
                           goodListService.updateById(goodList);
                       }
                   }
               }



            boolean ok = goodTypeService.updateById(goodType);
            //TODO 返回false说明什么？
            if(ok) {
                result.success("修改成功!");
            }else{
                result.error500("修改失败！");
            }
        }
        return result;
    }


    /**
     * 关联分类修改
     * @param goodTypeId 分类Id
     * @param status 状态
     */
    public void relevanceType(String goodTypeId,String status){
        //关联分类修改
        List<GoodType>  listGoodTypeThree;
        List<GoodList>  listGoodList;
        //二级分类集合
        List<GoodType>  listGoodType=goodTypeService.getGoodTypeOrParentIdListtwo(goodTypeId,null);
        QueryWrapper<GoodList> queryWrapper =new QueryWrapper<GoodList>();
        if(listGoodType.size()>0){
            for(GoodType gt:listGoodType){
                gt.setStatus(status);
                goodTypeService.updateGoodType(gt);
                queryWrapper =new QueryWrapper<GoodList>();
                //该分类的商品修改
                queryWrapper.eq("good_type_id",gt.getId());
                listGoodList=goodListService.list(queryWrapper);
                if(listGoodList.size()>0){
                    for( GoodList goodList:listGoodList){
                        goodList.setStatus(status);
                        goodListService.updateById(goodList);
                    }
                }
                //修改第三级分类修改
                listGoodTypeThree=goodTypeService.getGoodTypeOrParentIdListtwo(gt.getId(),null);
                if(listGoodTypeThree.size()>0){
                    for(GoodType gtTheer:listGoodTypeThree){
                        gtTheer.setStatus(status);
                        goodTypeService.updateGoodType(gtTheer);

                        //该分类的商品修改
                        /*（添加商品查询修改代码）*/
                        queryWrapper =new QueryWrapper<GoodList>();
                        queryWrapper.eq("good_type_id",gtTheer.getId());
                        listGoodList=goodListService.list(queryWrapper);
                        if(listGoodList.size()>0){
                            for( GoodList goodList:listGoodList){
                                goodList.setStatus(status);
                                goodListService.updateById(goodList);
                            }
                        }
                    }
                }
            }
        }
    }

    /***
     * 分类级联列表查询
     * @param  parentId 父级Id
     * @return
     */
    @RequestMapping(value = "/getgoodTypeListcascade", method = RequestMethod.GET)
    public Result<Map<String,Object>> getgoodTypeListcascade(@RequestParam(name="parentId",required=true) String parentId){
    Result<Map<String,Object>> result = new Result<Map<String,Object>>();
    List<GoodType> listGoodType=null;
        Map<String,Object> map=new HashMap<String,Object>();
       listGoodType=goodTypeService.getGoodTypeOrParentIdListtwo(parentId,null);//二级或三级分类集合
        map.put("listGoodType",listGoodType);
    result.setSuccess(true);
    result.setResult(map);
    return result;
}
    /***
     * 根据分类名称取所有三级类目名称
     * @param  nodeName 分类名称
     * @return
     */
    @RequestMapping(value = "/getgoodTypeListName", method = RequestMethod.GET)
    public Result<Map<String,Object>> getgoodTypeListName(@RequestParam(name="nodeName",required=true) String nodeName,@RequestParam(name="level",required=true)String level){
        Result<Map<String,Object>> result = new Result<Map<String,Object>>();
        Map<String,Object> map=new HashMap<String,Object>();
        List<GoodTypeDto> listGoodType=goodTypeService.getgoodTypeListName(nodeName,level);//二级或三级分类集合
        map.put("listGoodType",listGoodType);
        result.setSuccess(true);
        result.setResult(map);
        return result;
    }
    /***
     * 根据分类名称和节点名称搜列表
     * @param  name 分类名称
     * @return
     */
    @RequestMapping(value = "/getgoodTypeListNameAndParentId", method = RequestMethod.GET)
    public Result<Map<String,Object>> getgoodTypeListNameAndParentId(@RequestParam(name="name",required=true) String name,@RequestParam(name="parentId",required=true)String parentId){
        Result<Map<String,Object>> result = new Result<Map<String,Object>>();
        Map<String,Object> map=new HashMap<String,Object>();
        List<Map<String,String>> listGoodType=goodTypeService.getGoodTypeListNameAndParentId(name,parentId);//二级或三级分类集合
        map.put("listGoodType",listGoodType);
        result.setSuccess(true);
        result.setResult(map);
        return result;
    }
    /***
     * 封装分类树
     * @param id
     * @return
     */
    @RequestMapping(value = "/encapsulationlassificationTree", method = RequestMethod.GET)
    public Result<Map<String,Object>> encapsulationlassificationTree(@RequestParam(name="id",required=true) String id, HttpServletRequest request){
        // String parentId=json.getString("parentId");
        Result<Map<String,Object>> result = new Result<Map<String,Object>>();
        String userName=JwtUtil.getUserNameByToken(request);
        //String userName="jeecg";
        Map<String,Object> map=goodTypeService.encapsulationlassificationTree(id,userName);//二级或三级分类集合
        result.setSuccess(true);
        result.setResult(map);
        return result;
    }

    /**
     * 判断是否是三级
     * @return
     */
    @GetMapping(value = "/ClassificationJudgment")
    public Result<Boolean>  ClassificationJudgment(@RequestParam(name = "goodTypeId")String goodTypeId){
        Result<Boolean> result = new Result<Boolean>();
        Boolean bl;

        QueryWrapper<GoodType> queryWrapper = new QueryWrapper<GoodType>();
        queryWrapper.eq("id",goodTypeId );
        queryWrapper.eq("level","3" );
        List<GoodType> listGoodType= goodTypeService.list(queryWrapper);
        if(listGoodType.size()>0){
            bl = true;
        }else{
            bl = false;
        }
        result.setResult(bl);
        return  result;
    }

    /**
     * 复制分类地址
     * @param goodTypeId
     * @return
     */
    @AutoLog(value = "商品列表-分页列表查询")
    @ApiOperation(value = "商品列表-分页列表查询", notes = "商品列表-分页列表查询")
    @GetMapping(value = "/getTypeUrl")
    public  Result<Map<String,Object>> getTypeUrl(@RequestParam(name = "goodTypeId")String goodTypeId,@RequestParam(name = "name") String name) {
        Result<Map<String,Object>> result = new Result<>();
        Map<String,String> map = Maps.newHashMap();
        map.put("goodTypeId",goodTypeId);
        map.put("isPlatform","1");
        map.put("name",name);
        String url = "pages/index/search/search?info=";
        Map<String,Object> mapObject = Maps.newHashMap();
        mapObject.put("url",url);
        mapObject.put("parameter", JSONObject.toJSONString(map));
        result.setResult(mapObject);
        result.setSuccess(true);
        return result;
    }






}
