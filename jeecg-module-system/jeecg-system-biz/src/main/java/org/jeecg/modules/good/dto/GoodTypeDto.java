package org.jeecg.modules.good.dto;

import lombok.Data;

@Data
public class GoodTypeDto {
  /**
   * 一级类目名称
   */
  private String goodTypeParentName;
  /**
   * 二级类目名称
   */
  private String goodTypeSonName;
  /**
   * 三级类目名称
   */
  private String goodTypeGrandsonName;
  /**
   * 当前搜的类目id（比如当前搜的是第三级就是第三级，第二级就是第二级）
   */
  private String goodTypeGrandsonId;

}
