package com.example.lendbook;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MemberDashboard extends AppCompatActivity {

    private Button buttonCatalogue;
    private Button buttonMyBooks;
    private Button buttonMyRequests;
    private ImageButton profileOptionsIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_dashboard);

        buttonCatalogue = findViewById(R.id.Catalogue);
        buttonMyBooks = findViewById(R.id.MyBooks);
        buttonMyRequests = findViewById(R.id.MyRequests);
        profileOptionsIcon = findViewById(R.id.ProfileOptionsIcon);

        buttonCatalogue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MemberDashboard.this, Catalogue.class));
            }
        });

        buttonMyBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MemberDashboard.this, MyBooks.class));
            }
        });

        buttonMyRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MemberDashboard.this, MyRequests.class));
            }
        });

        profileOptionsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProfileOptionsMenu(view);
            }
        });
    }

    private void showProfileOptionsMenu(View anchor) {
        PopupMenu popupMenu = new PopupMenu(this, anchor);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.profile_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.menu_profile_settings) {
                    startActivity(new Intent(MemberDashboard.this, ProfileSettings.class));
                    return true;
                } else if (id == R.id.menu_sign_out) {
                    // You could add FirebaseAuth.getInstance().signOut(); here if you're using Firebase
                    Toast.makeText(MemberDashboard.this, "Signed out", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MemberDashboard.this, LoginActivity.class));
                    finish();
                    return true;
                }
                return false;
            }
        });

        popupMenu.show();
    }
}
