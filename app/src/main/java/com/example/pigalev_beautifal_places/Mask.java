package com.example.pigalev_beautifal_places;

import android.os.Parcel;
import android.os.Parcelable;

public class Mask implements Parcelable{

    private int id_beautiful_place;
    private String name;

    protected Mask(Parcel in) {
        id_beautiful_place = in.readInt();
        name = in.readString();
    }

    public static final Creator<Mask> CREATOR = new Creator<Mask>() {
        @Override
        public Mask createFromParcel(Parcel in) {
            return new Mask(in);
        }

        @Override
        public Mask[] newArray(int size) {
            return new Mask[size];
        }
    };

    public void setID(int ID) {
        this.id_beautiful_place = ID;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getID() {
        return id_beautiful_place;
    }

    public String getName() {
        return name;
    }


    public Mask(int ID, String Name) {
        this.id_beautiful_place = ID;
        this.name = Name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id_beautiful_place);
        dest.writeString(name);
    }
}
