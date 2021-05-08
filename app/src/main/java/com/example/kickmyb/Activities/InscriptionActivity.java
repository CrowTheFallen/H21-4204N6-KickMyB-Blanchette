package com.example.kickmyb.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kickmyb.R;
import com.example.kickmyb.Singleton;
import com.example.kickmyb.databinding.ActivityInscriptionBinding;
import com.example.kickmyb.http.NoConnectivityException;
import com.example.kickmyb.http.RetrofitUtil;
import com.example.kickmyb.http.Service;
import com.example.kickmyb.transfer.Utilisateur;

import org.kickmyb.transfer.SigninRequest;
import org.kickmyb.transfer.SigninResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InscriptionActivity extends AppCompatActivity {
    ProgressDialog progressD;
    private ActivityInscriptionBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInscriptionBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setTitle(R.string.Activity_inscription_inscriptionTv);
        Service service = RetrofitUtil.get(InscriptionActivity.this);



        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!binding.editTextTextPersonName.getText().toString().isEmpty() &&
                        !binding.editTextTextPassword2.getText().toString().isEmpty() &&
                        !binding.editTextTextPassword.getText().toString().isEmpty() &&
                        binding.editTextTextPassword.getText().toString().equals(binding.editTextTextPassword2.getText().toString())){

                    String message = getString(R.string.ProgressDialogWait);
                    String messageIns = getString(R.string.ProgressDialogInsc);

                    progressD = ProgressDialog.show(InscriptionActivity.this, message,
                            messageIns,true);



                    SigninRequest signinRequest = new SigninRequest();
                    signinRequest.username = binding.editTextTextPersonName.getText().toString();
                    signinRequest.password = binding.editTextTextPassword.getText().toString();


                Call<SigninResponse> utilisateurCall = service
                        .Inscription(signinRequest);

                utilisateurCall.enqueue(new Callback<SigninResponse>() {
                    @Override
                    public void onResponse(Call<SigninResponse> call, Response<SigninResponse> response) {
                        new DialogTask<>().execute();
                        if(response.isSuccessful()){
                            Singleton.getInstance().getUserName(binding.editTextTextPersonName.getText().toString());
                            Intent intent = new Intent(InscriptionActivity.this, HomeActivity.class);
                            startActivity(intent);
                        }
                        if (response.code() == 400) {
                            if(binding.editTextTextPersonName.getText().length() > 255){
                                String message = getString(R.string.Inscription400Length);
                                binding.editTextTextPersonName.setError(message + binding.editTextTextPersonName.getText().length());
                            }
                            else{
                                String message = getString(R.string.Inscription400Exist);
                                Toast.makeText(InscriptionActivity.this,message,Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SigninResponse> call, Throwable t) {
                        new DialogTask<>().execute();
                        if(t instanceof NoConnectivityException) {
                            String message = getString(R.string.NoInternet);
                            Toast.makeText(InscriptionActivity.this,message,Toast.LENGTH_SHORT).show();
                        }
                    }

                });
                }

                    if(binding.editTextTextPersonName.getText().toString().isEmpty()){
                        String message = getString(R.string.EnterName);
                        binding.editTextTextPersonName.setError(message);
                    }

                    if(binding.editTextTextPassword.getText().toString().isEmpty()){
                        String message = getString(R.string.EnterPass);
                        binding.editTextTextPassword.setError(message);
                    }

                    if(!binding.editTextTextPassword.getText().toString().equals(binding.editTextTextPassword2.getText().toString())){
                        String message = getString(R.string.EnterSamePass);
                        binding.editTextTextPassword2.setError(message);
                    }

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
