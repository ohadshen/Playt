package com.example.playt.adapters;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.playt.Constants;
import com.example.playt.R;
import com.example.playt.models.PostModel;
import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {
    private ArrayList<PostModel> posts;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        PostModel post;
        EditText editTextTitle;
        ImageView imageViewImage;
        TextView textViewCarNumber;
        TextView textViewPoints;
        TextView textViewDate;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.editTextTitle = (EditText) itemView.findViewById(R.id.editTextTitle);
            this.imageViewImage = (ImageView) itemView.findViewById(R.id.imageView);
            this.textViewCarNumber = (TextView) itemView.findViewById(R.id.textViewCarNumber);
            this.textViewPoints = (TextView) itemView.findViewById(R.id.textViewPoints);
            this.textViewDate = (TextView) itemView.findViewById(R.id.textViewDate);

            editTextTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {
                    if (!hasFocus) {
                        EditText editText = (EditText) view;
                        updateTitle(String.valueOf(editText.getText()));
                    }
                }
            });
        }

        private void updateTitle(String newTitle) {
            new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... params) {
                    final String URL = Constants.SERVER_URL + "/posts";
                    try {
                        // Create a new HTTP client
                        HttpClient client = new DefaultHttpClient();

                        // Create a new HTTP request with the server URL
                        HttpPut request = new HttpPut(URL);
                        request.setHeader("Accept", "application/json");
                        request.setHeader("Content-type", "application/json");
                        Gson gson = new Gson();
                        post.setTitle(newTitle);
                        StringEntity entity = new StringEntity(gson.toJson(post));
                        System.out.println(gson.toJson(post));
                        request.setEntity(entity);

                        // Execute the request and get the response
                        HttpResponse response = client.execute(request);

                        // Get the response status code
                        int statusCode = response.getStatusLine().getStatusCode();

                        if (statusCode == 200) {
                            // If the request was successful, get the response body
                            HttpEntity entityResponse = response.getEntity();
                            String responseBody = EntityUtils.toString(entityResponse);
                            System.out.println(responseBody);

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
            }.execute();
        }
    }

    public PostAdapter(ArrayList<PostModel> posts) {
        this.posts = posts;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_profile_card, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int index) {
        EditText editTextTitle = holder.editTextTitle;
        ImageView imageViewImage = holder.imageViewImage;
        TextView textViewCarNumber = holder.textViewCarNumber;
        TextView textViewPoints = holder.textViewPoints;
        TextView textViewDate = holder.textViewDate;

        byte[] bufferImage = Base64.decode(posts.get(index).getImage().data, Base64.DEFAULT);

        holder.post = posts.get(index);
        editTextTitle.setText(posts.get(index).getTitle());
        imageViewImage.setImageBitmap(BitmapFactory.decodeByteArray(bufferImage, 0, bufferImage.length));
        imageViewImage.setBackgroundColor(Color.rgb(100, 100, 100));
        textViewCarNumber.setText(posts.get(index).getCarPlate());
        textViewPoints.setText(String.valueOf(posts.get(index).getPoints()) + " Points");
        textViewDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(posts.get(index).getDate()));
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }
}
