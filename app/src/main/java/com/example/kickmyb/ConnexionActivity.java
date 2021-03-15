package com.example.kickmyb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.kickmyb.databinding.ActivityConnexionBinding;
import com.example.kickmyb.http.RetrofitUtil;
import com.example.kickmyb.http.Service;
import com.example.kickmyb.transfer.Utilisateur;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ConnexionActivity extends AppCompatActivity {
    private ActivityConnexionBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConnexionBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setTitle("Connexion");

        Service service = RetrofitUtil.get();

        binding.Connection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Call<Utilisateur> utilisateurCall = service.Connection(new Utilisateur(binding.editTextPersonName.getText().toString(),binding.editTextPassword.getText().toString()));

                utilisateurCall.enqueue(new Callback<Utilisateur>() {
                   @Override
                    public void onResponse(Call<Utilisateur> call, Response<Utilisateur> response) {
                        if(response.isSuccessful()){
                            Intent intent = new Intent(ConnexionActivity.this,HomeActivity.class);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call<Utilisateur> call, Throwable t) {

                    }
                });

            }
        });

        binding.Inscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ConnexionActivity.this,InscriptionActivity.class);
                startActivity(intent);
            }
        });
    }
}