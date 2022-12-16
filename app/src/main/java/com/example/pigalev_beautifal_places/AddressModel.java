package com.example.pigalev_beautifal_places;

public class AddressModel
{
    private String country;

    public AddressModel(String country)
    {
        this.country = country;
    }

    public void setAddress(String country)
    {
        this.country = country;
    }

    public String getAddress()
    {
        return country;
    }
}
