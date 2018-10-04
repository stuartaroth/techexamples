using System;
using System.Text;
using Grapevine.Interfaces.Server;
using Grapevine.Shared;
using httpcsharp.models;
using httpcsharp.services.data;
using httpcsharp.services.json;

namespace httpcsharp.routes
{ 
    public class BookRoute
    {
        private readonly IDataService _dataService;
        private readonly IJsonService _jsonService;

        public BookRoute(IDataService dataService, IJsonService jsonService)
        {
            _dataService = dataService;
            _jsonService = jsonService;
        }
               
        public IHttpContext Handler(IHttpContext context)
        {
            return GetHandler(context);
        }

        private IHttpContext GetHandler(IHttpContext context)
        {
            context.Response.ContentType = ContentType.JSON;

            String json;

            try
            {
                var idValues = context.Request.QueryString.GetValues("id");
                var authorIdValues = context.Request.QueryString.GetValues("author-id");
                var genreIdValues = context.Request.QueryString.GetValues("genre-id");
                
                if (idValues != null && idValues.Length > 0)
                {
                    var id = idValues[0];
                    var book = _dataService.GetBookByBookId(id);
                    json = _jsonService.Write(book);
                }
                else if (authorIdValues != null && authorIdValues.Length > 0)
                {
                    var authorId = authorIdValues[0];
                    var books = _dataService.GetBooksByAuthorId(authorId);
                    json = _jsonService.Write(books);
                }
                else if (genreIdValues != null && genreIdValues.Length > 0)
                {
                    var genreId = genreIdValues[0];
                    var books = _dataService.GetBooksByGenreId(genreId);
                    json = _jsonService.Write(books);
                }
                else
                {
                    var books = _dataService.GetBooks();
                    json = _jsonService.Write(books);
                }
            }
            catch (Exception e)
            {
                context.Response.StatusCode = HttpStatusCode.InternalServerError;
                json = _jsonService.Write(new ErrorMessage(e.Message));
            }
            
            context.Response.SendResponse(Encoding.ASCII.GetBytes(json));
            return context;
        }
    }
}