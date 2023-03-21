package com.example.playt;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginFragment extends Fragment {

    private EditText emailTextView, passwordTextView;
    private Button LoginButton;
    private Button RouteToSignUp;
    private ProgressBar progressbar;

    private FirebaseAuth mAuth;
    private SharedPreferences sharedPreferences;
    private void loginUserAccount(View view)
    {

        // show the visibility of progress bar to show loading
        progressbar.setVisibility(View.VISIBLE);

        // Take the value of two edit texts in Strings
        String email, password;
        email = emailTextView.getText().toString();
        password = passwordTextView.getText().toString();
        sharedPreferences = requireActivity().getSharedPreferences("my_prefs", MODE_PRIVATE);

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

                                    // if sign-in is successful
                                    // intent to home activity
                                    //todo navigate to main activity
//                                    Intent intent
//                                            = new Intent(ActivityLogin.this,
//                                            MainActivity.class);
//                                    startActivity(intent);
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


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            sharedPreferences = requireActivity().getSharedPreferences("my_prefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("username", user.getEmail());
            editor.apply();

            try {
                Navigation.findNavController(requireActivity(), R.id.main_navhost)
                        .navigate(R.id.action_loginFragment2_to_homePageFragment2);
            } catch (Exception e) {
                e.printStackTrace();
            }
//                    .navigate(R.id.action_loginFragment2_to_homePageFragment2);
            // todo: navigate to main activity
            // User is signed in
//            Intent i = new Intent(ActivityLogin.this, MainActivity.class);
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(i);
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