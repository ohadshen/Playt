package com.example.playt;

import static com.google.android.material.internal.ContextUtils.getActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.openalpr.OpenALPR;
import org.openalpr.model.Candidate;
import org.openalpr.model.Coordinate;
import org.openalpr.model.Result;
import org.openalpr.model.Results;
import org.openalpr.model.ResultsError;

import java.io.File;
import java.io.IOException;

public class AddPost extends AppCompatActivity {
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        chooseImage();
    }

    private static final int PICK_IMAGE = 1;

    private void chooseImage() {
        imageView = (ImageView) findViewById(R.id.imageView);

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE);
    }

    private String getPlateByALPR(String imagePath) {
        final String ANDROID_DATA_DIR = this.getApplicationInfo().dataDir;
        final String openAlprConfFile = ANDROID_DATA_DIR + File.separatorChar + "runtime_data" + File.separatorChar + "openalpr.conf";

        try {
            String result = OpenALPR.Factory.create(AddPost.this, ANDROID_DATA_DIR)
                    .recognizeWithCountryRegionNConfig("us", "", imagePath, openAlprConfFile, 10);

            Results results = new Gson().fromJson(result, Results.class);

            return results.getResults().get(0).getPlate();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                String imagePath = data.getData().getLastPathSegment();
                String carPlate = getPlateByALPR(imagePath);

                if (carPlate != null) {
                    System.out.println(carPlate);
                    // TODO: send to server and continue the flow
                } else {
                    System.out.println("no car plate found");
                    // TODO: show message that car plate not found
                }


                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}