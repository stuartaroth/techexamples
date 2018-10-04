namespace httpcsharp.models
{
    public class Book
    {
        public string Id { get; }
        public string AuthorId { get; set; }
        public string GenreId { get; set; }
        public string Name { get; set; }
        
        public Book(string id, string authorId, string genreId, string name)
        {
            Id = id;
            AuthorId = authorId;
            GenreId = genreId;
            Name = name;
        }

        public bool IsValid()
        {
            if (string.IsNullOrEmpty(AuthorId))
            {
                return false;
            }

            if (string.IsNullOrEmpty(GenreId))
            {
                return false;
            }

            if (string.IsNullOrEmpty(Name))
            {
                return false;
            }

            return true;
        }
    }
}