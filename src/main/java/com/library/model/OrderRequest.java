package com.library.model;

// better with JDBC
import java.time.LocalDate;
import java.util.Objects;

public class OrderRequest {
    private int requestId;
    private int memberId;     // FK to Member
    private String bookTitle;
    private String authorName;
    private LocalDate requestDate;
    private String status;    // either "pending" or "fulfilled"

    public OrderRequest() {}

    public OrderRequest(int requestId, int memberId, String bookTitle, String authorName, LocalDate requestDate, String status) {
        this.requestId = requestId;
        this.memberId = memberId;
        this.bookTitle = bookTitle;
        this.authorName = authorName;
        this.requestDate = requestDate;
        this.status = status;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        if (bookTitle == null || bookTitle.trim().isEmpty()) {
            throw new IllegalArgumentException("Book title cannot be empty.");
        }
        this.bookTitle = bookTitle.trim();
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        if (authorName == null || authorName.trim().isEmpty()) {
            throw new IllegalArgumentException("Author name cannot be empty.");
        }
        this.authorName = authorName.trim();
    }

    public LocalDate getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDate requestDate) {
        if (requestDate == null) {
            throw new IllegalArgumentException("Request date cannot be null.");
        }
        this.requestDate = requestDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (!Objects.equals(status, "pending") && !Objects.equals(status, "fulfilled")) {
            throw new IllegalArgumentException("Status must be 'pending' or 'fulfilled'.");
        }
        this.status = status;
    }

    @Override
    public String toString() {
        return "OrderRequest(requestId=" + requestId + ", memberId=" + memberId + ", bookTitle=" + bookTitle + ", authorName=" + authorName + ", requestDate=" + requestDate + ", status=" + status + ")";
    }
}
