package org.jeecg.modules.good.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.NodeVo;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.good.dto.GoodTypeDto;
import org.jeecg.modules.good.entity.GoodType;
import org.jeecg.modules.good.mapper.GoodTypeMapper;
import org.jeecg.modules.good.service.IGoodTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 商品分类
 * @Author: jeecg-boot
 * @Date:   2019-10-16
 * @Version: V1.0
 */
@Service
@Slf4j
public class GoodTypeServiceImpl extends ServiceImpl<GoodTypeMapper, GoodType> implements IGoodTypeService {
    @Autowired(required = false)
    private GoodTypeMapper goodTypeMapper;


    @Override
    public void addGoodType(GoodType goodType) {
        if(oConvertUtils.isEmpty(goodType.getParentId())){
            goodType.setParentId(IGoodTypeService.ROOT_PID_VALUE);
        }else{
            //如果当前节点父ID不为空 则设置父节点的hasChildren 为1
            GoodType parent = baseMapper.selectById(goodType.getParentId());
            if(parent!=null && !"1".equals(parent.getHasChild())){
                parent.setHasChild("1");
                baseMapper.updateById(parent);
            }
        }
        baseMapper.insert(goodType);
    }

    @Override
    public void updateGoodType(GoodType goodType) {
        GoodType entity = this.getById(goodType.getId());
        if(entity==null) {
            throw new JeecgBootException("未找到对应实体");
        }
        String old_pid = entity.getParentId();
        String new_pid = goodType.getParentId();
        if(!old_pid.equals(new_pid)) {
            updateOldParentNode(old_pid);
            if(oConvertUtils.isEmpty(new_pid)){
                goodType.setParentId(IGoodTypeService.ROOT_PID_VALUE);
            }
            if(!IGoodTypeService.ROOT_PID_VALUE.equals(goodType.getParentId())) {
                baseMapper.updateTreeNodeStatus(goodType.getParentId(), IGoodTypeService.HASCHILD);
            }
        }
        baseMapper.updateById(goodType);
    }

    @Override
    public void deleteGoodType(String id) throws JeecgBootException {
        GoodType goodType = this.getById(id);
        if(goodType==null) {
            throw new JeecgBootException("未找到对应实体");
        }
        updateOldParentNode(goodType.getParentId());
        baseMapper.deleteById(id);
    }



    /**
     * 根据所传pid查询旧的父级节点的子节点并修改相应状态值
     * @param pid
     */
    private void updateOldParentNode(String pid) {
        if(!IGoodTypeService.ROOT_PID_VALUE.equals(pid)) {
            Long count = baseMapper.selectCount(new QueryWrapper<GoodType>().eq("parent_id", pid));
            if(count==null || count<=1) {
                baseMapper.updateTreeNodeStatus(pid, IGoodTypeService.NOCHILD);
            }
        }
    }
    @Override
    public List<GoodType> getGoodTypeOrParentIdListtwo(String parentId,String level){
        List<GoodType> listGoodType=goodTypeMapper.getGoodTypeOrParentIdListtwo(parentId,level);
     return listGoodType;
    }
    @Override
    public List<GoodTypeDto> getgoodTypeListName(String nodeName,String level){
        List<Map<String,Object>> listGoodType=goodTypeMapper.getgoodTypeListName(nodeName,level);
        List<GoodTypeDto> goodTypeDtos=new ArrayList<GoodTypeDto>();
        if("1".equals(level)){
            listGoodType.forEach(map->{
                List<GoodType> goodTypes= getGoodTypeOrParentIdListtwo(map.get("id").toString(),"2");
                goodTypes.forEach(gts->{
                    GoodTypeDto goodTypeDto=new GoodTypeDto();
                    goodTypeDto.setGoodTypeGrandsonId(gts.getId());
                    goodTypeDto.setGoodTypeParentName(gts.getName());
                    goodTypeDtos.add(goodTypeDto);

                });

            });
        }
        if("2".equals(level)){
            listGoodType.forEach(map->{
                List<GoodType> goodTypes= getGoodTypeOrParentIdListtwo(map.get("id").toString(),"2");
                goodTypes.forEach(gts->{
                    GoodTypeDto goodTypeDto=new GoodTypeDto();
                    goodTypeDto.setGoodTypeGrandsonId(gts.getId());
                    goodTypeDto.setGoodTypeSonName(gts.getName());
                    goodTypeDtos.add(goodTypeDto);

                });

            });
        }
        if("3".equals(level)){
            listGoodType.forEach(map->{
               GoodTypeDto goodTypeDto= getGoodTypeByGoodTyeId3(map.get("id").toString());
                goodTypeDtos.add(goodTypeDto);
            });
        }
        return goodTypeDtos;
    }

    @Override
    @Transactional
    public Map<String,Object> encapsulationlassificationTree(String id,String userName) {
        Map<String,Object> map=new HashMap<String,Object>();
        GoodType goodType3= goodTypeMapper.selectById(id);
        GoodType goodType2=goodTypeMapper.selectById(goodType3.getParentId());
        GoodType goodType1=goodTypeMapper.selectById(goodType2.getParentId());
        NodeVo  nodeVo=new  NodeVo();
        nodeVo.setId(goodType1.getId());
        nodeVo.setName(goodType1.getName());
        nodeVo.setChildrenNode(getChildrenNodes(goodType1.getId()));
        map.put("nodeVo",nodeVo);
        map.put("goodTypeDto",getGoodTypeByGoodTyeId3(id));
        return map;
    }
    @Override
    public GoodTypeDto getGoodTypeByGoodTyeId3(String goodTypeId3){
        GoodType goodType3= goodTypeMapper.selectById(goodTypeId3);
        GoodType goodType2=goodTypeMapper.selectById(goodType3.getParentId());
        GoodType goodType1=goodTypeMapper.selectById(goodType2.getParentId());
        GoodTypeDto goodTypeDto =new GoodTypeDto();
        goodTypeDto.setGoodTypeParentName(goodType1.getName());
        goodTypeDto.setGoodTypeSonName(goodType2.getName());
        goodTypeDto.setGoodTypeGrandsonName(goodType3.getName());
        goodTypeDto.setGoodTypeGrandsonId(goodTypeId3);
        return goodTypeDto;
    }

    @Override
    public List<Map<String,String>> getGoodTypeListNameAndParentId(String name, String parentId) {
        return goodTypeMapper.selectGoodTypeByParentIdAndName(name,parentId);
    }

    public List<NodeVo> getChildrenNodes(String parentId){
        List<NodeVo> childList = new ArrayList<NodeVo>();
        List<GoodType> goodTypes=goodTypeMapper.selectGoodTypeByParentId(parentId);
        goodTypes.forEach(goodType -> {
            NodeVo  nodeVo=new  NodeVo();
            nodeVo.setId(goodType.getId());
            nodeVo.setName(goodType.getName());
            nodeVo.setParentId(goodType.getParentId());
            nodeVo.setChildrenNode(getChildrenNodes(goodType.getId()));
            childList.add(nodeVo);
        });
        return childList;
    }

    @Override
    public   List<Map<String,Object>> findTopGoodType(){
        return goodTypeMapper.findTopGoodType();
    }

    @Override
    public List<Map<String, Object>> findGoodTypeByParentId(String parentId) {
        return goodTypeMapper.findGoodTypeByParentId(parentId);
    }

    /***
     * 热门推荐分类查询
     * @param limit
     * @return
     */
    @Override
    public List<Map<String,Object>> getPopularityRecommended(Integer limit){
        Map<String,Object> mapList= new HashMap<>();
        List<String> strList =new ArrayList<>() ;
        return  goodTypeMapper.getHotRecommended(limit);
    };

    /***
     * 人气推荐分类查询
     * @param limit
     * @return
     */
    @Override
    public List<Map<String,Object>> getHotRecommended(Integer limit){
        Map<String,Object> mapList= new HashMap<>();
        List<String> strList =new ArrayList<>() ;
        List<Map<String,Object>> list = goodTypeMapper.getHotRecommended(limit);
        if(list.size()>0){
            for( Map<String,Object> mapList1:list){
                strList.add(mapList1.get("id").toString());
            }
           return goodTypeMapper.getHotRecommendedAVG(strList,list.size());
        }

        return null;
    };
    @Override
    public List<Map<String,Object>> getHotRecommendedAVG(List<String> goodTypeIds,Integer limit){
        return goodTypeMapper.getHotRecommendedAVG(goodTypeIds,limit);
    };

    /**
     * 人气排行根据购买分类内商品数量排行
     * @param limit
     * @return
     */
    @Override
    public List<Map<String,Object>> getGoodTypeNumberDesc(Integer limit){
        List<Map<String,Object>> list =    goodTypeMapper.getGoodTypeNumberDesc(limit);

        return list;
    }

    /**
     * 根据用户评分排行
     * @param goodTypeIds
     * @param limit
     * @return
     */
    @Override
    public List<Map<String,Object>>  getGoodTypeAvgDesc(List<String> goodTypeIds,Integer limit){
        return goodTypeMapper.getGoodTypeAvgDesc(goodTypeIds,limit);
    }
    /**
     * 人气排行根据购买分类内商品数量排行
     * @param limit
     * @return
     */
   @Override
   public List<Map<String,Object>> getGoodTypeNumberAndAvgDesc(Integer limit){
       List<Map<String,Object>> list =    goodTypeMapper.getGoodTypeNumberDesc(limit);
       if(list.size()>0){
           List<String> stringList=new ArrayList<>() ;
           for(Map<String,Object> map :list){
               stringList.add(map.get("id").toString());
           }
           List<Map<String,Object>> goodTypeList =   goodTypeMapper.getGoodTypeAvgDesc(stringList,limit);
           return goodTypeList;
       }
       return list;
   };
}
