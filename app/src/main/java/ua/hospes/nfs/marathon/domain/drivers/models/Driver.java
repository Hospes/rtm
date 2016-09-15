package ua.hospes.nfs.marathon.domain.drivers.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Andrew Khloponin
 */
public class Driver implements Parcelable {
    private int id = -1;
    private String name;
    private int teamId = -1;
    private String teamName;


    public Driver(String name) {
        this.name = name;
    }

    public Driver(int id, String name) {
        this.id = id;
        this.name = name;
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

    public String getTeamName() {
        return teamName;
    }
    //endregion

    //region Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
    //endregion


    @Override
    public String toString() {
        return "Driver{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", teamId=" + teamId +
                ", teamName='" + teamName + '\'' +
                '}';
    }


    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeInt(this.teamId);
        dest.writeString(this.teamName);
    }

    protected Driver(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.teamId = in.readInt();
        this.teamName = in.readString();
    }

    public static final Creator<Driver> CREATOR = new Creator<Driver>() {
        @Override
        public Driver createFromParcel(Parcel source) {return new Driver(source);}

        @Override
        public Driver[] newArray(int size) {return new Driver[size];}
    };
}