package com.app.pizza.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.pizza.R;
import com.app.pizza.adapters.IngredientsAdapter;
import com.app.pizza.adapters.NewStepAdapter;
import com.app.pizza.adapters.PaginationAdapter;
import com.app.pizza.model.step.Step;
import com.app.pizza.utils.FullLengthListView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class NewStepFragment extends Fragment {

    List<Step> stepArrayList;
    RecyclerView recyclerView;
    SharedPreferences sharedPref;
    LinearLayoutManager linearLayoutManager;
    int stepAmount;
    private NewStepAdapter adapter;

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
        stepArrayList = new ArrayList<>();
        sharedPref = view.getContext().getSharedPreferences("pref", 0);
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



        return view;
    }
}