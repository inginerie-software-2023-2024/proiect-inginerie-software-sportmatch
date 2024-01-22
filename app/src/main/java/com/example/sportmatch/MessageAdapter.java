package com.example.sportmatch;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;

import java.util.List;
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    Context context;
    List<Message> messages;
    DatabaseReference messagedb;

    public MessageAdapter(Context context, List<Message> messages, DatabaseReference messagedb) {
        this.context = context;
        this.messages = messages;
        this.messagedb = messagedb;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_message, parent, false);

        MessageViewHolder messageViewHolder = new MessageViewHolder(view);
        return messageViewHolder;
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.tvTitle.setText(message.getMessage());
        holder.tvMessageTime.setText(message.getCreatedAt()); // Set the time value
        holder.tvUsername.setText(message.getSender());
        holder.tvTitle.setGravity(Gravity.START);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        ImageButton btnDelete;
        LinearLayout l1;
        TextView tvUsername;
        TextView tvMessageTime;

        public MessageViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tvMessage);
            l1 = (LinearLayout) itemView.findViewById(R.id.l1Message);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUsername);
            tvMessageTime = (TextView) itemView.findViewById(R.id.tvMessageTime);


//            btnDelete.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    int position = getAdapterPosition();
//                    Message message = messages.get(position);
//                    messagedb.child(message.getKey()).removeValue();
//                }
//            });
        }
    }
}
