package com.example.kickmyb.http;

import com.example.kickmyb.transfer.Repo;
import com.example.kickmyb.transfer.Utilisateur;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface Service {

    @GET("users/{utilisateur}/repos")
    Call<String> listReposString(@Path("utilisateur") String utilisateur);

    @GET("users/{utilisateur}/repos")
    Call<List<Repo>> listRepos(@Path("utilisateur") String utilisateur);

    @GET("users/{utilisateur}")
    Call<String> utilisateurString(@Path("utilisateur") String utilisateur);

    @GET("users/{utilisateur}")
    Call<Utilisateur> utilisateur(@Path("utilisateur") String utilisateur);

    @POST("api/id/signin")
    Call<Utilisateur> Connection(@Body Utilisateur body);

    @POST("api/id/signup")
    Call<Utilisateur> Inscription(@Body Utilisateur body);

    @POST("api/id/signout")
    Call<String> Déconection();
}
