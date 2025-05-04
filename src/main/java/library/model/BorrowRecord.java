package library.model;

// better with JDBC
import java.time.LocalDate;
import java.util.Objects;

public class BorrowRecord {
    private int recordId;
    private int bookId;      // FK to Book
    private int memberId;    // FK to Member
    private LocalDate borrowDate;
    private LocalDate returnDate;
    private String status;   // either "borrowed" or "returned"

    public BorrowRecord() {}

    public BorrowRecord(int recordId, int bookId, int memberId, LocalDate borrowDate, LocalDate returnDate, String status) {
        this.recordId = recordId;
        this.bookId = bookId;
        this.memberId = memberId;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        // should start as borrowed
        this.status = status;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        if (borrowDate == null) {
            throw new IllegalArgumentException("Borrow date cannot be null.");
        }
        this.borrowDate = borrowDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        // null = book may not be returned yet
        this.returnDate = returnDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (!Objects.equals(status, "borrowed") && !Objects.equals(status, "returned")) {
            throw new IllegalArgumentException("Status must be 'borrowed' or 'returned'.");
        }
        this.status = status;
    }

    @Override
    public String toString() {
        return "BorrowRecord(recordId=" + recordId + ", bookId=" + bookId + ", memberId=" + memberId + ", borrowDate=" + borrowDate + ", returnDate=" + returnDate + ", status=" + status + ")";
    }
}
