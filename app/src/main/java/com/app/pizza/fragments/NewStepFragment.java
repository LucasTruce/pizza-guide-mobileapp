package com.app.pizza.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.app.pizza.R;
import com.app.pizza.adapters.IngredientsAdapter;
import com.app.pizza.adapters.NewStepAdapter;
import com.app.pizza.adapters.PaginationAdapter;
import com.app.pizza.model.component.Component;
import com.app.pizza.model.ingredient.Ingredient;
import com.app.pizza.model.step.Step;
import com.app.pizza.service.IngredientService;
import com.app.pizza.service.ServiceGenerator;
import com.app.pizza.service.StepSerivce;
import com.app.pizza.utils.FullLengthListView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewStepFragment extends Fragment {

    List<Step> stepArrayList;
    RecyclerView recyclerView;
    SharedPreferences sharedPref;
    LinearLayoutManager linearLayoutManager;
    Button add_and_save_recipe;
    int stepAmount;
    private NewStepAdapter adapter;
    List<Step> stepsToAdd = new ArrayList<>();
    StepSerivce stepSerivce;

    private boolean isLoading = true;
    private int pastVisibleItems, visibleItemCount, totalItemCount, previousTotal = 0;

    public NewStepFragment() {
        // Required empty public constructor
    }

   @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_step, container, false);
        recyclerView = view.findViewById(R.id.steps_recycler);
        add_and_save_recipe = view.findViewById(R.id.add_and_save_recipe);

        stepArrayList = new ArrayList<>();
        sharedPref = view.getContext().getSharedPreferences("pref", 0);
        stepSerivce = ServiceGenerator.createService(StepSerivce.class, sharedPref.getString("token", ""));
        stepAmount = sharedPref.getInt("amount", 0);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        for(int i = 0; i<stepAmount; i++)
        {
            stepArrayList.add(new Step("","",""));
        }
        adapter = new NewStepAdapter(stepArrayList, getContext());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

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
                        isLoading = true;
                    }
                }
            }
        });

        add_and_save_recipe.setOnClickListener(view1 -> {
            for(Step step: adapter.getStepList())
            {
                if(!step.getName().equals("") && !step.getDescription().equals("") && !step.getTime().equals(""))
                {
                    stepsToAdd.add(step);
                }
            }

            Call<List<Step>> call = stepSerivce.addSteps(stepsToAdd, sharedPref.getInt("newRecipeId", 0));
            call.enqueue(new Callback<List<Step>>() {
                @Override
                public void onResponse(Call<List<Step>> call, Response<List<Step>> response) {
                    if(response.isSuccessful()) {
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putInt("recipeId", sharedPref.getInt("newRecipeId", 0));
                        editor.apply();
                        loadFragment(new RecipeDetailsFragment());
                    }
                    else if(response.code()==400){
                        Toast.makeText(getContext(), "Wprowadź prawidłowy format godziny 00:00:00", Toast.LENGTH_SHORT).show();
                        stepsToAdd.clear();
                    }
                }

                @Override
                public void onFailure(Call<List<Step>> call, Throwable t) {
                    Log.d("Test", t.getMessage());
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