package org.jeecg.common.aspect.annotation;

import java.lang.annotation.*;

/**
 * 限制重复提交注解
 *
 * @author: zhangshaolin
 * @date: date()
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface LimitSubmit {
    String key();

    /**
     * 默认 10s
     */
    int limit() default 10;

    /**
     * 请求完成后 是否一直等待
     * true则等待
     *
     * @return
     */
    boolean needAllWait() default true;

}
