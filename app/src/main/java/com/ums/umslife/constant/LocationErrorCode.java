package com.ums.umslife.constant;

import java.util.HashMap;
import java.util.Map;

public class LocationErrorCode {
	public static final Map<String, String> ERROR_CODE=new HashMap<String, String>();
	{
		ERROR_CODE.put("62", "无法获取有效定位依据，定位失败，请检查运营商网络或者WiFi网络是否正常开启，尝试重新请求定位");
		ERROR_CODE.put("63", "网络异常，没有成功向服务器发起请求，请确认当前测试手机网络是否通畅，尝试重新请求定位");
		ERROR_CODE.put("68", "网络连接失败时，查找本地离线定位时对应的返回结果位");
		ERROR_CODE.put("167", "服务端定位失败，请您检查是否禁用获取位置信息权限，尝试重新请求定位");
	}

}
