package com.lms.admin.lms;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;

import com.ablanco.zoomy.Zoomy;

public class ShowPostDetailsActivity extends AppCompatActivity {
    ImageView mImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_post_details);
        mImage = findViewById(R.id.display_post_image);

        //for the action bar back button (Universal back button)
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        Zoomy.Builder builder = new Zoomy.Builder(this).target(mImage);
        builder.register();
        /*
        Zoomy.Builder builder = new Zoomy.Builder(MainActivity.this)
                .target(holder.itemView)
                .interpolator(new OvershootInterpolator())
                .tapListener(new TapListener() {
                    @Override
                    public void onTap(View v) {
                        Toast.makeText(MainActivity.this, "Tap on "
                                + v.getTag(), Toast.LENGTH_SHORT).show();
                    }
                });
        builder.register();*/

    }

    //for back button on the title bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
