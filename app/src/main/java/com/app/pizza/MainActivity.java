package com.app.pizza;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        userService = ServiceGenerator.createService(UserService.class);

        loginInput = findViewById(R.id.loginUsername);
        passwordInput = findViewById(R.id.loginPassword);
        textView = findViewById(R.id.errorTextLogin);

        findViewById(R.id.loginButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<UserAuthResponse> call = userService.authorization(new UserLogin(loginInput.getText().toString(), passwordInput.getText().toString()));

                final SharedPreferences sharedPref = getSharedPreferences("pref", 0);

                call.enqueue(new Callback<UserAuthResponse>() {
                    @Override
                    public void onResponse(Call<UserAuthResponse> call, Response<UserAuthResponse> response) {
                        if(response.isSuccessful()){
                            Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                            Log.d("string",response.body().toString());
                            UserAuthResponse userAuthResponse = response.body();
                            Log.d("TOKEN", userAuthResponse.getToken());

                            //wysyłanie wartości do innego pliku przez sharedpreferences
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("TOKEN", "Bearer " + response.body().getToken());
                            editor.apply();

                            Log.d("Rola", userAuthResponse.getRoles().toString());

                            if(userAuthResponse.getRoles().toString().contains("ROLE_ADMIN")){
                                startActivity(intent);
                            }
                            else
                            {
                                textView.setText("Brak uprawnien administratora");
                                textView.setVisibility(TextView.VISIBLE);
                            }

                        }
                        else
                    {
                        textView.setText("Zle dane");
                        textView.setVisibility(TextView.VISIBLE);
                        Log.d("ERROR","ERROR");
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
