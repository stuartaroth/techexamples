package org.stuartaroth.httpjava.services.data;

import org.stuartaroth.httpjava.models.*;
import org.stuartaroth.httpjava.services.id.IdService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class InMemoryDataService implements DataService {
    private IdService idService;

    private List<Genre> genres;
    private List<Author> authors;
    private List<Book> books;

    public InMemoryDataService(IdService idService) {
        this.idService = idService;
        genres = new ArrayList<>();
        authors = new ArrayList<>();
        books = new ArrayList<>();
    }

    @Override
    public void setupData(String data) throws Exception {
        String horrorId = idService.generateId();
        String scifiId = idService.generateId();
        String fantasyId = idService.generateId();

        genres.addAll(Arrays.asList(
                new Genre(horrorId, "Horror"),
                new Genre(scifiId, "Science Fiction"),
                new Genre(fantasyId, "Fantasy")
        ));

        String shellyId = idService.generateId();
        String wellsId = idService.generateId();
        String tolkienId = idService.generateId();
        String kingId = idService.generateId();
        String dickId = idService.generateId();
        String martinId = idService.generateId();

        authors.addAll(Arrays.asList(
                new Author(shellyId, "Mary Shelley"),
                new Author(wellsId, "H.G. Wells"),
                new Author(tolkienId, "J.R.R. Tolkien"),
                new Author(kingId, "Stephen King"),
                new Author(dickId, "Philip K. Dick"),
                new Author(martinId, "George R.R. Martin")
        ));

        books.addAll(Arrays.asList(
                new Book(idService.generateId(), shellyId, horrorId, "Frankenstein"),
                new Book(idService.generateId(), wellsId, scifiId, "The Time Machine"),
                new Book(idService.generateId(), wellsId, scifiId, "The War of the Worlds"),
                new Book(idService.generateId(), wellsId, scifiId, "The Invisible Man"),
                new Book(idService.generateId(), tolkienId, fantasyId, "The Fellowship of the Ring"),
                new Book(idService.generateId(), tolkienId, fantasyId, "The Two Towers"),
                new Book(idService.generateId(), tolkienId, fantasyId, "The Return of the King"),
                new Book(idService.generateId(), kingId, horrorId, "It"),
                new Book(idService.generateId(), kingId, horrorId, "The Shining"),
                new Book(idService.generateId(), kingId, fantasyId, "The Gunslinger"),
                new Book(idService.generateId(), dickId, scifiId, "Do Androids Dream of Electric Sheep?"),
                new Book(idService.generateId(), dickId, scifiId, "A Scanner Darkly"),
                new Book(idService.generateId(), martinId, fantasyId, "A Game of Thrones"),
                new Book(idService.generateId(), martinId, fantasyId, "A Clash of Kings"),
                new Book(idService.generateId(), martinId, fantasyId, "A Storm of Swords"),
                new Book(idService.generateId(), martinId, fantasyId, "A Feast for Crows"),
                new Book(idService.generateId(), martinId, fantasyId, "A Dance with Dragons"),
                new Book(idService.generateId(), martinId, scifiId, "Nightflyers")
        ));
    }

    @Override
    public List<Genre> getGenres() throws Exception {
        return genres;
    }

    @Override
    public Genre getGenreByGenreId(String genreId) throws Exception {
        return genres.stream().filter(g -> g.getId().equals(genreId)).findFirst().orElse(null);
    }

    @Override
    public Genre createGenre(Genre genre) throws Exception {
        Genre createdGenre = new Genre(idService.generateId(), genre.getName());
        genres.add(createdGenre);
        return createdGenre;
    }

    @Override
    public Genre updateGenre(Genre genre) throws Exception {
        for (Genre g : genres) {
            if (g.getId().equals(genre.getId())) {
                g.setName(genre.getName());
                return g;
            }
        }

        throw new Exception("No `genre` found by passed id");
    }

    @Override
    public void deleteGenre(String genreId) throws Exception {
        genres = genres.stream().filter(g -> !g.getId().equals(genreId)).collect(Collectors.toList());
    }

    @Override
    public List<Author> getAuthors() throws Exception {
        return authors;
    }

    @Override
    public Author getAuthorByAuthorId(String authorId) throws Exception {
        return authors.stream().filter(a -> a.getId().equals(authorId)).findFirst().orElse(null);
    }

    @Override
    public Author createAuthor(Author author) throws Exception {
        Author createdAuthor = new Author(idService.generateId(), author.getName());
        authors.add(createdAuthor);
        return createdAuthor;
    }

    @Override
    public Author updateAuthor(Author author) throws Exception {
        for (Author a : authors) {
            if (a.getId().equals(author.getId())) {
                a.setName(author.getName());
                return a;
            }
        }

        throw new Exception("No `author` found by passed id");
    }

    @Override
    public void deleteAuthor(String authorId) throws Exception {
        authors = authors.stream().filter(a -> !a.getId().equals(authorId)).collect(Collectors.toList());
    }

    @Override
    public List<Book> getBooks() throws Exception {
        return books;
    }

    @Override
    public Book getBookByBookId(String bookId) throws Exception {
        return books.stream().filter(b -> b.getId().equals(bookId)).findFirst().orElse(null);
    }

    @Override
    public Book createBook(Book book) throws Exception {
        Book createdBook = new Book(idService.generateId(), book.getAuthorId(), book.getGenreId(), book.getName());
        books.add(createdBook);
        return createdBook;
    }

    @Override
    public Book updateBook(Book book) throws Exception {
        for (Book b : books) {
            if (b.getId().equals(book.getId())) {
                b.setAuthorId(book.getAuthorId());
                b.setGenreId(book.getGenreId());
                b.setName(book.getName());
                return b;
            }
        }

        throw new Exception("No `book` found by passed id");
    }

    @Override
    public void deleteBook(String bookId) throws Exception {
        books = books.stream().filter(b -> !b.getId().equals(bookId)).collect(Collectors.toList());
    }

    @Override
    public List<Book> getBooksByAuthorId(String authorId) throws Exception {
        return books.stream().filter(b -> b.getAuthorId().equals(authorId)).collect(Collectors.toList());
    }

    @Override
    public List<Book> getBooksByGenreId(String genreId) throws Exception {
        return books.stream().filter(b -> b.getGenreId().equals(genreId)).collect(Collectors.toList());
    }
}
