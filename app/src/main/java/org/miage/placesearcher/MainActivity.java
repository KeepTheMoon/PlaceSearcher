package org.miage.placesearcher;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.miage.placesearcher.R;
import org.miage.placesearcher.model.Place;
import org.miage.placesearcher.ui.PlaceAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import okhttp3.Request;

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
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api-adresse.data.gouv.fr/search/?q=Place%20du%20commerce")
                .build();


    }
}
