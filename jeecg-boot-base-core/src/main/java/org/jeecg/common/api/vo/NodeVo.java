package org.jeecg.common.api.vo;

import lombok.Data;

import java.util.List;

@Data
public class NodeVo {
  private String id;
  private String name;
  private String parentId;
  public List<NodeVo> childrenNode;

}
