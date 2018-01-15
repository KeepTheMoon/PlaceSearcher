package org.miage.placesearcher;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by baptisterouger on 14/01/2018.
 */

public class PlaceDetailActivity extends AppCompatActivity {
    String street;
    private final int SELECT_PHOTO = 1;
    @BindView(R.id.detail_textView)
    TextView mTextView;
    @BindView(R.id.imageView)
    ImageView mImageView;
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
    @OnClick(R.id.imgButton)
    public void onClickImg(){
        Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }
    //@OnClick(R.id.shareImgButton)
    //public void onClickShareImg() throws IOException {
      //  Intent shareIntent = new Intent();
       // shareIntent.setAction(Intent.ACTION_SEND);
        //shareIntent.setType("image/*");
        ///AssetManager assetManager = new AssetManager();
        //final File photoFile = new File(getAssets().openFd("oss.mp3"), "emeu.jpg");
       // shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(photoFile));
       // startActivity(shareIntent);
    //}
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent){
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode){
            case SELECT_PHOTO:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                        mImageView.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        }
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
