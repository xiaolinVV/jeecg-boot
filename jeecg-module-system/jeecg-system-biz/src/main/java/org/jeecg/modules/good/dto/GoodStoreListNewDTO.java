package org.jeecg.modules.good.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GoodStoreListNewDTO {
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
    private String storeManageId;
    private String storeTemplateId;
    private BigDecimal marketPrice;
    private String frameStatus;
}
