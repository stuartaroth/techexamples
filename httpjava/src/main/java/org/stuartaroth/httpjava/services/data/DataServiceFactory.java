package org.stuartaroth.httpjava.services.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stuartaroth.httpjava.services.config.ConfigService;
import org.stuartaroth.httpjava.services.config.DataServiceType;
import org.stuartaroth.httpjava.services.file.FileService;
import org.stuartaroth.httpjava.services.id.IdService;
import org.stuartaroth.httpjava.services.id.UniqueIdService;
import org.stuartaroth.httpjava.services.json.JsonService;

public class DataServiceFactory {
    private static Logger logger = LoggerFactory.getLogger(DataServiceFactory.class);

    private FileService fileService;
    private JsonService jsonService;
    private ConfigService configService;

    public DataServiceFactory(FileService fileService, JsonService jsonService, ConfigService configService) {
        this.fileService = fileService;
        this.jsonService = jsonService;
        this.configService = configService;
    }

    public DataService getDataService() throws Exception {
        DataServiceType dataServiceType = configService.getDataServiceType();
        if (dataServiceType == null) {
            logger.info("dataServiceType was null, setting to IN_MEMORY");
            dataServiceType = DataServiceType.IN_MEMORY;
        }

        IdService idService = new UniqueIdService();

        DataService inMemoryDataService = new InMemoryDataService(idService);
        inMemoryDataService.setupData(null);

        switch (dataServiceType) {
            case IN_MEMORY:
                return inMemoryDataService;
            case MYSQL:
                DataService mysqlDataService = new MysqlDataService(configService.getMysqlConfig());
                String mysqlSchema = fileService.readFileFromResources("mysql_schema.sql");
                mysqlDataService.setupData(mysqlSchema);
                return mysqlDataService;
            case REDIS:
                DataService redisDataService = new RedisDataService(configService.getRedisConfig(), jsonService, idService, inMemoryDataService);
                redisDataService.setupData(null);
                return redisDataService;
            default:
                throw new Exception(String.format("Unknown dataServiceType: %s", dataServiceType));
        }
    }
}
