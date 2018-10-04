package org.stuartaroth.httpjava.services.id;

import java.util.UUID;

public class UniqueIdService implements IdService {
    @Override
    public String generateId() {
        return UUID.randomUUID().toString();
    }

    @Override
    public String generateId(String prefix) {
        return prefix + generateId();
    }
}
