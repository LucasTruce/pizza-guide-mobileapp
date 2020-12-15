package com.app.pizza.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.app.pizza.R;
import com.app.pizza.model.message.Message;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class MessageListAdapter extends RecyclerView.Adapter {
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    private Context mContext;
    private List<Message> mMessageList;

    public MessageListAdapter(List<Message> messageList, Context context) {
        mContext = context;
        mMessageList = messageList;
        //nie wiem czy tu nie mozna wykonać bezpośrednio calla i serwisu by pobierac dane po wyslaniu
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    // Determines the appropriate ViewType according to the sender of the message.
    @Override
    public int getItemViewType(int position) {
        Message message = (Message) mMessageList.get(position);

        SharedPreferences sharedPref = mContext.getSharedPreferences("pref", 0);
        String username = sharedPref.getString("username","");
        if (message.getSender().getUsername().equals(username)) {
            // If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            // If some other user sent the message
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    // Inflates the appropriate layout according to the ViewType.
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.message_item_send, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.message_item_received, parent, false);
            return new ReceivedMessageHolder(view);
        }

        return null;
    }

    // Passes the message object to a ViewHolder so that the contents can be bound to UI.
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Message message = (Message) mMessageList.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(message);
        }
    }

    private static class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText;

        SentMessageHolder(View itemView) {
            super(itemView);

            messageText = (TextView) itemView.findViewById(R.id.text_message_body);
            timeText = (TextView) itemView.findViewById(R.id.text_message_time);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        void bind(Message message) {
            messageText.setText(message.getMessage());
            LocalDateTime localDateTime = LocalDateTime.parse(message.getSendTime());
            timeText.setText(localDateTime.format(DateTimeFormatter.ofPattern("HH:mm")));
        }
    }

    private static class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText, nameText;

        ReceivedMessageHolder(View itemView) {
            super(itemView);

            messageText = (TextView) itemView.findViewById(R.id.text_message_body_received);
            timeText = (TextView) itemView.findViewById(R.id.text_message_time_received);
            nameText = (TextView) itemView.findViewById(R.id.text_message_name_received);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        void bind(Message message) {
            messageText.setText(message.getMessage());
            nameText.setText(message.getSender().getUsername());
            LocalDateTime localDateTime = LocalDateTime.parse(message.getSendTime());
            timeText.setText(localDateTime.format(DateTimeFormatter.ofPattern("HH:mm")));
        }

    }

    public void addMessages(List<Message> messages){
        mMessageList.addAll(messages);
        notifyDataSetChanged();
    }
}