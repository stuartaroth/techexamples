package org.stuartaroth.httpjava.services.id;

public interface IdService {
    String generateId();
    String generateId(String prefix);
}
