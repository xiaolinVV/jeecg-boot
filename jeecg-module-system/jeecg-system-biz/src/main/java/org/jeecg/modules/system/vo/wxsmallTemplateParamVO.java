package org.jeecg.modules.system.vo;

import lombok.Data;

@Data
public class wxsmallTemplateParamVO {
    // 参数名称
    private String name;
    // 参数值
    private String value;
    // 颜色  废弃了哎。。。。。。。。。。。。。。。。。。。
    private String color;
    public wxsmallTemplateParamVO(String name, String value, String color) {
        this.name = name;
        this.value = value;
        this.color = color;
    }

    public wxsmallTemplateParamVO(String name, String value) {
        this.name = name;
        this.value = value;

    }

}
