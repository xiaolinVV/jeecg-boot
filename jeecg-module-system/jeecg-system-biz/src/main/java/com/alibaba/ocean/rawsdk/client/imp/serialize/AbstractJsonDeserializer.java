/**
 * 
 */
package com.alibaba.ocean.rawsdk.client.imp.serialize;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.ocean.rawsdk.client.entity.ResponseWrapper;
import com.alibaba.ocean.rawsdk.client.serialize.DeSerializer;
import com.alibaba.ocean.rawsdk.client.util.LoggerHelper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.ParseException;

/**
 * @author hongbang.hb
 *
 */
public abstract class AbstractJsonDeserializer implements DeSerializer {

	public abstract <T> ResponseWrapper<T> deSerialize(String content, Class<T> resultType);

	public abstract Throwable buildException(String content, int statusCode);

	public <T> ResponseWrapper<T> deSerialize(InputStream istream, Class<T> resultType, String charSet)
			throws IOException, ParseException {
		LoggerHelper.getClientLogger().finer("Parse body by json.");
		String content = getStreamAsString(istream, charSet);
		return deSerialize(content, resultType);
	}

	public Throwable buildException(InputStream inputStream, int statusCode, String charSet) throws IOException,
			ParseException {
		LoggerHelper.getClientLogger().finer("Parse body by json.");
		String content = getStreamAsString(inputStream, charSet);
		return buildException(content, statusCode);
	}

	protected <T> T parseResult(JSONObject json, Class<T> resultType) {
		LoggerHelper.getClientLogger().finer("Parse body by json.");
		T result = JSON.toJavaObject(json, resultType);
		return result;
	}

	private String getStreamAsString(InputStream stream, String charset) throws IOException {

		Reader reader = new InputStreamReader(stream, charset);
		StringBuilder response = new StringBuilder();

		final char[] buff = new char[1024];
		int read = 0;
		while ((read = reader.read(buff)) > 0) {
			response.append(buff, 0, read);
		}
		return response.toString();

	}
}
