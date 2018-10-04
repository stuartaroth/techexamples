using System;
using System.Text;
using Grapevine.Interfaces.Server;
using Grapevine.Shared;
using httpcsharp.models;
using httpcsharp.services.data;
using httpcsharp.services.json;

namespace httpcsharp.routes
{ 
    public class GenreRoute
    {
        private readonly IDataService _dataService;
        private readonly IJsonService _jsonService;

        public GenreRoute(IDataService dataService, IJsonService jsonService)
        {
            _dataService = dataService;
            _jsonService = jsonService;
        }
               
        public IHttpContext Handler(IHttpContext context)
        {
            switch (context.Request.HttpMethod)
            {
                case HttpMethod.GET:
                    return GetHandler(context);
                case HttpMethod.PUT:
                    return PutHandler(context);
                default:
                    return context;
            }
        }

        private IHttpContext GetHandler(IHttpContext context)
        {
            context.Response.ContentType = ContentType.JSON;

            String json;

            try
            {
                var idValues = context.Request.QueryString.GetValues("id");
                if (idValues != null && idValues.Length > 0)
                {
                    var id = idValues[0];
                    var genre = _dataService.GetGenreByGenreId(id);
                    json = _jsonService.Write(genre);
                }
                else
                {
                    var genres = _dataService.GetGenres();
                    json = _jsonService.Write(genres);
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
        
        private IHttpContext PutHandler(IHttpContext context)
        {
            context.Response.ContentType = ContentType.JSON;

            String json;

            try
            {
                String body = context.Request.Payload;
                var genre = _jsonService.Read<Genre>(body);
                var updatedGenre = _dataService.UpdateGenre(genre);
                json = _jsonService.Write(updatedGenre);
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