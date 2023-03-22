package SearchPageFragment.placeholder;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.example.playt.Constants;
import com.example.playt.models.UserModel;
import com.example.playt.utils;
import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import SearchPageFragment.MyItemRecyclerViewAdapter;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class PlaceholderContent {

    /**
     * An array of sample (placeholder) items.
     */
    public static final List<PlaceholderItem> ITEMS = new ArrayList<PlaceholderItem>();
    public static final List<PlaceholderItem> FILTERED_ITEMS = new ArrayList<PlaceholderItem>();
    public static RecyclerView recyclerView = null;
    public static Activity activity;

    public static void getUsersForSearch() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                final String URL = Constants.SERVER_URL + "/users";
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
                UserModel[] users = new Gson().fromJson(result, UserModel[].class);
                // Do something with the response body
                int COUNT = users.length;

                for (int i = 0; i < COUNT; i++) {
                    addItem(createPlaceholderItem(users[i]));
                }

                PlaceholderContent.recyclerView.setAdapter(new MyItemRecyclerViewAdapter(PlaceholderContent.ITEMS,activity ));

                Log.d("HTTP response", users.toString());

            }
        }.execute();
    }


    /**
     * A map of sample (placeholder) items, by ID.
     */
    public static final Map<String, PlaceholderItem> ITEM_MAP = new HashMap<String, PlaceholderItem>();


    static {
        getUsersForSearch();
        // Add some sample items.
    }

    private static void addItem(PlaceholderItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static PlaceholderItem createPlaceholderItem(UserModel user) {
        return new PlaceholderItem(String.valueOf(user.get_id()),  user.getNickname(), utils.ImageBufferToBitmap(user.getImage()), user.getUsername());
    }

    public static void setItems(RecyclerView recyclerView) {
        PlaceholderContent.recyclerView = recyclerView;
        getUsersForSearch();
    }

    /**
     * A placeholder item representing a piece of content.
     */
    public static class PlaceholderItem {
        public final String id;
        public final String nickname;
        public final Bitmap image;

        public final String username;


        public PlaceholderItem(String id, String nickname, Bitmap image, String username) {
            this.id = id;
            this.nickname = nickname;
            this.image = image;
            this.username = username;
        }

        @Override
        public String toString() {
            return nickname;
        }
    }
}