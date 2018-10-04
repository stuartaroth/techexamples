package org.stuartaroth.httpjava.services.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stuartaroth.httpjava.models.Author;
import org.stuartaroth.httpjava.models.Book;
import org.stuartaroth.httpjava.models.Genre;
import org.stuartaroth.httpjava.services.config.RedisConfig;
import org.stuartaroth.httpjava.services.id.IdService;
import org.stuartaroth.httpjava.services.json.JsonService;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.*;

public class RedisDataService implements DataService {
    private static Logger logger = LoggerFactory.getLogger(RedisDataService.class);
    private static String GenrePrefix = "GENRE-";
    private static String AuthorPrefix = "AUTHOR-";
    private static String BookPrefix = "BOOK-";


    private RedisConfig redisConfig;
    private JsonService jsonService;
    private IdService idService;
    private DataService setupDataService;
    private JedisPool jedisPool;

    public RedisDataService(RedisConfig redisConfig, JsonService jsonService, IdService idService, DataService setupDataService) {
        this.redisConfig = redisConfig;
        this.jsonService = jsonService;
        this.idService = idService;
        this.setupDataService = setupDataService;
        this.jedisPool = new JedisPool(redisConfig.getHost(), redisConfig.getPort());
    }

    @Override
    public void setupData(String data) throws Exception {
        Jedis jedis = jedisPool.getResource();

        Set<String> keys = jedis.keys("*");
        for (String key : keys) {
            jedis.del(key);
        }

        jedis.close();

        Map<String, String> oldGenreIdToNewGenreId = new HashMap<>();
        List<Genre> genres = setupDataService.getGenres();
        for (Genre g : genres) {
            Genre createdGenre = createGenre(g);
            oldGenreIdToNewGenreId.put(g.getId(), createdGenre.getId());
        }

        Map<String, String> oldAuthorIdToNewAuthorId = new HashMap<>();
        List<Author> authors = setupDataService.getAuthors();
        for (Author a : authors) {
            Author createdAuthor = createAuthor(a);
            oldAuthorIdToNewAuthorId.put(a.getId(), createdAuthor.getId());
        }

        List<Book> books = setupDataService.getBooks();
        for (Book b : books) {
            String newGenreId = oldGenreIdToNewGenreId.get(b.getGenreId());
            String newAuthorId = oldAuthorIdToNewAuthorId.get(b.getAuthorId());
            b.setGenreId(newGenreId);
            b.setAuthorId(newAuthorId);
            createBook(b);
        }
    }

    @Override
    public List<Genre> getGenres() throws Exception {
        List<Genre> genres = new ArrayList<>();

        Jedis jedis = jedisPool.getResource();
        Set<String> keys = jedis.keys(GenrePrefix + "*");
        jedis.close();

        for (String key : keys) {
            Genre g = getGenreByGenreId(key);
            genres.add(g);
        }

        return genres;
    }

    @Override
    public Genre getGenreByGenreId(String genreId) throws Exception {
        Jedis jedis = jedisPool.getResource();

        String json = jedis.get(genreId);
        if (json == null) {
            return null;
        }

        jedis.close();

        return jsonService.read(json, Genre.class);
    }

    @Override
    public Genre createGenre(Genre genre) throws Exception {
        Jedis jedis = jedisPool.getResource();
        Genre g = new Genre(idService.generateId(GenrePrefix), genre.getName());
        jedis.set(g.getId(), jsonService.write(g));
        jedis.close();
        return getGenreByGenreId(g.getId());
    }

    @Override
    public Genre updateGenre(Genre genre) throws Exception {
        Jedis jedis = jedisPool.getResource();

        if (!genre.isValid() || genre.getId() == null) {
            throw new Exception("passed `genre` was invalid");
        }

        Genre storedGenre = getGenreByGenreId(genre.getId());
        if (storedGenre == null) {
            throw new Exception(String.format("Unable to update `genre` with id: %s to name: %s", genre.getId(), genre.getName()));
        }

        storedGenre.setName(genre.getName());
        jedis.set(storedGenre.getId(), jsonService.write(storedGenre));
        jedis.close();

        return getGenreByGenreId(storedGenre.getId());
    }

    @Override
    public void deleteGenre(String genreId) throws Exception {
        Jedis jedis = jedisPool.getResource();
        long keysRemoved = jedis.del(genreId);
        jedis.close();
        if (keysRemoved != 1) {
            throw new Exception(String.format("Unable to delete `genre` with id: %s", genreId));
        }
    }

    @Override
    public List<Author> getAuthors() throws Exception {
        List<Author> authors = new ArrayList<>();

        Jedis jedis = jedisPool.getResource();
        Set<String> keys = jedis.keys(AuthorPrefix + "*");
        jedis.close();

        for (String key : keys) {
            Author a = getAuthorByAuthorId(key);
            authors.add(a);
        }

        return authors;
    }

    @Override
    public Author getAuthorByAuthorId(String authorId) throws Exception {
        Jedis jedis = jedisPool.getResource();
        String json = jedis.get(authorId);
        jedis.close();

        if (json == null) {
            return null;
        }

        return jsonService.read(json, Author.class);
    }

    @Override
    public Author createAuthor(Author author) throws Exception {
        Jedis jedis = jedisPool.getResource();
        Author a = new Author(idService.generateId(AuthorPrefix), author.getName());
        jedis.set(a.getId(), jsonService.write(a));
        jedis.close();
        return getAuthorByAuthorId(a.getId());
    }

    @Override
    public Author updateAuthor(Author author) throws Exception {
        Jedis jedis = jedisPool.getResource();

        if (!author.isValid() || author.getId() == null) {
            throw new Exception("passed `author` was invalid");
        }

        Author storedAuthor = getAuthorByAuthorId(author.getId());
        if (storedAuthor == null) {
            throw new Exception(String.format("Unable to update `author` with id: %s to name: %s", author.getId(), author.getName()));
        }

        storedAuthor.setName(author.getName());
        jedis.set(storedAuthor.getId(), jsonService.write(storedAuthor));
        jedis.close();
        return getAuthorByAuthorId(storedAuthor.getId());
    }

    @Override
    public void deleteAuthor(String authorId) throws Exception {
        Jedis jedis = jedisPool.getResource();
        long keysRemoved = jedis.del(authorId);
        jedis.close();
        if (keysRemoved != 1) {
            throw new Exception(String.format("Unable to delete `author` with id: %s", authorId));
        }
    }

    @Override
    public List<Book> getBooks() throws Exception {
        return getBooksByPattern(BookPrefix + "*");
    }

    @Override
    public Book getBookByBookId(String bookId) throws Exception {
        Jedis jedis = jedisPool.getResource();
        String json = jedis.get(bookId);
        jedis.close();

        if (json == null) {
            return null;
        }


        return jsonService.read(json, Book.class);
    }

    private Book getBookWithUpdatedId(Book book) throws Exception {
        String id = idService.generateId(BookPrefix) + "-" + book.getAuthorId() + "-" + book.getGenreId();
        return new Book(id, book.getAuthorId(), book.getGenreId(), book.getName());
    }

    @Override
    public Book createBook(Book book) throws Exception {
        Jedis jedis = jedisPool.getResource();
        Book b = getBookWithUpdatedId(book);
        jedis.set(b.getId(), jsonService.write(b));
        jedis.close();
        return getBookByBookId(b.getId());
    }

    @Override
    public Book updateBook(Book book) throws Exception {
        Jedis jedis = jedisPool.getResource();

        if (!book.isValid() || book.getId() == null) {
            throw new Exception("passed `book` was invalid");
        }

        Book storedBook = getBookByBookId(book.getId());
        if (storedBook == null) {
            throw new Exception(String.format("Unable to update `book` with id: %s to name: %s", book.getId(), book.getName()));
        }

        storedBook.setGenreId(book.getGenreId());
        storedBook.setAuthorId(book.getAuthorId());
        storedBook.setName(book.getName());

        Book b = getBookWithUpdatedId(storedBook);
        jedis.set(b.getId(), jsonService.write(b));
        jedis.close();
        deleteBook(storedBook.getId());

        return getBookByBookId(b.getId());
    }

    @Override
    public void deleteBook(String bookId) throws Exception {
        Jedis jedis = jedisPool.getResource();
        long keysRemoved = jedis.del(bookId);
        jedis.close();
        if (keysRemoved != 1) {
            throw new Exception(String.format("Unable to delete `book` with id: %s", bookId));
        }
    }

    @Override
    public List<Book> getBooksByAuthorId(String authorId) throws Exception {
        return getBooksByPattern(BookPrefix + "*" + authorId + "*");
    }

    @Override
    public List<Book> getBooksByGenreId(String genreId) throws Exception {
        return getBooksByPattern(BookPrefix + "*" + genreId + "*");
    }

    private List<Book> getBooksByPattern(String pattern) throws Exception {
        List<Book> books = new ArrayList<>();

        Jedis jedis = jedisPool.getResource();
        Set<String> keys = jedis.keys(pattern);
        jedis.close();

        for (String key : keys) {
            Book b = getBookByBookId(key);
            books.add(b);
        }

        return books;
    }
}
