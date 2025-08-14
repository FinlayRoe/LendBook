package com.example.lendbook;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class Catalogue extends AppCompatActivity {
    private BookAdapter bookAdapter;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogue);

        ImageButton backButton = findViewById(R.id.backButton);
        RecyclerView recyclerView = findViewById(R.id.catalogueRecyclerView);
        Button addBookButton = findViewById(R.id.add_book_button);

        // Show/hide add button based on who opened this page
        String fromDashboard = getIntent().getStringExtra("fromDashboard");
        if (!"admin".equalsIgnoreCase(fromDashboard)) {
            addBookButton.setVisibility(View.GONE);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bookAdapter = new BookAdapter(this, new java.util.ArrayList<>());
        recyclerView.setAdapter(bookAdapter);

        dbHelper = new DatabaseHelper(this);
        loadBooksFromDatabase();

        backButton.setOnClickListener(v -> finish());
    }


    private void loadBooksFromDatabase() {
        new Thread(() -> {
            // Load all books from database
            List<Book> books = dbHelper.getAllBooks();

            // Update UI on main thread
            runOnUiThread(() -> bookAdapter.updateBooks(books));
        }).start();
    }
}
