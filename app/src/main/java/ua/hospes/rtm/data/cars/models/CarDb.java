package ua.hospes.rtm.data.cars.models;

import android.content.ContentValues;

import ua.hospes.dbhelper.IDbModel;
import ua.hospes.rtm.core.db.tables.Cars;
import ua.hospes.rtm.domain.cars.models.CarQuality;

/**
 * @author Andrew Khloponin
 */
public class CarDb implements IDbModel {
    private final int id;
    private int number = -1;
    private CarQuality quality = CarQuality.NORMAL;
    private boolean broken = false;


    public CarDb() {
        this.id = -1;
    }

    public CarDb(int id) {
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
        return "CarDb{" +
                "id=" + id +
                ", number=" + number +
                ", quality='" + quality + '\'' +
                ", broken=" + broken +
                '}';
    }


    @Override
    public ContentValues toContentValues() {
        ContentValues cv = new ContentValues();

        cv.put(Cars.NUMBER.name(), number);
        cv.put(Cars.QUALITY.name(), quality.toString());
        cv.put(Cars.BROKEN.name(), broken ? 1 : 0);

        return cv;
    }
}