package com.example.sportmatch;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ktx.Firebase;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
    private List<Event> events;
    private OnChatClickListener onChatClickListener;


    public EventAdapter(List<Event> events) {
        this.events = events;
    }
    public interface OnChatClickListener {
        void onChatClick(String eventId);
    }

    public void setOnChatClickListener(OnChatClickListener onChatClickListener) {
        this.onChatClickListener = onChatClickListener;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.registered_event_item, parent, false);
        EventViewHolder viewHolder = new EventViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        if (events != null && events.size() > position && events.get(position) != null) {
            Event event = events.get(position);

            // Initialize the views here
            TextView eventTitle = holder.itemView.findViewById(R.id.eventTitle);
            TextView eventDate = holder.itemView.findViewById(R.id.eventDate);
            TextView eventTime = holder.itemView.findViewById(R.id.eventTime);
            Button eventChat = holder.itemView.findViewById(R.id.buttonToChat);

            // Set the values of the views
            eventTitle.setText(event.getEventName());
            eventDate.setText(event.getDate());
            eventTime.setText(event.getTime());

            // Set the chat button listener
            eventChat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String eventId = event.getUid();
                    if (eventId != null && onChatClickListener != null) {
                        onChatClickListener.onChatClick(eventId);
                    }
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return events.size();
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView eventTitle, eventDate, eventTime;
        Button eventChat;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            eventTitle = itemView.findViewById(R.id.eventTitle);
            eventDate = itemView.findViewById(R.id.eventDate);
            eventTime = itemView.findViewById(R.id.eventTime);
            eventChat = itemView.findViewById(R.id.buttonToChat);
        }
    }
}
