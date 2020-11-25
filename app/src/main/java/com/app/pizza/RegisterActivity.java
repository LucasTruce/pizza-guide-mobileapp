package com.app.pizza;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.app.pizza.model.UserRegister;
import com.app.pizza.model.UserRegisterResponse;
import com.app.pizza.service.ServiceGenerator;
import com.app.pizza.service.UserService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    UserService userService;
    EditText loginInput;
    EditText passwordInput;
    EditText emailInput;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        userService = ServiceGenerator.createService(UserService.class);

        loginInput = findViewById(R.id.registerUsername);
        passwordInput = findViewById(R.id.registerPassword);
        emailInput = findViewById(R.id.registerEmail);
        textView = findViewById(R.id.errorTextRegister);




        findViewById(R.id.registerButtonRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Call<UserRegisterResponse> call = userService.registration(new UserRegister(loginInput.getText().toString(), passwordInput.getText().toString(), emailInput.getText().toString()));

                call.enqueue(new Callback<UserRegisterResponse>() {
                    @Override
                    public void onResponse(Call<UserRegisterResponse> call, Response<UserRegisterResponse> response) {
                        if(response.isSuccessful()){
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        }
                        else
                        {
                            textView.setText("Zle dane");
                            textView.setVisibility(TextView.VISIBLE);

                            Gson gson = new GsonBuilder().create();
                            UserRegisterResponse mError;
                            try {
                                mError= gson.fromJson(response.errorBody().string(), UserRegisterResponse.class);
                                Log.d("msg", mError.getEmail() + mError.getUsername());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<UserRegisterResponse> call, Throwable t) {
                    }
                });

            }
        });










    }
}
