package org.jeecg.modules.store.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.UUID;

@Component
@Slf4j
public class StoreImgUtils {

    /**
     * 通过文件路径下载文件
     * @param response
     * @param filePath 文件路径
     * @throws Exception
     */
    public static void download(HttpServletResponse response , String filePath ) throws Exception {
        // 判断文件是否存在
        File file = new File(filePath);
        log.info("下载路径：" + filePath);
        if (!file.exists() || file.isDirectory()) {
            log.info("文件不存在");
            return;
        }
        // 自定义文件名
        String filename = file.getAbsolutePath();
        String suffix = filename.substring(filename.lastIndexOf("."));
        //根据UUID重命名文件
        String attachmentName = UUID.randomUUID().toString().substring(0,30) + suffix;
        response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(attachmentName, "UTF-8"));
        InputStream in = null;
        BufferedInputStream bis = null;
        OutputStream out = null;
        BufferedOutputStream bos = null;
        try {
            in = new FileInputStream(filePath);
            bis = new BufferedInputStream(in);
            byte[] data = new byte[1024];
            int bytes = 0;
            out = response.getOutputStream();
            bos = new BufferedOutputStream(out);
            while ((bytes = bis.read(data, 0, data.length)) != -1) {
                bos.write(data, 0, bytes);
            }
            bos.flush();
        } catch (Exception e) {
            log.error("文件下载异常，异常信息：" + e.getMessage(), e);
            throw new Exception("文件下载异常");
        } finally {
            try {
                if (bos != null) {
                    bos.close();
                }
                if (out != null) {
                    out.close();
                }
                if (bis != null) {
                    bis.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}