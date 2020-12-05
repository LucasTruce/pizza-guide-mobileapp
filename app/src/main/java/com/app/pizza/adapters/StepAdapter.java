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
import com.app.pizza.model.step.Step;

import java.util.ArrayList;

public class StepAdapter extends ArrayAdapter<Step> {
    private Context mContext;
    private int mResource;

    public StepAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Step> objects) {
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

        TextView name = convertView.findViewById(R.id.labelStepName);
        TextView time = convertView.findViewById(R.id.labelStepTime);
        TextView description = convertView.findViewById(R.id.labelStepDescription);

        name.setText(getItem(position).getName());
        time.setText(getItem(position).getTime());
        description.setText(getItem(position).getDescription());


        return convertView;
    }
}
