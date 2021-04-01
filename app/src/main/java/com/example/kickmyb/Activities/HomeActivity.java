package com.example.kickmyb.Activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kickmyb.R;
import com.example.kickmyb.Singleton;
import com.example.kickmyb.transfer.Tâche;
import com.example.kickmyb.TâcheAdapter;
import com.example.kickmyb.databinding.ActivityHomeBinding;
import com.example.kickmyb.http.RetrofitUtil;
import com.example.kickmyb.http.Service;
import com.google.android.material.navigation.NavigationView;

import org.kickmyb.transfer.HomeItemResponse;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;
    private ActionBarDrawerToggle toggle;
    TâcheAdapter adapter;
    Service service = RetrofitUtil.get();
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

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView =  navigationView.getHeaderView(0);
        TextView nav_user = (TextView) hView.findViewById(R.id.textUser);
        nav_user.setText(Singleton.getInstance().giveUserName());

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
                //Service service = RetrofitUtil.get();
                Call<String> utilisateurCall = service.Déconection();
                utilisateurCall.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if(response.isSuccessful()){
                            Intent i2 = new Intent(HomeActivity.this, ConnexionActivity.class);
                            drawerLayout.closeDrawers();
                            startActivity(i2);
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });

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

    public String ActivityDifferentitaion(String responseTime) {

        SimpleDateFormat formatter = new SimpleDateFormat("dd MM yyyy");
        ParsePosition pos = new ParsePosition(0);
        long then = formatter.parse(responseTime, pos).getTime();
        long now = new Date().getTime();

        long seconds = (now - then) / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        String friendly = null;
        long num = 0;
        if (days > 0) {
            num = days;
            friendly = days + " day";
        }
        else if (hours > 0) {
            num = hours;
            friendly = hours + " hour";
        }
        else if (minutes > 0) {
            num = minutes;
            friendly = minutes + " minute";
        }
        else {
            num = seconds;
            friendly = seconds + " second";
        }
        if (num > 1) {
            friendly += "s";
        }
       return ("Temps écoulé : "+ friendly + " ago");

    }



    private void remplirRecycler() {
        //for (int i = 0 ; i < 250 ; i++) {

            Call<List<HomeItemResponse>> CreationdeTache =
                    service.ConsultationDeTache();
            CreationdeTache.enqueue(new Callback<List<HomeItemResponse>>() {
                @Override
                public void onResponse(Call<List<HomeItemResponse>> call, Response<List<HomeItemResponse>> response) {
                    if(response.isSuccessful()){
                        List<HomeItemResponse> resultat = response.body();
                        for (int i = 0; i < resultat.size();i++) {
                            Tâche p = new Tâche();

                            final String OLD_FORMAT = "EEE MMM dd HH:mm:ss Z yyyy";
                            final String NEW_FORMAT = "yyyy/MM/dd";
                            String oldDateString = String.valueOf(resultat.get(i).deadline);
                            String newDateString;

                            SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);
                            Date d = null;
                            try {
                                d = sdf.parse(oldDateString);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            sdf.applyPattern(NEW_FORMAT);
                            newDateString = sdf.format(d);


                            p.id = resultat.get(i).id;
                            p.nom = "Nom de la tâche : " + resultat.get(i).name;
                            p.pourcentage = resultat.get(i).percentageDone;
                            p.dateDeCréation = "Moments depuis la création : " + resultat.get(i).percentageTimeSpent;
                            p.dateDeFin = "Date limite : " + newDateString;
                            adapter.list.add(p);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<List<HomeItemResponse>> call, Throwable t) {
                    Log.i("Erreur",t.toString());
                }
            });




            //Tâche p = new Tâche();
            //Calendar maintenant = Calendar.getInstance();
            //p.nom = "Tâche numéro " + i;
            //p.pourcentage = new Random().nextInt(11) * 10;
            //p.dateDeCréation = ActivityDifferentitaion("22 03 2021");
                    //"Temps écoulé : " + "2021/02/03";
            //p.dateDeFin = "Date limite : " + "2021/02/08";
            //adapter.list.add(p);
        //}

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
