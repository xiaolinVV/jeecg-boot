package org.jeecg.modules.marketing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;

/**
 * @Description: 直播管理
 * @Author: jeecg-boot
 * @Date:   2021-04-24
 * @Version: V1.0
 */
@Data
@TableName("marketing_live_streaming")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="marketing_live_streaming对象", description="直播管理")
public class MarketingLiveStreaming {
    
	/**主键ID*/
	@TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "主键ID")
	private String id;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
    @ApiModelProperty(value = "创建人")
	private String createBy;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
	private java.util.Date createTime;
	/**修改人*/
	@Excel(name = "修改人", width = 15)
    @ApiModelProperty(value = "修改人")
	private String updateBy;
	/**修改时间*/
	@Excel(name = "修改时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
	private java.util.Date updateTime;
	/**创建年*/
	@Excel(name = "创建年", width = 15)
    @ApiModelProperty(value = "创建年")
	private Integer year;
	/**创建月*/
	@Excel(name = "创建月", width = 15)
    @ApiModelProperty(value = "创建月")
	private Integer month;
	/**创建日*/
	@Excel(name = "创建日", width = 15)
    @ApiModelProperty(value = "创建日")
	private Integer day;
	/**删除状态（0，正常，1已删除）*/
	@Excel(name = "删除状态（0，正常，1已删除）", width = 15)
    @ApiModelProperty(value = "删除状态（0，正常，1已删除）")
	@TableLogic
	private String delFlag;
	/**直播间名称*/
	@Excel(name = "直播间名称", width = 15)
    @ApiModelProperty(value = "直播间名称")
	private String roomName;
	/**直播流编号*/
	@Excel(name = "直播流编号", width = 15)
    @ApiModelProperty(value = "直播流编号")
	private String streamNumber;
	/**预告时间*/
	@Excel(name = "预告时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "预告时间")
	private java.util.Date noticeTime;
	/**开播时间*/
	@Excel(name = "开播时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "开播时间")
	private java.util.Date startTime;
	/**结束时间*/
	@Excel(name = "结束时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "结束时间")
	private java.util.Date endTime;
	/**封面图*/
	@Excel(name = "封面图", width = 15)
    @ApiModelProperty(value = "封面图")
	private String surfacePlot;
	/**海报图*/
	@Excel(name = "海报图", width = 15)
    @ApiModelProperty(value = "海报图")
	private String posters;
	/**背景墙*/
	@Excel(name = "背景墙", width = 15)
    @ApiModelProperty(value = "背景墙")
	private String wallPanel;
	/**主播名字*/
	@Excel(name = "主播名字", width = 15)
    @ApiModelProperty(value = "主播名字")
	private String hostName;
	/**主播头像*/
	@Excel(name = "主播头像", width = 15)
    @ApiModelProperty(value = "主播头像")
	private String headPortrait;
	/**排序*/
	@Excel(name = "排序", width = 15)
    @ApiModelProperty(value = "排序")
	private BigDecimal sort;
	/**推流地址*/
	@Excel(name = "推流地址", width = 15)
    @ApiModelProperty(value = "推流地址")
	private String pushStreamAddress;
	/**OBS推流地址*/
	@Excel(name = "OBS推流地址", width = 15)
    @ApiModelProperty(value = "OBS推流地址")
	private String obsStreamAddress;
	/**OBS推流名称*/
	@Excel(name = "OBS推流名称", width = 15)
    @ApiModelProperty(value = "OBS推流名称")
	private String obsStreamName;
	/**过期时间流过期时间*/
	@Excel(name = "过期时间流过期时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "过期时间流过期时间")
	private java.util.Date streamEndTime;
	/**RTMP播放地址*/
	@Excel(name = "RTMP播放地址", width = 15)
    @ApiModelProperty(value = "RTMP播放地址")
	private String rtmpBroadcastAddress;
	/**FLV播放地址*/
	@Excel(name = "FLV播放地址", width = 15)
    @ApiModelProperty(value = "FLV播放地址")
	private String flvBroadcastAddress;
	/**HLS播放地址*/
	@Excel(name = "HLS播放地址", width = 15)
    @ApiModelProperty(value = "HLS播放地址")
	private String hlsBroadcastAddress;
	/**UDP播放地址*/
	@Excel(name = "UDP播放地址", width = 15)
    @ApiModelProperty(value = "UDP播放地址")
	private String udpBroadcastAddress;

	/**
	 * 状态；0：预告中；1：进行中；2：结束
	 */
	private String status;

	/**
	 * 在线人数
	 */
	private BigDecimal onlineNumber;
	/**
	 * 虚拟在线人数
	 */
	private BigDecimal virtualOnlineNumber;

	/**
	 * 点赞数
	 */
	private BigDecimal thumb;

	/**
	 * 直播群id
	 */
	private String groupId;
}
