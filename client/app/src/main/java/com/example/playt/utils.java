package com.example.playt;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Base64;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;
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

    public static Bitmap SvgToBitmap (String svgString) throws SVGParseException {
        SVG svg = SVG.getFromString(svgString);

        Bitmap bitmap = Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawARGB(0, 0, 0, 0);
        svg.renderToCanvas(canvas);

        return bitmap;
    }
}
