/**
 * Project: ocean.client.java.basic
 * 
 * File Created at 2011-10-27
 * $Id: HttpResponseBuilder.java 412998 2015-05-22 06:37:04Z hongbang.hb $
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
package com.alibaba.ocean.rawsdk.client.http;

import com.alibaba.ocean.rawsdk.client.Response;
import com.alibaba.ocean.rawsdk.client.serialize.DeSerializerListener;

import java.io.InputStream;
import java.util.Collection;

/**
 * 
 * @author hongbang.hb
 *
 */
public interface HttpResponseBuilder {

	/**
	 * 
	 * @param istream
	 * @param httpCode
	 * @param contentEncoding
	 * @param contentType
	 * @param invokeContext
	 * @return
	 */
	public Response buildResponse(InputStream istream, int httpCode, String contentEncoding, String contentType,
                                  InvokeContext invokeContext, Collection<DeSerializerListener> deSerializerListners);

}
