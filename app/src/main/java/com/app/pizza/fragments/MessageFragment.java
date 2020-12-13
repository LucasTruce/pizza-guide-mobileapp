package com.app.pizza.fragments;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.pizza.R;
import com.app.pizza.adapters.ChatAdapter;
import com.app.pizza.adapters.MessageListAdapter;
import com.app.pizza.model.chat.Chat;
import com.app.pizza.model.chat.ChatPagination;
import com.app.pizza.model.message.Message;
import com.app.pizza.model.message.MessageResponse;
import com.app.pizza.service.ChatService;
import com.app.pizza.service.MessageService;
import com.app.pizza.service.ServiceGenerator;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageFragment extends Fragment {

    RecyclerView recyclerView;
    MessageService messageService;
    LinearLayoutManager linearLayoutManager;

    private MessageListAdapter adapter;
    private int pageNumber = 0;
    //variables for pagination
    private boolean isLoading = true;
    private int pastVisibleItems, visibleItemCount, totalItemCount, previousTotal = 0;

    public MessageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);

        recyclerView = view.findViewById(R.id.reyclerview_message_list);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        SharedPreferences sharedPref = view.getContext().getSharedPreferences("pref", 0);
        messageService = ServiceGenerator.createService(MessageService.class, sharedPref.getString("token", ""));
        Call<MessageResponse> call = messageService.getMessages(Integer.parseInt(sharedPref.getString("chatId","")));

        call.enqueue(new Callback<MessageResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(@NotNull Call<MessageResponse> call, @NotNull Response<MessageResponse> response) {
                List<Message> messages = response.body().getContent();

                adapter = new MessageListAdapter(messages, MessageFragment.this.getContext());
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(@NotNull Call<MessageResponse> call, @NotNull Throwable t) {
                Log.d("test", "dwa");
            }
        });


        return view;
    }
}