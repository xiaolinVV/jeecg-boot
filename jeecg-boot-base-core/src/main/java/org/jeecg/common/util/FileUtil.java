package org.jeecg.common.util;


import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLDecoder;

/**
 * @author zhouhy
 * @date 2017/8/9.
 */
public class FileUtil {


    /**
     * 文件下载
     *
     * @param response
     * @param filePath 文件从盘符开始的完整路径
     */
    public static void responseFileStream(HttpServletResponse response, String filePath) {

        if (filePath.contains("%")) {
            try {
                filePath = URLDecoder.decode(filePath, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        if (filePath.contains("%")) {
            try {
                filePath = URLDecoder.decode(filePath, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        ServletOutputStream out = null;
        FileInputStream in = null;
        try {
            in = new FileInputStream(new File(filePath));
            String[] dir = filePath.split("/");
            String fileName = dir[dir.length - 1];
            String[] array = fileName.split("[.]");
            String fileType = array[array.length - 1].toLowerCase();
            //设置文件ContentType类型
            if ("jpg,jepg,gif,png".contains(fileType)) {//图片类型
                response.setContentType("image/" + fileType);
            } else if ("pdf".contains(fileType)) {//pdf类型
                response.setContentType("application/pdf");
            } else {//自动判断下载文件类型
                response.setContentType("multipart/form-data");
            }
            //设置文件头：最后一个参数是设置下载文件名
            //response.setHeader("Content-Disposition", "attachment;fileName="+fileName);
            out = response.getOutputStream();
            // 读取文件流
            int len = 0;
            byte[] buffer = new byte[1024 * 10];
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
                in.close();
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 文件下载
     *
     * @param response
     * @param filePath 文件从盘符开始的完整路径
     */
    @SuppressWarnings("all")
    public static void responseFileStream(HttpServletResponse response, InputStream inputStream, String fileType) {

        ServletOutputStream out = null;
        try {
            //设置文件ContentType类型
            if ("jpg,jepg,gif,png".contains(fileType)) {//图片类型
                response.setContentType("image/" + fileType);
            } else if ("pdf".contains(fileType)) {//pdf类型
                response.setContentType("application/pdf");
            } else if ("mp4".contains(fileType)) {
                //response.setContentType("application/octet-stream");
                response.setHeader("Content-Type", "video/mp4");
                //response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
            } else {//自动判断下载文件类型
                response.setContentType("multipart/form-data");
            }
            //设置文件头：最后一个参数是设置下载文件名
            //response.setHeader("Content-Disposition", "attachment;fileName="+fileName);
            out = response.getOutputStream();
            // 读取文件流
            int len = 0;
            byte[] buffer = new byte[1024 * 10];
            while ((len = inputStream.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
                inputStream.close();
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static byte[] getBytes(String path) {
        return getBytes(new File(path));
    }

    public static byte[] getBytes(File file) {
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        ByteArrayOutputStream bos = null;
        try {
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            bos = new ByteArrayOutputStream(1000);
            byte[] buffer = new byte[1000];
            int count;
            while ((count = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, count);

            }
            return bos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (bos != null) {
                    bos.flush();
                    bos.close();
                }
                if (bis != null) {
                    bis.close();
                }
                if (fis != null) {
                    fis.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void displayFile(String filePath, HttpServletResponse response) {
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            File file = new File(filePath);
            byte[] bytes = getBytes(file);
            response.setContentLength(bytes.length);
            os.write(bytes, 0, bytes.length);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.flush();
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 指定图片宽度和高度和压缩比例对图片进行压缩
     *
     * @param imgsrc     源图片地址
     * @param imgdist    目标图片地址
     * @param widthdist  压缩后图片的宽度
     * @param heightdist 压缩后图片的高度
     * @param rate       压缩的比例
     */
    public static void reduceImg(String imgsrc, String imgdist, int widthdist, int heightdist, Float rate) {
        try {
            File srcfile = new File(imgsrc);
            // 检查图片文件是否存在
            if (!srcfile.exists()) {
                System.out.println("文件不存在");
            }
            // 如果比例不为空则说明是按比例压缩
            if (rate != null && rate > 0) {
                //获得源图片的宽高存入数组中
                int[] results = getImgWidthHeight(srcfile);
                if (results == null || results[0] == 0 || results[1] == 0) {
                    return;
                } else {
                    //按比例缩放或扩大图片大小，将浮点型转为整型
                    widthdist = (int) (results[0] * rate);
                    heightdist = (int) (results[1] * rate);
                }
            }
            // 开始读取文件并进行压缩
            Image src = ImageIO.read(srcfile);

            // 构造一个类型为预定义图像类型之一的 BufferedImage
            BufferedImage tag = new BufferedImage((int) widthdist, (int) heightdist, BufferedImage.TYPE_INT_RGB);

            //绘制图像  getScaledInstance表示创建此图像的缩放版本，返回一个新的缩放版本Image,按指定的width,height呈现图像
            //Image.SCALE_SMOOTH,选择图像平滑度比缩放速度具有更高优先级的图像缩放算法。
            tag.getGraphics().drawImage(src.getScaledInstance(widthdist, heightdist, Image.SCALE_SMOOTH), 0, 0, null);

            //创建文件输出流
//            FileOutputStream out = new FileOutputStream(imgdist);
//            //将图片按JPEG压缩，保存到out中
//            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
//            encoder.encode(tag);
//            //关闭文件输出流
//            out.close();


            String formatName = imgdist.substring(imgdist.lastIndexOf(".") + 1);
            ImageIO.write(tag, /*"GIF"*/ formatName /* format desired */, new File(imgdist) /* target */);
        } catch (Exception ef) {
            ef.printStackTrace();
        }
    }

    /**
     * 获取图片宽度和高度
     *
     * @return 返回图片的宽度
     */
    public static int[] getImgWidthHeight(File file) {
        InputStream is = null;
        BufferedImage src = null;
        int result[] = {0, 0};
        try {
            // 获得文件输入流
            is = new FileInputStream(file);
            // 从流里将图片写入缓冲图片区
            src = ImageIO.read(is);
            result[0] = src.getWidth(null); // 得到源图片宽
            result[1] = src.getHeight(null);// 得到源图片高
            is.close();  //关闭输入流
        } catch (Exception ef) {
            ef.printStackTrace();
        }

        return result;
    }

    public static void main(String[] args) {

        File srcfile = new File("C:\\Users\\12257\\Desktop\\未来派城市 日落 科幻 概念艺术4k壁纸_彼岸图网.jpg");

        File distfile = new File("C:\\Users\\12257\\Desktop\\新建文本文档 (3).jpg");

        System.out.println("压缩前图片大小：" + srcfile.length());
        reduceImg("C:\\Users\\12257\\Desktop\\未来派城市 日落 科幻 概念艺术4k壁纸_彼岸图网.jpg", "C:\\Users\\12257\\Desktop\\新建文本文档 (3).jpg", 200, 200, null);
        System.out.println("压缩后图片大小：" + distfile.length());

    }
}






