package com.example.playt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.lang.reflect.Type;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CAMERA_PERMISSION = 101;

    private Button LogOutButton;
    private TextView carDailyNumber;
    private String currentPhotoPath;
    private String[] dailyPattern;

    public void getDailyPattern() {
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

            String[] stringToArray(String stringArray) {
                Gson gson = new Gson();
                Type type = com.google.gson.internal.$Gson$Types.arrayOf(String.class);
                return gson.fromJson(stringArray, type);
            }


            private String dailyPatternForDisplay(String[] dailyPattern) {
                String result = "";
                for (int i = 0; i < dailyPattern.length; i++) {
                    if (dailyPattern[i].equals("-1")) {
                        result += " _ ";
                    } else {
                        result += dailyPattern[i];
                    }
                }

                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                // Do something with the response body
                dailyPattern = stringToArray(result);
                carDailyNumber.setText(dailyPatternForDisplay(dailyPattern));

                Log.d("HTTP response", result);
            }
        }.execute();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getDailyPattern();
        LogOutButton = findViewById(R.id.logoutBtn);
        carDailyNumber = findViewById(R.id.carDailyNumber);


        // Set on Click Listener on Sign-in button
        LogOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();

                Intent intent
                        = new Intent(MainActivity.this,
                        ActivityLogin.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    public void launchCamera(View view) {
        Intent intent
                = new Intent(MainActivity.this,
                AddPost.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
