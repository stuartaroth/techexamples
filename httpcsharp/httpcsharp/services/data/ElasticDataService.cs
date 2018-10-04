using System;
using System.Collections.Generic;
using System.Linq;
using httpcsharp.models;
using Nest;

namespace httpcsharp.services.data
{
    public class ElasticDataService : IDataService
    {
        private IDataService _dataService;
        private ElasticClient _elasticClient;
        private readonly string GENRE_INDEX = "genres";
        private readonly string AUTHOR_INDEX = "authors";
        private readonly string BOOK_INDEX = "books";
        private readonly int DEFAULT_SIZE = 100;

        public ElasticDataService(IDataService dataService)
        {
            _dataService = dataService;
            
            var connectionSettings = new ConnectionSettings()
                .EnableDebugMode()
                .RequestTimeout(TimeSpan.FromMinutes(2));
            _elasticClient = new ElasticClient(connectionSettings);            
        }
        
        public void SetupData(string data)
        {
            _elasticClient.DeleteIndex(GENRE_INDEX);
            _elasticClient.DeleteIndex(AUTHOR_INDEX);
            _elasticClient.DeleteIndex(BOOK_INDEX);

            var genreDescriptor = new CreateIndexDescriptor(GENRE_INDEX)
                .Mappings(ms => ms.Map<Genre>(m => m.AutoMap()));

            var authorDescriptor = new CreateIndexDescriptor(AUTHOR_INDEX)
                .Mappings(ms => ms.Map<Author>(m => m.AutoMap()));
            
            var bookDescriptor = new CreateIndexDescriptor(BOOK_INDEX)
                .Mappings(ms => ms.Map<Book>(m => m.AutoMap()));
            
            _elasticClient.CreateIndex(GENRE_INDEX, c => genreDescriptor);
            _elasticClient.CreateIndex(AUTHOR_INDEX, c => authorDescriptor);
            _elasticClient.CreateIndex(BOOK_INDEX, c => bookDescriptor);
            
            foreach (var genre in _dataService.GetGenres())
            {
                CreateGenre(genre);
            }

            foreach (var author in _dataService.GetAuthors())
            {
                CreateAuthor(author);
            }

            foreach (var book in _dataService.GetBooks())
            {
                CreateBook(book);
            }
        }

        public IEnumerable<Genre> GetGenres()
        {
            var response = _elasticClient.Search<Genre>(
                s => s.Index(GENRE_INDEX)
                      .Size(DEFAULT_SIZE)
                      .Query(q => q.MatchAll())
                );
            return response.Documents;
        }

        public Genre GetGenreByGenreId(string genreId)
        {
            var response = _elasticClient.Search<Genre>(
                s => s.Index(GENRE_INDEX)
                    .Size(1)
                    .Query(q => q.Bool(b => b.Should(bs => bs.Term(genre => genre.Id, genreId))))
                );

            return response.Documents.SingleOrDefault();
        }

        public Genre CreateGenre(Genre genre)
        {
            _elasticClient.Index(genre, i => i.Index(GENRE_INDEX));
            return GetGenreByGenreId(genre.Id);
        }

        public Genre UpdateGenre(Genre genre)
        {
            _elasticClient.Update(
                DocumentPath<Genre>.Id(genre.Id),
                update => update.Index(GENRE_INDEX)
                    .DocAsUpsert()
                    .Doc(genre)
            );
            return genre;
        }

        public void DeleteGenre(string genreId)
        {
            throw new System.NotImplementedException();
        }

        public IEnumerable<Author> GetAuthors()
        {
            var response = _elasticClient.Search<Author>(
                s => s.Index(AUTHOR_INDEX)
                    .Size(DEFAULT_SIZE)
                    .Query(q => q.MatchAll())
            );
            return response.Documents;
        }

        public Author GetAuthorByAuthorId(string authorId)
        {
            var response = _elasticClient.Search<Author>(
                s => s.Index(AUTHOR_INDEX)
                    .Size(1)
                    .Query(q => q.Bool(b => b.Should(bs => bs.Term(author => author.Id, authorId))))
            );

            return response.Documents.SingleOrDefault();
        }

        public Author CreateAuthor(Author author)
        {
            _elasticClient.Index(author, i => i.Index(AUTHOR_INDEX));
            return GetAuthorByAuthorId(author.Id);
        }

        public Author UpdateAuthor(Author author)
        {
            throw new System.NotImplementedException();
        }

        public void DeleteAuthor(string authorId)
        {
            throw new System.NotImplementedException();
        }

        public IEnumerable<Book> GetBooks()
        {
            var response = _elasticClient.Search<Book>(
                s => s.Index(BOOK_INDEX)
                    .Size(DEFAULT_SIZE)
                    .Query(q => q.MatchAll())
            );
            return response.Documents;
        }

        public Book GetBookByBookId(string bookId)
        {
            var response = _elasticClient.Search<Book>(
                s => s.Index(BOOK_INDEX)
                    .Size(1)
                    .Query(q => q.Bool(b => b.Should(bs => bs.Term(book => book.Id, bookId))))
            );

            return response.Documents.SingleOrDefault();
        }

        public Book CreateBook(Book book)
        {
            _elasticClient.Index(book, i => i.Index(BOOK_INDEX));
            return GetBookByBookId(book.Id);
        }

        public Book UpdateBook(Book book)
        {
            throw new System.NotImplementedException();
        }

        public void DeleteBook(string bookId)
        {
            throw new System.NotImplementedException();
        }

        public IEnumerable<Book> GetBooksByAuthorId(string authorId)
        {
            var response = _elasticClient.Search<Book>(
                s => s.Index(BOOK_INDEX)
                    .Size(DEFAULT_SIZE)
                    .Query(q => q.Bool(b => b.Should(bs => bs.Term(book => book.AuthorId, authorId))))
            );

            return response.Documents;
        }

        public IEnumerable<Book> GetBooksByGenreId(string genreId)
        {
            var response = _elasticClient.Search<Book>(
                s => s.Index(BOOK_INDEX)
                    .Size(DEFAULT_SIZE)
                    .Query(q => q.Bool(b => b.Should(bs => bs.Term(book => book.GenreId, genreId))))
            );

            return response.Documents;
        }
    }
}