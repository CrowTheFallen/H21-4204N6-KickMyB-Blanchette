package com.example.kickmyb.http;

import com.example.kickmyb.transfer.CreationTâche;
import com.example.kickmyb.transfer.Tâche;
import com.example.kickmyb.transfer.Utilisateur;

import org.kickmyb.transfer.AddTaskRequest;
import org.kickmyb.transfer.HomeItemResponse;
import org.kickmyb.transfer.SigninRequest;
import org.kickmyb.transfer.SigninResponse;
import org.kickmyb.transfer.TaskDetailResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface Service {


    @GET("api/progress/{id}/{valeur}")
    Call<String> NouveauPourcent(@Path("id") Long id, @Path("valeur") int valeur);

    @GET("api/detail/{id}")
    Call<TaskDetailResponse> Consultation(@Path("id") Long id);

    @GET("api/home")
    Call<List<HomeItemResponse>> ConsultationDeTache();

    @POST("api/add")
    Call<String> CreationDeTache(@Body AddTaskRequest body);

    @POST("api/id/signin")
    Call<SigninResponse> Connection(@Body SigninRequest body);

    @POST("api/id/signup")
    Call<SigninResponse> Inscription(@Body SigninRequest body);

    @POST("api/id/signout")
    Call<String> Déconection();
}
