package org.stuartaroth.httpjava.services.data;

import org.stuartaroth.httpjava.models.*;

import java.util.List;

public interface DataService {
    void setupData(String data) throws Exception;

    List<Genre> getGenres() throws Exception;
    Genre getGenreByGenreId(String genreId) throws Exception;
    Genre createGenre(Genre genre) throws Exception;
    Genre updateGenre(Genre genre) throws Exception;
    void deleteGenre(String genreId) throws Exception;

    List<Author> getAuthors() throws Exception;
    Author getAuthorByAuthorId(String authorId) throws Exception;
    Author createAuthor(Author author) throws Exception;
    Author updateAuthor(Author author) throws Exception;
    void deleteAuthor(String authorId) throws Exception;

    List<Book> getBooks() throws Exception;
    Book getBookByBookId(String bookId) throws Exception;
    Book createBook(Book book) throws Exception;
    Book updateBook(Book book) throws Exception;
    void deleteBook(String bookId) throws Exception;

    List<Book> getBooksByAuthorId(String authorId) throws Exception;
    List<Book> getBooksByGenreId(String genreId) throws Exception;
}
