package ua.hospes.rtm.core.base.extensions

inline fun <T> T.fluentIf(condition: Boolean, block: T.() -> T): T {
    return if (condition) block() else this
}
