package org.stuartaroth.httpjava.routes;

import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stuartaroth.httpjava.models.Author;
import org.stuartaroth.httpjava.models.ErrorMessage;
import org.stuartaroth.httpjava.models.SuccessMessage;
import org.stuartaroth.httpjava.services.data.DataService;
import org.stuartaroth.httpjava.services.json.JsonService;
import spark.Request;
import spark.Response;

import java.util.Set;

public class AuthorRoute {
    private static Logger logger = LoggerFactory.getLogger(AuthorRoute.class);

    private JsonService jsonService;
    private DataService dataService;

    public AuthorRoute(JsonService jsonService, DataService dataService) {
        this.jsonService = jsonService;
        this.dataService = dataService;
    }

    public Object get(Request request, Response response) throws Exception {
        response.type("application/json");
        try {
            Set<String> queryParams = request.queryParams();

            if (queryParams.contains("id")) {
                String id = request.queryMap("id").value();
                return jsonService.write(dataService.getAuthorByAuthorId(id));
            } else {
                return jsonService.write(dataService.getAuthors());
            }

        } catch (Exception e) {
            logger.error("Exception:", e);
            response.status(HttpStatus.INTERNAL_SERVER_ERROR_500);
            return jsonService.write(new ErrorMessage(e.getMessage()));
        }
    }

    public Object post(Request request, Response response) throws Exception {
        response.type("application/json");
        try {
            Author author = jsonService.read(request.body(), Author.class);
            Author createdAuthor = dataService.createAuthor(author);
            return jsonService.write(createdAuthor);
        } catch (Exception e) {
            logger.error("Exception:", e);
            response.status(HttpStatus.INTERNAL_SERVER_ERROR_500);
            return jsonService.write(new ErrorMessage(e.getMessage()));
        }
    }

    public Object put(Request request, Response response) throws Exception {
        response.type("application/json");
        try {
            Author author = jsonService.read(request.body(), Author.class);
            Author createdAuthor = dataService.updateAuthor(author);
            return jsonService.write(createdAuthor);
        } catch (Exception e) {
            logger.error("Exception:", e);
            response.status(HttpStatus.INTERNAL_SERVER_ERROR_500);
            return jsonService.write(new ErrorMessage(e.getMessage()));
        }
    }

    public Object delete(Request request, Response response) throws Exception {
        response.type("application/json");
        try {
            Set<String> queryParams = request.queryParams();

            if (queryParams.contains("id")) {
                String id = request.queryMap("id").value();
                dataService.deleteAuthor(id);
                return jsonService.write(new SuccessMessage(String.format("Deleted `author` with id: %s", id)));
            } else {
                response.status(HttpStatus.UNPROCESSABLE_ENTITY_422);
                return jsonService.write(new ErrorMessage("You must provide an `id`"));
            }
        } catch (Exception e) {
            logger.error("Exception:", e);
            response.status(HttpStatus.INTERNAL_SERVER_ERROR_500);
            return jsonService.write(new ErrorMessage(e.getMessage()));
        }
    }
}
