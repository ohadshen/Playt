package com.example.playt;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.content.Intent;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ActivityRegistration extends AppCompatActivity {

    private EditText emailTextView, passwordTextView, nicknameTextView;
    private ImageView userImageView;
    private Button addAvatarButton;
    private Button signUpButton;
    private Button goToSignInButton;
    public ProgressBar progressbar;
    private FirebaseAuth mAuth;

    private static final int PICK_IMAGE = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // taking FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();

        // initialising all views through id defined above
        emailTextView = findViewById(R.id.email);
        passwordTextView = findViewById(R.id.passwd);
        nicknameTextView = findViewById(R.id.nickname);
        signUpButton = findViewById(R.id.btnregister);
        progressbar = findViewById(R.id.progressbar);
        addAvatarButton = findViewById(R.id.addAvatarButton);
        userImageView = (ImageView) findViewById(R.id.userImageView);

        // Set on Click Listener on Registration button
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                registerNewUser();
            }
        });


        goToSignInButton = findViewById(R.id.route_to_sign_up);

        // Set on Click Listener on Sign-in button
        goToSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent
                        = new Intent(ActivityRegistration.this,
                        ActivityLogin.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });
    }

    public void chooseImage(View view) {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE);


    }

    private void registerNewUser()
    {

        // show the visibility of progress bar to show loading
        progressbar.setVisibility(View.VISIBLE);

        // Take the value of two edit texts in Strings
        String email, password, nickname;
        email = emailTextView.getText().toString();
        password = passwordTextView.getText().toString();
        nickname = nicknameTextView.getText().toString();

        // Validations for input email and password
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(),
                            "Please enter email!!",
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(),
                            "Please enter password!!",
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if (TextUtils.isEmpty(nickname)) {
            Toast.makeText(getApplicationContext(),
                            "Please enter nickname!!",
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if (userImageView.getDrawable() == null) {
            Toast.makeText(getApplicationContext(),
                            "Please choose an avatar!!",
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }

        addUser();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                String imagePath = data.getData().getLastPathSegment();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
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

    public void addUser() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                final String SERVER_URL = Constants.SERVER_URL + "/users";
                try {
                    // Create a new HTTP client
                    HttpClient client = new DefaultHttpClient();

                    // Create a new HTTP request with the server URL
                    HttpPost request = new HttpPost(SERVER_URL);

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

                    // Add the text to the data
                    EditText emailText = findViewById(R.id.email);
                    String email = emailText.getText().toString();
                    data.put("username", email);

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

                        // create new user or register new user
                        mAuth.createUserWithEmailAndPassword(email, passwordTextView.getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task)
                                    {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getApplicationContext(),
                                                            "Registration successful!",
                                                            Toast.LENGTH_LONG)
                                                    .show();

                                            // hide the progress bar
                                            progressbar.setVisibility(View.GONE);

                                            SharedPreferences sharedPreferences = getSharedPreferences("user_preferences", MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putString("username", email);
                                            editor.apply();

                                            // if the user created intent to login activity
                                            Intent intent
                                                    = new Intent(ActivityRegistration.this,
                                                    MainActivity.class);
                                            startActivity(intent);
                                        }
                                        else {

                                            // Registration failed
                                            Toast.makeText(
                                                            getApplicationContext(),
                                                            "Registration failed!!"
                                                                    + " Please try again later",
                                                            Toast.LENGTH_LONG)
                                                    .show();

                                            // hide the progress bar
                                            progressbar.setVisibility(View.GONE);
                                        }
                                    }
                                });
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

}
