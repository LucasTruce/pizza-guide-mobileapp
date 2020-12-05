package com.app.pizza.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.app.pizza.R;
import com.app.pizza.model.component.Component;

import java.util.ArrayList;

public class ComponentAdapter extends ArrayAdapter<Component> {
    private Context mContext;
    private int mResource;

    public ComponentAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Component> objects) {
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

        TextView name = convertView.findViewById(R.id.labelComponentName);
        TextView amount = convertView.findViewById(R.id.labelComponentAmount);

        name.setText(getItem(position).getIngredients().getName());
        amount.setText(getItem(position).getAmount()+"g");


        return convertView;
    }

}
