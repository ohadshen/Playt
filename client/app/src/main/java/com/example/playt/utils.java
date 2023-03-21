package com.example.playt;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.example.playt.models.ImageBuffer;
import com.google.gson.Gson;

import java.lang.reflect.Type;

public class utils {
    public static String[] stringToStringArray(String stringArray) {
        Gson gson = new Gson();
        Type type = com.google.gson.internal.$Gson$Types.arrayOf(String.class);
        return gson.fromJson(stringArray, type);
    }

    public static Bitmap ImageBufferToBitmap (ImageBuffer imageBuffer) {
        byte[] bufferImage = Base64.decode(imageBuffer.data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bufferImage, 0, bufferImage.length);
    }
}
