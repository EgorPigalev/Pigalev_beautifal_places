package com.example.pigalev_beautifal_places;

public class GradesModel
{
    private int id_user;
    private int id_beautiful_place;

    public GradesModel(int id_davorites, int id_user, int id_beautiful_place)
    {
        this.id_user = id_user;
        this.id_beautiful_place = id_beautiful_place;
    }

    public void setId_beautiful_place(int id_beautiful_place)
    {
        this.id_beautiful_place = id_beautiful_place;
    }

    public void  setId_user(int id_user)
    {
        this.id_user = id_user;
    }


    public int getId_beautiful_place()
    {
        return  id_beautiful_place;
    }

    public int getId_user()
    {
        return id_user;
    }
}
