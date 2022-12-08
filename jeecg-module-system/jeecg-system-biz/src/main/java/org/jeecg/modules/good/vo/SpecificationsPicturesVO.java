package org.jeecg.modules.good.vo;

import lombok.Data;

import javax.validation.Valid;

@Data
@Valid
public class SpecificationsPicturesVO {
    private String name;
    private String  url;
}
