package com.example.pigalev_beautifal_places;

public class BeautifulPlacesModel
{
    private String name;
    private String description;
    private int id_user;
    private int id_address;
    private int id_type_locality;
    private float latitude;
    private float longitude;
    private String image;
    private boolean bit;

    public BeautifulPlacesModel(String name, String description, int id_user, int id_address, int id_type_locality, float latitude, float longitude, String image, boolean bit) {
        this.name = name;
        this.description = description;
        this.id_user = id_user;
        this.id_address = id_address;
        this.id_type_locality = id_type_locality;
        this.latitude = latitude;
        this.longitude = longitude;
        this.image = image;
        this.bit = bit;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public void setId_address(int id_address) {
        this.id_address = id_address;
    }

    public void setId_type_locality(int id_type_locality) {
        this.id_type_locality = id_type_locality;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setBit(boolean bit) {
        this.bit = bit;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getId_user() {
        return id_user;
    }

    public int getId_address() {
        return id_address;
    }

    public int getId_type_locality() {
        return id_type_locality;
    }


    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public String getImage() {
        return image;
    }

    public boolean getBit() {
        return bit;
    }

}
