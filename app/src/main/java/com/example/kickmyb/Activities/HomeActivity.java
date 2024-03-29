package com.example.kickmyb.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kickmyb.R;
import com.example.kickmyb.Singleton;
import com.example.kickmyb.http.NoConnectivityException;
import com.example.kickmyb.transfer.Tâche;
import com.example.kickmyb.TâcheAdapter;
import com.example.kickmyb.databinding.ActivityHomeBinding;
import com.example.kickmyb.http.RetrofitUtil;
import com.example.kickmyb.http.Service;
import com.google.android.material.navigation.NavigationView;

import org.kickmyb.transfer.HomeItemResponse;
import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    ProgressDialog progressD;
    private ActivityHomeBinding binding;
    private ActionBarDrawerToggle toggle;
    TâcheAdapter adapter;
    Service service = RetrofitUtil.get(HomeActivity.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setTitle(R.string.HomeActivity_title);



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
                String message = getString(R.string.ProgressDialogWait);
                String messagediscon = getString(R.string.ProgressDialogDesc);

                progressD = ProgressDialog.show(HomeActivity.this, message,
                        messagediscon,true);

                Call<String> utilisateurCall = service.Déconection();
                utilisateurCall.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        new HomeActivity.DialogTask<>().execute();
                        if(response.isSuccessful()){
                            Singleton.getInstance().Exit();
                            String messageDeco = getString(R.string.DeconnectionMessage);
                            Toast.makeText(HomeActivity.this,messageDeco,Toast.LENGTH_SHORT).show();
                            Intent i2 = new Intent(HomeActivity.this, ConnexionActivity.class);
                            drawerLayout.closeDrawers();
                            startActivity(i2);
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        new HomeActivity.DialogTask<>().execute();
                        if(t instanceof NoConnectivityException) {
                            String message = getString(R.string.NoInternet);
                            Toast.makeText(HomeActivity.this,message,Toast.LENGTH_SHORT).show();
                        }
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



    private void remplirRecycler() {
        String message = getString(R.string.ProgressDialogWait);
        String messagelist = getString(R.string.ProgressDialogFetchList);

        progressD = ProgressDialog.show(HomeActivity.this, message,
                messagelist,true);

            Call<List<HomeItemResponse>> CreationdeTache =
                    service.ConsultationDeTache();
            CreationdeTache.enqueue(new Callback<List<HomeItemResponse>>() {
                @Override
                public void onResponse(Call<List<HomeItemResponse>> call, Response<List<HomeItemResponse>> response) {
                    new HomeActivity.DialogTask<>().execute();
                    if(response.isSuccessful()){
                        List<HomeItemResponse> resultat = response.body();
                        for (int i = 0; i < resultat.size();i++) {
                            Tâche p = new Tâche();

                            final String OLD_FORMAT = "EEE MMM dd HH:mm:ss Z yyyy";
                            final String NEW_FORMAT = "yyyy/MM/dd";
                            String oldDateString = String.valueOf(resultat.get(i).deadline);
                            String newDateString;

                            SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT, Locale.ENGLISH);
                            Date d = null;
                            try {
                                d = sdf.parse(oldDateString);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            sdf.applyPattern(NEW_FORMAT);
                            newDateString = sdf.format(d);


                            p.id = resultat.get(i).id;
                            String TaskName = getString(R.string.ConsultationActivity_TaskName);
                            p.nom =  TaskName + " " + resultat.get(i).name;
                            p.pourcentage = resultat.get(i).percentageDone;
                            String TimeSince = getString(R.string.ConsultationActivity_TimeSinceCreation);
                            p.dateDeCréation = TimeSince + " " + resultat.get(i).percentageTimeSpent;
                            String Limit = getString(R.string.ConsultationActivity_Limit);
                            p.dateDeFin = Limit + " " + newDateString;
                            adapter.list.add(p);
                        }
                        adapter.notifyDataSetChanged();
                    }
                    if(response.code() == 401 && !Singleton.getInstance().giveUserName().isEmpty()){
                        String message = getString(R.string.Home403Expiration);
                        Toast.makeText(HomeActivity.this,message,Toast.LENGTH_SHORT).show();
                    }
                    if(response.code() == 403){
                        String message = getString(R.string.Home403NotConnected);
                        Toast.makeText(HomeActivity.this,message,Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<HomeItemResponse>> call, Throwable t) {
                    new HomeActivity.DialogTask<>().execute();
                    Log.i("Erreur",t.toString());
                    if(t instanceof NoConnectivityException) {
                        String message = getString(R.string.NoInternet);
                        Toast.makeText(HomeActivity.this,message,Toast.LENGTH_SHORT).show();
                    }
                }
            });



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

    class DialogTask<A,B,C> extends AsyncTask<A,B,C> {

        @Override
        protected void onPostExecute(C c) {
            progressD.dismiss();
            super.onPostExecute(c);
        }

        @Override
        protected C doInBackground(A... params) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
