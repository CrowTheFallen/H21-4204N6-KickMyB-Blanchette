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

import com.example.kickmyb.R;
import com.example.kickmyb.Singleton;
import com.example.kickmyb.databinding.ActivityConsultationBinding;
import com.example.kickmyb.http.RetrofitUtil;
import com.example.kickmyb.http.Service;
import com.google.android.material.navigation.NavigationView;

import org.kickmyb.transfer.TaskDetailResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConsultationActivity extends AppCompatActivity {
    private ActivityConsultationBinding binding;
    private ActionBarDrawerToggle toggle;
    Service service = RetrofitUtil.get();
    private int progr = 0;
    private Long id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConsultationBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setTitle("Consultation");


        Call<TaskDetailResponse> consultation =
                service.Consultation("2");
        consultation.enqueue(new Callback<TaskDetailResponse>() {
           @Override
           public void onResponse(Call<TaskDetailResponse> call, Response<TaskDetailResponse> response) {
              if (response.isSuccessful()) {
                  final String OLD_FORMAT = "EEE MMM dd HH:mm:ss Z yyyy";
                  final String NEW_FORMAT = "yyyy/MM/dd";
                  String oldDateString = String.valueOf(response.body().deadLine);
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

              id = response.body().id;
              binding.textView5.setText("Nom de la tâche : " + response.body().name);
              progr = response.body().percentageDone;
              binding.textView7.setText(""+response.body().percentageTimeSpent);
              binding.textView8.setText(""+newDateString);
              updateProgressBar();
              }
           }

         @Override
         public void onFailure(Call<TaskDetailResponse> call, Throwable t) {
             Log.i("Erreur", t.toString());
         }

        });



        binding.button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ConsultationActivity.this, HomeActivity.class);
                startActivity(intent);


            }
        });

        //final String ContenueNom = getIntent().getStringExtra("NomTâche");
        //binding.textView5.setText("Nom de la tâche : " + ContenueNom);

        //final String ContenuePourcent = getIntent().getStringExtra("Pourcentage");
        //progr = Integer.parseInt(ContenuePourcent.replace("%",""));

        //final String ContenueTempsÉcoulé = getIntent().getStringExtra("TempsÉcoulé");
        //binding.textView7.setText(""+ContenueTempsÉcoulé);

        //final String ContenueDateLimite = getIntent().getStringExtra("DateLimite");
        //binding.textView8.setText(""+ContenueDateLimite);


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

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView =  navigationView.getHeaderView(0);
        TextView nav_user = (TextView) hView.findViewById(R.id.textUser);
        nav_user.setText(Singleton.getInstance().giveUserName());

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
                //Service service = RetrofitUtil.get();
                Call<String> utilisateurCall = service.Déconection();
                utilisateurCall.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if(response.isSuccessful()){
                            Intent i2 = new Intent(ConsultationActivity.this, ConnexionActivity.class);
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
