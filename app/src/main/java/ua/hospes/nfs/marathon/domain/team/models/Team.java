package ua.hospes.nfs.marathon.domain.team.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Andrew Khloponin
 */
public class Team implements Parcelable {
    private int id = -1;
    private String name;
    private List<String> drivers = new ArrayList<>();


    public Team(String name) {
        this.name = name;
    }

    public Team(int id, String name) {
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

    public List<String> getDrivers() {
        return drivers;
    }
    //endregion

    //region Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setDrivers(Collection<String> drivers) {
        this.drivers.clear();
        this.drivers.addAll(drivers);
    }
    //endregion


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Team team = (Team) o;

        return id == team.id;

    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "Team{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", drivers=" + drivers +
                '}';
    }


    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeStringList(this.drivers);
    }

    protected Team(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.drivers = in.createStringArrayList();
    }

    public static final Creator<Team> CREATOR = new Creator<Team>() {
        @Override
        public Team createFromParcel(Parcel source) {return new Team(source);}

        @Override
        public Team[] newArray(int size) {return new Team[size];}
    };
}