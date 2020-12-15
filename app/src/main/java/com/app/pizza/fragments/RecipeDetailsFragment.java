package com.app.pizza.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.app.pizza.R;
import com.app.pizza.adapters.CommentAdapter;
import com.app.pizza.adapters.ComponentAdapter;
import com.app.pizza.adapters.StepAdapter;
import com.app.pizza.model.component.Component;
import com.app.pizza.model.recipe.RecipeById;
import com.app.pizza.model.reviews.Comment;
import com.app.pizza.model.step.Step;
import com.app.pizza.service.RecipeService;
import com.app.pizza.service.ServiceGenerator;
import com.app.pizza.utils.FullLengthListView;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeDetailsFragment extends Fragment {
    private FullLengthListView listComments;
    private FullLengthListView listSteps;
    private FullLengthListView listComponents;
    private ArrayAdapter<String> adapter;
    private ImageView cardImage;
    private TextView inputName;
    private TextView inputAuthor;
    private TextView inputDesc;


    RecipeService recipeService;

    public RecipeDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_recipe_details, container, false);
        cardImage = view.findViewById(R.id.cardImage);
        inputAuthor = view.findViewById(R.id.inputAuthor);
        inputDesc = view.findViewById(R.id.inputDesc);
        inputName = view.findViewById(R.id.inputName);



        listComments = view.findViewById(R.id.commentsList);
        listComponents = view.findViewById(R.id.componentsList);
        listSteps = view.findViewById(R.id.stepList);

        ArrayList<Comment> comments = new ArrayList<>();
        ArrayList<Step> steps = new ArrayList<>();
        ArrayList<Component> components = new ArrayList<>();


        SharedPreferences sharedPref = view.getContext().getSharedPreferences("pref", 0);
        recipeService = ServiceGenerator.createService(RecipeService.class, sharedPref.getString("token", ""));
        Call<RecipeById> call = recipeService.getRecipeById(sharedPref.getInt("recipeId", 0));

        call.enqueue(new Callback<RecipeById>() {
            @Override
            public void onResponse(@NotNull Call<RecipeById> call, @NotNull Response<RecipeById> response) {
                if(response.isSuccessful())
                {
                    RecipeById recipe = response.body();
                    steps.addAll(recipe.getSteps());
                    comments.addAll(recipe.getReviews());
                    components.addAll(recipe.getComponents());

                    inputAuthor.setText(recipe.getUser().getUsername());
                    inputDesc.setText(recipe.getDescription());
                    inputName.setText(recipe.getName());
                    Picasso.get().load(recipe.getMediaList().get(0).getLink()).into(cardImage);

                    CommentAdapter commentAdapter = new CommentAdapter(getActivity().getApplicationContext(), R.layout.row_comments, comments);
                    StepAdapter stepAdapter = new StepAdapter(getActivity().getApplicationContext(), R.layout.row_steps, steps);
                    ComponentAdapter componentAdapter = new ComponentAdapter(getActivity().getApplicationContext(), R.layout.row_components, components);
                    listComponents.setAdapter(componentAdapter);
                    listComments.setAdapter(commentAdapter);
                    listSteps.setAdapter(stepAdapter);
                }
            }

            @Override
            public void onFailure(@NotNull Call<RecipeById> call, @NotNull Throwable t) {
                Log.d("Error", "Error");
            }
        });



        return view;
    }
}