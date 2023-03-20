package com.example.playt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;

//import org.openalpr.OpenALPR;
//import org.openalpr.model.Candidate;
//import org.openalpr.model.Coordinate;
//import org.openalpr.model.Result;
//import org.openalpr.model.Results;
//import org.openalpr.model.ResultsError;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

public class AddPost extends AppCompatActivity {
    private ImageView imageView;
    private String carPlate;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("user_preferences", Context.MODE_PRIVATE);


        setContentView(R.layout.activity_add_post);

        chooseImage();
    }

    public void addPost() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                final String URL = Constants.SERVER_URL + "/posts";
                try {
                    // Create a new HTTP client
                    HttpClient client = new DefaultHttpClient();

                    // Create a new HTTP request with the server URL
                    HttpPost request = new HttpPost(URL);

                    // Set the request headers
                    request.setHeader("Content-Type", "application/json");
                    request.setHeader("Accept", "application/json");

                    // Create a JSONObject to store the data
                    JSONObject data = new JSONObject();

                    // Add the image to the data as base64-encoded string
                    imageView.setDrawingCacheEnabled(true);
                    imageView.buildDrawingCache();
                    Bitmap bitmap = imageView.getDrawingCache();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    String base64Image = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    data.put("image", base64Image);

                    // Add the text to the data
                    EditText editText = findViewById(R.id.playtTitleText);
                    String text = editText.getText().toString();
                    data.put("title", text);

                    data.put("username", sharedPreferences.getString("username", ""));

                    data.put("carPlate", carPlate);

                    // Set the request body
                    StringEntity entity = new StringEntity(data.toString());
                    request.setEntity(entity);

                    // Execute the request and get the response
                    HttpResponse response = client.execute(request);

                    // Get the response status code
                    int statusCode = response.getStatusLine().getStatusCode();

                    if (statusCode == 200) {
                        // If the request was successful, get the response body
                        HttpEntity responseEntity = response.getEntity();
                        String responseBody = EntityUtils.toString(responseEntity);

                        // User is signed in
                        Intent i = new Intent(AddPost.this, MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);

                        // Return the response body
                        return responseBody;
                    } else {
                        // If the request failed, log an error
                        Log.e("HTTP error", "Server returned status code: " + statusCode);
                    }
                } catch (IOException | JSONException e) {
                    // If an exception was thrown, log the error
                    Log.e("HTTP error", "Error making HTTP request: " + e.getMessage());
                }

                return null;

        }}.execute();
    }


    public void makeHttpRequest(String plate) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                final String URL = Constants.SERVER_URL + "/dailyNumber";
                try {
                    // Create a new HTTP client
                    HttpClient client = new DefaultHttpClient();

                    // Create a new HTTP request with the server URL
                    HttpGet request = new HttpGet(URL);

                    // Execute the request and get the response
                    HttpResponse response = client.execute(request);

                    // Get the response status code
                    int statusCode = response.getStatusLine().getStatusCode();

                    if (statusCode == 200) {
                        // If the request was successful, get the response body
                        HttpEntity entity = response.getEntity();
                        String responseBody = EntityUtils.toString(entity);

                        // Return the response body
                        return responseBody;
                    } else {
                        // If the request failed, log an error
                        Log.e("HTTP error", "Server returned status code: " + statusCode);
                    }
                } catch (IOException e) {
                    // If an exception was thrown, log the error
                    Log.e("HTTP error", "Error making HTTP request: " + e.getMessage());
                }

                return null;
            }

            private boolean isPlateValid(String plate, String[] dailyPattern) {
                if (plate.length() != 8) {
                    return false;
                }

                String[] playtPattern = plate.split("");

                for (int i = 0; i < playtPattern.length; i++) {
                    if (dailyPattern[i].equals("-1")) {
                        continue;
                    }

                    if (!playtPattern[i].equals(dailyPattern[i])) {
                        return false;
                    }
                }

                return true;
            }

            String[] stringToArray(String stringArray) {
                Gson gson = new Gson();
                Type type = com.google.gson.internal.$Gson$Types.arrayOf(String.class);
                return gson.fromJson(stringArray, type);
            }

            @Override
            protected void onPostExecute(String result) {
                String[] arr = stringToArray(result);
                // Do something with the response body

                if(isPlateValid(plate, arr)) {
                    Button uploadPlaytButton = findViewById(R.id.uploadPlaytButton);
                    uploadPlaytButton.setVisibility(View.VISIBLE);

                    uploadPlaytButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            carPlate = plate;
                            addPost();
                        }
                    });

                    EditText playtTitleText = findViewById(R.id.playtTitleText);
                    playtTitleText.setVisibility(View.VISIBLE);
                } else {
                    Button tryAgainButton = findViewById(R.id.tryAgainButton);
                    tryAgainButton.setVisibility(View.VISIBLE);
                    // create an onclick event

                    tryAgainButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent
                                    = new Intent(AddPost.this,
                                    MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    });
                }
                Log.d("HTTP response", result);
                Log.d("Plate number", plate);
            }
        }.execute();
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
//            String result = OpenALPR.Factory.create(AddPost.this, ANDROID_DATA_DIR)
//                    .recognizeWithCountryRegionNConfig("us", "", imagePath, openAlprConfFile, 10);
//
//            Results results = new Gson().fromJson(result, Results.class);
//
//            return results.getResults().get(0).getPlate();
            return "4ABC21AB";
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
                    makeHttpRequest(carPlate);
                    // System.out.println(carPlate);
                    // make an http request to get the daily number


                    // TODO: send to server and continue the flow

                } else {
                    System.out.println("no car plate found");
                    // TODO: show message that car plate not found
                }


                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}