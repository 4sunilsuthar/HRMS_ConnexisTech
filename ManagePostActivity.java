package com.lms.admin.lms;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class ManagePostActivity extends AppCompatActivity {
    private static final String TAG = "ManagePostActivity";
    FragmentManager fragment_manager;
    FragmentTransaction fragmentTrans;
    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_post);
        //calling the method for the default fragment loading
        Log.e(TAG, "in OnCreate() of ManagePostActivity");
        fragment_manager = this.getSupportFragmentManager();
        fragmentTrans = fragment_manager.beginTransaction();
        fragment = new ContentUserHomeFragment();
        fragmentTrans.add(R.id.fragment_container2, fragment).commit();
    }

    //for showing options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_posts, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.addIcon) {
            startActivity(new Intent(this, StoriesUploadActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

}
