package org.jeecg.modules.system.test;

import org.jeecg.JeecgSystemApplication;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.util.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * jwt 单元测试
 *
 * @author 张少林
 * @date 2022年06月24日 4:04 下午
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = JeecgSystemApplication.class)
@SuppressWarnings({"FieldCanBeLocal", "SpringJavaAutowiredMembersInspection"})
public class JwtTest {

    //测试：用户名和密码
    private final String USERNAME = "admin";
    private final String PASSWORD = "123456";
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 测试用例：生成token
     */
    @Test
    public void sign() {
        String token = JwtUtil.sign(USERNAME, PASSWORD);
        redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, token);
        redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, JwtUtil.EXPIRE_TIME * 2 / 1000);
        System.out.println("token：" + token);
    }
}
