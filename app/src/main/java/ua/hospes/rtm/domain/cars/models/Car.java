package ua.hospes.rtm.domain.cars.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Andrew Khloponin
 */
public class Car implements Parcelable {
    private final int id;
    private int number = -1;
    private CarQuality quality = CarQuality.NORMAL;
    private boolean broken = false;


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

    public CarQuality getQuality() {
        return quality;
    }

    public boolean isBroken() {
        return broken;
    }
    //endregion

    //region Setters
    public void setNumber(int number) {
        this.number = number;
    }

    public void setQuality(CarQuality quality) {
        this.quality = quality;
    }

    public void setBroken(boolean broken) {
        this.broken = broken;
    }
    //endregion


    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", number=" + number +
                ", quality=" + quality +
                ", broken=" + broken +
                '}';
    }


    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.number);
        dest.writeInt(this.quality == null ? -1 : this.quality.ordinal());
        dest.writeByte(this.broken ? (byte) 1 : (byte) 0);
    }

    protected Car(Parcel in) {
        this.id = in.readInt();
        this.number = in.readInt();
        int tmpQuality = in.readInt();
        this.quality = tmpQuality == -1 ? CarQuality.NORMAL : CarQuality.values()[tmpQuality];
        this.broken = in.readByte() != 0;
    }

    public static final Creator<Car> CREATOR = new Creator<Car>() {
        @Override
        public Car createFromParcel(Parcel source) {return new Car(source);}

        @Override
        public Car[] newArray(int size) {return new Car[size];}
    };
}