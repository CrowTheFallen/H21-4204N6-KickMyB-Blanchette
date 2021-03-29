package com.example.kickmyb;

public class Singleton {

    private static Singleton _instance;
    private static String nomUtilisateur;
    private Singleton()
    {

    }

    public static synchronized Singleton getInstance()
    {
        if (_instance == null)
        {
            _instance = new Singleton();
        }
        return _instance;
    }

    public void getUserName(String nom)
    {
       nomUtilisateur = nom;
    }

    public String giveUserName()
    {
        return nomUtilisateur;
    }

    public String Exit()
    {
        nomUtilisateur = null;
        return nomUtilisateur;
    }

}


