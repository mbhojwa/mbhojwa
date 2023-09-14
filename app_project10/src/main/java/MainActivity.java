package com.example.project10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, BottomSheetClass.BottomSheetListener
{

    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    FrameLayout design_frame;
    BottomSheetClass lowersheet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        design_frame = findViewById(R.id.design_frame);
        lowersheet = new BottomSheetClass();

        design_frame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lowersheet.show(getSupportFragmentManager(), "lowersheet");
            }
        });

        // drawer layout instance to toggle the menu icon to open
        // drawer and back button to close drawer
        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // to make the Navigation drawer icon always appear on the action bar
        NavigationListener();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    // override the onOptionsItemSelected()
    // function to implement
    // the item click listener callback
    // to open and close the navigation
    // drawer when the icon is clicked
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        FragmentManager manager = getSupportFragmentManager();
        switch (item.getItemId()) {

            case R.id.nav_logout: {
                song_Fragment songs = new song_Fragment();
                manager.beginTransaction().replace(R.id.con_layout, songs, songs.getTag()).commit();
                break;
            }
            case R.id.nav_settings: {
                video_Fragment videos = new video_Fragment();
                manager.beginTransaction().replace(R.id.con_layout, videos, videos.getTag()).commit();
                break;
            }
            case R.id.nav_account: {
                playlist_Fragment playlist = new playlist_Fragment();
                manager.beginTransaction().replace(R.id.con_layout, playlist, playlist.getTag()).commit();
                break;
            }

        }
        //close navigation drawer
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;





    }
    private void NavigationListener() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigationview);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void play() {

        if (service.player != null) {
            service.player.start();

            BottomSheetClass.progressBar.setProgress(0);
            BottomSheetClass.progressBar.setMax(service.player.getDuration());
            BottomSheetClass.progressBar.postDelayed(changeProgressBar, 15);
        }
         /*

        else {
            Intent intent = new Intent(MainActivity.this, service.class);
            for (int i = 0; i < MyPlaylist.playList.size(); i++) {
                intent.putExtra("song" + i, MyPlaylist.playList.get(i).getSource());
            }
            intent.putExtra("songPos", 0);
            intent.putExtra("size", MyPlaylist.playList.size());
            startService(intent);
        }

         */

    }

    @Override
    public void pause() {
        service.player.pause();

    }

    @Override
    public void shuffle(Boolean checked) {
         service.shuffle = checked;

    }
    private Runnable changeProgressBar = new Runnable() {
       public void run() {
            long currentDuration = service.player.getCurrentPosition();
            BottomSheetClass.progressBar.setProgress((int)currentDuration);
            BottomSheetClass.progressBar.postDelayed(this, 15);
       }
    };
}