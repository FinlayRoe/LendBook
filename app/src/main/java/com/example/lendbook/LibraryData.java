package com.example.lendbook;

import java.util.ArrayList;
import java.util.List;

public class LibraryData {

    private static LibraryData instance;
    private final List<Book> myBooks;
    private final List<Book> myRequests;

    private LibraryData() {
        myBooks = new ArrayList<>();
        myRequests = new ArrayList<>();
    }

    public static LibraryData getInstance() {
        if (instance == null) {
            instance = new LibraryData();
        }
        return instance;
    }

    public List<Book> getMyBooks() {
        return myBooks;
    }

    public List<Book> getMyRequests() {
        return myRequests;
    }

    public void addBookToMyBooks(Book book) {
        myBooks.add(book);
    }

    public void addBookToMyRequests(Book book) {
        myRequests.add(book);
    }
}
