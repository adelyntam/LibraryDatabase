package com.library.model;

public class Author {
    private int authorId;
    private String name;
    private String nationality;

    public Author() {}

    public Author(int authorId, String name, String nationality) {
        this.authorId = authorId;
        this.name = name;
        this.nationality = nationality;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Author name cannot be empty.");
        }
        this.name = name.trim();
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    @Override
    public String toString() {
        return "Author(authorId=" + authorId + ", name=" + name + ", nationality=" + nationality + ")";
    }
}
