package com.example.playt;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class HomePageFragment extends Fragment {
    private static final int REQUEST_CAMERA_PERMISSION = 101;

    private Button cameraButton;
    private Button logOutButton;
    private Button searchPageButton;
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
                dailyPattern = utils.stringToStringArray(result);
                carDailyNumber.setText(dailyPatternForDisplay(dailyPattern));

                Log.d("HTTP response", result);
            }
        }.execute();
    }

    public void launchCamera(View view) {
        try {
            Navigation.findNavController(requireActivity(), R.id.main_navhost)
                    .navigate(R.id.action_homePageFragment2_to_addPostFragment);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);

        getDailyPattern();
        logOutButton = view.findViewById(R.id.logoutBtn);
        carDailyNumber = view.findViewById(R.id.carDailyNumber);
        cameraButton = view.findViewById(R.id.camera_button);
        searchPageButton = view.findViewById(R.id.searchBtn);

        // Set on Click Listener on Sign-in button
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCamera(v);
            }
        });


        // Set on Click Listener on Sign-in button
        searchPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();

                try {
                    Navigation.findNavController(requireActivity(), R.id.main_navhost)
                            .navigate(R.id.action_homePageFragment2_to_itemFragment2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        // Set on Click Listener on Sign-in button
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();

                try {
                    Navigation.findNavController(requireActivity(), R.id.main_navhost)
                            .navigate(R.id.action_homePageFragment2_to_loginFragment2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }
}