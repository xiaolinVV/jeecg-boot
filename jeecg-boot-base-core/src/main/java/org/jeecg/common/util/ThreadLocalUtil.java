package org.jeecg.common.util;

import cn.hutool.core.thread.ThreadUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 张少林
 * @desc ThreadLocal 工具类
 * @date 2022年11月30日 9:42 下午
 */
public class ThreadLocalUtil {

    /**
     * 定义 ThreadLocal
     */
    private static final ThreadLocal<ConcurrentHashMap<String, Object>> threadLocal = ThreadUtil.createThreadLocal(false);


    /**
     * 获取 ThreadLocal 值
     *
     * @return
     */
    public static ConcurrentHashMap<String, Object> getThreadLocal() {
        ConcurrentHashMap<String, Object> map = threadLocal.get();
        if (map == null) {
            map = new ConcurrentHashMap<>();
            threadLocal.set(map);
        }
        return map;
    }

    /**
     * 获取 ThreadLocal 中 Map 中对应 key 的值
     *
     * @param key
     * @param <T>
     * @return
     */
    public static <T> T get(String key) {
        return get(key, null);
    }

    /**
     * 获取 ThreadLocal 中 Map 中对应 key 的值
     *
     * @param key
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T get(String key, T defaultValue) {
        Map<String, Object> map = getThreadLocal();
        return (T) map.getOrDefault(key, defaultValue);
    }

    /**
     * 设置 ThreadLocal 中 Map 中对应 key 的值
     *
     * @param key
     * @return
     */
    public static void put(String key, Object value) {
        ConcurrentHashMap<String, Object> map = getThreadLocal();
        if (value != null) {
            map.put(key, value);
            threadLocal.set(map);
        }
    }

    /**
     * 设置 ThreadLocal 中 Map 中对应 key 的值
     *
     * @return
     */
    public static void put(Map<String, Object> keyValueMap) {
        ConcurrentHashMap<String, Object> map = getThreadLocal();
        map.putAll(keyValueMap);
        threadLocal.set(map);
    }

    /**
     * 清空 ThreadLocal
     */
    public static void remove() {
        threadLocal.remove();
    }

    /**
     * 移除 ThreadLocal 中 Map 中对应 key 的值
     *
     * @param key
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T remove(String key) {
        ConcurrentHashMap<String, Object> map = getThreadLocal();
        return (T) map.remove(key);
    }
}
