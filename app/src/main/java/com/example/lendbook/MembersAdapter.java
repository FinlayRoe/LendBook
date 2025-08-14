package com.example.lendbook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.MemberViewHolder> {

    private final Context context;
    private final List<Users> members;
    private OnEditClickListener onEditClickListener;
    private OnRemoveClickListener onRemoveClickListener;

    public MembersAdapter(Context context, List<Users> members) {
        this.context = context;
        this.members = new ArrayList<>(members);
    }

    @NonNull
    @Override
    public MemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_user, parent, false);
        return new MemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberViewHolder holder, int position) {
        Users member = members.get(position);

        holder.username.setText(member.getUsername());

        // Use string resource with placeholders to display full name
        String fullName = context.getString(R.string.Full_Name,
                member.getFirstname(),
                member.getLastname());
        holder.fullname.setText(fullName);

        holder.email.setText(member.getEmail());

        holder.editButton.setOnClickListener(v -> {
            if (onEditClickListener != null) {
                onEditClickListener.onEditClick(member);
            }
        });

        holder.removeButton.setOnClickListener(v -> {
            if (onRemoveClickListener != null) {
                onRemoveClickListener.onRemoveClick(member);
            }
        });
    }

    @Override
    public int getItemCount() {
        return members.size();
    }

    public void addMember(Users newMember) {
        members.add(newMember);
        notifyItemInserted(members.size() - 1); // specific change event
    }

    public void updateMember(Users updatedMember) {
        for (int i = 0; i < members.size(); i++) {
            if (members.get(i).getUsername().equals(updatedMember.getUsername())) {
                members.set(i, updatedMember);
                notifyItemChanged(i); // specific change event
                break;
            }
        }
    }

    public void removeMember(Users member) {
        int index = members.indexOf(member);
        if (index != -1) {
            members.remove(index);
            notifyItemRemoved(index); // specific change event
        }
    }

    public void setOnEditClickListener(OnEditClickListener listener) {
        this.onEditClickListener = listener;
    }

    public void setOnRemoveClickListener(OnRemoveClickListener listener) {
        this.onRemoveClickListener = listener;
    }

    public static class MemberViewHolder extends RecyclerView.ViewHolder {
        TextView username, fullname, email;
        Button editButton, removeButton;

        public MemberViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.Member_Username);
            fullname = itemView.findViewById(R.id.Member_FullName);
            email = itemView.findViewById(R.id.Member_Email);
            editButton = itemView.findViewById(R.id.edit_button);
            removeButton = itemView.findViewById(R.id.remove_user);
        }
    }

    public interface OnEditClickListener {
        void onEditClick(Users member);
    }

    public interface OnRemoveClickListener {
        void onRemoveClick(Users member);
    }
}
