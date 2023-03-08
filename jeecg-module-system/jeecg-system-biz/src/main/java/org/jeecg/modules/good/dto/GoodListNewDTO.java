package org.jeecg.modules.good.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GoodListNewDTO {
    private String id;
    private String goodDescribe;
    private String detailsImages;
    private String goodTypeId;
    private String goodName;
    private String mainImages;
    private String shopInfo;
    private String specifications;
    private String specificationsDecribes;
    private String goodNo;
    private String commitmentCustomers;
    private String status;
    private String sysUserId;
    private String providerTemplateId;
    private BigDecimal marketPrice;
    private String goodBrandId;
    private String goodMachineBrandIds;
    private String goodMachineModelIds;
    private String frameStatus;
}
