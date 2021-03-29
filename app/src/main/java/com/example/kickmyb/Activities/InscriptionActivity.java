package com.example.kickmyb.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kickmyb.Singleton;
import com.example.kickmyb.databinding.ActivityInscriptionBinding;
import com.example.kickmyb.http.RetrofitUtil;
import com.example.kickmyb.http.Service;
import com.example.kickmyb.transfer.Utilisateur;

import org.kickmyb.transfer.SigninRequest;
import org.kickmyb.transfer.SigninResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InscriptionActivity extends AppCompatActivity {
    private ActivityInscriptionBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInscriptionBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setTitle("Inscription");
        Service service = RetrofitUtil.get();


        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if(binding.editTextTextPassword.getText().toString() != null &&  binding.editTextTextPassword2.getText().toString() != null)
                if(!binding.editTextTextPersonName.getText().toString().isEmpty() &&
                        !binding.editTextTextPassword2.getText().toString().isEmpty() &&
                        !binding.editTextTextPassword.getText().toString().isEmpty() &&
                        binding.editTextTextPassword.getText().toString().equals(binding.editTextTextPassword2.getText().toString())){


                    SigninRequest signinRequest = new SigninRequest();
                    signinRequest.username = binding.editTextTextPersonName.getText().toString();
                    signinRequest.password = binding.editTextTextPassword.getText().toString();


                Call<SigninResponse> utilisateurCall = service
                        .Inscription(signinRequest);

                utilisateurCall.enqueue(new Callback<SigninResponse>() {
                    @Override
                    public void onResponse(Call<SigninResponse> call, Response<SigninResponse> response) {
                        Singleton.getInstance().getUserName(binding.editTextTextPersonName.getText().toString());
                        Intent intent = new Intent(InscriptionActivity.this, HomeActivity.class);
                        startActivity(intent);

                    }

                    @Override
                    public void onFailure(Call<SigninResponse> call, Throwable t) {

                    }

                });
                }

            }
        });
    }
}
