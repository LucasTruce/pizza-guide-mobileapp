package com.app.pizza.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.pizza.R;
import com.app.pizza.model.media.Media;
import com.app.pizza.model.recipe.RecipeAdd;
import com.app.pizza.service.MediaService;
import com.app.pizza.service.RecipeService;
import com.app.pizza.service.ServiceGenerator;
import com.app.pizza.utils.ImageFilePath;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import lombok.SneakyThrows;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Part;

public class NewMediaFragment extends Fragment {

    int SELECT_PICTURES = 1;
    int REQUEST_PERMISSION = 1;
    Button add_photo;
    Button add_new_media_button;
    ImageView image;
    Uri imageUri;
    TextView textCount;
    SharedPreferences sharedPref;
    List<String> imageList = new ArrayList<>();
    MediaService mediaService;
    List<MultipartBody.Part> partList = new ArrayList<>();
    File file;
    MultipartBody.Part part;
    RequestBody fbody;

    public NewMediaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SneakyThrows
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_PICTURES) {
            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount(); //evaluate the count before the for loop --- otherwise, the count is evaluated every loop.
                textCount.setText(count + "");
                for (int i = 0; i < count; i++) {
                    imageUri = data.getClipData().getItemAt(i).getUri();

                    Log.d("imageuri", ImageFilePath.getPath(getActivity().getApplicationContext(), imageUri));
                    imageList.add(ImageFilePath.getPath(getContext(), imageUri));
                }
                //do something with the image (save it to some directory or whatever you need to do with it here)
            } else if (data.getData() != null) {
                Log.d("jeden", ImageFilePath.getPath(getActivity().getApplicationContext(), data.getData()));
                imageList.add(ImageFilePath.getPath(getActivity().getApplicationContext(), data.getData()));
                textCount.setText("1");
                //do something with the image (save it to some directory or whatever you need to do with it here)
            }
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_media, container, false);
        sharedPref = view.getContext().getSharedPreferences("pref", 0);
        image = view.findViewById(R.id.image);
        add_photo = view.findViewById(R.id.add_photo);
        add_new_media_button = view.findViewById(R.id.add_new_media_button);
        textCount = view.findViewById(R.id.textCount);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //List<String> permissions = new ArrayList<String>();
            //permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            //permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE}, REQUEST_PERMISSION);
        }


        add_photo.setOnClickListener(view1 -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setType("image/*"); //allows any image file type. Change * to specific extension to limit it
            //**The following line is the important one!
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURES); //SELECT_PICTURES is simply a global int used to check the calling intent in onActivityResult
        });

        mediaService = ServiceGenerator.createService(MediaService.class, sharedPref.getString("token", ""));

        add_new_media_button.setOnClickListener(view1 -> {

            for (String temp : imageList) {
                file = new File(temp);
                fbody = RequestBody.create(MediaType.parse("image/*"), file);
                part = MultipartBody.Part.createFormData("image", "image", fbody);
                partList.add(part);
            }


            Call<List<Media>> call = mediaService.addImage(partList, sharedPref.getInt("newRecipeId", 0));
            call.enqueue(new Callback<List<Media>>() {
                @Override
                public void onResponse(@NotNull Call<List<Media>> call, @NotNull Response<List<Media>> response) {
                    loadFragment(new NewComponentsFragment());
                }

                @Override
                public void onFailure(@NotNull Call<List<Media>> call, @NotNull Throwable t) {
                    Log.d("xD", t.getMessage());
                }
            });
        });

        return view;
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment).commit();
        fragmentTransaction.addToBackStack(null);
    }

}