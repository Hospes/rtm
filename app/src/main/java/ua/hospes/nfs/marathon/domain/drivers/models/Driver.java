package ua.hospes.nfs.marathon.domain.drivers.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Andrew Khloponin
 */
public class Driver implements Parcelable {
    private int id = -1;
    private String name;
    private int teamId;


    public Driver(String name, int teamId) {
        this.name = name;
        this.teamId = teamId;
    }

    public Driver(int id, String name, int teamId) {
        this.id = id;
        this.name = name;
        this.teamId = teamId;
    }


    //region Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getTeamId() {
        return teamId;
    }
    //endregion

    //region Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }
    //endregion


    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeInt(this.teamId);
    }

    protected Driver(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.teamId = in.readInt();
    }

    public static final Parcelable.Creator<Driver> CREATOR = new Parcelable.Creator<Driver>() {
        @Override
        public Driver createFromParcel(Parcel source) {return new Driver(source);}

        @Override
        public Driver[] newArray(int size) {return new Driver[size];}
    };
}