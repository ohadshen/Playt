package com.example.playt.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.playt.R;
import com.example.playt.models.PostModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {
    private ArrayList<PostModel> posts;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        ImageView imageViewImage;
        TextView textViewCarNumber;
        TextView textViewPoints;
        TextView textViewDate;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.textViewTitle = (TextView) itemView.findViewById(R.id.textViewTitle);
            this.imageViewImage = (ImageView) itemView.findViewById(R.id.imageView);
            this.textViewCarNumber = (TextView) itemView.findViewById(R.id.textViewCarNumber);
            this.textViewPoints = (TextView) itemView.findViewById(R.id.textViewPoints);
            this.textViewDate = (TextView) itemView.findViewById(R.id.textViewDate);
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
        TextView textViewTitle = holder.textViewTitle;
        ImageView imageViewImage = holder.imageViewImage;
        TextView textViewCarNumber = holder.textViewCarNumber;
        TextView textViewPoints = holder.textViewPoints;
        TextView textViewDate = holder.textViewDate;

        byte[] bufferImage = Base64.decode(posts.get(index).getImage().data, Base64.DEFAULT);

        textViewTitle.setText(posts.get(index).getTitle());
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
