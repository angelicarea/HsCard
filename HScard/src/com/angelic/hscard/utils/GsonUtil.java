package com.angelic.hscard.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.google.gson.Gson;

public class GsonUtil {

	private static Gson gson = null;
	public static final String setMethodModify = "set";

	static {
		if (gson == null) {
			gson = new Gson();
		}
	}

	private GsonUtil() {

	}

	/**
	 * 
	 * @Title objectToJson
	 * @Description 将对象转换成json格式
	 * @param obj
	 * @return
	 */
	public static String objectToJson(Object obj) {
		String jsonStr = null;
		if (gson != null) {
			jsonStr = gson.toJson(obj);
		}
		return jsonStr;
	}

	/**
	 * 
	 * @Title jsonToList
	 * @Description 将json格式转换成list对象
	 * @param jsonStr
	 * @return
	 */
	public static List<?> jsonToList(String jsonStr) {
		List<?> objList = null;
		if (gson != null) {
			java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<List<?>>() {
			}.getType();
			objList = gson.fromJson(jsonStr, type);
		}
		return objList;
	}

	/**
	 * 
	 * @Title jsonToList
	 * @Description 将json格式转换成list对象，并准确指定类型
	 * @param jsonStr
	 * @param type
	 * @return
	 */
	public static List<?> jsonToList(String jsonStr, java.lang.reflect.Type type) {
		List<?> objList = null;
		if (gson != null) {
			objList = gson.fromJson(jsonStr, type);
		}
		return objList;
	}

	/**
	 * 
	 * @Title jsonToMap
	 * @Description 将json格式转换成map对象
	 * @param jsonStr
	 * @return
	 */
	public static Map<?, ?> jsonToMap(String jsonStr) {
		Map<?, ?> objMap = null;
		if (gson != null) {
			java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<Map<?, ?>>() {
			}.getType();
			objMap = gson.fromJson(jsonStr, type);
		}
		return objMap;
	}

	/**
	 * 
	 * @Title mapToObject
	 * @Description 将map转化为对象
	 * @param map 要转化的map
	 * @param obj 转化目标对象
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Object mapToObject(Map<String, String> map, Object obj) {

		Class class1 = obj.getClass();
		Method[] methods = class1.getMethods();
		for (Method method : methods) {
			String methodName = method.getName();
			if (methodName.startsWith(GsonUtil.setMethodModify)) {
				//toLowerCase为转化小写,toUpperCase为转化大写
				String propertyName = methodName.substring(3).toLowerCase(
						Locale.getDefault());
				String value = map.get(propertyName);
				try {
					method.invoke(obj, value);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
		return obj;
	}

	/**
	 * 
	 * @Title jsonToBean
	 * @Description 将json转换成bean对象
	 * @param jsonStr
	 * @param cl
	 * @return
	 */
	public static Object jsonToBean(String jsonStr, Class<?> cl) {
		Object obj = null;
		if (gson != null) {
			obj = gson.fromJson(jsonStr, cl);
		}
		return obj;
	}

	/**
	 * 
	 * @Title getJsonValue
	 * @Description 通过key获得Json的相应对象Value
	 * @param jsonStr
	 * @param key
	 * @return
	 */
	public static Object getJsonValue(String jsonStr, String key) {
		Object rulsObj = null;
		Map<?, ?> rulsMap = jsonToMap(jsonStr);
		if (rulsMap != null && rulsMap.size() > 0) {
			rulsObj = rulsMap.get(key);
		}
		return rulsObj;
	}

}