package com.app.pizza.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.pizza.R;

public class NewMediaFragment extends Fragment {

    int SELECT_PICTURES = 1;
    Button add_photo;
    Button add_new_media_button;
    ImageView image;
    Uri imageUri;
    TextView textCount;

    public NewMediaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PICTURES) {
            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount(); //evaluate the count before the for loop --- otherwise, the count is evaluated every loop.
                textCount.setText(count + "");
                for (int i = 0; i < count; i++)
                    imageUri = data.getClipData().getItemAt(i).getUri();
                //do something with the image (save it to some directory or whatever you need to do with it here)
            }
            else if (data.getData() != null) {
                textCount.setText("1");
                String imagePath = data.getData().getPath();
                //do something with the image (save it to some directory or whatever you need to do with it here)
            }
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_media, container, false);

        image = view.findViewById(R.id.image);
        add_photo = view.findViewById(R.id.add_photo);
        add_new_media_button = view.findViewById(R.id.add_new_media_button);
        textCount = view.findViewById(R.id.textCount);

        add_photo.setOnClickListener(view1 -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*"); //allows any image file type. Change * to specific extension to limit it
//**The following line is the important one!
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURES); //SELECT_PICTURES is simply a global int used to check the calling intent in onActivityResult
        });

        add_new_media_button.setOnClickListener(view1 -> {
            loadFragment(new NewComponentsFragment());
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