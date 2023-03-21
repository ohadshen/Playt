package com.example.playt;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.playt.adapters.PostAdapter;
import com.example.playt.models.PostModel;
import com.example.playt.models.UserModel;
import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class ActivityProfilePage extends AppCompatActivity {
    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<PostModel> posts;
    private static TextView textViewNickname;
    private static TextView textViewProfilePoints;
    private static ImageView imageViewProfile;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        SharedPreferences sharedPreferences = getSharedPreferences("user_preferences", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");

        recyclerView = (RecyclerView) findViewById(R.id.recycler_posts);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        textViewNickname = (TextView) findViewById(R.id.textViewNickname);
        textViewProfilePoints = (TextView) findViewById(R.id.textViewProfilePoints);
        imageViewProfile = (ImageView) findViewById(R.id.imageViewProfile);
        posts = new ArrayList<PostModel>();
        getUser();
    }

    private boolean isCurrentUser() {
        return false;
    }

    public void getUser() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                final String URL = Constants.SERVER_URL + "/users/" + username;
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
                Collections.addAll(posts, user.getPosts());

                adapter = new PostAdapter(posts, isCurrentUser());
                recyclerView.setAdapter(adapter);

                byte[] bufferImage = Base64.decode(user.getImage().data, Base64.DEFAULT);
                System.out.println(BitmapFactory.decodeByteArray(bufferImage, 0, bufferImage.length));

                textViewNickname.setText(user.getNickname() + " Profile");
                textViewProfilePoints.setText(getProfilePoints() + " Points");
                imageViewProfile.setImageBitmap(BitmapFactory.decodeByteArray(bufferImage, 0, bufferImage.length));
            }
        }.execute();
    }

    private int getProfilePoints() {
        int sum = 0;
        for (PostModel post : posts) {
            sum += post.getPoints();
        }

        return sum;
    }
}
