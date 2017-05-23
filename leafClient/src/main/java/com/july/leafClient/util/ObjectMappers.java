package com.july.leafClient.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.util.Map;

/**
 * Created by haoyifen on 2017/5/22 13:47.
 */
public class ObjectMappers {
    private static final ObjectMapper objectMapper = myObjectMapper();

    public static ObjectWriter STR_OBJECT_MAP_WRITTER = objectMapper.writerFor(new TypeReference<Map<String, Object>>() {
    });
    public static ObjectReader STR_OBJECT_MAP_READER = objectMapper.readerFor(new TypeReference<Map<String, Object>>() {
    });
    public static ObjectReader STR_STR_MAP_READER = objectMapper.readerFor(new TypeReference<Map<String, String>>() {
    });
    public static ObjectWriter STR_STR_MAP_WRITTER = objectMapper.writerFor(new TypeReference<Map<String, String>>() {
    });

    private ObjectMappers() {

    }

    public static ObjectMapper myObjectMapper() {
        ObjectMapper mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(DeserializationFeature.USE_LONG_FOR_INTS, true)
                .configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        return mapper;
    }

}
