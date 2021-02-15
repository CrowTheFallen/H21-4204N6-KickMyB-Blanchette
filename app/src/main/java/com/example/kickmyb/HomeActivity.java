package com.example.kickmyb;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kickmyb.databinding.ActivityCreationBinding;
import com.example.kickmyb.databinding.ActivityHomeBinding;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;
    TâcheAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this,CreationActivity.class);
                startActivity(intent);
            }
        });

        this.initRecycler();
        this.remplirRecycler();
    }

    private void remplirRecycler() {
        for (int i = 0 ; i < 250 ; i++) {
            Tâche p = new Tâche();
            //Calendar maintenant = Calendar.getInstance();
            p.nom = "Tâche numéro " + i;
            p.pourcentage = new Random().nextInt(11) * 10;
            p.dateDeCréation = "2021/02/03";
            p.dateDeFin = "2021/02/08";
            adapter.list.add(p);
        }
        adapter.notifyDataSetChanged();
    }

    private void initRecycler(){
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        adapter = new TâcheAdapter();
        recyclerView.setAdapter(adapter);

    }
}
