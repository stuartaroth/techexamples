package org.stuartaroth.httpjava.services.config;

public class MysqlConfig {
    private String host;
    private Integer port;
    private String database;
    private String username;
    private String password;

    public MysqlConfig(String host, Integer port, String database, String username, String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
    }

    public String getHost() {
        return host;
    }

    public Integer getPort() {
        return port;
    }

    public String getDatabase() {
        return database;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
