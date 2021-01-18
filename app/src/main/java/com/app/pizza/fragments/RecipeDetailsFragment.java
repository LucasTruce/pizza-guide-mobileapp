package com.app.pizza.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.app.pizza.R;
import com.app.pizza.adapters.CommentAdapter;
import com.app.pizza.adapters.ComponentAdapter;
import com.app.pizza.adapters.StepAdapter;
import com.app.pizza.model.component.Component;
import com.app.pizza.model.recipe.RecipeById;
import com.app.pizza.model.reviews.AddComment;
import com.app.pizza.model.reviews.Comment;
import com.app.pizza.model.step.Step;
import com.app.pizza.service.CommentService;
import com.app.pizza.service.RecipeService;
import com.app.pizza.service.ServiceGenerator;
import com.app.pizza.utils.FullLengthListView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
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
    private TextInputEditText add_comment_input;
    private TextInputEditText add_grade_input;
    private Button add_comment_button;
    private AddComment addComment = new AddComment();
    private CommentService commentService;


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
        add_grade_input = view.findViewById(R.id.add_grade_input);
        add_comment_input = view.findViewById(R.id.add_comment_input);
        add_comment_button = view.findViewById(R.id.add_comment_button);
        SharedPreferences sharedPref = view.getContext().getSharedPreferences("pref", 0);

        listComments = view.findViewById(R.id.commentsList);
        listComponents = view.findViewById(R.id.componentsList);
        listSteps = view.findViewById(R.id.stepList);

        ArrayList<Comment> comments = new ArrayList<>();
        ArrayList<Step> steps = new ArrayList<>();
        ArrayList<Component> components = new ArrayList<>();

        add_comment_button.setOnClickListener(view1 -> {
            addComment.setDescription(add_comment_input.getText().toString());
            addComment.setScore(Integer.parseInt(add_grade_input.getText().toString()));

            commentService = ServiceGenerator.createService(CommentService.class, sharedPref.getString("token", ""));
            if(addComment.getScore() >= 1 && addComment.getScore() <= 5)
            {
                Call<AddComment> call = commentService.addComment(addComment, sharedPref.getInt("recipeId", 0));

                call.enqueue(new Callback<AddComment>() {
                    @Override
                    public void onResponse(@NotNull Call<AddComment> call, @NotNull Response<AddComment> response) {
                        Log.d("responsecode", String.valueOf(response.code()));
                        if (response.isSuccessful()) {
                            loadFragment(new RecipeDetailsFragment());
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<AddComment> call, @NotNull Throwable t) {
                        Log.d("Message", t.getMessage());
                    }
                });
            }
            else
            {
                Toast.makeText(getContext(), "Skala ocen 1-5", Toast.LENGTH_SHORT).show();
                add_grade_input.setText("5");
            }


        });




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
            }
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