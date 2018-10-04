using System;
using System.Collections.Generic;
using httpcsharp.models;

namespace httpcsharp.services.data
{
    public interface IDataService
    {
        void SetupData(string data);

        IEnumerable<Genre> GetGenres();
        Genre GetGenreByGenreId(string genreId);
        Genre CreateGenre(Genre genre);
        Genre UpdateGenre(Genre genre);
        void DeleteGenre(String genreId);

        IEnumerable<Author> GetAuthors();
        Author GetAuthorByAuthorId(string authorId);
        Author CreateAuthor(Author author);
        Author UpdateAuthor(Author author);
        void DeleteAuthor(string authorId);

        IEnumerable<Book> GetBooks();
        Book GetBookByBookId(string bookId);
        Book CreateBook(Book book);
        Book UpdateBook(Book book);
        void DeleteBook(String bookId);

        IEnumerable<Book> GetBooksByAuthorId(string authorId);
        IEnumerable<Book> GetBooksByGenreId(string genreId);
    }
}