package com.tstd2.soa.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Json工具类。
 * 
 * @author yancey
 */
public final class JsonUtils {

	private static final Gson GSON = new GsonBuilder()
	.setDateFormat("yyyy-MM-dd HH:mm:ss")//设置时间格式
	//指定map实现类型
	.registerTypeAdapter(Map.class, new JsonDeserializer<Map<String,Object>>() {
		@Override
		public Map<String, Object> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
			JsonObject jsonObject = json.getAsJsonObject();
			//JsonObject不能为JsonArray
			if(jsonObject.isJsonArray()){
				throw new JsonParseException("JsonArray can't parse to Map : " + json.toString());
			}
			Set<Entry<String, JsonElement>> set = jsonObject.entrySet();
			Map<String, Object> map = new HashMap<>(set.size(), 1.0f);
			for(Entry<String, JsonElement> entry : set){
				JsonElement valueElement = entry.getValue();
				Object value;
				if(valueElement.isJsonPrimitive()){
					JsonPrimitive primitiveValue = (JsonPrimitive) valueElement;
					if(primitiveValue.isBoolean()){
						value = primitiveValue.getAsBoolean();
					}else if(primitiveValue.isNumber()){
						String stringValue = primitiveValue.getAsString();
						try{
							value = Long.parseLong(stringValue);
						} catch (NumberFormatException e){
							//其他情况做浮点数处理，这里还忽略了一个大数的情况。
							value = primitiveValue.getAsDouble();
						}
					}else if(primitiveValue.isString()){
						value = primitiveValue.getAsString();
					}else{
						value = primitiveValue;
					}
				}else if(valueElement.isJsonNull()){
					value = null;
				}else {
					value = valueElement;
				}
				map.put(entry.getKey(), value);
			}
			return map;
		}
	})
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
