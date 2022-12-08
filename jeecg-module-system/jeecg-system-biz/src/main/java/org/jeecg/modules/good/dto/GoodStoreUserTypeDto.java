package org.jeecg.modules.good.dto;

import lombok.Data;

import java.util.List;

@Data
public class GoodStoreUserTypeDto {
    private String id;
    private String name;
    public List<GoodStoreUserTypeDto> listGoodStoreUserTypeDto;
}
