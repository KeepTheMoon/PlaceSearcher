package org.miage.placesearcher;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.miage.placesearcher.R;
import org.miage.placesearcher.model.Place;
import org.miage.placesearcher.ui.PlaceAdapter;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.listView)
    ListView mListView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Intent seePlaceDetailsIntent = new Intent(this, PlaceDetailActivity.class);
        // Binding ButterKnife annotations now that content view has been set
        ButterKnife.bind(this);
        // Define list of places
        final List<Place> places = new ArrayList<Place>();
        for (int i = 0; i < 50; i ++) {
            places.add(new Place(0, 0, "Street" + i, "44000", "Nantes"));
        }
        // Instanciance PlaceAdapter
        PlaceAdapter placeAdapter = new PlaceAdapter(this, places);
        mListView.setAdapter(placeAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AssetFileDescriptor afd = null;
                try {
                    afd = getAssets().openFd("oss.mp3");

                    MediaPlayer player = new MediaPlayer();
                    player.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
                    player.prepare();
                    player.start();
                    seePlaceDetailsIntent.putExtra("street",  places.get(i).getStreet());
                    startActivity(seePlaceDetailsIntent);

                } catch (IOException e) {
                    // Silent catch : sound will not be played
                }
            }
        });
    }
    @Override
    protected void onResume() {
        // Do NOT forget to call super.onResume()
        super.onResume();
        EventBusManager.BUS.register(this);
        AsyncTask<String, Void, Response> asyncTask = new AsyncTask<String, Void, Response>() {
            @Override
            protected Response doInBackground(String... strings) {
                try{
                    final OkHttpClient okHttpClient = new OkHttpClient();
                    final Request request = new Request.Builder()
                            .url(strings[0])
                            .build();
                    return okHttpClient.newCall(request).execute();
                } catch (IOException e){
                    //
                }
                return null;
            }
            @Override
            protected void onPostExecute(Response response){
                super.onPostExecute(response);

                try {
                    JSONObject jsonresult = new JSONObject(response.body().string());
                    JSONArray jsonArray = jsonresult.getJSONArray("features");
                    List<Place> places = new ArrayList<Place>();
                    for(int i = 0; i < jsonArray.length(); i++ ){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        JSONObject type = jsonObject.getJSONObject("properties");
                        //Log.d("result", type);
                        Place place = new Place(0, 0, type.getString("name"), type.getString("postcode"), type.getString("city"));
                        places.add(place);
                    }
                    EventBusManager.BUS.post(new SearchResultEvent(places));

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        asyncTask.execute("https://api-adresse.data.gouv.fr/search/?q=Place%20du%20commerce");


    }
    @Override
    protected void onPause() {
        EventBusManager.BUS.unregister(this);
        super.onPause();
    }

    @Subscribe
    public void searchResult(SearchResultEvent event){
        for(Place place : event.getPlaces()){
            Log.d("result", place.getStreet());

        }
        PlaceAdapter placeAdapter = new PlaceAdapter(this, event.getPlaces());
        mListView.setAdapter(placeAdapter);
    }
}
