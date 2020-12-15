package com.app.pizza.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.app.pizza.R;
import com.app.pizza.fragments.MessageFragment;
import com.app.pizza.model.chat.Chat;
import com.app.pizza.utils.Helper;

import java.util.List;

import lombok.SneakyThrows;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {

    private List<Chat> chatList;
    private Context context;

    public ChatAdapter(List<Chat> chatList, Context context) {
        this.chatList = chatList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_list, parent,false );
        return new MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SneakyThrows
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Chat chat = chatList.get(position);

        holder.inputMessage.setText(chat.getMessage().get(0).getMessage());
        holder.inputTime.setText(Helper.setDataTime(chat.getMessage().get(0).getSendTime()));
        holder.inputName.setText(chat.getReceiver().getUsername());
        holder.linearLayout.setOnClickListener(view -> {
            Fragment fr = new MessageFragment();
            final SharedPreferences sharedPref = view.getContext().getSharedPreferences("pref", 0);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("chatId", chat.getId());
            editor.apply();
            loadFragment(fr);
        });
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView inputName;
        private TextView inputMessage;
        private TextView inputTime;
        private LinearLayout linearLayout;


        public MyViewHolder(View itemView) {
            super(itemView);
            inputName = itemView.findViewById(R.id.inputName);
            linearLayout = itemView.findViewById(R.id.chatCard);
            inputMessage = itemView.findViewById(R.id.inputMessage);
            inputTime = itemView.findViewById(R.id.inputTime);
        }
    }

    public void addChat(List<Chat> packages) {
        chatList.addAll(packages);
        notifyDataSetChanged();
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager= ((AppCompatActivity)context).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame,fragment).commit();
        fragmentTransaction.addToBackStack(null);
    }

}
