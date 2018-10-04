package org.stuartaroth.httpjava.services.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stuartaroth.httpjava.models.Author;
import org.stuartaroth.httpjava.models.Book;
import org.stuartaroth.httpjava.models.Genre;
import org.stuartaroth.httpjava.services.config.MysqlConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MysqlDataService implements DataService {
    private static Logger logger = LoggerFactory.getLogger(MysqlDataService.class);

    private String host;
    private String database;
    private String username;
    private String password;
    private Connection connection;

    public MysqlDataService(MysqlConfig mysqlConfig) throws Exception {
        host = mysqlConfig.getHost();
        database = mysqlConfig.getDatabase();
        username = mysqlConfig.getUsername();
        password = mysqlConfig.getPassword();
    }

    private void ensureConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(String.format("jdbc:mariadb://%s:3306/%s?user=%s&password=%s", host, database, username, password));
        }
    }

    @Override
    public void setupData(String data) throws Exception {
        connection = DriverManager.getConnection(String.format("jdbc:mariadb://%s:3306/?user=%s&password=%s", host, username, password));
        List<String> commands = Arrays.asList(data.split(";")).stream().map(String::trim).filter(s -> !s.isEmpty()).collect(Collectors.toList());

        for (String command : commands) {
            Statement statement = connection.createStatement();
            statement.execute(command);
        }

        connection.close();
        connection = null;
    }

    private Genre getGenre(ResultSet resultSet) throws Exception {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        return new Genre(
                String.format("%s", id),
                name
        );
    }

    private Author getAuthor(ResultSet resultSet) throws Exception {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        return new Author(
                String.format("%s", id),
                name
        );
    }

    private Book getBook(ResultSet resultSet) throws Exception {
        int id = resultSet.getInt("id");
        int authorId = resultSet.getInt("author_id");
        int genreId = resultSet.getInt("genre_id");
        String name = resultSet.getString("name");
        return new Book(
                String.format("%s", id),
                String.format("%s", authorId),
                String.format("%s", genreId),
                name
        );
    }

    @Override
    public List<Genre> getGenres() throws Exception {
        ensureConnection();

        List<Genre> genres = new ArrayList<>();

        PreparedStatement statement = connection.prepareStatement("select id, name from genres;");
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            Genre genre = getGenre(resultSet);
            genres.add(genre);
        }

        return genres;
    }

    @Override
    public Genre getGenreByGenreId(String genreId) throws Exception {
        ensureConnection();

        List<Genre> genres = new ArrayList<>();

        PreparedStatement statement = connection.prepareStatement("select id, name from genres where id = ?;");
        statement.setInt(1, Integer.parseInt(genreId));
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            Genre genre = getGenre(resultSet);
            genres.add(genre);
        }

        if (genres.isEmpty()) {
            return null;
        } else {
            return genres.get(0);
        }
    }

    @Override
    public Genre createGenre(Genre genre) throws Exception {
        ensureConnection();

        if (!genre.isValid()) {
            throw new Exception("passed `genre` was invalid");
        }

        PreparedStatement statement = connection.prepareStatement("insert into genres (name) values (?);", Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, genre.getName());
        int updatedRows = statement.executeUpdate();
        int id = 0;

        ResultSet resultSet = statement.getGeneratedKeys();
        if (resultSet.next()) {
            id = resultSet.getInt(1);
        }

        if (id == 0) {
            throw new Exception(String.format("Unable to create `genre` with name: %s", genre.getName()));
        }

        return getGenreByGenreId(String.format("%s", id));
    }

    @Override
    public Genre updateGenre(Genre genre) throws Exception {
        ensureConnection();

        if (!genre.isValid() || genre.getId() == null) {
            throw new Exception("passed `genre` was invalid");
        }

        PreparedStatement statement = connection.prepareStatement("update genres set name = ? where id = ?;");
        statement.setString(1, genre.getName());
        statement.setInt(2, Integer.parseInt(genre.getId()));
        int updatedRows = statement.executeUpdate();

        if (updatedRows != 1) {
            throw new Exception(String.format("Unable to update `genre` with id: %s to name: %s", genre.getId(), genre.getName()));
        }

        return getGenreByGenreId(genre.getId());
    }

    @Override
    public void deleteGenre(String genreId) throws Exception {
        ensureConnection();

        PreparedStatement statement = connection.prepareStatement("delete from genres where id = ?;");
        statement.setInt(1, Integer.parseInt(genreId));
        int updatedRows = statement.executeUpdate();

        if (updatedRows != 1) {
            throw new Exception(String.format("Unable to delete `genre` with id: %s", genreId));
        }
    }

    @Override
    public List<Author> getAuthors() throws Exception {
        ensureConnection();

        List<Author> authors = new ArrayList<>();

        PreparedStatement statement = connection.prepareStatement("select id, name from authors;");
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            Author author = getAuthor(resultSet);
            authors.add(author);
        }

        return authors;
    }

    @Override
    public Author getAuthorByAuthorId(String authorId) throws Exception {
        ensureConnection();

        List<Author> authors = new ArrayList<>();

        PreparedStatement statement = connection.prepareStatement("select id, name from authors where id = ?;");
        statement.setInt(1, Integer.parseInt(authorId));
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            Author author = getAuthor(resultSet);
            authors.add(author);
        }

        if (authors.isEmpty()) {
            return null;
        } else {
            return authors.get(0);
        }
    }

    @Override
    public Author createAuthor(Author author) throws Exception {
        ensureConnection();

        if (!author.isValid()) {
            throw new Exception("passed `author` was invalid");
        }

        PreparedStatement statement = connection.prepareStatement("insert into authors (name) values (?);", Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, author.getName());
        int updatedRows = statement.executeUpdate();
        int id = 0;

        ResultSet resultSet = statement.getGeneratedKeys();
        if (resultSet.next()) {
            id = resultSet.getInt(1);
        }

        if (id == 0) {
            throw new Exception(String.format("Unable to create `author` with name: %s", author.getName()));
        }

        return getAuthorByAuthorId(String.format("%s", id));
    }

    @Override
    public Author updateAuthor(Author author) throws Exception {
        ensureConnection();

        if (!author.isValid() || author.getId() == null) {
            throw new Exception("passed `author` was invalid");
        }

        PreparedStatement statement = connection.prepareStatement("update authors set name = ? where id = ?;");
        statement.setString(1, author.getName());
        statement.setInt(2, Integer.parseInt(author.getId()));
        int updatedRows = statement.executeUpdate();

        if (updatedRows != 1) {
            throw new Exception(String.format("Unable to update `author` with id: %s to name: %s", author.getId(), author.getName()));
        }

        return getAuthorByAuthorId(author.getId());
    }

    @Override
    public void deleteAuthor(String authorId) throws Exception {
        ensureConnection();

        PreparedStatement statement = connection.prepareStatement("delete from authors where id = ?;");
        statement.setInt(1, Integer.parseInt(authorId));
        int updatedRows = statement.executeUpdate();

        if (updatedRows != 1) {
            throw new Exception(String.format("Unable to delete `author` with id: %s", authorId));
        }
    }

    @Override
    public List<Book> getBooks() throws Exception {
        ensureConnection();

        List<Book> books = new ArrayList<>();

        PreparedStatement statement = connection.prepareStatement("select id, author_id, genre_id, name from books;");
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            Book book = getBook(resultSet);
            books.add(book);
        }

        return books;
    }

    @Override
    public Book getBookByBookId(String bookId) throws Exception {
        ensureConnection();

        List<Book> books = new ArrayList<>();

        PreparedStatement statement = connection.prepareStatement("select id, author_id, genre_id, name from books where id = ?;");
        statement.setInt(1, Integer.parseInt(bookId));
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            Book book = getBook(resultSet);
            books.add(book);
        }

        if (books.isEmpty()) {
            return null;
        } else {
            return books.get(0);
        }
    }

    @Override
    public Book createBook(Book book) throws Exception {
        ensureConnection();

        if (!book.isValid()) {
            throw new Exception("passed `book` was invalid");
        }

        PreparedStatement statement = connection.prepareStatement("insert into books (author_id, genre_id, name) values (?, ?, ?);", Statement.RETURN_GENERATED_KEYS);
        statement.setInt(1, Integer.parseInt(book.getAuthorId()));
        statement.setInt(2, Integer.parseInt(book.getGenreId()));
        statement.setString(3, book.getName());
        int updatedRows = statement.executeUpdate();
        int id = 0;

        ResultSet resultSet = statement.getGeneratedKeys();
        if (resultSet.next()) {
            id = resultSet.getInt(1);
        }

        if (id == 0) {
            throw new Exception(String.format("Unable to create `author` with name: %s", book.getName()));
        }

        return getBookByBookId(String.format("%s", id));
    }

    @Override
    public Book updateBook(Book book) throws Exception {
        ensureConnection();

        if (!book.isValid() || book.getId() == null) {
            throw new Exception("passed `book` was invalid");
        }

        PreparedStatement statement = connection.prepareStatement("update books set author_id = ?, genre_id = ?, name = ? where id = ?;");
        statement.setInt(1, Integer.parseInt(book.getAuthorId()));
        statement.setInt(2, Integer.parseInt(book.getGenreId()));
        statement.setString(3, book.getName());
        statement.setInt(4, Integer.parseInt(book.getId()));
        int updatedRows = statement.executeUpdate();

        if (updatedRows != 1) {
            throw new Exception(String.format("Unable to update `book` with id: %s to name: %s", book.getId(), book.getName()));
        }

        return getBookByBookId(book.getId());
    }

    @Override
    public void deleteBook(String bookId) throws Exception {
        ensureConnection();

        PreparedStatement statement = connection.prepareStatement("delete from books where id = ?;");
        statement.setInt(1, Integer.parseInt(bookId));
        int updatedRows = statement.executeUpdate();

        if (updatedRows != 1) {
            throw new Exception(String.format("Unable to delete `book` with id: %s", bookId));
        }
    }

    @Override
    public List<Book> getBooksByAuthorId(String authorId) throws Exception {
        ensureConnection();

        List<Book> books = new ArrayList<>();

        PreparedStatement statement = connection.prepareStatement("select id, author_id, genre_id, name from books where author_id = ?;");
        statement.setInt(1, Integer.parseInt(authorId));
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            Book book = getBook(resultSet);
            books.add(book);
        }

        return books;
    }

    @Override
    public List<Book> getBooksByGenreId(String genreId) throws Exception {
        ensureConnection();

        List<Book> books = new ArrayList<>();

        PreparedStatement statement = connection.prepareStatement("select id, author_id, genre_id, name from books where genre_id = ?;");
        statement.setInt(1, Integer.parseInt(genreId));
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            Book book = getBook(resultSet);
            books.add(book);
        }

        return books;
    }
}
