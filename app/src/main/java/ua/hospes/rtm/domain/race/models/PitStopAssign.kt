package ua.hospes.rtm.domain.race.models

/**
 * @author Andrew Khloponin
 */
enum class PitStopAssign {
    MANUAL,
    PREVIOUS,
    NEXT;


    companion object {


        fun fromString(string: String): PitStopAssign {
            for (pitStop in values())
                if (pitStop.name.equals(string, ignoreCase = true)) return pitStop
            return MANUAL
        }
    }
}