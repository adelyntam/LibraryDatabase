package com.library.model;

public class Book {
    private int bookId;
    private String title;
    private int authorId;  // FK to Author
    private String genre;
    private int publishYear;
    private boolean isAvailable;

    public Book() {}

    public Book(int bookId, String title, int authorId, String genre, int publishYear, boolean isAvailable) {
        this.bookId = bookId;
        this.title = title;
        this.authorId = authorId;
        this.genre = genre;
        this.publishYear = publishYear;
        this.isAvailable = isAvailable;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Book title cannot be empty.");
        }
        this.title = title.trim();
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getPublishYear() {
        return publishYear;
    }

    public void setPublishYear(int publishYear) {
        this.publishYear = publishYear;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    @Override
    public String toString() {
        return "Book(bookId=" + bookId + ", title=" + title + ", authorId=" + authorId + ", genre=" + genre + ", publishYear=" + publishYear + ", isAvailable=" + isAvailable + ")";
    }
}
