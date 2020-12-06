package com.app.pizza.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.app.pizza.R;
import com.app.pizza.fragments.RecipeDetailsFragment;
import com.app.pizza.model.recipe.Recipe;
import com.app.pizza.model.step.Step;
import com.app.pizza.utils.Helper;
import com.squareup.picasso.Picasso;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import lombok.SneakyThrows;

public class PaginationAdapter extends RecyclerView.Adapter<PaginationAdapter.MyViewHolder> {

    private List<Recipe> recipeList;
    private Context context;

    public PaginationAdapter(List<Recipe> recipeList, Context context) {
        this.recipeList = recipeList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipes_card_list, parent,false );
        return new MyViewHolder(view);
    }

    @SneakyThrows
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Recipe recipe = recipeList.get(position);

        String fullTime = "00:00:00";

        for (Step step : recipe.getSteps()
             ) {
            fullTime = Helper.setTime(fullTime, step.getTime());
        }

        holder.cardTitle.setText(recipe.getName());
        holder.cardDescription.setText(recipe.getDescription());
        holder.cardStepsTime.setText(fullTime);
        Picasso.get().load(recipe.getMediaList().get(0).getLink()).into(holder.image);
        holder.linearLayout.setOnClickListener(view -> {
            Fragment fr = new RecipeDetailsFragment();
            final SharedPreferences sharedPref = view.getContext().getSharedPreferences("pref", 0);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("recipeId", recipe.getId());
            editor.apply();
            loadFragment(fr);
        });
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView cardTitle;
        private TextView cardDescription;
        private TextView cardStepsTime;
        private ImageView image;
        private LinearLayout linearLayout;


        public MyViewHolder(View itemView) {
            super(itemView);
            cardTitle = itemView.findViewById(R.id.cardTitle);
            cardDescription = itemView.findViewById(R.id.cardDescription);
            cardStepsTime = itemView.findViewById(R.id.cardStepsTime);
            image = itemView.findViewById(R.id.cardImage);
            linearLayout = itemView.findViewById(R.id.recipeId);

        }
    }

    public void addRecipes(List<Recipe> packages) {
        recipeList.addAll(packages);
        notifyDataSetChanged();
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager= ((AppCompatActivity)context).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame,fragment).commit();
        fragmentTransaction.addToBackStack(null);
    }

}
