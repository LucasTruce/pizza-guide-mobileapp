package com.app.pizza;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.app.pizza.model.UserAuthResponse;
import com.app.pizza.model.UserLogin;
import com.app.pizza.service.ServiceGenerator;
import com.app.pizza.service.UserService;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    UserService userService;
    TextInputEditText loginInput;
    TextInputEditText passwordInput;
    TextInputLayout loginLabel;
    TextInputLayout passwordLabel;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userService = ServiceGenerator.createService(UserService.class);

        loginInput = findViewById(R.id.loginUsername);
        passwordInput = findViewById(R.id.loginPassword);
        textView = findViewById(R.id.errorTextLogin);
        loginLabel = findViewById(R.id.loginUsernameLabel);
        passwordLabel = findViewById(R.id.loginPasswordLabel);
        textView.setVisibility(TextView.INVISIBLE);

        findViewById(R.id.loginButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<UserAuthResponse> call = userService.authorization(new UserLogin(loginInput.getText().toString(), passwordInput.getText().toString()));

                final SharedPreferences sharedPref = getSharedPreferences("pref", 0);

                call.enqueue(new Callback<UserAuthResponse>() {
                    @Override
                    public void onResponse(Call<UserAuthResponse> call, Response<UserAuthResponse> response) {
                        textView.setVisibility(TextView.INVISIBLE);
                        loginLabel.setErrorEnabled(false);
                        passwordLabel.setErrorEnabled(false);

                        if(response.isSuccessful()){
                            Intent intent = new Intent(getApplicationContext(), MenuActivity.class);

                            UserAuthResponse userAuthResponse = response.body();

                            //wysyłanie wartości do innego pliku przez sharedpreferences
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("token", "Bearer " + response.body().getToken());
                            editor.putString("username", userAuthResponse.getUsername());
                            editor.putString("email", userAuthResponse.getEmail());
                            editor.apply();

                            Log.d("Rola", userAuthResponse.getRoles().toString());
                            startActivity(intent);

                        }
                        else if(response.code() == 400){
                            Gson gson = new GsonBuilder().create();
                            UserLogin mError;
                            try {
                                mError= gson.fromJson(response.errorBody().string(), UserLogin.class);
                                loginLabel.setError(mError.getUsername());
                                passwordLabel.setError(mError.getPassword());
                                loginLabel.setErrorEnabled(true);
                                passwordLabel.setErrorEnabled(true);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        else if(response.code() == 401)
                        {
                            textView.setVisibility(TextView.VISIBLE);
                            textView.setTextColor(Color.RED);
                        }
                    }
                    @Override
                    public void onFailure(Call<UserAuthResponse> call, Throwable t) {
                    }
                });
            }
        });

        findViewById(R.id.registerButtonMain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });




    }
}
