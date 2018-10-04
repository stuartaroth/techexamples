namespace httpcsharp.models
{
    public class Genre
    {
        public string Id { get; }
        public string Name { get; set; }

        public Genre(string id, string name)
        {
            Id = id;
            Name = name;
        }

        public bool IsValid()
        {
            if (string.IsNullOrEmpty(Name))
            {
                return false;
            }

            return true;
        }
    }
}