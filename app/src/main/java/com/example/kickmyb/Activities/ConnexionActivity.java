package com.example.kickmyb.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.kickmyb.R;
import com.example.kickmyb.Singleton;
import com.example.kickmyb.databinding.ActivityConnexionBinding;
import com.example.kickmyb.http.NoConnectivityException;
import com.example.kickmyb.http.RetrofitUtil;
import com.example.kickmyb.http.Service;
import com.example.kickmyb.transfer.Utilisateur;

import org.kickmyb.transfer.SigninRequest;
import org.kickmyb.transfer.SigninResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConnexionActivity extends AppCompatActivity {
    private ActivityConnexionBinding binding;
    ProgressDialog progressD;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConnexionBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setTitle(R.string.ConnexionActivity_title);

        Service service = RetrofitUtil.get(ConnexionActivity.this);


        binding.Connection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!binding.editTextPersonName.getText().toString().isEmpty() &&
                        !binding.editTextPassword.getText().toString().isEmpty()){
                    String message = getString(R.string.ProgressDialogWait);
                    String messageco = getString(R.string.ProgressDialogConnect);

                    progressD = ProgressDialog.show(ConnexionActivity.this, message,
                            messageco,true);

                    SigninRequest signinRequest = new SigninRequest();
                    signinRequest.username = binding.editTextPersonName.getText().toString();
                    signinRequest.password = binding.editTextPassword.getText().toString();
                Call<SigninResponse> utilisateurCall =
                        service.Connection(signinRequest);

                utilisateurCall.enqueue(new Callback<SigninResponse>() {
                   @Override
                    public void onResponse(Call<SigninResponse> call, Response<SigninResponse> response) {
                       new DialogTask<>().execute();
                        if(response.isSuccessful()){
                            Singleton.getInstance().getUserName(binding.editTextPersonName.getText().toString());
                            Intent intent = new Intent(ConnexionActivity.this, HomeActivity.class);
                            startActivity(intent);
                        }
                       if (response.code() == 400) {
                           String message = getString(R.string.Connexion400Invalid);
                           Toast.makeText(ConnexionActivity.this,message,Toast.LENGTH_LONG).show();
                       }

                    }

                    @Override
                    public void onFailure(Call<SigninResponse> call, Throwable t) {
                        new DialogTask<>().execute();
                        if(t instanceof NoConnectivityException) {
                            String message = getString(R.string.NoInternet);
                            Toast.makeText(ConnexionActivity.this,message,Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                }
                if(binding.editTextPersonName.getText().toString().isEmpty()){
                    String message = getString(R.string.EnterName);
                    binding.editTextPersonName.setError(message);
                }
                if(binding.editTextPassword.getText().toString().isEmpty()){
                    String message = getString(R.string.EnterPass);
                    binding.editTextPassword.setError(message);
                }
            }
        });

        binding.Inscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ConnexionActivity.this, InscriptionActivity.class);
                startActivity(intent);
            }
        });
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