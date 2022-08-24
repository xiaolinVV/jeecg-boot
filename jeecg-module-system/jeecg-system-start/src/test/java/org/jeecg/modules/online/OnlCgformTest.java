package org.jeecg.modules.online;

import org.jeecg.JeecgSystemApplication;
import org.jeecg.modules.online.cgform.mapper.OnlCgformFieldMapper;
import org.jeecg.modules.online.cgform.service.IOnlCgformFieldService;
import org.jeecg.modules.online.cgform.service.IOnlCgformHeadService;
import org.jeecg.modules.online.config.exception.a;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

/**
 * @author 张少林
 * @date 2022年08月22日 11:49 上午
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = JeecgSystemApplication.class)
@SuppressWarnings({"FieldCanBeLocal", "SpringJavaAutowiredMembersInspection"})
public class OnlCgformTest {

    @Autowired
    public IOnlCgformFieldService onlCgformFieldService;

    @Autowired
    public IOnlCgformHeadService onlCgformHeadService;

    @Autowired
    public OnlCgformFieldMapper onlCgformFieldMapper;

    /**
     * 根据 onl_cgform_head 表的id + 数据表的 id 获取数据表的数据详情字段，主要是接口 `/online/cgform/api/form/{code}/{id}` 调用
     * <p>
     * 核心逻辑是根据 onlCgformHeadId 查询 onl_cgform_head 表中设置的 table_name 。 再获取 onl_cgform_field 表中配置的字段信息列表，。获取需要查询的字段。
     * 再动态组装查询 SQL 查询出数据
     *
     * @throws a
     */
    @Test
    public void testQueryManyFormData() throws a {
        String onlCgformHeadId = "4028818582be3d6a0182be4d9b270001";
        String dataId = "1561276513470881793";
        Map<String, Object> formData = onlCgformHeadService.queryManyFormData(onlCgformHeadId, dataId);
        System.out.println(formData);
    }
}
