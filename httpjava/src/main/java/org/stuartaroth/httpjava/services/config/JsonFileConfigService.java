package org.stuartaroth.httpjava.services.config;

public class JsonFileConfigService implements ConfigService {
    private ApplicationType applicationType;
    private DataServiceType dataServiceType;
    private CassandraConfig cassandraConfig;
    private ElasticConfig elasticConfig;
    private MysqlConfig mysqlConfig;
    private RabbitConfig rabbitConfig;
    private RedisConfig redisConfig;

    public JsonFileConfigService(ApplicationType applicationType, DataServiceType dataServiceType, CassandraConfig cassandraConfig, ElasticConfig elasticConfig, MysqlConfig mysqlConfig, RabbitConfig rabbitConfig, RedisConfig redisConfig) {
        this.applicationType = applicationType;
        this.dataServiceType = dataServiceType;
        this.cassandraConfig = cassandraConfig;
        this.elasticConfig = elasticConfig;
        this.mysqlConfig = mysqlConfig;
        this.rabbitConfig = rabbitConfig;
        this.redisConfig = redisConfig;
    }

    @Override
    public ApplicationType getApplicationType() {
        return applicationType;
    }

    @Override
    public DataServiceType getDataServiceType() {
        return dataServiceType;
    }

    @Override
    public CassandraConfig getCassandraConfig() {
        return cassandraConfig;
    }

    @Override
    public ElasticConfig getElasticConfig() {
        return elasticConfig;
    }

    @Override
    public MysqlConfig getMysqlConfig() {
        return mysqlConfig;
    }

    @Override
    public RabbitConfig getRabbitConfig() {
        return rabbitConfig;
    }

    @Override
    public RedisConfig getRedisConfig() {
        return redisConfig;
    }
}
