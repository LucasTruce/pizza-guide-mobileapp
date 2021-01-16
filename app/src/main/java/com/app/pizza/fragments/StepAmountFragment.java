package com.app.pizza.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.app.pizza.R;
import com.google.android.material.textfield.TextInputEditText;

public class StepAmountFragment extends Fragment {

    Button add_step_amount_button;
    SharedPreferences sharedPref;
    TextInputEditText add_step_amount;
    int stepAmount;

    public StepAmountFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step_amount, container, false);

        add_step_amount_button = view.findViewById(R.id.add_step_amount_button);
        sharedPref = view.getContext().getSharedPreferences("pref", 0);


        add_step_amount_button.setOnClickListener(view1 ->
        {
            add_step_amount = view.findViewById(R.id.add_step_amount);
            stepAmount = Integer.parseInt(add_step_amount.getText().toString());
            SharedPreferences.Editor editor = sharedPref.edit();

            editor.putInt("amount", stepAmount);
            editor.apply();
            loadFragment(new NewStepFragment());
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