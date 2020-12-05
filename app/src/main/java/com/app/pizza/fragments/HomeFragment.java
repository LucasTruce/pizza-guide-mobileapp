package com.app.pizza.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.pizza.R;
import com.app.pizza.model.Recipe;
import com.app.pizza.model.RecipeResponse;
import com.app.pizza.service.RecipeService;
import com.app.pizza.service.ServiceGenerator;
import com.app.pizza.utils.PaginationAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    RecyclerView recyclerView;
    ProgressBar progressBar;
    LinearLayoutManager linearLayoutManager;
    RecipeService recipeService;

    private PaginationAdapter adapter;
    private int pageNumber = 0;
    //variables for pagination
    private boolean isLoading = true;
    private int pastVisibleItems, visibleItemCount, totalItemCount, previousTotal = 0;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        progressBar = view.findViewById(R.id.recycler_progress);
        recyclerView = view.findViewById(R.id.main_recycler);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        SharedPreferences sharedPref = view.getContext().getSharedPreferences("pref", 0);
        recipeService = ServiceGenerator.createService(RecipeService.class, sharedPref.getString("TOKEN", ""));

        Call<RecipeResponse> call = recipeService.getRecipes();

        call.enqueue(new Callback<RecipeResponse>() {
            @Override
            public void onResponse(Call<RecipeResponse> call, Response<RecipeResponse> response) {

                List<Recipe> recipes = response.body().getContent();
                adapter = new PaginationAdapter(recipes, HomeFragment.this.getContext());
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<RecipeResponse> call, Throwable t) {

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

        progressBar.setVisibility(View.VISIBLE);
        Call<RecipeResponse> call = recipeService.getRecipes(pageNumber);

        call.enqueue(new Callback<RecipeResponse>() {
            @Override
            public void onResponse(Call<RecipeResponse> call, Response<RecipeResponse> response) {

                if(response.body().getNumber() != response.body().getTotalPages()  ){
                    List<Recipe> recipes = response.body().getContent();
                    adapter.addRecipes(recipes);
                    Log.d("number", "" + pageNumber);
                    Toast.makeText(HomeFragment.this.getContext(), "Strona " + pageNumber, Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(HomeFragment.this.getContext(), "Brak więcej danych do wyświetlenia...", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<RecipeResponse> call, Throwable t) {

            }
        });
    }
}