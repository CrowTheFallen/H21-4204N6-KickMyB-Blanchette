package com.example.kickmyb;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kickmyb.databinding.ActivityConnectionBinding;
import com.example.kickmyb.databinding.ActivityConsultationBinding;

public class ConsultationActivity extends AppCompatActivity {
    private ActivityConsultationBinding binding;
    private int progr = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConsultationBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        final String ContenueNom = getIntent().getStringExtra("NomTâche");
        binding.textView5.setText(ContenueNom);

        final String ContenuePourcent = getIntent().getStringExtra("Pourcentage");
        progr = Integer.parseInt(ContenuePourcent.replace("%",""));
        binding.textView6.setText(ContenuePourcent);

        final String ContenueTempsÉcoulé = getIntent().getStringExtra("TempsÉcoulé");
        binding.textView7.setText(ContenueTempsÉcoulé);

        final String ContenueDateLimite = getIntent().getStringExtra("DateLimite");
        binding.textView8.setText(ContenueDateLimite);

        updateProgressBar();
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

    }
    private void updateProgressBar() {
        binding.progressBar.setProgress(progr);
        binding.textViewProgress.setText(""+progr+"%");
    }
}
