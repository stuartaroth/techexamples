namespace httpcsharp.services.json
{
    public interface IJsonService
    {
        string Write(object value);
        T Read<T>(string json);
    }
}