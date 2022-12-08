package com.alibaba.logistics.param;

import com.alibaba.ocean.rawsdk.client.APIId;
import com.alibaba.ocean.rawsdk.common.AbstractAPIRequest;

public class AlibabaLogisticsOpQueryLogisticCompanyListParam extends AbstractAPIRequest<AlibabaLogisticsOpQueryLogisticCompanyListResult> {

    public AlibabaLogisticsOpQueryLogisticCompanyListParam() {
        super();
        oceanApiId = new APIId("com.alibaba.logistics", "alibaba.logistics.OpQueryLogisticCompanyList", 1);
    }

}
