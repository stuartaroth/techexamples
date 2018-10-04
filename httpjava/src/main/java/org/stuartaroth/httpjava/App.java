package org.stuartaroth.httpjava;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stuartaroth.httpjava.routes.AuthorRoute;
import org.stuartaroth.httpjava.routes.BookRoute;
import org.stuartaroth.httpjava.routes.GenreRoute;
import org.stuartaroth.httpjava.services.config.ConfigService;
import org.stuartaroth.httpjava.services.config.JsonFileConfigService;
import org.stuartaroth.httpjava.services.data.DataService;
import org.stuartaroth.httpjava.services.data.DataServiceFactory;
import org.stuartaroth.httpjava.services.file.FileService;
import org.stuartaroth.httpjava.services.file.DefaultFileService;
import org.stuartaroth.httpjava.services.json.DefaultJsonService;
import org.stuartaroth.httpjava.services.json.JsonService;

import static spark.Spark.*;

public class App {
    private static Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        try {
            FileService fileService = new DefaultFileService();
            JsonService jsonService = new DefaultJsonService();

            String configJson = fileService.readFileFromResources("config.json");
            ConfigService configService = jsonService.read(configJson, JsonFileConfigService.class);

            DataServiceFactory dataServiceFactory = new DataServiceFactory(fileService, jsonService, configService);
            DataService dataService = dataServiceFactory.getDataService();

            GenreRoute genresHandler = new GenreRoute(jsonService, dataService);
            AuthorRoute authorsHandler = new AuthorRoute(jsonService, dataService);
            BookRoute booksHandler = new BookRoute(jsonService, dataService);

            ipAddress("127.0.0.1");
            port(8444);

            path("/genres", () -> {
                get("", genresHandler::get);
                post("", genresHandler::post);
                put("", genresHandler::put);
                delete("", genresHandler::delete);
            });

            path("/authors", () -> {
                get("", authorsHandler::get);
                post("", authorsHandler::post);
                put("", authorsHandler::put);
                delete("", authorsHandler::delete);
            });

            path("/books", () -> {
                get("", booksHandler::get);
                post("", booksHandler::post);
                put("", booksHandler::put);
                delete("", booksHandler::delete);
            });
        } catch (Exception e) {
            logger.error("Exception in main:", e);
        }
    }
}
