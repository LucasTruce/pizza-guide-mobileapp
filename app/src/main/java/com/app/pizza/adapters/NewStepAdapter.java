package com.app.pizza.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.app.pizza.R;
import com.app.pizza.fragments.RecipeDetailsFragment;
import com.app.pizza.model.recipe.Recipe;
import com.app.pizza.model.step.Step;
import com.app.pizza.utils.Helper;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import lombok.SneakyThrows;

public class NewStepAdapter extends RecyclerView.Adapter<NewStepAdapter.MyViewHolder> {

    private List<Step> stepList;
    private Context context;

    public NewStepAdapter(List<Step> stepList, Context context) {
        this.stepList = stepList;
        this.context = context;
    }

    @NonNull
    @Override
    public NewStepAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_new_step, parent, false);
        return new NewStepAdapter.MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SneakyThrows
    @Override
    public void onBindViewHolder(@NonNull NewStepAdapter.MyViewHolder holder, int position) {

        Step step = stepList.get(position);
        String number = String.valueOf(position+1);

        holder.input_step_number.setText(number);
        holder.name.setText("");
        holder.description.setText("");
        holder.time.setText("");
    }

    @Override
    public int getItemCount() {
        return stepList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextInputEditText name;
        private TextInputEditText time;
        private TextInputEditText description;
        private TextView input_step_number;


        public MyViewHolder(View itemView) {
            super(itemView);
            input_step_number = itemView.findViewById(R.id.input_step_number);
            name = itemView.findViewById(R.id.add_step_name);
            time = itemView.findViewById(R.id.add_step_time);
            description = itemView.findViewById(R.id.add_step_desc);
        }
    }
}
