/**
 * 
 */
package com.alibaba.ocean.rawsdk.client.http.platform;

import com.alibaba.ocean.rawsdk.client.Response;
import com.alibaba.ocean.rawsdk.client.http.AbstractHttpClient;
import com.alibaba.ocean.rawsdk.client.http.HttpResponseBuilder;
import com.alibaba.ocean.rawsdk.client.http.HttpSupport;
import com.alibaba.ocean.rawsdk.client.http.InvokeContext;
import com.alibaba.ocean.rawsdk.client.policy.ClientPolicy;
import com.alibaba.ocean.rawsdk.client.serialize.DeSerializerListener;
import com.alibaba.ocean.rawsdk.client.serialize.SerializerProvider;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * @author hongbang.hb
 *
 */
public class HttpURLConnectionClient extends AbstractHttpClient {

	public HttpURLConnectionClient(SerializerProvider serializerProvider, HttpResponseBuilder responseBuilder) {
		super(serializerProvider, responseBuilder);
	}
	
	protected Response doMultipartPost(URL url, Map<String, Object> parameters, InvokeContext invokeContext,
			ClientPolicy clientPolicy, Collection<DeSerializerListener> deSerializerListners) throws IOException {
		HttpURLConnection conn = null;
		Response response = null;
		String BOUNDARY = "---------------------------123821742118716";
		OutputStream out = null;
		try {
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			Map<String, String> clientHttpHeader = HttpSupport.buildHttpHeader(invokeContext, clientPolicy);
			for (Map.Entry<String, String> entry : clientHttpHeader.entrySet()) {
				conn.setRequestProperty(entry.getKey(), entry.getValue());
			}
			conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

			out = new DataOutputStream(conn.getOutputStream());

			StringBuffer strBuf = new StringBuffer();
			Iterator<Map.Entry<String, Object>> iter = parameters.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry<String, Object> entry = iter.next();
				String inputName = (String) entry.getKey();
				Object inputValue = entry.getValue();
				if (inputValue == null) {
					continue;
				}
				if (inputValue.getClass().isAssignableFrom(byte[].class)
						|| inputValue.getClass().isAssignableFrom(Byte[].class)) {
					strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
					strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"" + "\"\r\n");
					strBuf.append("Content-Type:" + "application/x-www-form-urlencoded" + "\r\n\r\n");
					out.write(strBuf.toString().getBytes());
					if (inputValue instanceof byte[]) {
						byte[] inputBytes = (byte[]) inputValue;
						out.write(inputBytes);
					}
					if (inputValue instanceof Byte[]) {
						Byte[] inputBytes = (Byte[]) inputValue;
						for (Byte b : inputBytes) {
							out.write(b.byteValue());
						}
					}
				} else {
					strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
					strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"\r\n\r\n");
					strBuf.append(inputValue);
					out.write(strBuf.toString().getBytes());
				}
			}
			int httpCode = conn.getResponseCode();
			String contentEncoding = conn.getContentEncoding();
			String contentType = conn.getContentType();
			InputStream inputStream = null;
			if (httpCode >= 200 && httpCode <= 299) {
				inputStream = conn.getInputStream();
			} else {
				inputStream = conn.getErrorStream();
			}
			response = this.getResponseBuilder().buildResponse(inputStream, httpCode, contentEncoding, contentType,
					invokeContext, deSerializerListners);
		} catch (IOException e) {
			throw e;
		} finally {
			if (out != null) {
				out.close();
			}
			if (conn != null) {
				conn.disconnect();
			}
		}
		return response;
	}

	protected Response doPost(URL url, Map<String, Object> parameters, InvokeContext invokeContext,
			ClientPolicy clientPolicy, Collection<DeSerializerListener> deSerializerListners) throws IOException {
		HttpURLConnection conn = null;
		OutputStream out = null;
		Response response = null;
		try {
			try {
				conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("POST");
				conn.setDoInput(true);
				conn.setDoOutput(true);
				Map<String, String> clientHttpHeader = HttpSupport.buildHttpHeader(invokeContext, clientPolicy);
				for (Map.Entry<String, String> entry : clientHttpHeader.entrySet()) {
					conn.setRequestProperty(entry.getKey(), entry.getValue());
				}
			} catch (IOException e) {
				throw e;
			}
			try {
				String query = HttpSupport.buildQuery(parameters);
				byte[] content = {};
				if (query != null) {
					content = query.getBytes(invokeContext.getPolicy().getContentCharset());
				}
				out = conn.getOutputStream();
				out.write(content);

				int httpCode = conn.getResponseCode();
				String contentEncoding = conn.getContentEncoding();
				String contentType = conn.getContentType();
				InputStream inputStream = null;
				if (httpCode >= 200 && httpCode <= 299) {
					inputStream = conn.getInputStream();
				} else {
					inputStream = conn.getErrorStream();
				}

				response = this.getResponseBuilder().buildResponse(inputStream, httpCode, contentEncoding, contentType,
						invokeContext, deSerializerListners);
			} catch (IOException e) {
				throw e;
			}
		} finally {
			if (out != null) {
				out.close();
			}
			if (conn != null) {
				conn.disconnect();
			}
		}
		return response;
	}

	protected Response doGet(URL url, Map<String, Object> parameters, InvokeContext invokeContext,
			ClientPolicy clientPolicy, Collection<DeSerializerListener> deSerializerListners) throws IOException {
		HttpURLConnection conn = null;
		OutputStream out = null;
		Response response = null;
		try {
			try {
				conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				conn.setDoInput(true);
				conn.setDoOutput(true);
				Map<String, String> clientHttpHeader = HttpSupport.buildHttpHeader(invokeContext, clientPolicy);
				for (Map.Entry<String, String> entry : clientHttpHeader.entrySet()) {
					conn.setRequestProperty(entry.getKey(), entry.getValue());
				}
			} catch (IOException e) {
				throw e;
			}
			try {
				int httpCode = conn.getResponseCode();
				String contentEncoding = conn.getContentEncoding();
				String contentType = conn.getContentType();
				InputStream inputStream = null;
				if (httpCode >= 200 && httpCode <= 299) {
					inputStream = conn.getInputStream();
				} else {
					inputStream = conn.getErrorStream();
				}

				response = this.getResponseBuilder().buildResponse(inputStream, httpCode, contentEncoding, contentType,
						invokeContext, deSerializerListners);
			} catch (IOException e) {
				throw e;
			}
		} finally {
			if (out != null) {
				out.close();
			}
			if (conn != null) {
				conn.disconnect();
			}
		}
		return response;
	}

}
