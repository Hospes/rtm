package ua.hospes.rtm.domain.cars.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Andrew Khloponin
 */
public class Car implements Parcelable {
    private final int id;
    private int number = 0;
    private int rating = 0;


    public Car() {
        this.id = -1;
    }

    public Car(int id) {
        this.id = id;
    }


    //region Getters
    public int getId() {
        return id;
    }

    public int getNumber() {
        return number;
    }

    public int getRating() {
        return rating;
    }
    //endregion

    //region Setters
    public void setNumber(int number) {
        this.number = number;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
    //endregion


    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", number=" + number +
                ", rating=" + rating +
                '}';
    }


    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.number);
        dest.writeInt(this.rating);
    }

    protected Car(Parcel in) {
        this.id = in.readInt();
        this.number = in.readInt();
        this.rating = in.readInt();
    }

    public static final Parcelable.Creator<Car> CREATOR = new Parcelable.Creator<Car>() {
        @Override
        public Car createFromParcel(Parcel source) {return new Car(source);}

        @Override
        public Car[] newArray(int size) {return new Car[size];}
    };
}