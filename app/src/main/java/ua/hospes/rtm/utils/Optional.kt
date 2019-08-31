package ua.hospes.rtm.utils

/**
 * @author Andrew Khloponin
 */
class Optional<T> private constructor() {
    private var t: T? = null

    val isPresent: Boolean
        get() = t != null


    fun get(): T? {
        return t
    }

    companion object {


        fun <T> empty(): Optional<T> {
            return Optional()
        }

        fun <T> of(value: T): Optional<T> {
            val o = Optional<T>()
            o.t = value
            return o
        }
    }
}