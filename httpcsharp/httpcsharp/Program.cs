using System;
using Grapevine.Server;
using httpcsharp.routes;
using httpcsharp.services.data;
using httpcsharp.services.id;
using httpcsharp.services.json;
using httpcsharp.models;
using httpcsharp.services;

namespace httpcsharp
{
    class Program
    {         
        static void Main(string[] args)
        {
            IIdService idService = new UniqueIdService();
            IDataService inMemoryDataService = new InMemoryDataService(idService);
            inMemoryDataService.SetupData(null);
            
            IDataService dataService = new ElasticDataService(inMemoryDataService);
            dataService.SetupData(null);

            IJsonService jsonService = new NewtonJsonService();
            
            using (var server = new RestServer())
            {
                var genreRoute = new GenreRoute(dataService, jsonService);
                var authorRoute = new AuthorRoute(dataService, jsonService);
                var bookRoute = new BookRoute(dataService, jsonService);

                server.Host = "127.0.0.1";
                server.Port = "8444";
                server.Router.Register(genreRoute.Handler, "/genres");
                server.Router.Register(authorRoute.Handler, "/authors");
                server.Router.Register(bookRoute.Handler, "/books");
                
                server.Start();
                Console.ReadLine();
                server.Stop();
            }
        }
    }
}