package com.example.kickmyb.Activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.kickmyb.R;
import com.example.kickmyb.Singleton;
import com.example.kickmyb.databinding.ActivityCreationBinding;
import com.example.kickmyb.http.NoConnectivityException;
import com.example.kickmyb.http.RetrofitUtil;
import com.example.kickmyb.http.Service;
import com.example.kickmyb.transfer.CreationTâche;
import com.example.kickmyb.transfer.Utilisateur;
import com.google.android.material.navigation.NavigationView;

import org.joda.time.DateTime;
import org.kickmyb.transfer.AddTaskRequest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreationActivity extends AppCompatActivity {
    ProgressDialog progressD;
    private ActivityCreationBinding binding;
    private ActionBarDrawerToggle toggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreationBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setTitle(R.string.CreationActivity_title);
        Service service = RetrofitUtil.get(CreationActivity.this);

        final String[] année = new String[1];
        final String[] mois = new String[1];
        final String[] jour = new String[1];

        binding.calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView arg0, int year, int month,
                                            int date) {
                année[0] = ""+year;
                if(month < 10){
                    mois[0] = "0"+(month + 1);
                }
                else{
                    mois[0] = ""+(month + 1);
                }
                if(date < 10)
                    jour[0] = "0"+date;
                else
                    jour[0] = ""+date;

                Toast.makeText(CreationActivity.this,année[0] + "-"+mois[0]+"-"+jour[0],Toast.LENGTH_LONG).show();
            }

        });

        binding.button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar today = Calendar.getInstance();
                today.set(Calendar.HOUR_OF_DAY, 0);
                today.set(Calendar.MINUTE, 0);
                today.set(Calendar.SECOND, 0);

                String deadLine= année[0] + "-"+mois[0]+"-"+jour[0];

                Date dateString = null;
                try {
                    dateString = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH).parse(deadLine);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            if(!binding.editTextTextPersonName2.getText().toString().isEmpty() && dateString.after(today.getTime())){
                String message = getString(R.string.ProgressDialogWait);
                String messagecre = getString(R.string.ProgressDialogCreation);

                progressD = ProgressDialog.show(CreationActivity.this, message,
                        messagecre,true);

                String name = binding.editTextTextPersonName2.getText().toString();
                AddTaskRequest addTaskRequest = new AddTaskRequest();
                addTaskRequest.name = name;
                addTaskRequest.deadLine = dateString;
                Call<String> CreationdeTache =
                        service.CreationDeTache(addTaskRequest);
                //name, deadLine
                CreationdeTache.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        new CreationActivity.DialogTask<>().execute();

                        if(response.isSuccessful()){
                            Intent intent = new Intent(CreationActivity.this, HomeActivity.class);
                            startActivity(intent);
                        }
                        if (response.code() == 400) {
                            if(binding.editTextTextPersonName2.getText().length() > 175){
                                String message = getString(R.string.Creation400Length);
                                binding.editTextTextPersonName2.setError(message + binding.editTextTextPersonName2.getText().length());
                            }
                            else{
                                String message = getString(R.string.Creation400Exist);
                                Toast.makeText(CreationActivity.this,message,Toast.LENGTH_SHORT).show();
                            }

                         if(response.code() == 401 && !Singleton.getInstance().giveUserName().isEmpty()){
                             String message = getString(R.string.Creation403Expired);
                             Toast.makeText(CreationActivity.this,message,Toast.LENGTH_SHORT).show();
                         }
                         if(response.code() == 403){
                             String message = getString(R.string.Creation403NotConnected);
                             Toast.makeText(CreationActivity.this,message,Toast.LENGTH_SHORT).show();
                         }
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        new CreationActivity.DialogTask<>().execute();
                        Log.i("Erreur",t.toString());
                        if(t instanceof NoConnectivityException) {
                            String message = getString(R.string.NoInternet);
                            Toast.makeText(CreationActivity.this,message,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                }
                if(binding.editTextTextPersonName2.getText().toString().isEmpty()){
                    String message = getString(R.string.CreationEnterName);
                    binding.editTextTextPersonName2.setError(message);
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
                String messagediscon = getString(R.string.ProgressDialogDesc);
                String message = getString(R.string.ProgressDialogWait);
                progressD = ProgressDialog.show(CreationActivity.this, message,
                        messagediscon,true);
                Call<String> utilisateurCall = service.Déconection();
                utilisateurCall.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        new CreationActivity.DialogTask<>().execute();
                        if(response.isSuccessful()){
                            Singleton.getInstance().Exit();
                            String messageDeco = getString(R.string.DeconnectionMessage);
                            Toast.makeText(CreationActivity.this,messageDeco,Toast.LENGTH_SHORT).show();
                            Intent i2 = new Intent(CreationActivity.this, ConnexionActivity.class);
                            drawerLayout.closeDrawers();
                            startActivity(i2);
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        new CreationActivity.DialogTask<>().execute();
                        if(t instanceof NoConnectivityException) {
                            String message = getString(R.string.NoInternet);
                            Toast.makeText(CreationActivity.this,message,Toast.LENGTH_SHORT).show();
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
