package org.stuartaroth.httpjava.services.config;

public interface ConfigService {
    ApplicationType getApplicationType();
    DataServiceType getDataServiceType();
    CassandraConfig getCassandraConfig();
    ElasticConfig getElasticConfig();
    MysqlConfig getMysqlConfig();
    RabbitConfig getRabbitConfig();
    RedisConfig getRedisConfig();
}
