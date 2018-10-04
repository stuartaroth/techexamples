package org.stuartaroth.httpjava.routes;

import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stuartaroth.httpjava.models.Book;
import org.stuartaroth.httpjava.models.ErrorMessage;
import org.stuartaroth.httpjava.models.SuccessMessage;
import org.stuartaroth.httpjava.services.data.DataService;
import org.stuartaroth.httpjava.services.json.JsonService;
import spark.Request;
import spark.Response;

import java.util.Set;

public class BookRoute {
    private static Logger logger = LoggerFactory.getLogger(BookRoute.class);

    private JsonService jsonService;
    private DataService dataService;

    public BookRoute(JsonService jsonService, DataService dataService) {
        this.jsonService = jsonService;
        this.dataService = dataService;
    }

    public Object get(Request request, Response response) throws Exception {
        response.type("application/json");

        try {
            Set<String> queryParams = request.queryParams();
            if (queryParams.contains("id")) {
                String id = request.queryMap("id").value();
                return jsonService.write(dataService.getBookByBookId(id));
            } else if (queryParams.contains("author-id")) {
                String authorId = request.queryMap("author-id").value();
                return jsonService.write(dataService.getBooksByAuthorId(authorId));
            } else if (queryParams.contains("genre-id")) {
                String genreId = request.queryMap("genre-id").value();
                return jsonService.write(dataService.getBooksByGenreId(genreId));
            } else {
                return jsonService.write(dataService.getBooks());
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
            Book book = jsonService.read(request.body(), Book.class);
            Book createdBook = dataService.createBook(book);
            return jsonService.write(createdBook);
        } catch (Exception e) {
            logger.error("Exception:", e);
            response.status(HttpStatus.INTERNAL_SERVER_ERROR_500);
            return jsonService.write(new ErrorMessage(e.getMessage()));
        }
    }

    public Object put(Request request, Response response) throws Exception {
        response.type("application/json");
        try {
            Book book = jsonService.read(request.body(), Book.class);
            Book createdBook = dataService.updateBook(book);
            return jsonService.write(createdBook);
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
                dataService.deleteBook(id);
                return jsonService.write(new SuccessMessage(String.format("Deleted `book` with id: %s", id)));
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
