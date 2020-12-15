package com.app.pizza.fragments;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.pizza.R;
import com.app.pizza.adapters.ChatAdapter;
import com.app.pizza.model.chat.Chat;
import com.app.pizza.model.chat.ChatPagination;
import com.app.pizza.service.ChatService;
import com.app.pizza.service.ServiceGenerator;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatFragment extends Fragment {

    RecyclerView recyclerView;
    ChatService chatService;
    LinearLayoutManager linearLayoutManager;

    private ChatAdapter adapter;
    private int pageNumber = 0;
    //variables for pagination
    private boolean isLoading = true;
    private int pastVisibleItems, visibleItemCount, totalItemCount, previousTotal = 0;

    public ChatFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        recyclerView = view.findViewById(R.id.chat_recycler);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        SharedPreferences sharedPref = view.getContext().getSharedPreferences("pref", 0);
        chatService = ServiceGenerator.createService(ChatService.class, sharedPref.getString("token", ""));
        Call<ChatPagination> call = chatService.getChats();

        call.enqueue(new Callback<ChatPagination>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(@NotNull Call<ChatPagination> call, @NotNull Response<ChatPagination> response) {
                List<Chat> chats = response.body().getContent();

                Collections.sort(chats, (o1, o2) -> {
                    try {
                        LocalDateTime time1 = LocalDateTime.parse(o1.getMessage().get(0).getSendTime());
                        LocalDateTime time2 = LocalDateTime.parse(o2.getMessage().get(0).getSendTime());
                        return time1.compareTo(time2);
                    } catch (Exception e) {
                        throw new IllegalArgumentException(e);
                    }
                });

                Collections.reverse(chats);

                adapter = new ChatAdapter(chats, ChatFragment.this.getContext());
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(@NotNull Call<ChatPagination> call, @NotNull Throwable t) {
            }
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
        Call<ChatPagination> call = chatService.getChats(pageNumber);

        call.enqueue(new Callback<ChatPagination>() {
            @Override
            public void onResponse(@NotNull Call<ChatPagination> call, @NotNull Response<ChatPagination> response) {

                if (response.body().getNumber() != response.body().getTotalPages()) {
                    List<Chat> chats = response.body().getContent();
                    adapter.addChat(chats);
                    Log.d("number", "" + pageNumber);
                    Toast.makeText(ChatFragment.this.getContext(), "Strona " + pageNumber, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ChatFragment.this.getContext(), "Brak więcej danych do wyświetlenia...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<ChatPagination> call, @NotNull Throwable t) {

            }
        });
    }
}