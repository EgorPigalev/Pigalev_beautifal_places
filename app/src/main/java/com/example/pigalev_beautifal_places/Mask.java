package com.example.pigalev_beautifal_places;

import android.os.Parcel;
import android.os.Parcelable;

public class Mask implements Parcelable{

    private int ID;
    private String Name;

    protected Mask(Parcel in) {
        ID = in.readInt();
        Name = in.readString();
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
        this.ID = ID;
    }

    public void setName(String name) {
        Name = name;
    }


    public int getID() {
        return ID;
    }

    public String getName() {
        return Name;
    }


    public Mask(int ID, String Name) {
        this.ID = ID;
        this.Name = Name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ID);
        dest.writeString(Name);
    }
}
