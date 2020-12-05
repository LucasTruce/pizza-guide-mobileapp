package com.app.pizza.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.app.pizza.R;
import com.app.pizza.model.Comment;

import java.util.ArrayList;

public class CommentAdapter extends ArrayAdapter<Comment> {
    private Context mContext;
    private int mResource;

    public CommentAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Comment> objects) {
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

        RatingBar score = convertView.findViewById(R.id.labelRate);
        TextView name = convertView.findViewById(R.id.labelCommentName);
        TextView comment = convertView.findViewById(R.id.labelComment);

        score.setRating(getItem(position).getScore());
        //name.setText(getItem(position).getUser().getUsername());
        name.setText("ADAMCWEL");
        comment.setText(getItem(position).getDescription());


        return convertView;
    }

}
