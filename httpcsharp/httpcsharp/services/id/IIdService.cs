namespace httpcsharp.services.id
{
    public interface IIdService
    {
        string GenerateId();
        string GenerateId(string prefix);
    }
}