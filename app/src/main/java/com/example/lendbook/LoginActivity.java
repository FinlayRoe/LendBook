package com.example.lendbook;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmailAddress;
    private EditText editTextPassword;
    private List<Users> usersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmailAddress = findViewById(R.id.editTextEmailAddress);
        editTextPassword = findViewById(R.id.editTextPassword);
        Button buttonLogin = findViewById(R.id.buttonLogin);

        // Load all members from API on startup
        API.getAllMembers(LoginActivity.this, new API.UsersCallback() {
            @Override
            public void onSuccess(List<Users> users) {
                // Members are loaded, Save to usersList
                usersList = users;
            }

            @Override
            public void onError(String errorMessage) {
                // Handle error
                Toast.makeText(LoginActivity.this, "Failed to load members: " + errorMessage, Toast.LENGTH_LONG).show();
            }
        });

        buttonLogin.setOnClickListener(v -> {
            String enteredEmail = editTextEmailAddress.getText().toString().trim();
            String enteredPassword = editTextPassword.getText().toString().trim();

            if (enteredEmail.isEmpty() || enteredPassword.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            if (usersList == null) {
                Toast.makeText(LoginActivity.this, "Members not loaded yet, try again", Toast.LENGTH_SHORT).show();
                return;
            }

            // Find user by email
            Users matchedUser = null;
            for (Users user : usersList) {
                if (enteredEmail.equalsIgnoreCase(user.getEmail())) {
                    matchedUser = user;
                    break;
                }
            }

            if (matchedUser == null) {
                Toast.makeText(LoginActivity.this, "Email not found", Toast.LENGTH_SHORT).show();
                return;
            }

            // Admin route
            if ("admin".equals(enteredPassword)) {
                Intent intent = new Intent(LoginActivity.this, AdminDashboard.class);
                intent.putExtra("firstname", matchedUser.getFirstname());
                intent.putExtra("lastname", matchedUser.getLastname());
                intent.putExtra("email", matchedUser.getEmail());
                intent.putExtra("username", matchedUser.getUsername());
                intent.putExtra("phone", matchedUser.getContact());
                startActivity(intent);
                finish();
                return;
            }

            // Normal member route
            if ("123".equals(enteredPassword)) {
                Intent intent = new Intent(LoginActivity.this, MemberDashboard.class);
                intent.putExtra("firstname", matchedUser.getFirstname());
                intent.putExtra("lastname", matchedUser.getLastname());
                intent.putExtra("email", matchedUser.getEmail());
                intent.putExtra("username", matchedUser.getUsername());
                intent.putExtra("phone", matchedUser.getContact());
                startActivity(intent);
                finish();
                return;
            }

            Toast.makeText(LoginActivity.this, "Invalid password", Toast.LENGTH_SHORT).show();
        });
    }
}
