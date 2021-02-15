package com.example.kickmyb;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TâcheAdapter extends RecyclerView.Adapter<TâcheAdapter.MyViewHolder> {
    public List<Tâche> list;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView NomTâche;
        public TextView Pourcentage;
        public TextView TempsÉcoulé;
        public TextView DateLimite;
        public MyViewHolder(LinearLayout v) {
            super(v);
            NomTâche = v.findViewById(R.id.NomTâche);
            Pourcentage = v.findViewById(R.id.Pourcentage);
            TempsÉcoulé = v.findViewById(R.id.TempsÉcoulé);
            DateLimite = v.findViewById(R.id.DateLimite);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(),ConsultationActivity.class);
                    intent.putExtra("NomTâche", NomTâche.getText());
                    intent.putExtra("Pourcentage", Pourcentage.getText());
                    intent.putExtra("TempsÉcoulé", TempsÉcoulé.getText());
                    intent.putExtra("DateLimite", DateLimite.getText());
                    view.getContext().startActivity(intent);
                }
            });
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public TâcheAdapter() {
        list = new ArrayList<>();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public TâcheAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tache_item, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        Log.i("DEBOGAGE", "appel a onCreateViewHolder");
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Tâche tâcheCourante = list.get(position);
        holder.NomTâche.setText(tâcheCourante.nom);
        holder.Pourcentage.setText(""+tâcheCourante.pourcentage +"%");
        holder.TempsÉcoulé.setText(""+tâcheCourante.dateDeCréation);
        holder.DateLimite.setText(""+tâcheCourante.dateDeFin );
        Log.i("DEBOGAGE", "appel a onBindViewHolder " + position);
    }

    // renvoie la taille de la liste
    @Override
    public int getItemCount() {
        return list.size();
    }
}