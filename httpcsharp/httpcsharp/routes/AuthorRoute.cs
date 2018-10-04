using System;
using System.Text;
using Grapevine.Interfaces.Server;
using Grapevine.Shared;
using httpcsharp.models;
using httpcsharp.services.data;
using httpcsharp.services.json;

namespace httpcsharp.routes
{ 
    public class AuthorRoute
    {
        private readonly IDataService _dataService;
        private readonly IJsonService _jsonService;

        public AuthorRoute(IDataService dataService, IJsonService jsonService)
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
                if (idValues != null && idValues.Length > 0)
                {
                    var id = idValues[0];
                    var author = _dataService.GetAuthorByAuthorId(id);
                    json = _jsonService.Write(author);
                }
                else
                {
                    var authors = _dataService.GetAuthors();
                    json = _jsonService.Write(authors);
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