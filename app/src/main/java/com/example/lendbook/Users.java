package com.example.lendbook;

public class Users {

    private String firstname;
    private String lastname;
    private String email;
    private String username;
    private String contact;
    private String membership_end_date;

    public Users() {}
    // Getters and setters
    public String getFirstname() { return firstname; }
    public void setFirstname(String firstname) { this.firstname = firstname; }

    public String getLastname() { return lastname; }
    public void setLastname(String lastname) { this.lastname = lastname; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    public String getMembershipEndDate() { return membership_end_date; }
    public void setMembershipEndDate(String membership_end_date) { this.membership_end_date = membership_end_date; }
}
