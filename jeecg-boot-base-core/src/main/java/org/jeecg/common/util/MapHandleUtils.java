package org.jeecg.common.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MapHandleUtils {

    /**
     * 处理请求数据
     *
     * @param map
     * @return
     */
   public static Map<String, Object> handleRequestMap(Map<String, String[]> map) {
        Map<String, Object> returnMap = new HashMap<String, Object>();
        Iterator entries = map.entrySet().iterator();
        Map.Entry entry;
        String name = "";
        String value = null;
        while (entries.hasNext()) {
            entry = (Map.Entry) entries.next();
            name = (String) entry.getKey();
            Object objvalue = entry.getValue();
            if (objvalue == null) {
                value = null;
            } else if (objvalue instanceof String[]) {
                String[] values = (String[]) objvalue;
                for (int i = 0; i < values.length; i++) {
                    value = values[i] + ",";
                }
                value = value.substring(0, value.length() - 1);
            } else {
                value = objvalue.toString();
            }
            returnMap.put(name, value);
        }
        Iterator it = returnMap.keySet().iterator();
        while (it.hasNext()) {
            Object key = it.next();
            if (returnMap.get(key) == null || "".equals(((String) returnMap.get(key)).trim())) {
                returnMap.put((String) key, null);
            }
        }
        return returnMap;
    }

}
