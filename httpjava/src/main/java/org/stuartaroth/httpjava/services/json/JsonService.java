package org.stuartaroth.httpjava.services.json;

import java.lang.reflect.Type;

public interface JsonService {
    String write(Object object) throws Exception;
    <T> T read(String json, Class<T> type) throws Exception;
    <T> T read(String json, Type type) throws Exception;
}
