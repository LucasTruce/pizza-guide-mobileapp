package com.app.pizza.fragments;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.pizza.R;
import com.app.pizza.adapters.MessageListAdapter;
import com.app.pizza.model.message.Message;
import com.app.pizza.model.message.MessageResponse;
import com.app.pizza.model.message.MessageSend;
import com.app.pizza.service.MessageService;
import com.app.pizza.service.ServiceGenerator;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageFragment extends Fragment {

    RecyclerView recyclerView;
    MessageService messageService;
    LinearLayoutManager linearLayoutManager;
    Button sendButton;
    EditText sendText;
    List<Message> messages;

    private MessageListAdapter adapter;
    private int pageNumber = 0;
    //variables for pagination
    private boolean isLoading = true;
    private int pastVisibleItems, visibleItemCount, totalItemCount, previousTotal = 0;

    SharedPreferences sharedPref;

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

        sendButton = view.findViewById(R.id.button_chatbox_send);
        sendText = view.findViewById(R.id.message_chatbox);
        recyclerView = view.findViewById(R.id.reyclerview_message_list);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        sharedPref = view.getContext().getSharedPreferences("pref", 0);
        messageService = ServiceGenerator.createService(MessageService.class, sharedPref.getString("token", ""));
        Call<MessageResponse> call = messageService.getMessages(Integer.parseInt(sharedPref.getString("chatId","")));

        call.enqueue(new Callback<MessageResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(@NotNull Call<MessageResponse> call, @NotNull Response<MessageResponse> response) {
                if(response.isSuccessful()){
                    messages = response.body().getContent();

                    adapter = new MessageListAdapter(messages, MessageFragment.this.getContext());
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(@NotNull Call<MessageResponse> call, @NotNull Throwable t) {
                Log.d("test", "dwa");
            }
        });


        sendButton.setOnClickListener(view1 -> {
            Call<Message> messageCall = messageService.saveMessage(Integer.parseInt(sharedPref.getString("chatId","")), new MessageSend(sendText.getText().toString()));
            messageCall.enqueue(new Callback<Message>() {
                @Override
                public void onResponse(Call<Message> call, Response<Message> response) {
                    if(response.isSuccessful()){
                        Message message = response.body();
                        messages.add(message);
                        adapter = new MessageListAdapter(messages, MessageFragment.this.getContext());
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<Message> call, Throwable t) {
                    Log.d("test2222", "dwa22222");
                }
            });
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = linearLayoutManager.getChildCount();
                totalItemCount = linearLayoutManager.getItemCount();
                pastVisibleItems = linearLayoutManager.findFirstVisibleItemPosition();

                if(dy>0){
                    if(isLoading){
                        if(totalItemCount > previousTotal) {
                            isLoading = false;
                            previousTotal = totalItemCount;
                        }
                    }
                    if(!isLoading && (pastVisibleItems + visibleItemCount) >= totalItemCount){
                        pageNumber++;
                        performPagination();
                        isLoading = true;
                    }
                }
            }
        });

        return view;
    }

    private void performPagination() {
        Call<MessageResponse> call = messageService.getMessages(Integer.parseInt(sharedPref.getString("chatId","")), pageNumber);

        call.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(@NotNull Call<MessageResponse> call, @NotNull Response<MessageResponse> response) {

                if(response.body().getNumber() != response.body().getTotalPages()  ){
                    List<Message> messages = response.body().getContent();
                    adapter.addMessages(messages);
                    Log.d("number", "" + pageNumber);
                    Toast.makeText(MessageFragment.this.getContext(), "Page " + pageNumber, Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(MessageFragment.this.getContext(), "No more data to display", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(@NotNull Call<MessageResponse> call, @NotNull Throwable t) {

            }
        });
    }
}