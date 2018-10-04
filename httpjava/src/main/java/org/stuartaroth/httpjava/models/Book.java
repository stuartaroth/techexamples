package org.stuartaroth.httpjava.models;

public class Book {
    private String id;
    private String authorId;
    private String genreId;
    private String name;

    public Book(String id, String authorId, String genreId, String name) {
        this.id = id;
        this.authorId = authorId;
        this.genreId = genreId;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getGenreId() {
        return genreId;
    }

    public void setGenreId(String genreId) {
        this.genreId = genreId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isValid() {
        if (authorId == null || authorId.equals("")) {
            return false;
        }

        if (genreId == null || genreId.equals("")) {
            return false;
        }

        if (name == null || name.equals("")) {
            return false;
        }

        return true;
    }
}
