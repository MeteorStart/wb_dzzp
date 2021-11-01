package com.qiyang.wb_dzzp.network.converter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.qiyang.wb_dzzp.utils.LogUtils;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Converter;

/**
 * @author: X_Meteor
 * @description: 类描述
 * @version: V_1.0.0
 * @date: 2017/6/14 14:32
 * @company:
 * @email: lx802315@163.com
 */
public class JsonRequestBodyConverter<T> implements Converter<T, RequestBody> {
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");
    private final Gson gson;
    private final TypeAdapter<T> adapter;

    /**
     * 构造器
     */

    public JsonRequestBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }


    @Override
    public RequestBody convert(T value) throws IOException {
        String postBody = gson.toJson(value); //对象转化成json
        LogUtils.Companion.print("转化后的数据：" + postBody);
        return RequestBody.create(MEDIA_TYPE, postBody);
    }

}
