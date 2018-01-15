package org.miage.placesearcher;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by baptisterouger on 14/01/2018.
 */

public class PlaceDetailActivity extends AppCompatActivity {
    String street;
    @BindView(R.id.detail_textView)
    TextView mTextView;
    @OnClick(R.id.detail_textView)
    public void textViewReturn() {
        PlaceDetailActivity.this.finish();
    }
    @OnClick(R.id.searchButton)
    public void onClickSearch(){
        Uri url = Uri.parse("https://www.google.fr/search?q="+street);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, url);
        startActivity(launchBrowser);
    }
    @OnClick(R.id.shareButton)
    public void onClickShare(){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, street);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_detail);
        ButterKnife.bind(this);
        street = getIntent().getStringExtra("street");
        mTextView.setText(street);

    }
    @Override
    protected void onResume() {
        // Do NOT forget to call super.onResume()
        super.onResume();
    }
}
