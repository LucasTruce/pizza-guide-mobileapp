package com.app.pizza;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.app.pizza.model.UserAuthResponse;
import com.app.pizza.model.UserLogin;
import com.app.pizza.service.ServiceGenerator;
import com.app.pizza.service.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    UserService userService;
    EditText loginInput;
    EditText passwordInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        userService = ServiceGenerator.createService(UserService.class);

        loginInput = findViewById(R.id.loginName);
        passwordInput = findViewById(R.id.loginPassword);

        findViewById(R.id.loginButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<UserAuthResponse> call = userService.authorization(new UserLogin(loginInput.getText().toString(), passwordInput.getText().toString()));
                call.enqueue(new Callback<UserAuthResponse>() {
                    @Override
                    public void onResponse(Call<UserAuthResponse> call, Response<UserAuthResponse> response) {
                        if(response.isSuccessful()){
                            Log.d("string",response.body().toString());
                            UserAuthResponse userAuthResponse = response.body();
                            Log.d("TOKEN", userAuthResponse.getToken());
                        }}

                    @Override
                    public void onFailure(Call<UserAuthResponse> call, Throwable t) {

                    }
                });
            }
        });

    }
}
