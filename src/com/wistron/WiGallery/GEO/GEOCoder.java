package com.wistron.WiGallery.GEO;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Utilities.CSStaticData;
import Utilities.FileOperation;
import android.util.Log;

/**
 * Copyright (c) 2011 Wistron SWPC
 * All rights reserved.
 * @author Cocoonshu
 * @date 2012-03-05 15:33:53
 * @purpose 联网查询Google地理信息
 * @detail  
 */


/*
 {
   "results" : [
      {
         "address_components" : [
            {
               "long_name" : "103省道",
               "short_name" : "S303",
               "types" : [ "route" ]
            },
            {
               "long_name" : "涪陵区",
               "short_name" : "涪陵区",
               "types" : [ "sublocality", "political" ]
            },
            {
               "long_name" : "重庆",
               "short_name" : "重庆",
               "types" : [ "locality", "political" ]
            },
            {
               "long_name" : "重庆",
               "short_name" : "重庆",
               "types" : [ "administrative_area_level_1", "political" ]
            },
            {
               "long_name" : "中国",
               "short_name" : "CN",
               "types" : [ "country", "political" ]
            }
         ],
         "formatted_address" : "中国重庆 涪陵区103省道",
         "geometry" : {
            "bounds" : {
               "northeast" : {
                  "lat" : 29.71671280,
                  "lng" : 107.41133150
               },
               "southwest" : {
                  "lat" : 29.7105910,
                  "lng" : 107.40940930
               }
            },
            "location" : {
               "lat" : 29.71371960,
               "lng" : 107.41010450
            },
            "location_type" : "APPROXIMATE",
            "viewport" : {
               "northeast" : {
                  "lat" : 29.71671280,
                  "lng" : 107.4117193802915
               },
               "southwest" : {
                  "lat" : 29.7105910,
                  "lng" : 107.4090214197085
               }
            }
         },
         "types" : [ "route" ]
      },
      {
         "address_components" : [
            {
               "long_name" : "涪陵区",
               "short_name" : "涪陵区",
               "types" : [ "sublocality", "political" ]
            },
            {
               "long_name" : "重庆",
               "short_name" : "重庆",
               "types" : [ "locality", "political" ]
            },
            {
               "long_name" : "重庆",
               "short_name" : "重庆",
               "types" : [ "administrative_area_level_1", "political" ]
            },
            {
               "long_name" : "中国",
               "short_name" : "CN",
               "types" : [ "country", "political" ]
            }
         ],
         "formatted_address" : "中国重庆 涪陵区",
         "geometry" : {
            "bounds" : {
               "northeast" : {
                  "lat" : 30.00142650,
                  "lng" : 107.71963580
               },
               "southwest" : {
                  "lat" : 29.35975380,
                  "lng" : 106.94597010
               }
            },
            "location" : {
               "lat" : 29.7031130,
               "lng" : 107.3892980
            },
            "location_type" : "APPROXIMATE",
            "viewport" : {
               "northeast" : {
                  "lat" : 30.00142650,
                  "lng" : 107.71963580
               },
               "southwest" : {
                  "lat" : 29.35975380,
                  "lng" : 106.94597010
               }
            }
         },
         "types" : [ "sublocality", "political" ]
      },
      {
         "address_components" : [
            {
               "long_name" : "重庆",
               "short_name" : "重庆",
               "types" : [ "administrative_area_level_1", "political" ]
            },
            {
               "long_name" : "中国",
               "short_name" : "CN",
               "types" : [ "country", "political" ]
            }
         ],
         "formatted_address" : "中国重庆",
         "geometry" : {
            "bounds" : {
               "northeast" : {
                  "lat" : 32.20118710,
                  "lng" : 110.19985820
               },
               "southwest" : {
                  "lat" : 28.16022530,
                  "lng" : 105.28976060
               }
            },
            "location" : {
               "lat" : 29.563010,
               "lng" : 106.5515570
            },
            "location_type" : "APPROXIMATE",
            "viewport" : {
               "northeast" : {
                  "lat" : 32.20118710,
                  "lng" : 110.19985820
               },
               "southwest" : {
                  "lat" : 28.16022530,
                  "lng" : 105.28976060
               }
            }
         },
         "types" : [ "administrative_area_level_1", "political" ]
      },
      {
         "address_components" : [
            {
               "long_name" : "重庆",
               "short_name" : "重庆",
               "types" : [ "locality", "political" ]
            },
            {
               "long_name" : "重庆",
               "short_name" : "重庆",
               "types" : [ "administrative_area_level_1", "political" ]
            },
            {
               "long_name" : "中国",
               "short_name" : "CN",
               "types" : [ "country", "political" ]
            }
         ],
         "formatted_address" : "中国重庆",
         "geometry" : {
            "bounds" : {
               "northeast" : {
                  "lat" : 32.20118710,
                  "lng" : 110.19985820
               },
               "southwest" : {
                  "lat" : 28.16022530,
                  "lng" : 105.28976060
               }
            },
            "location" : {
               "lat" : 29.563010,
               "lng" : 106.5515570
            },
            "location_type" : "APPROXIMATE",
            "viewport" : {
               "northeast" : {
                  "lat" : 29.74019680,
                  "lng" : 106.81382420
               },
               "southwest" : {
                  "lat" : 29.36962830,
                  "lng" : 106.28328320
               }
            }
         },
         "types" : [ "locality", "political" ]
      },
      {
         "address_components" : [
            {
               "long_name" : "中国",
               "short_name" : "CN",
               "types" : [ "country", "political" ]
            }
         ],
         "formatted_address" : "中国",
         "geometry" : {
            "bounds" : {
               "northeast" : {
                  "lat" : 53.56097399999999,
                  "lng" : 134.772810
               },
               "southwest" : {
                  "lat" : 18.15352160,
                  "lng" : 73.49941369999999
               }
            },
            "location" : {
               "lat" : 35.861660,
               "lng" : 104.1953970
            },
            "location_type" : "APPROXIMATE",
            "viewport" : {
               "northeast" : {
                  "lat" : 53.56097399999999,
                  "lng" : 134.772810
               },
               "southwest" : {
                  "lat" : 18.15352160,
                  "lng" : 73.49941369999999
               }
            }
         },
         "types" : [ "country", "political" ]
      }
   ],
   "status" : "OK"
}
*/

public class GEOCoder {
	private static final String TAG              = "GEOcoder";
	public  static final int    GEO_LINK_OK      = 200;
	public  static final int    PRECISION_ORG    = 0x0201;     //地理位置优化精度：原始坐标
	public  static final int    PRECISION_OPT    = 0x0202;     //地理位置优化精度：优化坐标
	private static       long   mLastRequestTime = 0;          //上次请求的时间
	
	
	/**
	 * 获取地理地址JSON, 不能在主线程中使用
	 * @param longitude
	 * @param latitude
	 * @return
	 */
	public static StringBuilder getAddress(double longitude, double latitude, String langeuage) {
		String         uriAPI       = null;
		StringBuilder  result       = null;
		BufferedReader buffer       = null;
		HttpGet        httpRequest  = null;
		HttpResponse   httpResponse = null;

		if(langeuage == null || langeuage.equals("")){
			langeuage = "en-EN";
		}

		if(Double.isNaN(longitude) && Double.isNaN(latitude)){
			return null;
		}
		
		if(longitude == 0 && latitude == 0){
			return null;
		}
		/*
		 * http://maps.googleapis.com/maps/api/geocode/json?latlng=40.714224,-73.961452&sensor=true_or_false
		 */

		uriAPI = "https://maps.googleapis.com/maps/api/geocode/json?"
				+ "latlng="
				+ FileOperation.double2String(longitude, 6)
				+ ","
				+ FileOperation.double2String(latitude, 6)
				+ "&sensor=false"
				+ "&language="
				+ langeuage;

		//执行GoolgeAPI请求等待
		long diffTime = System.currentTimeMillis() - mLastRequestTime;
		if(diffTime < CSStaticData.GEO_REQUEST_SLEEP){
			try {
				Thread.sleep(CSStaticData.GEO_REQUEST_SLEEP - diffTime);
			} catch (InterruptedException exp) {
				if(CSStaticData.DEBUG){
					exp.printStackTrace();
				}
			}
		}
		mLastRequestTime = System.currentTimeMillis();
		
		if(CSStaticData.DEBUG){
			Log.w(TAG, "[getAddress]在线请求 GPS(" + latitude + ", " + longitude + "):");
		}
		
		httpRequest = new HttpGet(uriAPI);
		try {
			httpResponse = new DefaultHttpClient().execute(httpRequest);
			if (httpResponse.getStatusLine().getStatusCode() == GEO_LINK_OK) {
				result = new StringBuilder();
				buffer = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
				for (String itr = buffer.readLine(); itr != null; itr = buffer.readLine()) {
					result.append(itr);
				}
			}else{
				result = null;
			}

		} catch (Exception e) {
			if(CSStaticData.DEBUG){
				e.printStackTrace();
			}
			result = null;
		}
		
		return result;
	}

	public static String[] parserJSON(String jsonContent, int precision) {
		String[]   result                               = null;
		int        compArraySize                        = 0;
		int        resultsArraySize                     = 0;
		JSONObject jsonRoot                             = null; //整个JSON文件
		JSONArray  jsonResults                          = null; //Results节点
		String     jsonStatus                           = null; //Status节点
		JSONArray  jsonAddressComponents                = null; //AddressCompenents节点
		JSONObject jsonResultsNode                      = null; //AddressResults节点
		JSONObject jsonAddressNode                      = null; //AddressDetail节点
		
		String     addressCountry                       = null; //表示国家政治实体。在地址解析器返回的结果中，该部分通常列在最前面。
		String     addressAdministrativeAreaLevel_1     = null; //表示仅次于国家级别的行政实体。在美国，这类行政实体是指州。并非所有国家都有该行政级别。
		String     addressAdministrativeAreaLevel_2     = null; //表示国家级别下的二级行政实体。在美国，这类行政实体是指县。并非所有国家都有该行政级别。
		String     addressAdministrativeAreaLevel_3     = null; //表示国家级别下的三级行政实体。此类型表示较小的行政单位。并非所有国家都有该行政级别。
		String     addressLocality                      = null; //表示合并的市镇级别政治实体。
		String     addressSublocality                   = null; //表示仅次于地区级别的行政实体。
		
		
		try {
			//获取JSON root层节点序列
			jsonRoot    = new JSONObject(jsonContent);
			jsonResults = jsonRoot.optJSONArray("results");
			jsonStatus  = jsonRoot.optString("status");

			//判断status节点
			if(CSStaticData.DEBUG){
				Log.w(TAG, "[parserJSON]Google服务器状态： " + jsonStatus);
			}
			if(jsonStatus == null || !jsonStatus.equals("OK")){
				return null;
			}
			//获取JSON address_components层节点序列
			if(jsonResults != null && jsonResults.length() > 0){
				resultsArraySize = jsonResults.length();
				for(int i = 0; i < resultsArraySize; i++){
					jsonResultsNode =  jsonResults.optJSONObject(i);
					jsonAddressComponents = jsonResultsNode.optJSONArray("address_components");
					if(jsonResultsNode != null && jsonResultsNode.optJSONArray("types").getString(0).equals("route")){// 优先检查精确地址
						if(jsonAddressComponents != null && jsonAddressComponents.length() > 0){
							compArraySize = jsonAddressComponents.length();
							for(int j = 0; j < compArraySize; j++){
								jsonAddressNode = jsonAddressComponents.optJSONObject(j);
								if(jsonAddressNode != null){
									if(jsonAddressNode.optJSONArray("types").getString(0).equals("country")){
										addressCountry = jsonAddressNode.optString("long_name");
										continue;
									}
									if(jsonAddressNode.optJSONArray("types").getString(0).equals("administrative_area_level_1")){
										addressAdministrativeAreaLevel_1 = jsonAddressNode.optString("long_name");
										continue;
									}
									if(jsonAddressNode.optJSONArray("types").getString(0).equals("administrative_area_level_2")){
										addressAdministrativeAreaLevel_2 = jsonAddressNode.optString("long_name");
										continue;
									}
									if(jsonAddressNode.optJSONArray("types").getString(0).equals("administrative_area_level_3")){
										addressAdministrativeAreaLevel_3 = jsonAddressNode.optString("long_name");
										continue;
									}
									if(jsonAddressNode.optJSONArray("types").getString(0).equals("locality")){
										addressLocality = jsonAddressNode.optString("long_name");
										continue;
									}
									if(jsonAddressNode.optJSONArray("types").getString(0).equals("sublocality")){
										addressSublocality = jsonAddressNode.optString("long_name");
										continue;
									}
								}
							}
						}
						
						break;
					}else{// 没有精确地址，则检查第二级地址
						if(jsonAddressComponents != null && jsonAddressComponents.length() > 0){
							compArraySize = jsonAddressComponents.length();
							for(int j = 0; j < compArraySize; j++){
								jsonAddressNode = jsonAddressComponents.optJSONObject(j);
								if(jsonAddressNode != null){
									if(jsonAddressNode.optJSONArray("types").getString(0).equals("country")){
										addressCountry = jsonAddressNode.optString("long_name");
										continue;
									}
									if(jsonAddressNode.optJSONArray("types").getString(0).equals("administrative_area_level_1")){
										addressAdministrativeAreaLevel_1 = jsonAddressNode.optString("long_name");
										continue;
									}
									if(jsonAddressNode.optJSONArray("types").getString(0).equals("administrative_area_level_2")){
										addressAdministrativeAreaLevel_2 = jsonAddressNode.optString("long_name");
										continue;
									}
									if(jsonAddressNode.optJSONArray("types").getString(0).equals("administrative_area_level_3")){
										addressAdministrativeAreaLevel_3 = jsonAddressNode.optString("long_name");
										continue;
									}
									if(jsonAddressNode.optJSONArray("types").getString(0).equals("locality")){
										addressLocality = jsonAddressNode.optString("long_name");
										continue;
									}
									if(jsonAddressNode.optJSONArray("types").getString(0).equals("sublocality")){
										addressSublocality = jsonAddressNode.optString("long_name");
										continue;
									}
								}
							}
						}
						break;
					}
				}
				
				int m = 0;
				result = new String[4];
				result[m] = addressCountry;
				m++;
				
				if(addressAdministrativeAreaLevel_1 != null && !addressAdministrativeAreaLevel_1.equals("")){
					result[m] = addressAdministrativeAreaLevel_1;
					m++;
				}
				if(addressAdministrativeAreaLevel_2 != null && !addressAdministrativeAreaLevel_2.equals("")){
					result[m] = addressAdministrativeAreaLevel_2;
					m++;
				}
				if(addressAdministrativeAreaLevel_3 != null && !addressAdministrativeAreaLevel_3.equals("")){
					result[m] = addressAdministrativeAreaLevel_3;
					m++;
				}
				if(m < 4){
					result[m] = addressLocality;
					m++;
				}
				if(m < 4){
					result[m] = addressSublocality;
					m++;
				}
			}
			
			if(CSStaticData.DEBUG){
				Log.w(TAG,"[parserJSON]原始坐标地理信息：" + jsonRoot.toString());
			}
			
		} catch (JSONException e) {
			if(CSStaticData.DEBUG){
				e.printStackTrace();
			}
			result = null;
		}

		return result;
	}
}
