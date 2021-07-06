package com.example.makenotesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.makenotesapp.data.NoteData;
import com.example.makenotesapp.observer.Publisher;
import com.example.makenotesapp.ui.ListFragment;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    private Navigation mNavigation;
    private Publisher mPublisher = new Publisher();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getNavigation().addFragmentToFirstFrame(ListFragment.getInstance(getSupportFragmentManager()
                    , savedInstanceState), false, ListFragment.TAG, false);
        }

        Button alertDialog = findViewById(R.id.clear_all);
        alertDialog.setOnClickListener(clickAlertBeforeClear);
        initView();
    }

    private View.OnClickListener clickAlertBeforeClear = view -> {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.dialog_title)
                .setMessage(R.string.alert_before_delete)
                .setIcon(R.mipmap.ic_launcher_round)
                .setCancelable(false)
                .setPositiveButton(R.string.yes,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                Toast.makeText(MainActivity.this, "Кнопка нажата", Toast.LENGTH_SHORT).show();
                            }
                        })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(MainActivity.this, "Кнопка нажата", Toast.LENGTH_SHORT).show();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    };

    private void initView() {
        initDrawer(initToolbar());
    }

    private void initDrawer(Toolbar toolbar) {
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (navigateFragment(id)) {
                    drawer.closeDrawer(GravityCompat.START);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {

        if (mNavigation.getBackStackEntryCount() > 0) {
            mNavigation.popBackStack();
        } else {
            //super.onBackPressed();
        }
    }

    private boolean navigateFragment(int id) {
        switch (id) {
            case R.id.action_settings:
                Toast.makeText(getApplicationContext(),
                        "Settings will open here", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_main:
                Toast.makeText(getApplicationContext(),
                        "Main fragment will open here", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_favorite:
                Toast.makeText(getApplicationContext(),
                        "Favourites fragment will be here", Toast.LENGTH_SHORT).show();
                return true;
        }
        return false;
    }


    private Toolbar initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        return toolbar;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (navigateFragment(id)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem search = menu.findItem(R.id.action_search);
        SearchView searchText = (SearchView) search.getActionView();
        searchText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(MainActivity.this, query, Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public Navigation getNavigation() {
        if (mNavigation == null)
            mNavigation = new Navigation(getSupportFragmentManager());
        return mNavigation;
    }

    public Publisher getPublisher() {
        return mPublisher;
    }

    @Override
    public void onPause() {
        System.out.println("Saving webview state");

        super.onPause();

    }

}