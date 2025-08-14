package com.example.lendbook;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileSettings extends AppCompatActivity {

    private TextView textFullName, textEmail, textPhone;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);

        // Views
        ImageButton backButton = findViewById(R.id.backButton);
        ImageButton editNameIcon = findViewById(R.id.EditNameIcon);
        ImageButton editEmailIcon = findViewById(R.id.EditEmailIcon);
        ImageButton editPhoneIcon = findViewById(R.id.editPhoneNumberIcon);

        textFullName = findViewById(R.id.FullName);
        textEmail = findViewById(R.id.editTextEmailAddress);
        textPhone = findViewById(R.id.editPhoneNumber);

        // Back button
        backButton.setOnClickListener(v -> finish());

        // Get data from Intent (marked as final so lambdas accept them)
        username = getIntent().getStringExtra("username");
        final String firstname = getIntent().getStringExtra("firstname");
        final String lastname = getIntent().getStringExtra("lastname");
        final String email = getIntent().getStringExtra("email");
        final String phone = getIntent().getStringExtra("phone");

        // Combine names
        String fullName = "";
        if (firstname != null) fullName += firstname;
        if (lastname != null && !lastname.isEmpty()) {
            if (!fullName.isEmpty()) fullName += " ";
            fullName += lastname;
        }

        textFullName.setText(fullName);
        textEmail.setText(email);
        textPhone.setText(phone);

        // Edit name
        String finalFullName = fullName;
        editNameIcon.setOnClickListener(v -> showEditDialog(
                this, "Edit Name", finalFullName, newValue -> {
                    String[] parts = newValue.split(" ", 2);
                    String newFirst = parts[0];
                    String newLast = parts.length > 1 ? parts[1] : "";
                    updateMemberDetails(newFirst, newLast, textEmail.getText().toString(), textPhone.getText().toString());
                }
        ));

        // Edit email
        editEmailIcon.setOnClickListener(v -> showEditDialog(
                this, "Edit Email", textEmail.getText().toString(), newValue -> updateMemberDetails(firstname, lastname, newValue, textPhone.getText().toString())
        ));

        // Edit phone
        editPhoneIcon.setOnClickListener(v -> showEditDialog(
                this,
                "Edit Phone Number",
                textPhone.getText().toString(),
                newValue -> updateMemberDetails(firstname, lastname, textEmail.getText().toString(), newValue)
        ));
    }
    // Update details dialog
    private void showEditDialog(Context context, String title, String currentValue, OnValueChanged callback) {
        final android.widget.EditText input = new android.widget.EditText(context);
        input.setText(currentValue);

        new AlertDialog.Builder(context).setTitle(title).setView(input).setPositiveButton("Save", (dialog, which) -> {
                    String newValue = input.getText().toString().trim();
                    if (!newValue.isEmpty()) {
                        callback.onChanged(newValue);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
    // Update member details using dialog from above
    private void updateMemberDetails(String firstname, String lastname, String email, String phone) {
        Users updatedMember = new Users();
        updatedMember.setFirstname(firstname);
        updatedMember.setLastname(lastname);
        updatedMember.setEmail(email);
        updatedMember.setContact(phone);
        updatedMember.setMembershipEndDate("2025-12-31");

        API.updateMember(this, username, updatedMember, new API.APICallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(ProfileSettings.this, "Profile updated", Toast.LENGTH_SHORT).show();
                String fullName = getString(R.string.Full_Name, firstname, lastname);
                textFullName.setText(fullName);
                textEmail.setText(email);
                textPhone.setText(phone);
            }

            @Override
            public void onError(String error) {
                Toast.makeText(ProfileSettings.this, "Update failed: " + error, Toast.LENGTH_LONG).show();
            }
        });
    }

    interface OnValueChanged {
        void onChanged(String newValue);
    }
}
