package ua.hospes.nfs.marathon.domain.race.models;

/**
 * @author Andrew Khloponin
 */
public enum PitStopAssign {
    MANUAL,
    PREVIOUS,
    NEXT;


    public static PitStopAssign fromString(String string) {
        for (PitStopAssign pitStop : values())
            if (pitStop.name().equalsIgnoreCase(string)) return pitStop;
        return MANUAL;
    }
}