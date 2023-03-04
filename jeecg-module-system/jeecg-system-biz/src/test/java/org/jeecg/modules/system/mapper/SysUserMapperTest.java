package org.jeecg.modules.system.mapper;

import com.baomidou.mybatisplus.core.MybatisSqlSessionFactoryBuilder;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author 张少林
 * @date 2023年01月10日 9:37 上午
 */
public class SysUserMapperTest {
    private static SysUserMapper mapper;

    /**
     * 在生成的 testcase 中有一个 setUp 方法，将 SqlSessionFactoryBuilder 改成 MybatisSqlSessionFactoryBuilder 即可测试 mybatisplus 自带的一些方法
     */
    @BeforeClass
    public static void setUpMybatisDatabase() {
        SqlSessionFactory builder = new MybatisSqlSessionFactoryBuilder().build(SysUserMapperTest.class.getClassLoader().getResourceAsStream("mybatisTestConfiguration/SysUserMapperTestConfiguration.xml"));
        //mybatisplus 添加分页插件和乐观锁插件
        final MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        builder.getConfiguration().addInterceptor(interceptor);
        //mybatisplus 添加分页插件和乐观锁插件
        //you can use builder.openSession(false) to not commit to database
        mapper = builder.getConfiguration().getMapper(SysUserMapper.class, builder.openSession(true));
    }

    @Test
    public void testGetUserByName() {
        mapper.getUserByName("admin");
    }

    @Test
    public void testSelectList() {
        mapper.selectList(new QueryWrapper<>());
    }
}
