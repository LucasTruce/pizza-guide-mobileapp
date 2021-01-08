package com.app.pizza.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.app.pizza.R;
import com.app.pizza.model.component.Component;
import com.app.pizza.model.ingredient.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class IngredientsAdapter extends ArrayAdapter<Ingredient> {
    private Context mContext;
    private int mResource;

    public IngredientsAdapter(@NonNull Context context, int resource, @NonNull List<Ingredient> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        convertView = layoutInflater.inflate(mResource, parent, false);

        TextView name = convertView.findViewById(R.id.labelIngredientsName);
        name.setText(getItem(position).getName());

        return convertView;
    }

}
