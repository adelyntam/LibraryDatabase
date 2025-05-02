package com.library.model;

// better with JDBC
import java.time.LocalDate;

public class Member {
    private int memberId;
    private String name;
    private String email;
    private LocalDate membershipDate;

    public Member() {}

    public Member(int memberId, String name, String email, LocalDate membershipDate) {
        this.memberId = memberId;
        this.name = name;
        this.email = email;
        this.membershipDate = membershipDate;
    }

    public Member(String name, String email, LocalDate membershipDate) {
        this.name = name;
        this.email = email;
        this.membershipDate = membershipDate;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Member name cannot be empty.");
        }
        this.name = name.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty.");
        }
        if (!email.matches("^[\\w+.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            throw new IllegalArgumentException("Invalid email format.");
        }
        this.email = email.trim();
    }

    public LocalDate getMembershipDate() {
        return membershipDate;
    }

    public void setMembershipDate(LocalDate membershipDate) {
        if (membershipDate == null) {
            throw new IllegalArgumentException("Membership date cannot be null.");
        }
        this.membershipDate = membershipDate;
    }

    @Override
    public String toString() {
        return "Member(memberId=" + memberId + ", name=" + name + ", email=" + email + ", membershipDate=" + membershipDate + ")";
    }
}
