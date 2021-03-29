package com.example.kickmyb.transfer;

import com.google.gson.annotations.SerializedName;

public class CreationTâche {
    @SerializedName("name")
    final String name;
    @SerializedName("deadLine")
    final String deadLine;

    public CreationTâche(String name
            ,String deadLine){
        this.name = name;
        this.deadLine = deadLine;
    }
}
