package ua.hospes.nfs.marathon.data.cars.models;

import android.content.ContentValues;

import ua.hospes.nfs.marathon.core.db.ModelBaseInterface;
import ua.hospes.nfs.marathon.core.db.tables.Cars;

/**
 * @author Andrew Khloponin
 */
public class CarDb implements ModelBaseInterface {
    private final int id;
    private int number = 0;
    private int rating = 0;

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
    public ContentValues toContentValues() {
        ContentValues cv = new ContentValues();

        cv.put(Cars.NUMBER, number);
        cv.put(Cars.RATING, rating);

        return cv;
    }
}