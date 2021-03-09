package com.example.kickmyb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.kickmyb.databinding.ActivityConnexionBinding;

public class ConnexionActivity extends AppCompatActivity {
    private ActivityConnexionBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConnexionBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setTitle("Connexion");

        binding.Connection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ConnexionActivity.this,HomeActivity.class);
                startActivity(intent);
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