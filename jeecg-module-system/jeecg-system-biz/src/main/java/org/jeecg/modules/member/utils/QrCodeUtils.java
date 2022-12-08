package org.jeecg.modules.member.utils;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.ijpay.core.kit.QrCodeKit;
import org.jeecg.modules.system.service.ISysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class QrCodeUtils {


    @Value(value = "${jeecg.path.upload}")
    private String uploadpath;

    @Autowired
    private ISysDictService iSysDictService;


    /**
     * 生成店铺收款码
     *
     * @param id
     * @return
     */
    public String getMoneyReceivingCode(String id){
        String ctxPath = uploadpath;
        String fileName = null;
        String bizPath = "moneyReceivingCode";
        String nowday = new SimpleDateFormat("yyyyMMdd").format(new Date());
        File file = new File(ctxPath + File.separator + bizPath + File.separator + nowday);
        if (!file.exists()) {
            file.mkdirs();// 创建文件根目录
        }

        String orgName = "moneyReceivingCode.png";// 获取文件名
        fileName = orgName.substring(0, orgName.lastIndexOf(".")) + "_" + System.currentTimeMillis() +
                orgName.substring(orgName.indexOf("."));
        String savePath = file.getPath() + File.separator + fileName;
        String sysBaseUrl= iSysDictService.queryTableDictTextByKey("sys_dict_item","item_value","item_text","sys_base_url");
        String storeCheckstandUrl= iSysDictService.queryTableDictTextByKey("sys_dict_item","item_value","item_text","store_checkstand_url");

        String moneyReceivingUrl=sysBaseUrl+storeCheckstandUrl+"?storeManageById="+id;

        Boolean encode = QrCodeKit.encode(moneyReceivingUrl, BarcodeFormat.QR_CODE, 3, ErrorCorrectionLevel.H, "png", 400,
                400,
                savePath);
        String dbpath = bizPath + File.separator + nowday + File.separator + fileName;
        return dbpath;
    }


    /**
     * 生成用户二维码
     *
     * @param id
     * @return
     */
    public String getMemberQrCode(String id){
        String ctxPath = uploadpath;
        String fileName = null;
        String bizPath = "memberQRCode";
        String nowday = new SimpleDateFormat("yyyyMMdd").format(new Date());
        File file = new File(ctxPath + File.separator + bizPath + File.separator + nowday);
        if (!file.exists()) {
            file.mkdirs();// 创建文件根目录
        }

        String orgName = "memberQRCode2.png";// 获取文件名
        fileName = orgName.substring(0, orgName.lastIndexOf(".")) + "_" + System.currentTimeMillis() +
                orgName.substring(orgName.indexOf("."));
        String savePath = file.getPath() + File.separator + fileName;

        Boolean encode = QrCodeKit.encode(id, BarcodeFormat.QR_CODE, 3, ErrorCorrectionLevel.H, "png", 400,
                400,
                savePath);
        String dbpath = bizPath + File.separator + nowday + File.separator + fileName;
        return dbpath;
    }

    public String getMemberQrWelfareCode(String id){
        String ctxPath = uploadpath;
        String fileName = null;
        String bizPath = "memberQrWelfareCode";
        String nowday = new SimpleDateFormat("yyyyMMdd").format(new Date());
        File file = new File(ctxPath + File.separator + bizPath + File.separator + nowday);
        if (!file.exists()) {
            file.mkdirs();// 创建文件根目录
        }

        String orgName = "memberQrWelfareCode2.png";// 获取文件名
        fileName = orgName.substring(0, orgName.lastIndexOf(".")) + "_" + System.currentTimeMillis() +
                orgName.substring(orgName.indexOf("."));
        String savePath = file.getPath() + File.separator + fileName;

        Boolean encode = QrCodeKit.encode(id, BarcodeFormat.QR_CODE, 3, ErrorCorrectionLevel.H, "png", 400,
                400,
                savePath);
        String dbpath = bizPath + File.separator + nowday + File.separator + fileName;
        return dbpath;
    }
}
