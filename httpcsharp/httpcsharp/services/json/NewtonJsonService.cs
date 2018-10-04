using Newtonsoft.Json;
using Newtonsoft.Json.Serialization;

namespace httpcsharp.services.json
{
    public class NewtonJsonService : IJsonService
    {
        private readonly JsonSerializerSettings _serializerSettings;
        
        public NewtonJsonService()
        {
            _serializerSettings = new JsonSerializerSettings
            {
                ContractResolver = new CamelCasePropertyNamesContractResolver()
            };
        }
        
        public string Write(object value)
        {
            return JsonConvert.SerializeObject(value, _serializerSettings);
        }

        public T Read<T>(string json)
        {
            return JsonConvert.DeserializeObject<T>(json, _serializerSettings);
        }
    }
}