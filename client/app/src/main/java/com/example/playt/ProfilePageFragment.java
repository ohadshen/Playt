package com.example.playt;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class ProfilePageFragment extends Fragment {


    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<PostModel> posts;
    private static TextView textViewNickname;
    private static TextView textViewProfilePoints;
    private static ImageView imageViewProfile;
    private String profileUsername;
    private String currentUsername;

    private boolean isCurrentUser() {
        return currentUsername == profileUsername;
    }

    public void getUser() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                final String URL = Constants.SERVER_URL + "/users/" + profileUsername;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("user_preferences", MODE_PRIVATE);

        currentUsername = sharedPreferences.getString("username", "");

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_profile_page, container, false);

        ProfilePageFragmentArgs args = ProfilePageFragmentArgs.fromBundle(getArguments());

        profileUsername = args.getProfileUsername();

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_posts);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        textViewNickname = (TextView) view.findViewById(R.id.textViewNickname);
        textViewProfilePoints = (TextView) view.findViewById(R.id.textViewProfilePoints);
        imageViewProfile = (ImageView) view.findViewById(R.id.imageViewProfile);
        posts = new ArrayList<PostModel>();
        getUser();

        return view;
    }
}