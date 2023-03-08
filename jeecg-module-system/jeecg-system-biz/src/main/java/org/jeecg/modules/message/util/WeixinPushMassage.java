package org.jeecg.modules.message.util;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.jeecg.common.util.gongke.HttpClientUtil;
import org.jeecg.config.jwt.service.TokenManager;
import org.jeecg.modules.system.service.ISysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 微信推送信息
 * 
 * @author My
 * @CreateDate 2016-1-19
 * @param
 */
@Component
@Slf4j
public class WeixinPushMassage {

	@Autowired
	private CommonUtil commonUtil;
	@Autowired
	private ISysDictService iSysDictService;
	@Autowired
	private TokenManager tokenManager;
	@Autowired
	private HttpClientUtil httpClientUtil;

	//测试订单消息发送
	public Boolean tixiantz(String openid) {

		String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";
		//获取常量信息
		String appid = "wxce76e2251b91625b";
		String appSecret = "c7d2fa38b5f7ddd86b69ff1f0c5e6f54";

		//获取token凭证。
		String  accessToken = tokenManager.createWeiXinToken( appid, appSecret);
		url = url.replace("ACCESS_TOKEN", accessToken);// 转换为响应接口模式

		// 封装数据
		JSONObject json = new JSONObject();
		json.put("touser", openid);// 接收者wxName
		//json.put("template_id","mxx3tsG4vM3XQ2Yv8kfb3bfiJ-ZZmU_TCiR40FN7mxs");// 消息模板
		json.put("template_id","hqTPUTPFJZo6haaHvFp176yZz4k-lOZ9S5cP0Rzd5T8");// 消息模板
		
		json.put("url", "http://www.baidu.com");//填写url可查看详情

		JSONObject dd = new JSONObject();

		JSONObject dd2 = new JSONObject();
		dd2.put("value", "测试模板消息!");// 消息提示
		dd2.put("color", "red");
		dd.put("first", dd2);

		JSONObject cc2 = new JSONObject();
		cc2.put("value", "测试用");// 订单状态
		cc2.put("color", "#173177");
		dd.put("keyword1", cc2);

		JSONObject ee2 = new JSONObject();
		ee2.put("value", "测试1");
		ee2.put("color", "#173177");
		dd.put("keyword2", ee2);

		JSONObject ff2 = new JSONObject();
		ff2.put("value", new Date());// 时间
		ff2.put("color", "#173177");
		dd.put("keyword3", ff2);
		
		JSONObject gg2 = new JSONObject();
		gg2.put("value", "测试2");
	    gg2.put("color", "#173177");
		dd.put("remark", gg2);

		json.put("data", dd);
		System.out.println(json.toString());
		JSONObject jsonResultStr = CommonUtil.httpsRequest(url, "POST", json.toString());
		//System.out.println("js==" + jsonResultStr);
		JSONObject jsonResult = JSONObject.fromObject(jsonResultStr);
		Boolean flag = false ;
		if (jsonResult != null) {
			Integer errorCode = jsonResult.getInt("errcode");
			String errorMessage = jsonResult.getString("errmsg");
			if (errorCode == 0) {
				flag = true;
			} else {
				System.out.println("模板消息发送失败:" + errorCode + "," + errorMessage);
				flag = false;
			}
		}
		return flag;
	}
	
	


	/**
	 * 请求token
	 *
	 * @Description :
	 * @param
	 * @return ---------------
	 * @Author : My
	 * @CreateData : 2016-1-18
	 */
	/*public  Token headtoken(String appId, String appSrecet) {
		Token token = new Token();
		token = commonUtil.getToken(appId, appSrecet);
		return token;
	}*/
}
