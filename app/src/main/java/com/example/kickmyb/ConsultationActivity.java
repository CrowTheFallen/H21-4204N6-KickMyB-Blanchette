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

public class ConsultationActivity extends AppCompatActivity {
    private ActivityConsultationBinding binding;
    private ActionBarDrawerToggle toggle;
    private int progr = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConsultationBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setTitle("Consultation");

        binding.button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ConsultationActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        final String ContenueNom = getIntent().getStringExtra("NomTâche");
        binding.textView5.setText("Nom de la tâche : " + ContenueNom);

        final String ContenuePourcent = getIntent().getStringExtra("Pourcentage");
        progr = Integer.parseInt(ContenuePourcent.replace("%",""));
        //binding.textView6.setText(ContenuePourcent);

        final String ContenueTempsÉcoulé = getIntent().getStringExtra("TempsÉcoulé");
        binding.textView7.setText("Date de départ : "+ContenueTempsÉcoulé);

        final String ContenueDateLimite = getIntent().getStringExtra("DateLimite");
        binding.textView8.setText("Date limite : "+ContenueDateLimite);

        updateProgressBar();
        binding.buttonIncr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (progr <= 90) {
                    progr += 10;
                    updateProgressBar();
                }
            }
        });
        binding.buttonDecr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (progr >= 10) {
                    progr -= 10;
                    updateProgressBar();
                }
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
            }
        };

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        binding.navView.getMenu().findItem(R.id.nav_item_one).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent i2 = new Intent(ConsultationActivity.this, HomeActivity.class);
                drawerLayout.closeDrawers();
                startActivity(i2);
                return true;
            }
        });

        binding.navView.getMenu().findItem(R.id.nav_item_two).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent i2 = new Intent(ConsultationActivity.this, CreationActivity.class);
                drawerLayout.closeDrawers();
                startActivity(i2);
                return true;
            }
        });

        binding.navView.getMenu().findItem(R.id.nav_item_three).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent i2 = new Intent(ConsultationActivity.this, ConnexionActivity.class);
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

    private void updateProgressBar() {
        binding.progressBar.setProgress(progr);
        binding.textViewProgress.setText(""+progr+"%");
    }
}
