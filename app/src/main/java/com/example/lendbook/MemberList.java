package com.example.lendbook;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MemberList extends AppCompatActivity {

    private RecyclerView recyclerViewMembers;
    private MembersAdapter membersAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_list);

        recyclerViewMembers = findViewById(R.id.memberlistRecyclerView);
        recyclerViewMembers.setLayoutManager(new LinearLayoutManager(this));

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        Button addMemberButton = findViewById(R.id.add_member_button);
        String fromDashboard = getIntent().getStringExtra("fromDashboard");
        if (!"admin".equalsIgnoreCase(fromDashboard)) {
            addMemberButton.setVisibility(View.GONE);
        } else {
            addMemberButton.setOnClickListener(v -> showAddMemberDialog());
        }

        loadMembers();
    }

    private void loadMembers() {
        API.getAllMembers(this, new API.UsersCallback() {
            @Override
            public void onSuccess(List<Users> users) {
                membersAdapter = new MembersAdapter(MemberList.this, users);
                membersAdapter.setOnEditClickListener(member -> showEditMemberDialog(MemberList.this, member));
                membersAdapter.setOnRemoveClickListener(member -> removeMember(member));
                recyclerViewMembers.setAdapter(membersAdapter);
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(MemberList.this, "Failed to load members: " + errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showAddMemberDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Member");

        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_edit_member, null);
        EditText firstNameField = dialogView.findViewById(R.id.edit_firstname);
        EditText lastNameField = dialogView.findViewById(R.id.edit_lastname);
        EditText emailField = dialogView.findViewById(R.id.edit_email);
        EditText usernameField = dialogView.findViewById(R.id.edit_username);
        EditText contactField = dialogView.findViewById(R.id.edit_contact);
        EditText membershipEndDateField = dialogView.findViewById(R.id.edit_membership_end_date);

        builder.setView(dialogView);
        builder.setPositiveButton("Add", (dialog, which) -> {});
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();

        Button addButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        addButton.setOnClickListener(v -> {
            String firstName = firstNameField.getText().toString().trim();
            String lastName = lastNameField.getText().toString().trim();
            String email = emailField.getText().toString().trim();
            String username = usernameField.getText().toString().trim();
            String contact = contactField.getText().toString().trim();
            String membershipEndDate = membershipEndDateField.getText().toString().trim();

            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || username.isEmpty()) {
                Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
                return;
            }

            Users newMember = new Users();
            newMember.setFirstname(firstName);
            newMember.setLastname(lastName);
            newMember.setEmail(email);
            newMember.setUsername(username);
            newMember.setContact(contact);
            newMember.setMembershipEndDate(membershipEndDate);

            API.addMember(this, newMember, new API.APICallback() {
                @Override
                public void onSuccess() {
                    membersAdapter.addMember(newMember);
                    Toast.makeText(MemberList.this, "Member added successfully", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }

                @Override
                public void onError(String errorMessage) {
                    Toast.makeText(MemberList.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void showEditMemberDialog(Context context, Users member) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_edit_member, null);

        EditText editFirstname = dialogView.findViewById(R.id.edit_firstname);
        EditText editLastname = dialogView.findViewById(R.id.edit_lastname);
        EditText editEmail = dialogView.findViewById(R.id.edit_email);
        EditText editUsername = dialogView.findViewById(R.id.edit_username); // display only
        EditText editContact = dialogView.findViewById(R.id.edit_contact);
        EditText editMembershipEndDate = dialogView.findViewById(R.id.edit_membership_end_date);

        // Pre-fill all fields
        editFirstname.setText(member.getFirstname());
        editLastname.setText(member.getLastname());
        editEmail.setText(member.getEmail());
        editUsername.setText(member.getUsername());
        editContact.setText(member.getContact());
        editMembershipEndDate.setText(member.getMembershipEndDate());

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("Edit Member")
                .setView(dialogView)
                .setPositiveButton("Save", null)
                .setNegativeButton("Cancel", (d, which) -> d.dismiss())
                .create();

        dialog.show();

        Button saveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        saveButton.setOnClickListener(v -> {
            String firstName = editFirstname.getText().toString().trim();
            String lastName = editLastname.getText().toString().trim();
            String email = editEmail.getText().toString().trim();
            String contact = editContact.getText().toString().trim();
            String membershipEndDate = editMembershipEndDate.getText().toString().trim();

            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()) {
                Toast.makeText(context, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
                return;
            }

            Users updated = new Users();
            updated.setFirstname(firstName);
            updated.setLastname(lastName);
            updated.setEmail(email);
            updated.setUsername(member.getUsername());
            updated.setContact(contact);
            updated.setMembershipEndDate(membershipEndDate);

            API.updateMember(context, member.getUsername(), updated, new API.APICallback() {
                @Override
                public void onSuccess() {
                    membersAdapter.updateMember(updated);
                    Toast.makeText(context, "Member updated successfully", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }

                @Override
                public void onError(String errorMessage) {
                    Toast.makeText(context, "Error: " + errorMessage, Toast.LENGTH_LONG).show();
                }
            });
        });
    }

    private void removeMember(Users member) {
        API.deleteMember(this, member.getUsername(), new API.APICallback() {
            @Override
            public void onSuccess() {
                membersAdapter.removeMember(member);
                Toast.makeText(MemberList.this, "Member removed successfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(MemberList.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
