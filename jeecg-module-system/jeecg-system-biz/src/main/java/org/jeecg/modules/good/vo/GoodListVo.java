package org.jeecg.modules.good.vo;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import org.jeecg.modules.good.dto.SpecificationsPicturesDTO;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Valid
public class GoodListVo {
  /**主键ID*/
  private String id;
  /**创建人*/
  private String createBy;
  /**创建时间*/
  private Date createTime;
  /**修改人*/
  private String updateBy;
  /**修改时间*/
  private Date updateTime;
  /**创建年*/
  private Integer year;
  /**创建月*/
  private Integer month;
  /**创建日*/
  private Integer day;
  /**供应链用户id*/
  private String sysUserId;
  /**删除状态（0，正常，1已删除）*/
  @TableLogic
  private String delFlag;
  /**平台商品分类id*/
 // @NotEmpty(message = "平台商品分类不能为空")
  private String goodTypeId;
  /**平台商品分类 二级id*/
  // @NotEmpty(message = "平台商品分类不能为空")
  private String goodTypeIdTwoevel;
  /**平台商品分类 三级id*/
  // @NotEmpty(message = "平台商品分类不能为空")
  private String goodTypeIdThreeLevel;
  /**商品主图相对地址（以json的形式存储多张）*/
 // @NotEmpty(message = "商品主图不能为空")
  private String mainPicture;
  /**商品名称*/
  //@NotEmpty(message = "商品名称不能为空")
  private String goodName;
  /**商品别名（默认与商品名称相同）*/
  private String nickName;
  /**商品销售价格*/
 // @NotEmpty(message = "商品销售价格不能为空")
  private String price;
  /**商品成本价*/
  //@NotEmpty(message = "商品成本价不能为空")
  private String costPrice;
  /**商品供货价*/
  //@NotEmpty(message = "商品供货价不能为空")
  private String supplyPrice;
  /**商品会员价，按照利润比例，根据数据字段设置的比例自动填入*/
  //@NotEmpty(message = "商品会员价不能为空")
  private String vipPrice;
  /**参与活动;对应数据字典字段的活动类型数据，为空代表没有参与活动*/
  private String activitiesType;
  /**商品市场价*/
  //@NotEmpty(message = "商品市场价不能为空")
  private String marketPrice;
  /**状态：0：停用；1：启用*/
  /*	@TableField("'status'")*/
  private String status;
  /**商品描述*/
  /*	@TableField("'describe'")*/
  private String goodDescribe;
  /**商品视频地址*/
  private String goodVideo;
  /**商品详情图多张以json的形式存储*/
  private String detailsGoods;
  /**库存*/
  private BigDecimal repertory;
  /**上下架；0：下架；1：上架*/
  private String frameStatus;
  /**上下架说明*/
  private String frameExplain;
  /**商品编号*/
  private String goodNo;
  /**状态说明，停用说明*/
  private String statusExplain;
  /**规格说明按照json的形式保存*/
  private String specification;
  /**有无规格；0：无规格；1：有规格*/
  private String isSpecification;
  /**运费模板id*/
  private String providerTemplateId;
  /**服务承诺，来自数据字段逗号隔开*/
  //@NotEmpty(message = "服务承诺不能为空")
  private String commitmentCustomers;
  /**审核状态：0:草稿；1：待审核；2：审核通过；3：审核不通过*/
  private String auditStatus;
  /**审核原因*/
  private String auditExplain;
  /**删除原因*/
  private String delExplain;
  /**删除时间*/
  private Date delTime;
  /**商品形态；0：常规商品；1：特价商品*/
 // @NotEmpty(message = "商品形态不能为空")
  private String goodForm;
  //@Valid
  private String goodListSpecificationVOs1;
  //@Valid
  private List<GoodListSpecificationVO> GoodListSpecificationVOs;
  /**有无销售价区间；0：无；1：有*/
  private String priceRange;
  /**查询条件开始时间*/
  private String createTime_begin;
  /**查询条件开始时间*/
  private String createTime_end;

  /**最低商品价格；*/
  private String smallPrice;
  /**最低vip价格；*/
  private String smallVipPrice;
  /**最低成本价；*/
  private String smallCostPrice;
  /**最低供货价；*/
  private String smallSupplyPrice;
  /**规格图片处理数据*/
  private String specificationsPictures;
  /**规格图片处理数据List*/
  private List<SpecificationsPicturesDTO> listSpecificationsPicturesDTO;


  /**三级分类查询*/
  private String goodTypeIdThree;
  private String goodTypeIdTwo;
  private String goodTypeIdOne;
  private String goodTypeThreeName;
  private String goodTypeTwoName;
  private String goodTypeOneName;
  private String goodTypeNames; //三级分类拼接名称 ：分类 - 分类 - 分类


  /**活动价；*/
  private String activityPrice;

  private String weight;
  /**传入Id数组*/
  private List<String> strings;
  /**供应商名称*/
  private String  realname;
  /**是否限制商品*/
  private BigDecimal astrictPriceProportion;
  /*专区选择的商品id集合*/
  private List<String> marketingPrefectureGoodIds ;
  /*专区内的商品id集合*/
  private List<String> marketingPrefectureGoodNotIds;
  /*专区id*/
  private String marketingPrefectureId;

  /*商品ids:id,id,id*/
  private String ids;
  private String skuUrl;
  /**更新版本号*/
  private BigDecimal updateVersion;
 /** 是否免审核标识*/
  private String goodAudit;
  /**折扣比例*/
  private String discount;
  /**最大折扣比例*/
  private BigDecimal maxDiscount;
  /**最小折扣比例*/
  private BigDecimal minDiscount;

  /**素材ID*/
  private String marketingMaterialListId;
  /**
   * 图片类型 1: 需拼接 2: 无需拼接
   */
  private String pictureType;

  private String salesVolume;
  private BigDecimal sort;

}
