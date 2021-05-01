package com.example.kickmyb.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.kickmyb.R;
import com.example.kickmyb.Singleton;
import com.example.kickmyb.databinding.ActivityConnexionBinding;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConnexionBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setTitle(R.string.ConnexionActivity_title);

        Service service = RetrofitUtil.get();





        binding.Connection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!binding.editTextPersonName.getText().toString().isEmpty() &&
                        !binding.editTextPassword.getText().toString().isEmpty()){
                    SigninRequest signinRequest = new SigninRequest();
                    signinRequest.username = binding.editTextPersonName.getText().toString();
                    signinRequest.password = binding.editTextPassword.getText().toString();
                Call<SigninResponse> utilisateurCall =
                        service.Connection(signinRequest);

                utilisateurCall.enqueue(new Callback<SigninResponse>() {
                   @Override
                    public void onResponse(Call<SigninResponse> call, Response<SigninResponse> response) {
                        if(response.isSuccessful()){
                            Singleton.getInstance().getUserName(binding.editTextPersonName.getText().toString());
                            Intent intent = new Intent(ConnexionActivity.this, HomeActivity.class);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call<SigninResponse> call, Throwable t) {

                    }
                });
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
}