package org.stuartaroth.httpjava.services.config;

public class ElasticConfig {
    private String host;
    private Integer tcpPort;
    private String clusterName;

    public ElasticConfig(String host, Integer tcpPort, String clusterName) {
        this.host = host;
        this.tcpPort = tcpPort;
        this.clusterName = clusterName;
    }

    public String getHost() {
        return host;
    }

    public Integer getTcpPort() {
        return tcpPort;
    }

    public String getClusterName() {
        return clusterName;
    }
}
