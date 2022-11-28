package org.jeecg.modules.message.util;

import com.deepoove.poi.XWPFTemplate;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author 张少林
 * @date 2022年11月27日 10:52 上午
 * @desc 基于 Apache POI 的 Word 模板引擎工具
 */
public class PoiTlUtil {

    // TODO: 2022/11/28 封装工具接口 @chenxin

    public static void main(String[] args) throws FileNotFoundException {
        XWPFTemplate template = XWPFTemplate.compile("/Users/zhangshaolin/Downloads/template.docx").render(
                new HashMap<String, Object>(){{
                    put("title", "Hi, poi-tl Word模板引擎");
                }});
        try {
            template.writeAndClose(new FileOutputStream("/Users/zhangshaolin/Downloads/output.docx"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
