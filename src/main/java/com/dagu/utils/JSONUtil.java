package com.dagu.utils;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JSONUtil<T> {

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static  String getString(Object o){
        String result = null;

        try {
            result = objectMapper.writeValueAsString(o);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public T getObject(String s,Class<T> c){
        T t = null;

        try {
            t = objectMapper.readValue(s,c);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return t;
    }
}
