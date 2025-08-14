package com.example.lendbook;

import android.os.Bundle;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MyRequests extends AppCompatActivity {

    private BookAdapter bookAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);

        ImageButton backButton = findViewById(R.id.backButton);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewMyRequests);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bookAdapter = new BookAdapter(this, new java.util.ArrayList<>());
        recyclerView.setAdapter(bookAdapter);

        loadMyRequests();

        backButton.setOnClickListener(v -> finish());
    }

    private void loadMyRequests() {
        List<Book> myRequests = LibraryData.getInstance().getMyRequests();
        bookAdapter.updateBooks(myRequests);
    }
}
