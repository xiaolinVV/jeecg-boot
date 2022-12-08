package org.jeecg.modules.marketing.dto;


import lombok.Data;

import java.util.Date;

@Data
public class MarketingStoreGiftCardListDTO {
    private String serialNumber;

    private String storeName;

    private String carName;

    private String status;

    private Date createTimeStart;

    private Date createTimeEnd;
}
