namespace httpcsharp.models
{
    public class Author
    {
        public string Id { get; }
        public string Name { get; set; }

        public Author(string id, string name)
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