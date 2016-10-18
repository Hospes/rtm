package ua.hospes.nfs.marathon.domain.race.models;

import ua.hospes.nfs.marathon.domain.sessions.models.Session;

/**
 * @author Andrew Khloponin
 */
public class DriverDetails {
    private int id = -1;
    private String name;
    private long prevDuration = 0L;
    private Session session;

    public DriverDetails() {}


    //region Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getPrevDuration() {
        return prevDuration;
    }

    public Session getSession() {
        return session;
    }
    //endregion

    //region Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrevDuration(long prevDuration) {
        this.prevDuration = prevDuration;
    }

    public void setSession(Session session) {
        this.session = session;
    }
    //endregion
}