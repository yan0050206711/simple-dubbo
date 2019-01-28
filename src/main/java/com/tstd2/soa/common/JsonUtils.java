package com.tstd2.soa.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import java.lang.reflect.Type;

/**
 * Json工具类。
 * 
 * @author yancey
 */
public final class JsonUtils {

	private static final Gson GSON = new GsonBuilder()
	.setDateFormat("yyyy-MM-dd HH:mm:ss")
	.disableHtmlEscaping()
	.create();

	/**
	 * 将一个对象序列化成字符串。
	 * 
	 * @param obj 要序列化的对象。
	 * @return
	 *      序列化后的字符串。
	 */
	public static String toJson(Object obj){
		return GSON.toJson(obj);
	}

	/**
	 * 将一个对象序列化成字符串。
	 * 
	 * @param obj 要序列化的对象。
	 * @param type obj对象的类型信息。
	 * @return
	 *      序列化后的字符串。
	 */
	public static String toJson(Object obj, Type type){
		return GSON.toJson(obj, type);
	}

	/**
	 * 将一个Json串反序列化成Java对象。
	 * 
	 * @param json 要进行反序列化的Json。
	 * @param clazz 反序列化后的Java对象类型。
	 * @return
	 *      反序列后的Java对象。
	 */
	public static <T> T fromJson(String json, Class<T> clazz){
		return GSON.fromJson(json, clazz);
	}

	/**
	 * 将一个Json元素反序列化成Java对象。
	 *
	 * @param jsonElement 要进行反序列化的Json元素。
	 * @param clazz 反序列化后的Java对象类型。
	 * @return
	 *      反序列后的Java对象。
	 */
	public static <T> T fromJson(JsonElement jsonElement, Class<T> clazz ){
		return GSON.fromJson(jsonElement, clazz);
	}

	/**
	 * 将一个Json串反序列化成Java对象。
	 * 
	 * @param json 要进行反序列化的Json。
	 * @param typeOfT 反序列化后的Java对象的类型信息。
	 * <p>如：Type typeOfT = new TypeToken&lt;Collection&lt;Foo>>(){}.getType();
	 * @return
	 * 		反序列后的Java对象。
	 */
	public static <T> T fromJson(String json, Type typeOfT){
		return GSON.fromJson(json, typeOfT);
	}

	/**
	 * 将一个Json元素反序列化成Java对象。
	 *
	 * @param jsonElement 要进行反序列化的Json元素。
	 * @param typeOfT 反序列化后的Java对象的类型信息。
	 * <p>如：Type typeOfT = new TypeToken&lt;Collection&lt;Foo>>(){}.getType();
	 * @return
	 * 		反序列后的Java对象。
	 */
	public static <T> T fromJson(JsonElement jsonElement, Type typeOfT){
		return GSON.fromJson(jsonElement, typeOfT);
	}

	private JsonUtils(){}

}
