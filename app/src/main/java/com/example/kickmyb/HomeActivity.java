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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kickmyb.databinding.ActivityHomeBinding;

import java.util.Random;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;
    private ActionBarDrawerToggle toggle;
    TâcheAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setTitle("Accueil");

        binding.button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this,CreationActivity.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawerLayout = binding.drawerLayout;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.dOpen, R.string.dClose){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                //getSupportActionBar().setTitle(R.string.dClose);
            }
        };

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        binding.navView.getMenu().findItem(R.id.nav_item_one).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent i2 = new Intent(HomeActivity.this, HomeActivity.class);
                drawerLayout.closeDrawers();
                startActivity(i2);
                return true;
            }
        });

        binding.navView.getMenu().findItem(R.id.nav_item_two).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent i2 = new Intent(HomeActivity.this, CreationActivity.class);
                drawerLayout.closeDrawers();
                startActivity(i2);
                return true;
            }
        });

        binding.navView.getMenu().findItem(R.id.nav_item_three).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent i2 = new Intent(HomeActivity.this, ConnexionActivity.class);
                drawerLayout.closeDrawers();
                startActivity(i2);
                return true;
            }
        });

        this.initRecycler();
        this.remplirRecycler();
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

    private void remplirRecycler() {
        for (int i = 0 ; i < 250 ; i++) {
            Tâche p = new Tâche();
            //Calendar maintenant = Calendar.getInstance();
            p.nom = "Tâche numéro " + i;
            p.pourcentage = new Random().nextInt(11) * 10;
            p.dateDeCréation = "2021/02/03";
            p.dateDeFin = "2021/02/08";
            adapter.list.add(p);
        }
        adapter.notifyDataSetChanged();
    }

    private void initRecycler(){
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        adapter = new TâcheAdapter();
        recyclerView.setAdapter(adapter);

    }
}
