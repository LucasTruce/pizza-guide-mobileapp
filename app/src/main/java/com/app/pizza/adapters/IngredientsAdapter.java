package com.app.pizza.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.app.pizza.R;
import com.app.pizza.model.component.Component;
import com.app.pizza.model.ingredient.Ingredient;
import com.app.pizza.model.ingredient.OneIngredient;
import com.app.pizza.model.reviews.Comment;
import com.app.pizza.model.step.Step;
import com.google.android.material.textfield.TextInputEditText;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import lombok.SneakyThrows;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.MyViewHolder> {

    private List<Component> componentList;
    private Context context;

    public IngredientsAdapter(List<Component> componentList, Context context) {
        this.componentList = componentList;
        this.context = context;
    }

    @NonNull
    @Override
    public IngredientsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_ingredients, parent, false);
        return new IngredientsAdapter.MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SneakyThrows
    @Override
    public void onBindViewHolder(@NonNull IngredientsAdapter.MyViewHolder holder, int position) {

        Component component = componentList.get(position);

        holder.labelIngredientsName.setText(component.getIngredients().getName());
        holder.labelIngredientsAmount.setText(component.getAmount());

        holder.labelIngredientsAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                componentList.get(position).setAmount(s.toString().trim());
            }
        });



    }

    public List<Component> getComponentList() {
        return componentList;
    }

    @Override
    public int getItemCount() {
        return componentList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView labelIngredientsName;
        private EditText labelIngredientsAmount;

        public MyViewHolder(View itemView) {
            super(itemView);
            labelIngredientsName = itemView.findViewById(R.id.labelIngredientsName);
            labelIngredientsAmount = itemView.findViewById(R.id.labelIngredientsAmount);
        }
    }
}
