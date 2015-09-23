package com.alinc.instagramstream;

import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.TimeZone;

public class ImageActivity extends AppCompatActivity  {

    public static final String CLIENT_ID = "fe24800ca87b4a41907aad9167f4dd65";
    private ArrayList<InstagramPhoto> instagramPhotos;
    private InstagramPhotosAdapter aPhotos;
    private SwipeRefreshLayout swipeContainer;
    private ListView lvPhotos;
    InstagramPhoto photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            actionBar.setLogo(R.mipmap.superpics_launcher);
            getSupportActionBar().setDisplayUseLogoEnabled(true);
        }

        lvPhotos = (ListView) findViewById(R.id.lv_images);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchPopularPhotos();
            }
        });

        instagramPhotos = new ArrayList<>();
        aPhotos = new InstagramPhotosAdapter(this, instagramPhotos);

        lvPhotos.setAdapter(aPhotos);
        fetchPopularPhotos();
        showCommentsDialog();

    }

    public void fetchPopularPhotos() {
        AsyncHttpClient httpClient = new AsyncHttpClient();
        String url = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;
        httpClient.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //Log.i("DEBUG", response.toString());
                aPhotos.clear();
                JSONArray photosJSON;
                try {
                    photosJSON = response.getJSONArray("data");
                    Log.i("DEBUG", String.valueOf(photosJSON.length()));
                    for (int i = 0; i < photosJSON.length(); i++) {
                        JSONObject photoJSON = photosJSON.getJSONObject(i);
                        photo = new InstagramPhoto();
                        photo.id = photoJSON.getString("id");
                        photo.userImageURL = photoJSON.getJSONObject("user").getString("profile_picture");
                        photo.username = photoJSON.getJSONObject("user").getString("username");
                        photo.timeStamp = shortLapseTime(timeAgoFromDate(photoJSON.getLong("created_time")));
                        photo.caption = (photoJSON.optJSONObject("caption") != null) ? photoJSON.getJSONObject("caption").getString("text") : "no caption";
                        photo.imageURL = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                        photo.imageHeight = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
                        photo.imageWidth = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("width");
                        photo.likesCount = (photoJSON.optJSONObject("likes") != null) ? photoJSON.getJSONObject("likes").getInt("count") : 0;
                        photo.commentsCount = photoJSON.getJSONObject("comments").getInt("count");
                        if (photo.commentsCount > 0) {
                            photo.allComments = new ArrayList<>();
                            JSONArray commentsData = photoJSON.getJSONObject("comments").getJSONArray("data");
                            for (int j = 0; j < commentsData.length(); j++) {
                                JSONObject commentsObject = commentsData.getJSONObject(j);
                                photo.allComments.add("<font color=\"#125688\"><b>" + commentsObject.getJSONObject("from").getString("username") + "</b></font>: " + commentsObject.getString("text"));
                            }
                        }
                        instagramPhotos.add(photo);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                aPhotos.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject jsonObject) {
                super.onFailure(statusCode, headers, throwable, jsonObject);
                if (throwable.getMessage().contains("UnknownHostException")) {
                    showErrorDialog("Network Exception", "Unable to contact server host. Server may be rained out or your network connection is faulty!");
                }


            }
        });
        swipeContainer.setRefreshing(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_image, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public String shortLapseTime(String lapseTime) {
        String shortTime = lapseTime.replaceAll("ago", "");
        //shortTime = shortTime.replaceAll("in", "");
        shortTime = shortTime.replaceAll("years", "y");
        shortTime = shortTime.replaceAll("days", "d");
        shortTime = shortTime.replaceAll("hour", "h");
        shortTime = shortTime.replaceAll("minutes", "m");
        shortTime = shortTime.replaceAll("hs", "h");
        shortTime = shortTime.replaceAll("mutes", "m");
        shortTime = shortTime.replaceAll("seconds", "s");
        return shortTime.replaceAll("\\s", "");
    }

    public static String timeAgoFromDate(Long dateStamp) {
        String relativeTimeSpan = DateUtils.getRelativeTimeSpanString(dateStamp * 1000 + TimeZone.getDefault().getRawOffset(), System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        return relativeTimeSpan;
    }

    private void showErrorDialog(String errorTitle, String errorMessage) {
        FragmentManager fm = getSupportFragmentManager();
        ErrorDialog errorDialog = ErrorDialog.newInstance(errorTitle, errorMessage);
        errorDialog.show(fm, "error_fragment");

    }

   private void showCommentsDialog() {
        lvPhotos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentManager fm = getSupportFragmentManager();
                CommentsDialog commentsDialog = CommentsDialog.newInstance(photo.id, photo.allComments);
                commentsDialog.show(fm, "comments_fragment");
            }
        });

    }
}
