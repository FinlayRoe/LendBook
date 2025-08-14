package com.example.lendbook;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private final Context context;
    private List<Book> bookList;

    // Constructor
    public BookAdapter(Context context, List<Book> bookList) {
        this.context = context;
        this.bookList = bookList;
    }

    // ViewHolder class
    public static class BookViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView authorTextView;
        TextView availabilityTextView;
        Button catalogueButton;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.book_name);
            authorTextView = itemView.findViewById(R.id.book_author);
            availabilityTextView = itemView.findViewById(R.id.book_availability);
            catalogueButton = itemView.findViewById(R.id.Catalogue);
        }
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book currentBook = bookList.get(position);

        // Bind title and author
        holder.titleTextView.setText(currentBook.getTitle());
        holder.authorTextView.setText(currentBook.getAuthor());

        // Show availability and set button text/color using string resources
        if (currentBook.isAvailable()) {
            holder.availabilityTextView.setText(context.getString(R.string.Book_Available));
            holder.availabilityTextView.setTextColor(Color.parseColor("#008000"));  // green
            holder.catalogueButton.setText(context.getString(R.string.Loan_Book));
        } else {
            holder.availabilityTextView.setText(context.getString(R.string.Book_Unavailable));
            holder.availabilityTextView.setTextColor(Color.parseColor("#FF0000"));  // red
            holder.catalogueButton.setText(context.getString(R.string.Request_Book));
        }

        // Hide button if book already in MyBooks or MyRequests
        boolean isInMyBooks = LibraryData.getInstance().getMyBooks().contains(currentBook);
        boolean isInMyRequests = LibraryData.getInstance().getMyRequests().contains(currentBook);

        holder.catalogueButton.setVisibility((isInMyBooks || isInMyRequests) ? View.GONE : View.VISIBLE);

        holder.catalogueButton.setOnClickListener(v -> {
            if (currentBook.isAvailable()) {
                LibraryData.getInstance().addBookToMyBooks(currentBook);
            } else {
                LibraryData.getInstance().addBookToMyRequests(currentBook);
            }
            holder.catalogueButton.setVisibility(View.GONE);
        });
    }
    @Override
    public int getItemCount() {
        return bookList.size();
    }
    public void updateBooks(List<Book> newBooks) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return bookList.size();
            }
            @Override
            public int getNewListSize() {
                return newBooks.size();
            }
            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                // Compare unique IDs (or title+author) to see if it’s the same book
                return bookList.get(oldItemPosition).getId() == newBooks.get(newItemPosition).getId();
            }
            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                return bookList.get(oldItemPosition).equals(newBooks.get(newItemPosition));
            }
        });

        bookList = newBooks;
        diffResult.dispatchUpdatesTo(this);
    }
}
