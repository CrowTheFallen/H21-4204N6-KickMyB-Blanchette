package com.example.kickmyb;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.kickmyb.databinding.ActivityConsultationBinding;
import com.example.kickmyb.databinding.ActivityCreationBinding;

public class CreationActivity extends AppCompatActivity {
    private ActivityCreationBinding binding;
    private ActionBarDrawerToggle toggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreationBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreationActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawerLayout = binding.drawerLayout;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.dOpen, R.string.dClose){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(R.string.dOpen);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getSupportActionBar().setTitle(R.string.dClose);
            }
        };

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        binding.navView.getMenu().findItem(R.id.nav_item_one).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent i2 = new Intent(CreationActivity.this, HomeActivity.class);
                drawerLayout.closeDrawers();
                startActivity(i2);
                return true;
            }
        });

        binding.navView.getMenu().findItem(R.id.nav_item_two).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent i2 = new Intent(CreationActivity.this, CreationActivity.class);
                drawerLayout.closeDrawers();
                startActivity(i2);
                return true;
            }
        });

        binding.navView.getMenu().findItem(R.id.nav_item_three).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent i2 = new Intent(CreationActivity.this, ConnectionActivity.class);
                drawerLayout.closeDrawers();
                startActivity(i2);
                return true;
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(toggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        toggle.onConfigurationChanged(newConfig);
        super.onConfigurationChanged(newConfig);
    }
}
