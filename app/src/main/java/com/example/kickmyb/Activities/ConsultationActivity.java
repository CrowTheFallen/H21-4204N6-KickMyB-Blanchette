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

import com.example.kickmyb.R;
import com.example.kickmyb.Singleton;
import com.example.kickmyb.databinding.ActivityConsultationBinding;
import com.example.kickmyb.http.NoConnectivityException;
import com.example.kickmyb.http.RetrofitUtil;
import com.example.kickmyb.http.Service;
import com.google.android.material.navigation.NavigationView;

import org.kickmyb.transfer.TaskDetailResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConsultationActivity extends AppCompatActivity {
    ProgressDialog progressD;
    private ActivityConsultationBinding binding;
    private ActionBarDrawerToggle toggle;
    Service service = RetrofitUtil.get(ConsultationActivity.this);
    private int progr = 0;
    private Long id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConsultationBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setTitle(R.string.ConsultationActivity_title);

        final Long idtâche = getIntent().getLongExtra("id",2);

        String message = getString(R.string.ProgressDialogWait);
        String messagefetch = getString(R.string.ProgressDialogFetchInformation);

        progressD = ProgressDialog.show(ConsultationActivity.this, message,
                messagefetch,true);
        Call<TaskDetailResponse> consultation =
                service.Consultation(idtâche);
        consultation.enqueue(new Callback<TaskDetailResponse>() {
           @Override
           public void onResponse(Call<TaskDetailResponse> call, Response<TaskDetailResponse> response) {
               new ConsultationActivity.DialogTask<>().execute();
              if (response.isSuccessful()) {
                  final String OLD_FORMAT = "EEE MMM dd HH:mm:ss Z yyyy";
                  final String NEW_FORMAT = "yyyy/MM/dd";
                  String oldDateString = String.valueOf(response.body().deadLine);
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

              id = response.body().id;
              String TaskName = getString(R.string.ConsultationActivity_TaskName);
              binding.textView5.setText(TaskName + " " + response.body().name);
              progr = response.body().percentageDone;
              String TimeSince = getString(R.string.ConsultationActivity_TimeSinceCreation);
              binding.textView7.setText(TimeSince + " "+response.body().percentageTimeSpent);
              String Limit = getString(R.string.ConsultationActivity_Limit);
              binding.textView8.setText(Limit + " " +newDateString);
              updateProgressBar();
              }
              if(response.code() == 401 && !Singleton.getInstance().giveUserName().isEmpty()){
                  String message = getString(R.string.Creation403Expired);
                  Toast.makeText(ConsultationActivity.this,message,Toast.LENGTH_SHORT).show();
              }
              if(response.code() == 403){
                  String message = getString(R.string.Creation403NotConnected);
                  Toast.makeText(ConsultationActivity.this,message,Toast.LENGTH_SHORT).show();
              }
           }

         @Override
         public void onFailure(Call<TaskDetailResponse> call, Throwable t) {
             new ConsultationActivity.DialogTask<>().execute();
             Log.i("Erreur", t.toString());
             if(t instanceof NoConnectivityException) {
                 String message = getString(R.string.NoInternet);
                 Toast.makeText(ConsultationActivity.this,message,Toast.LENGTH_SHORT).show();
             }
         }

        });



        binding.button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = getString(R.string.ProgressDialogWait);
                String messageProgress = getString(R.string.ProgressDialogProgress);

                progressD = ProgressDialog.show(ConsultationActivity.this, message,
                        messageProgress,true);

                Call<String> CreationdeTache =
                        service.NouveauPourcent(id,progr);
                CreationdeTache.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        new ConsultationActivity.DialogTask<>().execute();
                        if(response.isSuccessful()){
                            Intent intent = new Intent(ConsultationActivity.this, HomeActivity.class);
                            startActivity(intent);
                        }
                        if(response.code() == 403){
                            String message = getString(R.string.Creation403NotConnected);
                            Toast.makeText(ConsultationActivity.this,message,Toast.LENGTH_SHORT).show();
                        }
                        if(response.code() == 404){
                            String message = getString(R.string.Consultation404);
                            Toast.makeText(ConsultationActivity.this,message,Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        new ConsultationActivity.DialogTask<>().execute();
                        if(t instanceof NoConnectivityException) {
                            String message = getString(R.string.NoInternet);
                            Toast.makeText(ConsultationActivity.this,message,Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });



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
                String messagediscon = getString(R.string.ProgressDialogDesc);
                String message = getString(R.string.ProgressDialogWait);
                progressD = ProgressDialog.show(ConsultationActivity.this, message,
                        messagediscon,true);
                //Service service = RetrofitUtil.get();
                Call<String> utilisateurCall = service.Déconection();
                utilisateurCall.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        new ConsultationActivity.DialogTask<>().execute();
                        if(response.isSuccessful()){
                            Singleton.getInstance().Exit();
                            String messageDeco = getString(R.string.DeconnectionMessage);
                            Toast.makeText(ConsultationActivity.this,messageDeco,Toast.LENGTH_SHORT).show();
                            Intent i2 = new Intent(ConsultationActivity.this, ConnexionActivity.class);
                            drawerLayout.closeDrawers();
                            startActivity(i2);
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        new ConsultationActivity.DialogTask<>().execute();
                        if(t instanceof NoConnectivityException) {
                            String message = getString(R.string.NoInternet);
                            Toast.makeText(ConsultationActivity.this,message,Toast.LENGTH_SHORT).show();
                        }
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
