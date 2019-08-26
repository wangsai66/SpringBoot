package com.base;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.google.common.collect.ImmutableMap;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

/**
 * @ClassName DataSerializer
 * @Description 为兼容之前格式
 * @Author liwd
 * @Date 2019/07/20
 */
public class DataSerializer extends JsonSerializer {

    @Override
    public void serialize(Object object, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        String simpleName=object.getClass().getSimpleName();
        if(object instanceof Collection){
            simpleName="list";
        }
        String dk=simpleName.substring(0, 1).toLowerCase()+simpleName.substring(1);
        Map map = ImmutableMap.of(dk, object);
        jsonGenerator.writeObject(map);
    }
}
