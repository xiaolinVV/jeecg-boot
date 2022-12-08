package org.jeecg.modules.good.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.NodeVo;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.good.dto.GoodStoreTypeDto;
import org.jeecg.modules.good.entity.GoodStoreType;
import org.jeecg.modules.good.mapper.GoodStoreTypeMapper;
import org.jeecg.modules.good.service.IGoodStoreTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 商品分类
 * @Author: jeecg-boot
 * @Date:   2019-10-17
 * @Version: V1.0
 */
@Service
@Slf4j
public class GoodStoreTypeServiceImpl extends ServiceImpl<GoodStoreTypeMapper, GoodStoreType> implements IGoodStoreTypeService {
@Autowired(required = false)
private GoodStoreTypeMapper goodStoreTypeMapper;

	@Override
	public void addGoodStoreType(GoodStoreType goodStoreType) {
		if(oConvertUtils.isEmpty(goodStoreType.getParentId())){
			goodStoreType.setParentId(IGoodStoreTypeService.ROOT_PID_VALUE);
		}else{
			//如果当前节点父ID不为空 则设置父节点的hasChildren 为1
			GoodStoreType parent = baseMapper.selectById(goodStoreType.getParentId());
			if(parent!=null && !"1".equals(parent.getHasChild())){
				parent.setHasChild("1");
				baseMapper.updateById(parent);
			}
		}
		baseMapper.insert(goodStoreType);
	}
	
	@Override
	public void updateGoodStoreType(GoodStoreType goodStoreType) {
		GoodStoreType entity = this.getById(goodStoreType.getId());
		if(entity==null) {
			throw new JeecgBootException("未找到对应实体");
		}
		String old_pid = entity.getParentId();
		String new_pid = goodStoreType.getParentId();
		if(!old_pid.equals(new_pid)) {
			updateOldParentNode(old_pid);
			if(oConvertUtils.isEmpty(new_pid)){
				goodStoreType.setParentId(IGoodStoreTypeService.ROOT_PID_VALUE);
			}
			if(!IGoodStoreTypeService.ROOT_PID_VALUE.equals(goodStoreType.getParentId())) {
				baseMapper.updateTreeNodeStatus(goodStoreType.getParentId(), IGoodStoreTypeService.HASCHILD);
			}
		}
		baseMapper.updateById(goodStoreType);
	}
	
	@Override
	public void deleteGoodStoreType(String id) throws JeecgBootException {
		GoodStoreType goodStoreType = this.getById(id);
		if(goodStoreType==null) {
			throw new JeecgBootException("未找到对应实体");
		}
		updateOldParentNode(goodStoreType.getParentId());
		baseMapper.deleteById(id);
	}



	/**
	 * 根据所传pid查询旧的父级节点的子节点并修改相应状态值
	 * @param pid
	 */
	private void updateOldParentNode(String pid) {
		if(!IGoodStoreTypeService.ROOT_PID_VALUE.equals(pid)) {
			Long count = baseMapper.selectCount(new QueryWrapper<GoodStoreType>().eq("parent_id", pid));
			if(count==null || count<=1) {
				baseMapper.updateTreeNodeStatus(pid, IGoodStoreTypeService.NOCHILD);
			}
		}
	}
	public List<GoodStoreType> getGoodStoreTypeOrParentIdListtwo(String parentId, String level,String sysUserId){
		List<GoodStoreType> listGoodStoreType=goodStoreTypeMapper.getGoodTypeOrParentIdListtwo(parentId,level,sysUserId);
		return listGoodStoreType;
	}

	@Override
	public List<GoodStoreType> getGoodTypeOrParentIdListtwo(String parentId, String level,String sysUserId) {
		List<GoodStoreType> listGoodType=goodStoreTypeMapper.getGoodTypeOrParentIdListtwo(parentId,level,sysUserId);
		return listGoodType;
	}

	@Override
	public List<GoodStoreTypeDto> getgoodTypeListName(String nodeName, String level,String sysUserId) {
		List<Map<String,Object>> listGoodType=goodStoreTypeMapper.getgoodTypeListName(nodeName,level,sysUserId);
		List<GoodStoreTypeDto> goodTypeDtos=new ArrayList<GoodStoreTypeDto>();
		if("1".equals(level)){
			listGoodType.forEach(map->{
				List<GoodStoreType> goodTypes= getGoodTypeOrParentIdListtwo(map.get("id").toString(),"2",map.get("sysUserId").toString());
				goodTypes.forEach(gts->{
					GoodStoreTypeDto goodTypeDto=new GoodStoreTypeDto();
					goodTypeDto.setGoodTypeGrandsonId(gts.getId());
					goodTypeDto.setGoodTypeParentName(gts.getName());
					goodTypeDtos.add(goodTypeDto);

				});

			});
		}
		if("2".equals(level)){
			listGoodType.forEach(map->{
				GoodStoreTypeDto goodTypeDto= getGoodTypeByGoodTyeId3(map.get("id").toString());
				goodTypeDtos.add(goodTypeDto);
			});
		}
		return goodTypeDtos;
	}

	@Override
	public Map<String, Object> encapsulationlassificationTree(String id, String userName) {
		Map<String,Object> map=new HashMap<String,Object>();
		GoodStoreType goodType2= goodStoreTypeMapper.selectById(id);
		GoodStoreType goodType1=goodStoreTypeMapper.selectById(goodType2.getParentId());
		NodeVo nodeVo=new  NodeVo();
		nodeVo.setId(goodType1.getId());
		nodeVo.setName(goodType1.getName());
		nodeVo.setChildrenNode(getChildrenNodes(goodType1.getId()));
		map.put("nodeVo",nodeVo);
		map.put("goodTypeDto",getGoodTypeByGoodTyeId3(id));
		return map;
	}

	@Override
	public GoodStoreTypeDto getGoodTypeByGoodTyeId3(String goodTypeId2) {
		GoodStoreType goodType2= goodStoreTypeMapper.selectById(goodTypeId2);
		GoodStoreType goodType1=goodStoreTypeMapper.selectById(goodType2.getParentId());
		GoodStoreTypeDto goodTypeDto =new GoodStoreTypeDto();
		goodTypeDto.setGoodTypeParentName(goodType1.getName());
		goodTypeDto.setGoodTypeSonName(goodType2.getName());
		goodTypeDto.setGoodTypeGrandsonId(goodTypeId2);
		return goodTypeDto;
	}

	@Override
	public List<Map<String, String>> getGoodTypeListNameAndParentId(String name, String parentId,String sysUserId) {
		return goodStoreTypeMapper.selectGoodTypeByParentIdAndName(name,parentId,sysUserId);
	}

	@Override
	public List<Map<String, Object>> findGoodTypeBySysUserId(String sysUserId) {
		return goodStoreTypeMapper.findGoodTypeBySysUserId(sysUserId);
	}

	@Override
	public List<Map<String, Object>> findGoodTypeByParentId(String parentId) {
		return goodStoreTypeMapper.findGoodTypeByParentId(parentId);
	}

	public List<NodeVo> getChildrenNodes(String parentId){
		List<NodeVo> childList = new ArrayList<NodeVo>();
		List<GoodStoreType> goodTypes=goodStoreTypeMapper.selectGoodTypeByParentId(parentId);
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
	/***
	 * 热门推荐分类查询
	 * @param limit
	 * @return
	 */
	@Override
	public List<Map<String,Object>> getHotRecommended (String sysUserId,Integer limit){
		Map<String,Object> mapList= new HashMap<>();
		List<String> strList =new ArrayList<>() ;
		return  goodStoreTypeMapper.getHotRecommended(sysUserId,limit);
	};

	/***
	 * 人气推荐分类查询
	 * @param limit
	 * @return
	 */
	@Override
	public List<Map<String,Object>> getPopularityRecommended(String sysUserId,Integer limit){
		Map<String,Object> mapList= new HashMap<>();
		List<String> strList =new ArrayList<>() ;
		List<Map<String,Object>> list = goodStoreTypeMapper.getHotRecommended(sysUserId,limit);
		if(list.size()>0){
			for( Map<String,Object> mapList1:list){
				strList.add(mapList1.get("id").toString());
			}
			return goodStoreTypeMapper.getHotRecommendedAVG(sysUserId,strList,list.size());
		}

		return list;
	};
	@Override
	public List<Map<String,Object>> getHotRecommendedAVG(String sysUserId,List<String> goodTypeIds,Integer limit){
		return goodStoreTypeMapper.getHotRecommendedAVG(sysUserId,goodTypeIds,limit);
	};

    /**
     * 人气排行根据购买分类内商品数量排行
     * @param limit
     * @return
     */
    @Override
   public List<Map<String,Object>> getGoodStoreTypeNumberDesc(String sysUserId,Integer limit){
       List<Map<String,Object>> list =    goodStoreTypeMapper.getGoodStoreTypeNumberDesc(sysUserId,limit);

       return list;
   };

    /**
     * 根据用户评分排行
     * @param goodTypeIds
     * @param limit
     * @return
     */
    @Override
    public List<Map<String,Object>>  getGoodStoreTypeAvgDesc(List<String> goodTypeIds,Integer limit){
            return  goodStoreTypeMapper.getGoodStoreTypeAvgDesc(goodTypeIds,limit);
    }


    /**
     * 人气排行根据购买分类内商品数量后根据评分排行
     * @param limit
     * @return
     */
    @Override
    public List<Map<String,Object>> getGoodStoreTypeNumberAndAvgDesc(String sysUserId,Integer limit){
        List<Map<String,Object>> list =    goodStoreTypeMapper.getGoodStoreTypeNumberDesc(sysUserId,limit);

        if(list.size()>0){
            List<String> stringList=new ArrayList<>() ;
            for(Map<String,Object> map :list){
                stringList.add(map.get("id").toString());
            }
            List<Map<String,Object>> goodTypeList =   goodStoreTypeMapper.getGoodStoreTypeAvgDesc(stringList,limit);
            return goodTypeList;
        }
        return list;
    }
}
