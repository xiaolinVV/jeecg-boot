package org.jeecg.modules.good.dto;

import lombok.Data;

import javax.validation.Valid;

@Data
@Valid
public class SpecificationsPicturesDTO {
    private String name;
    private String  url;
}
