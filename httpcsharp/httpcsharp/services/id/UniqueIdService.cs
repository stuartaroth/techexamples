using System;

namespace httpcsharp.services.id
{
    public class UniqueIdService : IIdService
    {
        public string GenerateId()
        {
            return Guid.NewGuid().ToString().Replace("-", "");
        }

        public string GenerateId(string prefix)
        {
            return prefix + GenerateId();
        }
    }
}