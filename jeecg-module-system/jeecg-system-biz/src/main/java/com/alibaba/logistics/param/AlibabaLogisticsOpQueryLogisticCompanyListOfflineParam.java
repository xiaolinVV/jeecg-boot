package com.alibaba.logistics.param;

import com.alibaba.ocean.rawsdk.client.APIId;
import com.alibaba.ocean.rawsdk.common.AbstractAPIRequest;

public class AlibabaLogisticsOpQueryLogisticCompanyListOfflineParam extends AbstractAPIRequest<AlibabaLogisticsOpQueryLogisticCompanyListOfflineResult> {

    public AlibabaLogisticsOpQueryLogisticCompanyListOfflineParam() {
        super();
        oceanApiId = new APIId("com.alibaba.logistics", "alibaba.logistics.OpQueryLogisticCompanyList.offline", 1);
    }

}
