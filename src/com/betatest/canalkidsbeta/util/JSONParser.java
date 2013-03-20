package com.betatest.canalkidsbeta.util;

import java.io.IOException;

import android.util.Log;

import com.betatest.canalkidsbeta.vo.ChannelContentsResponseParcel;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONParser {

	private static final String TAG = JSONParser.class.getSimpleName();

	/**
	 * 
	 * Gets a content from a url
	 * 
	 * @param url
	 * 
	 * @return String
	 * 
	 */
	public static String getUrlResponse(String url) {

		String response = "";
		Log.d(TAG, "Getting response from server");
		try {
			response = HttpUtil.doHttpGet(url);
		} catch (Exception e) {
			Log.d(TAG, "Error when getting response from server " + e);
			e.printStackTrace();
		}
		Log.d(TAG, "Response ok");
		return response;
	}

	/**
	 * 
	 * Get a ChannelContentsResponse from the json
	 * 
	 * @param json
	 * 
	 * @return ChannelContentsResponseParcel
	 * 
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 * 
	 */
	public static ChannelContentsResponseParcel getChannelContentsPojoFromJson(
			String json) throws JsonParseException, JsonMappingException,
			IOException {

		Log.d(TAG, "Getting contents from json and creating pojo");
		ObjectMapper objectMapper = new ObjectMapper();

		return objectMapper.readValue(json,
				new TypeReference<ChannelContentsResponseParcel>() {
				});
	}
}