package org.jeecg.modules.mobileIMSDKServer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author 张少林
 * @date 2022年09月20日 1:56 下午
 */
@Slf4j
@Component
public class ChatServerRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        log.info("================= ↓↓↓↓↓↓ 启动MobileIMSDK服务端 ↓↓↓↓↓↓ =================");

        // 实例化后记得startup哦，单独startup()的目的是让调用者可以延迟决定何时真正启动IM服务
        final ServerLauncherImpl sli = new ServerLauncherImpl();

        // 启动MobileIMSDK服务端的Demo
        sli.startup();

        // 加一个钩子，确保在JVM退出时释放netty的资源
        Runtime.getRuntime().addShutdownHook(new Thread(sli::shutdown));
    }
}
