package org.stuartaroth.httpjava.services.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;

public class DefaultJsonService implements JsonService {
    private static Logger logger = LoggerFactory.getLogger(DefaultJsonService.class);

    private Gson gson;

    public DefaultJsonService() {
        gson = new GsonBuilder().disableHtmlEscaping().serializeNulls().create();
    }

    @Override
    public String write(Object object) {
        return gson.toJson(object);
    }

    @Override
    public <T> T read(String json, Class<T> type) {
        return gson.fromJson(json, type);
    }

    @Override
    public <T> T read(String json, Type type) {
        return gson.fromJson(json, type);
    }
}
