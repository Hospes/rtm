package ua.hospes.nfs.marathon.domain.team.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Andrew Khloponin
 */
public class Team implements Parcelable {
    private int id = -1;
    private String name;


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
    //endregion

    //region Setters
    public void setName(String name) {
        this.name = name;
    }
    //endregion


    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
    }

    protected Team(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
    }

    public static final Parcelable.Creator<Team> CREATOR = new Parcelable.Creator<Team>() {
        @Override
        public Team createFromParcel(Parcel source) {return new Team(source);}

        @Override
        public Team[] newArray(int size) {return new Team[size];}
    };
}