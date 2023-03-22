package com.example.playt;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.caverock.androidsvg.SVGParseException;
import com.example.playt.models.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class EditProfileFragment extends Fragment {

    private View viewReference;
    private EditText nicknameTextView;
    public ImageView userImageView;
    private Button addAvatarButton;
    private Button updateUserButton;
    private Button goToSignInButton;
    public ProgressBar progressbar;
    private FirebaseAuth mAuth;
    public Bitmap userImageBitmap;

    private SharedPreferences sharedPreferences;

    private static final int PICK_IMAGE = 1;

    public void getUser() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                final String URL = Constants.SERVER_URL + "/users/" + sharedPreferences.getString("username", null);
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

            @Override
            protected void onPostExecute(String result) {
                UserModel user = new Gson().fromJson(result, UserModel.class);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("nickname", user.getNickname());
                editor.putString("profilePicture", utils.bitmapToString(utils.ImageBufferToBitmap(user.getImage())));
                editor.apply();


            }
        }.execute();
    }


    private void updateUser(String username)
    {

        // show the visibility of progress bar to show loading
        progressbar.setVisibility(View.VISIBLE);

        // Take the value of two edit texts in Strings
        String email, nickname;
        nickname = nicknameTextView.getText().toString();

        if (TextUtils.isEmpty(nickname)) {
            Toast.makeText(getContext(),
                            "Please enter nickname!!",
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if (userImageView.getDrawable() == null) {
            Toast.makeText(getContext(),
                            "Please choose an avatar!!",
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }

        updateUserRequest(username);
    }



    public void updateUserRequest(String username) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                final String SERVER_URL = Constants.SERVER_URL + "/users";
                try {
                    // Create a new HTTP client
                    HttpClient client = new DefaultHttpClient();

                    // Create a new HTTP request with the server URL
                    HttpPut request = new HttpPut(SERVER_URL);

                    // Set the request headers
                    request.setHeader("Content-Type", "application/json");
                    request.setHeader("Accept", "application/json");

                    // Create a JSONObject to store the data
                    JSONObject data = new JSONObject();

                    // Add the image to the data as base64-encoded string
                    userImageView.setDrawingCacheEnabled(true);
                    userImageView.buildDrawingCache();

                    Bitmap bitmap = userImageView.getDrawingCache();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    String base64Image = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    data.put("image", base64Image);


                    data.put("username", username);


                    // Add the text to the data
                    String nickname = nicknameTextView.getText().toString();
                    data.put("nickname", nickname);

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

            }
            @Override
            protected void onPostExecute(String result) {

                    getUser();

                    Toast.makeText(getContext(),
                                    "Update Succeed!!",
                                    Toast.LENGTH_LONG)
                            .show();
                    Log.d("HTTP response", result);

            }}.execute();


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                String imagePath = data.getData().getLastPathSegment();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), uri);
                addAvatarButton.setVisibility(View.GONE);
                userImageView.setVisibility(View.VISIBLE);
                userImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void setNewAvatarImage(EditProfileFragment context) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {



                final String URL = Constants.AVATARS_API_URL + Math.random();

                try {
                    DefaultHttpClient httpClient = new DefaultHttpClient();
                    SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
                    socketFactory.setHostnameVerifier(SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);
                    Scheme scheme = new Scheme("https", socketFactory, 443);
                    httpClient.getConnectionManager().getSchemeRegistry().register(scheme);

                    // Create a new HTTP request with the server URL
                    HttpGet request = new HttpGet(URL);

                    // Execute the request and get the response
                    HttpResponse response = httpClient.execute(request);

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

            @Override
            protected void onPostExecute(String result) {
                try {
                    Bitmap bitmap = utils.SvgToBitmap(result);

                    context.userImageBitmap = bitmap;
                    context.userImageView.setImageBitmap(bitmap);
                    Log.d("HTTP response", result);

                } catch (SVGParseException e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_edit_profile, container, false);
        viewReference = view;
        // taking FirebaseAuth instance

        // initialising all views through id defined above
        nicknameTextView = view.findViewById(R.id.nickname);
        updateUserButton = view.findViewById(R.id.btnupdateuser);
        progressbar = view.findViewById(R.id.progressbar);
        userImageView = (ImageView) view.findViewById(R.id.userImageView);

        // get values from shared preferences
        sharedPreferences = getActivity().getSharedPreferences("user_preferences", Context.MODE_PRIVATE);
        nicknameTextView.setText(sharedPreferences.getString("nickname", ""));
        String profilePicture = sharedPreferences.getString("profilePicture", "");
        Bitmap newImage = utils.stringToBitmap(profilePicture);
        userImageView.setImageBitmap(newImage);


        // Set on Click Listener on Registration button
        userImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                setNewAvatarImage(EditProfileFragment.this);
            }
        });
        updateUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                updateUser(sharedPreferences.getString("username", ""));
            }
        });

        return view;
    }
}