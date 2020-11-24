package com.app.pizza;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        SharedPreferences sharedPref = getSharedPreferences("pref", 0);
        Log.d("TOKEN2", sharedPref.getString("TOKEN",""));


        textView = findViewById(R.id.menuText);
        textView.setText(sharedPref.getString("TOKEN",""));

    }


}
