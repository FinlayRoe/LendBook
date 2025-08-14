package com.example.lendbook;

import android.os.Bundle;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MyBooks extends AppCompatActivity {

    private BookAdapter bookAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_books);

        ImageButton backButton = findViewById(R.id.backButton);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewMyBooks);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bookAdapter = new BookAdapter(this, new java.util.ArrayList<>());
        recyclerView.setAdapter(bookAdapter);

        loadMyBooks();

        backButton.setOnClickListener(v -> finish());
    }

    private void loadMyBooks() {
        List<Book> myBooks = LibraryData.getInstance().getMyBooks();
        bookAdapter.updateBooks(myBooks);
    }
}
