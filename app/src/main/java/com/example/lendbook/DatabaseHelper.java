package com.example.lendbook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String TABLE_BOOKS = "books";
    public static final String BOOK_ID = "id";
    public static final String BOOK_TITLE = "title";
    public static final String BOOK_AUTHOR = "author";
    public static final String BOOK_AVAILABLE = "available";

    public DatabaseHelper(Context context) {
        super(context, "library.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create table
        String createTable = "CREATE TABLE " + TABLE_BOOKS + " (" +
                BOOK_ID + " INTEGER PRIMARY KEY, " +
                BOOK_TITLE + " TEXT, " +
                BOOK_AUTHOR + " TEXT, " +
                BOOK_AVAILABLE + " INTEGER DEFAULT 1" +
                ")";
        db.execSQL(createTable);
        //Hardcoded books with their ID, Title, Author and Availability.
        db.execSQL("INSERT INTO " + TABLE_BOOKS + " (" +
                BOOK_ID + ", " + BOOK_TITLE + ", " + BOOK_AUTHOR + ", " + BOOK_AVAILABLE +
                ") VALUES (1, 'Little Women', 'Louisa May Alcott', 0)");

        db.execSQL("INSERT INTO " + TABLE_BOOKS + " (" +
                BOOK_ID + ", " + BOOK_TITLE + ", " + BOOK_AUTHOR + ", " + BOOK_AVAILABLE +
                ") VALUES (2, 'Sunburst and Luminary an Apollo Memoir', 'Don Eyles', 1)");

        db.execSQL("INSERT INTO " + TABLE_BOOKS + " (" +
                BOOK_ID + ", " + BOOK_TITLE + ", " + BOOK_AUTHOR + ", " + BOOK_AVAILABLE +
                ") VALUES (3, 'Sons of Sanguinius', 'Guy Haley', 1)");
        db.execSQL("INSERT INTO " + TABLE_BOOKS + " (" +
                BOOK_ID + ", " + BOOK_TITLE + ", " + BOOK_AUTHOR + ", " + BOOK_AVAILABLE +
                ") VALUES (4, 'Skunk Works', 'Ben R. Rich', 1)");
        db.execSQL("INSERT INTO " + TABLE_BOOKS + " (" +
                BOOK_ID + ", " + BOOK_TITLE + ", " + BOOK_AUTHOR + ", " + BOOK_AVAILABLE +
                ") VALUES (5, 'Chaos', 'James Gleick', 1)");
        db.execSQL("INSERT INTO " + TABLE_BOOKS + " (" +
                BOOK_ID + ", " + BOOK_TITLE + ", " + BOOK_AUTHOR + ", " + BOOK_AVAILABLE +
                ") VALUES (6, 'The Gruffalo', 'Julia Donaldson', 1)");
        db.execSQL("INSERT INTO " + TABLE_BOOKS + " (" +
                BOOK_ID + ", " + BOOK_TITLE + ", " + BOOK_AUTHOR + ", " + BOOK_AVAILABLE +
                ") VALUES (7, 'Dune', 'Frank Herbert', 1)");
        db.execSQL("INSERT INTO " + TABLE_BOOKS + " (" +
                BOOK_ID + ", " + BOOK_TITLE + ", " + BOOK_AUTHOR + ", " + BOOK_AVAILABLE +
                ") VALUES (8, 'The Summer I Turned Pretty', 'Jenny Han', 1)");
        db.execSQL("INSERT INTO " + TABLE_BOOKS + " (" +
                BOOK_ID + ", " + BOOK_TITLE + ", " + BOOK_AUTHOR + ", " + BOOK_AVAILABLE +
                ") VALUES (9, 'Gone With The Wind', 'Margaret Mitchell', 1)");
        db.execSQL("INSERT INTO " + TABLE_BOOKS + " (" +
                BOOK_ID + ", " + BOOK_TITLE + ", " + BOOK_AUTHOR + ", " + BOOK_AVAILABLE +
                ") VALUES (10, 'Sorry Not Sorry', 'Alyssa Milano', 1)");
        db.execSQL("INSERT INTO " + TABLE_BOOKS + " (" +
                BOOK_ID + ", " + BOOK_TITLE + ", " + BOOK_AUTHOR + ", " + BOOK_AVAILABLE +
                ") VALUES (11, 'Holes', 'Louis Sachar', 1)");
        db.execSQL("INSERT INTO " + TABLE_BOOKS + " (" +
                BOOK_ID + ", " + BOOK_TITLE + ", " + BOOK_AUTHOR + ", " + BOOK_AVAILABLE +
                ") VALUES (12, 'Fist of the Imperium', 'Andy Clark', 1)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKS);
        onCreate(db);
    }

    // Insert a book into database
    public boolean addBook(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(BOOK_ID, book.getId());
        cv.put(BOOK_TITLE, book.getTitle());
        cv.put(BOOK_AUTHOR, book.getAuthor());
        cv.put(BOOK_AVAILABLE, book.isAvailable() ? 1 : 0);

        long result = db.insert(TABLE_BOOKS, null, cv);
        db.close();
        return result != -1;
    }

    // Get all books from database
    public List<Book> getAllBooks() {
        List<Book> outputList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_BOOKS;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String author = cursor.getString(2);
                int availableInt = cursor.getInt(3);
                boolean available = (availableInt == 1);

                Book book = new Book(id, title, author, available);
                outputList.add(book);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return outputList;
    }

    // Delete a book by ID
    public boolean deleteBook(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_BOOKS, BOOK_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return rows > 0;
    }
}
