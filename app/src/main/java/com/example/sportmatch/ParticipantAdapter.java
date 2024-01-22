package com.example.sportmatch;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ParticipantAdapter extends RecyclerView.Adapter<ParticipantAdapter.ViewHolder> {

    public List<User> userList;
    public Event event;
    public List<String> userListstring;

    public ParticipantAdapter(List<User> userList, Event event, List<String> userListstring) {
        this.userList = userList;
        this.event = event;
        this.userListstring = userListstring;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_part_item, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        User user = userList.get(position);
        String userId = userListstring.get(position);
        holder.txtUsername.setText(user.getUsername());

        holder.txtUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), OtherProfileActivity.class);
                intent.putExtra("user", userId);

                // Start the activity
                v.getContext().startActivity(intent);            }
        });

/*        holder.btnConfirm.setVisibility(View.GONE);
        holder.btnDelete.setVisibility(View.GONE);*/


    }

    @Override
    public int getItemCount() {
        if (this.userList == null) {
            return 0;
        }
        return this.userList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtUsername;
        Button btnConfirm, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtUsername = itemView.findViewById(R.id.txtUsername);
            btnConfirm = itemView.findViewById(R.id.btnConfirm);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
