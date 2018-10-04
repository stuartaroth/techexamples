package org.stuartaroth.httpjava.services.config;

public class RedisConfig {
    private String host;
    private Integer port;

    public RedisConfig(String host, Integer port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public Integer getPort() {
        return port;
    }
}
