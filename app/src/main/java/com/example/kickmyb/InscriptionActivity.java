package com.example.kickmyb;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kickmyb.databinding.ActivityHomeBinding;
import com.example.kickmyb.databinding.ActivityInscriptionBinding;
import com.example.kickmyb.http.RetrofitUtil;
import com.example.kickmyb.http.Service;
import com.example.kickmyb.transfer.Utilisateur;

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

                if(Integer.parseInt(binding.editTextTextPassword.getText().toString())  ==
                        Integer.parseInt(binding.editTextTextPassword2.getText().toString())){





                Call<Utilisateur> utilisateurCall = service
                        .Inscription(new Utilisateur(binding.editTextTextPersonName.getText().toString(),
                                binding.editTextTextPassword.getText().toString()));

                utilisateurCall.enqueue(new Callback<Utilisateur>() {
                    @Override
                    public void onResponse(Call<Utilisateur> call, Response<Utilisateur> response) {

                        Intent intent = new Intent(InscriptionActivity.this,HomeActivity.class);
                        startActivity(intent);

                    }

                    @Override
                    public void onFailure(Call<Utilisateur> call, Throwable t) {

                    }

                });
                }

            }
        });
    }
}
