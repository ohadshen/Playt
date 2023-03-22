package com.example.playt;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.playt.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class LoginFragment extends Fragment {

    private EditText emailTextView, passwordTextView;
    private Button LoginButton;
    private Button RouteToSignUp;
    private ProgressBar progressbar;

    private FirebaseAuth mAuth;
    private SharedPreferences sharedPreferences;


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
                System.out.println(utils.stringToBitmap(utils.bitmapToString(utils.ImageBufferToBitmap(user.getImage()))));
                editor.putString("profilePicture", utils.bitmapToString(utils.ImageBufferToBitmap(user.getImage())));
                editor.apply();


            }
        }.execute();
    }

    private void loginUserAccount(View view)
    {

        // show the visibility of progress bar to show loading
        progressbar.setVisibility(View.VISIBLE);

        // Take the value of two edit texts in Strings
        String email, password;
        email = emailTextView.getText().toString();
        password = passwordTextView.getText().toString();
        sharedPreferences = requireContext().getSharedPreferences("user_preferences", MODE_PRIVATE);

        // validations for input email and password
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getContext(),
                            "Please enter email!!",
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getContext(),
                            "Please enter password!!",
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }

        // signin existing user
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(
                                    @NonNull Task<AuthResult> task)
                            {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getContext(),
                                                    "Login successful!!",
                                                    Toast.LENGTH_LONG)
                                            .show();

                                    // hide the progress bar
                                    progressbar.setVisibility(View.GONE);

                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("username", email);
                                    editor.apply();

                                    getUser();

                                    try {

                                        Navigation.findNavController(view)
                                                .navigate(R.id.action_loginFragment2_to_homePageFragment2);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                else {

                                    // sign-in failed
                                    Toast.makeText(getContext(),
                                                    "Login failed!!",
                                                    Toast.LENGTH_LONG)
                                            .show();

                                    // hide the progress bar
                                    progressbar.setVisibility(View.GONE);
                                }
                            }
                        });

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_login, container, false);
        sharedPreferences = requireContext().getSharedPreferences("user_preferences", MODE_PRIVATE);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("username", user.getEmail());
            editor.apply();

            getUser();

            try {
                Navigation.findNavController(requireActivity(), R.id.main_navhost)
                        .navigate(R.id.action_loginFragment2_to_homePageFragment2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // taking instance of FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // initialising all views through id defined above
        emailTextView = view.findViewById(R.id.email);
        passwordTextView = view.findViewById(R.id.password);
        LoginButton = view.findViewById(R.id.login);
        progressbar = view.findViewById(R.id.progressBar);

        // Set on Click Listener on Sign-in button
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                loginUserAccount(v);
            }
        });

        RouteToSignUp = view.findViewById(R.id.route_to_registration);
        // Set on Click Listener on Sign-in button
        RouteToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                try {
                    Navigation.findNavController(requireActivity(), R.id.main_navhost)
                            .navigate(R.id.action_loginFragment2_to_registrationFragment);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });




        return view;
    }
}