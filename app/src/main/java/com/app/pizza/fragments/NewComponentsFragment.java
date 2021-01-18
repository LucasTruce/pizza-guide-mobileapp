package com.app.pizza.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.app.pizza.R;
import com.app.pizza.adapters.ComponentAdapter;
import com.app.pizza.adapters.IngredientsAdapter;
import com.app.pizza.adapters.NewStepAdapter;
import com.app.pizza.model.chat.Chat;
import com.app.pizza.model.component.Component;
import com.app.pizza.model.ingredient.Ingredient;
import com.app.pizza.model.reviews.Comment;
import com.app.pizza.service.ChatService;
import com.app.pizza.service.IngredientService;
import com.app.pizza.service.ServiceGenerator;
import com.app.pizza.utils.FullLengthListView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewComponentsFragment extends Fragment {

    IngredientService ingredientService;
    SharedPreferences sharedPref;
    FullLengthListView ingredientsList;
    Button add_new_components_button;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    List<Component> components = new ArrayList<>();
    List<Component> componentsToAdd = new ArrayList<>();

    private IngredientsAdapter adapter;

    public NewComponentsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_components, container, false);

        sharedPref = view.getContext().getSharedPreferences("pref", 0);
        ingredientService = ServiceGenerator.createService(IngredientService.class, sharedPref.getString("token", ""));
        recyclerView = view.findViewById(R.id.recycler_ingredients);
        add_new_components_button = view.findViewById(R.id.add_new_components_button);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        Call<List<Ingredient>> call = ingredientService.getIngredients();
        call.enqueue(new Callback<List<Ingredient>>() {

            @Override
            public void onResponse(Call<List<Ingredient>> call, Response<List<Ingredient>> response) {
                if(response.isSuccessful()) {
                    for(Ingredient ingredient : response.body())
                    {
                        components.add(new Component("0", ingredient));
                    }

                    adapter = new IngredientsAdapter(components, getContext());
                    recyclerView.setAdapter(adapter);

                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Ingredient>> call, Throwable t) {

            }
        });

        add_new_components_button.setOnClickListener(view1 ->
        {
            components = adapter.getComponentList();
            for(Component component : components)
            {
                if(component.getAmount() != "0")
                    componentsToAdd.add(component);
            }

            Call<List<Component>> callSecond = ingredientService.addComponents(componentsToAdd, sharedPref.getInt("newRecipeId", 0));
            callSecond.enqueue(new Callback<List<Component>>() {
                @Override
                public void onResponse(Call<List<Component>> call, Response<List<Component>> response) {
                    if(response.isSuccessful()) {
                        loadFragment(new StepAmountFragment());
                    }
                    else if(response.code()==400)
                    {
                        componentsToAdd.clear();
                    }
                }
                @Override
                public void onFailure(Call<List<Component>> call, Throwable t) {
                    Log.d("error", t.getMessage());
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