package org.stuartaroth.httpjava.services.config;

public class RabbitConfig {
    private String localhost;
    private Integer port;
    private String username;
    private String password;

    public RabbitConfig(String localhost, Integer port, String username, String password) {
        this.localhost = localhost;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    public String getLocalhost() {
        return localhost;
    }

    public Integer getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
