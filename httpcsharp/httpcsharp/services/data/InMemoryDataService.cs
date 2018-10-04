using System.Collections.Generic;
using System.Linq;
using httpcsharp.models;
using httpcsharp.services.id;

namespace httpcsharp.services.data
{
    public class InMemoryDataService : IDataService
    {
        private readonly IIdService _idService;
        private List<Genre> _genres;
        private List<Author> _authors;
        private List<Book> _books;

        public InMemoryDataService(IIdService idService)
        {
            _idService = idService;
            _genres = new List<Genre>();
            _authors = new List<Author>();
            _books = new List<Book>();
        }

        public void SetupData(string data)
        {
            var horrorId = _idService.GenerateId();
            var scifiId = _idService.GenerateId();
            var fantasyId = _idService.GenerateId();
            
            _genres = new List<Genre>()
            {
                new Genre(horrorId, "Horror"),
                new Genre(scifiId, "Science Fiction"),
                new Genre(fantasyId, "Fantasy")
            };
         
            var shellyId = _idService.GenerateId();
            var wellsId = _idService.GenerateId();
            var tolkienId = _idService.GenerateId();
            var kingId = _idService.GenerateId();
            var dickId = _idService.GenerateId();
            var martinId = _idService.GenerateId();

            _authors = new List<Author>()
            {
                new Author(shellyId, "Mary Shelley"),
                new Author(wellsId, "H.G. Wells"),
                new Author(tolkienId, "J.R.R. Tolkien"),
                new Author(kingId, "Stephen King"),
                new Author(dickId, "Philip K. Dick"),
                new Author(martinId, "George R.R. Martin")
            };

            _books = new List<Book>() {
                new Book(_idService.GenerateId(), shellyId, horrorId, "Frankenstein"),
                new Book(_idService.GenerateId(), wellsId, scifiId, "The Time Machine"),
                new Book(_idService.GenerateId(), wellsId, scifiId, "The War of the Worlds"),
                new Book(_idService.GenerateId(), wellsId, scifiId, "The Invisible Man"),
                new Book(_idService.GenerateId(), tolkienId, fantasyId, "The Fellowship of the Ring"),
                new Book(_idService.GenerateId(), tolkienId, fantasyId, "The Two Towers"),
                new Book(_idService.GenerateId(), tolkienId, fantasyId, "The Return of the King"),
                new Book(_idService.GenerateId(), kingId, horrorId, "It"),
                new Book(_idService.GenerateId(), kingId, horrorId, "The Shining"),
                new Book(_idService.GenerateId(), kingId, fantasyId, "The Gunslinger"),
                new Book(_idService.GenerateId(), dickId, scifiId, "Do Androids Dream of Electric Sheep?"),
                new Book(_idService.GenerateId(), dickId, scifiId, "A Scanner Darkly"),
                new Book(_idService.GenerateId(), martinId, fantasyId, "A Game of Thrones"),
                new Book(_idService.GenerateId(), martinId, fantasyId, "A Clash of Kings"),
                new Book(_idService.GenerateId(), martinId, fantasyId, "A Storm of Swords"),
                new Book(_idService.GenerateId(), martinId, fantasyId, "A Feast for Crows"),
                new Book(_idService.GenerateId(), martinId, fantasyId, "A Dance with Dragons"),
                new Book(_idService.GenerateId(), martinId, scifiId, "Nightflyers")
            };
        }

        public IEnumerable<Genre> GetGenres()
        {
            return _genres;
        }

        public Genre GetGenreByGenreId(string genreId)
        {
            return _genres.FirstOrDefault(g => g.Id == genreId);
        }

        public Genre CreateGenre(Genre genre)
        {
            var g = new Genre(_idService.GenerateId(), genre.Name); 
            _genres.Add(g);
            return g;
        }

        public Genre UpdateGenre(Genre genre)
        {
            foreach (var g in _genres)
            {
                if (g.Id == genre.Id)
                {
                    g.Name = genre.Name;
                }
            }

            return GetGenreByGenreId(genre.Id);
        }

        public void DeleteGenre(string genreId)
        {
            _genres.RemoveAll(g => g.Id == genreId);
        }

        public IEnumerable<Author> GetAuthors()
        {
            return _authors;
        }

        public Author GetAuthorByAuthorId(string authorId)
        {
            return _authors.FirstOrDefault(a => a.Id == authorId);
        }

        public Author CreateAuthor(Author author)
        {
            var a = new Author(_idService.GenerateId(), author.Name);
            _authors.Add(a);
            return a;
        }

        public Author UpdateAuthor(Author author)
        {
            foreach (var a in _authors)
            {
                if (a.Id == author.Id)
                {
                    a.Name = author.Name;
                }
            }

            return GetAuthorByAuthorId(author.Id);
        }

        public void DeleteAuthor(string authorId)
        {
            _authors.RemoveAll(a => a.Id == authorId);
        }

        public IEnumerable<Book> GetBooks()
        {
            return _books;
        }

        public Book GetBookByBookId(string bookId)
        {
            return _books.FirstOrDefault(b => b.Id == bookId);
        }

        public Book CreateBook(Book book)
        {
            var b = new Book(_idService.GenerateId(), book.AuthorId, book.GenreId, book.Name);
            _books.Add(b);
            return b;
        }

        public Book UpdateBook(Book book)
        {
            foreach (var b in _books)
            {
                if (b.Id == book.Id)
                {
                    b.AuthorId = book.AuthorId;
                    b.GenreId = book.GenreId;
                    b.Name = book.Name;
                }
            }

            return GetBookByBookId(book.Id);
        }

        public void DeleteBook(string bookId)
        {
            _books.RemoveAll(b => b.Id == bookId);
        }

        public IEnumerable<Book> GetBooksByAuthorId(string authorId)
        {
            return _books.FindAll(b => b.AuthorId == authorId);
        }

        public IEnumerable<Book> GetBooksByGenreId(string genreId)
        {
            return _books.FindAll(b => b.GenreId == genreId);
        }
    }
}