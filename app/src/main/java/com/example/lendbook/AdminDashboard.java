package com.example.lendbook;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AdminDashboard extends AppCompatActivity {

    private String firstname, lastname, email, username, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        Button buttonCatalogue = findViewById(R.id.Catalogue);
        Button buttonMyBooks = findViewById(R.id.MyBooks);
        Button buttonMyRequests = findViewById(R.id.MyRequests);
        Button buttonManageMembers = findViewById(R.id.ManageMembers);
        ImageButton profileOptionsIcon = findViewById(R.id.ProfileOptionsIcon);
        TextView welcomeText = findViewById(R.id.WelcomeText);

        // Get user details from LoginActivity
        firstname = getIntent().getStringExtra("firstname");
        lastname = getIntent().getStringExtra("lastname");
        email = getIntent().getStringExtra("email");
        username = getIntent().getStringExtra("username");
        phone = getIntent().getStringExtra("phone");

        welcomeText.setText(getString(R.string.welcomeText, firstname));

        buttonCatalogue.setOnClickListener(v -> startActivity(new Intent(AdminDashboard.this, Catalogue.class)));
        buttonMyBooks.setOnClickListener(v -> startActivity(new Intent(AdminDashboard.this, MyBooks.class)));
        buttonMyRequests.setOnClickListener(v -> startActivity(new Intent(AdminDashboard.this, MyRequests.class)));
        buttonManageMembers.setOnClickListener(v -> startActivity(new Intent(AdminDashboard.this, MemberList.class)));
        profileOptionsIcon.setOnClickListener(this::showProfileOptionsMenu);
        buttonCatalogue.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboard.this, Catalogue.class);
            intent.putExtra("fromDashboard", "admin");
            startActivity(intent);
        });

        buttonManageMembers.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboard.this, MemberList.class);
            intent.putExtra("fromDashboard", "admin");
            startActivity(intent);
        });
    }
    //PopupMenu for profile options and sign out
    private void showProfileOptionsMenu(View anchor) {
        PopupMenu popupMenu = new PopupMenu(this, anchor);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.profile_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.menu_profile_settings) {
                Intent intent = new Intent(AdminDashboard.this, ProfileSettings.class);
                intent.putExtra("firstname", firstname);
                intent.putExtra("lastname", lastname);
                intent.putExtra("email", email);
                intent.putExtra("username", username);
                intent.putExtra("phone", phone);
                startActivity(intent);
                return true;
            } else if (item.getItemId() == R.id.menu_sign_out) {
                Toast.makeText(AdminDashboard.this, "Signed out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AdminDashboard.this, LoginActivity.class));
                finish();
                return true;
            }
            return false;
        });
        popupMenu.show();
    }
}
