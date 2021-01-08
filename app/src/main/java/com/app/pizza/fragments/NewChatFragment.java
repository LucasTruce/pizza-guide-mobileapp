package com.app.pizza.fragments;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.app.pizza.R;
import com.app.pizza.adapters.MessageListAdapter;
import com.app.pizza.model.chat.Chat;
import com.app.pizza.model.chat.ChatError;
import com.app.pizza.model.message.MessageResponse;
import com.app.pizza.model.user.User;
import com.app.pizza.model.user.UserRegisterResponse;
import com.app.pizza.service.ChatService;
import com.app.pizza.service.ServiceGenerator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NewChatFragment extends Fragment {

    TextInputLayout add_Chat_Username_Label;
    TextInputEditText add_Chat_Username;
    Button add_new_chat_button;

    ChatService chatService;
    SharedPreferences sharedPref;
    public NewChatFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_chat, container, false);

        add_Chat_Username = view.findViewById(R.id.add_Chat_Username);
        add_new_chat_button = view.findViewById(R.id.add_new_chat_button);
        add_Chat_Username_Label = view.findViewById(R.id.add_Chat_Username_Label);

        sharedPref = view.getContext().getSharedPreferences("pref", 0);
        chatService = ServiceGenerator.createService(ChatService.class, sharedPref.getString("token", ""));



        add_new_chat_button.setOnClickListener(view1 -> {
            User user = new User(add_Chat_Username.getText().toString());
            Call<Chat> call = chatService.saveChat(user);
            call.enqueue(new Callback<Chat>() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onResponse(@NotNull Call<Chat> call, @NotNull Response<Chat> response) {
                    if(response.isSuccessful()){
                        Chat chat = response.body();
                        Fragment fr = new MessageFragment();
                        final SharedPreferences sharedPref = view.getContext().getSharedPreferences("pref", 0);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("chatId", chat.getId());
                        editor.apply();
                        loadFragment(fr);
                    }
                    else if(response.code() == 404)
                    {
                        Gson gson = new GsonBuilder().create();
                        ChatError chatError;
                        try {
                            Log.d("jd", response.errorBody().toString());
                            chatError= gson.fromJson(response.errorBody().string(), ChatError.class);
                            add_Chat_Username_Label.setError(chatError.getMessage());
                            add_Chat_Username_Label.setErrorEnabled(true);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(@NotNull Call<Chat> call, @NotNull Throwable t) {

                }
            });


        });

        return view;
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager= getFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame,fragment).commit();
        fragmentTransaction.addToBackStack(null);
    }
}