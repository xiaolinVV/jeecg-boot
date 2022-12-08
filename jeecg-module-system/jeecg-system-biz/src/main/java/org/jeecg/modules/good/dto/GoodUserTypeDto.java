package org.jeecg.modules.good.dto;

import lombok.Data;

import java.util.List;
@Data
public class GoodUserTypeDto {
    private String id;
    private String name;
    public List<GoodUserTypeDto> listGoodUserTypeDto;
}
