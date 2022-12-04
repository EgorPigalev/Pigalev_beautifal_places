package com.example.pigalev_beautifal_places;

public class UserModel
{
    private String login;
    private String password;
    private String image;
    private int id_role;

    public UserModel(String login, String password, String image, int id_role) {
        this.login = login;
        this.password = password;
        this.image = image;
        this.id_role = id_role;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public void setImage(String image) {
        this.image = image;
    }

    public void setId_role(int id_role) {
        this.id_role = id_role;
    }


    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getImage() {
        return image;
    }

    public int getId_role() {
        return id_role;
    }

}
