package com.example.sportmatch;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {

    public List<User> userList;
    public Event event;
    public List<String> userListstring;

    public RequestAdapter(List<User> userList, Event event, List<String> userListstring) {
        this.userList = userList;
        this.event = event;
        this.userListstring = userListstring;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_request_item, parent, false);
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

        holder.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", "onClick: Confirm clicked for user " + user.getUsername() + " and event " + event.getEventName());
                //il sterg din lista de Requests
                event.removeRequestFromEvent(userId);
                Log.d("TAG", "onClick: removed request" + userId);
                //il pun in lista de Participants
                event.addParticipant(userId);
                //sterg request din Requests db
                DatabaseReference requestsRef = database.getReference("Requests");
                requestsRef.orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot requestSnapshot : dataSnapshot.getChildren()) {
                            // Check if the event key matches the desired event
                            if (requestSnapshot.child("event/key").getValue(String.class).equals(event.getKey())) {
                                // Delete the request
                                requestSnapshot.getRef().removeValue()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                // Request deleted successfully
                                                // Handle any additional operations or show a success message
                                                Log.e("TAG", "onSuccess: deleted request");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Failed to delete the request
                                                // Handle the error or show an error message
                                            }
                                        });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle the error
                        Log.d("Error", "Error while reading the database");
                    }
                });


                //trimit notif userului ca a fost acceptat
                Toast.makeText(v.getContext(), "Confirm clicked for user " + user.getUsername(), Toast.LENGTH_SHORT).show();
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                event.removeRequestFromEvent(userId);
                Log.d("TAG", "onClick: removed request" + userId);
                //sterg request din Requests db
                DatabaseReference requestsRef = database.getReference("Requests");
                requestsRef.orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot requestSnapshot : dataSnapshot.getChildren()) {
                            // Check if the event key matches the desired event
                            if (requestSnapshot.child("event/key").getValue(String.class).equals(event.getKey())) {
                                // Delete the request
                                requestSnapshot.getRef().removeValue()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                // Request deleted successfully
                                                // Handle any additional operations or show a success message
                                                Log.e("TAG", "onSuccess: deleted request");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Failed to delete the request
                                                // Handle the error or show an error message
                                            }
                                        });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle the error
                        Log.d("Error", "Error while reading the database");
                    }
                });


                //trimit notif userului ca a fost acceptat
                Toast.makeText(v.getContext(), "Delete clicked for user " + user.getUsername(), Toast.LENGTH_SHORT).show();
            }
        });
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
