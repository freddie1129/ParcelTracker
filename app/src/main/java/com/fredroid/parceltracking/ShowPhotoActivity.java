package com.fredroid.parceltracking;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.io.File;

public class ShowPhotoActivity extends AppCompatActivity {

    public static final String PARAM_SHOW_PHOTO="show-photo";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_photo);




        if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
                getSupportActionBar().setTitle(getString(R.string.activity_show_photo_show));
        }

        if (null != getIntent())
        {
            String photoPath = getIntent().getStringExtra(PARAM_SHOW_PHOTO);
            File file = new File(photoPath);
            if (file.isFile())
            {
                Bitmap image = BitmapFactory.decodeFile(photoPath);
                //   if (image != null)
                //      holder.mView.setImageBitmap(image)
                ((ImageView)findViewById(R.id.imageView_show_photo)).setImageBitmap(image);
            }
        }


    }
    @Override
    public boolean onSupportNavigateUp() {

            finish();
            return true;




    }
}
