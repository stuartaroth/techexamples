package org.stuartaroth.httpjava.services.config;

public class CassandraConfig {
    private String seeds;
    private String keyspace;
    private Integer port;
    private String consistency;

    public CassandraConfig(String seeds, String keyspace, Integer port, String consistency) {
        this.seeds = seeds;
        this.keyspace = keyspace;
        this.port = port;
        this.consistency = consistency;
    }

    public String getSeeds() {
        return seeds;
    }

    public String getKeyspace() {
        return keyspace;
    }

    public Integer getPort() {
        return port;
    }

    public String getConsistency() {
        return consistency;
    }
}
