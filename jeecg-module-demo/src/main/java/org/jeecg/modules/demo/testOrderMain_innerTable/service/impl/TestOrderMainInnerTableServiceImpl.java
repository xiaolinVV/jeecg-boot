package org.jeecg.modules.demo.testOrderMain_innerTable.service.impl;

import org.jeecg.modules.demo.testOrderMain_innerTable.entity.TestOrderMainInnerTable;
import org.jeecg.modules.demo.testOrderMain_innerTable.entity.TestOrderProductInnerTable;
import org.jeecg.modules.demo.testOrderMain_innerTable.mapper.TestOrderProductInnerTableMapper;
import org.jeecg.modules.demo.testOrderMain_innerTable.mapper.TestOrderMainInnerTableMapper;
import org.jeecg.modules.demo.testOrderMain_innerTable.service.ITestOrderMainInnerTableService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 测试订单主表-内嵌子表风格
 * @Author: jeecg-boot
 * @Date:   2022-11-16
 * @Version: V1.0
 */
@Service
public class TestOrderMainInnerTableServiceImpl extends ServiceImpl<TestOrderMainInnerTableMapper, TestOrderMainInnerTable> implements ITestOrderMainInnerTableService {

	@Autowired
	private TestOrderMainInnerTableMapper testOrderMainInnerTableMapper;
	@Autowired
	private TestOrderProductInnerTableMapper testOrderProductInnerTableMapper;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(TestOrderMainInnerTable testOrderMainInnerTable, List<TestOrderProductInnerTable> testOrderProductInnerTableList) {
		testOrderMainInnerTableMapper.insert(testOrderMainInnerTable);
		if(testOrderProductInnerTableList!=null && testOrderProductInnerTableList.size()>0) {
			for(TestOrderProductInnerTable entity:testOrderProductInnerTableList) {
				//外键设置
				entity.setOrderFkId(testOrderMainInnerTable.getId());
				testOrderProductInnerTableMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(TestOrderMainInnerTable testOrderMainInnerTable,List<TestOrderProductInnerTable> testOrderProductInnerTableList) {
		testOrderMainInnerTableMapper.updateById(testOrderMainInnerTable);
		
		//1.先删除子表数据
		testOrderProductInnerTableMapper.deleteByMainId(testOrderMainInnerTable.getId());
		
		//2.子表数据重新插入
		if(testOrderProductInnerTableList!=null && testOrderProductInnerTableList.size()>0) {
			for(TestOrderProductInnerTable entity:testOrderProductInnerTableList) {
				//外键设置
				entity.setOrderFkId(testOrderMainInnerTable.getId());
				testOrderProductInnerTableMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		testOrderProductInnerTableMapper.deleteByMainId(id);
		testOrderMainInnerTableMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			testOrderProductInnerTableMapper.deleteByMainId(id.toString());
			testOrderMainInnerTableMapper.deleteById(id);
		}
	}
	
}
