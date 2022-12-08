/**
 * Project: ocean.client.java.basic
 *
 * File Created at 2011-10-27
 * $Id: Json2HttpResponseParser.java 311300 2013-12-23 06:15:28Z yichun.wangyc $
 *
 * Copyright 2008 Alibaba.com Croporation Limited.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * Alibaba Company. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Alibaba.com.
 */
package com.alibaba.ocean.rawsdk.client.imp.serialize;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.ocean.rawsdk.client.entity.ResponseWrapper;
import com.alibaba.ocean.rawsdk.client.policy.Protocol;
import com.alibaba.ocean.rawsdk.client.serialize.DeSerializerListener;
import com.alibaba.ocean.rawsdk.client.util.ExceptionParser;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 
 * @author hongbang.hb
 *
 */
public class Json2Deserializer extends AbstractJsonDeserializer {

	private Map<Class<? extends DeSerializerListener>, DeSerializerListener> listnerList = new LinkedHashMap<Class<? extends DeSerializerListener>, DeSerializerListener>();

	public String supportedContentType() {
		return Protocol.json2.name();
	}

	@Override
	public <T> ResponseWrapper<T> deSerialize(String content, Class<T> resultType) {
		for (DeSerializerListener deSerializerListener : listnerList.values()) {
			deSerializerListener.onResponseDeSerialized(content);
		}

		//更换日期转换为开放平台的日期类型反序列化类
		ParserConfig config=ParserConfig.getGlobalInstance();
		config.putDeserializer(java.util.Date.class, OpenPlatformDateCodec.instance);
		T result = JSON.parseObject(content,resultType,config,null,JSON.DEFAULT_PARSER_FEATURE);

		//JSONObject json = JSON.parseObject(content);
		//T result = this.parseResult(json, resultType);
		ResponseWrapper<T> responseWrapper = new ResponseWrapper<T>();
		responseWrapper.setResult(result);
		return responseWrapper;
	}

	@Override
	public Throwable buildException(String content, int statusCode) {
		for (DeSerializerListener deSerializerListener : listnerList.values()) {
			deSerializerListener.onResponseExceptioned(content);
		}
		Map result = JSON.parseObject(content, Map.class);
		return ExceptionParser.buildException4Json2(result);
	}

	public void registeDeSerializerListener(DeSerializerListener listner) {
		if (!listnerList.containsKey(listner.getClass())) {
			listnerList.put(listner.getClass(), listner);
		}
	}

}
